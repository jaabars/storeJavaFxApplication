package sample.controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import sample.db.DbHelper;
import sample.models.Account;
import sample.models.User;

public class RegisterController {

    private Stage stage;
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtLogin;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtApprovePassword;

    @FXML
    private Button btnRegister;

    @FXML
    private Button btnCancel;

    @FXML
    void onButtonClicked(ActionEvent event) {
        if (event.getSource().equals(btnRegister)){
            boolean validate = validate();
           if (!validate){
               return;
           }
           boolean isLoginUnique = DbHelper.INSTANCE.isLoginUnique(txtLogin.getText());
           if (!isLoginUnique){
               Alert alert = new Alert(Alert.AlertType.WARNING,"Логин уже существует");
               alert.show();
               return;
           }
           boolean approvePassword = approvePassword();
           if (!approvePassword){
               return;
           }
           registerNewUser();
        }
    }

    private void registerNewUser() {
        User user = new User();
        user.setName(txtName.getText());
        user = DbHelper.INSTANCE.saveUser(user);
        if (user!=null&&user.getId()!=null){
        Account account = new Account();
        account.setUser(user);
        account.setLogin(txtLogin.getText());
        account.setActive(true);
        account.setPassword(txtPassword.getText());
        boolean isSaved = DbHelper.INSTANCE.saveAccount(account);
        if (isSaved){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Вы успешно зарегистрировались!");
            alert.show();
            alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                @Override
                public void handle(DialogEvent event) {
                    Stage stage = (Stage) btnRegister.getScene().getWindow();
                    stage.close();
                }
            });
        }
        }
    }

    private boolean approvePassword() {
        if (!txtPassword.getText().equals(txtApprovePassword.getText())){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Пароли не совпадают!");
            alert.show();
            return false;
        }
        return true;
    }

    private boolean validate() {
        if (txtName.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Заполните поле имя");
            alert.show();
            return false;
        }else if (txtLogin.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Заполните поле логин");
            alert.show();
            return false;
        }else if (txtPassword.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Заполните поле пароль");
            alert.show();
            return false;
        }else if(txtApprovePassword.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Заполните поле подтвердить пароль");
            alert.show();
            return false;
        }
        return true;
    }

    public void initData(Stage stage) {
        this.stage = stage;
    }
}
