package hello.itemservice;

import hello.itemservice.config.JdbcTemplateV1Config;
import hello.itemservice.config.JdbcTemplateV2Config;
import hello.itemservice.repository.ItemRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;

/**
 * {@link SpringBootApplication}
 * : @SpringBootApplication(scanBasePackages = "hello.itemservice.web")
 * : hello.itemservice.web 하위 패키지만 @ComponentScan 을 하겠다.
 * : 나머지 등록할 Bean 들을 수동 등록하기 위한 설정.
 *
 * {@link Profile}
 * : @Profile("local")
 * : 특정 프로필(설정정보; 환경)이 활성화되어 있을 때만 @Bean 등록을 하겠다.
 * : application.properties -> spring.profiles.active=local
 * 		local 이라는 profile을 active 하겠다.
 * 		The following 1 profile is active: "local"
 *
 * : application.properties -> #spring.profiles.active=local(설정하지 않은 경우)
 * 		No active profile set, falling back to 1 default profile: "default"
 * : The following 1 profile is active: "local"
 * : https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles
 */
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
@Import(JdbcTemplateV2Config.class)
@SpringBootApplication(scanBasePackages = "hello.itemservice.web")
public class ItemServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	@Bean
	@Profile("local")
	public TestDataInit testDataInit(ItemRepository itemRepository) {
		return new TestDataInit(itemRepository);
	}

}
