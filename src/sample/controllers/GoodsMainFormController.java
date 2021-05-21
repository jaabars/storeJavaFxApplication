package sample.controllers;

import javafx.beans.property.SimpleStringProperty;
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
import sample.models.dto.ProductDto;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GoodsMainFormController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private MenuItem mnItemClose;

    @FXML
    private MenuItem mnItemAdd;

    @FXML
    private MenuItem mnItemEdit;

    @FXML
    private MenuItem mnItemDelete;

    @FXML
    private MenuItem mnItemHelp;

    @FXML
    private TableView<ProductDto> tblViewGoods;


    @FXML
    private TableColumn<ProductDto,Long> colmnId;

    @FXML
    private TableColumn<ProductDto, String> colmnName;

    @FXML
    private TableColumn<ProductDto, String> colmBarcode;

    @FXML
    private TableColumn<ProductDto, String> columnPrice;

    @FXML
    private TableColumn<ProductDto, String> colmnActiveFrom;

    @FXML
    private TableColumn<ProductDto, String> colmnCategory;

    @FXML
    private TableColumn<ProductDto, String> colmActiveUntil;

    @FXML
    void onMenuItemClicked(ActionEvent event) {
        if(event.getSource().equals(mnItemAdd)){
            addNewGoods();
        }else if (event.getSource().equals(mnItemEdit)){
            editCurrentGood();
        }
    }

    private void editCurrentGood() {
        ProductDto productDto = tblViewGoods.getSelectionModel().getSelectedItem();
        if (productDto==null){
            Alert alert = new Alert(Alert.AlertType.WARNING,"Выберите товар который хотите отредактировать!");
            alert.show();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/goodEditAndAddForm.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            GoodsEditAndAddFormController goodsEditAndAddFormController = loader.getController();
            goodsEditAndAddFormController.initDataToEdit(stage,productDto);
            stage.show();
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    initTableView();
                    tblViewGoods.refresh();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void addNewGoods() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/goodEditAndAddForm.fxml"));
            loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.getRoot()));
            GoodsEditAndAddFormController goodsEditAndAddFormController = loader.getController();
            goodsEditAndAddFormController.initData(stage);
            stage.show();
            stage.setOnCloseRequest(event -> {
                initTableView();
                tblViewGoods.refresh();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        initTableView();
        initData();
    }

    private void initData() {
        colmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colmnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colmBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
        colmnCategory.setCellValueFactory(columnCategory->new SimpleStringProperty(columnCategory.getValue().getCategory().getName()));
        colmnActiveFrom.setCellValueFactory(columnActiveFrom->new SimpleStringProperty(columnActiveFrom.getValue().getPrice().getStartDate().toString()));
        colmActiveUntil.setCellValueFactory(columnActiveUntil->new SimpleStringProperty(columnActiveUntil.getValue().getPrice().getEndDate().toString()));
        columnPrice.setCellValueFactory(colmnPrice->new SimpleStringProperty(colmnPrice.getValue().getPrice().getPrice().toString()));
    }

    private void initTableView() {
        List<ProductDto> productDtoList = DbHelper.INSTANCE.getAllProducts();
        if (productDtoList.isEmpty()){
            tblViewGoods.setPlaceholder(new Label("Нет данных для отображения"));
            return;
        }
        tblViewGoods.setItems(FXCollections.observableArrayList(productDtoList));
    }
}
