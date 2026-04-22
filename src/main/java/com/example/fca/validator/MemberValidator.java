package com.example.fca.validator;

import com.example.fca.entity.Member;
import com.example.fca.entity.dto.CreateMember;
import com.example.fca.entity.enums.MemberOccupation;
import com.example.fca.repository.MemberRepository;
import org.springframework.stereotype.Component;
import java.sql.SQLException;
import java.util.List;

@Component
public class MemberValidator {
    private final MemberRepository memberRepository;

    public MemberValidator(MemberRepository memberRepository) { this.memberRepository = memberRepository; }

    public void validate(CreateMember dto) throws SQLException {
        if (!dto.isRegistrationFeePaid() || !dto.isMembershipDuesPaid())
            throw new IllegalArgumentException("Membership fee (50k) and annual dues must be paid");
        if (dto.getReferees() == null || dto.getReferees().size() < 2)
            throw new IllegalArgumentException("At least two sponsors are required");
        List<Member> referees = memberRepository.findByIds(dto.getReferees());
        if (referees.size() != dto.getReferees().size())
            throw new IllegalArgumentException("One or more sponsors do not exist");
        for (Member ref : referees) {
            if (ref.getOccupation() != MemberOccupation.SENIOR)
                throw new IllegalArgumentException("Sponsor " + ref.getId() + " is not a confirmed member");
        }
        String targetCollectivity = dto.getCollectivityIdentifier();
        if (targetCollectivity != null && !targetCollectivity.isBlank()) {
            long inside = referees.stream().filter(r -> targetCollectivity.equals(r.getCollectivityId())).count();
            long outside = referees.size() - inside;
            if (inside < outside)
                throw new IllegalArgumentException("Sponsorship rule not respected: at least as many internal sponsors as external ones are required");
        }
    }
}