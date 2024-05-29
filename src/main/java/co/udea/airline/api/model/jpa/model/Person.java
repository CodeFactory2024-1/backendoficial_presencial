package co.udea.airline.api.model.jpa.model;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "PERSON")
@Table(name = "PERSON")
public class Person{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERSON_ID")
    private Integer personId;

    @ManyToOne
    @JoinColumn(name = "ID_IDENTIFICATION_TYPE")
    private IdentificationType identificationType;

    @Column(name = "IDENTIFICATION_NUMBER")
    private String idNumber;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    private Character genre;

    @Column(name = "BIRTH_DATE")
    private LocalDate birthDate;

    @Column(name = "PHONE_NUMBER")
    private String phoneNumber;
    @Column(name = "COUNTRY")
    private String country;
    @Column(name = "PROVINCE")
    private String province;
    @Column(name = "CITY")
    private String city;
    @Column(name = "ADDRESS")
    private String address;

    @NotBlank
    @Email
    private String email;

}