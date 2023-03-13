package hello.itemservice.repository.jpa;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import hello.itemservice.repository.ItemSearchCond;
import hello.itemservice.repository.ItemUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 예외 변환
 * 스프링 데이터 JPA도 스프링 예외 추상화를 지원한다.
 * 스프링 데이터 JPA가 만들어주는 프록시(SpringDataJpaItemRepository 의 구현체)에서 이미 예외 변환을 처리하기 때문에, @Repository 와 관계없이 예외가 변환된다.
 */
//@Repository
@Transactional
@RequiredArgsConstructor
public class JpaItemRepositoryV2 implements ItemRepository {
    private final SpringDataJpaItemRepository springDataJpaItemRepository;
    @Override
    public Item save(Item item) {
        return springDataJpaItemRepository.save(item);
    }

    @Override
    public void update(Long itemId, ItemUpdateDto updateParam) {
        Item item = springDataJpaItemRepository.findById(itemId).orElseGet(null);
        item.setItemName(updateParam.getItemName());
        item.setPrice(updateParam.getPrice());
        item.setQuantity(updateParam.getQuantity());
    }

    @Override
    public Optional<Item> findById(Long id) {
        return springDataJpaItemRepository.findById(id);
    }

    @Override
    public List<Item> findAll(ItemSearchCond cond) {
        String itemName = cond.getItemName();
        Integer maxPrice = cond.getMaxPrice();
        boolean isNotEmptyItemName = StringUtils.hasText(itemName);
        boolean isNotNullMaxPrice = maxPrice != null;
        List<Item> result = new ArrayList<>();

        if (isNotEmptyItemName && isNotNullMaxPrice) {
            result = springDataJpaItemRepository.findItems("%"+itemName+"%", maxPrice);
        }
        if (isNotEmptyItemName && !isNotNullMaxPrice) {
            result = springDataJpaItemRepository.findByItemNameLike("%"+itemName+"%");
        }
        if (!isNotEmptyItemName && isNotNullMaxPrice) {
            result = springDataJpaItemRepository.findByPriceLessThanEqual(maxPrice);
        }
        if (!isNotEmptyItemName && !isNotNullMaxPrice) {
            result = springDataJpaItemRepository.findAll();
        }
        return result;
    }
}