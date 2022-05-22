package hr.java.production.controller;

import hr.java.production.model.Item;
import hr.java.production.model.NamedEntity;
import hr.java.production.util.ModelList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.regex.Pattern;

public class AddNewItemController {

    @FXML
    private TextField itemNameTextField;
    @FXML
    private TextField widthTextField;
    @FXML
    private TextField heightTextField;
    @FXML
    private TextField lengthTextField;
    @FXML
    private TextField productionCostTextField;
    @FXML
    private TextField sellingPriceTextField;
    @FXML
    private ChoiceBox<String> chosenCategoryChoiceBox;

    private static final Logger logger = LoggerFactory.getLogger(AddNewItemController.class);
    private StringBuilder errorMessages = new StringBuilder();
    private final Pattern pattern = Pattern.compile("[a-zA-Z]+");


    public void initialize() {

        chosenCategoryChoiceBox.getItems().addAll(ModelList.getListOfCategories().stream()
                .map(NamedEntity::getName).sorted(String::compareTo).toList());
        chosenCategoryChoiceBox.setValue(ModelList.getListOfCategories().get(0).getName());
    }

    private String checkBigDecimal(String number, int maxLength, String name){
        if ((number.contains(".") && number.length() - 1 > 10)
                || (!number.contains(".") && number.length() > 10))
        {
            return "item "+name+" too long.\n";
        }

        return "";



    }

    @FXML
    protected void onSave() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Alert alert;
        String itemNameString = Optional.ofNullable(itemNameTextField.getText()).orElse("");
        String widthString = Optional.ofNullable(widthTextField.getText()).orElse("");
        String heightString = Optional.ofNullable(heightTextField.getText()).orElse("");
        String lengthString = Optional.ofNullable(lengthTextField.getText()).orElse("");
        String productionCostString = Optional.ofNullable(productionCostTextField.getText()).orElse("");
        String sellingPriceString = Optional.ofNullable(sellingPriceTextField.getText()).orElse("");
        Optional<String> chosenCategory = Optional.ofNullable(chosenCategoryChoiceBox.getValue());

        if (itemNameString.isBlank()
                || widthString.isBlank()
                || heightString.isBlank()
                || lengthString.isBlank()
                || productionCostString.isBlank()
                || sellingPriceString.isBlank()
                || chosenCategory.isEmpty()) {

            errorMessages.append("Some fields were left blank.\n");

        } else if (pattern.matcher(widthString).matches() || pattern.matcher(heightString).matches()
                || pattern.matcher(widthString).matches()
                || pattern.matcher(productionCostString).matches() || pattern.matcher(sellingPriceString).matches()) {

            errorMessages.append("One or more fields contain invalid characters.\n");

        } else {

            if (itemNameString.length() > 25)
            {
                errorMessages.append("item name too long.\n");
            }

            errorMessages.append(checkBigDecimal(widthString, 10, "width"));
            errorMessages.append(checkBigDecimal(heightString, 10, "height"));
            errorMessages.append(checkBigDecimal(lengthString, 10, "length"));
            errorMessages.append(checkBigDecimal(productionCostString, 15, "production cost"));
            errorMessages.append(checkBigDecimal(sellingPriceString, 15, "selling cost"));


            if(errorMessages.isEmpty()) {

                Future<Long> itemIdFuture = executorService.submit(() -> ModelList.db
                        .saveItemToDatabase(itemNameString, ModelList.getListOfCategories().stream()
                                .filter(category -> category.getName().equals(chosenCategory.get())).findFirst().get(),
                        new BigDecimal(widthString)
                        , new BigDecimal(heightString)
                        , new BigDecimal(lengthString)
                        , new BigDecimal(productionCostString)
                        , new BigDecimal(sellingPriceString)));


                Long itemID = null;
                try {
                    itemID = itemIdFuture.get();
                } catch (InterruptedException | ExecutionException e) {
                    logger.error(e.getMessage());
                }
                if (itemID > 0) {

                    ModelList.getListOfItems().add(new Item(itemNameString, ModelList.getListOfCategories().stream()
                            .filter(category -> category.getName().equals(chosenCategory.get())).findFirst().get()
                            , new BigDecimal(widthString)
                            , new BigDecimal(heightString)
                            , new BigDecimal(lengthString)
                            , new BigDecimal(productionCostString)
                            , new BigDecimal(sellingPriceString)
                            , itemID));

                   Future<?> sortList = executorService.submit(() -> ModelList.
                           getListOfItems().sort(Comparator.comparing(Item::getSellingPrice)));
                    try {
                        sortList.get();
                    } catch (InterruptedException | ExecutionException e) {
                        logger.error(e.getMessage());
                    }


                } else {
                    errorMessages.append("Error while saving item.\n");
                }
            }


        }

        ModelList.awaitTerminationAfterShutdown(executorService);

        if (!errorMessages.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Item not saved!");
            alert.setContentText(errorMessages.toString());

            errorMessages = new StringBuilder();
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeed!");
            alert.setHeaderText("Item saved!");
            alert.setContentText(itemNameString + " saved to the database!");
            itemNameTextField.clear();
            widthTextField.clear();
            itemNameTextField.clear();
            heightTextField.clear();
            lengthTextField.clear();
            productionCostTextField.clear();
            sellingPriceTextField.clear();
            chosenCategoryChoiceBox.setValue(ModelList.getListOfCategories().get(0).getName());
        }

        alert.showAndWait();

    }


}
