package com.example.jpademo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByName(String name);
	
	Optional<Order> findByRef(String ref);

}
