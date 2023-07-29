package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Gypsum;

@Repository
public interface GypsumRepository extends CrudRepository<Gypsum, Long>{
    
}
