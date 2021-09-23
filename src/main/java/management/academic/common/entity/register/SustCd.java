package management.academic.common.entity.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum SustCd {
    SUSTCD001("SUSTCD001", "법학"),
    SUSTCD002("SUSTCD002", "행정학");

    private String code;
    private String codeName;

    /**
     * 일단 이렇게 각 구현체에 각각 static 코드 넣어서 임시로 개발하고 나중에 리팩토링 할 때 하나의 구조로 단일화해서
     * 구조체 자동으로 선택되어 사용할 수 있도록 바꾸기
     */
    public static String findCodeName(String code) {
        String result = Arrays.stream(SustCd.values())
                .filter(sustCd -> sustCd.code.equals(code))
                .findAny()
                .map(SustCd::getCodeName)       // .map(mjrCd -> mjrCd.getCodeName())    // 이것도 가능
                .orElse("");
        System.out.println("CodeName ===> " + result);
        return result;
    }
}
