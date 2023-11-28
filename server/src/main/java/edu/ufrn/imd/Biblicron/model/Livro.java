package edu.ufrn.imd.Biblicron.model;

import edu.ufrn.imd.Biblicron.model.Enum.Genero;
import edu.ufrn.imd.Biblicron.model.Produto;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_LIVRO")
public class Livro extends Produto implements Serializable {
    public static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Column(nullable = false, unique = true, length = 255)
    private String titulo;

    @Getter
    @Setter
    @Column(nullable = false, unique = false, length = 255)
    private String autor;

    @Getter
    @Setter
    @Column(nullable = false, unique = false)
    private LocalDate dataPublicacao;

    @Getter
    @Setter
    @Column(nullable = false, unique = false)
    private int paginas;

    @Getter
    @Setter
    @ElementCollection(targetClass = Genero.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "TB_LIVRO_GENERO", joinColumns = @JoinColumn(name = "livro_id"))
    @Enumerated(EnumType.STRING)
    private List<Genero> generos = new ArrayList<>();

}
