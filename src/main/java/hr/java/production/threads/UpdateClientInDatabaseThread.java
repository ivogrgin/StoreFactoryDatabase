package hr.java.production.threads;

import hr.java.production.model.Client;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

/**
 * UpdateClientInDatabaseThread updates fields for a client at a specified id
 */
public class UpdateClientInDatabaseThread implements Callable<Long> {
    private final Client clientToAdd;


    public UpdateClientInDatabaseThread(Client clientToAdd) {
      this.clientToAdd = clientToAdd;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    @Override
    public Long call() throws SQLException, IOException {
        return ModelList.db.updateClientToDatabase(clientToAdd);
    }
}
