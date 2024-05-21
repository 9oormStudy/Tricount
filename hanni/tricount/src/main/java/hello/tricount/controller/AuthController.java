package hello.tricount.controller;

import hello.tricount.TricountApiConst;
import hello.tricount.model.LoginRequest;
import hello.tricount.model.Member;
import hello.tricount.model.SignupRequest;
import hello.tricount.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    // @RequestBody -> 이름이나 비밀번호가 노출되면 안되기 때문
    @PostMapping("signup")
    public ResponseEntity<Object> signup( @RequestBody SignupRequest signupRequest ){
//        이렇게 할수도 있지만 Member에 @Builder를 생성해 아래 코드로 간단하게 변경 가능
//        Member member = new Member();
//        member.setLoginId(signupRequest.getName());
        Member member = Member.builder()
                .loginId(signupRequest.getUserId())
                .password(signupRequest.getPassword())
                .name(signupRequest.getName())
                .build();
        return new ResponseEntity<>(memberService.signup(member), HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<Member> login(
        @RequestBody LoginRequest loginRequest,
        HttpServletResponse response
    ){
        Member member = memberService.login(loginRequest.getLoginId(), loginRequest.getPassword());

        Cookie cookie = new Cookie(TricountApiConst.LOGIN_MEMBER_COOKIE, String.valueOf(member.getId()));
        response.addCookie(cookie);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<Member> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie(TricountApiConst.LOGIN_MEMBER_COOKIE, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
