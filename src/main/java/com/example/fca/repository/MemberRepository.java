package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.Member;
import com.example.fca.entity.dto.CollectivityOverallStats;
import com.example.fca.entity.dto.MemberStatistics;
import com.example.fca.entity.enums.Gender;
import com.example.fca.entity.enums.MemberOccupation;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

@Repository
public class MemberRepository {
    private final Datasource datasource;

    public MemberRepository(Datasource datasource) {
        this.datasource = datasource;
    }

    public Optional<Member> findById(String id) throws SQLException {
        String sql = "SELECT * FROM member WHERE id = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return Optional.of(map(rs));
            return Optional.empty();
        }
    }

    public List<Member> findByIds(List<String> ids) throws SQLException {
        if (ids == null || ids.isEmpty()) return List.of();
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String sql = "SELECT * FROM member WHERE id IN (" + placeholders + ")";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < ids.size(); i++) stmt.setString(i+1, ids.get(i));
            ResultSet rs = stmt.executeQuery();
            List<Member> members = new ArrayList<>();
            while (rs.next()) members.add(map(rs));
            return members;
        }
    }

    public Member save(Member m) throws SQLException {
        String sql = "INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, occupation, collectivity_id, active, membership_date) VALUES (?, ?, ?, ?, ?::gender_enum, ?, ?, ?, ?, ?::member_occupation_enum, ?, ?, ?)";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String id = UUID.randomUUID().toString();
            stmt.setString(1, id);
            stmt.setString(2, m.getFirstName());
            stmt.setString(3, m.getLastName());
            stmt.setDate(4, Date.valueOf(m.getBirthDate()));
            stmt.setString(5, m.getGender().name());
            stmt.setString(6, m.getAddress());
            stmt.setString(7, m.getProfession());
            stmt.setString(8, m.getPhoneNumber());
            stmt.setString(9, m.getEmail());
            stmt.setString(10, m.getOccupation().name());
            stmt.setString(11, m.getCollectivityId());
            stmt.setBoolean(12, m.isActive());
            stmt.setDate(13, Date.valueOf(m.getMembershipDate()));
            stmt.executeUpdate();
            m.setId(id);
            return m;
        }
    }

    private Member map(ResultSet rs) throws SQLException {
        Member m = new Member();
        m.setId(rs.getString("id"));
        m.setFirstName(rs.getString("first_name"));
        m.setLastName(rs.getString("last_name"));
        m.setBirthDate(rs.getDate("birth_date").toLocalDate());
        m.setGender(Gender.valueOf(rs.getString("gender")));
        m.setAddress(rs.getString("address"));
        m.setProfession(rs.getString("profession"));
        m.setPhoneNumber(rs.getString("phone_number"));
        m.setEmail(rs.getString("email"));
        m.setOccupation(MemberOccupation.valueOf(rs.getString("occupation")));
        m.setCollectivityId(rs.getString("collectivity_id"));
        m.setActive(rs.getBoolean("active"));
        m.setMembershipDate(rs.getDate("membership_date").toLocalDate());
        return m;
    }
    public List<Member> findByCollectivityId(String collectivityId, Boolean actif) throws SQLException {
        String sql = "SELECT * FROM member WHERE collectivity_id = ?";
        if (actif != null) sql += " AND active = ?";
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectivityId);
            if (actif != null) stmt.setBoolean(2, actif);
            ResultSet rs = stmt.executeQuery();
            List<Member> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    public List<MemberStatistics> getMemberStatistics(String collectivityId, LocalDate from, LocalDate to) throws SQLException {
        String sql = """
        SELECT 
            m.id,
            m.first_name,
            m.last_name,
            m.email,
            m.occupation,
            COALESCE(SUM(mp.amount), 0) as earned_amount,
            (SELECT COALESCE(SUM(mf.amount), 0)
             FROM membership_fee mf
             WHERE mf.collectivity_id = ? AND mf.status = 'ACTIVE' AND mf.eligible_from <= ?) as total_due
        FROM member m
        LEFT JOIN member_payment mp ON mp.member_id = m.id AND mp.creation_date BETWEEN ? AND ?
        WHERE m.collectivity_id = ? AND m.active = true
        GROUP BY m.id
        """;
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectivityId);
            stmt.setDate(2, Date.valueOf(to));
            stmt.setDate(3, Date.valueOf(from));
            stmt.setDate(4, Date.valueOf(to));
            stmt.setString(5, collectivityId);
            ResultSet rs = stmt.executeQuery();
            List<MemberStatistics> list = new ArrayList<>();
            while (rs.next()) {
                MemberStatistics ms = new MemberStatistics();
                ms.setMemberId(rs.getString("id"));
                ms.setFirstName(rs.getString("first_name"));
                ms.setLastName(rs.getString("last_name"));
                ms.setEmail(rs.getString("email"));
                ms.setOccupation(rs.getString("occupation"));
                ms.setEarnedAmount(rs.getDouble("earned_amount"));
                double totalDue = rs.getDouble("total_due");
                ms.setUnpaidAmount(Math.max(0, totalDue - ms.getEarnedAmount()));
                list.add(ms);
            }
            return list;
        }
    }

    public List<CollectivityOverallStats> getOverallStatistics(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
        WITH active_members AS (
            SELECT id, collectivity_id FROM member WHERE active = true
        ),
        member_payments AS (
            SELECT member_id, SUM(amount) as paid
            FROM member_payment
            WHERE creation_date BETWEEN ? AND ?
            GROUP BY member_id
        ),
        collectivity_total_due AS (
            SELECT collectivity_id, SUM(amount) as total_due
            FROM membership_fee
            WHERE status = 'ACTIVE' AND eligible_from <= ?
            GROUP BY collectivity_id
        ),
        new_members AS (
            SELECT collectivity_id, COUNT(*) as nb_new
            FROM member
            WHERE membership_date BETWEEN ? AND ? AND active = true
            GROUP BY collectivity_id
        ),
        member_status AS (
            SELECT 
                am.collectivity_id,
                COALESCE(mp.paid, 0) as paid,
                COALESCE(ctd.total_due, 0) as due
            FROM active_members am
            LEFT JOIN member_payments mp ON mp.member_id = am.id
            LEFT JOIN collectivity_total_due ctd ON ctd.collectivity_id = am.collectivity_id
        ),
        collectivity_stats AS (
            SELECT 
                collectivity_id,
                COUNT(*) as total_members,
                SUM(CASE WHEN paid >= due THEN 1 ELSE 0 END) as up_to_date
            FROM member_status
            GROUP BY collectivity_id
        )
        SELECT 
            c.id,
            c.unique_name,
            c.unique_number,
            COALESCE(nm.nb_new, 0) as new_members,
            CASE WHEN cs.total_members = 0 THEN 0 
                 ELSE 100.0 * cs.up_to_date / cs.total_members 
            END as percentage_up_to_date
        FROM collectivity c
        LEFT JOIN collectivity_stats cs ON cs.collectivity_id = c.id
        LEFT JOIN new_members nm ON nm.collectivity_id = c.id
        """;
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));
            stmt.setDate(3, Date.valueOf(to));
            stmt.setDate(4, Date.valueOf(from));
            stmt.setDate(5, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            List<CollectivityOverallStats> list = new ArrayList<>();
            while (rs.next()) {
                CollectivityOverallStats stat = new CollectivityOverallStats();
                stat.setCollectivityId(rs.getString("id"));
                stat.setCollectivityName(rs.getString("unique_name"));
                stat.setCollectivityNumber(rs.getString("unique_number"));
                stat.setNewMembersNumber(rs.getInt("new_members"));
                stat.setOverallMemberCurrentDuePercentage(rs.getDouble("percentage_up_to_date"));
                list.add(stat);
            }
            return list;
        }
    }

    public Map<String, Double> getAssiduityByMember(String collectivityId, LocalDate from, LocalDate to) throws SQLException {
        String sql = """
        SELECT 
            a.member_id,
            ROUND(COUNT(CASE WHEN a.status = 'ATTENDED' THEN 1 END) * 100.0 / NULLIF(COUNT(*), 0), 2) AS percentage
        FROM attendance a
        JOIN activity act ON act.id = a.activity_id
        WHERE act.collectivity_id = ? 
          AND act.executive_date BETWEEN ? AND ?
        GROUP BY a.member_id
        """;
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, collectivityId);
            stmt.setDate(2, Date.valueOf(from));
            stmt.setDate(3, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            Map<String, Double> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("member_id"), rs.getDouble("percentage"));
            }
            return map;
        }
    }

    public Map<String, Double> getOverallAssiduityByCollectivity(LocalDate from, LocalDate to) throws SQLException {
        String sql = """
        SELECT 
            act.collectivity_id,
            ROUND(SUM(CASE WHEN a.status = 'ATTENDED' THEN 1 ELSE 0 END) * 100.0 / NULLIF(COUNT(*), 0), 2) AS global_percentage
        FROM attendance a
        JOIN activity act ON act.id = a.activity_id
        WHERE act.executive_date BETWEEN ? AND ?
        GROUP BY act.collectivity_id
        """;
        try (Connection conn = datasource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(from));
            stmt.setDate(2, Date.valueOf(to));
            ResultSet rs = stmt.executeQuery();
            Map<String, Double> map = new HashMap<>();
            while (rs.next()) {
                map.put(rs.getString("collectivity_id"), rs.getDouble("global_percentage"));
            }
            return map;
        }
    }
}