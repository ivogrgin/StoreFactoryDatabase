package hr.java.production.threads;

import hr.java.production.model.Factory;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

public class GetFactoriesFromDatabase implements Callable<ArrayList<Factory>> {
    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws Exception if unable to compute a result
     */
    @Override
    public ArrayList<Factory> call() throws SQLException, IOException {
        return ModelList.db.getAllFactoriesFromDatabase();
    }
}

