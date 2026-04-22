package com.example.fca.repository;

import com.example.fca.config.Datasource;
import com.example.fca.entity.Member;
import com.example.fca.entity.enums.Gender;
import com.example.fca.entity.enums.MemberOccupation;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.sql.Date;
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
        if (actif != null) sql += " AND actif = ?";
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
}