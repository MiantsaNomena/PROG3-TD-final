package com.example.fca.service;

import com.example.fca.entity.Member;
import com.example.fca.entity.dto.CreateMember;
import com.example.fca.entity.dto.MemberResponse;
import com.example.fca.repository.MemberRepository;
import com.example.fca.validator.MemberValidator;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberValidator validator;

    public MemberService(MemberRepository memberRepository, MemberValidator validator) {
        this.memberRepository = memberRepository;
        this.validator = validator;
    }

    public List<MemberResponse> createMembers(List<CreateMember> dtos) throws Exception {
        List<MemberResponse> responses = new java.util.ArrayList<>();
        for (CreateMember dto : dtos) {
            validator.validate(dto);
            Member member = new Member();
            member.setFirstName(dto.getMemberInfo().getFirstName());
            member.setLastName(dto.getMemberInfo().getLastName());
            member.setBirthDate(dto.getMemberInfo().getBirthDate());
            member.setGender(dto.getMemberInfo().getGender());
            member.setAddress(dto.getMemberInfo().getAddress());
            member.setProfession(dto.getMemberInfo().getProfession());
            member.setPhoneNumber(dto.getMemberInfo().getPhoneNumber());
            member.setEmail(dto.getMemberInfo().getEmail());
            member.setOccupation(dto.getMemberInfo().getOccupation());
            member.setCollectivityId(dto.getCollectivityIdentifier());
            member.setActive(true);
            member.setMembershipDate(LocalDate.now());
            member = memberRepository.save(member);

            List<Member> referees = memberRepository.findByIds(dto.getReferees());
            MemberResponse resp = new MemberResponse();
            resp.setId(member.getId());
            resp.setFirstName(member.getFirstName());
            resp.setLastName(member.getLastName());
            resp.setBirthDate(member.getBirthDate());
            resp.setGender(member.getGender());
            resp.setAddress(member.getAddress());
            resp.setProfession(member.getProfession());
            resp.setPhoneNumber(member.getPhoneNumber());
            resp.setEmail(member.getEmail());
            resp.setOccupation(member.getOccupation());
            resp.setReferees(referees);
            responses.add(resp);
        }
        return responses;
    }
}