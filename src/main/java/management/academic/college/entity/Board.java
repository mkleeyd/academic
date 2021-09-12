package management.academic.college.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import management.academic.schoolregister.entity.BaseEntity;

import javax.persistence.*;

/**
 * <공지사항 엔티티>
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board
//        extends BaseEntity
{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boardNo")
    private Long id;
    private String name;
    private String title;
    private String content;

    //===== 연관관계 편의 메서드 =====//

    //===== 엔티티 비지니스 로직 =====//

}/////
