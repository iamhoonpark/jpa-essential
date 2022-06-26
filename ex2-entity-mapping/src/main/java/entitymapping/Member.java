package entitymapping;

import javax.persistence.*;
import java.util.Date;

/** 엔티티 매핑 */

@Entity // JPA 객체이고 DB 테이블과 매핑해서 사용됨
public class Member {

    /* unique 여부 ture, 글자수(varchar) 10
    @Column(unique = true, length = 10)
    private String name; */

    // PK 매핑
    @Id
    private Long id;

    // 객체는 username으로 쓰고 싶은데, DB에는 name일 때
    // 조건 : not null, 길이 10제한
    @Column(name = "name", nullable = false, length = 10)
    private String username;

    // Integer지만 DB에는 숫자타입으로 만들어짐(Integer에 적절한 숫자타입으로 생성)
    private Integer age;

    // DB에는 enum 타입이 없지만, 객체에서 enum 타입으로 사용하고 싶을 때 @Enumerated
    // enum은 반드시 EnumType.STRING으로 매핑해야 함
    // default인 ORDINAL을 사용할 경우 INT형이며 enum에 data가 추가 또는 수정 될 경우 혼란
    // enum의 문자를 그대로 들어갈 수 있도록 STRING
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    // 타입이 3가지(TIMESTAMP부분을 ctrl + spacebar 할 경우 enum 확인)
    // 자바의 Date 타입은 날짜, 시간이 있지만 DB는 보통 날짜, 시간, 날짜+시간을 구분해서 사용하기에
    // 매핑정보를 줘야하기에 TemporalType.TIMESTAMP
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    // DB에 varchar를 넘어서는 큰 컨텐츠를 넘길 때
    @Lob
    private String description;

    /*
        결과화면 :
        create table Member (
            id bigint not null,
            age integer,            // H2 DB에서는 integer라는 타입이 있음
            createdDate timestamp,
            description clob,       // @Lob과 String 문자타입이 매핑되면 clob으로 생성됨
            lastModifiedDate timestamp,
            roleType varchar(255),  // enum이 varchar랑 미팽됨
            name varchar(255),
            primary key (id)
        )
    */

