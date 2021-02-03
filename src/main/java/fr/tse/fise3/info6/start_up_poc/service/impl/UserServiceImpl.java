package fr.tse.fise3.info6.start_up_poc.service.impl;

import fr.tse.fise3.info6.start_up_poc.dao.RoleStatusRepository;
import fr.tse.fise3.info6.start_up_poc.dao.UserRepository;
import fr.tse.fise3.info6.start_up_poc.domain.RoleStatus;
import fr.tse.fise3.info6.start_up_poc.domain.User;
import fr.tse.fise3.info6.start_up_poc.service.UserService;
import fr.tse.fise3.info6.start_up_poc.utils.Constants;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleStatusRepository roleStatusRepository;

    @Override
    @Transactional(readOnly = true)
    public Collection<User> findAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> findAllManagers() {

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);

        return this.userRepository.findAll()
                .stream()
                .filter(user -> user.getRoleStatus().equals(managerStatus))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> findAllManagersAndUsers() {

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);
        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        return this.userRepository.findAll()
                .stream()
                .filter(user -> user.getRoleStatus().equals(managerStatus) || user.getRoleStatus().equals(userStatus))
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public User findUser(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleStatus findRoleStatus(Long id){
        return this.roleStatusRepository.findById(id).orElse(null);
    }

    @Override
    public User changeRoleStatusFor(User user, RoleStatus roleStatus){

        user = this.userRepository.save(user);
        user.setRoleStatus(roleStatus);

        return user;
    }

    private RoleStatus getTargetRoleStatusForUpgrade(RoleStatus roleStatus){

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);
        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);
        RoleStatus adminStatus = this.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);
        RoleStatus result = null;

        if(roleStatus != null){

            if(roleStatus.equals(managerStatus)){
                result = adminStatus;
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
                result = managerStatus;
            }
        }else{
            throw new IllegalArgumentException();
        }

        return result;
    }

    @Override
    @Transactional(readOnly = true)
    public Collection<User> findSubordinatesForManager(User manager){

        User foundManager = this.findUser(manager.getId());

        if(foundManager != null){
            Hibernate.initialize(foundManager.getSubordinates());
            return foundManager.getSubordinates();
        }else{
            return new HashSet<>();
        }
    }

    @Override
    public User upgradeUser(User user) {

        RoleStatus targetRoleStatus = this.getTargetRoleStatusForUpgrade(user.getRoleStatus());

        return this.changeRoleStatusFor(user,targetRoleStatus);
    }

    @Override
    public User downgradeUser(User user) {

        RoleStatus targetRoleStatus = this.getTargetRoleStatusForDowngrade(user.getRoleStatus());

        return this.changeRoleStatusFor(user,targetRoleStatus);
    }

    @Override
    public void affectUserToManager(User user, User manager) {

        RoleStatus managerStatus = this.findRoleStatus(Constants.ROLE_STATUS_MANAGER_ID);
        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);

        if(!user.getRoleStatus().equals(userStatus) || !manager.getRoleStatus().equals(managerStatus)){
            throw new IllegalStateException();
        }else{
            User oldManager = user.getManager();

            if(oldManager != null){
                oldManager.getSubordinates().remove(user);
            }
            user.setManager(manager);
            manager.getSubordinates().add(user);
        }
    }

    @Override
    public User createUser(User user) {

        RoleStatus userStatus = this.findRoleStatus(Constants.ROLE_STATUS_USER_ID);
        user.setRoleStatus(userStatus);
        return this.userRepository.save(user);
    }

    @Override
    public User createAdmin(User user) {
        RoleStatus adminStatus = this.findRoleStatus(Constants.ROLE_STATUS_ADMIN_ID);
        user.setRoleStatus(adminStatus);
        return this.userRepository.save(user);
    }
}
