package edu.ufrn.imd.Biblicron.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity
@Table(name = "TB_PRODUTO")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private Long id;

    @Getter
    @Setter
    @Column(nullable = false, unique = false, length = 1000)
    private int quantidadeDisponivel;

    @Getter
    @Setter
    @Column(nullable = false, unique = false, length = 1000)
    private int quantidade;

    @Getter
    @Setter
    @Column(nullable = false, unique = false)
    private float valor;

    @Getter
    @Setter
    @Column(nullable = false, unique = true, length = 255)
    private String nomeProduto;
}
