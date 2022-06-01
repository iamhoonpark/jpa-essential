package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/** 2. 영속성 컨텍스트 : 실제 JPA가 내부에서 어떻게 동작할까?  */

public class PersistenceContext {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            // entity가 비영속 상태 = JPA와 아무 관련없는 상태
            Member member = new Member();
            member.setId(100L);
            member.setName("HelloJPA");

           // 영속상태 = em을 안에 있는 영속성 컨텍스트를 통해 entity가 관리가 되어짐
           System.out.println("------------BEFORE------------");
           em.persist(member); // 실행 할 경우 BEFORE와 AFTER 사이에서 query가 실행되지 않고
           System.out.println("-------------AFTER------------");

           // AFTER 뒤, 이 곳에서 INSERT Query가 실행됨
           // 즉, 영속상태가 된다고 해서 바로 DB에 쿼리가 날라가지 않고 transaction을 commit하는 시점에서
           // 영속성 컨텍스트에서 관리되어 있는 entity가 DB에 query를 통해 날라감
           tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}

/** 1. 영속성 컨텍스트
  - 영속성 컨텍스트는 JPA를 이해하는데 가장 중요한 용어로 엔티티를 영구 저장하는 환경이라는 뜻
  - EntityManager.persist(entity);
   ·사실 persist 메서드는 DB에 저장하는 것이 아닌 entity를 영속성 컨텍스트에 저장한다는 뜻
  - 영속성 컨텍스트는 논리적인 개념으로 눈에 보이지 않음
  - 엔티티 매니저를 통해서 영속성 컨텍스트에 접근
  - (J2SE환경) EntityManager를 생성하면 그 안에 1:1로 persistenceContext 가 생성됨
  - (J2EE, 스프링프레임워크 같은 컨테이너 환경) EntityManager가 N:1로 여러 em에서 하나의 persistenceContext를 쳐다보는 환경

 ** 2. 엔티티의 생명주기
  1) 비영속(new/transient)
   - 영속성 컨텍스트와 전혀 관계가 없는 새로운 상태
  2) 영속(managed)
   - 영속성 컨텍스트에 관리되는 상태(ex. em.persist(member);)
  3) 준영속(detached)
   - 영속성 컨텍스트에 저장되었다가 분리된 상태(ex. em.detach(member);)
  4) 삭제(removed)
   - 삭제된 상태(ex. em.remove(member);)

 ** 3. 영속성 컨텍스트의 이점
 - 왜 이렇게 이상한 매커니즘들을 다룰까?
 - 지금까지 다룬 내용을 토대로 생각해보면 애플리케이션과 DB사이에 중간 계층에 존재하며 이것을 가지고 큰 이점을 낼 수 있는데
  1) 1차 캐시
  2) 동일성(identity) 보장
  3) 트랜잭션을 지원하는 쓰기 지연(transactional write-behind)
  4) 변경 감지(Dirty Checking)
  5) 지연 로딩(Lazy Loading)
 - 이렇게 중간에 존재할 경우 버퍼링을 할 수 있고 캐싱을 할수도 있고 위와 같이 영속성 컨텍스트를 통해 이점들을 누릴 수 있음 */
