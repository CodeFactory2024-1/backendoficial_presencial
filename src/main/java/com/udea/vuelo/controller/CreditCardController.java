package com.udea.vuelo.controller;

import com.udea.vuelo.model.CreditCard;
import com.udea.vuelo.service.CreditCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/credit-cards")
public class CreditCardController {

    private final CreditCardService creditCardService;

    @Autowired
    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @GetMapping("/{cardNumber}")
    public ResponseEntity<String> checkFundsAvailability(@PathVariable String cardNumber,
                                                         @RequestParam BigDecimal amount) {
        Optional<CreditCard> optionalCreditCard = creditCardService.findByCardNumber(cardNumber);
        if (optionalCreditCard.isPresent()) {
            CreditCard creditCard = optionalCreditCard.get();
            if (creditCardService.checkSufficientFunds(creditCard, amount)) {
                return ResponseEntity.ok("Funds available. Transaction approved.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found.");
        }
    }
}
