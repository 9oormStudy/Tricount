package proj.tricount.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import proj.tricount.domain.balance.BalanceDTO;
import proj.tricount.service.BalanceService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class BalanceController {
    private final BalanceService balanceService;

    @GetMapping("/balances")
    @ResponseBody
    public List<BalanceDTO> calculateBalances(@RequestParam Long settlementId) {
        return balanceService.calculateBalances(settlementId);
    }
}
