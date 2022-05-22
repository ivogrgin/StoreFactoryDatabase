package hr.java.production.threads;

import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

public class SaveCategoryToDatabaseThread implements Callable<Long> {

    private String name;
    private String description;

    public SaveCategoryToDatabaseThread(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    @Override
    public Long call() throws SQLException, IOException {
        return ModelList.db.saveCategoryToDatabase(name, description);
    }
}