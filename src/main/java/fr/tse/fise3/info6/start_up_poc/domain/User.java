package fr.tse.fise3.info6.start_up_poc.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class User {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @ManyToOne
    @Valid
    private RoleStatus roleStatus;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy="users", fetch= FetchType.EAGER)
    private Set<Project> projects;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "manager")
    private Set<User> subordinates;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private User manager;

    public void addSubordinate(User user){
        User oldManager = user.getManager();
        if(oldManager != null) {
            oldManager.removeSubordinate(user);
        }
        this.subordinates.add(user);
        user.setManager(this);
    }

    public void removeSubordinate(User user){
        this.subordinates.remove(user);
    }

    public void clearSubordiantes(){
        for(User user : this.subordinates){
            user.setManager(null);
        }
        this.subordinates.clear();
    }

    public User(){
        this.projects = new HashSet<>();
        this.subordinates = new HashSet<>();

    }

    public void addProject(Project project){
        project.getUsers().add(this);
        this.projects.add(project);
    }

    public void removeProject(Project project){
        this.projects.remove(project);
    }

    public void clearProjects(){
        for (Project project : this.projects){
            project.removeUser(this);
        }
        this.projects.clear();
    }
}
