package hr.java.production.controller;

import hr.java.production.model.Client;
import hr.java.production.model.Item;
import hr.java.production.model.Transaction;
import hr.java.production.util.ModelList;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddNewTransactionController {

    @FXML
    private TableView<Item> selectedItemsTableView;
    @FXML
    private TableColumn<Item, String> selectedNameTableColumn;
    @FXML
    private Text title;

    private static final Logger logger = LoggerFactory.getLogger(AddNewTransactionController.class);
    private Client client;
    private StringBuilder errorMessages = new StringBuilder();


    public void initData(Client client)
    {
        title.setText("Add transaction for " + client.getFirstName() + " " + client.getLastName());
        this.client = client;
    }
    public void initialize(){

        ObservableList<Item> itemObservableList = FXCollections.observableList(ModelList.getListOfItems());
        selectedItemsTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        selectedNameTableColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getName()));

        selectedItemsTableView.getItems().addAll(itemObservableList);

    }

    public void onSave()
    {
        Alert alert;
        ExecutorService executorService = Executors.newCachedThreadPool();
        if(client != null)
        {

            List<Item> selectedItems = selectedItemsTableView.getSelectionModel().getSelectedItems().stream().toList();
            LocalDate transactionDate = LocalDate.now();

            if (selectedItems.isEmpty())
            {
                errorMessages.append("Select at least 1 item\n");
            }

            if (errorMessages.isEmpty())
            {


                Future<Long> transactionIDFuture = executorService.submit(() -> ModelList.db
                        .saveTransactionToDatabase(transactionDate
                        ,  selectedItems, client));
                Long transactionID = null;
                try {
                    transactionID = transactionIDFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.getMessage());
                }

                if (transactionID > 0) {

                    ModelList.getListOfTransactions().add(new Transaction("T" +
                            transactionID,
                            transactionID, client,
                            transactionDate, new HashSet<>(selectedItems)));
                } else {
                    errorMessages.append("Error while saving factory.\n");
                }
            }
        }else{
            errorMessages.append("There was an error. Client not chosen. Please try again.");
        }

        ModelList.awaitTerminationAfterShutdown(executorService);

        if (!errorMessages.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Transaction not saved!");
            alert.setContentText(errorMessages.toString());
            errorMessages = new StringBuilder();
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeed!");
            alert.setHeaderText("Transaction saved!");
            alert.setContentText("Transaction for "+ client.getFirstName() + " " + client.getLastName() +
                    " saved to the database!");




        }

        alert.showAndWait();

        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("transactionSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }


}
