package co.udea.airline.api.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum SeatClassEnum {
    TOURIST("T",0.1f,new BigDecimal("0")),
    FIRST_CLASS("FC",0.1f, new BigDecimal("85000")),
    EXECUTIVE("E",0.8f, new BigDecimal("50000"));

    private final String tag;
    private final float proportion;
    private final BigDecimal surcharge;
}
