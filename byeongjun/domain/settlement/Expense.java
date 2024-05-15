package proj.tricount.domain.settlement;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Expense {
    private Long expenseId;

    private Long settlementId;
    private String name;
    private Long payerId;
    private BigDecimal amount;

    private String date;

    public Expense(Long expenseId, Long settlementId, String name, Long payerId, BigDecimal amount, String date) {
        this.expenseId = expenseId;
        this.settlementId = settlementId;
        this.name = name;
        this.payerId = payerId;
        this.amount = amount;
        this.date = date;
    }

    public Expense() {
    }
}
