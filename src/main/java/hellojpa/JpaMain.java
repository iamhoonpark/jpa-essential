package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaMain {

    public static void main(String[] args) {
        // 시작
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        // EntityManager를 꺼내고 실제 동작하는 코드를 작성하는 부분(CRUD)

        // 종료
        em.close();
        emf.close();
    }

}
