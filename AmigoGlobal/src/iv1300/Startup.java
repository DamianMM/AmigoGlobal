package iv1300;

import iv1300.controllers.Controller;
import iv1300.util.SqlConnection;
import iv1300.views.View;

/**
 * Created by Christoffer on 2016-09-22.
 */
public class Startup {

    public static void main(String[] args) {
        SqlConnection.getInstance().connect();

        Controller controller = new Controller();

        View v = new View();
        v.setController(controller);
        View.startView();
    }
}