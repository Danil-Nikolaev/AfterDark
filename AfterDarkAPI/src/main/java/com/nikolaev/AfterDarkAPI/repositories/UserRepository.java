package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
}
