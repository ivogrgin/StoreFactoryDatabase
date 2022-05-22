package hr.java.production.threads;

import hr.java.production.model.Address;
import hr.java.production.model.Item;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.Callable;

public class SaveFactoryToDatabaseThread implements Callable<Long> {
    private String factoryNameString;
    private Address address;
    private Set<Item>  itemSet;

    public SaveFactoryToDatabaseThread(String factoryNameString, Address address, Set<Item> itemSet) {
        this.factoryNameString = factoryNameString;
        this.address = address;
        this.itemSet = itemSet;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    @Override
    public Long call() throws SQLException, IOException {
        return ModelList.db.saveFactoryToDatabase(factoryNameString, address, itemSet);
    }
}
