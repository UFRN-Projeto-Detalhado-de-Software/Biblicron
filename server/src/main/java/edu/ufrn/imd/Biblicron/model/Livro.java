package edu.ufrn.imd.Biblicron.model;

import edu.ufrn.imd.Biblicron.model.Enum.Genero;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_LIVRO")
public class Livro implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

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
    @Column(nullable = false, unique = false, length = 1000)
    private int quantidade;

    @Getter
    @Setter
    @Column(nullable = false, unique = false, length = 1000)
    private int quantidadeDisponivel;

    @Getter
    @Setter
    @Column(nullable = false, unique = false)
    private LocalDate dataPublicacao;

    @Getter
    @Setter
    @Column(nullable = false, unique = false)
    private int paginas;

    @ElementCollection(targetClass = Genero.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "TB_LIVRO_GENERO", joinColumns = @JoinColumn(name = "livro_id"))
    @Enumerated(EnumType.STRING)
    private List<Genero> generos = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<Genero> getGeneros() {
        return generos;
    }

    public void setGeneros(List<Genero> generos) {
        this.generos = generos;
    }
}
