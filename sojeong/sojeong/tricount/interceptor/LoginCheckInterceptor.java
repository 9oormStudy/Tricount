package clonecoding.tricount.interceptor;

import clonecoding.tricount.MemberContext;
import clonecoding.tricount.entity.Member;
import clonecoding.tricount.repository.MemberRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

    private final MemberRepository memberRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 세션
//        // 로그인이 필요한 페이지에 접근할 때 세션 확인
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("LoginMember") == null){
//            throw new RuntimeException();
//        }
//        // 세션이 존재하고 로그인 된 상태인 경우 요청을 처리
//        Member loginMember = (Member) session.getAttribute("LoginMember");
//        MemberContext.setMember(loginMember);

        // 2. 쿠키
        Cookie[] cookies = request.getCookies();
        if(cookies == null) {
            throw new AccessException("로그인이 필요합니다.");
        }

        Map<String, Cookie> cookieMap = Arrays.stream(cookies).collect(Collectors.toMap(Cookie::getName, Function.identity()));
        Cookie loginCookie = cookieMap.get("memberLoginId");
        if(loginCookie == null) {
            throw new AccessException("로그인이 필요합니다.");
        }

        try {
            Member loginMember = memberRepository.findByLoginId(loginCookie.getValue());
            MemberContext.setMember(loginMember);
        } catch (Exception e) {
            throw new AccessException("회원정보를 찾을 수 없습니다.");
        }

        return true;
    }
}
