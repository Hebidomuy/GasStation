package gas_station;

import java.math.BigDecimal;

public class CheckItem {
   private Fuel fuel;
   private BigDecimal count;

    public CheckItem(Fuel name, BigDecimal count) {
        this.fuel = name;
        this.count = count;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public BigDecimal getCount() {
        return count;
    }

    public void setCount(BigDecimal count) {
        this.count = count;
    }

}
