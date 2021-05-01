package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.db.DbHelper;
import sample.models.Account;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnEnter;

    @FXML
    private Button btnCancel;

    @FXML
    private Button btnRegister;

    @FXML
    void onButtonClicked(ActionEvent event) throws IOException {
        if (event.getSource().equals(btnEnter)){
            login();
        }else if (event.getSource().equals(btnCancel)){
            Stage stage = (Stage)btnCancel.getScene().getWindow();
            stage.close();
        }else if (event.getSource().equals(btnRegister)){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/registrationForm.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            RegisterController registerController = loader.getController();
            registerController.initData(stage);
            stage.show();
        }
    }

    private void login() {
        if (txtLogin.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Поле логин не может быть пустым!");
            alert.show();
            return;
        }else if (txtPassword.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Пароль не может быть пустым!");
            alert.show();
            return;
        }
        Account account = DbHelper.INSTANCE.findUserByLogin(txtLogin.getText());
        if (account==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Пользователь не существует");
            alert.show();
            return;
        }
        if (!account.isActive()){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Пользователь не активен, обратитесь в службу поддержки!");
            alert.show();
            return;
        }
        if(!account.getPassword().equals(txtPassword)){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Неверный пароль");
            alert.show();
            return;
        }
        System.out.println("Успешно");

    }
}
