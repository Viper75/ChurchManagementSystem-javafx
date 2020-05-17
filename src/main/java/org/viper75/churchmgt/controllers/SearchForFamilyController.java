package org.viper75.churchmgt.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.dto.FamilyDto;
import org.viper75.churchmgt.model.Family;
import org.viper75.churchmgt.model.Member;
import org.viper75.churchmgt.utils.AlertUtil;

import javax.persistence.criteria.CriteriaQuery;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SearchForFamilyController implements Initializable {

    @FXML private Button cancelBtn;

    @FXML private TableView<FamilyDto> familyDtoTV;
    @FXML
    TableColumn<FamilyDto, Long> familyIdCol;
    @FXML
    TableColumn<FamilyDto, String> familyMainMemberCol;

    private ComboBox isMainMemberCB;
    private TextField familyIdTF;
    private TextField familyMainMemberNameTF;

    private final SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        familyIdCol.setCellValueFactory(new PropertyValueFactory<>("familyId"));
        familyMainMemberCol.setCellValueFactory(new PropertyValueFactory<>("familyMainMemberName"));
        //Initialize family table
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        CriteriaQuery<Family> familyCriteriaQuery = session.getCriteriaBuilder().createQuery(Family.class);
        familyCriteriaQuery.from(Family.class);

        List<Family> families = session.createQuery(familyCriteriaQuery).getResultList();
        session.close();

        List<FamilyDto> familyList = new ArrayList<>();
        families.forEach(f -> {
            Member mainMember = getFamilyMainMember(f);
            if (mainMember != null){
                FamilyDto familyDto = new FamilyDto();
                familyDto.setFamilyId(f.getFamilyId());
                familyDto.setFamilyMainMemberName(mainMember.getMemberFname() + " " + mainMember.getMemberLname());
                familyList.add(familyDto);
            }
        });

        familyDtoTV.getItems().addAll(familyList);

        familyDtoTV.setOnMouseClicked(this::handleTableMouse_Click);

        //Handle Cancel Event
        cancelBtn.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    public Member getFamilyMainMember(Family family){
        Member maimMember = null;
        for (Member member : family.getFamilyMembers()){
            if (member.getMainMember().equals("Yes")){
                maimMember = member;
                break;
            }
        }
        return maimMember;
    }

    private void handleTableMouse_Click(MouseEvent mouseEvent) {
        if (familyDtoTV.getSelectionModel().getSelectedItem() != null && mouseEvent.getClickCount() == 2){
            FamilyDto familyDto = familyDtoTV.getSelectionModel().getSelectedItem();
            familyIdTF.setText(String.valueOf(familyDto.getFamilyId()));
            familyMainMemberNameTF.setText(familyDto.getFamilyMainMemberName());
            familyIdTF.setDisable(false);
            familyMainMemberNameTF.setDisable(false);
            Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
            stage.close();
        }
    }


    public void setPassThisStagesVars(ComboBox<String> isMainMemberCB, TextField familyIdTF, TextField familyMainMemberNameTF) {
        this.isMainMemberCB = isMainMemberCB;
        this.familyIdTF = familyIdTF;
        this.familyMainMemberNameTF = familyMainMemberNameTF;
    }
}
