package hr.java.production.controller;

import hr.java.production.model.Store;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.stream.Collectors;

public class SearchStoreController {
    @FXML
    private TextField storeNameTextField;
    @FXML
    private TableView<Store> resultsTableView;
    @FXML
    private TableColumn<Store, String> nameTableColumn;
    @FXML
    private TableColumn<Store, String> webAddressTableColumn;
    @FXML
    private TableColumn<Store, String> itemsTableColumn;
    @FXML
    private TableColumn<Store, String> idTableColumn;




    public void initialize()
    {
        ObservableList<Store> storeObservableList = FXCollections.observableList(ModelList.getListOfStores());

        idTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        nameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        webAddressTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWebAddress()));
        itemsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getItems()
                .stream().map(item -> item.getName().concat("\n")).collect(Collectors.joining())));
        resultsTableView.setItems(storeObservableList);
    }



    @FXML
    protected void onSearchButtonClick()
    {
        String storeNameString = storeNameTextField.getText();

        resultsTableView.setItems( FXCollections.observableList(ModelList.getListOfStores().stream()
                .filter(category -> category.getName().toLowerCase()
                        .contains(storeNameString.toLowerCase())).collect(Collectors.toList())));

    }
}
