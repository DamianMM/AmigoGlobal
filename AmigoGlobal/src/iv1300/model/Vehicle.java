package iv1300.model;

/**
 * Created by Christoffer on 2016-10-06.
 */
public class Vehicle {

    private int id;
    private String name;
    private float emissionPerKm;

    public Vehicle(int id, String name, float emissionPerKm) {
        this.id = id;
        this.name = name;
        this.emissionPerKm = emissionPerKm;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float getEmissionPerKm() {
        return emissionPerKm;
    }

    @Override
    public String toString() {
        return name;
    }
}
