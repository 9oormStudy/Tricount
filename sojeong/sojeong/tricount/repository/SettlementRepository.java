package clonecoding.tricount.repository;

import clonecoding.tricount.MemberContext;
import clonecoding.tricount.entity.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class SettlementRepository {
    private final JdbcTemplate jdbcTemplate;

    public void create(String title){
        String sql = "INSERT INTO settlement (title) VALUES (?)";
        jdbcTemplate.update(sql, title);
    }

    public void addParticipant(Long id){
        String checkDuplicateSql = "SELECT COUNT(*) FROM settlement_participants WHERE settlement_id = ? AND member_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkDuplicateSql, Integer.class, id, MemberContext.getMember().getId());
        if (count == null){
            throw new AccessException("없는 정산 모임입니다.");
        }
        if (count > 0){
            throw new AccessException("이미 정산에 포함되어 있는 회원입니다.");
        }
        String sql = "INSERT INTO settlement_participants (settlement_id, member_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, id, MemberContext.getMember().getId());
    }

    public Settlement findParticipantsBySettlementId(Long id){
        String checkExistSql = "SELECT COUNT(*) FROM settlement_participants WHERE settlement_id = ? AND member_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkExistSql, Integer.class, id, MemberContext.getMember().getId());
        if (count == null || count == 0){
            throw new AccessException("정산에 포함 되어있는 않은 회원입니다.");
        }
        String sql = "SELECT settlement_id, title, member_id, login_id, password, name " +
                "FROM settlement s " +
                "LEFT JOIN settlement_participants sp ON s.id = sp.settlement_id " +
                "LEFT JOIN member m ON sp.member_id = m.id " +
                "WHERE s.id = ?";

        return jdbcTemplate.queryForObject(sql, participantsRowMapper(), id);
    }

    private RowMapper<Settlement> participantsRowMapper() {
        return (rs, rowNum) -> {
            Settlement settlement = new Settlement();
            settlement.setId(rs.getLong("settlement_id"));
            settlement.setTitle(rs.getString("title"));

            List<Member> participants = new ArrayList<>();
            do {
                Member participant = new Member();
                participant.setId(rs.getLong("member_id"));
                participant.setLoginId(rs.getString("login_id"));
                participant.setPassword(rs.getString("password"));
                participant.setName(rs.getString("name"));
                participants.add(participant);
            } while(rs.next());
            settlement.setParticipants(participants);

            return settlement;
        };
    }

    public Long addExpense(ExpenseResult expense, Long id){
        String checkExistSql = "SELECT COUNT(*) FROM settlement_participants WHERE settlement_id = ? AND member_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkExistSql, Integer.class, id, expense.getMemberId());
        if (count == null || count == 0){
            throw new AccessException("정산에 포함 되어있는 않은 회원입니다.");
        }

        String sql = "INSERT INTO expense (title, settlement_id, member_id, amount, date) VALUES (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
            ps.setString(1, expense.getTitle());
            ps.setLong(2, id);
            ps.setLong(3, expense.getMemberId());
            ps.setBigDecimal(4, expense.getAmount());
            ps.setTimestamp(5, Timestamp.valueOf(expense.getDate()));
            return ps;
        }, keyHolder);
        return keyHolder.getKey().longValue();
    }

    public List<Expense> findExpenseListBySettlementId(Long id){
        String checkExistSql = "SELECT COUNT(*) FROM settlement_participants WHERE settlement_id = ? AND member_id = ?";
        Integer count = jdbcTemplate.queryForObject(checkExistSql, Integer.class, id, MemberContext.getMember().getId());
        if (count == null || count == 0){
            throw new AccessException("정산에 포함 되어있는 않은 회원입니다.");
        }

        String sql = "SELECT * FROM expense WHERE settlement_id = ?";
        return jdbcTemplate.queryForObject(sql, expenseListRowMapper(), id);
    }

    private RowMapper<List<Expense>> expenseListRowMapper() {
        return (rs, rowNum) -> {
            List<Expense> list = new ArrayList<>();
            do {
                Expense expense = new Expense();
                expense.setTitle(rs.getString("title"));
                expense.setMemberId(rs.getLong("member_id"));
                expense.setAmount(rs.getBigDecimal("amount"));
                list.add(expense);
            } while(rs.next());
            return list;
        };
    }

}
