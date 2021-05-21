package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import sample.models.Account;

import java.io.IOException;

public class MenuFormController {

    private Stage stage;
    private Account account;
    @FXML
    private Button btnCategories;

    @FXML
    private Button btnUsers;

    @FXML
    private Button btnPrice;

    @FXML
    private Button btnGoods;

    @FXML
    private Button btnSell;

    @FXML
    private Button btnOperations;

    public MenuFormController() {
    }

    @FXML
    void onButtonClicked(ActionEvent event) {
        if (event.getSource().equals(btnCategories)){
            openCategoriesMainForm();
        }else if (event.getSource().equals(btnGoods)){
            openGoodsMainForm();
        }
    }

    private void openGoodsMainForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/goodsMainForm.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openCategoriesMainForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/categoriesMainForm.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void initData(Stage stage, Account account) {
        this.stage=stage;
        this.account=account;
    }
}
