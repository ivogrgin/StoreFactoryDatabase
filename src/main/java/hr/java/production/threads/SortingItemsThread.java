package hr.java.production.threads;

import hr.java.production.controller.FirstScreen;
import hr.java.production.model.Client;
import hr.java.production.model.Item;
import hr.java.production.model.Transaction;
import hr.java.production.util.ModelList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

public class SortingItemsThread implements Runnable{

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

        ModelList.getListOfItems().sort(Comparator.comparing(Item::getSellingPrice));
    }
    public static void runMostExpensiveItemBySellingPrice()
    {
        Timeline getItem = new Timeline(new KeyFrame(Duration.seconds(1), actionEvent ->
        {
            Optional<Item> mostExpensiveItemBySellingPrice = ModelList.getListOfItems().stream()
                    .max(Comparator.comparing(Item::getSellingPrice));
            mostExpensiveItemBySellingPrice.ifPresent(item ->
                    FirstScreen.getStage().setTitle("The most expensive item by Selling Price: " + item));
        }));
        getItem.setCycleCount(Animation.INDEFINITE);
        getItem.play();
    }

    public static void runYoungestOldestClient()
    {

        Timeline getclientyoungestOldest = new Timeline(new KeyFrame(Duration.seconds(5), actionEvent ->
        {
            Optional<Client> youngestClient = ModelList.getListOfClients().stream()
                    .max(Comparator.comparing(Client::getDateOfBirth));
            youngestClient.ifPresent(item ->
                    FirstScreen.getStage().setTitle("The youngest client: " + youngestClient.get().getFirstName()
                            +" "+ youngestClient.get().getLastName()));
        }), new KeyFrame(Duration.seconds(10), actionEvent ->
        {
            Optional<Client> oldestClient = ModelList.getListOfClients().stream()
                    .min(Comparator.comparing(Client::getDateOfBirth));
            oldestClient.ifPresent(item ->
                    FirstScreen.getStage().setTitle("The oldest client: " + oldestClient.get().getFirstName()
                            +" "+ oldestClient.get().getLastName()));
        }));
        getclientyoungestOldest.setCycleCount(Animation.INDEFINITE);
        getclientyoungestOldest.play();


    }

    public static void runThePriciestTransaction()
    {

        Timeline priciestTransactionTimeline = new Timeline(new KeyFrame(Duration.seconds(5), actionEvent ->
        {
            Optional<Transaction> priciestTransaction = ModelList.getListOfTransactions().stream()
                    .max(Comparator.comparing(Transaction::sumItemSellingPrice));
            priciestTransaction.ifPresent(transaction ->
                    FirstScreen.getStage().setTitle("The Most expensive transaction was made by " + priciestTransaction.get().getClient().getFirstName()
                            +" "+ priciestTransaction.get().getClient().getLastName()+" with items: " + transaction.getItems().stream().map(Item::getName).collect(Collectors.joining())));
        }), new KeyFrame(Duration.seconds(10), actionEvent -> FirstScreen.getStage().setTitle("Production Application")));
        priciestTransactionTimeline.setCycleCount(Animation.INDEFINITE);
        priciestTransactionTimeline.play();


    }

}
