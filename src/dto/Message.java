package dto;

public class Message {
    String messageText;
    short currentMenu;
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

}
