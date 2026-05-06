package com.example.fca.service;

import com.example.fca.entity.dto.CollectivityLocalStatistics;
import com.example.fca.entity.dto.CollectivityOverallStats;
import com.example.fca.entity.dto.MemberDescription;
import com.example.fca.entity.dto.MemberStatistics;
import com.example.fca.repository.MemberRepository;
import com.example.fca.repository.CollectivityRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {

    private final MemberRepository memberRepository;
    private final CollectivityRepository collectivityRepository;

    public StatisticsService(MemberRepository memberRepository, CollectivityRepository collectivityRepository) {
        this.memberRepository = memberRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<CollectivityLocalStatistics> getLocalStatistics(String collectivityId, LocalDate from, LocalDate to) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");

        List<MemberStatistics> financialStats = memberRepository.getMemberStatistics(collectivityId, from, to);
        Map<String, Double> assiduityMap = memberRepository.getAssiduityByMember(collectivityId, from, to);

        List<CollectivityLocalStatistics> result = new ArrayList<>();
        for (MemberStatistics ms : financialStats) {
            MemberDescription desc = new MemberDescription();
            desc.setId(ms.getMemberId());
            desc.setFirstName(ms.getFirstName());
            desc.setLastName(ms.getLastName());
            desc.setEmail(ms.getEmail());
            desc.setOccupation(ms.getOccupation());

            CollectivityLocalStatistics dto = new CollectivityLocalStatistics();
            dto.setMemberDescription(desc);
            dto.setEarnedAmount(ms.getEarnedAmount());
            dto.setUnpaidAmount(ms.getUnpaidAmount());
            Double assiduity = assiduityMap.get(ms.getMemberId());
            dto.setAssiduityPercentage(assiduity != null ? assiduity : 0.0);
            result.add(dto);
        }
        return result;
    }

    public List<CollectivityOverallStats> getOverallStatistics(LocalDate from, LocalDate to) throws SQLException {
        List<CollectivityOverallStats> stats = memberRepository.getOverallStatistics(from, to);
        Map<String, Double> assiduityMap = memberRepository.getOverallAssiduityByCollectivity(from, to);

        for (CollectivityOverallStats stat : stats) {
            Double globalAssiduity = assiduityMap.get(stat.getCollectivityId());
            stat.setOverallMemberAssiduityPercentage(globalAssiduity != null ? globalAssiduity : 0.0);
        }
        return stats;
    }
}