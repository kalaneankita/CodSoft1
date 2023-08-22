package application;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.io.*;
import javafx.stage.FileChooser;
import java.util.Optional;


public class StudentManagementGUI extends Application {
    private StudentManagementSystem sms = new StudentManagementSystem();
    private ListView<String> studentListView;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Student Management System");

        BorderPane root = new BorderPane();
        Scene scene = new Scene(root, 800, 600);

        studentListView = new ListView<>();
        Button addButton = new Button("Add Student");
        Button removeButton = new Button("Remove Student");
        Button editButton = new Button("Edit Student");
        Button searchButton = new Button("Search Student");
        Button loadButton = new Button("Load from File");
        Button saveButton = new Button("Save to File");
        Button exitButton = new Button("Exit");

        VBox buttonBox = new VBox(10, addButton, removeButton, editButton, searchButton, loadButton, saveButton, exitButton);
        buttonBox.setPadding(new Insets(10));
        HBox hbox = new HBox(10, studentListView, buttonBox);
        hbox.setPadding(new Insets(10));

        root.setCenter(hbox);

        primaryStage.setScene(scene);
        primaryStage.show();

        updateStudentListView();

        addButton.setOnAction(e -> showAddStudentDialog());
        removeButton.setOnAction(e -> removeSelectedStudent());
        editButton.setOnAction(e -> showEditStudentDialog());
        searchButton.setOnAction(e -> showSearchStudentDialog());
        loadButton.setOnAction(e -> loadFromFile());
        saveButton.setOnAction(e -> saveToFile());
        exitButton.setOnAction(e -> primaryStage.close());
    }

    private void updateStudentListView() {
        studentListView.getItems().clear();
        for (Student student : sms.getAllStudents()) {
            studentListView.getItems().add(student.toString());
        }
    }

    private void showAddStudentDialog() {
    	Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("Add Student");
        dialog.setHeaderText("Enter student details:");

        ButtonType addButton = new ButtonType("Add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField nameField = new TextField();
        TextField rollNumberField = new TextField();
        TextField gradeField = new TextField();

        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Roll Number:"), 0, 1);
        grid.add(rollNumberField, 1, 1);
        grid.add(new Label("Grade:"), 0, 2);
        grid.add(gradeField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == addButton) {
                return new String[] {
                    nameField.getText(),
                    rollNumberField.getText(),
                    gradeField.getText()
                };
            }
            return null;
        });

        dialog.showAndWait().ifPresent(result -> {
            try {
                String name = result[0];
                int rollNumber = Integer.parseInt(result[1]);
                String grade = result[2];
                sms.addStudent(new Student(name, rollNumber, grade));
                updateStudentListView();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Input");
                alert.setContentText("Please enter a valid roll number.");
                alert.showAndWait();
            }
        });
    }

    private void removeSelectedStudent() {
        int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String studentInfo = studentListView.getItems().get(selectedIndex);
            int rollNumber = Integer.parseInt(studentInfo.split(",")[1].split(":")[1].trim());
            sms.removeStudent(rollNumber);
            updateStudentListView();
        }

    }
    private void showEditStudentDialog() {
    	int selectedIndex = studentListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            String studentInfo = studentListView.getItems().get(selectedIndex);
            String[] parts = studentInfo.split(",");
            int rollNumber = Integer.parseInt(parts[1].split(":")[1].trim());
            Student student = sms.getStudentByRollNumber(rollNumber);
            
            if (student != null) {
                Dialog<Student> dialog = new Dialog<>();
                dialog.setTitle("Edit Student");
                dialog.setHeaderText("Edit student details:");

                ButtonType saveButton = new ButtonType("Save", ButtonBar.ButtonData.OK_DONE);
                dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);

                TextField nameField = new TextField(student.getName());
                TextField rollNumberField = new TextField(String.valueOf(student.getRollNumber()));
                TextField gradeField = new TextField(student.getGrade());

                grid.add(new Label("Name:"), 0, 0);
                grid.add(nameField, 1, 0);
                grid.add(new Label("Roll Number:"), 0, 1);
                grid.add(rollNumberField, 1, 1);
                grid.add(new Label("Grade:"), 0, 2);
                grid.add(gradeField, 1, 2);

                dialog.getDialogPane().setContent(grid);

                dialog.setResultConverter(buttonType -> {
                    if (buttonType == saveButton) {
                        String name = nameField.getText();
                        int newRollNumber = Integer.parseInt(rollNumberField.getText());
                        String grade = gradeField.getText();
                        student.setName(name);
                        student.setRollNumber(newRollNumber);
                        student.setGrade(grade);
                        return student;
                    }
                    return null;
                });

                Optional<Student> updatedStudent = dialog.showAndWait();
                updatedStudent.ifPresent(s -> {
                    sms.updateStudent(s);
                    updateStudentListView();
                });
            }
        }
    }
    private void showSearchStudentDialog() {
    	TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Student");
        dialog.setHeaderText("Enter Roll Number:");
        dialog.setContentText("Roll Number:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(rollNumberStr -> {
            try {
                int rollNumber = Integer.parseInt(rollNumberStr.trim());
                Student student = sms.getStudentByRollNumber(rollNumber);
                if (student != null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Student Details");
                    alert.setHeaderText("Student Found");
                    alert.setContentText(student.toString());
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Student Not Found");
                    alert.setHeaderText("Student with Roll Number " + rollNumber + " not found.");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Invalid Roll Number");
                alert.setContentText("Please enter a valid roll number.");
                alert.showAndWait();
            }
        });
    }

    private void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Students from File");
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            sms.loadFromFile(file.getAbsolutePath());
            updateStudentListView();
        }
    }

    private void saveToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Students to File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            sms.saveToFile(file.getAbsolutePath());
        }
    }

}
