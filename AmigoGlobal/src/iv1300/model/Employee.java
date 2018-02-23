package iv1300.model;

import iv1300.dao.TripDAO;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Christoffer on 2016-09-26.
 */
public class Employee {

    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String eMail;
    private ObservableList<Trip> trips;
    private float totalEmission;

    public Employee(int id, String firstName, String lastName, String phoneNumber, String eMail) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.eMail = eMail;
        trips = getTripsForEmployee(id);
        totalEmission = calculateTotalEmission(trips);
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEMail() {
        return eMail;
    }

    public ObservableList<Trip> getTrips() { return trips; }

    public float getTotalEmission() {
        return totalEmission;
    }

    public void addTrip(Trip trip) {
        trips.add(trip);
        totalEmission += trip.getTotalEmission();
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    private float calculateTotalEmission(List<Trip> trips) {
        if(trips.isEmpty()) { return 0.f; }
        float total = 0.f;
        for(Trip t : trips) {
            total += t.getTotalEmission();
        }
        return total;
    }

    private ObservableList<Trip> getTripsForEmployee(int id) {
        return TripDAO.getInstance().getTripsForEmployee(id);
    }
}
