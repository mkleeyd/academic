package management.academic.schoolregister.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import management.academic.common.entity.college.CptnDivCd;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {
    MAN("MAN", "남자"),
    WOMAN("t", "여자");

    private String code;
    private String codeName;


    public static String findCodeName(String code) {
        String result = Arrays.stream(Gender.values())
                .filter(gender -> gender.code.equals(code))
                .findAny()
                .map(Gender::getCodeName)       // .map(mjrCd -> mjrCd.getCodeName())    // 이것도 가능
                .orElse("");
        System.out.println("CodeName ===> " + result);
        return result;
    }

}/////
