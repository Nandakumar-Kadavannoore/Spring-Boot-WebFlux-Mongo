package com.example.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.example.entity.User;

public interface UserRepository extends ReactiveMongoRepository<User, String> {

}
