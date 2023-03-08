package gas_station;

import dto.Message;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GasStation {
    String name;
    String address;
    private List<Fuel> fuelList;
    private List<Check> checkList;

    public GasStation(String name, String address) {
        this.name = name;
        this.address = address;
        this.fuelList = new ArrayList<>();
        this.checkList = new ArrayList<>();
    }

    public BigDecimal fuelQuantity(short fuelWeight) {
        BigDecimal result = BigDecimal.valueOf(0);

        for (Fuel item : getFuelList()) {
            if (item.getSortWeight().equals(fuelWeight)) {
                result = item.getLitersAvailable();
            }
        }

        return result;
    }

    public Check getCheckByNumber(int checkNumber) {
        for (Check item : this.getCheckList()) {
            if (item.getNumber() == checkNumber) {
                return item;
            }
        }
        return null;
    }

    public Check getCheckByCarNumber(String carNumber) {
        for (Check item : this.getCheckList()) {
            if (item.getCarNumber().equals(carNumber) && !item.getClose()) {
                return item;
            }
        }
        return null;
    }

    public Fuel getFuelFromGasStation(short fuelWeight) {
        Fuel result = null;
        for (Fuel item : getFuelList()) {
            if (item.getSortWeight().equals(fuelWeight)) {
                result = item;
            }
        }
        return result;
    }

    public int orderFuel(Short currentFuel, String carNumber, BigDecimal clientOrder) {
        int result = -1;
        Fuel fuel = getFuelFromGasStation(currentFuel);

        if (fuel.getLitersAvailable().compareTo(clientOrder) >= 0) {
            List<Check> tempList = this.getCheckList();
            Check check = new Check(carNumber, fuel, clientOrder, true);
            tempList.add(check);
            this.setCheckList(tempList);
            result = check.getNumber();
            fuel.setLitersAvailable(fuel.getLitersAvailable().subtract(clientOrder));
        }
        return result;
    }

    public int bookFuel(Short currentFuel, String carNumber, BigDecimal clientOrder) {
        int result = -1;
        Fuel fuel = getFuelFromGasStation(currentFuel);

        if (fuel.getLitersAvailable().compareTo(clientOrder) >= 0) {
            List<Check> tempList = this.getCheckList();
            Check clientCheck = null;
            for (Check item : tempList) {
                if (item.getCarNumber().equals(carNumber) && !item.getClose()) {
                    clientCheck = item;
                }
            }

            if (clientCheck == null) {
                clientCheck = new Check(carNumber, fuel, clientOrder, false);
                tempList.add(clientCheck);
                this.setCheckList(tempList);
                result = clientCheck.getNumber();
            } else {

                List<CheckItem> tempCheckItemList = clientCheck.getCheckItems();
                CheckItem tempCheckItem = null;
                for (CheckItem checkItem : tempCheckItemList) {
                    if (checkItem.getFuel().getSortWeight().equals(fuel.getSortWeight())) {
                        tempCheckItem = checkItem;
                    }
                }
                if (tempCheckItem == null) {
                    tempCheckItemList.add(new CheckItem(fuel, clientOrder));
                } else {
                    tempCheckItem.setCount(tempCheckItem.getCount().add(clientOrder));
                }
                clientCheck.setCheckItems(tempCheckItemList);
                result = clientCheck.getNumber();
            }
            fuel.setLitersAvailable(fuel.getLitersAvailable().subtract(clientOrder));
        }

        return result;
    }

    public List<Fuel> getFuelList() {
        fuelList.sort(Comparator.comparing(Fuel::getSortWeight));
        return fuelList;
    }

    public void setFuelList(List<Fuel> fuelList) {
        this.fuelList = fuelList;
    }

    public List<Check> getCheckList() {
        return checkList;
    }

    public void setCheckList(List<Check> checkList) {
        this.checkList = checkList;
    }
}