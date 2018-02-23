package iv1300.controllers;

import iv1300.dao.CompanyDAO;
import iv1300.dao.EmployeeDAO;
import iv1300.dao.TripDAO;
import iv1300.model.*;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Christoffer on 2016-09-24.
 */
public class Controller {

    public List<Company> getAllCompanies() {
        return CompanyDAO.getInstance().getAll();
    }

    public Company getCompany(String name) {
        return CompanyDAO.getInstance().getCompany(name);
    }

    public List<Employee> getAllEmployees() {
        return EmployeeDAO.getInstance().getAll();
    }

    public List<Employee> getEmployeesForCompany(Company company) {
        return EmployeeDAO.getInstance().getEmployeesForCompany(company);
    }

    public void insertCompany(String name) {
        CompanyDAO.getInstance().insertCompany(name);
    }

    public int insertEmployee(int company, String firstName, String lastName,
                               String phoneNumber, String eMail) {
        return EmployeeDAO.getInstance().insertEmployee(company,firstName,lastName,phoneNumber,eMail);
    }

    public ObservableList<Vehicle> getAllVehicles() {
        return TripDAO.getInstance().getVehicles();
    }

    public Distance getDistance(float distance) {
        return TripDAO.getInstance().getDistance(distance);
    }

    public int addDistance(float distance) {
        return TripDAO.getInstance().addDistance(distance);
    }

    public int insertResa(int distId, int vehicleId) {
        return TripDAO.getInstance().insertResa(distId, vehicleId);
    }

    public int getResa(int distId, int vehicleId) {
        return TripDAO.getInstance().getResa(distId, vehicleId);
    }

    public boolean tripExists(int employeeId, int resaId) {
        return TripDAO.getInstance().tripExists(employeeId, resaId);
    }

    public int insertTrip(int employeeId, int resaId, int times) {
        return TripDAO.getInstance().insertTrip(employeeId, resaId, times);
    }

    public void addTripForEmploye(Employee employee, Trip trip) {
        employee.addTrip(trip);
    }
}
