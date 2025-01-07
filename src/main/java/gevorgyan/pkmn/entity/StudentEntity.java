package gevorgyan.pkmn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "students")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="firstName")
    private String firstName;

    @Column(name="surName")
    private String surName;

    @Column(name="familyName")
    private String familyName;

    @Column(name="\"group\"")
    private String group;

    public String getFatherName() {
        return familyName;
    }
}