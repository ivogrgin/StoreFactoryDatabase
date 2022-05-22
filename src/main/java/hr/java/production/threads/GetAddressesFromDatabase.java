package hr.java.production.threads;

import hr.java.production.model.Address;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

public class GetAddressesFromDatabase implements Callable<ArrayList<Address>> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public ArrayList<Address> call() throws SQLException, IOException {
        return ModelList.db.getAllAddressesFromDatabase();
    }
}

