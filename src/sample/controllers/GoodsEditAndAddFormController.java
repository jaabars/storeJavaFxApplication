package sample.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.StringConverter;
import sample.db.DbHelper;
import sample.models.Category;
import sample.models.Price;
import sample.models.Product;
import sample.models.dto.ProductDto;

import java.net.URL;
import java.time.chrono.Chronology;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class GoodsEditAndAddFormController {

    private Stage stage;

    private ProductDto productDto;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField txtGoodName;

    @FXML
    private ComboBox<Category> cmbCategories;

    @FXML
    private Spinner<Double> spnPrice;

    @FXML
    private DatePicker dtPckrStart;

    @FXML
    private DatePicker dtPckrEnd;

    @FXML
    private CheckBox chckBoxActive;


    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;


    @FXML
    void onButtonClicked(ActionEvent event) {
        if (event.getSource().equals(btnSave)){
            if (productDto==null){
                saveNewGoods();
            }else {
                updateCurrentGood();
            }
        }
    }

    private void updateCurrentGood() {
        boolean valid = validateAllFields();
        if (!valid){
            return;
        }
        productDto.setName(txtGoodName.getText());
        productDto.getPrice().setPrice(Double.valueOf(spnPrice.getEditor().getText()));
        productDto.getPrice().setStartDate(dtPckrStart.getValue());
        productDto.getPrice().setEndDate(dtPckrEnd.getValue());
        productDto.setActive(chckBoxActive.isSelected());
        productDto.setCategory(cmbCategories.getValue());
        int result = DbHelper.INSTANCE.updateProductAndPrice(productDto);
        if (result==1){
            Alert alert = new Alert(Alert.AlertType.INFORMATION,"Данные успешно сохранены");
            alert.show();
        }

    }

    private void saveNewGoods() {
        System.out.println(cmbCategories.getSelectionModel().getSelectedItem());
        System.out.println(cmbCategories.getValue());
        boolean valid = validateAllFields();
        if (!valid){
            return;
        }
        ProductDto productDto = new ProductDto();
        productDto.setName(txtGoodName.getText());
        productDto.setCategory(cmbCategories.getValue());
        productDto.setBarcode(createBarcode());
        productDto.setActive(chckBoxActive.isSelected());
        productDto.setPrice(new Price(Double.parseDouble(spnPrice.getEditor().getText()),dtPckrStart.getValue(),dtPckrEnd.getValue()));
        int result = DbHelper.INSTANCE.saveProductDtoTransactionMethod(productDto);
        if (result==1){
            System.out.println("успешно");
        }
        txtGoodName.clear();
        cmbCategories.getSelectionModel().select(null);
        dtPckrStart.setChronology(null);
        dtPckrEnd.setChronology(null);
    }

    private String createBarcode() {
        StringBuilder stringBuilder = new StringBuilder();
        int [] nums = {0,1,2,3,4,5,6,7,8,9};
        for (int i=0;i<12;i++){
            Random random = new Random();
            stringBuilder.append(nums[random.nextInt(10)]);
        }
        return stringBuilder.toString();
    }

    private boolean validateAllFields() {
        if (txtGoodName.getText().isEmpty()){
            showAlert("Название товара не должно быть пустым!");
            return false;
        }/*else if (cmbCategories.getSelectionModel().isEmpty()){
            showAlert("Выберите категорию товара");
            return true;
        }*/else if (dtPckrStart.getEditor().getText().isEmpty()){
            showAlert("Выберите дату начала действия цены");
            return false;
        }else if (dtPckrEnd.getEditor().getText().isEmpty()){
            showAlert("Выберите дату окончания цены");
            return false;
        }
        if (dtPckrStart.getValue().isAfter(dtPckrEnd.getValue())){
            showAlert("Дата начала цены не может быть позднее даты окончания цены");
            return false;
        }
        return true;
    }

    private void showAlert(String s) {
        Alert alert = new Alert(Alert.AlertType.WARNING,s);
        alert.show();
    }

    @FXML
    void initialize() {
       initComboBox();
       initSpinner();
       initCategoryComboBox();
    }

    private void initCategoryComboBox() {
        List<Category> categoryList = DbHelper.INSTANCE.getAllActiveCategories();
        cmbCategories.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category object) {
                return object.getName();
            }

            @Override
            public Category fromString(String string) {
                return null;
            }
        });
        cmbCategories.setItems(FXCollections.observableArrayList(categoryList));
    }

    private void initSpinner() {
        spnPrice.setEditable(true);
        spnPrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,10000000,0,1));
    }

    private void initComboBox() {
        cmbCategories.setItems(FXCollections.observableArrayList());
    }

    public void initData(Stage stage) {
        this.stage=stage;
    }

    public void initDataToEdit(Stage stage, ProductDto productDto) {
        this.stage = stage;
        this.productDto = productDto;
        txtGoodName.setText(productDto.getName());
        cmbCategories.getSelectionModel().select(productDto.getCategory());
        chckBoxActive.setSelected(productDto.isActive());
        dtPckrStart.setValue(productDto.getPrice().getStartDate());
        dtPckrEnd.setValue(productDto.getPrice().getEndDate());
        spnPrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0,50000,productDto.getPrice().getPrice(),1));
    }
}
