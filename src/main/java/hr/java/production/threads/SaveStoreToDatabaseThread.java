package hr.java.production.threads;

import hr.java.production.model.Item;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;

public class SaveStoreToDatabaseThread implements Callable<Long> {

    private String storeNameString;
    private String storeWebsiteString;
    private Set<Item> itemSet;

    public SaveStoreToDatabaseThread(String storeNameString, String storeWebsiteString, Set<Item> itemSet) {
        this.storeNameString = storeNameString;
        this.storeWebsiteString = storeWebsiteString;
        this.itemSet = itemSet;
    }


    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     */
    @Override
    public Long call() throws SQLException, IOException {
        return ModelList.db.saveStoreToDatabase(storeNameString
                , storeWebsiteString, (TreeSet<Item>) itemSet);
    }
}
