package org.viper75.churchmgt.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.model.MemberType;
import org.viper75.churchmgt.utils.AlertUtil;
import org.viper75.churchmgt.utils.TextFieldUtil;

import javax.persistence.Query;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddMemberTypeController implements Initializable {

    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;

    @FXML private TextField memberTypeTF;

    private ComboBox<MemberType> memberTypeCB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //adding limitter
        TextFieldUtil.addTextLimiter(memberTypeTF, 20);

        //Cancel Button Handler
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();
        });

        //Save Button Handler
        saveBtn.setOnAction(this::handleSaveBtn_Click);
    }

    private void handleSaveBtn_Click(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        String memberType = memberTypeTF.getText();
        if (!memberType.equals("")){
            SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();
            Session session = sessionFactory.openSession();

            String memberTypeHql = "FROM " + MemberType.class.getName() + " mt WHERE mt.memberTypeName LIKE :memberTypeName ORDER BY mt.memberTypeName ASC";
            Query query = session.createQuery(memberTypeHql);
            query.setParameter("memberTypeName", memberType);
            List<MemberType> memberTypes = query.getResultList();

            if (memberTypes.isEmpty()){
                session.beginTransaction();
                MemberType memberType1 = MemberType.builder().memberTypeName(memberType).build();
                session.save(memberType1);
                session.getTransaction().commit();

                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Member type: " + memberType + " has been successfully added.");
                memberTypeCB.getItems().add(memberType1);
                stage.close();
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Member type: " + memberType + " already exists in the database." );
            }
        } else {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "You cannot save a blank member type");
        }
    }

    public void setMemberTypeCB(ComboBox<MemberType> memberTypeCB) {
        this.memberTypeCB = memberTypeCB;
    }
}
