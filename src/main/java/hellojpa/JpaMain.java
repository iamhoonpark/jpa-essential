package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        // 시작
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        // JPA의 모든 데이터 변경은 트랜잭션 안에서 이루어져야 함
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 문제가 발생 시, em, emf가 호출이 안될 수 있음 따라서 try~catch 구문
        try {
            // 1번에 대한 데이터를 수정(UPDATE) : em.remove(1L);
            Member findMember = em.find(Member.class, 1L);

            // em.persist(findMember)으로 저장하는 것이 아님
            // 자바컬렉션과 같이 다루는 것처럼 설계되어 있어서 그럼
            // 자바 객체에서 값만 바꾸었는데 수정이 가능
            // JPA를 통해서 Entity를 가져오면 JPA가 관리를 하고,
            // 변경이 되었는지 안 되었는지 트랜잭션을 커밋하는 시점에 체크를 함
            // 무언가 변경 된걸 감지하고 UPDATE 쿼리를 실행하고 트랜잭션이 커밋됨
            findMember.setName("HelloJPA");

            // 정상일 때 commit
            tx.commit();
        } catch (Exception e) {
            // 문제가 생기면 rollback
            tx.rollback();
        } finally {
            // 작업이 다 끝나면 entity manager가 닫아줌
            // 내부적으로 Data Connection을 물고 동작하기 때문에 사용 후 꼭 닫아줘야 함
            em.close();
        }

        // 전체 애플리케이션이 끝나면 emf도 종료
        emf.close();
    }

}

/** 
 * Member 테이블 생성
 * create table Member (
 *     id bigint not null,
 *     name varchar(255),
 *     primary key (id)
 * );
 */

/**
 * 주의 사항
 * 1) Entity Manager Factory
 * - Web Application Service 를 운영한다고 했을 때, Web-Server가 올라 오는 시점에 DB당 딱 하나만 생성이 되고 Entity Manager는
 * 2) Entity Manager
 * - 고객의 요청이 올 때마다 계속 썼다가(CRUD), 버렸다가(em.close()) 반복
 * - 따라서 Entity Mangager는 쓰레드 간에 공유를 하면 안 됨(사용하고 버려야 함)
 *   하나를 만들어서 여러 쓰레드에서 같이 사용하면 장애가 발생함
 *   DB 커넥션을 빨리 쓰고 돌려주는 것 처럼
 * 3) JPA의 모든 데이터 변경은 트랜잭션 안에서 실행
 */

