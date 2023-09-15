package edu.ufrn.imd.Biblicron.repository;

import edu.ufrn.imd.Biblicron.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
}
