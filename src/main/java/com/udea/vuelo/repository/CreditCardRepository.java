package com.udea.vuelo.repository;

import com.udea.vuelo.model.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {

    Optional<CreditCard> findByCardNumber(String cardNumber);

}
