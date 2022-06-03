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
 - 트랜잭션 커밋 : 플러쉬 자동 호출
 - JPQL 쿼리 실행 : 플러쉬 자동 호출

*/