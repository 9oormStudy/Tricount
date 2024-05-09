package proj.tricount.domain.balance;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceDTO {
    private Long payerId;
    private String payerName;
    private Long receiverId;
    private String receiverName;
    private BigDecimal amount;

    public BalanceDTO(Long payerId, String payerName, Long receiverId, String receiverName, BigDecimal amount) {
        this.payerId = payerId;
        this.payerName = payerName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.amount = amount;
    }

    public BalanceDTO() {
    }
}
