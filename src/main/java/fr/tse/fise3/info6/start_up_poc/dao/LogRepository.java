package fr.tse.fise3.info6.start_up_poc.dao;

import fr.tse.fise3.info6.start_up_poc.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<Log,Long> {
}
