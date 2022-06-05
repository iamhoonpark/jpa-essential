package entitymapping;

import javax.persistence.Entity;
import javax.persistence.Id;

/** 1. 객체와 테이블 매핑 */

@Entity // JPA 객체이고 DB 테이블과 매핑해서 사용됨
public class Member {

    @Id
    private Long id;
    private String name;

    public Member() {

    }

    public Member(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
/*
    객체와 테이블 매핑 : @Entity, @Table
    필드와 컬럼 매핑 : @Column
    기본 키 매핑 : @Id
    연관관계 매핑 : @ManyToOne, @JoinColumn

    1. @Entity
    - @Entity가 붙은 클래스는 JPA가 관리, 엔티티라 함
    - JPA를 사용해서 테이블과 매핑할 클래스는 @Entity 필수
    - 주의사항
     ① 기본 생성자 필수(파라미터가 없는 public 또는 protected 생성자)
      · JPA 스펙상 규정된 내용
     ② final 클래스, enum, interface, inner 클래스에는 @Entity로 매핑이 불가
     ③ 저장할 필드에 final 사용 X
      · 내가 DB에 저장하고 싶은 필드에는 final이 안 됨

    2. @Entity 속성
    - 속성 : name
     · name 이라는 속성이 있지만 차라리 클래스명을 고쳐서 사용할 것
     · 기본 값은 클래스 명과 동일하게 설정이 됨 ex) @Entity(name="Member")
     · JPA가 내부적으로 구분하는 이름
    - 같은 클래스 이름이 없으면 가급적 기본값을 사용하여 유지
    
   3. @Table
   - 엔티티와 매핑할 테이블을 지정
   - name : 매핑할 테이블 이름을 지정, 기본값은 엔티티명
   - 기타 속성 :catalog, schema, uniqueConstraints(DDL)

 */