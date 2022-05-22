package hr.java.production.threads;

import hr.java.production.model.Address;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

/**
 * SaveClientToDatabaseThread saves client to database
 */
public class SaveClientToDatabaseThread implements Callable<Long> {

    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private Address address;

    public SaveClientToDatabaseThread(String firstName, String lastName, LocalDate dateOfBirth, Address address) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public Long call() throws SQLException, IOException {
        return ModelList.db.saveClientToDatabase(firstName, lastName, dateOfBirth, address);
    }
}
