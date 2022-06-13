package entitymapping;

import javax.persistence.Entity;
import javax.persistence.Id;

/** 1. 객체와 테이블 매핑 */

@Entity // JPA 객체이고 DB 테이블과 매핑해서 사용됨
public class Member {

    @Id
    private Long id;
    private String name;
    private int age;

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

   4. 데이터베이스 스키마 자동 생성
   - JPA는 객체 매핑을 다 해놓으면 애플리케이션이 실행 될 때(로딩 시점), Create문으로 DB 테이블을 다 만들어주고 시작 가능
    · 장점: 개발할 때, 테이블을 생성하고 객체(Entity)를 생성하지만, JPA는 객체 매핑을 다 해놓으면,
      애플리케이션 뜰 때, 테이블을 자동으로 다 생성해줌
   - 데이터베이스 방언을 활용해서 DataBase에 맞는 적절한 DDL을 생성
    ex) oracle: varchar2 = mySQL: varcar
   - <property name="hibernate.hbm2ddl.auto" value="create" /> 해당 설정을 persistence.xml에 선언 후 서버 run
    · value="creat" 이 외의 옵션
    1) create : 기존테이블 삭제 후 다시 생성(DROP > CREATE)
    2) create-drop : create와 같으나 애플리케이션 종료시점에 테이블 DROP
     · 테스트 케이스 때, 실험해보고 날리고 싶을 때 주로 사용
    3) update : 변경분만 반영(운영 DB에는 사용하면 안됨)
    4) validate : 엔티티와 테이블이 정상 매핑되었는지만 확인
    5) none : 사용하지 않음 
   - 주의사항 이렇게 생성된 DDL은 반드시 개발장비에서만
    · 운영서버에서 필요한 경우, 생성된 DDL은 불안할 수 있기 때문 적절히 다듬은 후에 사용하는 것을 권장
 */