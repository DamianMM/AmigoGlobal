package iv1300.dao;

import iv1300.model.Distance;
import iv1300.model.Employee;
import iv1300.model.Trip;
import iv1300.model.Vehicle;
import iv1300.util.SqlConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Christoffer on 2016-09-26.
 */
public class TripDAO {

    private static TripDAO instance = new TripDAO();

    public static TripDAO getInstance() {
        return instance;
    }

    private TripDAO() {}

    private Connection connection = SqlConnection.getInstance().getCon();

    public ObservableList<Trip> getTripsForEmployee(int id) {

        ObservableList<Trip> trips = FXCollections.observableArrayList();

        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT a.id, f.id, a.antal, s.kilometer, f.namn, f.kilometerutsläpp " +
                                                                              "FROM anställdresa AS a, resa AS r, sträcka AS s, fordon AS f " +
                                                                              "WHERE a.anställd = ? " +
                                                                              "AND r.id = a.resa AND r.sträcka = s.id AND f.id = r.fordon");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                trips.add(new Trip(resultSet.getInt("a.id"),
                                   new Vehicle(resultSet.getInt("f.id"),
                                               resultSet.getString("namn"),
                                               resultSet.getFloat("kilometerutsläpp")),
                                   resultSet.getFloat("kilometer"),
                                   resultSet.getInt("antal")));
            }
        } catch (SQLException e) {
            System.out.println("Couldn't fetch trips for employee: " + id + "\n");
            e.printStackTrace();
        }

        return trips;
    }

    public ObservableList<Trip> getTripsForEmployee(Employee employee) {
        return getTripsForEmployee(employee.getId());
    }

    public ObservableList<Vehicle> getVehicles() {
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM fordon");

            while(resultSet.next()) {
                vehicles.add(new Vehicle(resultSet.getInt("id"),
                                         resultSet.getString("namn"),
                                         resultSet.getFloat("kilometerutsläpp")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return vehicles;
    }

    public Distance getDistance(float distance) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM sträcka " +
                                                                              "WHERE ABS(kilometer - ?) < 0.001");

            preparedStatement.setFloat(1, distance);
            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                return new Distance(resultSet.getInt("id"),
                                    resultSet.getFloat("kilometer"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addDistance(float distance) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO sträcka (kilometer) VALUES (?)",
                                                                                Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setFloat(1, distance);

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

    public int insertResa(int distId, int vehicleId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO resa (sträcka, fordon) VALUES (?,?)",
                                                                                Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,distId);
            preparedStatement.setInt(2,vehicleId);

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

    public int getResa(int distId, int vehicleId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT id FROM resa " +
                                                                              "WHERE sträcka = ? AND fordon = ?");

            preparedStatement.setInt(1,distId);
            preparedStatement.setInt(2,vehicleId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while(resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public boolean tripExists(int employeeId, int resaId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM anställdresa " +
                                                                              "WHERE anställd = ? AND resa= ?");

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, resaId);

            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int insertTrip(int employeeId, int resaId, int times) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO anställdresa (anställd,resa,antal) " +
                                                                              "VALUES (?,?,?)",Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setInt(1, employeeId);
            preparedStatement.setInt(2, resaId);
            preparedStatement.setInt(3, times);

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
