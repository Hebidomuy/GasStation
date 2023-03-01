package gas_station;

public class Fuel {
    Short sortWeight;
    String name;
    double price;
    double litersAvailable;

    public Fuel(String name, double price, double litersAvailable) {
        this.name = name;
        this.price = price;
        this.litersAvailable = litersAvailable;
    }
}
