package com.it.naturlink.repository;

import com.it.naturlink.db.EstrazioneMineraria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  EstrazioneRepository extends  JpaRepository<EstrazioneMineraria, Integer> {

}

