package jpql;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

/**
 * TypeQuery : 반환 타입이 명확할떄
 * Query : 반환 타입이 명확하지 않을때
 * <p>
 * 실무에서 묵시적(내부)조인 대신에 명시적 조인 사용
 * 조인은 sql 튜닝에 중요 포인트
 * 묵시적 조인은 조인이 일어나는 상황을 한눈에 파악하기 어려움
 * <p>
 * 패치 조인
 * jpql 에서 성능 최적활르 위해 제공하는 기능
 * 연관된 엔티티나 컬렉션은 SQL 한번에 함께 조회하는 기능
 * join fetch 명령어 사용
 * <p>
 * 패치 조인의 특징과 한계
 * <p>
 * 패치 조인 대상에는 별칭을 줄 수 없다.
 * 하이버네이트는 가능, 가급적 사용 X
 * <p>
 * 둘 이상의 컬렉션은 패치 조인 할 수 없다
 * <p>
 * 컬렉션을 패치 조인하면 페이징 API 를 사용 할 수 없다.
 * 알대일, 다대일 같은 단일 값 연관 필드들은 패치 조인해도 페이징 가능
 * 하이버네이트는 경고 로그를 남기고 메모리에서 페이징(매우 위험)
 * <p>
 * 페치 조인의 특징과 한계
 * 연관된 엔티티들을 SQL 한번으로 조회- 성능 최적화
 * 엔티티에 직접 적용하는 글로벌 로딩 전략보다 우선함
 * 실무에서 글로벌 로딩 전략은 모두 지연로딩
 * 최적화가 필요한 곳은 페치 조인 적용용
 */
public class JpaMain {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            Team teamA = new Team();
            teamA.setName("팀A");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("팀B");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setUsername("회원1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setUsername("회원2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setUsername("회원3");
            member3.setTeam(teamB);
            em.persist(member3);


            // FLUSH 자동 호출
            int resultCount = em.createQuery("update Member m set m.age=20").executeUpdate();

            em.clear();
            Member findMember = em.find(Member.class, member1.getId());
            System.out.println("findMember = " + findMember.getAge());

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }

        emf.close();
    }

}
