package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/** 3. 영속성 컨텍스트 : 이점에 대해  */

public class PersistenceContext2 {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {

            /** 4. 엔티티 조회, 1차 캐시
            Member member = new Member();
            member.setId(101L);
            member.setName("HelloJPA");

            System.out.println("------------BEFORE------------");
            em.persist(member); // 1차 캐시에 저장
            System.out.println("-------------AFTER------------");

            // 1차 캐시에 저장된 것을 조회하기 때문에 select 구문이 실행되지 않음
            Member findMember = em.find(Member.class, 101L);
            System.out.println("findMember.getId : " + findMember.getId());
            System.out.println("findMember.getName : " +findMember.getName());
            
            // console : BEFORE > AFTER > (SELECT 쿼리 실행 안 함)101, HelloJPA > INSERT 쿼리 실행

            // 여기에서도 1차 캐시에서 저장된 것을 당겨서 조회하기 때문에 SELECT 쿼리를 실행하지 않음
            Member findMember2 = em.find(Member.class, 101L);
            */

            /** 5. 영속 엔티티의 동일성 보장(같은 트랜잭션 내)
            Member findMember = em.find(Member.class, 101L);
            Member findMember2 = em.find(Member.class, 101L);
            System.out.println("result = " + (findMember == findMember2)); // true 반환 */

            /** 6. 엔티티 등록 : 트랜잭션을 지원하는 쓰기 지연 SQL 저장소
            Member member1 = new Member(150L, "A");
            Member member2 = new Member(160L, "B");
            em.persist(member1);
            em.persist(member2);
            System.out.println(" --- 여기까지 쿼리를 '쓰기지연SQL저장소'에 차곡차곡 쌓아놓고 이 println 이후에 쿼리실행(=flush) --- ");*/

            /** 7. 엔티티 수정 : 변경 감지(Dirty Checking) */
            Member member = em.find(Member.class, 150L);
            // 자바 다루듯 컬렉션의 값만 바꾸었음에도 불구하고 자동으로 값의 변경을 감지 후 update 쿼리를 실행
            member.setName("ZZZZZ");

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}/*

 ** 4. 엔티티 조회, 1차 캐시
 - 영속 컨텍스트(entityManager)라는 큰 박스 안에 '1차 캐시' 라는 공간이 존재
  · 1차 캐시라는 곳에 find 메서트를 통해 값을 호출할 수 있고, 값이 없다면 DB에서 조회해서 1차 캐시에 저장
  · (개인적 의견) 그러니까 java의 static 같이 메모리에 공간이 할당되고 서버를 내리기 전 까지 계속 할당 되어 있는 것 같음
 - 영속 컨텍스트(entityManager)는 DataBase Transaction 단위(한 트랙)로 만들고 DBT가 끝날 때, 같이 종료시켜버림
  · 즉, 고객의 요청이 들어와서 비지니스 로직 한 개를 끝내버리면 영속성 컨텍스트를 지우는 것(1차 캐시도 날라감)
 - 굉장히 짧은 찰나에서만 1차 캐시의 존재하기 때문에 큰 성능 이점이라고 할 수는 없음
  · 비지니스 로직이 굉장히 복잡할 때, 같은 것을 조회 할 수도 있기 때문에 이점이 있음
  · 여러 명의 고객이 사용하는 캐시가 아니고 애플리케이션 전체를 공유하는 캐시는 JPA나 Hibernate에선 2차 캐시라 함

 ** 5. 영속 엔티티의 동일성 보장
 - 1차 캐시로 반복 가능한 읽기(REPEATABLE READ) 등급의 트랜잭션 격리 수준을 DB가 아닌 애플리케이션 차원에서 제공
  · 즉, java collection에서 가져올 때 주소(레퍼런스)가 같은 것 처럼, JPA가 영속 엔티티의 동일성을 보장해줌(== 비교의 보장)

 ** 6. 엔티티 등록 : 트랜잭션을 지원하는 쓰기 지연 SQL 저장소
 tx.begin(); // 트랜잭션 시작
 em.persist(memberA);
 em.persist(memberB); // 여기까지 ISNERT 쿼리를 DB에 보내지 않고
 tx.commit(); // 트랜잭션 커밋하는 순간 DB에 INSERT 쿼리를 보냄
 - 영속 컨텍스트(entityManager)라는 큰 박스 안에 '쓰기 지연 SQL 저장소' 라는 공간이 존재
 - 커밋을 하는 순간 (SQL)요청들을 쌓아놓은 '쓰기 지연 SQL 저장소' 에 있던 애들을 DB로 플러쉬(flush, 쏟아내다)
  · 즉, 기본적으로는 JPA가 요청들을 쌓아놓고 커밋하는 순간 INSERT 쿼리들을 DB에 보냄
 - 만약 저장소가 없이 SQL을 일일이 flush 한다면 최적화 할 수 있는 여지 자체가 없음
  · DB에 한방에 보낼 수 있는 이점이 있음(=JDBC Batch)
  · hibernate 에는 batch_size 를 value의 크기를 설정할 경우, 해당 value 사이즈 만큼 한번에 모아서 DB 한방에
    네트워크로 이 쿼리를 보내고 DB에 commit 함
 - 그래서 모았다가 한번에 보낼 수 있는 기능(=버퍼링 기능) 제공
 - 실전에서 같은 로직을 여러번 저장하는 기능은 특별한 배치쿼리 짜지 않는 이상 많지 않아서 큰 이점은 아니지만
   중요한 것은 이런 이점들을 얻는 것
 - JPA를 사용하므로써 성능이 떨어질 것에 대한 우려가 있다면 잘만 활용하면 옵션 하나로 기본적으로 성능을 먹고 들어갈 수 있음
  · 직접 Query문을 작성해서 모아놓고 commit 직전에 찍는 것은 구현하기 정말 힘듦

 ** 7. 엔티티 수정 : 변경 감지(Dirty Checking)
 - JPA 목적은 JAVA 컬렉션 다루듯 객체를 다루는 것
  · 자바 컬렉션에서 값을 꺼내서 값을 변경하면 끝. 다시 컬렉션에 집어넣지 않음
  · 그래서 persist() 메서드를 사용하지 않고 PK로 값을 찾아온 후에 setter를 통해 값을 수정만 해도
    '변경감지'를 통해 update query가 자동으로로실행됨
 - 변경감지 내부 매커니점에 대해
  1) JPA는 DataBase Trasaction을 commit하는 시점에 내부적으로 flush() 라는 것을 호출하게 됨
  2) 1차 캐시에 있는 엔티티와 스탭샷을 비교함
  · @ID: PK, Entity: 객체, 스냅샷: 값을 읽어온 최초 시점에 상태를 screen shot
  · 트랜잭션이 커밋되는 시점에 내부적으로 flush를 호출하면서 JPA가 전부를 비교함
  3) 값이 바뀌었다면 update 쿼리를 '쓰기 지연 SQL 저장소'에 만들어 놓고 update 쿼리를 DB에 반영 후 커밋
  4) 1~3번의 행위가 변경 감지 매커니즘의 동작원리이자, 변경 감지라고 함

 ** 8. 엔티티 삭제 : .remove() */