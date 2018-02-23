package iv1300.model;

import iv1300.dao.EmployeeDAO;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Christoffer on 2016-09-26.
 */
public class Company {

    private int id;
    private String name;
    private ObservableList<Employee> employees;

    private int numberOfEmployees;
    private float totalEmission;
    private float averageEmission;

    public String getName() {
        return name;
    }

    public ObservableList<Employee> getEmployees() {
        return employees;
    }

    public float getTotalEmission() {
        return totalEmission;
    }

    public float getAverageEmission() {
        return averageEmission;
    }

    public Company(int id, String name) {
        this.id = id;
        this.name = name;
        employees = getEmployeesForCompany(id);
        numberOfEmployees = employees.size();
        totalEmission = calculateTotalEmission(employees);
        averageEmission = calculateAverageEmission();
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    private float calculateTotalEmission(List<Employee> employees) {
        if(employees.isEmpty()) { return 0.f; }
        float total = 0.f;
        for(Employee e : employees) {
            total += e.getTotalEmission();
        }
        return total;
    }

    private float calculateAverageEmission() {
        return (numberOfEmployees > 0) ? totalEmission/numberOfEmployees : 0;
    }

    private ObservableList<Employee> getEmployeesForCompany(int id) {
        return EmployeeDAO.getInstance().getEmployeesForCompany(id);
    }
}
