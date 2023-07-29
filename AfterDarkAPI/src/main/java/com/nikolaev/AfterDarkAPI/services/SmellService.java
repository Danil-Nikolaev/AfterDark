package com.nikolaev.AfterDarkAPI.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.nikolaev.AfterDarkAPI.models.Smell;
import com.nikolaev.AfterDarkAPI.repositories.SmellRepository;

@Service
public class SmellService {

    private SmellRepository smellRepository;

    public SmellService(SmellRepository smellRepository) {
        this.smellRepository = smellRepository;
    }

    public List<Smell> index() {
        Iterable<Smell> iterableSmell = smellRepository.findAll();
        List<Smell> result = new ArrayList<>();
        iterableSmell.forEach(result::add);
        return result;
    }

    public Smell show(long id) {
        return smellRepository.findById(id).orElse(null);
    }

    public Smell save(Smell smell) {
        return smellRepository.save(smell);
    }

    public Smell update(Smell smell, long id) {
        Optional<Smell> optionalSmell = smellRepository.findById(id);
        if (optionalSmell.isPresent()) {
            Smell existingSmell = optionalSmell.get();
            existingSmell.setName(smell.getName());
            existingSmell.setDescription(smell.getDescription());
            existingSmell.setPrice(smell.getPrice());
            existingSmell.setQuanity(smell.getQuanity());
            return smellRepository.save(existingSmell);
        }

        return null;
    }

    public void delete(long id) {
        smellRepository.deleteById(id);
    }

}
