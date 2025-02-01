package fr.esic.mastering.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.esic.mastering.entities.Convocation;

public interface ConvocationRepository extends JpaRepository<Convocation, Long> {

}