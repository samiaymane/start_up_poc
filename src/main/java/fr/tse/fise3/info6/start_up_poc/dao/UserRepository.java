package fr.tse.fise3.info6.start_up_poc.dao;

import fr.tse.fise3.info6.start_up_poc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);
}
