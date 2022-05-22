package hr.java.production.threads;

import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;

/**
 * GetClientTableFromDatabaseThread runs thread to retrieve
 * all clients from database
 */
public class GetClientTableFromDatabaseThread implements Runnable{


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
            System.out.println("START CLIENT");
            ModelList.setListOfClients(ModelList.db.getAllClientsFromDatabase());
            System.out.println("DONE CLIENT");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
