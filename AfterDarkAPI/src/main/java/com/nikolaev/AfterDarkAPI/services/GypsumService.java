package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Gypsum;
import com.nikolaev.AfterDarkAPI.repositories.GypsumRepository;

@Service
public class GypsumService {
    private GypsumRepository gypsumRepository;

    public GypsumService(@Autowired GypsumRepository gypsumRepository) {
        this.gypsumRepository = gypsumRepository;
    }

    public List<Gypsum> index() {
        List<Gypsum> result = new ArrayList<>();
        Iterable<Gypsum> source = gypsumRepository.findAll();
        source.forEach(result::add);
        return result;
    }

    public Gypsum show(long id) {
        return gypsumRepository.findById(id).orElse(null);
    }

    public Gypsum save(Gypsum gypsum) {
        return gypsumRepository.save(gypsum);
    } 

    public Gypsum update(Gypsum gypsum, long id) {
        Optional<Gypsum> optionalGypsum = gypsumRepository.findById(id);
        if (optionalGypsum.isPresent()) {
            Gypsum existingGypsum = optionalGypsum.get();
            existingGypsum.setName(gypsum.getName());
            existingGypsum.setDescription(gypsum.getDescription());
            existingGypsum.setPrice(gypsum.getPrice());
            existingGypsum.setQuanity(gypsum.getQuanity());
            return existingGypsum; 
        }
        return null;
    }

    public void delete(long id) {
        gypsumRepository.deleteById(id);
    }
}
