package proj.tricount.controller;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import proj.tricount.domain.user.LoginForm;
import proj.tricount.domain.user.Member;
import proj.tricount.service.MemberService;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/login")
    public String login(@Validated @ModelAttribute LoginForm loginForm, HttpSession httpSession) {
        Optional<Member> optionalMember = Optional.ofNullable(memberService.getMemberByLoginId(loginForm.getLoginId()));
        if (optionalMember.isPresent() && optionalMember.get().getPassword().equals(loginForm.getPassword())) {
            httpSession.setAttribute("member", optionalMember.get());
            return "redirect:/";
        } else {
            log.info("로그인 실패");
            return "login/loginForm";
        }
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<Object> register(@ModelAttribute Member member) {
        try {
            memberService.registerMember(member);
            log.info("회원가입 성공");
            return ResponseEntity.ok(member);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
