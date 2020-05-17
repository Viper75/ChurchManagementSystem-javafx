package org.viper75.churchmgt.controllers;

import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.enumeration.AccountStatus;
import org.viper75.churchmgt.model.ChurchInfo;
import org.viper75.churchmgt.model.Currency;
import org.viper75.churchmgt.model.MobileMoney;
import org.viper75.churchmgt.model.embeddable.Address;
import org.viper75.churchmgt.utils.AlertUtil;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrganizationSetupController implements Initializable {

    @FXML private Button saveAndCloseBtn;
    @FXML private TabPane organizationSettingsTP;
    @FXML private Tab paymentDetailsTab;
    @FXML private Tab identificationTab;

    /* Identity Tab Components */
    @FXML
    private TextField churchNameTF;
    @FXML private TextField addressLine1TF;
    @FXML private TextField addressLine2TF;
    @FXML private TextField addressLine3TF;
    @FXML private TextField addressLine4TF;
    @FXML private TextField contactPersonTF;
    @FXML private TextField telephoneNumberTF;
    @FXML private TextField faxNumberTF;
    @FXML private TextField contactCellphone;
    @FXML private TextField emailAddress;
    @FXML private TextField churchLogoPath;
    @FXML private TextField churchMoto;

    @FXML private Button addLogoPathBtn;
    @FXML private Button deleteLogoPath;
    /* ./Identity Tab Components */

    /* Currencies Tab Components */
    @FXML private Button addCurrencyBtn;
    @FXML private TextField currencyNameTF;
    @FXML private TextField currencyRateTF;
    @FXML private TableView<Currency> currencyTV;
    @FXML private TableColumn<Currency, String> currencyNameTC;
    @FXML private TableColumn<Currency, BigDecimal> currencyRateTC;
    @FXML private Tab currenciesTab;

    private boolean editCurrecny = false;
    /* ./Currencies Tab Components */

    /* Payment Details Tab Controls*/
    @FXML private Tab mobileMoneyTab;
    /* BANK ACCOUNTS TAB CONTROLS*/

    /* ./BANK ACCOUNTS TAB CONTROLS*/

    /* MOBILE MONEY TAB CONTROLS*/
        @FXML private Button addMobileMoneyBtn;
        @FXML private Button editMobileMoneyBtn;
        @FXML private Button deleteMobileMoneyBtn;

        @FXML private TableView<MobileMoney> mobileMoneyTableView;
        @FXML private TableColumn<MobileMoney, Integer> mobileMoneyCodeCol;
        @FXML private TableColumn<MobileMoney, String> mobileMoneyServiceProviderCol;
        @FXML private TableColumn<MobileMoney, Integer> mobileMoneyMerchantCodeCol;
        @FXML private TableColumn<MobileMoney, AccountStatus> mobileMoneyAccountStatusCol;
    /* ./MOBILE MONEY TAB CONTROLS*/
    /* ./Payment Details Tab Controls*/

    private SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        organizationSettingsTP.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            switch (newValue.getId()){
                case "identificationTab":
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/OrganizationSetupIdentification.fxml"));
                    try {
                        Parent root = loader.load();
                        identificationTab.setContent(root);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        });

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/OrganizationSetupIdentification.fxml"));
        try {
            Parent root = loader.load();
            identificationTab.setContent(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
        organizationSettingsTP.getSelectionModel().select(identificationTab);

        //Handle Save And Close Button Click
        saveAndCloseBtn.setOnAction(this::handleSaveAndCloseBtn_Click);


        /* Payment Details Tab*/
        mobileMoneyTab.setOnSelectionChanged(e -> loadMobileMoney());
        /* Mobile Money */
        addMobileMoneyBtn.setOnAction(e -> handleAddMobileMoneyBtn_Click());
        editMobileMoneyBtn.setOnAction(e -> handleEditMobileMoneyBtn_Click());
        mobileMoneyCodeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
        mobileMoneyServiceProviderCol.setCellValueFactory(new PropertyValueFactory<>("provider"));
        mobileMoneyMerchantCodeCol.setCellValueFactory(new PropertyValueFactory<>("merchantCode"));
        mobileMoneyAccountStatusCol.setCellValueFactory(new PropertyValueFactory<>("accountStatus"));
        /* ./Mobile Money*/
        /* ./Payment Details Tab*/

        /* Currency Tab */

        currencyNameTF.setTextFormatter(new TextFormatter<>((change) -> {
            change.setText(change.getText().toUpperCase());
            return change;
        }));

        //Defining CurrencyDTO Table Columns Cell Value Factory
        currencyNameTC.setCellValueFactory(new PropertyValueFactory<>("currencyName"));
        currencyRateTC.setCellValueFactory(new PropertyValueFactory<>("currencyRate"));

        final ContextMenu rowMenu = new ContextMenu();
        MenuItem editItem = new MenuItem("Edit");
        MenuItem removeItem = new MenuItem("Delete");
        editItem.setOnAction(e -> {
            Currency currency = currencyTV.getSelectionModel().getSelectedItem();
            currencyNameTF.setText(currency.getCurrencyName());
            editCurrecny = true;
            addCurrencyBtn.setText("Save");
            currencyNameTF.setEditable(false);
            currencyRateTF.setText(String.format("%.2f", currency.getCurrencyRate()));
            currencyRateTF.requestFocus();
        });

        removeItem.setOnAction(e -> {
            Currency currency = currencyTV.getSelectionModel().getSelectedItem();
            Session session = sessionFactory.openSession();
            session.beginTransaction();
            session.delete(currency);
            session.getTransaction().commit();
            currencyTV.getItems().remove(currency);
        });
        rowMenu.getItems().addAll(editItem, removeItem);

        currencyTV.setContextMenu(rowMenu);

        //Handle Add CurrencyDTO Button Click
        addCurrencyBtn.setOnAction(e -> handleAddCurrecnyBtn_Click());

        //Handle TableView Item double clicked
        currencyTV.setOnMouseClicked(me -> {
            if (me.getClickCount() == 2){
                editCurrecny = true;
                Currency currency = currencyTV.getSelectionModel().getSelectedItem();
                currencyNameTF.setText(currency.getCurrencyName());
                currencyNameTF.setEditable(false);
                currencyRateTF.setText(String.valueOf(currency.getCurrencyRate()));
                currencyRateTF.requestFocus();
                addCurrencyBtn.setText("Save");
            }
        });

        currenciesTab.setOnSelectionChanged(e -> loadCurrencies());
        /* ./Currency Tab*/

    /* Payment Details Tab*/


    }

    private void loadMobileMoney() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<MobileMoney> criteriaQuery = session.getCriteriaBuilder().createQuery(MobileMoney.class);
        criteriaQuery.from(MobileMoney.class);
        List<MobileMoney> mobileMoneyList = session.createQuery(criteriaQuery).getResultList();
        session.close();
        mobileMoneyTableView.getItems().addAll(mobileMoneyList);
    }
    /* Mobile Money Tab*/

    private void handleAddMobileMoneyBtn_Click() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/MobileMoneyForm.fxml"));

        Parent root = null;
        try {
            root = loader.load();
            MobileMoneyFormController mobileMoneyFormController = loader.getController();
            mobileMoneyFormController.setMobileMoneyTableView(mobileMoneyTableView, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage.setTitle("Mobile Money Details");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }


    private void handleEditMobileMoneyBtn_Click() {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/MobileMoneyForm.fxml"));

        Parent root = null;
        try {
            root = loader.load();
            MobileMoneyFormController mobileMoneyFormController = loader.getController();
            mobileMoneyFormController.setMobileMoneyTableView(mobileMoneyTableView, true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scene = new Scene(root);
        stage.setTitle("Mobile Money Details");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    /* ./Mobile Money Tab*/
    /* ./Payment Details Tab*/

    /* Identity Tab*/

    private void choosePathToLogo() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpeg, *.jpg)", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        churchLogoPath.setText(file.getAbsolutePath());
    }

    /* ./Identity Tab*/

    /* Currency Tab*/

    private void loadCurrencies() {
        Session session = sessionFactory.openSession();
        CriteriaQuery<Currency> criteriaQuery = session.getCriteriaBuilder().createQuery(Currency.class);
        criteriaQuery.from(Currency.class);
        List<Currency> currencies = session.createQuery(criteriaQuery).getResultList();
        session.close();

        currencyNameTF.setText("");
        currencyRateTF.setText("");
        addCurrencyBtn.setText("Add");
        editCurrecny = false;

        currencyTV.getItems().clear();
        currencyTV.getItems().addAll(currencies);
    }

    private void handleAddCurrecnyBtn_Click() {
        String currencyName = currencyNameTF.getText();
        BigDecimal currencyRate = ((!currencyRateTF.getText().equals(""))? BigDecimal.valueOf(Double.valueOf(currencyRateTF.getText())) : BigDecimal.valueOf(0));

        if (currencyName.equals("") || currencyRate.equals(0)){
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Invalid currency");
            return;
        }

        Session session = sessionFactory.openSession();
        if (editCurrecny){
            Currency currency = currencyTV.getSelectionModel().getSelectedItem();
            currency.setCurrencyName(currencyName);
            currency.setCurrencyRate(currencyRate);

            session.beginTransaction();
            session.update(currency);
            session.getTransaction().commit();

            editCurrecny = false;
            addCurrencyBtn.setText("Add");
        } else{
            Currency currency = Currency.builder().currencyName(currencyName).currencyRate(currencyRate).build();
            currencyTV.getItems().add(currency);

            session.beginTransaction();
            session.save(currency);
            session.getTransaction().commit();
        }
        session.close();

        currencyTV.refresh();
        currencyNameTF.clear();
        currencyRateTF.clear();
    }

    /* ./Currency Tab*/

    private void handleSaveAndCloseBtn_Click(ActionEvent event) {

    }
}
