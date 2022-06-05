package entitymapping;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

/*
    JPA에서 가장 중요한 2가지
    1. 매커니즘 측면 : 영속성 컨텍스트, JPA 내부 동작 방식
    2. 설계적인 측면 : 객체와 관계형 DataBase를 어떻게 매핑을 해서 쓰는지
*/

    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("entitymapping");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            List<Member> result = em.createQuery("select m from Member as m", Member.class).getResultList();
            for (Member member : result ) {
                System.out.println("member = " + member.getId() + ", " + member.getName());
            }
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}
