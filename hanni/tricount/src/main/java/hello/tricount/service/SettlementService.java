package hello.tricount.service;

import hello.tricount.MemberContext;
import hello.tricount.model.BalanceResult;
import hello.tricount.model.ExpenseResult;
import hello.tricount.model.Member;
import hello.tricount.model.Settlement;
import hello.tricount.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementService {
    private final SettlementRepository settlementRepository;
    public Settlement createSettlement(String settlementName) {
        Settlement settlement = settlementRepository.create(settlementName);
        settlementRepository.addParticipantToSettlement(settlement.getId(), MemberContext.getMember().getId());
        settlement.getParticipants().add(MemberContext.getMember());

        return settlement;
    }

    public void joinSettlement(Long settlementId) {
        //정산방이 있는지 확인
        settlementRepository.addParticipantToSettlement(settlementId, MemberContext.getMember().getId());
    }

    // 정산 로직 구현
    public List<BalanceResult> getBalanceResult(Long settlementId) {
        Map<Member, List<ExpenseResult>> collect = settlementRepository.findExpensesWithMemberBySettlementId(settlementId)
                .stream().collect(Collectors.groupingBy(ExpenseResult::getPayerMember));

        //A: 1000, 2000
        //B: 1000

        Map<Member, BigDecimal> memberAmountSumMap = collect.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, memberListEntry ->
                memberListEntry.getValue().stream().map(ExpenseResult::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));

        //A: 3000
        //B: 1000

        BigDecimal sumAmount = memberAmountSumMap.values().stream().reduce(BigDecimal.ZERO, BigDecimal::add); // 4000
        BigDecimal averageAmount = sumAmount.divide(BigDecimal.valueOf(memberAmountSumMap.size()), BigDecimal.ROUND_DOWN); //2000

        //A: 3000 -2000 : 1000
        //B: 1000 - 2000 : -1000

        Map<Member, BigDecimal> calcuratedAmountMap = memberAmountSumMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, memberBigDecimalEntry -> memberBigDecimalEntry.getValue().subtract(averageAmount)));

        List<Map.Entry<Member, BigDecimal>> receivers = calcuratedAmountMap.entrySet().stream()
                .filter(memberBigDecimalEntry -> memberBigDecimalEntry.getValue().signum() > 0)
                .collect(Collectors.toList());

        List<Map.Entry<Member, BigDecimal>> senders = calcuratedAmountMap.entrySet().stream()
                .filter(memberBigDecimalEntry -> memberBigDecimalEntry.getValue().signum() < 0)
                .collect(Collectors.toList());

        // A: 1000, B: 1000, C: 1000
        // D: -2000, E: -1000

        List<BalanceResult> balanceResults = new ArrayList<>();
        int receiverIndex = 0;
        int senderIndex = 0;
        while (receiverIndex < receivers.size() && senderIndex < senders.size()) {
            BigDecimal amountToTransfer = receivers.get(receiverIndex).getValue().add(senders.get(senderIndex).getValue());

            if(amountToTransfer.signum() < 0) {
                balanceResults.add(new BalanceResult(
                        senders.get(senderIndex).getKey().getId(),
                        senders.get(senderIndex).getKey().getName(),
                        receivers.get(receiverIndex).getValue().abs(),
                        receivers.get(receiverIndex).getKey().getId(),
                        receivers.get(receiverIndex).getKey().getName()
                ));
                receivers.get(receiverIndex).setValue(BigDecimal.ZERO);
                senders.get(senderIndex).setValue(amountToTransfer);
                receiverIndex++;
            } else if (amountToTransfer.signum() > 0) {
                balanceResults.add(new BalanceResult(
                        senders.get(senderIndex).getKey().getId(),
                        senders.get(senderIndex).getKey().getName(),
                        senders.get(senderIndex).getValue().abs(),
                        receivers.get(receiverIndex).getKey().getId(),
                        receivers.get(receiverIndex).getKey().getName()
                ));
                receivers.get(receiverIndex).setValue(amountToTransfer);
                senders.get(senderIndex).setValue(BigDecimal.ZERO);
                senderIndex++;
            } else {
                balanceResults.add(new BalanceResult(
                        senders.get(senderIndex).getKey().getId(),
                        senders.get(senderIndex).getKey().getName(),
                        senders.get(senderIndex).getValue().abs(),
                        receivers.get(receiverIndex).getKey().getId(),
                        receivers.get(receiverIndex).getKey().getName()
                ));
                receivers.get(receiverIndex).setValue(BigDecimal.ZERO);
                senders.get(senderIndex).setValue(BigDecimal.ZERO);
                receiverIndex++;
                senderIndex++;
            }
        }

        return balanceResults;
    }
}
