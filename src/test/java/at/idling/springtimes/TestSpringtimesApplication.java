package at.idling.springtimes;

import org.springframework.boot.SpringApplication;

public class TestSpringtimesApplication {

	public static void main(String[] args) {
		SpringApplication.from(SpringtimesApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
