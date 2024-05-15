package proj.tricount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import proj.tricount.domain.user.Member;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MemberService {

    private final JdbcTemplate jdbcTemplate;

    // 회원가입
    public void registerMember(Member member) {
        String sql = "INSERT INTO member (username, password, login_Id) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, member.getUsername(), member.getPassword(), member.getLoginId());
    }

    // 로그인
    public Member getMemberByLoginId(String loginId) {
        String sql = "SELECT * FROM member WHERE login_Id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{loginId}, (rs, rowNum) ->
                new Member(rs.getLong("id"), rs.getString("username"), rs.getString("password"), rs.getString("login_Id"))
        );
    }

    // 정산관련 유저 찾기
    public Optional<Member> getMemberById(Long id) {
        String sql = "SELECT * FROM member WHERE id = ?";
        Member member = jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) ->
                new Member(rs.getLong("id"), rs.getString("username"), rs.getString("password"), rs.getString("login_Id"))
        );
        return Optional.ofNullable(member);
    }

}