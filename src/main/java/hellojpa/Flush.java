package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/** 4. 플러쉬 : 영속성 컨텍스트의 내용을 DB에 반영  */

public class Flush {

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            Member member = new Member(200L, "member200");
            // 트랜잭션이 커밋되기 전 까진 해당 persist의 insert 쿼리를 볼 수가 없음
            // 그래서 내가 미리 DB에 반영하고 싶을 때, 또는 쿼리 날리는 걸 미리 보고 싶다면 강제로 flush 호출
            em.flush();
            em.persist(member);
            System.out.println("----------");
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

 1. 플러쉬
 - 영속성 컨텍스트 내에 변경 사항과 DB를 맞추는 작업
 - 영속성 컨텍스트에 쌓여진 쿼리들을 날려주는 역할
 - DB 트랜잭션이 발동하면 Flush가 자동으로 발생

 2. 플러쉬 발생
 - 변경 감지가 일어나고 수정한 엔티티를 '쓰기 지연 SQL 저장소' 에 등록
 - '쓰기 지연 SQL 저장소(등록, 수정, 삭제 쿼리들)' 의 쿼리를 DB에 전송
 - 플러쉬가 발생한다고 해서 데이터 베이스 트랜잭션이 커밋되는 건 아니고, 보내고 DataBase commit(ex: tx.commit();) 한 경우

 3. 영속성 컨텍스트를 플러쉬하는 방법
 - em.flush() : 직접호출
 - 트랜잭션을 커밋하면 플러쉬가 자동 호출
 - JPQL 쿼리를 실행할 때도 플러쉬가 자동 호출
  · 자동으로 호출되는 이유?
  · em.persist(memberA);
    em.persist(memberB);
    em.persist(memberC);
    query = em.createQuery("select m from Member m", Member.class);
    List<Member> members = query.getResultList();
  · memberA,B,C를 DB에 넣은 다음에 바로 그 아래 코드에서 모든 member를 조회
  · 그러나 member A,B,C 는 DB에서 조회가 불가능
  · 왜냐? insert 쿼리 자체가 실행되지 않기 때문(tx.commit 전)
  · JPQL은 sql로 번역이 되어서 실행이 됨 따라서, DB에서 가져올게 없음
  · 그래서 잘못하면 문제가 생길 수 있기에 JPA는 이런 것을 방지하기 위해 JPQL을 실행할 때 기본 모드가 무조건 flush를 실행

  4. 플러쉬 모드 옵션
  - em.setFlushMode(FlushModeType.COMMIT)
   · 그러나 쓸 일이 없고 큰 도움이 되지 않음
  1) 커밋이나 쿼리를 실행할 때 플러쉬(기본 값) : FlushModeType.AUTO
  2) 커밋할 때만 플러쉬 : FlushModeType.COMMIT
   · 쿼리를 실행할 때는 플러쉬를 하지 않지만, 커밋을 할 때만 플러쉬가 됨

  5. 플러쉬는
  - 영속성 컨텍스트를 비우는 것이 아님
  - 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화하는 것
  - 플러쉬라는 매커니즘이 동작할 수 있는 이유는 이 트랜잭션이라는 작업단위가 있기 때문,
    데이터베이스 트랜잭션이라는 단위가 있기 때문에 가능한 것이며, 중요한 것은 커밋 직전에만 동기화를 하면 됨
    (트랜잭션 커밋 직전에만 변경내역을 DB에 날려주면 되기 때문에 이런 매커니즘이 가능함)
  * JPA는 기본적으로 어떤 데이터를 맞추거나 동시성에 대한 것은 전부 DB 트랜잭션에 위임해서 씀
*/