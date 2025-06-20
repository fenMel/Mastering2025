package fr.esic.mastering.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.esic.mastering.entities.Convocation;
import fr.esic.mastering.repository.ConvocationRepository;


@Service
public class ConvocationService {
	@Autowired
    private ConvocationRepository repository;

    public Convocation createConvocation(Convocation convocation) {
        return repository.save(convocation);
}
    public Optional<Convocation> getConvocationById(Long id) {
        return repository.findById(id);
}

}
