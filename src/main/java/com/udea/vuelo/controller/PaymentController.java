package com.udea.vuelo.controller;

import com.udea.vuelo.model.CreditCard;
import com.udea.vuelo.model.Flight;
import com.udea.vuelo.model.Price;
import com.udea.vuelo.service.CreditCardService;
import com.udea.vuelo.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final FlightService flightService;
    private final CreditCardService creditCardService;

    @Autowired
    public PaymentController(FlightService flightService, CreditCardService creditCardService) {
        this.flightService = flightService;
        this.creditCardService = creditCardService;
    }

    @PostMapping("/pay-flight/{flightId}")
    public ResponseEntity<String> payForFlight(@PathVariable int flightId,
                                               @RequestParam String cardNumber,
                                               @RequestParam String expirationDate,
                                               @RequestParam String cvv) {
        // Check the flight price
        Optional<Price> optionalFlight = flightService.searchPriceById(flightId);
        if (optionalFlight.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Flight not found.");
        }

        Price price = optionalFlight.get();
        BigDecimal amount = BigDecimal.valueOf(price.getTotalCost());

        // Check if the credit card exists
        Optional<CreditCard> optionalCreditCard = creditCardService.findByCardNumber(cardNumber);
        if (optionalCreditCard.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Credit card not found.");
        }

        // Check credit card validity
        CreditCard creditCard = optionalCreditCard.get();
        if (!creditCardService.checkCreditCardValidity(creditCard, cardNumber, cvv, LocalDate.parse(expirationDate))) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid credit card information.");
        }

        // Check if the credit card has enough funds
        if (!creditCardService.checkSufficientFunds(creditCard, amount)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient funds.");
        }

        // Update the credit card balance
        creditCard.setBalance(creditCard.getBalance().subtract(amount));

        // Update the credit card balance in the database
        creditCardService.updateBalance(creditCard);

        LocalDateTime transactionDate = LocalDateTime.now();
        String transactionStatus = "Approved";
        creditCardService.generateTransactionSummary(flightId, cardNumber, transactionDate, BigDecimal.valueOf(price.getTotalCost()), transactionStatus);

        return ResponseEntity.ok("Payment successful.");
    }

}
