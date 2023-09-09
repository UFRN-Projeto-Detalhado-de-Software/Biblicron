package edu.ufrn.imd.Biblicron.model;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;

    public void setTitulo(String title){
        this.titulo = title;
    }

    public void setAutor(String author){
        this.autor = author;
    }
}
