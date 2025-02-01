package com.it.naturlink.repository;

import com.it.naturlink.db.Agricolo;
import com.it.naturlink.db.Allevamento;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgricoloRepository extends JpaRepository<Agricolo, Integer> {

}

