package com.example.fca.service;


import com.example.fca.entity.dto.CollectivityActivity;
import com.example.fca.entity.dto.CreateCollectivityActivity;
import com.example.fca.entity.Activity;
import com.example.fca.repository.ActivityRepository;
import com.example.fca.repository.CollectivityRepository;
import com.example.fca.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public ActivityService(ActivityRepository ar, CollectivityRepository cr, MemberRepository mr) {
        this.activityRepository = ar;
        this.collectivityRepository = cr;
        this.memberRepository = mr;
    }

    public List<CollectivityActivity> getActivities(String collectivityId) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        return activityRepository.findByCollectivityId(collectivityId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<CollectivityActivity> createActivities(String collectivityId, List<CreateCollectivityActivity> dtos) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        List<CollectivityActivity> result = new java.util.ArrayList<>();
        for (CreateCollectivityActivity dto : dtos) {
            if (dto.getExecutiveDate() == null)
                throw new IllegalArgumentException("Executive date required (recurrence not supported)");
            Activity a = new Activity();
            a.setCollectivityId(collectivityId);
            a.setLabel(dto.getLabel());
            a.setActivityType(dto.getActivityType());
            a.setExecutiveDate(dto.getExecutiveDate());
            activityRepository.save(a);
            var members = memberRepository.findByCollectivityId(collectivityId, true);
            List<String> memberIds = members.stream().map(m -> m.getId()).collect(Collectors.toList());
            result.add(toDTO(a));
        }
        return result;
    }

    private CollectivityActivity toDTO(Activity a) {
        CollectivityActivity dto = new CollectivityActivity();
        dto.setId(a.getId());
        dto.setLabel(a.getLabel());
        dto.setActivityType(a.getActivityType());
        dto.setExecutiveDate(a.getExecutiveDate());
        return dto;
    }
}