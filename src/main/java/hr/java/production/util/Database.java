package hr.java.production.util;

import hr.java.production.enums.Cities;
import hr.java.production.model.*;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Database {

    private static Boolean activeDatabaseConnection = false;

    private synchronized Connection connectToDatabase() throws IOException, SQLException {

        while (activeDatabaseConnection) {
            try {

                System.out.println("WAITING");

                wait();


            } catch (InterruptedException ex) {
                ex.printStackTrace();

            }

        }
        
        activeDatabaseConnection = true;
        Properties configuration = new Properties();
        configuration.load(new FileReader("dat/database.properties"));

        String databaseURL = configuration.getProperty("databaseURL");
        String databaseUsername = configuration.getProperty("databaseUsername");
        String databasePassword = configuration.getProperty("databasePassword");

        return DriverManager.getConnection(databaseURL, databaseUsername, databasePassword);
    }

    private synchronized void closeConnectionToDatabase(Connection connection) throws SQLException {


        if(Optional.ofNullable(connection).isPresent())
        {
            connection.close();
        }

        activeDatabaseConnection = false;
        notifyAll();
        

    }

    public ArrayList<Category> getAllCategoriesFromDatabase() throws SQLException, IOException {

        Connection connection = null;
        List<Category> categoryList = new ArrayList<>();
        try {
            connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet categoryResultSet = sqlStatement.executeQuery("SELECT * FROM CATEGORY;");

            while (categoryResultSet.next()) {
                Category newCategory = getCategoryFromResultSet(categoryResultSet);
                categoryList.add(newCategory);
            }
        } finally {

                closeConnectionToDatabase(connection);
            
        }



        return (ArrayList<Category>) categoryList;
    }

    private static Category getCategoryFromResultSet(ResultSet categoryResultSet) throws SQLException {

        Long id = categoryResultSet.getLong("ID");
        String name = categoryResultSet.getString("NAME");
        String description = categoryResultSet.getString("DESCRIPTION");

        return new Category(name, description, id);
    }

    public ArrayList<Item> getAllItemsFromDatabase() throws SQLException, IOException {

        Connection connection = null;
        List<Item> itemList = new ArrayList<>();
        try {
            connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet itemResultSet = sqlStatement.executeQuery("SELECT * FROM ITEM;");
            while (itemResultSet.next()) {
                Item newItem = getItemFromResultSet(itemResultSet, false);
                itemList.add(newItem);
            }
        }finally {

            assert connection != null;
            closeConnectionToDatabase(connection);
            
        }


        return (ArrayList<Item>) itemList;
    }

    private static Item getItemFromResultSet(ResultSet itemResultSet, Boolean fromStoreOrFactory) throws SQLException {
        Long id;

        if (fromStoreOrFactory) {
            id = itemResultSet.getLong("ITEM_ID");

        } else {

            id = itemResultSet.getLong("ID");

        }

        String name = itemResultSet.getString("NAME");
        Long categoryId = itemResultSet.getLong("CATEGORY_ID");
        //Category itemCategory = getCategoryById(categoryId);
        Category itemCategory = ModelList.getListOfCategories().stream()
                .filter(category -> category.getId().equals(categoryId)).findFirst().get();
        BigDecimal width = itemResultSet.getBigDecimal("WIDTH");
        BigDecimal height = itemResultSet.getBigDecimal("HEIGHT");
        BigDecimal length = itemResultSet.getBigDecimal("LENGTH");
        BigDecimal productionCost = itemResultSet.getBigDecimal("PRODUCTION_COST");
        BigDecimal sellingPrice = itemResultSet.getBigDecimal("SELLING_PRICE");

        return new Item(name, itemCategory, width, height, length, productionCost, sellingPrice, id);
    }

    public ArrayList<Client> getAllClientsFromDatabase() throws SQLException, IOException {
        Connection connection = null;
        List<Client> clientList = new ArrayList<>();
        try {
            connection = connectToDatabase();
            clientList = new ArrayList<>();
            Statement sqlStatement = connection.createStatement();
            ResultSet clientResultSet = sqlStatement.executeQuery("SELECT * FROM CLIENT;");
            while (clientResultSet.next()) {
                Client newClient = getClientFromResultSet(clientResultSet);
                clientList.add(newClient);
            }
        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }


        return (ArrayList<Client>) clientList;
    }

    private static Client getClientFromResultSet(ResultSet clientResultSet) throws SQLException {
        Long id = clientResultSet.getLong("ID");
        String firstName = clientResultSet.getString("FIRST_NAME");
        String lastName = clientResultSet.getString("LAST_NAME");
        LocalDate dateOfBirth = LocalDate.parse(clientResultSet.getDate("DATE_OF_BIRTH").toString()
                , DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        Long addressID = clientResultSet.getLong("ADDRESS_ID");
        Address clientAddress = ModelList.getListOfAddresses()
                .stream().filter(address -> address.getId().equals(addressID)).findFirst().get();

        return new Client(id, firstName, lastName, dateOfBirth, clientAddress);
    }

    public ArrayList<Address> getAllAddressesFromDatabase() throws SQLException, IOException {

        Connection connection = null;
        List<Address> addressList = new ArrayList<>();
        try {
            connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet addressResultSet = sqlStatement.executeQuery("SELECT * FROM ADDRESS;");
            while (addressResultSet.next()) {
                Address newAddress = getAddressFromResultSet(addressResultSet);
                addressList.add(newAddress);
            }
        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }


        return (ArrayList<Address>) addressList;
    }

    private static Address getAddressFromResultSet(ResultSet addressResultSet) throws SQLException {

        Optional<Cities> city = Optional.empty();

        Long id = addressResultSet.getLong("ID");
        Long postalCode = addressResultSet.getLong("POSTAL_CODE");
        String street = addressResultSet.getString("STREET");
        String houseNumber = addressResultSet.getString("HOUSE_NUMBER");
        String cityString = addressResultSet.getString("CITY");


        if(Arrays.stream(Cities.values()).anyMatch(cities -> cities.getCityName().equals(cityString)))
        {

            city = Arrays.stream(Cities.values()).filter(cities -> cities.getCityName().equals(cityString))
                    .findFirst();
        }

        if(city.isPresent())
        {
            return new Address.Builder().setId(id).setStreet(street).setHouseNumber(houseNumber).setCity(city.get())
                    .setPostalCode(Long.parseLong(city.get().getPostalCode())).build();
        }else
        {
            return new Address.Builder().setId(id).setStreet(street).setHouseNumber(houseNumber)
                    .setCityString(cityString)
                    .setPostalCode(postalCode).build();
        }

    }

    public ArrayList<Factory> getAllFactoriesFromDatabase() throws SQLException, IOException {

        Connection connection = null;
        List<Factory> factoryList = new ArrayList<>();
        try {
            connection = connectToDatabase();
            Statement sqlStatement = connection.createStatement();
            ResultSet factoryResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY;");

            while (factoryResultSet.next()) {
                Factory newFactory = getFactoryFromResultSet(factoryResultSet, connection);
                factoryList.add(newFactory);
            }
        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }


        return (ArrayList<Factory>) factoryList;
    }

    private Factory getFactoryFromResultSet(ResultSet factoryResultSet, Connection connection)
            throws SQLException {

        Long id = factoryResultSet.getLong("ID");
        String name = factoryResultSet.getString("NAME");
        Long addressId = factoryResultSet.getLong("ADDRESS_ID");
        //Address chosenAddress = getAddressById(addressId);
        Address chosenAddress = ModelList.getListOfAddresses().stream()
                .filter(address -> address.getId().equals(addressId)).findFirst().get();

        Set<Item> items = new TreeSet<>(Comparator.comparing(Item::getName));
        Statement sqlStatement = connection.createStatement();
        ResultSet factoryItemsResultSet = sqlStatement.executeQuery("SELECT * FROM FACTORY_ITEM FI" +
                ", ITEM I WHERE FI.FACTORY_ID = "+ id +" AND FI.ITEM_ID = I.ID");

        while (factoryItemsResultSet.next()) {
            Item newItem = getItemFromResultSet(factoryItemsResultSet, true);
            items.add(newItem);
        }


        return new Factory(name, chosenAddress, (TreeSet<Item>) items, id);
    }

    public ArrayList<Transaction> getAllTransactionsFromDatabase() throws SQLException, IOException {

        Connection connection = null;
        List<Transaction> transactionList = new ArrayList<>();
        try {
            connection = connectToDatabase();
            transactionList = new ArrayList<>();
            Statement sqlStatement = connection.createStatement();
            ResultSet transactionResultSet = sqlStatement.executeQuery("SELECT * FROM TRANSACTION;");

            while (transactionResultSet.next()) {
                Transaction newTransaction = getTransactionFromResultSet(transactionResultSet, connection);
                transactionList.add(newTransaction);
            }
        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }


        return (ArrayList<Transaction>) transactionList;
    }

    private Transaction getTransactionFromResultSet(ResultSet transactionResultSet, Connection connection)
            throws SQLException {

        Long id = transactionResultSet.getLong("ID");
        Long clientID = transactionResultSet.getLong("CLIENT_ID");
        LocalDate transactionDate = transactionResultSet.getDate("TRANSACTION_DATE").toLocalDate();
        Client chosenClient = ModelList.getListOfClients().stream()
                .filter(client -> client.getId().equals(clientID)).findFirst().get();

        Set<Item> items = new TreeSet<>(Comparator.comparing(Item::getName));
        Statement sqlStatement = connection.createStatement();
        ResultSet transactionItemsResultSet = sqlStatement.executeQuery("SELECT * FROM TRANSACTION_ITEM TI" +
                ", ITEM I WHERE TI.TRANSACTION_ID = "+ id +" AND TI.ITEM_ID = I.ID");

        while (transactionItemsResultSet.next()) {
            Item newItem = getItemFromResultSet(transactionItemsResultSet, true);
            items.add(newItem);
        }




        return new Transaction("T" + id, id, chosenClient, transactionDate, items);
    }

    public ArrayList<Store> getAllStoresFromDatabase() throws SQLException, IOException {

        Connection connection = null;
        List<Store> storeList = new ArrayList<>();
        try {
            connection = connectToDatabase();

            Statement sqlStatement = connection.createStatement();
            ResultSet storeResultSet = sqlStatement.executeQuery("SELECT * FROM STORE;");
            while (storeResultSet.next()) {
                Store newStore = getStoreFromResultSet(storeResultSet, connection);
                storeList.add(newStore);
            }
        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }


        return (ArrayList<Store>) storeList;
    }

    private Store getStoreFromResultSet(ResultSet storeResultSet, Connection connection)
            throws SQLException {
        Long id = storeResultSet.getLong("ID");
        String name = storeResultSet.getString("NAME");
        String webAddress = storeResultSet.getString("WEB_ADDRESS");
        Set<Item> items = new TreeSet<>(Comparator.comparing(Item::getName));
        Statement sqlStatement = connection.createStatement();
        ResultSet storeItemsResultSet = sqlStatement.executeQuery("SELECT * FROM STORE_ITEM SI" +
                ", ITEM I WHERE SI.STORE_ID = "+ id +" AND SI.ITEM_ID = I.ID;");

        while (storeItemsResultSet.next()) {
            Item newItem = getItemFromResultSet(storeItemsResultSet, true);
            items.add(newItem);
        }

        return new Store(name, webAddress, (TreeSet<Item>) items, id);

    }

    public Long updateClientToDatabase(Client clientUpdate) throws SQLException, IOException {
        Connection connection = null;
        Long id = 0L;
        try{
            connection = connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement(
                    "UPDATE CLIENT SET FIRST_NAME = ?, LAST_NAME = ?," +
                            " DATE_OF_BIRTH = ?, ADDRESS_ID = ? WHERE ID = "+ clientUpdate.getId() +";", Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, clientUpdate.getFirstName());
            stmt.setString(2, clientUpdate.getLastName());
            stmt.setDate(3, Date.valueOf(clientUpdate.getDateOfBirth()));
            stmt.setLong(4, clientUpdate.getAddress().getId());
            stmt.executeUpdate();

            ResultSet getid = stmt.getGeneratedKeys();
            getid.first();
            id = getid.getLong(1);


        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return id;
    }

    public Long saveCategoryToDatabase(String name, String description) throws SQLException, IOException {

        Connection connection = null;
        Long id = 0L;
        try{
            connection = connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO " +
                    "CATEGORY(NAME, DESCRIPTION) VALUES(?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, name);
            stmt.setString(2, description);
            stmt.executeUpdate();
            ResultSet getid = stmt.getGeneratedKeys();
            getid.first();
            id = getid.getLong(1);


        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return id;
    }

    public Long saveItemToDatabase(String itemNameString, Category itemCategory,
                                      BigDecimal width, BigDecimal height, BigDecimal length,
                                      BigDecimal productionCost, BigDecimal sellingPrice) throws SQLException, IOException {
        Connection connection = null;
        Long id = 0L;
        try{
            connection = connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO " +
                    "ITEM(CATEGORY_ID, NAME, WIDTH, HEIGHT, LENGTH, PRODUCTION_COST, SELLING_PRICE) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

            stmt.setLong(1, itemCategory.getId());
            stmt.setString(2, itemNameString);
            stmt.setBigDecimal(3, width);
            stmt.setBigDecimal(4, height);
            stmt.setBigDecimal(5, length);
            stmt.setBigDecimal(6, productionCost);
            stmt.setBigDecimal(7, sellingPrice);
            stmt.executeUpdate();
            ResultSet getid = stmt.getGeneratedKeys();
            getid.first();
            id = getid.getLong(1);


        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return id;
    }

    public Long saveClientToDatabase(String firstName, String lastName, LocalDate dateOfBirth, Address address) throws SQLException, IOException {
        Connection connection = null;
        Long id = 0L;
        try{
            connection = connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO CLIENT(FIRST_NAME," +
                    " LAST_NAME, DATE_OF_BIRTH, ADDRESS_ID) VALUES(?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            stmt.setDate(3, Date.valueOf(dateOfBirth.format(DateTimeFormatter
                    .ofPattern("yyyy-MM-dd"))));
            stmt.setLong(4, address.getId());
            stmt.executeUpdate();
            ResultSet getid = stmt.getGeneratedKeys();
            getid.first();
            id = getid.getLong(1);



        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return id;
    }



    public Long saveAddressToDatabase(Address addressToAdd) throws SQLException, IOException {
        Connection connection = null;
        Long id = 0L;
        try{
            connection = connectToDatabase();

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO " +
                    "ADDRESS(STREET, HOUSE_NUMBER, CITY, POSTAL_CODE) " +
                    "VALUES(?, ?, ?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, addressToAdd.getStreet());
            stmt.setString(2, addressToAdd.getHouseNumber());

            if (addressToAdd.getCity() == null)
            {
                stmt.setString(3, addressToAdd.getCityString());
            }else
            {
                stmt.setString(3, addressToAdd.getCity().getCityName());
            }

            stmt.setLong(4, addressToAdd.getPostalCode());
            stmt.executeUpdate();
            ResultSet getid = stmt.getGeneratedKeys();
            getid.first();
            id = getid.getLong(1);
        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return id;
    }

    public Long saveTransactionToDatabase(LocalDate transactionDate, List<Item> items, Client client) throws SQLException, IOException {
        Connection connection = null;
        Long transactionId = 0L;
        try{
            connection = connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO TRANSACTION" +
                    "(CLIENT_ID, TRANSACTION_DATE) VALUES(?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setLong(1, client.getId());
            stmt.setDate(2, Date.valueOf(transactionDate));
            stmt.executeUpdate();
            ResultSet getid = stmt.getGeneratedKeys();
            getid.first();
            transactionId = getid.getLong(1);
            PreparedStatement itemStmt = connection.prepareStatement("INSERT INTO" +
                    " TRANSACTION_ITEM(TRANSACTION_ID," +
                    " ITEM_ID) VALUES(?,?);");

            for (Item item : items) {
                    itemStmt.setLong(1, transactionId);
                    itemStmt.setLong(2, item.getId());
                    itemStmt.executeUpdate();

            }


        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return transactionId;
    }

    public Long saveFactoryToDatabase(String factoryNameString, Address address, Set<Item> itemSet) throws SQLException, IOException {
        Connection connection = null;
        Long factoryId = 0L;
        try{
            connection = connectToDatabase();
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO FACTORY(NAME, ADDRESS_ID) " +
                    "VALUES(?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, factoryNameString);
            stmt.setLong(2, address.getId());
            stmt.executeUpdate();
            ResultSet getid = stmt.getGeneratedKeys();
            getid.first();
            factoryId = getid.getLong(1);
            PreparedStatement itemStmt = connection.prepareStatement("INSERT INTO " +
                    "FACTORY_ITEM(FACTORY_ID, ITEM_ID) VALUES(?, ?);");

            for (Item item : itemSet) {

                    itemStmt.setLong(1, factoryId);
                    itemStmt.setLong(2, item.getId());
                    itemStmt.executeUpdate();
            }

        } finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return factoryId;
    }

    public Long saveStoreToDatabase(String storeNameString, String storeWebsiteString, TreeSet<Item> itemSet) throws SQLException, IOException {
        Connection connection = null;
        Long storeId = 0L;
        try{
            connection = connectToDatabase();

            PreparedStatement stmt = connection.prepareStatement("INSERT INTO STORE(NAME, WEB_ADDRESS) " +
                    "VALUES(?, ?);", Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, storeNameString);
            stmt.setString(2, storeWebsiteString);
            stmt.executeUpdate();
            ResultSet getID = stmt.getGeneratedKeys();
            getID.first();
            storeId = getID.getLong(1);
            PreparedStatement itemStmt = connection
                    .prepareStatement("INSERT INTO STORE_ITEM(STORE_ID, ITEM_ID) VALUES(?, ?);");

            for (Item item : itemSet) {

                    itemStmt.setLong(1, storeId);
                    itemStmt.setLong(2, item.getId());
                    itemStmt.executeUpdate();


            }


        }finally {
            assert connection != null;
            closeConnectionToDatabase(connection);
        }

        return storeId;
    }


}
