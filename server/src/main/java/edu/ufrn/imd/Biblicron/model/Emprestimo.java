package edu.ufrn.imd.Biblicron.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "TB_EMPRESTIMOS")
public class Emprestimo implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = false, length = 50)
    private Long byUserId;

    @Column(nullable = false, unique = false, length = 50)
    private Long toUserId;

    @Column(nullable = false, unique = false, length = 50)
    private Long livroId;

    @Column(nullable = false, unique = false)
    private LocalDate dataEmprestimo;
}
