package sample.controllers;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sample.db.DbHelper;
import sample.models.Account;
import sample.models.User;
import sample.models.dto.OperationItemDto;
import sample.models.dto.ProductDto;

import java.util.ArrayList;
import java.util.List;

public class SaleFormController {

    private List<OperationItemDto> operationItemDtoList = new ArrayList<>();
    private int num=1;

    private Account account;
    private Stage stage;

    @FXML
    private MenuItem mnItemEdit;

    @FXML
    private TableView<OperationItemDto> tblViewOperations;


    @FXML
    private TableColumn<OperationItemDto, Integer> colmnNum;

    @FXML
    private TableColumn<OperationItemDto, String> collmnGoodName;

    @FXML
    private TableColumn<OperationItemDto, Double> colmnPrice;

    @FXML
    private TableColumn<OperationItemDto, Integer> colmnQuantity;

    @FXML
    private TableColumn<OperationItemDto, Double> colmnSum;


    @FXML
    private TextField txtBarcode;

    @FXML
    private Label lblQuantity;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    private Label lblSum;

    @FXML
    private MenuItem mnItemDelete;

    @FXML
    void onButtonClicked(ActionEvent event) {
        if(event.getSource().equals(btnSave)){
            if (!operationItemDtoList.isEmpty()){
                int result = DbHelper.INSTANCE.saveOperationAndItems(account.getUser().getId(),Double.parseDouble(lblSum.getText()),operationItemDtoList);
                if (result!=1){
                    Alert alert = new Alert(Alert.AlertType.WARNING,"Не удалось сохранить");
                    alert.show();
                }
                refresh();
                lblSum.setText("0");
                lblQuantity.setText("0");
            }
        }
    }
    @FXML
    void initialize() {
        initTblViewColumns();
        txtBarcode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!txtBarcode.getText().isEmpty()) {
                    barcodeEntered(newValue);
                }
            }
        });
    }

    private void barcodeEntered(String barcode) {
        ProductDto productDto = DbHelper.INSTANCE.findProductByBarcode(barcode);
        if (productDto==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Товар не найден");
            alert.show();
            return;
        }
        if(productDto.getAmount()<=0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Товар закончился");
            alert.show();
            return;
        }
        DbHelper.INSTANCE.decrementAmount(productDto.getId());
        long count = operationItemDtoList.stream().filter(operationItemDto -> operationItemDto.getProductId().equals(productDto.getId())).count();
        if (count==0){
            OperationItemDto operationItemDto = new OperationItemDto();
            operationItemDto.setProductId(productDto.getId());
            operationItemDto.setAmount(1);
            operationItemDto.setName(productDto.getName());
            operationItemDto.setPrice(productDto.getPrice().getPrice());
            operationItemDto.setSum(productDto.getPrice().getPrice());
            operationItemDto.setNum(num++);
            operationItemDtoList.add(operationItemDto);
        }else{
            operationItemDtoList.stream().filter(operationItemDto -> operationItemDto.getProductId().equals(productDto.getId()))
                    .forEach(operationItemDto -> {
                        operationItemDto.setAmount(operationItemDto.getAmount()+1);
                        operationItemDto.setSum(operationItemDto.getAmount()*productDto.getPrice().getPrice());
                    });
        }

        Platform.runLater(()->txtBarcode.clear());
        refresh();
        double totalSum = operationItemDtoList.stream()
                .map(operationItemDto -> operationItemDto.getSum())
                .reduce(0.0,(a,b)->a+b);
        lblSum.setText(String.valueOf(totalSum));
    }

    private void refresh() {
        tblViewOperations.setItems(FXCollections.observableArrayList(operationItemDtoList));
        tblViewOperations.refresh();
    }

    private void initTblViewColumns() {
        colmnNum.setCellValueFactory(new PropertyValueFactory<>("num"));
        collmnGoodName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colmnQuantity.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colmnSum.setCellValueFactory(new PropertyValueFactory<>("sum"));
        colmnPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    @FXML
    void onMenuItemClicked(ActionEvent event) {
        if (event.getSource().equals(mnItemDelete)){
            deleteItem();

        }else if (event.getSource().equals(mnItemEdit)){
            decrementAmount();
        }
    }

    private void decrementAmount() {
        OperationItemDto operationItemDto = tblViewOperations.getSelectionModel().getSelectedItem();
        int index = operationItemDtoList.indexOf(operationItemDto);
        if (operationItemDto.getAmount()>1) {
            operationItemDto.setAmount(operationItemDto.getAmount() - 1);
            operationItemDto.setSum(operationItemDto.getSum() - operationItemDto.getPrice());
            double sum = Double.parseDouble(lblSum.getText())-operationItemDto.getPrice();
            lblSum.setText(String.valueOf(sum));
            operationItemDtoList.set(index,operationItemDto);
            DbHelper.INSTANCE.incrementAmount(operationItemDto.getProductId(),1);
            refresh();
        }else {
            deleteItem();
        }
    }

    private void deleteItem() {
        OperationItemDto operationItemDto = tblViewOperations.getSelectionModel().getSelectedItem();
        operationItemDtoList.remove(operationItemDto);
        int amount = operationItemDto.getAmount();
        DbHelper.INSTANCE.incrementAmount(operationItemDto.getProductId(),amount);
        operationItemDtoList.stream().filter(o->o.getNum()>1).forEach(o->o.setNum(o.getNum()-1));
        if (num>1) {
            this.num -= 1;
        }
        double totalSum = Double.parseDouble(lblSum.getText())-operationItemDto.getSum();
        lblSum.setText(String.valueOf(totalSum));
        refresh();
    }

    public void initData(Account account, Stage stage) {
        this.stage = stage;
        this.account = account;
    }

    public void resetData() {
        if (!operationItemDtoList.isEmpty()){
            for (OperationItemDto o:operationItemDtoList){
                DbHelper.INSTANCE.incrementAmount(o.getProductId(),o.getAmount());
            }
        }
        }
}
