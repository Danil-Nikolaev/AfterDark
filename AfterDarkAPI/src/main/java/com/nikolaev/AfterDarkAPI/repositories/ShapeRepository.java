package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.Shape;

@Repository
public interface ShapeRepository extends CrudRepository<Shape, Long> {

}
