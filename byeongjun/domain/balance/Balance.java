package proj.tricount.domain.balance;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Balance {
    private Long balanceId;

    private Long settlementId;
    private Long payerId;
    private Long receiverId;
    private BigDecimal amount;

    public Balance(Long balanceId, Long settlementId, Long payerId, Long receiverId, BigDecimal amount) {
        this.balanceId = balanceId;
        this.settlementId = settlementId;
        this.payerId = payerId;
        this.receiverId = receiverId;
        this.amount = amount;
    }

    public Balance() {
    }
}
