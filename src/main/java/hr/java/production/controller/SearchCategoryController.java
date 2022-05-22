package hr.java.production.controller;

import hr.java.production.model.Category;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.stream.Collectors;

public class SearchCategoryController {
    @FXML
    private TextField categoryNameTextField;
    @FXML
    private TableView<Category> resultsTableView;
    @FXML
    private TableColumn<Category, String> nameTableColumn;
    @FXML
    private TableColumn<Category, String> categoryDescriptionTableColumn;
    @FXML
    private TableColumn<Category, String> idTableColumn;



    public void initialize()
    {
        ObservableList<Category> categoryObservableList = FXCollections.observableList(ModelList.getListOfCategories());

        idTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        categoryDescriptionTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescription()));
        nameTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        resultsTableView.setItems(categoryObservableList);
    }


    @FXML
    protected void onSearchButtonClick()
    {

        String categoryNameString = categoryNameTextField.getText();

        resultsTableView.setItems( FXCollections.observableList(ModelList.getListOfCategories().stream()
                .filter(category -> category.getName().toLowerCase()
                        .contains(categoryNameString.toLowerCase())).collect(Collectors.toList())));




    }
}
