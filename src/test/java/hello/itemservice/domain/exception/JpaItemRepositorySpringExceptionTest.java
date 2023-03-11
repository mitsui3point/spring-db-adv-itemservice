package hello.itemservice.domain.exception;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cglib.SpringCglibInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.dao.support.PersistenceExceptionTranslator;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * {@link Repository} 의 기능
 * : @Repository 가 붙은 클래스는 컴포넌트 스캔의 대상이 된다.
 * : @Repository 가 붙은 클래스는 예외 변환 AOP의 적용 대상이 된다.
 * 스프링과 JPA를 함께 사용하는 경우 스프링은 JPA 예외 변환기 ( {@link PersistenceExceptionTranslator} )를 등록한다.
 * 예외 변환 AOP 프록시는 JPA 관련 예외가 발생하면 JPA 예외 변환기를 통해 발생한 예외를 스프링 데이터 접근 예외로 변환한다.
 * 결과적으로 리포지토리에 @Repository 애노테이션만 있으면 스프링이 예외 변환을 처리하는 AOP 를 만들어준다.
 *
 * 참고
 * : 스프링 부트는 {@link PersistenceExceptionTranslationPostProcessor} 를 자동으로 등록하는데, 여기에서 @Repository 를 AOP 프록시로 만드는 어드바이저가 등록된다.
 * : 복잡한 과정을 거쳐서 실제 예외를 변환하는데, 실제 JPA 예외를 변환하는 코드는 {@link EntityManagerFactoryUtils#convertJpaAccessExceptionIfPossible(RuntimeException)} 이다.
 *
 * 참고
 * : JdbcTemplate -> spring 에 포함된 기술이기 때문에 Exception 변환을 해줌.
 * : MyBatis -> MyBatis Spring Module(MyBatis 패키지 내부에 존재하는 module) 에서 해줌.
 * : Jpa -> EntityManager 는 순수 Jpa 기능이므로 Spring 의 기능을 포함하지 않아, @Repository 가 추가적으로 AOP 적용을 하여 Exception 변환을 해야 한다.
 */
@Slf4j
@Transactional
@SpringBootTest
class JpaItemRepositorySpringExceptionTest {
    @Autowired
    ItemExceptionRepository itemRepository;

    @Test
    void springExceptionTranslateTest() {
        assertThatThrownBy(() -> {
            assertThat(itemRepository).isInstanceOf(JpaItemExceptionRepository.class);//CGLIB 인스턴스로 변경요!
            log.info("itemRepository={}", itemRepository.getClass());
            itemRepository.findAll(new ItemSearchCond("itemName", 10000));
        })
                .isInstanceOf(InvalidDataAccessApiUsageException.class)//Spring Translated Exception
                .isNotInstanceOf(IllegalArgumentException.class);//Jpa Exception
    }

    @TestConfiguration
    @RequiredArgsConstructor
    static class TestConfig {
        @Bean
        public ItemExceptionRepository itemExceptionRepository(EntityManager em) {
            return new JpaItemExceptionRepository(em);
        }
    }

    @Repository
    @TestComponent
    @RequiredArgsConstructor
    static class JpaItemExceptionRepository implements ItemExceptionRepository {
        private final EntityManager em;
        public List<Item> findAll(ItemSearchCond cond) {
            String sql = "selectxxx i from Item i";

            TypedQuery<Item> query = em.createQuery(sql, Item.class);
            return query.getResultList();
        }
    }
}
