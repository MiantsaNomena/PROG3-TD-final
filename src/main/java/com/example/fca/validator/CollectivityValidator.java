package com.example.fca.validator;
// validator/CollectivityValidator.java

import com.example.fca.entity.Member;
import com.example.fca.entity.dto.CreateCollectivity;
import com.example.fca.entity.dto.*;
import com.example.fca.repository.MemberRepository;
import org.springframework.stereotype.Component;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class CollectivityValidator {
    private final MemberRepository memberRepository;

    public CollectivityValidator(MemberRepository memberRepository) { this.memberRepository = memberRepository; }

    public void validate(CreateCollectivity dto) throws SQLException {
        if (!dto.isFederationApproval())
            throw new IllegalArgumentException("L'autorisation de la fédération est requise");
        if (dto.getMembers() == null || dto.getMembers().size() < 10)
            throw new IllegalArgumentException("Au moins 10 membres requis");
        List<Member> members = memberRepository.findByIds(dto.getMembers());
        if (members.size() != dto.getMembers().size())
            throw new IllegalArgumentException("Certains membres n'existent pas");
        long count = members.stream().filter(m -> ChronoUnit.MONTHS.between(m.getMembershipDate(), LocalDate.now()) >= 6).count();
        if (count < 5)
            throw new IllegalArgumentException("Au moins 5 membres doivent avoir une ancienneté ≥ 6 mois");
        CreateCollectivityStructure struct = dto.getStructure();
        if (struct == null) throw new IllegalArgumentException("Structure requise");
        List<String> roleIds = List.of(struct.getPresident(), struct.getVicePresident(), struct.getTreasurer(), struct.getSecretary());
        if (roleIds.stream().anyMatch(id -> id == null || id.isBlank()))
            throw new IllegalArgumentException("Tous les postes spécifiques doivent être pourvus");
        List<Member> roleMembers = memberRepository.findByIds(roleIds);
        if (roleMembers.size() != roleIds.size())
            throw new IllegalArgumentException("Certains membres de la structure n'existent pas");
        List<String> memberIds = dto.getMembers();
        for (String roleId : roleIds) {
            if (!memberIds.contains(roleId))
                throw new IllegalArgumentException("Le membre " + roleId + " n'est pas dans la liste des membres fondateurs");
        }
    }
}