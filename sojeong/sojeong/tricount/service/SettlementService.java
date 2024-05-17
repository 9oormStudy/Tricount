package clonecoding.tricount.service;

import clonecoding.tricount.entity.*;
import clonecoding.tricount.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class SettlementService {
    private final SettlementRepository settlementRepository;

    public void createSettlement(String title){
        settlementRepository.create(title);
    }

    public void addParticipant(Long id) {
        settlementRepository.addParticipant(id);
    }

    public Settlement findParticipantsBySettlementId(Long id){
        return settlementRepository.findParticipantsBySettlementId(id);
    }

    public ExpenseResult addExpense(Expense expense, Long id){
        ExpenseResult expenseResult = new ExpenseResult();
        expenseResult.setTitle(expense.getTitle());
        expenseResult.setMemberId(expense.getMemberId());
        expenseResult.setAmount(expense.getAmount());
        expenseResult.setDate(LocalDateTime.now());
        Long expenseId = settlementRepository.addExpense(expenseResult, id);
        expenseResult.setId(expenseId);
        return expenseResult;
    }

    public List<Expense> listOfExpense(Long id){
        return settlementRepository.findExpenseListBySettlementId(id);
    }

    public List<Balance> settlementBalance(Long id){
        List<Expense> expenseList = settlementRepository.findExpenseListBySettlementId(id);
        // 참가자 지출액 및 정산 총지출액
        Map<Long, BigDecimal> totalAmountByMemberId = new HashMap<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Expense expense : expenseList){
            Long memberId = expense.getMemberId();
            BigDecimal amount = expense.getAmount();
            totalAmountByMemberId.merge(memberId, amount, BigDecimal::add);
            totalAmount = totalAmount.add(amount);
        }

        // 평균 금액 계산
        BigDecimal averageAmount = totalAmount.divide(BigDecimal.valueOf(totalAmountByMemberId.size()), RoundingMode.DOWN);

        // 각 참가자의 정산액
        Map<Long, BigDecimal> balances = new HashMap<>();
        for(Long memberId : totalAmountByMemberId.keySet()){
            balances.put(memberId, totalAmountByMemberId.get(memberId).subtract(averageAmount));
        }

        // 잔액이 양수, 음수인 회원 분리
        List<Long> receivers = balances.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(BigDecimal.ZERO) > 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Long> senders = balances.entrySet().stream()
                .filter(entry -> entry.getValue().compareTo(BigDecimal.ZERO) < 0)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 정산 결과
        List<Balance> balanceResult = new ArrayList<>();
        for (Long receiver : receivers){
            BigDecimal receiverBalance = balances.get(receiver);
            for (Long sender : senders) {
                BigDecimal senderBalance = balances.get(sender);
                BigDecimal minAmount = receiverBalance.min(senderBalance.abs());

                balanceResult.add(new Balance(sender, minAmount, receiver));

                receiverBalance = receiverBalance.subtract(minAmount);
                senderBalance = senderBalance.add(minAmount);

                balances.put(receiver, receiverBalance);
                balances.put(sender, senderBalance);

                if (receiverBalance.equals(BigDecimal.ZERO)) break;
            }
        }
        return balanceResult;
    }


}
