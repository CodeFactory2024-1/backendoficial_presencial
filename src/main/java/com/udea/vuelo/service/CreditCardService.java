package com.udea.vuelo.service;

import com.udea.vuelo.model.CreditCard;
import com.udea.vuelo.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.udea.vuelo.repository.HistoricalReservesRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;
    private final HistoricalReservesRepository historicalReservesRepository;

    @Autowired
    public CreditCardService(CreditCardRepository creditCardRepository, HistoricalReservesRepository historicalReservesRepository) {
        this.creditCardRepository = creditCardRepository;
        this.historicalReservesRepository = historicalReservesRepository;
    }


    // Método para buscar la tarjeta por número
    public Optional<CreditCard> findByCardNumber(String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    // Método para verificar los fondos
    public boolean checkSufficientFunds(CreditCard creditCard, BigDecimal amount) {
        return creditCard.getBalance().compareTo(amount) >= 0;
    }

    // Método para verificar la validez de la tarjeta(Que numero, cvv y fecha de expiración sean correctos)
    public boolean checkCreditCardValidity(CreditCard creditCard, String cardNumber, String cvv, LocalDate expirationDate) {
        return creditCard.getCardNumber().equals(cardNumber) && creditCard.getCvv().equals(cvv) && creditCard.getExpirationDate().equals(expirationDate);
    }

    // Método para actualizar el balance de la tarjeta
    public void updateBalance(CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }

    public void generateTransactionSummary(int flightId, String cardNumber, LocalDateTime transactionDate, BigDecimal totalCost, String transactionStatus) {
        historicalReservesRepository.saveTransactionSummary(flightId, cardNumber, transactionDate, totalCost, transactionStatus);
    }
}
