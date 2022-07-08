package com.example.jpademo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderServiceTest {

	@Autowired
	private OrderService service;

	@Autowired
	private OrderRepository repository;
	
	@Spy
	private DummyRestCall dummyRestCall;

	private static final List<Long> RANDOM_IDS = new ArrayList<>();

	@Test
	void testConcurrency() throws InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(5);

		for (int i = 0; i < 10; i++) {
			long random = generateUniqueRandom();
			long random2 = generateUniqueRandom();

			Order order = new Order();
			order.setName(String.valueOf(random));

			Order order2 = new Order();
			order2.setName(String.valueOf(random2));
			order2.setRef(String.valueOf(random));

			executor.execute(() -> service.execute(order));
			executor.execute(() -> service.execute(order2));
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.MINUTES);

		assertEquals(0, repository.findAll().size());
		verify(dummyRestCall, times(20)).callRest(Mockito.any());
	}

	private long generateUniqueRandom() {
		long random = (long) (Math.random() * 1000);
		while (RANDOM_IDS.contains(random)) {
			random = (long) (Math.random() * 1000);
		}
		RANDOM_IDS.add(random);
		return random;
	}
}
