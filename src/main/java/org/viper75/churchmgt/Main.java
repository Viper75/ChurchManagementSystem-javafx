package org.viper75.churchmgt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.viper75.churchmgt.config.HibernateConfiguration;
import org.viper75.churchmgt.model.ChurchInfo;

import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.util.List;

public class Main extends Application {

    private SessionFactory sessionFactory = HibernateConfiguration.getSessionFactory();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("views/MainWindow.fxml"));

        Parent root = loader.load();

        Session session = sessionFactory.openSession();
        CriteriaQuery<ChurchInfo> churchInfoCriteriaQuery = session.getCriteriaBuilder().createQuery(ChurchInfo.class);
        churchInfoCriteriaQuery.from(ChurchInfo.class);
        List<ChurchInfo> churchInfo = session.createQuery(churchInfoCriteriaQuery).getResultList();
        session.close();

        Scene scene = new Scene(root);
        primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("views/images/application-icon.png")));
        primaryStage.setTitle("My Church Management System");
        if (!churchInfo.isEmpty()){
            if (churchInfo.get(0).getChurchName() != null)
                primaryStage.setTitle(churchInfo.get(0).getChurchName());
        }
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
