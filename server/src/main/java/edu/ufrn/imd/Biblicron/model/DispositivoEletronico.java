package edu.ufrn.imd.Biblicron.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "TB_DISPOSITIVO_ELETRONICO")
public class DispositivoEletronico extends Produto implements Serializable {

    public static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Column(nullable = false, unique = true, length = 255)
    private String serialNumber;

    @Getter
    @Setter
    @Column(nullable = false, unique = false, length = 255)
    private String marca;

    @Getter
    @Setter
    @Column(nullable = false, unique = false, length = 255)
    private String modelo;
}
