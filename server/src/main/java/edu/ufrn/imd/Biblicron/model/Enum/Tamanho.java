package edu.ufrn.imd.Biblicron.model.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Tamanho {

    PP("PP"),
    P("P"),
    M("M"),
    G("G"),
    GG("GG");

    @Getter
    private final String tamanho;
}
