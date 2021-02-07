package fr.tse.fise3.info6.start_up_poc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Entity
public class User implements UserDetails {

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
    @JsonIgnoreProperties({"subordinates","manager"})
    @OneToMany(mappedBy = "manager")
    private Set<User> subordinates;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonIgnoreProperties({"subordinates","manager"})
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

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        GrantedAuthority grantedAuthority = new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return roleStatus.getRoleTitle();
            }
        };
        return List.of(grantedAuthority);
    }

    @JsonIgnore
    @Override
    public String getUsername() {
        return null;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return false;
    }
}
