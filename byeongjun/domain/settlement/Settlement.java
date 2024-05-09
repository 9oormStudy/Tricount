package proj.tricount.domain.settlement;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Settlement {
    private Long settlementId;

    private String name;
    private Timestamp createdDate;

    public Settlement(Long id, String name, Timestamp createDate) {
        this.settlementId = id;
        this.name = name;
        this.createdDate = createDate;
    }

    public Settlement() {

    }
}
