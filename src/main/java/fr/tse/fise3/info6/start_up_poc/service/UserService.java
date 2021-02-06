package fr.tse.fise3.info6.start_up_poc.service;

import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;

import java.util.Collection;
import java.util.List;

public interface UserService {

    public List<User> findAllUsers();

    public List<User> findAllManagers();

    public List<User> findAllManagersAndUsers();

    public User findUser(Long id);

    public User findUser(String username, String password);

    public RoleStatus findRoleStatus(Long id);

    public User changeRoleStatusFor(User user, RoleStatus roleStatus);

    public List<User> findSubordinatesForManager(User manager);

    public List<Project> findProjectsForUser(User user);

    public User upgradeUser(User user);

    public User downgradeUser(User user);

    public User affectUserToManager(User user, User manager);

    public User createUser(User user);

    public void deleteUser(User user);

    public User createAdmin(User user);
}
