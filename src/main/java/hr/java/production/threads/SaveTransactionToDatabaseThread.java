package hr.java.production.threads;

import hr.java.production.model.Client;
import hr.java.production.model.Item;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.Callable;

public class SaveTransactionToDatabaseThread implements Callable<Long> {


    private final LocalDate transactionDate;
    private final List<Item> selectedItems;
    private final Client client;

    public SaveTransactionToDatabaseThread(LocalDate transactionDate, List<Item> selectedItems, Client client) {

        this.transactionDate = transactionDate;
        this.selectedItems = selectedItems;
        this.client = client;
    }

    /**
     * Computes a result, or throws an exception if unable to do so.
     *
     * @return computed result
     * @throws SQLException if unable to compute a result
     */
    @Override
    public Long call() throws SQLException, IOException {
        return ModelList.db.saveTransactionToDatabase(transactionDate
                ,selectedItems, client);
    }
}
