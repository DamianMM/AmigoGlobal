package iv1300.model;

/**
 * Created by Christoffer on 2016-10-06.
 */
public class Distance {

    private int id;
    private float dist;

    public Distance(int id, float dist) {
        this.id = id;
        this.dist = dist;
    }

    public int getId() {
        return id;
    }

    public float getDist() {
        return dist;
    }
}
