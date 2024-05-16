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

}
