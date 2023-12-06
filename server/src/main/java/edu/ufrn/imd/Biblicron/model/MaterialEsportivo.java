package edu.ufrn.imd.Biblicron.model;

import edu.ufrn.imd.Biblicron.model.Enum.Categoria;
import edu.ufrn.imd.Biblicron.model.Enum.Genero;
import edu.ufrn.imd.Biblicron.model.Enum.Tamanho;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "TB_MaterialEsportivo")
public class MaterialEsportivo extends Produto implements Serializable {
    public static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @Column(nullable = false, unique = false, length = 255)
    private String marca;

    @Getter
    @Setter
    @Column(nullable = false, unique = false)
    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Getter
    @Setter
    @ElementCollection(targetClass = Tamanho.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "TB_MATERIALESPORTIVO_TAMANHO", joinColumns = @JoinColumn(name = "materialesportivo_id"))
    @Enumerated(EnumType.STRING)
    private List<Tamanho> tamanhos;

}
