package com.it.naturlink.db.mapper;

import com.it.naturlink.db.Agricolo;
import com.it.naturlink.naturlink.model.Prodotto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MapperAll {

    MapperAll INSTANCE = Mappers.getMapper(MapperAll.class);

    List<Agricolo> toAgricoloList(List<Prodotto> agricoloDtoList);
    List<Prodotto> toProdottoList(List<Agricolo> agricoloDtoList);


   Agricolo toAgricolo(Prodotto prodotto);

}
