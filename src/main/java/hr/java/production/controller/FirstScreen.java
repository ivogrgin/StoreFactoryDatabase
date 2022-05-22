package hr.java.production.controller;

import hr.java.production.threads.SortingItemsThread;
import hr.java.production.util.ModelList;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstScreen extends Application {
    private static Stage mainStage;
    private static final Logger logger = LoggerFactory.getLogger(FirstScreen.class);

    @Override
    public void start(Stage stage) {
        mainStage = stage;
        //SortingItemsThread.runMostExpensiveItemBySellingPrice();
        //SortingItemsThread.runYoungestOldestClient();
        SortingItemsThread.runThePriciestTransaction();
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("firstScreen.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        stage.setTitle("Production Application");
        stage.setScene(scene);
        stage.show();

    }

    public static Stage getStage(){
        return mainStage;
    }
    public static void main(String[] args) {
        boolean errorOccurred = false;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future<?> sortingItemsFuture = executorService.submit(new SortingItemsThread());
        try {
            Instant start = Instant.now();
            ModelList.initializeLists();
            Instant end = Instant.now();
            System.out.println("duration: " + Duration.between(start, end).toMillis() + " ms");
            sortingItemsFuture.get();

        } catch (ExecutionException | InterruptedException e) {
            logger.error(e.getMessage());
            Platform.runLater(() -> {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection to database failed!");
                alert.setHeaderText("Couldn't get data from database!");
                alert.setContentText("Connection to server failed. This application requires" +
                        " a valid internet connection and make sure the database is up.");
                alert.showAndWait();
                System.exit(-1);

            });
            errorOccurred = true;

        }


        ModelList.awaitTerminationAfterShutdown(executorService);
        if (!errorOccurred) launch();

    }
}