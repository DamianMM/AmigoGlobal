package iv1300.model;

/**
 * Created by Christoffer on 2016-09-26.
 *
 * Class to represent a recurring trip for an employee.
 * Such a trip is characterized by the vehicle used,
 * the vehicles emission, the distance of the trip as
 * well as
 */
public class Trip {

    private int id;

    private Vehicle vehicle;

    private float distance;
    private int timesTraveled;
    private float totalEmission;

    public Trip(int id, Vehicle vehicle, float distance, int timesTraveled) {
        this.id = id;
        this.vehicle = vehicle;
        this.distance = distance;
        this.timesTraveled = timesTraveled;
        totalEmission = calculateTotalEmission(distance, vehicle.getEmissionPerKm(), timesTraveled);
    }

    public Vehicle getVehicle() { return vehicle; }

    public float getTotalEmission() {
        return totalEmission;
    }

    @Override
    public String toString() {
        return "Resa"
                + "\n\tFordon: " + vehicle.getName()
                + "\n\tUtsläpp per kilometer: " + vehicle.getEmissionPerKm()
                + "\n\tSträcka: " + distance
                + "\n\tTotalt utsläpp: " + totalEmission
                + "\n\tAntal: " + timesTraveled + "\n";
    }

    private float calculateTotalEmission(float distance, float emissionPerKM, int timesTraveled) {
        return distance * emissionPerKM * timesTraveled;
    }

}
