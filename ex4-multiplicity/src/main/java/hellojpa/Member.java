package hellojpa;

import javax.persistence.*;

@Entity
public class Member {

    // 객체를 테이블에 맞추어 모델링(참조 대신에 외래 키를 그대로 사용)

    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(name = "USER_NAME")
    private String name;

    /* 객체지향 모델링 하는 방법
    @Column(name = "TEAM_ID")
    private Long teamId;
    
    Team 객체를 import 해도 error가 발생 JPA한테
    해당 객체와 Member 객체가 무슨 사이(1:1, 1:N 등)인지 DB관점에서 알려줘야 함
    1) @ManyToOne
     - 어노테이션들은 DB 관점에서 바라보는 것
     - Member객체는 Many, Team객체는 One N:1
    2) JoinColumn
     - Member객체의 team 레퍼런스와 Member테이블의 team_id FK와 매핑
    3) 이렇게 했는데, Data Source 에러가 발생한다면,
     - 인텔리제이에서 DB에 실제 해당 값이 있는지 체킹을 위해 에러 발생내는 것으로 무시해도 됨 */
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public Member() {
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

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
