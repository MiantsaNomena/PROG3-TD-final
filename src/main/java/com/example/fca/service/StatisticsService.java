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
import java.util.List;
import java.util.stream.Collectors;

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

        List<MemberStatistics> stats = memberRepository.getMemberStatistics(collectivityId, from, to);
        return stats.stream().map(ms -> {
            CollectivityLocalStatistics dto = new CollectivityLocalStatistics();
            MemberDescription desc = new MemberDescription();
            desc.setId(ms.getMemberId());
            desc.setFirstName(ms.getFirstName());
            desc.setLastName(ms.getLastName());
            desc.setEmail(ms.getEmail());
            desc.setOccupation(ms.getOccupation());
            dto.setMemberDescription(desc);
            dto.setEarnedAmount(ms.getEarnedAmount());
            dto.setUnpaidAmount(ms.getUnpaidAmount());
            return dto;
        }).collect(Collectors.toList());
    }

    public List<CollectivityOverallStats> getOverallStatistics(LocalDate from, LocalDate to) throws SQLException {
        return memberRepository.getOverallStatistics(from, to);
    }
}