package com.example.jpademo;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DummyRestCall {

	public void callRest(Order o) {
		LoggerFactory.getLogger(DummyRestCall.class).info("Id: {} Name: {}", o.getId(), o.getName());
	}
}
