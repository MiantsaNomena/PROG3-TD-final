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
            throw new IllegalArgumentException("Frais d'adhésion (50k) et cotisations annuelles doivent être payés");
        if (dto.getReferees() == null || dto.getReferees().size() < 2)
            throw new IllegalArgumentException("Au moins deux parrains requis");
        List<Member> referees = memberRepository.findByIds(dto.getReferees());
        if (referees.size() != dto.getReferees().size())
            throw new IllegalArgumentException("Un ou plusieurs parrains n'existent pas");
        for (Member ref : referees) {
            if (ref.getOccupation() != MemberOccupation.SENIOR)
                throw new IllegalArgumentException("Le parrain " + ref.getId() + " n'est pas un membre confirmé");
        }
        String targetCollectivity = dto.getCollectivityIdentifier();
        if (targetCollectivity != null && !targetCollectivity.isBlank()) {
            long inside = referees.stream().filter(r -> targetCollectivity.equals(r.getCollectivityId())).count();
            long outside = referees.size() - inside;
            if (inside < outside)
                throw new IllegalArgumentException("Règle des parrains non respectée : il faut au moins autant de parrains internes qu'externes");
        }
    }
}