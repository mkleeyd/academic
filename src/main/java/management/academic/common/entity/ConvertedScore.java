package management.academic.common.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * <환산점수 공통 Enum>
 *
 * input ==> Score
 * output ==> Code
 */
@Getter
@AllArgsConstructor
public enum ConvertedScore {

    CONVERTTOAP("A+","4.3"),
    CONVERTTOAE("A","4.0"),
    CONVERTTOAM("A-","3.8"),
    CONVERTTOBP("B+","3.6"),
    CONVERTTOBE("B","3.4"),
    CONVERTTOBM("B-","3.2"),
    CONVERTTOCP("C+","3.0"),
    CONVERTTOCE("C","2.8"),
    CONVERTTOCM("C-","2.6"),
    CONVERTTODP("D+","2.4"),
    CONVERTTODE("D","2.2"),
    CONVERTTODM("D-","2.0"),
    CONVERTTOFF("F","0");

    private String code;
    private String codeName;

//    public static ConvertedScore convertCode(Integer score){
//        return ConvertedScore
//    }

}
