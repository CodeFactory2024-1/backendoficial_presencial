package co.udea.airline.api.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatClassEnum {
    TOURIST("T",0.1f),
    FIRST_CLASS("FC",0.1f),
    EXECUTIVE("E",0.8f);

    private final String tag;
    private final float proportion;
}
