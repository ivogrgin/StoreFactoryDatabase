package hr.java.production.controller;

import hr.java.production.model.*;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Controller for searching transactions
 */
public class SearchTransactionController {
    @FXML
    private TextField clientFirstNameTextField;
    @FXML
    private TextField clientLastNameTextField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private TableView<Transaction> resultsTableView;
    @FXML
    private TableColumn<Transaction, String> idTableColumn;
    @FXML
    private TableColumn<Transaction, String> clientTableColumn;
    @FXML
    private TableColumn<Transaction, String> itemsTableColumn;
    @FXML
    private TableColumn<Transaction, String> transactionDateTableColumn;

    public void initialize()
    {

        ObservableList<Transaction> transactionObservableList = FXCollections.observableArrayList(ModelList
                .getListOfTransactions());

        idTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getId().toString()));
        itemsTableColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData
                .getValue().getItems()
                .stream().map(item -> item.getName().concat("\n")).collect(Collectors.joining())));
        clientTableColumn.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleStringProperty(transactionStringCellDataFeatures.getValue().getClient().getFirstName()
                        + " " + transactionStringCellDataFeatures.getValue().getClient().getLastName()));
        transactionDateTableColumn.setCellValueFactory(transactionStringCellDataFeatures ->
                new SimpleStringProperty(DateTimeFormatter.ofPattern("dd.MM.yyyy.")
                        .format(transactionStringCellDataFeatures.getValue().getTransactionDate())));

        resultsTableView.setItems(transactionObservableList);

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


    }

    public void onSearchButtonClick()
    {
        ObservableList<Transaction> transactionObservableList = FXCollections.observableArrayList(ModelList
                .getListOfTransactions());

        String clientFirstName = clientFirstNameTextField.getText();
        String clientLastName = clientLastNameTextField.getText();
        LocalDate transactionDate = datePicker.getValue();

        if(clientFirstName != null)
        {
            transactionObservableList = FXCollections.observableList(transactionObservableList
                    .stream().filter(transaction -> transaction.getClient().getFirstName().toLowerCase(Locale.ROOT)
                            .contains(clientFirstName.toLowerCase(Locale.ROOT))).toList());
        }

        if (clientLastName != null)
        {
            transactionObservableList = FXCollections.observableList(transactionObservableList
                    .stream().filter(transaction -> transaction.getClient().getLastName().toLowerCase(Locale.ROOT)
                            .contains(clientLastName.toLowerCase(Locale.ROOT))).toList());
        }

        if(transactionDate != null)
        {
            transactionObservableList = FXCollections.observableList(transactionObservableList
                    .stream().filter(transaction -> transaction.getTransactionDate()
                            .isEqual(transactionDate)).toList());
        }

        resultsTableView.setItems(transactionObservableList);


    }

}