package com.udea.vuelo.service;

import com.udea.vuelo.model.CreditCard;
import com.udea.vuelo.repository.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CreditCardService {

    private final CreditCardRepository creditCardRepository;

    @Autowired
    public CreditCardService(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    // Método para buscar la tarjeta por número
    public Optional<CreditCard> findByCardNumber(String cardNumber) {
        return creditCardRepository.findByCardNumber(cardNumber);
    }

    // Método para verificar los fondos
    public boolean checkSufficientFunds(CreditCard creditCard, BigDecimal amount) {
        return creditCard.getBalance().compareTo(amount) >= 0;
    }
}
