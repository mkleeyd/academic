//package management.academic.schoolregister.entity;
//
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.Column;
//import javax.persistence.GeneratedValue;
//import javax.persistence.Id;
//import java.io.Serializable;
//import java.util.Objects;
//
///**
// * 여기에 @GeneratedValue 추가해서 다 같이 쓰는것
// *   - Entity에 직접 @GeneratedValue 쓰면 안됨
// *   - Entity에는 @IdClass(StuNoPk.class)와 필드만 추가해주면 됨
// */
//public class StuNoPk implements Serializable {
//
//    @Id @GeneratedValue
//    @Column(name = "stuNo")
//    private String stuNo;
//
//    @Column(name = "enterYear")
//    private String enterYear; // 입학년도
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        StuNoPk stuNoPk = (StuNoPk) o;
//        return Objects.equals(stuNo, stuNoPk.stuNo) && Objects.equals(enterYear, stuNoPk.enterYear);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(stuNo, enterYear);
//    }
//}
