package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class JpaMain {
    
    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("jpashop");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 주문을 한 유저를 찾고 싶을 때
            Order order = em.find(Order.class, 1L);
            // getMemberId로 memberId를 세팅해서
            Long memberId = order.getMemberId();
            // 또 em을 통해 memberId를 find 메서드로 찾음
            em.find(Member.class, memberId);

            // 이러한 과정은 객체지향의 의미가 아님
            // 객체지향 : id만 있는 것이 아니라 바로 객체를 찾아서 값을 끄집어 낼 수 있게 해야 함
            // Member Entity 객체에 Member 자료형을 가진 멤버필드를 선언 후 아래와 같이 코딩
            Member findMember = order.getMember();
            findMember.getCity();

            // 이를 해결하기 위한 연관관계가 반드시 필요함
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
*     데이터 중심 설계의 문제점
*    - 현재 방식은 객체 설계를 테이블 설계에 맞춘 방식
*    - 테이블의 외래키를 객체에 그대로 가져옴
*    - 객체 그래프 탐색 불가능
*    - 참조가 없으므로 UML도 잘못됨
* 
*  */
