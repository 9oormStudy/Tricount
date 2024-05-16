package clonecoding.tricount.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class Balance {
    private Long senderMemberId;
    private BigDecimal sendAmount;
    private Long receiverMemberId;
}
