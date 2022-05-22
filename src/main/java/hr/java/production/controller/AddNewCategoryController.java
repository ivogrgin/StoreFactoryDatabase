package hr.java.production.controller;

import hr.java.production.model.Category;
import hr.java.production.util.ModelList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class AddNewCategoryController {
    @FXML
    private TextField categoryNameTextField;
    @FXML
    private TextArea categoryDescriptionTextArea;

    private static final Logger logger = LoggerFactory.getLogger(AddNewCategoryController.class);
    private StringBuilder errorMessages = new StringBuilder();

    @FXML
    protected void onSave() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Alert alert;
        String categoryNameString = categoryNameTextField.getText();

        if(categoryNameString.isBlank())
        {
            errorMessages.append("Category name can't be empty.\n");
        }
        String categoryDescriptionString = categoryDescriptionTextArea.getText();
        if(categoryDescriptionString.isBlank())
        {
            errorMessages.append("Category description can't be empty.\n");
        }


        if (errorMessages.isEmpty()) {

            Future<Long> categoryIDFuture = executorService.submit(() -> ModelList.db
                    .saveCategoryToDatabase(categoryNameString, categoryDescriptionString));
            Long CategoryID = 0L;
            try {
                CategoryID = categoryIDFuture.get();
            } catch (InterruptedException | ExecutionException e) {
                logger.error(e.getMessage());
            }

            if(CategoryID > 0)
            {
                ModelList.getListOfCategories().add(new Category(categoryNameString, categoryDescriptionString,
                        CategoryID));
            }else
            {
                errorMessages.append("Error while saving item.\n");
            }

        }

        ModelList.awaitTerminationAfterShutdown(executorService);

        if (!errorMessages.isEmpty()) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save action failed!");
            alert.setHeaderText("Category not saved!");
            alert.setContentText(errorMessages.toString());
            errorMessages = new StringBuilder();
        } else {
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save action succeed!");
            alert.setHeaderText("Category saved!");
            alert.setContentText(categoryNameString + " saved to the database!");
            categoryNameTextField.clear();
            categoryDescriptionTextArea.clear();
        }
        alert.showAndWait();

    }




}
