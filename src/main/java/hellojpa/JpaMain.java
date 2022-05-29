package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

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

            /**
             * JPQL
             * 가장 단순한 조회는 entity manager를 통해 find 메서드 인자에 type과 pk값을 넣으면 단건조회가 쉽게 이루어지나,
             * 단건 조회가 아닌 리스트 조회, 조인 즉 내가 원하는 데이터를 최적화해서 가져오기 위해 필요한 것
             * */
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    // 페이지네이션 해당 메서드를 선언하여 실행할 경우 limit과 offset이 자동 반영
                    .setFirstResult(1) // 1번 데이터 부터
                    .setMaxResults(5)  // 5개의 데이터를 호출
                    .getResultList();
            /**
             * createQuery의 sql문을 보면 기존 sql문과 조금 다름
             * JPA 입장에서는 코드를 짤 때, 테이블 대상으로 코드를 짜지 않고,
             * Member 객체(entity)를 대상으로 쿼리를 작성.
             * 따라서, JPA의 대상은 테이블이 아닌 객체
             * */
            for (Member member : result ) {
                System.out.println("member = " + member.getId() + ", " + member.getName());
            }

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

/**
 * JPQL 이란?
 * - JPA를 사용하면 Entity 객체를 중심으로 개발
 * - 그런데 문제는 검색 쿼리
 * - 그래서 검색을 할 때도 테이블이 아닌 객체를 대상으로 쿼리를 짤 수 있는 문법
 * - 모든 DB데이터를 객체로 변환해서 검색하는 것은 불가능
 * - 애플리케이션이 필요한 데이터만 DB에서 불러오려면 결국 검색조건이 포함된 SQL을 날려야 함
 * - 실제 RDB에 있는, 물리적인 대상인 쿼리를 날려버리면 그 DB에 종속적으로 설계가 되어버림
 * - 그래서 그게 아니라 Entity 객체를 대상으로 쿼리를 사용할 수 있는 JPQL이란 것이 제공됨
 * 1) JPA는 SQL을 추상화한 JPQL이라는 객체 지향 쿼리 언어를 제공
 * 2) SQL과 문법 유사, SELECT, FROM, WHERE, GROUP BY, HANVING, JOIN 지원
 * 3) 차이점 : JPQL은 엔티티 객체를 대상으로 쿼리(SQL은 DB 테이블을 대상으로 쿼리)
 *    장점 : DB를 바꿔도 JPQL을 변경할 필요없이 그대로 사용해도 실행됨
 *          왜냐하면 SQL을 추상화해서 특정 DB의 SQL에 의존하지 않음
 * 4) JPQL을 한마디로 정의하면 객체 지향 SQL
 */

// 22-05-29 : H2 DB 사용법, JPA 라이브러리 세팅, persistence.xml 에 대해, EntityManagerFactory(DB당 1개),

/**
 * 22-05-29
 * 1) H2 DB 사용
 * 2) JPA 라이브러리 세팅
 * 3) persistence.xml
 *  - DB설정, name설정
 * 4) EntityManagerFactory
 *  - DB당 1개만 생성
 *  - name을 통해 설정파일의 경로를 읽어와서 만듬
 * 5) EntityManager
 *  - 고객의 요청이 올 때마다 해당 객체를 통해 작업이 진행
 *  - 모든 데이터 변경은 트랜잭션 안에서 이루어져야 함(실제 필요한 로직 선언)
 *    (단순 데이터 조회는 트랜잭션 선언 안 해도 DB내부에서 트랜잭션 처리하기에 조회 로직이 가능)
 *  - transaction commit() 을 하지 않을 경우 변경이 되지 않음
 *  - 모든 로직이 끝난 후에는 entity manager를 꼭 close()해야 반환이 됨
 *  - 그리고 (WebApplicationService일 경우)WAS가 내려 갈 때, 꼭 EntityManagerFactory를 close() 닫아줘야
 *    내부적으로 커넥션 풀링이나 리소스가 릴리즈 됨
 * */
