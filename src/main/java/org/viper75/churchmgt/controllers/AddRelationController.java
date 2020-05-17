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
import org.viper75.churchmgt.model.Relation;
import org.viper75.churchmgt.utils.AlertUtil;
import org.viper75.churchmgt.utils.TextFieldUtil;

import javax.persistence.Query;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class AddRelationController implements Initializable {

    @FXML private Button cancelBtn;
    @FXML private Button saveBtn;
    @FXML private TextField relationTF;

    private ComboBox<Relation> relationCB;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Adding text limitter
        TextFieldUtil.addTextLimiter(relationTF, 20);

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
        String relation = relationTF.getText();
        if (!relation.equals("")){
            SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();
            Session session = sessionFactory.openSession();

            String relationHql = "FROM " + Relation.class.getName() + " r WHERE r.relation LIKE :relation ORDER BY r.relation ASC";
            Query query = session.createQuery(relationHql);
            query.setParameter("relation", relation);
            List<Relation> relations = query.getResultList();

            if (relations.isEmpty()){
                session.beginTransaction();
                Relation relation1 = Relation.builder().relation(relation).build();
                session.save(relation1);
                session.getTransaction().commit();

                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Relation: " + relation + " has been successfully added.");
                relationCB.getItems().add(relation1);
                stage.close();
            } else {
                AlertUtil.showAlert(Alert.AlertType.ERROR, "Relation: " + relation + " already exists in the database." );
            }
        } else {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "You cannot save a blank relation");
        }
    }

    public void setRelationCB(ComboBox<Relation> relationCB) {
        this.relationCB = relationCB;
    }
}
