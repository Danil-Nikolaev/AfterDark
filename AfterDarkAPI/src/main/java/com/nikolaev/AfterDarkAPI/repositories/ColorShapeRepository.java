package com.nikolaev.AfterDarkAPI.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.nikolaev.AfterDarkAPI.models.ColorShape;

@Repository
public interface ColorShapeRepository extends CrudRepository<ColorShape, Long> {
    
}
