package sample.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import sample.db.DbHelper;
import sample.models.Category;
import sun.font.DelegatingShape;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class CategoryMainFormController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem mnItemClose;

    @FXML
    private MenuItem mnItemEdit;

    @FXML
    private MenuItem mnItemAdd;

    @FXML
    private MenuItem mnItmDelete;

    @FXML
    private MenuItem mnitemHelp;

    @FXML
    private TableView<Category> tblViewCategories;

    @FXML
    private TableColumn<Category, Long> colmId;

    @FXML
    private TableColumn<Category, String> colmName;

    @FXML
    void onMenuItemClicked(ActionEvent event) {
        if (event.getSource().equals(mnItemAdd)){
            addNewCategory();
        }else if (event.getSource().equals(mnItemEdit)){
            editSelectedCategory();
        }else if (event.getSource().equals(mnItmDelete)) {
            deactivateCategory();
        }else if (event.getSource().equals(mnItemClose)){
            /**
             * Закрыть окно
             * */
        }else if (event.getSource().equals(mnitemHelp)){
            /**
             * Открыть новое окно где будет описание о разделе "Категории" и инструкция по фунцкиональным кнопкам(редактировать,добавить,деактивировать)
             * */
        }
    }

    private void deactivateCategory() {
        Category category = tblViewCategories.getSelectionModel().getSelectedItem();
        if (category==null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Выберите категории которую нужно удалить");
            alert.show();
        }
        boolean isDeactivated = DbHelper.INSTANCE.deactivateCategory(category);
        if (isDeactivated){
            initTableViewCategories();
            tblViewCategories.refresh();
        }
    }

    private void editSelectedCategory() {
        Category category = tblViewCategories.getSelectionModel().getSelectedItem();
        if (category==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Чтобы отредактивировать, выберете категорию");
            alert.show();
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/categoryAddAndEditForm.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            CategoryAddEditController categoryAddEditController = loader.getController();
            categoryAddEditController.initDataToEditCategory(stage,category);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    initTableViewCategories();
                    tblViewCategories.refresh();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addNewCategory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/categoryAddAndEditForm.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            CategoryAddEditController categoryAddEditController = loader.getController();
            categoryAddEditController.initData(stage);
            stage.show();
            stage.setOnCloseRequest(event -> {
                initTableViewCategories();
                tblViewCategories.refresh();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {

        initData();
        initTableViewCategories();
    }

    private void initTableViewCategories() {
        List<Category> categoryList = DbHelper.INSTANCE.getAllCategories();
        if (categoryList.isEmpty()){
            tblViewCategories.setPlaceholder(new Label("Нет данных для отображения"));
        }else {
            categoryList = categoryList.stream().sorted(Comparator.comparing(Category::isActive).reversed()).collect(Collectors.toList());
            tblViewCategories.setItems(FXCollections.observableArrayList(categoryList));
        }
        tblViewCategories.setRowFactory(tv -> new TableRow<Category>() {
            @Override
            protected void updateItem(Category category, boolean empty) {
                super.updateItem(category, empty);
                if (category!=null&&!category.isActive()){
                    setStyle("-fx-background-color: #ff9999;");
                }else {
                setStyle("");
                }
            }
        });
    }

    private void initData() {
        colmId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colmName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }
}
