package hr.java.production.threads;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
import hr.java.production.util.Database;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetStoreTableFromDatabaseThread implements Runnable{


    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

        try {
            System.out.println("START S");
            ModelList.setListOfStores(ModelList.db.getAllStoresFromDatabase());
            System.out.println("DONE S");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
}
