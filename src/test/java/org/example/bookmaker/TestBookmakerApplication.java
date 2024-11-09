package org.example.bookmaker;

import org.springframework.boot.SpringApplication;

public class TestBookmakerApplication {

	public static void main(String[] args) {
		SpringApplication.from(BookmakerApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
