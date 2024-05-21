package hello.tricount.repository;

import hello.tricount.model.ExpenseResult;
import hello.tricount.model.Member;
import hello.tricount.model.Settlement;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
@RequiredArgsConstructor
public class SettlementRepository {
    private final JdbcTemplate jdbcTemplate;

    public Settlement create(String name) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("settlement").usingGeneratedKeyColumns("id");

        Map<String, Object> parmas = new HashMap<>();
        parmas.put("name", name);

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parmas));

        Settlement settlement = new Settlement();
        settlement.setId(key.longValue());
        settlement.setName(name);

        return settlement;
    }

    public void addParticipantToSettlement(Long settlementId, Long memberId) {
        jdbcTemplate.update("INSERT INTO settlement_participant (settlement_id, member_id) VALUES (?, ?)",
                settlementId, memberId);
    }

    public Optional<Settlement> findById(Long id) {
        List<Settlement> result = jdbcTemplate.query("select * from settlement "
                + "join settlement_participant on settlement.id = settlement_participant.settlement_id "
                + "join member on settlement_participant.member_id = member.id "
                + "where settlement.id = ?", settlementParticipantsRowMapper(), id);
        return result.stream().findAny();
    }

    private RowMapper<Settlement> settlementParticipantsRowMapper() {
        return ((rs, rowNum) -> {
            Settlement settlement = new Settlement();
            settlement.setId(rs.getLong("settlement.id"));
            settlement.setName(rs.getString("settlement.name"));

            List<Member> participants = new ArrayList<>();
            do {
                Member participant = new Member(
                        rs.getLong("member.id"),
                        rs.getString("member.login_id"),
                        rs.getString("member.name"),
                        rs.getString("member.password")
                );
                participants.add(participant);
            } while(rs.next());

            settlement.setParticipants(participants);
            return settlement;
        });
    }

    public List<ExpenseResult> findExpensesWithMemberBySettlementId(Long settlementId) {
        String sql = "SELECT * " +
                "FROM settlement_participant " +
                "JOIN member ON settlement_participant.member_id = member.id " +
                "LEFT JOIN expense ON settlement_participant.member_id = expense.payer_member_id " +
                "AND settlement_participant.settlement_id = expense.settlement_id " +
                "WHERE settlement_participant.settlement_id = ?";
        return jdbcTemplate.query(sql, expenseResultRowMapper(), settlementId);
    }

    private RowMapper<ExpenseResult> expenseResultRowMapper() {
        return (rs, rowNum) -> {
            ExpenseResult expenseResult = new ExpenseResult();
            expenseResult.setSettlementId(rs.getLong("settlement_participant.settlement_id"));
            BigDecimal amt = rs.getBigDecimal("expense.amount");
            expenseResult.setAmount(amt != null ? amt : BigDecimal.ZERO);

            Member member = new Member();
            if(rs.getLong("member.id") != 0) {
                member.setId(rs.getLong("member.id"));
                member.setLoginId(rs.getString("member.login_id"));
                member.setPassword(rs.getString("member.password"));
                member.setName(rs.getString("member.name"));

                expenseResult.setPayerMember(member);
            }

            return expenseResult;
        };
    }
}
