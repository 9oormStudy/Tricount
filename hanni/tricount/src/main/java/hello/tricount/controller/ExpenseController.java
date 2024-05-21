package hello.tricount.controller;

import hello.tricount.model.ExpenseRequest;
import hello.tricount.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ExpenseController {
    private final ExpenseService expenseService;

    @PostMapping("/expenses/add")
    public ResponseEntity<Void> addExpenseToSettlement(@RequestBody ExpenseRequest expenseRequest) {
        expenseService.addExpense(expenseRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
