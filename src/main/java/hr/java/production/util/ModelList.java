package hr.java.production.util;
import hr.java.production.model.*;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ModelList {
    public static Database db = new Database();
    private static List<Category> listOfCategories = new ArrayList<>();
    private static List<Item> listOfItems = new ArrayList<>();
    private static List<Store> listOfStores = new ArrayList<>();
    private static List<Factory> listOfFactories = new ArrayList<>();
    private static List<Address> listOfAddresses = new ArrayList<>();
    private static List<Client> listOfClients = new ArrayList<>();
    private static List<Transaction> listOfTransactions = new ArrayList<>();

    public static void initializeLists() throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        Instant start = Instant.now();
        Future<ArrayList<Category>> categories = executorService.submit(ModelList.db::getAllCategoriesFromDatabase);
        Future<ArrayList<Address>> addresses = executorService.submit(ModelList.db::getAllAddressesFromDatabase);
        listOfCategories = categories.get();
        Future<ArrayList<Item>> items = executorService.submit(ModelList.db::getAllItemsFromDatabase);
        listOfAddresses = addresses.get();
        listOfItems = items.get();
        Future<ArrayList<Store>> stores = executorService.submit(ModelList.db::getAllStoresFromDatabase);
        Future<ArrayList<Factory>> factory = executorService.submit(ModelList.db::getAllFactoriesFromDatabase);
        Future<ArrayList<Client>> client = executorService.submit(ModelList.db::getAllClientsFromDatabase);
        listOfClients = client.get();
        Future<ArrayList<Transaction>> transactions = executorService
                .submit(ModelList.db::getAllTransactionsFromDatabase);
        listOfTransactions = transactions.get();
        listOfStores = stores.get();
        listOfFactories = factory.get();
        ModelList.awaitTerminationAfterShutdown(executorService);
        Instant end = Instant.now();
        System.out.println("!duration: " + Duration.between(start, end).toMillis() + " ms");

    }


    public static void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(30, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static List<Transaction> getListOfTransactions()
    {
        return listOfTransactions;
    }

    public static List<Category> getListOfCategories() {
        return listOfCategories;
    }

    public static List<Store> getListOfStores() {
        return listOfStores;
    }

    public static List<Factory> getListOfFactories() {
        return listOfFactories;
    }

    public static List<Item> getListOfItems() {
        return listOfItems;
    }

    public static List<Address> getListOfAddresses() {
        return listOfAddresses;
    }

    public static List<Client> getListOfClients() {
        return listOfClients;
    }

    public static void setListOfItems(List<Item> listOfItems) {
        ModelList.listOfItems = listOfItems;
    }

    public static void setListOfStores(List<Store> listOfStores) {
        ModelList.listOfStores = listOfStores;
    }

    public static void setListOfFactories(List<Factory> listOfFactories) {
        ModelList.listOfFactories = listOfFactories;
    }

    public static void setListOfAddresses(List<Address> listOfAddresses) {
        ModelList.listOfAddresses = listOfAddresses;
    }

    public static void setListOfClients(List<Client> listOfClients) {
        ModelList.listOfClients = listOfClients;
    }

    public static void setListOfCategories(List<Category> listOfCategories) {
        ModelList.listOfCategories = listOfCategories;
    }
}
