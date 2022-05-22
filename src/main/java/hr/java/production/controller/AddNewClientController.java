package hr.java.production.controller;

import hr.java.production.model.Address;
import hr.java.production.model.Client;
import hr.java.production.threads.UpdateClientInDatabaseThread;
import hr.java.production.util.ModelList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


/**
 * controller for adding new client to database
 */
public class AddNewClientController {
    @FXML
    private TextField clientFirstNameTextField;
    @FXML
    private TextField clientLastNameTextField;
    @FXML
    private TextField streetNameTextField;
    @FXML
    private TextField houseNumberTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private DatePicker datePicker;

    @FXML
    private Text heading;

    private static final Logger logger = LoggerFactory.getLogger(AddNewClientController.class);
    private StringBuilder errorMessages = new StringBuilder();
    public Boolean editMode = false;
    public long clientIDEdit = 0;

    public void initData(Client clientToEdit) {
        if (Optional.ofNullable(clientToEdit).isPresent()) {
            heading.setText("Editing Client: " + clientToEdit.getFirstName() + " " + clientToEdit.getLastName());
            clientIDEdit = clientToEdit.getId();
            clientFirstNameTextField.setText(clientToEdit.getFirstName());
            clientLastNameTextField.setText(clientToEdit.getLastName());
            streetNameTextField.setText(clientToEdit.getAddress().getStreet());
            houseNumberTextField.setText(clientToEdit.getAddress().getHouseNumber());
            if (clientToEdit.getAddress().getCity() != null) {
                cityTextField.setText(clientToEdit.getAddress().getCity().getCityName());
            } else {
                cityTextField.setText(clientToEdit.getAddress().getCityString());
            }
            postalCodeTextField.setText(clientToEdit.getAddress().getPostalCode().toString());
            datePicker.setValue(clientToEdit.getDateOfBirth());
            editMode = true;
        }


    }

    public void initialize()
    {
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


    @FXML
    protected void onSave() {

        //System.out.println(editMode);
        Client clientToAdd = null;
        ExecutorService executorService = Executors.newCachedThreadPool();
        Alert alert;
        List<String> textFields = new ArrayList<>();
        textFields.add(clientFirstNameTextField.getText());
        textFields.add(clientLastNameTextField.getText());
        textFields.add(streetNameTextField.getText());
        textFields.add(houseNumberTextField.getText());
        textFields.add(cityTextField.getText());
        textFields.add(postalCodeTextField.getText());
        LocalDate dateOfBirth = datePicker.getValue();
        if (textFields.stream().noneMatch(Objects::isNull) && dateOfBirth != null) {

            Address addressToSave = new Address.Builder().setStreet(textFields.get(2)).setHouseNumber(textFields.get(3))
                    .setCityString(textFields.get(4)).setPostalCode(Long.parseLong(textFields.get(5))).build();


            if (!editMode) {

                Address finalAddressToSave1 = addressToSave;
                Future<Long> addressIDFuture = executorService.submit( () -> ModelList.db
                        .saveAddressToDatabase(finalAddressToSave1));
                Long addressID = null;
                try {
                    addressID = addressIDFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.getMessage());
                }
                if (addressID > 0) {

                    addressToSave.setId(addressID);
                    ModelList.getListOfAddresses().add(addressToSave);
                } else {
                    errorMessages.append("Problem with saving Client to Database. Please try again.\n");
                }



                if (errorMessages.isEmpty()) {
                    Address finalAddressToSave2 = addressToSave;
                    Future<Long> clientIDFuture = executorService.submit(() -> ModelList.db
                            .saveClientToDatabase(textFields.get(0),textFields.get(1), dateOfBirth
                            , finalAddressToSave2));
                    Long clientID = null;
                    try {
                        clientID = clientIDFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }

                    if (clientID > 0) {
                        clientToAdd = new Client(clientID, textFields.get(0)
                                , textFields.get(1), dateOfBirth, addressToSave);
                        ModelList.getListOfClients().add(clientToAdd);
                    } else {
                        errorMessages.append("Problem with saving address to Database. Please try again.\n");
                    }
                }


            } else {
                Address finalAddressToSave = addressToSave;

                if (ModelList.getListOfAddresses().stream().noneMatch(address ->
                        address.getCityString().equals(finalAddressToSave.getCityString())
                                && address.getStreet().equals(finalAddressToSave.getStreet())
                                && address.getPostalCode().equals(finalAddressToSave.getPostalCode())
                                && address.getHouseNumber().equals(finalAddressToSave.getHouseNumber()))) {

                    Future<Long> addressIDFuture = executorService.submit(() -> ModelList.db
                            .saveAddressToDatabase(finalAddressToSave));
                    Long addressID = null;
                    try {
                        addressID = addressIDFuture.get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error(e.getMessage());
                    }
                    if (addressID > 0) {

                        addressToSave.setId(addressID);
                        ModelList.getListOfAddresses().add(addressToSave);
                    } else {
                        errorMessages.append("Problem with saving Address to Database. Please try again.\n");
                    }
                } else {
                    addressToSave = ModelList.getListOfClients().stream()
                            .filter(client -> client.getId().equals(clientIDEdit)).findFirst().get().getAddress();
                }

                if (errorMessages.isEmpty()) {
                    clientToAdd = new Client(clientIDEdit, textFields.get(0)
                            , textFields.get(1), dateOfBirth, addressToSave);

                    Future<Long> clientUpdate = executorService.submit(new UpdateClientInDatabaseThread(clientToAdd));
                    Long clientUpdateID = null;
                    try {
                        clientUpdateID = clientUpdate.get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error(e.getMessage());
                    }

                    if (clientUpdateID == clientIDEdit) {

                        ModelList.getListOfClients().removeIf(client -> client.getId().equals(clientIDEdit));
                        ModelList.getListOfClients().add(clientToAdd);
                    } else {
                        errorMessages.append("Problem with saving address to Database. Please try again.\n");
                    }
                }
            }

        } else {
            errorMessages.append("Some mandatory fields were left blank. Please fill All fields.\n");


        }
        ModelList.awaitTerminationAfterShutdown(executorService);

        if (!errorMessages.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Client not saved!");
            alert.setContentText(errorMessages.toString());

            errorMessages = new StringBuilder();
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeed!");
            alert.setHeaderText("Client saved!");
            alert.setContentText(clientToAdd.getFirstName() +" "+ clientToAdd.getLastName() + " saved to the database!");

            FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("clientSearch.fxml"));

            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load(), 800, 400);
            } catch (IOException e) {
                logger.error(e.getMessage());
            }

            FirstScreen.getStage().setScene(scene);
            FirstScreen.getStage().show();

        }

        alert.showAndWait();

    }


}
