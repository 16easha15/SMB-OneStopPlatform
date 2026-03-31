package com.example.smbone.repositories;

import com.example.smbone.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class UserRespository implements JpaRepository<User, Integer> {
}