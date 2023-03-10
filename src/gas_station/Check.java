package gas_station;

import utils.RandomInt;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Check {
    private int number;
    private String clientName;
    private String carNumber;

    private Boolean isClose;
    private List<CheckItem> checkItems = new ArrayList<>();


    public Check(String clientName, String carNumber, List<CheckItem> checkItems) {
        this.number = RandomInt.getRandomNumberUsingNextInt(1, 100000);
        this.clientName = clientName;
        this.carNumber = carNumber;
        this.checkItems = checkItems;
    }

    public Check(String clientName, String carNumber, Fuel fuel, BigDecimal clientOrder, boolean isClose) {
        this.number = RandomInt.getRandomNumberUsingNextInt(1, 100000);
        this.clientName = clientName;
        this.carNumber = carNumber;
        List<CheckItem> tempCheckItemList = new ArrayList<>();
        tempCheckItemList.add(new CheckItem(fuel, clientOrder));
        this.checkItems = tempCheckItemList;
        this.isClose = isClose;
    }

    public String getStringForPrintToConsole(String delimiter) {
        BigDecimal totalPrice = BigDecimal.valueOf(0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("================================").append(delimiter)
                .append("        Check № : " + this.getNumber()).append(delimiter).append(delimiter)
                .append("Client name: " + this.getClientName()).append(delimiter).append(delimiter)
                .append("Car number: " + this.getCarNumber()).append(delimiter).append(delimiter);
        for (CheckItem item : this.getCheckItems()) {
            stringBuilder
                    .append(item.getFuel().getName())
                    .append(" X ")
                    .append(item.getCount()).append(" liters ")
                    .append(" : ")
                    .append(item.getCount().multiply(item.getFuel().getPrice()).setScale(2))
                    .append(" UAH")
                    .append(delimiter);
            totalPrice = totalPrice.add(item.getCount().multiply(item.getFuel().getPrice()));
        }
        stringBuilder
                .append(delimiter)
                .append("TOTAL PRICE: ").append(totalPrice.setScale(2)).append(" UAH")
                .append(delimiter)
                .append("================================")
                .append(delimiter);
        return stringBuilder.toString();
    }

    public int getNumber() {
        return number;
    }

    public Boolean getClose() {
        return isClose;
    }

    public void setClose(Boolean close) {
        isClose = close;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public List<CheckItem> getCheckItems() {
        return checkItems;
    }

    public void setCheckItems(List<CheckItem> checkItems) {
        this.checkItems = checkItems;
    }
}
