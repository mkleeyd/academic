package management.academic.common.entity.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum FinSchregDivCd {

    FinSchregDivCd001("FinSchregDivCd001", "재학"),
    FinSchregDivCd002("FinSchregDivCd002", "휴학"),
    FinSchregDivCd003("FinSchregDivCd003", "제적");

    private String code;
    private String codeName;

    /**
     * 일단 이렇게 각 구현체에 각각 static 코드 넣어서 임시로 개발하고 나중에 리팩토링 할 때 하나의 구조로 단일화해서 
     * 구조체 자동으로 선택되어 사용할 수 있도록 바꾸기
     */
    public static String findCodeName(String code) {

        String result = Arrays.stream(FinSchregDivCd.values())
                .filter(commonRegister -> commonRegister.code.equals(code))
                .findAny()
                .map(FinSchregDivCd::getCodeName)       // .map(mjrCd -> mjrCd.getCodeName())    // 이것도 가능
                .orElse("");
        System.out.println("CodeName ===> " + result);
        return result;
    }

}
