package com.it.naturlink.repository;

import com.it.naturlink.db.Sivicolture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  SivicoltureRepository extends  JpaRepository<Sivicolture, Integer> {

}
