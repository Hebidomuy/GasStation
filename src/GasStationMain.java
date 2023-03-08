import gas_station.Fuel;
import gas_station.GasStation;
import server.MultiThreadedServer;

import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GasStationMain {
    public static void main(String[] args) throws IOException {
        GasStation gasStation =new GasStation("WOG", "90 Peremohy Street");

        List<Fuel> fuelList = new ArrayList<>();
        fuelList.add(new Fuel((short) 1, "A95", BigDecimal.valueOf(47.00d), BigDecimal.valueOf(47.00d)));
        fuelList.add(new Fuel((short) 2,"A92", BigDecimal.valueOf(43.00d), BigDecimal.valueOf(50.00d)));
        fuelList.add(new Fuel((short) 3,"Gas", BigDecimal.valueOf(24.00d), BigDecimal.valueOf(25.00d)));
        fuelList.add(new Fuel((short) 4,"Diesel", BigDecimal.valueOf(49.00d), BigDecimal.valueOf(10.00d)));
        fuelList.add(new Fuel((short) 5,"Diesel+", BigDecimal.valueOf(49.00d), BigDecimal.valueOf(15.00d)));
        fuelList.add(new Fuel((short) 6,"Diesel++", BigDecimal.valueOf(49.00d), BigDecimal.valueOf(10.00d)));
        gasStation.setFuelList(fuelList);


        MultiThreadedServer.start(gasStation);
    }
}