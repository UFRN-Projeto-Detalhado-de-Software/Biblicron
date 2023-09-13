package edu.ufrn.imd.Biblicron.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "TB_LIVRO")
public class Livro implements Serializable {
    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Id;

    @Column(nullable = false, unique = true, length = 255)
    private String titulo;

    @Column(nullable = false, unique = false, length = 255)
    private String autor;

    @Column(nullable = false, unique = false, length = 10)
    private int quantidade;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
