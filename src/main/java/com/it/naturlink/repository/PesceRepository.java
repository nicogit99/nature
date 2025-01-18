package com.it.naturlink.repository;

import com.it.naturlink.db.Pesce;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  PesceRepository extends  JpaRepository<Pesce, Integer> {

}

