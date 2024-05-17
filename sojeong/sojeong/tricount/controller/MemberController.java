package clonecoding.tricount.controller;

import clonecoding.tricount.entity.Join;
import clonecoding.tricount.entity.Login;
import clonecoding.tricount.entity.Member;
import clonecoding.tricount.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<Member> join(@RequestBody Join join){
        // 회원 가입 시도
        Member member = memberService.join(join);
        // 회원 가입 성공 시 회원 정보 반환
        if (member != null){
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Member> login(@RequestBody Login login, HttpServletResponse response){
        // 로그인 시도
        Member member = memberService.login(login);
        // 로그인 실패 시 오류 발생
        if (member == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // 로그인 성공 처리
        // 1. 세션
//        // 세션 있으면 세션 반환, 없으면 신규 세션 생성
//        HttpSession session = request.getSession();
//        // 세션에 로그인 회원 정보 보관
//        session.setAttribute("LoginMember", member);

        // 2. 쿠키
        Cookie idCookie = new Cookie("memberLoginId", String.valueOf(member.getLoginId()));
        response.addCookie(idCookie);
        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        // 1. 세션 삭제
//        HttpSession session = request.getSession(false);
//        if (session != null) {
//            session.invalidate();
//        }

        // 2. 쿠키 삭제
        Cookie idCookie = new Cookie("memberLoginId", null);
        idCookie.setMaxAge(0);
        response.addCookie(idCookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
