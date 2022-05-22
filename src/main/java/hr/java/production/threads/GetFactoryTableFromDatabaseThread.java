package hr.java.production.threads;

import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;

public class GetFactoryTableFromDatabaseThread implements Runnable{


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
            System.out.println("START F");
            ModelList.setListOfFactories(ModelList.db.getAllFactoriesFromDatabase());
            System.out.println("DONE F");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }

    }
}