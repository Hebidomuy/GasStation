package gas_station;

import java.math.BigDecimal;

public class Fuel {
    private Short sortWeight;
    private String name;
    private BigDecimal price;
    private BigDecimal litersAvailable;

    public Fuel(Short sortWeight, String name, BigDecimal price, BigDecimal litersAvailable) {
        this.sortWeight = sortWeight;
        this.name = name;
        this.price = price;
        this.litersAvailable = litersAvailable;
    }

    public Short getSortWeight() {
        return sortWeight;
    }

    public void setSortWeight(Short sortWeight) {
        this.sortWeight = sortWeight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getLitersAvailable() {
        return litersAvailable;
    }

    public void setLitersAvailable(BigDecimal litersAvailable) {
        this.litersAvailable = litersAvailable;
    }
}