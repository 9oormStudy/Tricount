package clonecoding.tricount.repository;

import clonecoding.tricount.entity.Join;
import clonecoding.tricount.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public Member save(Join join){
        // 회원 가입 처리
        String sql = "INSERT INTO member (login_id, password, name) VALUES (?, ?, ?)";
        int result = jdbcTemplate.update(sql, join.getLoginId(), join.getPassword(), join.getName());
        return findByLoginId(join.getLoginId());
    }

    public Member findByLoginId(String loginId){
        String sql = "SELECT * FROM member WHERE login_id = ?";
        try{
            return jdbcTemplate.queryForObject(sql, memberRowMapper(), loginId);
        } catch (EmptyResultDataAccessException e) {
            throw new RuntimeException("찾을 수 없는 회원입니다");
        }
    }

    private RowMapper<Member> memberRowMapper() {
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId((rs.getLong("id")));
            member.setLoginId(rs.getString("login_id"));
            member.setPassword(rs.getString("password"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
