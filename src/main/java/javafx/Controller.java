package javafx;

import database.DatabaseController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import model.Word;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import parser.Parser;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static constants.Constants.*;

public class Controller implements Initializable {

    private static final Logger logger = LogManager.getLogger(Controller.class);

    private List<Word> words;
    private DatabaseController db;

    @FXML
    private Button buttonParse;

    @FXML
    private Button saveButton;

    @FXML
    private TextField tableName;

    @FXML
    private TextField url;

    @FXML
    private TableView<Word> table;

    @FXML
    private TableColumn<Word, String> word;

    @FXML
    private TableColumn<Word, Integer> count;

    @FXML
    private Text textField;


    public Controller() {
        words = new ArrayList<>();
        try {
            db = new DatabaseController();
        } catch (SQLException | IOException e) {
            showAlert(NOTIFICATION_ERROR_TITLE, e.getMessage(), Alert.AlertType.ERROR);
            logger.error("Exception: " + e);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        count.setCellValueFactory(new PropertyValueFactory<>("count"));
        word.setCellValueFactory(new PropertyValueFactory<>("word"));
        ObservableList<Word> list = FXCollections.observableArrayList();
        table.setItems(list);
    }


    @FXML
    private void parseWord(ActionEvent event) {
        String pageUrl = url.getText();
        if (validateTextField(pageUrl)) {
            try {
                Parser parser = new Parser(pageUrl);
                words = parser.getWords();
                ObservableList<Word> statistics = FXCollections.observableArrayList(words);
                table.setItems(statistics);
                textField.setText(MSG_TOTAL_WORDS + parser.getCountOfWords() + MSG_UNIQUE_WORDS + parser.getUniqueWordsCount());
                showAlert(NOTIFICATION_INFO_TITLE, MSG_GET_WORDS + pageUrl, Alert.AlertType.INFORMATION);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("Exception: " + e);
                showAlert(NOTIFICATION_ERROR_TITLE, e.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            showAlert(NOTIFICATION_ERROR_TITLE, MSG_INCORRECT_URL, Alert.AlertType.ERROR);
        }
    }

    @FXML
    private void saveToDatabase(ActionEvent event) {
        if (words.isEmpty()) {
            showAlert(NOTIFICATION_ERROR_TITLE, MSG_NO_WORDS_PARSED, Alert.AlertType.ERROR);
            return;
        }
        String table = tableName.getText();
        if (validateTextField(table)) {
            addWordToDatabase(table);
        } else {
            showAlert(NOTIFICATION_ERROR_TITLE, MSG_INCORRECT_TABLE, Alert.AlertType.ERROR);
        }
    }

    private void addWordToDatabase(String table) {
        try {
            if (db.isTableExist(table)) {
                showAlert(NOTIFICATION_ERROR_TITLE, MSG_TABLE_EXIST, Alert.AlertType.ERROR);
            } else {
                Parser parser = new Parser(url.getText());
                db.addWords(table, parser.getWords());
                showAlert(NOTIFICATION_INFO_TITLE, MSG_WORDS_ADDED_TO_DATABASE + table, Alert.AlertType.INFORMATION);
            }
        } catch (SQLException | IOException e) {
            logger.error("Exception: " + e.getMessage());
            showAlert(NOTIFICATION_ERROR_TITLE, e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        } catch (NullPointerException e) {
            showAlert(NOTIFICATION_ERROR_TITLE, MSG_WRONG_PROPERTIES, Alert.AlertType.ERROR);
            logger.error("Exception " + MSG_WRONG_PROPERTIES);
            e.printStackTrace();
        }
    }


    private void showAlert(String title, String text, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    private boolean validateTextField(String text) {
        return !text.trim().isEmpty();
    }

}
