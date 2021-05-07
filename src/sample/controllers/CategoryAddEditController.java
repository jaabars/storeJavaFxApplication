package sample.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import sample.db.DbHelper;
import sample.models.Category;

import java.net.URL;
import java.util.ResourceBundle;

public class CategoryAddEditController {

    private Category category;

    private Stage stage;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtName;

    @FXML
    private CheckBox chkBoxActive;

    @FXML
    private Button btnAdd;

    @FXML
    void onButtonClicked(ActionEvent event) {
        if (event.getSource().equals(btnAdd)){
            if (category==null){
                saveNewCategory();
            }else{
                editCategory();
            }
        }
    }

    private void editCategory() {
        boolean isValid = validateFields();
        if (!isValid){
            return;
        }
        category.setName(txtName.getText());
        category.setActive(chkBoxActive.isSelected());
        boolean success = DbHelper.INSTANCE.updateCategory(category);
        if (success){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Успешно");
            alert.show();
        }else {
            Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось сохранить");
            alert.show();
        }
    }

    private void saveNewCategory() {
        boolean isValid = validateFields();
        if (!isValid){
            return;
        }
        Category category = new Category();
        category.setName(txtName.getText());
        category.setActive(chkBoxActive.isSelected());
        boolean success = DbHelper.INSTANCE.saveCategory(category);
        if (success){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Успешно сохранено");
            alert.show();
            txtName.clear();
            chkBoxActive.setSelected(false);
        }else{
            Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось сохранить");
            alert.show();
        }
    }

    private boolean validateFields() {
        if (txtName.getText().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Заполните название");
            alert.show();
            return false;
        }
        return true;
    }

    @FXML
    void initialize() {
        Image plusIcon = new Image("sample/image/plusIconDark.png");
        ImageView imageView = new ImageView(plusIcon);
        imageView.setFitHeight(55);
        imageView.setFitWidth(65);
        imageView.setPreserveRatio(true);
        btnAdd.setGraphic(imageView);

    }

    public void initData(Stage stage) {
        this.stage = stage;
    }

    public void initDataToEditCategory(Stage stage,Category category) {
        this.stage = stage;
        this.category=category;
        txtName.setText(category.getName());
        chkBoxActive.setSelected(category.isActive());
    }
}
