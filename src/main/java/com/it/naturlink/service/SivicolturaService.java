package com.it.naturlink.service;

import com.it.naturlink.db.mapper.MapperAll;
import com.it.naturlink.naturlink.api.SivicolturaApiDelegate;
import com.it.naturlink.naturlink.model.Minerale;
import com.it.naturlink.naturlink.model.Prodotto;
import com.it.naturlink.naturlink.model.Sivicoltura;
import com.it.naturlink.repository.SivicoltureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SivicolturaService  implements SivicolturaApiDelegate {

    @Autowired
    SivicoltureRepository sivicoltureRepository;

    @Override
    public ResponseEntity<List<Sivicoltura>> sivicolturaGet() {
        List<Sivicoltura> sivicolturaList = MapperAll.INSTANCE.toSivicolturaList(sivicoltureRepository.findAll());
        if (sivicolturaList.isEmpty())
            return new ResponseEntity<>(sivicolturaList, HttpStatusCode.valueOf(400));
        return ResponseEntity.ok(sivicolturaList);
    }



    public void sivicoP(Sivicoltura sivicoltura) {
        sivicoltureRepository.save(MapperAll.INSTANCE.toSivicolture(sivicoltura));
    }
}
