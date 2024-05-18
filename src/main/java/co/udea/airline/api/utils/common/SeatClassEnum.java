package co.udea.airline.api.utils.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SeatClassEnum {
    TOURIST("T"), 
    FIRST_CLASS("FC"), 
    EXECUTIVE("E");

    private String tag;
}
