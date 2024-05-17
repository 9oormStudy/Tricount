package clonecoding.tricount.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Expense {
    private String title;
    private Long memberId;
    private BigDecimal amount;
}
