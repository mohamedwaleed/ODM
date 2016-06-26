//package com.odm.persistence;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.SQLException;
//import java.sql.Statement;
//
///**
// * Created by mohamed on 6/24/16.
// */
//@Component
//public class DatabaseCreator {
//    @Autowired
//    private DataSource dataSource;
//
//    @PostConstruct
//    public void initialize(){
//        try {
//            Connection connection = dataSource.getConnection();
//            Statement statement = connection.createStatement();
//            statement.execute("DROP TABLE IF EXISTS PERSON_INFO");
//            statement.executeUpdate(
//                    "CREATE TABLE PERSON_INFO(" +
//                            "ID INTEGER Primary key, " +
//                            "FIRST_NAME varchar(30) not null, " +
//                            "LAST_NAME varchar(30) not null)"
//            );
//            statement.executeUpdate(
//                    "INSERT INTO PERSON_INFO " +
//                            "(FIRST_NAME, LAST_NAME) " +
//                            "VALUES " + "('DONALD', 'TRUMP')"
//            );
//            statement.close();
//            connection.close();
//        }
//        catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//}
