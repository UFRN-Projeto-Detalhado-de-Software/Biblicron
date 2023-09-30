package edu.ufrn.imd.Biblicron.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Genero {

    FICCAO_CIENTIFICA("Ficção Científica"),
    FANTASIA("Fantasia"),
    ROMANCE("Romance"),
    SUSPENSE("Suspense"),
    AVENTURA("Aventura"),
    BIOGRAFIA("Biografia"),
    DIDATICOS("Didáticos"),
    HISTORIA_EM_QUADRINHOS("História em Quadrinhos"),
    CLASSICOS("Clássicos");

    @Getter
    private final String genero;
}
