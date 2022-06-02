package hellojpa;

import javax.persistence.Entity;
import javax.persistence.Id;

/** table과 mapping할 Entity 객체 */
@Entity // JPA가 관리해야 하는 애라고 인식
//@Table(name = "USER") 테이블 이름이 Member가 아니라 USER일 경우 해당 데코레이터로 매핑
public class Member {

    @Id // PK
    private Long id;
    //@Column(name = "username") DB 컬럼명이 name이 아닌 username일 경우 해당 데코레이터로 매핑
    private String name;

    // JPA는 내부적으로 리플렉션이나 동적으로 객체를 생성해야 하기 때문에 (인자가 있는 생성자를 만들 경우) 기본생성자가 있어야 함
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
