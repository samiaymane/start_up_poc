package fr.tse.fise3.info6.start_up_poc.service.impl;

import fr.tse.fise3.info6.start_up_poc.dao.RoleStatusRepository;
import fr.tse.fise3.info6.start_up_poc.dao.UserRepository;
import fr.tse.fise3.info6.start_up_poc.domain.Log;
import fr.tse.fise3.info6.start_up_poc.domain.Project;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleStatusRepository roleStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllManagers() {

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        return this.userRepository.findAll()
                .stream()
                .filter(user -> user.getRoleStatus().equals(managerStatus))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllManagersAndUsers() {

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);
        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        return this.userRepository.findAll()
                .stream()
                .filter(user -> user.getRoleStatus().equals(managerStatus) || user.getRoleStatus().equals(userStatus))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public User findUser(String username, String password){

        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public RoleStatus findRoleStatus(Long id){
        return this.roleStatusRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public User changeRoleStatusFor(User user, RoleStatus roleStatus){

        user.setRoleStatus(roleStatus);
        this.userRepository.save(user);
        return user;
    }

    private RoleStatus getTargetRoleStatusForUpgrade(RoleStatus roleStatus){

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);
        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);
        RoleStatus adminStatus = this.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);
        RoleStatus result = null;

        if(roleStatus != null){

            if(roleStatus.equals(managerStatus)){
                throw new IllegalStateException();
            }
            else if(roleStatus.equals(userStatus)){
                result = managerStatus;
            }
            else if(roleStatus.equals(adminStatus)){
                throw new IllegalStateException();
            }
        }else{
            throw new IllegalArgumentException();
        }

        return result;
    }

    private RoleStatus getTargetRoleStatusForDowngrade(RoleStatus roleStatus){

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);
        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);
        RoleStatus adminStatus = this.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);
        RoleStatus result = null;

        if(roleStatus != null){

            if(roleStatus.equals(managerStatus)){
               result = userStatus;
            }
            else if(roleStatus.equals(userStatus)){
                throw new IllegalStateException();
            }
            else if(roleStatus.equals(adminStatus)){
                throw new IllegalStateException();
            }
        }else{
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findSubordinatesForManager(User manager){

        User foundManager = this.findUser(manager.getId());

        if(foundManager != null){
            Hibernate.initialize(foundManager.getSubordinates());
            return new ArrayList<>(foundManager.getSubordinates());
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Project> findProjectsForUser(User user){

        User foundUser = this.findUser(user.getId());

        if(user != null){
            Hibernate.initialize(foundUser.getProjects());
            return new ArrayList<>(foundUser.getProjects());
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public User upgradeUser(User user) {

        RoleStatus targetRoleStatus = this.getTargetRoleStatusForUpgrade(user.getRoleStatus());
        userRepository.save(user);
        return this.changeRoleStatusFor(user,targetRoleStatus);
    }

    @Override
    @Transactional
    public User downgradeUser(User user) {

        RoleStatus targetRoleStatus = this.getTargetRoleStatusForDowngrade(user.getRoleStatus());
        userRepository.save(user);
        return this.changeRoleStatusFor(user,targetRoleStatus);
    }

    @Override
    @Transactional
    public User affectUserToManager(User user, User manager) {

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);
        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if(!user.getRoleStatus().equals(userStatus) || !manager.getRoleStatus().equals(managerStatus)){
            throw new IllegalStateException();
        }else{
            manager.addSubordinate(user);
            return this.userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public User createUser(User user) {

        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);
        user.setRoleStatus(userStatus);
        return this.userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(User user){

        user = this.findUser(user.getId());
        User manager = user.getManager();

        if(manager != null){
            user.getManager().removeSubordinate(user);
        }

        user.setManager(null);
        user.clearProjects();
        this.userRepository.delete(user);
    }

    @Override
    @Transactional
    public User createAdmin(User user) {
        RoleStatus adminStatus = this.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);
        user.setRoleStatus(adminStatus);
        return this.userRepository.save(user);
    }
}
