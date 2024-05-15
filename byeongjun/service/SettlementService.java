package proj.tricount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import proj.tricount.domain.settlement.Expense;
import proj.tricount.domain.settlement.Settlement;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SettlementService {

    private final JdbcTemplate jdbcTemplate;

    public void createSettlement(Settlement settlement) {
        String sql = "INSERT INTO settlement (name, created_Date) VALUES (?, ?)";
        jdbcTemplate.update(sql, settlement.getName(), settlement.getCreatedDate());
    }

    public void addParticipantToSettlement(Long settlementId, Long memberId) {
        String sql = "INSERT INTO settlement_Participant (settlement_Id, member_Id) VALUES (?, ?)";
        jdbcTemplate.update(sql, settlementId, memberId);
    }

    public Settlement getSettlementById(Long settlementId) {
        String sql = "SELECT * FROM settlement WHERE settlement_Id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{settlementId}, (rs, rowNum) ->
                new Settlement(rs.getLong("settlement_Id"), rs.getString("name"), rs.getTimestamp("CREATED_DATE"))
        );
    }

    public Settlement getSettlementWithExpenses(Long settlementId) {
        String sql = "SELECT s.*, e.* FROM settlement s LEFT JOIN expense e ON s.settlement_id = e.settlement_id WHERE s.settlement_id = ?";
        return jdbcTemplate.query(sql, new Object[]{settlementId}, rs -> {
            Settlement settlement = null;
            List<Expense> expenses = new ArrayList<>();
            while (rs.next()) {
                if (settlement == null) {
                    settlement = new Settlement(
                            rs.getLong("settlement_id"),
                            rs.getString("name"),
                            rs.getTimestamp("created_date")
                    );
                }
                Long expenseId = rs.getLong("expense_id");
                if (expenseId != 0) {
                    Expense expense = new Expense(
                            expenseId,
                            rs.getLong("settlement_id"),
                            rs.getString("name"),
                            rs.getLong("payer_id"),
                            rs.getBigDecimal("amount"),
                            rs.getDate("date").toString()
                    );
                    expenses.add(expense);
                }
            }
            if (settlement != null) {
                settlement.setExpenses(expenses);
            }
            return settlement;
        });
    }

    public List<Settlement> getAllSettlements() {
        String spl = "SELECT * FROM settlement";
        return jdbcTemplate.query(spl, (rs,rowNum) ->
            new Settlement(
                    rs.getLong("settlement_Id"),
                    rs.getString("name"),
                    rs.getTimestamp("CREATED_DATE")
            )
        );
    }

    @Transactional
    public void deleteSettlement(Long settlementId) {
        String expenseSql = "DELETE FROM expense where settlement_id = ?";
        jdbcTemplate.update(expenseSql, settlementId);

        String settlementSql = "DELETE FROM settlement WHERE settlement_id = ?";
        jdbcTemplate.update(settlementSql, settlementId);
    }
}
