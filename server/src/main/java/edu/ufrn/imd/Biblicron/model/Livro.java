package edu.ufrn.imd.Biblicron.model;
import edu.ufrn.imd.Biblicron.model.interfaces.IEntidade;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Livro implements IEntidade {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "{livro.titulo.notblank}")
    @Size(max = 100, message = "{livro.titulo.size}")
    private String titulo;
    private String autor;

    public void setTitulo(String title){
        this.titulo = title;
    }

    public void setAutor(String author){
        this.autor = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Livro that = (Livro) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