    public Member() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
/*

  Ⅰ. 객체와 테이블 매핑

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


  Ⅱ. 데이터베이스 스키마 자동 생성

   1. 데이터베이스 스키마 자동 생성
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

   2. 데이터베이스 스키마 자동 생성 - 주의사항
   - 운영 장비에는 절대 create, create-drop, update 사용하면 안 됨
   - 개발 초기 단계는 create 또는 update
   - 테스트 서버(여러 명의 개발자가 함께 사용하는 중간 서버 격)에는 update 또는 validate
   - 스테이징과 운영 서버는 validate 또는 none

   3. DDL 생성 기능
   - 제약조건 추가 : 회원 이름은 필수, 10자 초과 X
    ex) @Column(nullable = false, length = 10)
   - 유니크 제약조건 추가
    ex) @Table(uniqueConstraints = {@UniqueConstraint(name = "NAME_AGE_UNIQUE", columnNames={"NAME", "AGE"})})
   - DDL 생성 기능은 DDL을 자동 생성할 때만 사용되고 JPA의 실행 로직에는 영향을 주지 않음
    · 즉 애플리케이션(JPA 실행 매커니즘)에 영향을 주지 않고 DB에만 영향을 줌


  Ⅲ. 필드와 컬럼 매핑

    1. 요구사항 추가
    - 회원은 일반 회원과 관리자로 구분해야 함
    - 회원 가입일과 수정일이 있어야 함
    - 회원을 설명할 수 있는 필드가 있어야 하며 해당 필드는 길이 제한이 없어야 함

    2. 매핑 어노테이션 정리
    1) @Column 컬럼매핑
    2) @Temporal 날짜 타입 매핑
     - 해당 어노테이션을 사용할 경우 반드시 아래의 속성 중 하나를 선언해야 함
      · TemporalType.DATE,  TemporalType.TIME, TemporalType.TIMESTAMP
     - JAVA8 이후로는 해당 어노테이션을 사용하지 않고 DATA TYPE(자료형)을 LocalDate, LocalDateTime 형으로 선언 후 사용
      · ex: private LocalDate testLD; = DB의 Date형
            private LocalDateTime testLDT; = DB의 Timestamp형
     - 과거버전에서 Date형을 사용할 경우 해당 어노테이션을 사용하여 매핑
    3) @Enumerated : enum 타입
     - 속성: EnumType.ORDINAL (default)순서를 int형으로 카운팅해서 DB에 저장
            EnumType.STRING 문자열 그대로 DB에 저장
     - ORDINAL은 쉼표(,)로 데이터를 구분하고 순서에 따라 0,1,2,3… 값으로 DB에 저장되기 때문에
       데이터가 추가되거나 수정될 경우 DB 값이 중복될 수 있기에 혼란을 가중할 수 있음
     - 따라서 STRING을 사용할 것을 권장
    4) @Lob        : BLOB, CLOB
     - Lob은 지정할 수 있는 속성이 없음
     - 다만, 매핑하는 필드 타입(자료형)이 문자열 경우 CLOB, 나머지는 BLOB으로 매핑됨
     - CLOB : String, char[], java.sql.CLOB
       BLOB : byte[], java.sql.BLOB
    5) @Transient  : 특정 필드를 컬럼에 매핑하지 않음(매핑 무시)
     - 주로 메모리상에서만 임시로 어떤 값을 보관하고 싶을 때
     - DB랑 관계없이 메모리에서만 계산하고 싶을 때
      · ex: @Transient private int temp;

  Ⅳ. 기본 키 매핑
    1. 기본 키 매핑 어노테이션
    ※ ex: @Id @GeneratedValue(strategy = GenerationType.AUTO)
          private Long id;
    1) 직접 할당
     - @Id 만 사용
    2) 자동 생성 할당
     - @GeneratedValue
      ① AUTO: DB 방언에 따라 자동 지정, 기본값
       · TABLE, SEQUENCE, IDENTITY 셋 중 하나 선택 됨
      ② SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용
       · 주로 ORACLE 에서 사용
       · 그러나, 난.. POSCO ICT 출입관리통합시스템 구축 플젝에서 PostgresQL을 사용해서 시퀀스를 만든 후 해당 전략(strategy)를 사용해서 개발했음
       · @SequenceGenerator 필요
      ③ IDENTITY: 기본 키 생성을 데이터베이스에 위임
       · JPA : '나는 모르겠고 DB야 니가 알아서 해줘'
       · MySQL의 경우 AUTO_INCREMENT 가 실행 됨
       · 주로 MySQL, PostgresQL, SQL Server, DB2에서 사용
      ④ TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용
       · @TableGenerator 필요

    2. 권장하는 식별자 전략
    - 기본키 제약 조건: not null, 유일, 변하면 안 됨
    - 미래까지 이 조건을 만족하는 자연키는 찾기 어려우니 대리키(대체키)를 사용
    - 예를 들어 주민등록번호도 기본 키로 적절하지 않음
    - 권장: Long형 + 대체키 + 키 생성전략(ex. uuid, 랜덤값 등) 사용

    3. 전략(strategy)
    1) TABLE
     - 키 생성 전용 테이블을 하나 만들어서 DB 시퀀스를 흉내내는 전략(잘안씀)
     - 장점: 모든 DB에서 적용 가능
     - 단점: 테이블을 직접 사용하다보니 성능이 떨어짐. 숫자 뽑는 시퀀스와 다르게 그것에 대한 최적화가 되어 있지 않음
    2) SEQUENCE
     - Ex:
        @Entity
        @SequenceGenerator( name = “MEMBER_SEQ_GENERATOR",
                            sequenceName = “MEMBER_SEQ", //매핑할 데이터베이스 시퀀스 이름
                            initialValue = 1, // DDL 생성할 때 처음 1로 시작
                            allocationSize = 1 ) // DB 시퀀스 값이 하나씩 증가하도록 설정되어 있으면 이 값을 반드시 1로 설정해야 함
        public class Member {
            @Id
            @GeneratedValue(strategy = GenerationType.SEQUENCE,
                            generator = "MEMBER_SEQ_GENERATOR")
            private Long id;
        }
      ① em.persist(member) -> call next value for MEMEBER_SEQ = seq 오브젝트에서 다음 value 가져오라는 명령
      ② DB에서 값을 가져온 후 Entity에 매핑되어 있는 컬럼으로 값을 저장
      ③ 영속성 컨텍스트에 저장 -> tx.commit(); 때 insert query 실행
     - allocationSize 의 기본값은 50
      · em.persist(member1);
        em.persist(member2);
        em.persist(member3);
      · call next value for SEQ 로 가져올 경우 어찌되었든 네트워크를 계속 타는 것이기 때문에 성능 문제가 생길 수 있어서
       ＊ SQL 편집기 : call next value for MEMBER_SEQ
      · allocationSize 로 미리 50개를 당겨와서 성능을 최적화 함
      · 그래서, call value 할 때, 미리 DB에서 50개를 미리 쌓아놓고 개발자는 메모리에서 1개씩 사용함
      · 50개를 쓸 경우 또 51번 부터 50개를 미리 쌓아놓음
      · 동시성 문제 없이 다양한 문제를 해결할 수 있음
     - 첫 호출 시 DB SEQ = 1, 두 번째 호출 시 DB SEQ = 51
      · em.persist(member1); // 1, 51(최적화)
      · em.persist(member2); // DB에서 호출하지 않고 memory에서 호출
      · em.persist(member3); // DB에서 호출하지 않고 memory에서 호출



Ⅴ. 실제 예제 1 - 요구사항 분석과 기본 매핑
 */