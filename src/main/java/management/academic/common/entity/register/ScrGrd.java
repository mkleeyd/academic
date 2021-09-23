package management.academic.common.entity.register;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScrGrd {

    SCRGRDAP("4.3", "A+"),
    SCRGRDAE("4.0", "A"),
    SCRGRDAM("3.8", "A-"),
    SCRGRDBP("3.6", "B+"),
    SCRGRDBE("3.4", "B"),
    SCRGRDBM("3.2", "B-"),
    SCRGRDCP("3.0", "C+"),
    SCRGRDCE("2.8", "C"),
    SCRGRDCM("2.6", "C-"),
    SCRGRDDP("2.4", "D+"),
    SCRGRDDE("2.2", "D"),
    SCRGRDDM("2.0", "D-"),
    SCRGRDFF("0", "F");

    private String code;
    private String codeName;

}
