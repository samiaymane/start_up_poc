package fr.tse.fise3.info6.start_up_poc.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Project {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) Long id;
    private String title;
    private String description;
    private LocalDate creationDate;

}
