package hr.java.production.controller;

import hr.java.production.model.Item;
import hr.java.production.model.Store;
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

public class AddNewStoreController {

    @FXML
    private TextField storeNameTextField;
    @FXML
    private TextField storeWebsiteTextField;
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

    private static final Logger logger = LoggerFactory.getLogger(AddNewStoreController.class);
    private StringBuilder errorMessages = new StringBuilder();
    //checks if website link is valid
    private final Pattern patternURL =
            Pattern.compile("[-a-zA-Z0-9:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9():%_+.~#?&/=]*)");


    public void initialize() {


        ObservableList<Item> itemObservableList = FXCollections.observableList(ModelList.getListOfItems());

        selectedNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        unselectedNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        unselectedItemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        selectedItemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        unselectedItemsTableView.getItems().addAll(itemObservableList);

        selectedItemsTableView.getItems().addAll(FXCollections.observableList(new ArrayList<>()));

        addSelectedItem.setOnAction(actionEvent -> {
            List<Item> selectedItems = unselectedItemsTableView.
                    getSelectionModel().getSelectedItems().stream().filter(Objects::nonNull).toList();

            unselectedItemsTableView.getItems().removeAll(selectedItems);
            selectedItemsTableView.getItems().addAll(selectedItems);
            unselectedItemsTableView.getSelectionModel().clearSelection();
        });

        removeSelectedItem.setOnAction(actionEvent -> {
            List<Item> selectedItems = selectedItemsTableView.
                    getSelectionModel().getSelectedItems().stream().filter(Objects::nonNull).toList();

            selectedItemsTableView.getItems().removeAll(selectedItems);
            unselectedItemsTableView.getItems().addAll(selectedItems);
            selectedItemsTableView.getSelectionModel().clearSelection();
        });


    }

    @FXML
    protected void onSave() {
        Alert alert;
        String storeNameString = storeNameTextField.getText();
        ExecutorService executorService = Executors.newCachedThreadPool();


        if (storeNameString.isBlank()) {
            errorMessages.append("Store name can't be blank.\n");
        }
        String storeWebsiteString = storeWebsiteTextField.getText();
        if (storeWebsiteString.isBlank()) {
            errorMessages.append("Store website can't be blank.\n");
        }else if (!patternURL.matcher(storeWebsiteString).matches()) {
            errorMessages.append("Website URL in wrong format.\n");
        }


        if (errorMessages.isEmpty()) {
            ArrayList<Item> selectedItems = new ArrayList<>(selectedItemsTableView.getItems().stream().toList());
            Set<Item> itemSet = new TreeSet<>(Comparator.comparing(Item::getName));
            if (selectedItems.isEmpty()) {
                errorMessages.append("No Items were selected.\n");
            } else {

                itemSet.addAll(selectedItems);

                Future<Long> storeIDFuture = executorService.submit(() -> ModelList.db
                        .saveStoreToDatabase(storeNameString
                        , storeWebsiteString, (TreeSet<Item>) itemSet));

                Long storeID = null;
                try {
                    storeID = storeIDFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.getMessage());
                }

                if (storeID > 0) {
                    ModelList.getListOfStores().add(new Store(storeNameString, storeWebsiteString,
                            (TreeSet<Item>) itemSet,
                            storeID));
                } else {
                    errorMessages.append("Error while saving Store.\n");
                }

            }

        }

        ModelList.awaitTerminationAfterShutdown(executorService);

        if (!errorMessages.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Store not saved!");
            alert.setContentText(errorMessages.toString());
            errorMessages = new StringBuilder();

        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeed!");
            alert.setHeaderText("Store saved!");
            alert.setContentText(storeNameString + " saved to the database!");
            storeNameTextField.clear();
            storeWebsiteTextField.clear();
            unselectedItemsTableView.getItems().clear();
            selectedItemsTableView.getItems().clear();

            selectedItemsTableView.getItems().addAll(FXCollections.observableList(new ArrayList<>()));
            unselectedItemsTableView.getItems().addAll(FXCollections.observableList(ModelList.getListOfItems()));
        }
        alert.showAndWait();
    }

}
