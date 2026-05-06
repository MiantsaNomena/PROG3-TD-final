package com.example.fca.service;


import com.example.fca.entity.dto.ActivityMemberAttendance;
import com.example.fca.entity.dto.CreateActivityMemberAttendance;
import com.example.fca.entity.dto.MemberDescription;
import com.example.fca.repository.ActivityRepository;
import com.example.fca.repository.AttendanceRepository;
import com.example.fca.repository.CollectivityRepository;
import com.example.fca.repository.MemberRepository;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AttendanceService {
    private final ActivityRepository activityRepository;
    private final AttendanceRepository attendanceRepository;
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public AttendanceService(ActivityRepository ar, AttendanceRepository atr,
                             CollectivityRepository cr, MemberRepository mr) {
        this.activityRepository = ar;
        this.attendanceRepository = atr;
        this.collectivityRepository = cr;
        this.memberRepository = mr;
    }

    public List<ActivityMemberAttendance> getAttendance(String collectivityId, String activityId) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        if (activityRepository.findById(activityId).isEmpty())
            throw new IllegalArgumentException("Activity not found");
        var attendances = attendanceRepository.findByActivityId(activityId);
        List<ActivityMemberAttendance> result = new ArrayList<>();
        for (var att : attendances) {
            var member = memberRepository.findById(att.getMemberId()).orElse(null);
            if (member != null) {
                ActivityMemberAttendance dto = new ActivityMemberAttendance();
                dto.setId(att.getId());
                MemberDescription desc = new MemberDescription();
                desc.setId(member.getId());
                desc.setFirstName(member.getFirstName());
                desc.setLastName(member.getLastName());
                desc.setEmail(member.getEmail());
                desc.setOccupation(member.getOccupation().name());
                dto.setMemberDescription(desc);
                dto.setAttendanceStatus(att.getStatus());
                result.add(dto);
            }
        }
        return result;
    }

    public List<ActivityMemberAttendance> setAttendance(String collectivityId, String activityId, List<CreateActivityMemberAttendance> dtos) throws SQLException {
        if (collectivityRepository.findById(collectivityId).isEmpty())
            throw new IllegalArgumentException("Collectivity not found");
        var activity = activityRepository.findById(activityId)
                .orElseThrow(() -> new IllegalArgumentException("Activity not found"));
        List<ActivityMemberAttendance> result = new ArrayList<>();
        for (CreateActivityMemberAttendance dto : dtos) {
            boolean updated = attendanceRepository.updateStatus(activityId, dto.getMemberIdentifier(), dto.getAttendanceStatus());
            if (!updated) {
                throw new IllegalArgumentException("Cannot update attendance for member " + dto.getMemberIdentifier() +
                        " (already confirmed or missing)");
            }
            var member = memberRepository.findById(dto.getMemberIdentifier()).orElseThrow(() -> new IllegalArgumentException("Member not found"));
            ActivityMemberAttendance response = new ActivityMemberAttendance();
            response.setId(null);
            MemberDescription desc = new MemberDescription();
            desc.setId(member.getId());
            desc.setFirstName(member.getFirstName());
            desc.setLastName(member.getLastName());
            desc.setEmail(member.getEmail());
            desc.setOccupation(member.getOccupation().name());
            response.setMemberDescription(desc);
            response.setAttendanceStatus(dto.getAttendanceStatus());
            result.add(response);
        }
        return result;
    }
}