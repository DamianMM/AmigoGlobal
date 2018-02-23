package iv1300.dao;

import iv1300.model.Company;
import iv1300.model.Employee;
import iv1300.util.SqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoffer on 2016-09-26.
 */
public class EmployeeDAO {

    private static EmployeeDAO instance = new EmployeeDAO();

    public static EmployeeDAO getInstance() { return instance; }

    private EmployeeDAO() {}

    private Connection connection = SqlConnection.getInstance().getCon();

    public ObservableList<Employee> getAll() {

        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM anställd");

            while(resultSet.next()) {
                Employee emp = new Employee(resultSet.getInt("id"),
                                            resultSet.getString("förnamn"),
                                            resultSet.getString("efternamn"),
                                            resultSet.getString("telefonnummer"),
                                            resultSet.getString("email"));
                employees.add(emp);
            }
        } catch (SQLException e) {
            System.out.println("Could't fetch all employees.\n");
            e.printStackTrace();
        }

        return employees;
    }

    public ObservableList<Employee> getEmployeesForCompany(Company company) {
        return getEmployeesForCompany(company.getId());
    }

    public ObservableList<Employee> getEmployeesForCompany(int id) {

        ObservableList<Employee> employees = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.id, a.förnamn, a.efternamn, a.telefonnummer, a.email " +
                                                                              "FROM Anställd AS a, Företag AS f " +
                                                                              "WHERE f.id = ? AND a.företag = f.id");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                employees.add(new Employee(resultSet.getInt("id"),
                                           resultSet.getString("förnamn"),
                                           resultSet.getString("efternamn"),
                                           resultSet.getString("telefonnummer"),
                                           resultSet.getString("email")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return employees;
    }

    /**
     *
     * @param company
     * @param firstName
     * @param lastName
     * @param phoneNumber
     * @param eMail
     * @return The ID of the employee inserted. returns -1 when error.
     */
    public int insertEmployee(int company, String firstName, String lastName,
                               String phoneNumber, String eMail) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO anställd (företag,förnamn,efternamn,telefonnummer,email) " +
                                                                              "VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,company);
            preparedStatement.setString(2,firstName);
            preparedStatement.setString(3,lastName);
            preparedStatement.setString(4,phoneNumber);
            preparedStatement.setString(5,eMail);

            preparedStatement.executeUpdate();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if(resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}
