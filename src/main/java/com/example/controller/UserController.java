/**
 * User Controller Implementation
 * @author Nandakumar.K
 * @date 27 April 2018
 */
package com.example.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.User;
import com.example.repository.UserRepository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/user")
@CrossOrigin
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@PostMapping
	public Mono<User> addUser(@Valid @RequestBody User user) {
		return  userRepository.save(user);
	}
	
	@PutMapping("/{id}")
	public Mono<ResponseEntity<User>> updatUser(@PathVariable(value = "id") String id,
			@Valid @RequestBody User user) {
		return userRepository.findById(id).flatMap( existingUser -> {
			existingUser.setPhoneNumber(user.getPhoneNumber());
			return userRepository.save(existingUser);
		}).map( updatedUser -> new ResponseEntity<>(updatedUser, HttpStatus.OK))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/{id}")
	public Mono<ResponseEntity<User>> getUserById(@PathVariable(value = "id") String id) {
		return userRepository.findById(id).map( savedUser ->  ResponseEntity.ok(savedUser))
				.defaultIfEmpty(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Void>> deleteUserById(@PathVariable(value = "id") String id) {
		return userRepository.findById(id).flatMap(user -> userRepository.delete(user))
				.then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
				.defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping
	public Flux<User> getAllUsers() {
		return userRepository.findAll();
	}
	

}
