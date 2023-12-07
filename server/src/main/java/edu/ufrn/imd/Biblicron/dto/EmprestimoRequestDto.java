package edu.ufrn.imd.Biblicron.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EmprestimoRequestDto {
    @Getter
    @Setter
    @NotBlank
    @Size(max = 50)
    private String nomeProduto;

    @Getter
    @Setter
    @NotBlank
    @Size(max = 255)
    private String nomeUsuario;

}
