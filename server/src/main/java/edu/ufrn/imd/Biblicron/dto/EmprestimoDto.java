package edu.ufrn.imd.Biblicron.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class EmprestimoDto {

    @NotBlank
    @Size(max = 50)
    private long toUserId;

    @NotBlank
    @Size(max = 50)
    private long byUserId;

    @NotBlank
    @Size(max = 50)
    private String passwordBiblio;

    @NotBlank
    @Size(max = 50)
    private long idLivro;

    public long getIdLivro() {
      return this.idLivro;
    }

    public long getUserBiblioId() {
      return this.byUserId;
    }

    public long getUserId() {
      return this.toUserId;
    }
    
    public String getUserPass() {
      return this.passwordBiblio;
    }
}
