package com.example.tingeso1;

import com.example.tingeso1.entities.DocumentEntity;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest//(classes = {Tingeso1Application.class}, exclude = DocumentEntity.class)
class Tingeso1ApplicationTests {

	@Test
	void contextLoads() {
	}

}
