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

    CONVERTTOAP(4.3f, "A+"),
    CONVERTTOAE(4.0f, "A"),
    CONVERTTOAM(3.8f, "A-"),
    CONVERTTOBP(3.6f, "B+"),
    CONVERTTOBE(3.4f, "B"),
    CONVERTTOBM(3.2f, "B-"),
    CONVERTTOCP(3.0f, "C+"),
    CONVERTTOCE(2.8f, "C"),
    CONVERTTOCM(2.6f, "C-"),
    CONVERTTODP(2.4f, "D+"),
    CONVERTTODE(2.2f, "D"),
    CONVERTTODM(2.0f, "D-"),
    CONVERTTOFF(0f, "F");

    private Float score;
    private String value;

//    public static ConvertedScore convertCode(Integer score){
//        return ConvertedScore
//    }

}
