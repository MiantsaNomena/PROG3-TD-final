package com.example.fca.service;

import com.example.fca.entity.MembershipFee;
import com.example.fca.entity.enums.ActivityStatus;
import com.example.fca.repository.CollectivityRepository;
import com.example.fca.repository.MembershipFeeRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class MembershipFeeService {
    private final MembershipFeeRepository feeRepository;
    private final CollectivityRepository collectivityRepository;

    public MembershipFeeService(MembershipFeeRepository feeRepository, CollectivityRepository collectivityRepository) {
        this.feeRepository = feeRepository;
        this.collectivityRepository = collectivityRepository;
    }

    public List<MembershipFee> getFees(String collectivityId) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        return feeRepository.findByCollectivityId(collectivityId);
    }

    public List<MembershipFee> createFees(String collectivityId, List<MembershipFee> fees) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        for (MembershipFee fee : fees) {
            if (fee.getAmount() < 0) throw new IllegalArgumentException("Amount must be >= 0");
            fee.setStatus(ActivityStatus.ACTIVE);
        }
        return feeRepository.saveAll(collectivityId, fees);
    }
}