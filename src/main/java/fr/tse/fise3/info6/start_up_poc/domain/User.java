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
    @ManyToMany(mappedBy="users",fetch= FetchType.EAGER)
    private Set<Project> projects;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "manager")
    private Set<User> subordinates;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private User manager;

    public User(){
        this.projects = new HashSet<>();
        this.subordinates = new HashSet<>();
    }
}
