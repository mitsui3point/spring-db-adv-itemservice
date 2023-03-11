package hello.itemservice;

import hello.itemservice.config.SpringDataJpaConfig;
import hello.itemservice.repository.ItemRepository;
import lombok.extern.slf4j.Slf4j;
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
 * <p>
 * {@link Profile}
 * : @Profile("local")
 * : 특정 프로필(설정정보; 환경)이 활성화되어 있을 때만 @Bean 등록을 하겠다.
 * : application.properties -> spring.profiles.active=local
 * local 이라는 profile을 active 하겠다.
 * The following 1 profile is active: "local"
 * <p>
 * : application.properties -> #spring.profiles.active=local(설정하지 않은 경우)
 * No active profile set, falling back to 1 default profile: "default"
 * : The following 1 profile is active: "local"
 * : https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.profiles
 */
//@Import(MemoryConfig.class)
//@Import(JdbcTemplateV1Config.class)
//@Import(JdbcTemplateV2Config.class)
//@Import(JdbcTemplateV3Config.class)
//@Import(MyBatisConfig.class)
//@Import(JpaConfig.class)
@Import(SpringDataJpaConfig.class)
@Slf4j
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

	/**
	 * {@link Profile} 이 테스트일 경우에만 데이터소스를 스프링 빈으로 등록한다.(테스트 케이스에서만 사용하겠다)
	 * : jdbc:h2:mem:db; 데이터소스를 만들때 이렇게만 적으면 임베디드 모드(메모리 모드) 로 동작하는 H2 데이터베이스를 사용할 수 있다.
	 * : DB_CLOSE_DELAY=-1; 임베디드 모드에서는 데이터베이스 커넥션 연결이 모두 끊어지면 데이터베이스도 종료되는데, 그것을 방지하는 설정이다.
	 *
	 * 이 코드 없이도 test/application.properties 에 별다른 DB 설정이 없게 되면, 테스트 수행시 스프링 부트가 자동적으로 임베디드 데이터베이스 설정을 해 준다.
	 * : 임베디드 데이터베이스에 대한 스프링 부트의 더 자세한 설정 공식 매뉴얼
	 * > https://docs.spring.io/spring-boot/docs/current/reference/html/
	 * : 임베디드 데이터베이스 커넥션 로그
	 * data.html#data.sql.datasource.embedded
	 * 2023-03-09 14:46:10.788 DEBUG 6464 --- [           main] o.s.jdbc.support.JdbcTransactionManager  : Acquired Connection [HikariProxyConnection@1455696228 wrapping conn0: url=jdbc:h2:mem:b1fc9737-4c6a-4e93-8fc7-05f3a85897b1 user=SA] for JDBC transaction
	 * 2023-03-09 14:46:10.794 DEBUG 6464 --- [           main] o.s.jdbc.support.JdbcTransactionManager  : Switching JDBC Connection [HikariProxyConnection@1455696228 wrapping conn0: url=jdbc:h2:mem:b1fc9737-4c6a-4e93-8fc7-05f3a85897b1 user=SA] to manual commit
	 */
//	@Bean
//	@Profile("test")
//	public DataSource dataSource() {
//		log.info("메모리 데이터베이스 초기화");
//		DriverManagerDataSource dataSource = new DriverManagerDataSource();
//		dataSource.setDriverClassName("org.h2.Driver");
//		dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
//		dataSource.setUsername("sa");
//		dataSource.setPassword("");
//		return dataSource;
//	}
}
