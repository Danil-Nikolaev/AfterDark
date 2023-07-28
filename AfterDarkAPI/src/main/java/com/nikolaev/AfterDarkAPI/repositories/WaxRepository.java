package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Wax;

@Repository
public interface WaxRepository extends CrudRepository<Wax, Long> {
    
}
