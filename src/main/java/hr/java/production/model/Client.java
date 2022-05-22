package hr.java.production.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * client class
 */
public class Client extends NamedEntity {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Address address;
    public Client(Long id, String firstName)
    {
        super(firstName, id);

    }
    public Client(Long id, String firstName,
                  String lastName, LocalDate dateOfBirth,
                  Address address) {
        super(firstName, id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


}
