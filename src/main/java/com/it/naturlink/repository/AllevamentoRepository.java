package com.it.naturlink.repository;

import com.it.naturlink.db.Allevamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  AllevamentoRepository extends  JpaRepository<Allevamento, Integer> {


    
}

