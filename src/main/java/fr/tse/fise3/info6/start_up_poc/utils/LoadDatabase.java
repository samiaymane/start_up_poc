package fr.tse.fise3.info6.start_up_poc.utils;

import fr.tse.fise3.info6.start_up_poc.dao.LogRepository;
import fr.tse.fise3.info6.start_up_poc.dao.ProjectRepository;
import fr.tse.fise3.info6.start_up_poc.dao.RoleStatusRepository;
import fr.tse.fise3.info6.start_up_poc.dao.UserRepository;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Slf4j
public class LoadDatabase {

    private void initRoleStatus(RoleStatusRepository roleStatusRepository){

        RoleStatus userStatus = new RoleStatus(Constants.ROLE_STATUS_USER_ID, Constants.ROLE_STATUS_USER_ROLE_TITLE);
        roleStatusRepository.save(userStatus);
        log.info(userStatus + " saved to database.");

        RoleStatus managerStatus = new RoleStatus(Constants.ROLE_STATUS_MANAGER_ID, Constants.ROLE_STATUS_MANAGER_ROLE_TITLE);
        roleStatusRepository.save(managerStatus);
        log.info(managerStatus + " saved to database.");

        RoleStatus adminStatus = new RoleStatus(Constants.ROLE_STATUS_ADMIN_ID, Constants.ROLE_STATUS_ADMIN_ROLE_TITLE);
        roleStatusRepository.save(adminStatus);
        log.info(adminStatus + " saved to database.");
    }

    @Bean
    @Profile("test")
    CommandLineRunner initTestDatabase(LogRepository logRepository, ProjectRepository projectRepository,
                                       RoleStatusRepository roleStatusRepository, UserRepository userRepository){
        return args -> {
            initRoleStatus(roleStatusRepository);

            User admin = new User();
            admin.setEmail("admin@dev.dev");
            admin.setFirstName("admin");
            admin.setLastName("admin");
            admin.setPassword("admin");
            admin.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_ADMIN_ID).orElse(null));
            userRepository.save(admin);
            log.info(admin + " saved to database.");

            User manager1 = new User();
            manager1.setEmail("manager1@dev.dev");
            manager1.setFirstName("manager1");
            manager1.setLastName("manager1");
            manager1.setPassword("manager1");
            manager1.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_MANAGER_ID).orElse(null));
            userRepository.save(manager1);
            log.info(manager1 + " saved to database.");

            User manager2 = new User();
            manager2.setEmail("manager1@dev.dev");
            manager2.setFirstName("manager2");
            manager2.setLastName("manager2");
            manager2.setPassword("manager2");
            manager2.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_MANAGER_ID).orElse(null));
            userRepository.save(manager2);
            log.info(manager2 + " saved to database.");

            User user1 = new User();
            user1.setEmail("dev1@dev.dev");
            user1.setFirstName("dev1");
            user1.setLastName("dev1");
            user1.setPassword("dev1");
            user1.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_USER_ID).orElse(null));
            userRepository.save(user1);
            log.info(user1 + " saved to database.");

            User user2 = new User();
            user2.setEmail("dev2@dev.dev");
            user2.setFirstName("dev2");
            user2.setLastName("dev2");
            user2.setPassword("dev2");
            user2.setRoleStatus(roleStatusRepository.findById(Constants.ROLE_STATUS_USER_ID).orElse(null));
            userRepository.save(user1);
            log.info(user2 + " saved to database.");

            log.info("All seem to be OK for the app initialization!");
        };
    }

}
