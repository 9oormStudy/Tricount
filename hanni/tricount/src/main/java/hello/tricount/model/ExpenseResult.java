package hello.tricount.model;

import hello.tricount.MemberContext;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExpenseResult {
    private Long settlementId;
    private Member payerMember;
    private BigDecimal amount;
}
