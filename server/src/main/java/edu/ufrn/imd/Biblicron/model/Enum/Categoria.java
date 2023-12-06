package edu.ufrn.imd.Biblicron.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Categoria {

    FUTEBOL("Futebol"),
    NATACAO("Natação"),
    TENIS("Tênis"),
    CORRIDA("Corrida"),
    MUSCULACAO("Musculação"),
    VOLEI("Vôlei");


    @Getter
    private final String categoria;
}
