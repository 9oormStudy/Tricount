package hello.tricount.service;

import hello.tricount.model.Expense;
import hello.tricount.model.ExpenseRequest;
import hello.tricount.model.Settlement;
import hello.tricount.repository.ExpenseRepository;
import hello.tricount.repository.SettlementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final SettlementRepository settlementRepository;
    private final ExpenseRepository expenseRepository;

    public void addExpense(ExpenseRequest request) {
        Optional<Settlement> settlementOptional = settlementRepository.findById(request.getSettlementId());
        if(!settlementOptional.isPresent()) {
            throw new RuntimeException("");
        }

        Expense expense = new Expense();
        expense.setName(request.getName());
        expense.setSettlementId(request.getSettlementId());
        expense.setPayerMemberId(request.getPayerMemberId());
        expense.setAmount(request.getAmount());
        expense.setExpenseDateTime(LocalDateTime.now());
        expenseRepository.save(expense);
    }
}
