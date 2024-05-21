package hello.tricount.service;

import hello.tricount.model.Member;
import hello.tricount.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public Member signup(Member member) {
        return memberRepository.save(member);
    }
}
