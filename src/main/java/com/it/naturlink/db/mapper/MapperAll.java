package com.it.naturlink.db.mapper;

import com.it.naturlink.db.*;
import com.it.naturlink.naturlink.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MapperAll {

    MapperAll INSTANCE = Mappers.getMapper(MapperAll.class);

    //mapper agricolo
    List<Agricolo> toAgricoloList(List<Prodotto> agricoloDtoList);
    List<Prodotto> toProdottoList(List<Agricolo> agricoloDtoList);
    Agricolo toAgricolo(Prodotto prodotto);

    //mapper allevamento
    List<Allevamento> toAllevamentoList(List<Animale> animaleList);
    List<Animale> toAnimaleList(List<Allevamento> agricoloDtoList);
    Allevamento toAllevamento( Animale animale);

    //pesca
    List<Pesce> toPesceList(List<Pesca> pesceList);
    List<Pesca> toPescaList(List<Pesce> agricoloDtoList);
    Pesce toPesce( Pesca pesca);
    //minerali
    List<EstrazioneMineraria> toEstrazioneMinerariaList(List<Minerale> mineraleList);
    List<Minerale> toMineraleList(List<EstrazioneMineraria> estrazioneMinerariaList);
    EstrazioneMineraria toEstrazioneMineraria( Minerale minerale);

    //sivicoliture
    List<Sivicolture> toSivivoltureList(List<Sivicoltura> sivicolturaList);
    List<Sivicoltura> toSivicolturaList(List<Sivicolture> sivicoltureList);
    Sivicolture toSivicolture( Sivicoltura sivicoltura);

}
