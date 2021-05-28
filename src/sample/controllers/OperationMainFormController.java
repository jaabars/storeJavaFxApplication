package sample.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.db.DbHelper;
import sample.models.OperationDetail;
import sample.models.dto.OperationDto;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class OperationMainFormController {
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<OperationDto> tblViewOperations;

    @FXML
    private TableColumn<OperationDto, Long> colmnId;

    @FXML
    private TableColumn<OperationDto, LocalDate> colmnSoldDate;

    @FXML
    private TableColumn<OperationDto, Double> colmnTotalSum;

    @FXML
    private TableColumn<OperationDto, String> colmnUserLogin;

    @FXML
    private TableView<OperationDetail> tblViewOperationDetails;

    @FXML
    private TableColumn<OperationDetail, Long> colmnItemsId;

    @FXML
    private TableColumn<OperationDetail, String> colmnGoodsName;

    @FXML
    private TableColumn<OperationDetail, Integer> colmnQuantity;

    @FXML
    private TableColumn<OperationDetail, Double> colmnSum;

    @FXML
    void initialize() {
        initOperationsTableView();
        initData();
        tblViewOperations.getSelectionModel().selectedItemProperty()
        .addListener(new ChangeListener<OperationDto>() {
            @Override
            public void changed(ObservableValue<? extends OperationDto> observable, OperationDto oldValue, OperationDto newValue) {
                if (newValue!=null) {
                    initOperationsDetails(newValue);
                }
            }
        });
    }

    private void initOperationsDetails(OperationDto newValue) {
        List<OperationDetail> operationDetailList = DbHelper.INSTANCE.getAllOperationDetails(newValue);
        if (operationDetailList.isEmpty()){
            tblViewOperationDetails.setPlaceholder(new Label("Нет данных для отображения"));
        }
        tblViewOperationDetails.setItems(FXCollections.observableArrayList(operationDetailList));
    }

    private void initData() {
        colmnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colmnSoldDate.setCellValueFactory(new PropertyValueFactory<>("addDate"));
        colmnTotalSum.setCellValueFactory(new PropertyValueFactory<>("sum"));
        colmnUserLogin.setCellValueFactory(colmn->new SimpleStringProperty(colmn.getValue().getAccount().getLogin()));
        colmnItemsId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colmnGoodsName.setCellValueFactory(column->new SimpleStringProperty(column.getValue().getProduct().getName()));
        colmnSum.setCellValueFactory(new PropertyValueFactory<>("price"));
        colmnQuantity.setCellValueFactory(new PropertyValueFactory<>("amount"));
    }

    private void initOperationsTableView() {
        List<OperationDto> operationDtoList = DbHelper.INSTANCE.getAllOperations();
        if (operationDtoList.isEmpty()){
            tblViewOperations.setPlaceholder(new Label("Нет данных для отображения"));
        }
        tblViewOperations.setItems(FXCollections.observableArrayList(operationDtoList));
    }
}
