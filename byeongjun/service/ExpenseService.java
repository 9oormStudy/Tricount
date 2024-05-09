package proj.tricount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import proj.tricount.domain.settlement.Expense;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final JdbcTemplate jdbcTemplate;

    public void addExpense(Expense expense) {
        String sql = "INSERT INTO expense (settlement_Id, name, payer_Id, amount, date) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, expense.getSettlementId(), expense.getName(), expense.getPayerId(), expense.getAmount(), expense.getDate());
    }

    public List<Expense> getExpensesBySettlementId(Long settlementId) {
        String sql = "SELECT * FROM expense WHERE settlement_Id = ?";
        return jdbcTemplate.query(sql, new Object[]{settlementId}, (rs, rowNum) -> new Expense(
                rs.getLong("expense_Id"),
                rs.getLong("settlement_Id"),
                rs.getString("name"),
                rs.getLong("payer_Id"),
                rs.getBigDecimal("amount"),
                rs.getString("date")
        ));
    }

    public void deleteExpense(int expenseId) {
        String sql = "DELETE FROM expense WHERE expense_Id = ?";
        jdbcTemplate.update(sql, expenseId);
    }
}
