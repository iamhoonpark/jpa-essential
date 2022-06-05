package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/** 5. 준영속 상태 : 영속성 컨텍스트의 내용을 DB에 반영  */

public class detached {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // em.find() 메서드로 인자에 있는 값을 가져올 경우 영속상태로 전환
            // 1) DB에서 값을 가져오는데, 영속성 컨텍스트 1차 캐시에 값을 올 리고
            Member member = em.find(Member.class, 150L);

            // 2) 값을 변경하기에 더티체킹킹
            member.setName("AAAAA");

            // 4) em.detach(entity) 로 JPA 에서 관리 안하게 함 = 준영속(분리)
            // 따라서, tx.commit할 때 아무 일도 일어나지 않음
            // 실행할 경우 select 쿼리만 발생
            em.detach(member);

            // 5) select 쿼리 2번 실행 em.clear();
            // Member member2 = em.find(Member.class, 150L);

            System.out.println("――――――――――――――――");

            // 3) 변경 내역을 추적해서 update 쿼리를 이 시점에서 날림
            tx.commit();

        } catch (Exception e) {
            tx.rollback();
        } finally {
            // 6) em.close();
            em.close();
        }
        emf.close();
    }

}
/*

  1. 준영속 상태
  - 영속 > 준영속
   1) 영속 상태란?
    · 영속성 컨텍스트 내부 1차 캐시로 올라온 상태로 JPA가 관리하는 상태
    ⓐ em.persist() 인자에 객체를 담으면 영속상태
    ⓑ em.find()를 해서 DB에서 값을 가져와서 1차 캐시에 올린 상태(=JPA를 통해 조회)
   2) 준영속 상태란?
    · 영속 상태의 엔티티가 영속성 컨텍스트에서 분리(detached)
    · 따라서, 영속성 컨텍스트가 제공하는 기능* 을 사용하지 못함
    * update 기능, 변경감지(dirty checking) 등

  2. 준영속 상태로 만드는 방법
  1) em.detach(entity)
   - 특정 엔티티만 준영속 상태로 전환
  2) em.clear()
   - 영속성 컨텍스트를 완전히 초기화
    · Entity Manager(영속성 매니저) 안에 있는 영속성 컨텍스트를 아예 지워버림
  3) em.close()
   - 영속성 컨텍스트를 종료

*/