package clonecoding.tricount.service;

import clonecoding.tricount.entity.Join;
import clonecoding.tricount.entity.Login;
import clonecoding.tricount.entity.Member;
import clonecoding.tricount.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    public Member join(Join join){
        return memberRepository.save(join);
    }

    public Member login(Login login){
        return memberRepository.findByLoginId(login.getLoginId());
    }

}
