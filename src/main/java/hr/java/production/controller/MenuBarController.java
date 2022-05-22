package hr.java.production.controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;


public class MenuBarController {

    public void showItemSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("itemSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showItemAddScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("itemAdd.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showTransactionSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("transactionSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showTransactionAddScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("transactionAdd.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showClientSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("clientSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showClientAddScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("clientAdd.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }



    public void showCategoryAddScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("categoryAdd.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showStoreAddScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("storeAdd.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showFactoryAddScreen()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("factoryAdd.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showCategorySearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("categorySearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showStoresSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("storesSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }

    public void showFactoriesSearchScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(FirstScreen.class.getResource("factoriesSearch.fxml"));
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load(), 800, 400);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FirstScreen.getStage().setScene(scene);
        FirstScreen.getStage().show();
    }
}
