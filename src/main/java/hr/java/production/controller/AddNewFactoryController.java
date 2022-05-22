package hr.java.production.controller;

import hr.java.production.model.Address;
import hr.java.production.model.Factory;
import hr.java.production.model.Item;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class AddNewFactoryController {

    @FXML
    private TextField factoryNameTextField;
    @FXML
    private TableView<Address> AddressTableView;
    @FXML
    private TableColumn<Address, String> addressNameTableColumn;
    @FXML
    private TableView<Item> unselectedItemsTableView;
    @FXML
    private TableView<Item> selectedItemsTableView;
    @FXML
    private TableColumn<Item, String> unselectedNameTableColumn;
    @FXML
    private TableColumn<Item, String> selectedNameTableColumn;
    @FXML
    private Button addSelectedItem;
    @FXML
    private Button removeSelectedItem;

    private StringBuilder errorMessages = new StringBuilder();
    private static final Logger logger = LoggerFactory.getLogger(AddNewFactoryController.class);
    //checks if website link is valid
    private final Pattern patternURL =
            Pattern.compile("[-a-zA-Z0-9:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9():%_+.~#?&/=]*)");


    @FXML
    public void initialize()
    {

        ObservableList<Item> itemObservableList = FXCollections.observableList(ModelList.getListOfItems());
        ObservableList<Address> addressObservableList = FXCollections.observableList(ModelList.getListOfAddresses());

        selectedNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        unselectedNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        unselectedItemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedItemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        addressNameTableColumn.setCellValueFactory(cellData -> {
            if(Optional.ofNullable(cellData.getValue().getCity()).isPresent())
            {
                return new SimpleStringProperty(cellData.getValue().getStreet()+
                        " " + cellData.getValue().getHouseNumber()+ "\n" + cellData.getValue()
                        .getPostalCode() + " " + cellData.getValue()
                        .getCity().getCityName());
            }else
            {
                return new SimpleStringProperty(cellData.getValue().getStreet()+
                        " " + cellData.getValue().getHouseNumber()+ "\n" + cellData.getValue()
                        .getPostalCode() + " " + cellData.getValue()
                        .getCityString());
            }
        });

        AddressTableView.getItems().addAll(addressObservableList);

        unselectedItemsTableView.getItems().addAll(itemObservableList);
        selectedItemsTableView.getItems().addAll(FXCollections.observableList(new ArrayList<>()));

        addSelectedItem.setOnAction(actionEvent -> {

            List<Item> selectedItems = (new ArrayList<>(unselectedItemsTableView.
                    getSelectionModel().getSelectedItems())).stream().filter(Objects::nonNull).toList();

                unselectedItemsTableView.getItems().removeAll(selectedItems);
                selectedItemsTableView.getItems().addAll(selectedItems);
                unselectedItemsTableView.getSelectionModel().clearSelection();



        });

        removeSelectedItem.setOnAction(actionEvent -> {
            List<Item> selectedItems =  (new ArrayList<>(selectedItemsTableView.
                    getSelectionModel().getSelectedItems())).stream().filter(Objects::nonNull).toList();

            selectedItemsTableView.getItems().removeAll(selectedItems);
            unselectedItemsTableView.getItems().addAll(selectedItems);
            selectedItemsTableView.getSelectionModel().clearSelection();

        });




    }

    @FXML
    protected void onSave(){

        Alert alert;
        ExecutorService executorService = Executors.newCachedThreadPool();
        String factoryNameString = factoryNameTextField.getText();
        if(factoryNameString.isBlank())
        {
            errorMessages.append("Factory name can't be blank.\n");
        }

        Optional<Address> address = Optional.ofNullable(AddressTableView.getSelectionModel().getSelectedItem());

        ArrayList<Item> selectedItems = new ArrayList<>(selectedItemsTableView.getItems().stream().toList());
        Set<Item> itemSet = new TreeSet<>(Comparator.comparing(Item::getName));
        if(errorMessages.isEmpty())
        {
            if(selectedItems.isEmpty())
            {
                errorMessages.append("No Items were selected.\n");
            }else
            {
                itemSet.addAll(selectedItems);
                if(address.isPresent())
                {


                    Future<Long> factoryIDFuture= executorService.submit(() -> ModelList.db
                            .saveFactoryToDatabase(factoryNameString,
                            address.get(), itemSet));

                    Long factoryID = null;
                    try {
                        factoryID = factoryIDFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error(e.getMessage());
                    }

                    if (factoryID > 0) {
                        ModelList.getListOfFactories().add(new Factory(factoryNameString
                                , address.get(), (TreeSet<Item>) itemSet, factoryID));
                    } else {
                        errorMessages.append("Error while saving factory.\n");
                    }

                }else
                {
                    errorMessages.append("Address was not selected.\n");
                }
            }
        }

        ModelList.awaitTerminationAfterShutdown(executorService);

        if (!errorMessages.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Factory not saved!");
            alert.setContentText(errorMessages.toString());
            errorMessages = new StringBuilder();
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeed!");
            alert.setHeaderText("Factory saved!");
            alert.setContentText(factoryNameString + " saved to the database!");

            factoryNameTextField.clear();

            unselectedItemsTableView.getItems().clear();
            selectedItemsTableView.getItems().clear();

            selectedItemsTableView.getItems().addAll(FXCollections.observableList(new ArrayList<>()));
            unselectedItemsTableView.getItems().addAll(FXCollections.observableList(ModelList.getListOfItems()));


        }

        alert.showAndWait();
    }


}
