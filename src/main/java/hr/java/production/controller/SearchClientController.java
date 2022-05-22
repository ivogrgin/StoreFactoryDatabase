package hr.java.production.controller;

import hr.java.production.model.Address;
import hr.java.production.model.Client;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller for searching Clients
 */
public class SearchClientController {

    @FXML
    private TextField clientFirstNameTextField;
    @FXML
    private TextField clientLastNameTextField;
    @FXML
    private TableView<Client> resultsTableView;
    @FXML
    private TableView<Address> resultsAddressTableView;
    @FXML
    private TableColumn<Client, String> addressTableColumn;
    @FXML
    private TableColumn<Client, String> firstNameTableColumn;
    @FXML
    private TableColumn<Client, String> lastNameTableColumn;
    @FXML
    private TableColumn<Client, String> birthDateTableColumn;
    @FXML
    private TableColumn<Client, String> idTableColumn;
    @FXML
    private TableColumn<Address, String> addressPickerTableColumn;
    @FXML
    private DatePicker datePicker;





    public void initialize() {
        ContextMenu ContextMenu = new ContextMenu();
        MenuItem mi1 = new MenuItem("Edit Client");
        MenuItem mi2 = new MenuItem("Add new Transaction");

        resultsTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, MouseClicked -> {
            if(MouseClicked.getButton() == MouseButton.SECONDARY && Optional.ofNullable(resultsTableView
                    .getSelectionModel().getSelectedItem()).isPresent()) {
                ContextMenu.show(resultsTableView, MouseClicked.getScreenX(), MouseClicked.getScreenY());
            }
        });

        datePicker.setConverter(new StringConverter<>() {
            final String pattern = "dd.MM.yyyy.";
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                datePicker.setPromptText(pattern.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        mi1.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("clientAdd.fxml"));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 400);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((AddNewClientController) fxmlLoader.getController()).initData(resultsTableView.getSelectionModel()
                    .getSelectedItem());

            FirstScreen.getStage().setScene(scene);
            FirstScreen.getStage().show();

        });

        mi2.setOnAction(actionEvent -> {
            FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("transactionAdd.fxml"));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 400);
            } catch (IOException e) {
                e.printStackTrace();
            }
            ((AddNewTransactionController) fxmlLoader.getController()).initData(resultsTableView.getSelectionModel()
                    .getSelectedItem());

            FirstScreen.getStage().setScene(scene);
            FirstScreen.getStage().show();

        });

        ContextMenu.getItems().addAll(mi1,mi2);

        ObservableList<Client> clientObservableArrayList = FXCollections.observableArrayList(ModelList
                .getListOfClients());
        ObservableSet<Address> addressObservableSet = FXCollections
                .observableSet(clientObservableArrayList.stream().map(Client::getAddress).collect(Collectors.toSet()));
        idTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getId().toString()));
        firstNameTableColumn.setCellValueFactory(clientStringCellDataFeatures ->
                new SimpleStringProperty(clientStringCellDataFeatures.getValue().getFirstName()));
        lastNameTableColumn.setCellValueFactory(clientStringCellDataFeatures ->
                new SimpleStringProperty(clientStringCellDataFeatures.getValue().getLastName()));
        birthDateTableColumn.setCellValueFactory(clientStringCellDataFeatures ->
                new SimpleStringProperty(DateTimeFormatter.ofPattern("dd.MM.yyyy.")
                        .format(clientStringCellDataFeatures.getValue().getDateOfBirth())));

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
                        " " + cellData.getValue().getAddress().getHouseNumber()+ "\n" + cellData
                        .getValue().getAddress()
                        .getPostalCode() + " " + cellData.getValue().getAddress()
                        .getCityString());
            }
        });
        addressPickerTableColumn.setCellValueFactory(cellData -> {
            if(Optional.ofNullable(cellData.getValue().getCity()).isPresent())
            {
                return new SimpleStringProperty(cellData.getValue().getStreet()+
                        " " + cellData.getValue().getHouseNumber()+ "\n"
                        + cellData.getValue().getPostalCode() + " " + cellData.getValue().getCity().getCityName());
            }else
            {
                return new SimpleStringProperty(cellData.getValue().getStreet() +
                        " " + cellData.getValue().getHouseNumber()+ "\n" + cellData
                        .getValue().getPostalCode() + " " + cellData.getValue().getCityString());
            }
        });

        resultsTableView.setItems(clientObservableArrayList);
        resultsAddressTableView.setItems(FXCollections.observableArrayList(addressObservableSet.stream().toList()));



    }

    @FXML
    private void onSearchButtonClick(){
        ObservableList<Client> clientObservableList = FXCollections.observableArrayList(ModelList.getListOfClients());
        if (resultsAddressTableView.getSelectionModel().getSelectedItem() != null)
        {
            clientObservableList = FXCollections.observableArrayList(clientObservableList.stream()
                    .filter(client -> client.getAddress().equals(resultsAddressTableView.getSelectionModel()
                            .getSelectedItem())).toList());

            resultsAddressTableView.getSelectionModel().clearSelection();


        }

        if(clientFirstNameTextField.getText() != null && !clientFirstNameTextField.getText().isEmpty())
        {

            clientObservableList = FXCollections.observableArrayList(clientObservableList.stream()
                    .filter(client -> client.getFirstName()
                            .toLowerCase(Locale.ROOT).contains(clientFirstNameTextField
                            .getText().toLowerCase(Locale.ROOT))).toList());
            clientFirstNameTextField.clear();
        }

        if(clientLastNameTextField.getText() != null && !clientLastNameTextField.getText().isEmpty())
        {
            clientObservableList = FXCollections.observableArrayList(clientObservableList.stream()
                    .filter(client -> client.getLastName()
                            .toLowerCase(Locale.ROOT).contains(clientLastNameTextField.getText()
                            .toLowerCase(Locale.ROOT))).toList());
            clientLastNameTextField.clear();
        }
        if(datePicker.getValue() != null)
        {


            clientObservableList = FXCollections.observableArrayList(clientObservableList.stream()
                    .filter(client -> client.getDateOfBirth().isEqual(datePicker.getValue())).toList());

            datePicker.setValue(null);

        }

        resultsTableView.setItems(clientObservableList);

    }

}