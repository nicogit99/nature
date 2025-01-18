package com.it.naturlink.service;

import com.it.naturlink.db.mapper.MapperAll;
import com.it.naturlink.naturlink.api.MineraliApiDelegate;
import com.it.naturlink.naturlink.model.Minerale;
import com.it.naturlink.repository.EstrazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstrazioneService  implements MineraliApiDelegate {


    @Autowired
    EstrazioneRepository estrazioneRepository;


    @Override
    public ResponseEntity<List<Minerale>> mineraliGet() {
        List<Minerale> mineraleList = MapperAll.INSTANCE.toMineraleList(estrazioneRepository.findAll());
        if (mineraleList.isEmpty())
            return new ResponseEntity<>(mineraleList, HttpStatusCode.valueOf(400));
        return ResponseEntity.ok(mineraleList);
    }


}
