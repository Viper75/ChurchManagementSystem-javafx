package org.viper75.churchmgt.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.model.ChurchInfo;
import org.viper75.churchmgt.model.embeddable.Address;
import org.viper75.churchmgt.utils.AlertUtil;

import javax.persistence.criteria.CriteriaQuery;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class OrganizationSetupIdentificationController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addLogoPathBtn.setOnAction(e -> choosePathToLogo());

        Session session = HibernateConfiguration.getSessionFactory().openSession();

        CriteriaQuery<ChurchInfo> criteriaQuery = session.getCriteriaBuilder().createQuery(ChurchInfo.class);
        criteriaQuery.from(ChurchInfo.class);
        List<ChurchInfo> churchInfos = session.createQuery(criteriaQuery).getResultList();

        if (!churchInfos.isEmpty()){
            ChurchInfo churchInfo = churchInfos.get(0);
            churchNameTF.setText(churchInfo.getChurchName());
            addressLine1TF.setText(churchInfo.getChurchAddress().getAddressLine1());
            addressLine2TF.setText(churchInfo.getChurchAddress().getAddressLine2());
            addressLine3TF.setText(churchInfo.getChurchAddress().getAddressLine3());
            addressLine4TF.setText(churchInfo.getChurchAddress().getAddressLine4());
            telephoneNumberTF.setText(churchInfo.getTelephoneNumber());
            faxNumberTF.setText(churchInfo.getFaxNumber());
            contactPersonTF.setText(churchInfo.getContactPerson());
            contactCellphone.setText(churchInfo.getCellphoneNumber());
            emailAddress.setText(churchInfo.getEmailAddress());
            churchLogoPath.setText(churchInfo.getLogoPath());
            churchMoto.setText(churchInfo.getMotto());
        }

//        /* ./Identity Tab*/
    }

    private void choosePathToLogo() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image Files (*.png, *.jpeg, *.jpg)", "*.png", "*.jpeg", "*.jpg");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(new Stage());
        churchLogoPath.setText(file.getAbsolutePath());
    }

    private void handleSaveAndCloseBtn_Click(ActionEvent event) {
        String name = churchNameTF.getText();
        String addressLine1 = addressLine1TF.getText();
        String addressLine2 = addressLine2TF.getText();
        String addressLine3 = addressLine3TF.getText();
        String addressLine4 = addressLine4TF.getText();
        String telephoneNumber = telephoneNumberTF.getText();
        String faxNumber = faxNumberTF.getText();
        String contactNumber = contactCellphone.getText();
        String contactPerson = contactPersonTF.getText();
        String email = emailAddress.getText();
        String logoPath = churchLogoPath.getText();
        String motto = churchMoto.getText();

        Session session = HibernateConfiguration.getSessionFactory().openSession();

        CriteriaQuery<ChurchInfo> criteriaQuery = session.getCriteriaBuilder().createQuery(ChurchInfo.class);
        criteriaQuery.from(ChurchInfo.class);
        List<ChurchInfo> churchInfos = session.createQuery(criteriaQuery).getResultList();

        ChurchInfo churchInfo = null;

        Address address = Address.builder().addressLine1(addressLine1).addressLine2(addressLine2).addressLine3(addressLine3).addressLine4(addressLine4).build();

        if (churchInfos.isEmpty()){

            churchInfo = ChurchInfo.builder().churchName(name).churchAddress(address).telephoneNumber(telephoneNumber).faxNumber(faxNumber).cellphoneNumber(contactNumber)
                    .contactPerson(contactPerson).emailAddress(email).logoPath(logoPath).motto(motto).build();
        } else {
            churchInfo = churchInfos.get(0);
            churchInfo.setChurchName(name);
            churchInfo.setChurchAddress(address);
            churchInfo.setTelephoneNumber(telephoneNumber);
            churchInfo.setFaxNumber(faxNumber);
            churchInfo.setContactPerson(contactPerson);
            churchInfo.setCellphoneNumber(contactNumber);
            churchInfo.setEmailAddress(email);
            churchInfo.setLogoPath(logoPath);
            churchInfo.setMotto(motto);
        }

        session.beginTransaction();
        session.save(churchInfo);
        session.getTransaction().commit();
        session.close();

        Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Organization settings saved successfully!");

        stage.close();
    }
}
