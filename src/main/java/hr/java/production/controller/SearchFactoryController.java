package hr.java.production.controller;

import hr.java.production.model.Factory;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.Optional;
import java.util.stream.Collectors;

public class SearchFactoryController {
    @FXML
    private TextField factoryNameTextField;
    @FXML
    private TableView<Factory> resultsTableView;
    @FXML
    private TableColumn<Factory, String> nameTableColumn;
    @FXML
    private TableColumn<Factory, String> addressTableColumn;
    @FXML
    private TableColumn<Factory, String> itemsTableColumn;
    @FXML
    private TableColumn<Factory, String> idTableColumn;




    public void initialize()
    {

        ObservableList<Factory> factoriesObservableList = FXCollections.observableList(ModelList.getListOfFactories());

        idTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId().toString()));
        nameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));
        addressTableColumn.setCellValueFactory(cellData -> {
            if(Optional.ofNullable(cellData.getValue().getAddress().getCity()).isPresent())
            {
                return new SimpleStringProperty(cellData.getValue().getAddress().getStreet()+
                                       " " + cellData.getValue().getAddress().getHouseNumber()+ "\n"
                        + cellData.getValue().getAddress()
                                        .getPostalCode() + " " + cellData.getValue().getAddress()
                                       .getCity().getCityName());
            }else
            {
                return new SimpleStringProperty(cellData.getValue().getAddress().getStreet() +
                        " " + cellData.getValue().getAddress().getHouseNumber()+ "\n" + cellData.getValue().getAddress()
                        .getPostalCode() + " " + cellData.getValue().getAddress()
                        .getCityString());
            }
        });

        itemsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData
                .getValue().getItems()
                .stream().map(item -> item.getName().concat("\n")).collect(Collectors.joining())));

        resultsTableView.setItems(factoriesObservableList);

    }



    @FXML
    protected void onSearchButtonClick()
    {
        String factoryNameString = factoryNameTextField.getText();

        resultsTableView.setItems( FXCollections.observableList(ModelList.getListOfFactories().stream()
                .filter(category -> category.getName().toLowerCase()
                        .contains(factoryNameString.toLowerCase())).collect(Collectors.toList())));
    }
}
