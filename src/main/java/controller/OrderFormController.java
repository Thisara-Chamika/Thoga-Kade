package controller;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.CartTM;
import model.Customer;
import model.Item;
import service.ServiceFactory;
import service.SuperService;
import service.custom.CustomerService;
import service.custom.ItemService;
import service.custom.impl.CustomerServiceImpl;
import util.ServiceEnum;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrderFormController implements Initializable {

    @FXML
    private TableColumn colCode;

    @FXML
    private TableColumn colDes;

    @FXML
    private TableColumn colQty;

    @FXML
    private TableColumn colTotal;

    @FXML
    private TableColumn colUPrice;

    @FXML
    private JFXComboBox comBItem;

    @FXML
    private JFXComboBox combCustomer;

    @FXML
    private Label lblNetPrice;

    @FXML
    private TableView orderTbl;

    @FXML
    private JFXTextField txtAddress;

    @FXML
    private JFXTextField txtCusName;

    @FXML
    private JFXTextField txtDescription;

    @FXML
    private JFXTextField txtQtyOnHand;

    @FXML
    private JFXTextField txtQuantity;

    @FXML
    private JFXTextField txtSalary;

    @FXML
    private JFXTextField txtUnitPrice;

    CustomerService service = ServiceFactory.getInstance().getFactory(ServiceEnum.CUSTOMER);
    ItemService service1 = ServiceFactory.getInstance().getFactory(ServiceEnum.ITEM);

    ArrayList<CartTM> cartTM = new ArrayList<>();

    @FXML
    void addToCartBtnOnAction(ActionEvent event) {

        Double unitPrice = Double.parseDouble(txtUnitPrice.getText());
        Integer qty = Integer.parseInt(txtQuantity.getText());

        Double total = unitPrice * qty;

        cartTM.add(new CartTM(
                comBItem.getValue().toString(),
                txtDescription.getText(),
                unitPrice,
                qty,
                total
        ));

        orderTbl.setItems(FXCollections.observableArrayList(cartTM));

    }

    @FXML
    void placeOrderbtnOnAction(ActionEvent event) {

    }

    void setCustomerCombValue(){
        try {
            combCustomer.setItems(FXCollections.observableArrayList(service.getCustomerIds()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void setItemCombValue(){
        try {
            comBItem.setItems(FXCollections.observableArrayList(service1.getItemCode()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colDes.setCellValueFactory(new PropertyValueFactory<>("description"));
        colUPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        colQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        setCustomerCombValue();
        setItemCombValue();
        combCustomer.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue!=null){
                setTextToValuesCustomer((String) newValue);
            }
        });

        comBItem.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, newValue) -> {
            if (newValue!=null){
                setTextToValuesItem((String) newValue);
            }
        }));

    }
    void setTextToValuesCustomer(String newValue){
        try {
            Customer customer = service.search(newValue);
            txtCusName.setText(customer.getName());
            txtAddress.setText(customer.getAddress());
            txtSalary.setText(customer.getSalary().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    void setTextToValuesItem(String newValue){
        try {
            Item item = service1.search(newValue);
            txtDescription.setText(item.getDescription());
            txtUnitPrice.setText(item.getUnitPrice().toString());
            txtQtyOnHand.setText(item.getQtyOnHand().toString());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

