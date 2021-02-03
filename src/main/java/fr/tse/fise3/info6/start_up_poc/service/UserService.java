package fr.tse.fise3.info6.start_up_poc.service;

import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;

import javax.management.relation.Role;
import java.util.Collection;

public interface UserService {

    public Collection<User> findAllUsers();

    public Collection<User> findAllManagers();

    public Collection<User> findAllManagersAndUsers();

    public User findUser(Long id);

    public RoleStatus findRoleStatus(Long id);

    public User changeRoleStatusFor(User user, RoleStatus roleStatus);

    public Collection<User> findSubordinatesForManager(User manager);

    public User upgradeUser(User user);

    public User downgradeUser(User user);

    public void affectUserToManager(User user, User manager);

    public User createUser(User user);

    public User createAdmin(User user);
}
