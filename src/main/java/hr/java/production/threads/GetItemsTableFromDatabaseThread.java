package hr.java.production.threads;

import hr.java.production.model.Category;
import hr.java.production.model.Item;
import hr.java.production.util.Database;
import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetItemsTableFromDatabaseThread implements Runnable{


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
            System.out.println("START I");
            ModelList.setListOfItems(ModelList.db.getAllItemsFromDatabase());
            System.out.println("DONE I");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
