package edu.ufrn.imd.Biblicron.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    @Setter
    private long Id;

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

}
