package database;

import model.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.List;
import java.util.Properties;

public class DatabaseController {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseController.class);
    private static final String PROPERTIES_FILE_NAME = "database.properties";

    private Connection connection;
    private String username;
    private String password;
    private String databaseName;
    private String url;


    public DatabaseController() throws SQLException, IOException {
        Properties props = new Properties();
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("properties/" + PROPERTIES_FILE_NAME)) {
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
            throw new FileNotFoundException("Database properties file not found");
        }
        this.url = props.getProperty("url");
        this.databaseName = props.getProperty("database_name");
        this.username = props.getProperty("username");
        this.password = props.getProperty("password");
        this.connection = getConnection();
        LOGGER.info("Database connection successful " + url);
    }

    public void addWords(String tableName, List<Word> words) throws SQLException {
        connection = getConnection();
        createTable(tableName);
        int counter = 0;
        for (Word word : words) {
            PreparedStatement preparedStatement = connection.prepareStatement(" INSERT INTO " + tableName + " (id,word,count)"
                    + " VALUES(?,?,?)");
            preparedStatement.setInt(1, counter);
            preparedStatement.setString(2, word.getWord());
            preparedStatement.setLong(3, word.getCount());
            preparedStatement.execute();
            counter++;
        }
        LOGGER.info("Words were added to table " + tableName);
        connection.close();
    }

    public void createTable(String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        String query =
                "create table if not exists " + tableName +
                        " (id  integer not null primary key ," +
                        "word varchar(50), " +
                        "count integer)";
        statement.execute(query);
        LOGGER.info("Table '" + tableName + "' has been created");
    }

    public boolean isTableExist(String tableName) throws SQLException {
        connection = getConnection();
        String query = "SELECT COUNT(*) FROM pg_tables WHERE tablename = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, tableName);
        ResultSet resultSet = statement.executeQuery();
        resultSet.next();
        connection.close();
        return resultSet.getInt(1) > 0;
    }

    private Connection getConnection() throws SQLException {
        try {
            return DriverManager.getConnection(url + databaseName, username, password);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to the database " + url);
        }
    }

}
