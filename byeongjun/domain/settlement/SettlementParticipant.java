package proj.tricount.domain.settlement;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class SettlementParticipant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long participantId;

    private Long settlementId;
    private Long memberId;

    public SettlementParticipant() {
    }

    public SettlementParticipant(Long participantId, Long settlementId, Long memberId) {
        this.participantId = participantId;
        this.settlementId = settlementId;
        this.memberId = memberId;
    }
}
