package iv1300.dao;

import iv1300.model.Company;
import iv1300.util.SqlConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoffer on 2016-09-26.
 */
public class CompanyDAO {

    private static CompanyDAO instance = new CompanyDAO();

    public static CompanyDAO getInstance() { return instance; }

    private CompanyDAO() {}

    private Connection connection = SqlConnection.getInstance().getCon();

    public List<Company> getAll() {

        List<Company> companies = new ArrayList<>();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM företag");

            while(resultSet.next()) {
                companies.add(new Company(resultSet.getInt("id"),
                                          resultSet.getString("namn")));
            }
        } catch (SQLException e) {
            System.out.println("Could't fetch all companies.\n");
            e.printStackTrace();
        }

        return companies;
    }

    public Company getCompany(String name) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM företag " +
                                                                              "WHERE namn = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            resultSet.next();
            return new Company(resultSet.getInt("id"),
                               resultSet.getString("namn"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertCompany(String name) {

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement("INSERT INTO företag (namn) VALUES (?)");
            preparedStatement.setString(1, name);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
