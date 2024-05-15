package proj.tricount.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import proj.tricount.domain.settlement.Expense;
import proj.tricount.service.ExpenseService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/expense")
    @ResponseBody
    public ResponseEntity<Object> addExpense(@ModelAttribute Expense expense) {
        try {
            expenseService.addExpense(expense);
            log.info("지출 추가 완료");
            return ResponseEntity.ok(expense);
        } catch (DataAccessException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/expenses")
    @ResponseBody
    public List<Expense> getExpensesBySettlementId(@RequestParam Long settlementId) {
        return expenseService.getExpensesBySettlementId(settlementId);
    }

    @DeleteMapping("/delete_expense")
    @ResponseBody
    public void deleteExpense(@RequestParam Long expense_id) {
        expenseService.deleteExpense(expense_id);
    }

}
