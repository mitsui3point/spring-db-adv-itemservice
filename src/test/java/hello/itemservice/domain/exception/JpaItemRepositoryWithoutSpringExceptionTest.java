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
import org.springframework.context.annotation.Bean;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@Transactional
@SpringBootTest
class JpaItemRepositoryWithoutSpringExceptionTest {

    @Autowired
    ItemExceptionRepository itemRepository;

    @Test
    void withoutSpringExceptionTest() {
        assertThatThrownBy(() -> {
            assertThat(itemRepository).isInstanceOf(JpaItemExceptionRepository.class);
            log.info("itemRepository={}", itemRepository.getClass());
            itemRepository.findAll(new ItemSearchCond("itemName", 10000));
        })
                .isInstanceOf(IllegalArgumentException.class)//Jpa Exception
                .isNotInstanceOf(InvalidDataAccessApiUsageException.class);//Spring Translated Exception
    }

    @TestConfiguration
    @RequiredArgsConstructor
    static class TestConfig {
        @Bean
        public ItemExceptionRepository itemExceptionRepository(EntityManager em) {
            return new JpaItemExceptionRepository(em);
        }
    }

    @TestComponent
    @RequiredArgsConstructor
    static class JpaItemExceptionRepository implements ItemExceptionRepository {
        private final EntityManager em;
        public List<Item> findAll(ItemSearchCond cond) {
            return em.createQuery("selectxxx i from Item i", Item.class)
                    .getResultList();
        }

    }
}
