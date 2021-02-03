package fr.tse.fise3.info6.start_up_poc.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class RoleStatus {

    @NotNull(message = "Id cannot be null")
    private @Id Long id;

    @NotNull(message = "Role cannot be null")
    @NotEmpty(message = "Role cannot be empty")
    private String roleTitle;

    public RoleStatus(Long id, String roleTile){
        this.id = id;
        this.roleTitle = roleTile;
    }

    public RoleStatus(){

    }
}
