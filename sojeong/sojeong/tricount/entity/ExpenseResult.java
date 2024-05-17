package clonecoding.tricount.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ExpenseResult {
    private Long id;
    private String title;
    private Long memberId; // 매핑 정보
    private BigDecimal amount;
    private LocalDateTime date;
}
