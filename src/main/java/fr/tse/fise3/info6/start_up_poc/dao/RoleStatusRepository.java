package fr.tse.fise3.info6.start_up_poc.dao;

import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleStatusRepository extends JpaRepository<RoleStatus,Long> {
}
