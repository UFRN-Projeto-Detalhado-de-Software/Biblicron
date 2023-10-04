package edu.ufrn.imd.Biblicron.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EstipularPrazoRequest {
    @NotBlank
    private String nomeLivro;
    @NotBlank
    private int parametro;

    // Getters e setters
}

