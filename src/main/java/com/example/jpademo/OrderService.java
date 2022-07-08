package com.example.jpademo;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private DummyRestCall dummyRest;
	
	@Transactional
	public Order execute(Order order) {
		orderRepository.save(order);
		if (order.getRef() != null) { // { id:1, name:1, ref: 2 }
			Optional<Order> findByName = orderRepository.findByName(order.getRef());
			findByName.ifPresent(o -> {
				dummyRest.callRest(order);
				orderRepository.delete(o);
				orderRepository.delete(order);
			});
		} else { // { id:2, name:2, ref: null }
			Optional<Order> otherOrder = orderRepository.findByRef(order.getName());
			otherOrder.ifPresentOrElse(o -> {
				dummyRest.callRest(o);
				dummyRest.callRest(order);
				orderRepository.delete(o);
				orderRepository.delete(order);
			}, () -> {
				dummyRest.callRest(order);
			});
		}
		return null;
	}
}
