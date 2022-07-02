package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 저장
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            // Jpa가 알아서 PK값을 꺼내서 저장할 때, FK를 사용
            member.setTeam(team);
            em.persist(member);

            /* flush
            * em.flush(); 강제호출, 현재 영속성 컨텍스트에 있는 쿼리를 DB에 다 날려버려서 싱크를 맞춤
            * em.clear(); 영속성 컨텍스트를 완전 초기화
            * 결과 → insert 쿼리 두번 → select 쿼리(조인문) 한번 → sout 실행 → Hibernate 실행
            * jpa가 member와 team을 join을 통해 한번에 가져옴 → 분리하고 싶다면 @ManyToOne(fetch설정) */

            // 조회
            Member findMember = em.find(Member.class, member.getId());
            Team findTeamId = findMember.getTeam();
            
            // Q. 실행 시, 여기서 쿼리문이 발생하지 않는 이유
            // A. 영속성 컨텍스트 때문에 → 1차캐시에서 가져옴
            // 만약 쿼리문을 보고싶다? → flush 로 이동
            
            // 객체지향스럽게 레퍼런스들을 바로 가져올 수 있다.
            System.out.println("findTeamId.getId() + \",\" + findTeamId.getName() = " + findTeamId.getId() + "," + findTeamId.getName());

            /* 만약 찾은 멤버로 다른 팀으로 바꾸고 싶을 때,
            * Team newTeam = em.find(Team.class, 100L);
            * findTeamId.setTeam(newTeam); */

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}
