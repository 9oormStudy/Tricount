package hello.tricount.controller;

import hello.tricount.model.Settlement;
import hello.tricount.service.SettlementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SettlementController {
    private final SettlementService settlementService;

    @PostMapping("/settles/create")
    public ResponseEntity<Object> createSettlement(@RequestParam String settlementName) {
        Settlement settlement = settlementService.createSettlement(settlementName);
        return new ResponseEntity<>(settlement, HttpStatus.OK);
    }


}
