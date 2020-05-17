package org.viper75.churchmgt.config;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.viper75.churchmgt.model.*;

public class HibernateConfiguration {

    private static final String CONNECTION_URL = "jdbc:sqlserver://localhost:1433";
    private static final String USER_NAME = "sa";
    private static final String PASSWORD = "Sysadmin12";
    private static final String DB_NAME = "church_management_system";

//    public static void createDatabase() throws SQLException{
//        String url = CONNECTION_URL + ";databaseName=master;" + "user=" + USER_NAME + ";password=" + PASSWORD;
//
//        System.out.println("Connecting to SQL Server ...");
//        try (Connection connection = DriverManager.getConnection(url)) {
//            System.out.println("Connected!!");
//
//            System.out.println("Dropping and creating database '" + DB_NAME + "' ...");
//            String createDbSQL = "DROP DATABASE IF EXISTS [" + DB_NAME + "]; CREATE DATABASE [" + DB_NAME + "]";
//
//            try (Statement statement = connection.createStatement()){
//                statement.executeUpdate(createDbSQL);
//                System.out.println("Database created!!");
//            }
//        }
//    }

    public static SessionFactory getSessionFactory(){
        return createHibernateConfiguration().buildSessionFactory();
    }

    private static Configuration createHibernateConfiguration(){
        String url = CONNECTION_URL + ";databaseName=" + DB_NAME;
        Configuration cfg = new Configuration()
                .setProperty("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .setProperty("hibernate.connection.url", url)
                .setProperty("hibernate.connection.username", USER_NAME)
                .setProperty("hibernate.connection.password", PASSWORD)
                .setProperty("hibernate.connection.autocommit", "true")
                .setProperty("hibernate.show_sql", "true");

        cfg.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        cfg.setProperty("hibernate.hbm2ddl.auto", "update");

        cfg.addAnnotatedClass(Family.class);
        cfg.addAnnotatedClass(Member.class);
        cfg.addAnnotatedClass(MemberType.class);
        cfg.addAnnotatedClass(Relation.class);
        cfg.addAnnotatedClass(Currency.class);
        cfg.addAnnotatedClass(ChurchInfo.class);
        cfg.addAnnotatedClass(MobileMoney.class);

        return cfg;
    }
}
