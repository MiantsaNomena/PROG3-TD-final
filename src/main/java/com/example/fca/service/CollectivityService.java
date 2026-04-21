package com.example.fca.service;

import com.example.fca.entity.Collectivity;
import com.example.fca.entity.Member;
import com.example.fca.entity.dto.*;
import com.example.fca.repository.CollectivityRepository;
import com.example.fca.repository.MemberRepository;
import com.example.fca.validator.CollectivityValidator;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;
    private final CollectivityValidator validator;

    public CollectivityService(CollectivityRepository collectivityRepository,
                               MemberRepository memberRepository,
                               CollectivityValidator validator) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    public List<CollectivityResponse> createCollectivities(List<CreateCollectivity> dtos) throws Exception {
        List<CollectivityResponse> responses = new java.util.ArrayList<>();
        for (CreateCollectivity dto : dtos) {
            validator.validate(dto);
            List<Member> members = memberRepository.findByIds(dto.getMembers());
            CreateCollectivityStructure structDto = dto.getStructure();
            Member president = memberRepository.findById(structDto.getPresident()).orElseThrow();
            Member vicePresident = memberRepository.findById(structDto.getVicePresident()).orElseThrow();
            Member treasurer = memberRepository.findById(structDto.getTreasurer()).orElseThrow();
            Member secretary = memberRepository.findById(structDto.getSecretary()).orElseThrow();

            Collectivity collectivity = new Collectivity();
            collectivity.setLocation(dto.getLocation());
            collectivity.setMembers(members);
            collectivity.setPresident(president);
            collectivity.setVicePresident(vicePresident);
            collectivity.setTreasurer(treasurer);
            collectivity.setSecretary(secretary);
            collectivity.setCreationDate(LocalDate.now());
            collectivity.setFederationApproval(dto.isFederationApproval());

            collectivity = collectivityRepository.save(collectivity);

            CollectivityResponse resp = new CollectivityResponse();
            resp.setId(collectivity.getId());
            resp.setLocation(collectivity.getLocation());
            CollectivityStructure structResp = new CollectivityStructure();
            structResp.setPresident(president);
            structResp.setVicePresident(vicePresident);
            structResp.setTreasurer(treasurer);
            structResp.setSecretary(secretary);
            resp.setStructure(structResp);
            resp.setMembers(members);
            responses.add(resp);
        }
        return responses;
    }
}