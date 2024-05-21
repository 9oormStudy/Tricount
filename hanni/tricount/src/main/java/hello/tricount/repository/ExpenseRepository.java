package hello.tricount.repository;

import hello.tricount.model.Expense;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class ExpenseRepository {
    private final JdbcTemplate jdbcTemplate;

    public Expense save(Expense expense) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.withTableName("expense").usingGeneratedKeyColumns("id");

        Map<String, Object> parmas = new HashMap<>();
        parmas.put("name", expense.getName());
        parmas.put("settlement_id", expense.getSettlementId());
        parmas.put("payer_member_id", expense.getPayerMemberId());
        parmas.put("amount", expense.getAmount());
        parmas.put("expense_date_time", expense.getExpenseDateTime());

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parmas));
        expense.setId(key.longValue());

        return expense;
    }
}
