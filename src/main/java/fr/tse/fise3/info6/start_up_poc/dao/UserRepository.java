package fr.tse.fise3.info6.start_up_poc.dao;

import fr.tse.fise3.info6.start_up_poc.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
