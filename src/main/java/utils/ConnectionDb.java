package utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class ConnectionDb {
    private static String url;
    private static String userName;
    private static String password;

    static {
        try (FileReader propertyFile = new FileReader("src/main/resources/config.properties")) {
            Properties properties = new Properties();
            properties.load(propertyFile);
            url = (String) properties.get("url");
            userName = (String) properties.get("userName");
            password = (String) properties.get("password");
        } catch (Exception e) {
            log.error("something went wrong during file loading",e);
        }
    }

    public static Connection connectionDataBase() {
        Connection conn = null;
        try {
           conn = DriverManager.getConnection(url, userName, password);
            System.out.println(" connection with the dataBase is done...");
            log.info("connection with the dataBase is done...");
        } catch (SQLException e) {
            log.error("something went wrong during the dataBase connection..", e);
        }
        return conn;
    }
}
