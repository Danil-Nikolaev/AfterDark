package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Wick;

@Repository
public interface WickRepository extends CrudRepository<Wick, Long> {

}
