package hello.tricount.interceptor;

import hello.tricount.MemberContext;
import hello.tricount.TricountApiConst;
import hello.tricount.exception.ForbiddenAccessException;
import hello.tricount.model.Member;
import hello.tricount.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {
    private final MemberService memberService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new ForbiddenAccessException("로그인이 필요합니다.");
        }

        Map<String, Cookie> cookieMap = Arrays.stream(cookies).collect(Collectors.toMap(Cookie::getName, Function.identity()));

        // 로그인할때 사용하는 쿠키가 있는지 확인
        Cookie loginCookie = cookieMap.get(TricountApiConst.LOGIN_MEMBER_COOKIE);
        if(loginCookie == null) {
            throw new ForbiddenAccessException("로그인이 필요합니다.");
        }

        try {
            Member member = memberService.findMemberById(Long.valueOf(loginCookie.getValue()));
            MemberContext.setMember(member);
        } catch (Exception e) {
            throw new ForbiddenAccessException("회원정보를 찾을 수 없습니다.");
        }

        return true;
    }
}
