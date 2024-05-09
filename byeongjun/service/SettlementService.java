package proj.tricount.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import proj.tricount.domain.settlement.Settlement;

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
}
