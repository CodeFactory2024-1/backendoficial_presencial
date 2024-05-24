package co.udea.airline.api.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
@Getter
@AllArgsConstructor
public enum SeatLocationEnum {
    WINDOW(new BigDecimal("15000")),
    CENTER(new BigDecimal("0")),
    AISLE(new BigDecimal("10000"));

    private final BigDecimal surcharge;
}
