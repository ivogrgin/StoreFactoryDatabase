package hr.java.production.controller;

import hr.java.production.model.*;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class SearchItemsController {

    @FXML
    private TextField itemNameTextField;
    @FXML
    private ChoiceBox<String> ChosenCategoryChoiceBox;
    @FXML
    private TableView<Item> resultsTableView;
    @FXML
    private TableColumn<Item, String> nameTableColumn;
    @FXML
    private TableColumn<Item, String> categoryTableColumn;
    @FXML
    private TableColumn<Item, String> widthTableColumn;
    @FXML
    private TableColumn<Item, String> heightTableColumn;
    @FXML
    private TableColumn<Item, String> lengthTableColumn;
    @FXML
    private TableColumn<Item, String> productionCostTableColumn;
    @FXML
    private TableColumn<Item, String> sellingPriceTableColumn;
    @FXML
    private TableColumn<Item, String> idTableColumn;


    public void initialize() {


        ChosenCategoryChoiceBox.getItems().addAll(ModelList.getListOfCategories().stream()
                .map(NamedEntity::getName).toList());

        ObservableList<Item> itemObservableList = FXCollections.observableList(ModelList.getListOfItems());

        idTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()
                .getId().toString()));

        categoryTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCategory().getName()));

        widthTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getWidth().toString()));

        heightTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getHeight().toString()));

        lengthTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getLength().toString()));

        productionCostTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProductionCost().toString()));

        sellingPriceTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSellingPrice().toString()));

        nameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        resultsTableView.setItems(itemObservableList);

    }


    @FXML
    protected void onSearchButtonClick() {
        String itemNameString = itemNameTextField.getText();
        Optional<String> chosenCategory = Optional.
                ofNullable(ChosenCategoryChoiceBox.getValue());
        resultsTableView.setItems(FXCollections.observableList(ModelList.getListOfItems().stream().filter(item -> {
                    if (!itemNameString.isEmpty() && chosenCategory.isPresent()) {
                        return item.getName().toLowerCase().contains(itemNameString.toLowerCase())
                                && item.getCategory().getName().equals(chosenCategory.get());
                    } else
                        return chosenCategory.map(s -> item.getCategory().getName().equals(s))
                                .orElseGet(() -> item.getName().toLowerCase(Locale.ROOT).contains(itemNameString
                                        .toLowerCase()));

                }).
                collect(Collectors.toList())));

        resultsTableView.setItems(FXCollections.observableList(ModelList.getListOfItems().stream().filter(item -> {
                    if (!itemNameString.equals("") && chosenCategory.isPresent()) {
                        return item.getName().toLowerCase().contains(itemNameString.toLowerCase())
                                && item.getCategory().getName().equals(chosenCategory.get());
                    } else if (itemNameString.equals("") && chosenCategory.isEmpty()) {
                        return true;
                    } else {
                        return chosenCategory.map(s -> item.getCategory().getName().equals(s))
                                .orElseGet(() -> item.getName().toLowerCase(Locale.ROOT)
                                        .contains(itemNameString.toLowerCase()));
                    }


                }).
                collect(Collectors.toList())));

    }

}
