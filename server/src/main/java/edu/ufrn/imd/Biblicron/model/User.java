package edu.ufrn.imd.Biblicron.model;

import edu.ufrn.imd.Biblicron.model.Enum.UserType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
@Table(name = "TB_USERS")
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 50)
    @Getter
    @Setter
    private String username;

    @Column(nullable = false, unique = false, length = 50)
    @Getter
    @Setter
    private String password;

    @Column(nullable = false, unique = false)
    @NotNull(message = "{user.usertype.notnull}")
    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private UserType userType;

    @Column(nullable = false, unique = true, length = 50)
    @Getter
    @Setter
    private String email;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
