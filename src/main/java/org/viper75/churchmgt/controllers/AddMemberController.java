package org.viper75.churchmgt.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.enumeration.Gender;
import org.viper75.churchmgt.enumeration.MaritalStatus;
import org.viper75.churchmgt.enumeration.Salutation;
import org.viper75.churchmgt.model.*;
import org.viper75.churchmgt.utils.AlertUtil;
import org.viper75.churchmgt.utils.TextFieldUtil;
import org.viper75.churchmgt.utils.ViewRender;

import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class AddMemberController implements Initializable {

    @FXML private Button addMemberTypeBtn;
    @FXML private Button addRelationBtn;
    @FXML private Button saveBtn;

    @FXML private ComboBox<Salutation> salutationCB;
    @FXML private ComboBox<Gender> genderCB;
    @FXML private ComboBox<MaritalStatus> maritalStatusCB;
    @FXML private ComboBox<MemberType> memberTypeCB;
    @FXML private ComboBox<Relation> relationCB;
    @FXML private ComboBox<String> isMainMemberCB;

    @FXML private CheckBox useFamilyAddressCheckBox;

    @FXML private DatePicker dobDP;

    @FXML private TextField firstnameTF;
    @FXML private TextField lastnameTF;
    @FXML private TextField middleTF;
    @FXML private TextField memberSuffixTF;
    @FXML private TextField homePhoneTF;
    @FXML private TextField cellPhoneTF;
    @FXML private TextField emailTF;
    @FXML private TextField addressLine1TF;
    @FXML private TextField addressLine2TF;
    @FXML private TextField cityTF;
    @FXML private TextField familyMainMemberNameTF;
    @FXML private TextField familyIdTF;

    @FXML private ToggleGroup memberStatusTG;

    private static final String DEFAULT_PHONE_NO = "###-###-####";

    private boolean generateBtnClicked = false;
    private TableView<Member> membersTv;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Initialize Save Button Action
        saveBtn.setOnAction(this::handleSaveBtn_Click);

        //Initializing Salutation ComboBox
        salutationCB.getItems().addAll(Salutation.values());

        //Initializing isMainMember ComboBox
        isMainMemberCB.getItems().addAll("Yes", "No");

        //Initializing Marital Status ComboBox
        maritalStatusCB.getItems().addAll(MaritalStatus.values());

        //Initializing Gender ComboBox
        genderCB.getItems().addAll(Gender.values());

        //Initializing Member Type ComboBox
        memberTypeCB.getItems().addAll(fetchAllMemberTypes());

        //Initializing Relation ComboBox
        relationCB.getItems().addAll(fetchAllRelations());

        homePhoneTF.setTextFormatter(new TextFormatter<>(this::phoneNumberMask));
        cellPhoneTF.setTextFormatter(new TextFormatter<>(this::phoneNumberMask));

        //Add member type button handler
        addMemberTypeBtn.setOnAction(this::handleAddMemberTypeBtn_Click);

        //add relation
        addRelationBtn.setOnAction(this::handleAddRelationBtn_Click);

        //Adding text limit to name fields
        TextFieldUtil.addTextLimiter(new TextField[]{firstnameTF, lastnameTF, middleTF}, 30);

        //adding text limit to member suffix
        TextFieldUtil.addTextLimiter(memberSuffixTF, 10);

        isMainMemberCB.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == "Yes"){
                useFamilyAddressCheckBox.setSelected(false);
                useFamilyAddressCheckBox.setDisable(true);
                addressLine1TF.setEditable(true);
                addressLine2TF.setEditable(true);
                cityTF.setEditable(true);
                familyMainMemberNameTF.setDisable(true);
                familyIdTF.setDisable(true);
                addressLine1TF.setDisable(false);
                addressLine2TF.setDisable(false);
                cityTF.setDisable(false);
            } else if (newValue == "No"){
                useFamilyAddressCheckBox.setDisable(false);
                useFamilyAddressCheckBox.setSelected(true);
                addressLine1TF.setEditable(false);
                addressLine2TF.setEditable(false);
                cityTF.setEditable(false);
                familyMainMemberNameTF.setDisable(false);
                familyIdTF.setDisable(false);

                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/SearchForFamily.fxml"));

                try {
                    Parent root = loader.load();
                    SearchForFamilyController controller = loader.getController();
                    controller.setPassThisStagesVars(isMainMemberCB, familyIdTF, familyMainMemberNameTF);
                    ViewRender.getView(root, StageStyle.UNDECORATED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                useFamilyAddressCheckBox.setDisable(true);
                addressLine1TF.setDisable(true);
                addressLine2TF.setDisable(true);
                cityTF.setDisable(true);
            }
        });

        useFamilyAddressCheckBox.setOnAction(e -> {
            if (useFamilyAddressCheckBox.isSelected()){
                addressLine1TF.setEditable(false);
                addressLine2TF.setEditable(false);
                cityTF.setEditable(false);
            } else {
                addressLine1TF.setEditable(true);
                addressLine2TF.setEditable(true);
                cityTF.setEditable(true);
            }
        });
    }

    private void handleSaveBtn_Click(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        Salutation salutation = salutationCB.getValue();
        String fname = firstnameTF.getText();
        String lname = lastnameTF.getText();
        String middle = middleTF.getText();
        String suffix = memberSuffixTF.getText();
        String homePhone = homePhoneTF.getText();
        String cellPhone = cellPhoneTF.getText();
        String email = emailTF.getText();
        String mainMember = isMainMemberCB.getValue();
        String addressLine1 = addressLine1TF.getText();
        String addressLine2 = addressLine2TF.getText();
        String city = cityTF.getText();
        Gender gender = genderCB.getValue();
        MaritalStatus maritalStatus = maritalStatusCB.getValue();
        MemberType memberType = memberTypeCB.getValue();
        Relation relation = relationCB.getValue();
        LocalDate dob = dobDP.getValue();

        if (!fname.equals("") && !lname.equals("") && gender != null && maritalStatus != null && memberType != null){
            Member member = new Member(fname, lname, mainMember, gender, maritalStatus, memberType);

            if (salutation != null) member.setMemberSalutaion(salutation);

            if (!middle.equals("")) member.setMemberMiddle(middle);

            if (!suffix.equals("")) member.setMemberSuffix(suffix);

            if (!homePhone.equals(DEFAULT_PHONE_NO)) member.setMemberHomePhone(homePhone.replaceAll("-", ""));

            if (!cellPhone.equals(DEFAULT_PHONE_NO)) member.setMemberCellPhone(cellPhone.replaceAll("-", ""));

            if (!email.equals("")) member.setMemberEMail(email);

            Address address = new Address();
            if (!addressLine1.equals("")) address.setStreet(addressLine1);

            if (!addressLine2.equals("")) address.setTown(addressLine2);

            if (!city.equals("")) address.setCity(city);

            if (address.getStreet() != null || address.getTown() != null || address.getCity() != null)
                member.setMemberAddress(address);

            if (relation != null) member.setMemberRelation(relation);

            if (dob != null) member.setMemberDOB(dob);

            Session session = HibernateConfiguration.getSessionFactory().openSession();

            if (isMainMemberCB.getValue().equals("No")){
                Family family = session.find(Family.class, Long.valueOf(familyIdTF.getText()));
                family.getFamilyMembers().add(member);
                member.setFamily(family);
                session.save(family);
            }

            if (isMainMemberCB.getValue().equals("Yes")){
                Family family = Family.builder().build();
                family.getFamilyMembers().add(member);
                member.setFamily(family);
            }
            session.save(member);
            session.close();

            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Member has been added successfully");

            membersTv.getItems().add(member);

            stage.close();
        } else {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "All fields with an astrisk are required.");
        }
    }

    private void handleAddRelationBtn_Click(ActionEvent event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddRelation.fxml"));

        try {
            Parent root = loader.load();
            AddRelationController controller = loader.getController();
            controller.setRelationCB(relationCB);
            ViewRender.getView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAddMemberTypeBtn_Click(ActionEvent event) {
        Stage thisStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/AddMemberType.fxml"));

        try {
            Parent root = loader.load();
            AddMemberTypeController controller = loader.getController();
            controller.setMemberTypeCB(memberTypeCB);
            ViewRender.getView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Relation> fetchAllRelations(){
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaQuery<Relation> relationCriteriaQuery = session.getCriteriaBuilder().createQuery(Relation.class);
        relationCriteriaQuery.from(Relation.class);

        List<Relation> relations = session.createQuery(relationCriteriaQuery).getResultList();
        session.close();
        return relations;
    }

    public List<MemberType> fetchAllMemberTypes() {
        Session session = HibernateConfiguration.getSessionFactory().openSession();
        session.beginTransaction();
        CriteriaQuery<MemberType> memberTypeCriteriaQuery = session.getCriteriaBuilder().createQuery(MemberType.class);
        memberTypeCriteriaQuery.from(MemberType.class);

        List<MemberType> memberTypes = session.createQuery(memberTypeCriteriaQuery).getResultList();
        session.close();
        return memberTypes;
    }

    private TextFormatter.Change phoneNumberMask( TextFormatter.Change change) {
        // Ignore cursor movements, unless the text is empty (in which case
        // we're initializing the field).
        if (!change.isContentChange() && !change.getControlNewText().isEmpty()) {
            return change;
        }

        String text = change.getControlNewText();
        int start = change.getRangeStart();
        int end = change.getRangeEnd();

        int anchor = change.getAnchor();
        int caret = change.getCaretPosition();

        StringBuilder newText = new StringBuilder(text);

        int dash;
        while ((dash = newText.lastIndexOf("-")) >= start) {
            newText.deleteCharAt(dash);
            if (caret > dash) {
                caret--;
            }
            if (anchor > dash) {
                anchor--;
            }
        }

        while (newText.length() < 3) {
            newText.append('#');
        }
        if (newText.length() == 3 || newText.charAt(3) != '-') {
            newText.insert(3, '-');
            if (caret > 3 || (caret == 3 && end <= 3 && change.isDeleted())) {
                caret++;
            }
            if (anchor > 3 || (anchor == 3 && end <= 3 && change.isDeleted())) {
                anchor++;
            }
        }

        while (newText.length() < 7) {
            newText.append('#');
        }
        if (newText.length() == 7 || newText.charAt(7) != '-') {
            newText.insert(7, '-');
            if (caret > 7 || (caret == 7 && end <= 7 && change.isDeleted())) {
                caret++;
            }
            if (anchor > 7 || (anchor == 7 && end <= 7 && change.isDeleted())) {
                anchor++;
            }
        }

        while (newText.length() < 12) {
            newText.append('#');
        }

        if (newText.length() > 12) {
            newText.delete(12, newText.length());
        }

        text = newText.toString();
        anchor = Math.min(anchor, 12);
        caret = Math.min(caret, 12);

        change.setText(text);
        change.setRange(0, change.getControlText().length());
        change.setAnchor(anchor);
        change.setCaretPosition(caret);

        return change;
    }

    public void sendVariables(TableView<Member> membersTV) {
        this.membersTv = membersTV;
    }
}
