package edu.ufrn.imd.Biblicron.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "TB_EMPRESTIMOS")
public class Emprestimo implements Serializable {

    private static final Long serialVersionUID = 1l;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    @Column(nullable = false, unique = false)
    private LocalDate loanDate;

    @Column(nullable = true, unique = false)
    private LocalDate returnDate; //Essa é a data em que o emprestimo foi realmente devoldido, para manter o registro nos logs

    @Column(nullable = false, unique = false)
    private LocalDate maxReturnDate; //Esse é o prazo máximo pra devolução, será colocado por padrão ao inserir o empréstimo na database

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livro getLivro() {
        return livro;
    }

    public void setLivro(Livro livro) {
        this.livro = livro;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getMaxReturnDate() {
        return maxReturnDate;
    }

    public void setMaxReturnDate(LocalDate maxReturnDate) {
        this.maxReturnDate = maxReturnDate;
    }
}
