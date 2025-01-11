package com.it.naturlink.repository;

import com.it.naturlink.db.Agricolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgricoloRepository extends JpaRepository<Agricolo, Integer> {

}
