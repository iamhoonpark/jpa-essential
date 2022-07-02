package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/** 1. 객체와 관계형 데이터베이스 매핑하기(ORM)  */

public class BeforeJpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /* 현재 Member 테이블에 team_id를 그대로 가져온 상태(참조가 아닌 외래키 값을 그대로 가져옴)
             * 객체를 테이블에 맞춰 모델링을 한 상태(=> 결과값 하단 주석내용 참조) 이 것의 문제점은 */
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team); // id에 값이 들어감(영속상태가 되면 무조건 PK에 값이 세팅되고 영속상태 들어감)

            Member member = new Member();
            member.setName("member1");

            // member1을 TeamA에 소속시키고 싶다면
            // team에 id를 부여해야 함
            //member.setTeamId(team.getId());
            em.persist(member);

            // call next value for hibernate_sequence 시퀀스를 공용으로 사용하기에 teamId = 1, memberId = 2

            // 멤버의 Team을 알고 싶을 때
            Member findMember = em.find(Member.class, member.getId());
            // Long findTeamId =  findMember.getTeamId();
            // Team findTeam = em.find(Team.class, findTeamId);
            
            // 연관관계가 없기 때문에 위 작업 처럼 DB에서 계속 꺼낸 후에야 출력 가능
            // System.out.println("findTeam : " + findTeam);

            /* 이 처럼 객체를 테이블에 맞추어 데이터 중심으로 모델링하면, 협력 관계를 만들 수 없다.
               - 테이블: 외래 키로 조인을 사용해서 연관된 테이블을 찾는다.
               - 객  체: 참조를 사용해서 연관된 객체를 찾는다.
               - 즉, 테이블과 객체 사이에는 이런 큰 간격이 있다.
               - 객체지향 모델링하는 방법 → Member */
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}
/*
- 결과값
Hibernate:

    create table Member (
       MEMBER_ID bigint not null,
        USER_NAME varchar(255),
        TEAM_ID bigint,
        primary key (MEMBER_ID)
    )
Hibernate:

    create table Team (
       TEAM_ID bigint not null,
        name varchar(255),
        primary key (TEAM_ID)
    )
*/
