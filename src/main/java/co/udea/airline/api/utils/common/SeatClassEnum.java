package co.udea.airline.api.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum SeatClassEnum {
    FIRST_CLASS("T",0.1f,new BigDecimal("85000")),
    EXECUTIVE("FC",0.1f, new BigDecimal("50000")),
    TOURIST("E",0.8f, new BigDecimal("0"));

    private final String tag;
    private final float proportion;
    private final BigDecimal surcharge;
}
