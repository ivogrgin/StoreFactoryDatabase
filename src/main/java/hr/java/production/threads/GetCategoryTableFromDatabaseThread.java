package hr.java.production.threads;

import hr.java.production.util.ModelList;

import java.io.IOException;
import java.sql.SQLException;

public class GetCategoryTableFromDatabaseThread implements Runnable{




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
            System.out.println("START C");
            ModelList.setListOfCategories(ModelList.db.getAllCategoriesFromDatabase());
            System.out.println("DONE C");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


    }
}
