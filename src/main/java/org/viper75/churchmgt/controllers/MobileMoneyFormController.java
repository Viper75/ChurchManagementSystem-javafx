package org.viper75.churchmgt.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.enumeration.AccountStatus;
import org.viper75.churchmgt.model.MobileMoney;
import org.viper75.churchmgt.utils.AlertUtil;

import java.net.URL;
import java.util.ResourceBundle;

public class MobileMoneyFormController implements Initializable {

    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private ComboBox<String> accountNameCB;
    @FXML private ComboBox<AccountStatus> accountStatusCB;
    @FXML private TextField merchantCodeTF;
    private TableView<MobileMoney> tableView;

    private boolean edit;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        AlertUtil.showAlert(Alert.AlertType.INFORMATION, String.valueOf(edit));

        //Initializing account statuses
        accountStatusCB.getItems().addAll(AccountStatus.values());
        accountStatusCB.getSelectionModel().select(AccountStatus.ACTIVE);

        //Initialing accountNameCB
        accountNameCB.getItems().addAll("Ecocash", "Telecash", "One Money");

        if (edit){
            MobileMoney mobileMoney = tableView.getSelectionModel().getSelectedItem();
            accountNameCB.getSelectionModel().select(mobileMoney.getProvider());
            merchantCodeTF.setText(String.valueOf(mobileMoney.getCode()));
            accountStatusCB.getSelectionModel().select(mobileMoney.getAccountStatus());
        }

        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) ((Node)(e).getSource()).getScene().getWindow();
            stage.close();
        });

        saveBtn.setOnAction(this::handleSaveBtn_Click);
    }

    private void handleSaveBtn_Click(ActionEvent actionEvent) {
        String accountName = accountNameCB.getSelectionModel().getSelectedItem();
        if (accountName == null)
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Please select account name.");

        if (merchantCodeTF.getText().equals(""))
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Please enter a merchant code.");

        int merchantCode = Integer.valueOf(merchantCodeTF.getText());

        MobileMoney mobileMoney = null;
        if (edit){
            mobileMoney = tableView.getSelectionModel().getSelectedItem();
            mobileMoney.setMerchantCode(merchantCode);
            mobileMoney.setProvider(accountName);
            mobileMoney.setAccountStatus(accountStatusCB.getValue());
        } else {
            mobileMoney = MobileMoney.builder().provider(accountName).merchantCode(merchantCode).build();
            tableView.getItems().add(mobileMoney);
        }
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        session.save(mobileMoney);
        session.getTransaction().commit();
        session.close();

        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Account saved successfully.");

        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    public void setMobileMoneyTableView(TableView<MobileMoney> tableView, boolean edit){
        this.tableView = tableView;
        this.edit = edit;
    }
}
