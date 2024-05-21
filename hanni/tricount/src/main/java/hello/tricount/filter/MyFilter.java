package hello.tricount.filter;

import hello.tricount.exception.ForbiddenAccessException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class MyFilter extends OncePerRequestFilter {
    // filter -> | ==> spring(interceptor -> controller)
    // filter: servlet filter
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 로그인하다 오류가 나는 상황을 가정
//        throw new ForbiddenAccessException("aaaa");
    }
}
