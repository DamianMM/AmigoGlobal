package iv1300.views;

import iv1300.controllers.Controller;
import iv1300.model.*;
import iv1300.util.SqlConnection;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;

/**
 * Created by arvid on 2016-09-20.
 */
public class View extends Application {

    private static Controller controller;

    private ObservableList<Company> companies;

    private TableView<Company> companyTable;
    private TableView<Employee> employeeTable;

    private Stage window;
    private Scene scene1, scene2, scene3, scene4;

    private ComboBox companyCombo, employeeCombo;
    private BorderPane borderpane1;
    private VBox scene1leftMenu;
    private VBox scene1RightTableBox;
    private HBox addButtons;

    private GridPane companyGridPane;
    private TextField companyTextField;

    private GridPane employeeGridPane;
    private TextField firstNameTextField;
    private TextField lastNameTextField;
    private TextField phoneNumberTextField;
    private TextField eMailTextField;
    private Label firstName;
    private Label lastName;
    private Label phoneNumber;
    private Label eMail;
    private Button addEmployeeBtn;

    private GridPane tripGridPane;
    private TextField distanceTextField;
    private TextField timesTextField;
    private Label distance;
    private Label times;
    private Button addTripBtn;

    private Alert alert;

    public static void startView() {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        window = primaryStage;
        window.setTitle("Ashmouth Technologies");
        primaryStage.setTitle("Ashmouth Technologies");

        window.setOnCloseRequest(e -> closeProgram());

        companies = FXCollections.observableArrayList(controller.getAllCompanies());

        //-------------------------------Error Stage-------------------------------

        alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Inmatningsfel");
        alert.setHeaderText("Fel vid inmatning:");

        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));

        //-------------------------------Error Stage-------------------------------

        //---------------------SCENE1--------------------------

        Label labelScene1 = new Label();
        labelScene1.setText("Ashmouth Technologies alpha v0.1");
        labelScene1.setFont(Font.font("Verdana", 20));

        scene1leftMenu = new VBox(20);
        scene1leftMenu.setPadding(new Insets(20, 20, 20, 20));

        scene1RightTableBox = new VBox();

        Button addCompanyBtn = new Button("Lägg till företag");
        addCompanyBtn.setOnAction(e -> switchToScene2());
        Button addEmployeeBtn = new Button("Lägg till anställd");
        addEmployeeBtn.setOnAction(event -> switchToScene3());
        Button addTripBtn = new Button("Lägg till resa");
        addTripBtn.setOnAction(event -> switchToScene4());

        addButtons = new HBox(20);
        addButtons.getChildren().addAll(addCompanyBtn, addEmployeeBtn, addTripBtn);
        addButtons.setAlignment(Pos.CENTER);

        addButtons.setPadding(new Insets(0,0,25,0));

        Button s1q1 = new Button("Avsluta");
        s1q1.setOnAction(e -> closeProgram());

        createCompanyDropDown();
        //createEmployeeDropDown();

        scene1leftMenu.getChildren().addAll(labelScene1, s1q1, companyCombo);

        BorderPane leftSide = new BorderPane();
        leftSide.setTop(scene1leftMenu);
        leftSide.setBottom(addButtons);

        createCompanyTable();
        fillCompanyTable();
        scene1RightTableBox.getChildren().add(companyTable);

        createEmployeeTable();

        borderpane1 = new BorderPane();
        borderpane1.setLeft(leftSide);
        borderpane1.setCenter(scene1RightTableBox);

        scene1 = new Scene(borderpane1, 964, 400);

        scene1.getStylesheets().add(this.getClass().getResource("css/style.css").toExternalForm());

        //---------------------SCENE1--------------------------

        //---------------------SCENE2--------------------------

        createScene2();

        //---------------------SCENE2--------------------------

        //---------------------SCENE3--------------------------

        createScene3();

        //---------------------SCENE3--------------------------

        //---------------------SCENE4--------------------------

        createScene4();

        //---------------------SCENE4--------------------------

        window.getIcons().add(new Image(getClass().getResourceAsStream("images/icon.png")));

        window.setMinHeight(290);
        window.setMinWidth(964);
        window.setScene(scene1);
        window.show();
    }
    public void setController(Controller controller) {
        this.controller = controller;
    }

    private void switchToScene1() {
        window.setScene(scene1);
    }

    private void createScene2() {
        BorderPane container = new BorderPane();

        companyGridPane = new GridPane();

        Label companyNameLabel = new Label("Företagets namn:");

        companyTextField = new TextField();

        Button btn = new Button("Lägg till");
        btn.setOnAction(e -> addCompany(companyTextField.getCharacters().toString()));

        companyGridPane.add(companyNameLabel, 0, 0);
        companyGridPane.add(companyTextField, 0, 1);
        companyGridPane.add(btn, 1, 1);

        companyGridPane.setHgap(10);
        companyGridPane.setVgap(5);

        companyGridPane.setAlignment(Pos.CENTER);

        Button backButton = new Button("Tillbaka");
        backButton.setAlignment(Pos.TOP_CENTER);
        backButton.setOnAction(e -> switchToScene1());

        container.setCenter(companyGridPane);
        container.setLeft(backButton);
        container.setPadding(new Insets(10,10,10,10));

        scene2 = new Scene(container);
    }

    private void switchToScene2() {
        window.setScene(scene2);
    }

    private void addCompany(String name) {
        if(!validDBString(name)) {
            alert.setContentText("Det angivna företagsnamnet är ej giltigt. \n" +
                    "Företagets namn måste bestå av mist ett tecken. \n" +
                    "Namnet får inte börja med ett mellanslag. \n" +
                    "Namnet får ej överskrida 45 tecken.");
            alert.showAndWait();
        } else {
            boolean exists = false;
            for (Company c : companies) {
                if(c.getName().toLowerCase().equals(name.toLowerCase())) {
                    exists = true;
                    alert.setContentText("Det angivna företagsnamnet existerar redan.");
                    alert.showAndWait();
                }
            }
            if(!exists) {
                controller.insertCompany(name);
                companies.add(controller.getCompany(name));
                companyTextField.clear();
                switchToScene1();
            }
        }
    }

    ComboBox<Company> drop;

    private void createScene3() {
        BorderPane container = new BorderPane();

        employeeGridPane = new GridPane();

        firstName = new Label("Förnamn:");
        lastName = new Label("Efternamn:");
        phoneNumber = new Label("Telefonnummer:");
        eMail = new Label("E-mail:");

        firstNameTextField = new TextField();
        lastNameTextField = new TextField();
        phoneNumberTextField = new TextField();
        eMailTextField = new TextField();

        drop = new ComboBox();
        drop.setId("drop");
        drop.setPromptText("Anställds företag");

        drop.setItems(companies);
        drop.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> displayEmployeeInput());

        addEmployeeBtn = new Button("Lägg till");
        addEmployeeBtn.setOnAction(e -> addEmployee(drop.getSelectionModel().selectedIndexProperty().intValue(),
                                                    firstNameTextField.getCharacters().toString(),
                                                    lastNameTextField.getCharacters().toString(),
                                                    phoneNumberTextField.getCharacters().toString(),
                                                    eMailTextField.getCharacters().toString()));

        employeeGridPane.add(drop, 0, 1);

        employeeGridPane.setHgap(10);
        employeeGridPane.setVgap(5);

        employeeGridPane.setAlignment(Pos.CENTER);

        Button backButton = new Button("Tillbaka");
        backButton.setAlignment(Pos.TOP_CENTER);
        backButton.setOnAction(e -> switchToScene1());

        container.setCenter(employeeGridPane);
        container.setLeft(backButton);
        container.setPadding(new Insets(10,10,10,10));

        scene3 = new Scene(container);
    }

    private void switchToScene3() {
        window.setScene(scene3);
    }

    private void addEmployee(int company, String firstName, String lastName,
                             String phoneNumber, String eMail) {
        if(!validDBString(firstName) || !validDBString(lastName) ||
                !validDBString(phoneNumber) || !validDBString(eMail)) {
            alert.setContentText("De angivna fälten är ej giltiga. \n" +
                    "Samtliga fält måste inehålla minst ett tecken. \n" +
                    "De får inte börja med ett mellanslag. \n" +
                    "De får ej överskrida 45 tecken.");
            alert.showAndWait();
        } else {
            boolean exists = false;
            List<Employee> employees = controller.getAllEmployees();
            for (Employee e : employees) {
                if(e.getPhoneNumber().toLowerCase().equals(phoneNumber.toLowerCase())) {
                    exists = true;
                    alert.setContentText("En anställd har redan detta telefonnummer.");
                    alert.showAndWait();
                }
                else if(e.getEMail().toLowerCase().equals(eMail.toLowerCase())) {
                    exists = true;
                    alert.setContentText("En anställd har redan denna e-mail.");
                    alert.showAndWait();
                }
            }
            if(!exists) {
                int id = controller.insertEmployee(companies.get(company).getId(),firstName,lastName,
                        phoneNumber,eMail);
                //Update view
                Employee e = new Employee(id, firstName, lastName, phoneNumber, eMail);
                companies.get(company).getEmployees().add(e);
                cleanEmployeInput();
                switchToScene1();
            }
        }
    }

    private void cleanEmployeInput() {
        drop.setValue(null);
        employeeGridPane.getChildren().remove(1,10);
        firstNameTextField.clear();
        lastNameTextField.clear();
        phoneNumberTextField.clear();
        eMailTextField.clear();
    }

    private void displayEmployeeInput() {
        if(!employeeGridPane.getChildren().contains(firstName)) {
            employeeGridPane.add(firstName, 1, 0);
            employeeGridPane.add(firstNameTextField, 1, 1);
            employeeGridPane.add(lastName, 1, 2);
            employeeGridPane.add(lastNameTextField, 1, 3);
            employeeGridPane.add(phoneNumber, 1, 4);
            employeeGridPane.add(phoneNumberTextField, 1, 5);
            employeeGridPane.add(eMail, 1, 6);
            employeeGridPane.add(eMailTextField, 1, 7);
            employeeGridPane.add(addEmployeeBtn, 2, 7);
        }
    }

    ComboBox<Company> s4d1;
    ComboBox<Employee> s4d2;
    ComboBox<Vehicle> s4d3;

    private void createScene4() {
        BorderPane container = new BorderPane();

        tripGridPane = new GridPane();

        distance = new Label("Sträcka (km):");
        times = new Label("Antal:");

        distanceTextField = new TextField();
        timesTextField = new TextField();

        s4d1 = new ComboBox();
        s4d1.setId("drop1");
        s4d1.setPromptText("Anställds företag");

        s4d2 = new ComboBox();
        s4d2.setId("drop2");
        s4d2.setPromptText("Anställd");

        s4d3 = new ComboBox();
        s4d3.setId("drop3");
        s4d3.setPromptText("Fordon");

        ObservableList<Vehicle> vehicles = controller.getAllVehicles();

        s4d3.setItems(vehicles);

        s4d1.setItems(companies);
        s4d1.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> displayEmployeeDrop(newValue.intValue()));

        s4d2.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> displayVehicleDrop());

        s4d3.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> displayTripFields());

        addTripBtn = new Button("Lägg till");
        addTripBtn.setOnAction(e -> addTrip());

        tripGridPane.add(s4d1, 0, 1);

        tripGridPane.setHgap(10);
        tripGridPane.setVgap(5);

        tripGridPane.setAlignment(Pos.CENTER);

        Button backButton = new Button("Tillbaka");
        backButton.setAlignment(Pos.TOP_CENTER);
        backButton.setOnAction(e -> switchToScene1());

        container.setCenter(tripGridPane);
        container.setLeft(backButton);
        container.setPadding(new Insets(10,10,10,10));

        scene4 = new Scene(container);
    }

    private void switchToScene4() {
        window.setScene(scene4);
    }

    private void displayEmployeeDrop(int newValue) {
        s4d2.setItems(companies.get(newValue).getEmployees());
        if(!tripGridPane.getChildren().contains(s4d2))
            tripGridPane.add(s4d2, 0, 2);
    }

    private void displayVehicleDrop() {
        if(!tripGridPane.getChildren().contains(s4d3)) {
            tripGridPane.add(s4d3, 0, 3);
        }
    }

    private void displayTripFields() {
        if(!tripGridPane.getChildren().contains(distance)) {
            tripGridPane.add(distance, 0, 4);
            tripGridPane.add(distanceTextField, 0, 5);
            tripGridPane.add(times, 0, 6);
            tripGridPane.add(timesTextField, 0, 7);
            tripGridPane.add(addTripBtn, 1, 7);
        }
    }

    private void addTrip() {
        if(!validFloat(distanceTextField.getText())) {
            alert.setContentText("Sträckan måste vara ett positivt decimaltal eller heltal.\n" +
                    "Deimal ges av en punkt och inte ett kommatecken!");
            alert.showAndWait();
        }
        else if(!validInt(timesTextField.getText())) {
            alert.setContentText("Antalet måste vara ett heltal större än 0.");
            alert.showAndWait();
        }
        else if(s4d2.getSelectionModel().getSelectedIndex() == -1) {
            alert.setContentText("En anställd måste vara vald.");
            alert.showAndWait();
        }
        float dist = Float.parseFloat(distanceTextField.getText());
        int times = Integer.parseInt(timesTextField.getText());
        int resaId;
        int vehicleId = s4d3.getSelectionModel().getSelectedItem().getId();
        int employeeId = s4d2.getSelectionModel().getSelectedItem().getId();
        boolean exists = false;

        Distance distance = controller.getDistance(dist);
        if(distance == null) {
            int distId = controller.addDistance(dist);
            resaId = controller.insertResa(distId, vehicleId);
        } else {
            resaId = controller.getResa(distance.getId(), vehicleId);
            if(resaId == -1) {
                resaId = controller.insertResa(distance.getId(), vehicleId);
            } else if(controller.tripExists(employeeId, resaId)) {
                exists = true;
                alert.setContentText("Den valda anställda har redan denna resa.");
                alert.showAndWait();
            }
        }

        if(!exists) {
            int tripId = controller.insertTrip(employeeId, resaId, times);
            //update view
            Trip trip = new Trip(tripId, s4d3.getSelectionModel().getSelectedItem(),
                    dist, times);
            controller.addTripForEmploye(companies.get(s4d1.getSelectionModel().getSelectedIndex()).
                    getEmployees().get(s4d2.getSelectionModel().getSelectedIndex()), trip);
            employeeTable.setItems(companies.get(s4d1.getSelectionModel().getSelectedIndex()).
                    getEmployees());
            //CLEAN scene4!
            switchToScene1();
        }

    }

    private void createCompanyDropDown() {
        companyCombo = new ComboBox();
        companyCombo.setId("companyCombo");
        companyCombo.setPromptText("Välj ett företag");

        companyCombo.setItems(companies);
        companyCombo.getSelectionModel().selectedIndexProperty().addListener(
                (observable, oldValue, newValue) -> displaySelectedCompany(newValue.intValue()));
    }

    private void displaySelectedCompany(int newValue) {
        if(!scene1RightTableBox.getChildren().contains(employeeTable)) {
            scene1RightTableBox.getChildren().add(employeeTable);
        }

        employeeTable.setItems(companies.get(companyCombo.getSelectionModel().getSelectedIndex()).getEmployees());
    }

    private void createCompanyTable() {
        TableColumn<Company, String> nameCol = new TableColumn<>("Namn");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setMinWidth(80);

        TableColumn<Company, Float> totalEmissionCol = new TableColumn<>("Totalt utsläpp");
        totalEmissionCol.setCellValueFactory(new PropertyValueFactory<>("totalEmission"));
        totalEmissionCol.setMinWidth(120);

        TableColumn<Company, Float> averageEmissionCol = new TableColumn<>("Medelutsläpp per person");
        averageEmissionCol.setCellValueFactory(new PropertyValueFactory<>("averageEmission"));
        averageEmissionCol.setMinWidth(150);

        companyTable = new TableView<>();
        companyTable.getColumns().addAll(nameCol, totalEmissionCol, averageEmissionCol);

        companyTable.prefHeightProperty().bind(scene1RightTableBox.heightProperty());
    }

    private void fillCompanyTable() {
        companyTable.setItems(companies);
    }

    private void createEmployeeTable() {
        TableColumn<Employee, String> firstNameCol = new TableColumn<>("Förnamn");
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        firstNameCol.setMinWidth(100);

        TableColumn<Employee, String> lastNameCol = new TableColumn<>("Efternamn");
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        lastNameCol.setMinWidth(100);

        TableColumn<Employee, String> phoneNumberCol = new TableColumn<>("Telefonnummer");
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneNumberCol.setMinWidth(120);

        TableColumn<Employee, String> eMailCol = new TableColumn<>("E-mail");
        eMailCol.setCellValueFactory(new PropertyValueFactory<>("eMail"));
        eMailCol.setMinWidth(150);

        TableColumn<Employee, String> totalEmissionCol = new TableColumn<>("Totalt utsläpp");
        totalEmissionCol.setCellValueFactory(new PropertyValueFactory<>("totalEmission"));
        totalEmissionCol.setMinWidth(100);

        employeeTable = new TableView<>();
        employeeTable.getColumns().addAll(firstNameCol, lastNameCol, phoneNumberCol, eMailCol, totalEmissionCol);
        employeeTable.prefHeightProperty().bind(scene1RightTableBox.heightProperty());
        employeeTable.setPlaceholder(new Text("Företaget har inga anställda."));
    }

    private boolean validDBString(String s) {
        if(s.length() == 0 || s.charAt(0) == 32 || s.length() > 45) {
            return false;
        }
        return true;
    }

    private boolean validFloat(String s) {
        try {
            float f = Float.parseFloat(s);
            if(f < 0) return false;
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    private boolean validInt(String s) {
        try {
            int i = Integer.parseInt(s);
            if(i < 1) return false;
        } catch(NumberFormatException e) {
            return false;
        }
        return true;
    }

    private void closeProgram(){
        try {
            System.out.println("Committing changes to database...");
            SqlConnection.getInstance().getCon().commit();
            System.out.println("Closing SQL connection...");
            SqlConnection.getInstance().getCon().close();
            System.out.println("Closing window...");
            window.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Could not close application properly");
        }
    }
}