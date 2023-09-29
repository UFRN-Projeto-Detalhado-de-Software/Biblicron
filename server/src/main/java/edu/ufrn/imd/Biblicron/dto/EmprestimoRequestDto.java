package edu.ufrn.imd.Biblicron.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EmprestimoRequestDto {
    @NotBlank
    @Size(max = 50)
    private String nomeLivro;

    @NotBlank
    @Size(max = 255)
    private String nomeUsuario;

    public String getNomeLivro() {
        return nomeLivro;
    }

    public void setNomeLivro(String nomeLivro) {
        this.nomeLivro = nomeLivro;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }
}
