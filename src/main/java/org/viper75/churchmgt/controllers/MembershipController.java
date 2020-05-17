package org.viper75.churchmgt.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.model.Member;
import org.viper75.churchmgt.utils.ViewRender;

import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class MembershipController implements Initializable {

    @FXML
    private Button addMemberBtn;
    @FXML private Button generateTemplateBtn;

    @FXML private ScrollPane membersSP;

    @FXML private TableView<Member> membersTV;
    @FXML private TableColumn checkBoxCol;
    @FXML private TableColumn<Member, Long> memberIDCol;
    @FXML private TableColumn<Member, String> memberFirstnameCol;
    @FXML private TableColumn<Member, String> memberLastnameCol;
    @FXML private TableColumn<Member, String> memberMiddleCol;
    @FXML private TableColumn<Member, String> memberSalutationCol;
    @FXML private TableColumn<Member, String> memberSuffixCol;
    @FXML private TableColumn<Member, String> memberRelationCol;
    @FXML private TableColumn<Member, String> memberGenderCol;
    @FXML private TableColumn<Member, String> memberHomePhoneCol;
    @FXML private TableColumn<Member, String> memberCellPhoneCol;
    @FXML private TableColumn<Member, String> memberAddressCol;
    @FXML private TableColumn<Member, LocalDate> memberDOBCol;
    @FXML private TableColumn<Member, String> memberMaritalStatusCol;
    @FXML private TableColumn<Member, String> memberEMailCol;
    @FXML private TableColumn<Member, String> memberTypeCol;
    @FXML private TableColumn<Member, String> memberStatusCol;
    @FXML private TableColumn<Member, String> mainMemberCol;

    private static final SessionFactory SESSION_FACTORY = HibernateConfiguration.getSessionFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Setting table columns cell value factory
        memberIDCol.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        memberFirstnameCol.setCellValueFactory(new PropertyValueFactory<>("memberFname"));
        memberLastnameCol.setCellValueFactory(new PropertyValueFactory<>("memberLname"));
        memberMiddleCol.setCellValueFactory(new PropertyValueFactory<>("memberMiddle"));
        memberSalutationCol.setCellValueFactory(new PropertyValueFactory<>("memberSalutaion"));
        memberSuffixCol.setCellValueFactory(new PropertyValueFactory<>("memberSuffix"));
        memberRelationCol.setCellValueFactory(new PropertyValueFactory<>("memberRelation"));
        memberGenderCol.setCellValueFactory(new PropertyValueFactory<>("memberGender"));
        memberCellPhoneCol.setCellValueFactory(new PropertyValueFactory<>("memberCellPhone"));
        memberHomePhoneCol.setCellValueFactory(new PropertyValueFactory<>("memberHomePhone"));
        memberAddressCol.setCellValueFactory(new PropertyValueFactory<>("memberAddress"));
        memberDOBCol.setCellValueFactory(new PropertyValueFactory<>("memberDOB"));
        memberMaritalStatusCol.setCellValueFactory(new PropertyValueFactory<>("memberMaritalStatus"));
        memberEMailCol.setCellValueFactory(new PropertyValueFactory<>("memberEMail"));
        memberTypeCol.setCellValueFactory(new PropertyValueFactory<>("memberType"));
        memberStatusCol.setCellValueFactory(new PropertyValueFactory<>("memberStatus"));
        mainMemberCol.setCellValueFactory(new PropertyValueFactory<>("mainMember"));

        //Initializing Members Table View
        membersTV.getItems().addAll(getAllMembers());

        membersSP.setFitToHeight(true);

        //Add Button Action
        addMemberBtn.setOnAction(this::handleAddMemberBtn_Click);
    }

    private void handleAddMemberBtn_Click(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddMember.fxml"));
        try {
            Parent root = loader.load();
            AddMemberController controller = loader.getController();
            controller.sendVariables(membersTV);
            ViewRender.getView(root);
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public List<Member> getAllMembers(){
        Session session = SESSION_FACTORY.openSession();
        session.beginTransaction();
        CriteriaQuery<Member> memberCriteriaQuery = session.getCriteriaBuilder().createQuery(Member.class);
        memberCriteriaQuery.from(Member.class);

        List<Member> members = session.createQuery(memberCriteriaQuery).getResultList();
        session.close();
        return members;
    }
}
