package management.academic.common.entity.college;

import lombok.AllArgsConstructor;
import lombok.Getter;
import management.academic.common.entity.register.ShtmCd;

import java.util.Arrays;

/**
 * 이수구분 코드
 */
@Getter
@AllArgsConstructor
public enum CptnDivCd {

    CPTNDIVCD001("CPTNDIVCD001", "전공필수"),
    CPTNDIVCD002("CPTNDIVCD002", "전공선택"),
    CPTNDIVCD003("CPTNDIVCD003", "교양"),
    CPTNDIVCD004("CPTNDIVCD004", "일반");

    private String code;
    private String codeName;

    /**
     * 일단 이렇게 각 구현체에 각각 static 코드 넣어서 임시로 개발하고 나중에 리팩토링 할 때 하나의 구조로 단일화해서
     * 구조체 자동으로 선택되어 사용할 수 있도록 바꾸기
     */
    public static String findCodeName(String code) {
        String result = Arrays.stream(CptnDivCd.values())
                .filter(cptnDivCd -> cptnDivCd.code.equals(code))
                .findAny()
                .map(CptnDivCd::getCodeName)       // .map(mjrCd -> mjrCd.getCodeName())    // 이것도 가능
                .orElse("");
        System.out.println("CodeName ===> " + result);
        return result;
    }

}
