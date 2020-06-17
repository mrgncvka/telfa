package base.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "usr")
@Data
public class User {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private boolean bot;

    private String username;
    private String password;



}
