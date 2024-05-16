package clonecoding.tricount.controller;

import clonecoding.tricount.entity.*;
import clonecoding.tricount.service.SettlementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementService settlementService;

    @PostMapping("/settlement/create")
    public ResponseEntity<Void> createSettlement(@RequestBody String title){
        settlementService.createSettlement(title);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/settlement/{id}/add")
    public ResponseEntity<Void> addParticipant(@PathVariable("id") Long id){
        settlementService.addParticipant(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/settlement/{id}/participants")
    public ResponseEntity<Settlement> findParticipantsBySettlementId(@PathVariable("id") Long id){
        Settlement settlement = settlementService.findParticipantsBySettlementId(id);
        return new ResponseEntity<>(settlement, HttpStatus.OK);
    }

    @PostMapping("/settlement/{id}/expense/add")
    public ResponseEntity<ExpenseResult> addExpenseToSettlement(@RequestBody Expense expense, @PathVariable("id") Long id){
        ExpenseResult result = settlementService.addExpense(expense, id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/settlement/{id}/expense")
    public ResponseEntity<List<Expense>> listOfExpense(@PathVariable Long id){
        List<Expense> expenses = settlementService.listOfExpense(id);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }


}
