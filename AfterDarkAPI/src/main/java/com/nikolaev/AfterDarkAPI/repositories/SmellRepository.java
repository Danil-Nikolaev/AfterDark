package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Smell;

@Repository
public interface SmellRepository extends CrudRepository<Smell, Long> {

}
