package dto;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class Message {
    String messageText;
    String carNumber;
    short currentMenu;
    Short currentFuel;
    Boolean isOrder;
    public static final String DELIMITER = "asdasdqwer";

    public Message(String messageText, short currentMenu) {
        this.messageText = messageText;
        this.currentMenu = currentMenu;
    }

    public Message() {
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public short getCurrentMenu() {
        return currentMenu;
    }

    public void setCurrentMenu(short currentMenu) {
        this.currentMenu = currentMenu;
    }

    public Short getCurrentFuel() {
        return currentFuel;
    }

    public void setCurrentFuel(Short currentFuel) {
        this.currentFuel = currentFuel;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Boolean isOrder() {
        return isOrder;
    }

    public void setOrder(Boolean order) {
        isOrder = order;
    }
}