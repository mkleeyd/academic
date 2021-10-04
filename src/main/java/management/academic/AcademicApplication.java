package management.academic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AcademicApplication {

	// 단위 테스트 위한 초기 데이터 세팅
//	private UsersRepository repository;
//
//	@PostConstruct
//	public void initUsers() {
//		List<Users> users = Stream.of(
//				new Users(101, "javatechie", "password", "javatechie@gmail.com"),
//				new Users(102, "user1", "pwd1", "user1@gmail.com"),
//				new Users(103, "user2", "pwd2", "user2@gmail.com"),
//				new Users(104, "user3", "pwd3", "user3@gmail.com")
//		).collect(Collectors.toList());
//		repository.saveAll(users);
//	}

	public static void main(String[] args) {
		SpringApplication.run(AcademicApplication.class, args);
	}
}
