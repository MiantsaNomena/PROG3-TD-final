package com.example.fca.service;

import com.example.fca.entity.dto.CollectivityInformation;
import com.example.fca.entity.dto.CreateCollectivity;
import com.example.fca.entity.Collectivity;
import com.example.fca.entity.Member;
import com.example.fca.repository.CollectivityRepository;
import com.example.fca.repository.MemberRepository;
import com.example.fca.validator.CollectivityValidator;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final CollectivityValidator validator;

    public CollectivityService(CollectivityRepository cr, MemberRepository mr, CollectivityValidator v) {
        this.collectivityRepository = cr;
        this.memberRepository = mr;
        this.validator = v;
    }

    public List<Collectivity> createCollectivities(List<CreateCollectivity> dtos) throws Exception {
        List<Collectivity> results = new java.util.ArrayList<>();
        for (CreateCollectivity dto : dtos) {
            validator.validate(dto);
            List<Member> members = memberRepository.findByIds(dto.getMembers());
            Member president = memberRepository.findById(dto.getStructure().getPresident()).orElseThrow();
            Member vice = memberRepository.findById(dto.getStructure().getVicePresident()).orElseThrow();
            Member treasurer = memberRepository.findById(dto.getStructure().getTreasurer()).orElseThrow();
            Member secretary = memberRepository.findById(dto.getStructure().getSecretary()).orElseThrow();
            Collectivity c = new Collectivity();
            c.setLocation(dto.getLocation());
            c.setMembers(members);
            c.setPresident(president);
            c.setVicePresident(vice);
            c.setTreasurer(treasurer);
            c.setSecretary(secretary);
            c.setCreationDate(LocalDate.now());
            c.setFederationApproval(dto.isFederationApproval());
            results.add(collectivityRepository.save(c));
        }
        return results;
    }

    public Collectivity assignInformations(String id, CollectivityInformation info) throws Exception {
        collectivityRepository.assignUniqueIdentifiers(id, info.getName(), String.valueOf(info.getNumber()));
        return collectivityRepository.findById(id).orElseThrow();
    }
    public Optional<Collectivity> getCollectivityById(String id) throws SQLException {
        return collectivityRepository.findById(id);
    }
}