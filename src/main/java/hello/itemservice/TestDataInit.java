package hello.itemservice;

import hello.itemservice.domain.Item;
import hello.itemservice.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@Slf4j
@RequiredArgsConstructor
public class TestDataInit {

    private final ItemRepository itemRepository;

    /**
     * 확인용 초기 데이터 추가
     * {@link EventListener}
     * : @EventListener(ApplicationReadyEvent.class)
     * : h.itemservice.ItemServiceApplication     : Started ItemServiceApplication in 2.981 seconds (JVM running for 4.777)
     * : Spring Container 가 초기화를 끝내고(위의 로그가 찍힌 이후), 실행 준비가 완료되었을 때 발생하는 이벤트이다.
     *
     * : 이 기능 대신 @PostConstruct 를 사용할 경우 AOP 같은 부분이 아직 다 처리되지 않은 시점에 호출될 수 있기 때문에, 간혹 문제가 발생할 수 있다.
     * : 예를 들어서 @Transactional 과 관련된 AOP 가 적용되지 않은 상태로 호출될 수 있다.
     * : @EventListener(ApplicationReadyEvent.class) 는 AOP를 포함한 스프링 컨테이너가 완전히 초기화 된 이후에 호출되기 때문에 이런 문제가 발생하지 않는다.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        log.info("test data init");
        itemRepository.save(new Item("itemA", 10000, 10));
        itemRepository.save(new Item("itemB", 20000, 20));
    }

}
