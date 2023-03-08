package server;

import dto.Message;
import gas_station.Check;
import gas_station.Fuel;
import gas_station.GasStation;
import utils.JsonUtil;

import java.io.DataOutputStream;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerThread implements Runnable {
    private Socket socket;
    private GasStation gasStation;

    ServerThread(Socket s, GasStation gasStation) {
        socket = s;
        this.gasStation = gasStation;
    }

    @Override
    public void run() {
        try {
            Scanner inFromClient = new Scanner(socket.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            Message messageToUser = new Message();
            Message messageFromClient = new Message();
            String response, request;
            Boolean validCarNumber = false;
            messageToUser.setMessageText("Write your car number ");
            outToClient.writeBytes(JsonUtil.toJson(messageToUser) + "\n");

            while (!validCarNumber) {
                messageFromClient = JsonUtil.fromJson(inFromClient.nextLine());
                String text = messageFromClient.getMessageText();
                Pattern pattern = Pattern.compile("[A-Z]{2}[0-9]{4}[A-Z]{2}");
                Matcher matcher = pattern.matcher(text);
                while (matcher.find()) {
                    System.out.println(text.substring(matcher.start(), matcher.end()));
                    validCarNumber = true;

                }
                if (!validCarNumber) {
                    messageToUser.setMessageText("Invalid car number, try again ");
                    outToClient.writeBytes(JsonUtil.toJson(messageToUser) + "\n");
                }
            }
            messageToUser.setCurrentMenu((short) 1);
            messageToUser.setCarNumber(messageFromClient.getMessageText());
            messageToUser.setMessageText(getFirstMenu(gasStation));
            outToClient.writeBytes(JsonUtil.toJson(messageToUser) + "\n");

            while (true) {
                request = inFromClient.nextLine();
                messageFromClient = JsonUtil.fromJson(request);
                System.out.println("Log: " + messageFromClient.getMessageText());

                if (messageFromClient.getCurrentMenu() == (short) 1) {
                    if (isClientMessageInvalid(gasStation, messageFromClient)) {
                        messageToUser.setCurrentMenu((short) 1);
                        messageToUser.setMessageText(getFirstMenu(gasStation));
                        messageToUser = appendIncorrectInputMessage(messageToUser);
                    } else {
                        if (messageFromClient.getMessageText().equals("0")) {
                            messageToUser = getFourthMenu(gasStation, messageFromClient);
                        } else {
                            messageToUser.setCurrentMenu((short) 2);
                            messageToUser.setCurrentFuel(Short.parseShort(messageFromClient.getMessageText()));
                            messageToUser.setMessageText(getSecondMenu(gasStation, Short.parseShort(messageFromClient.getMessageText())));
                        }

                    }

                } else if (messageFromClient.getCurrentMenu() == (short) 2) {
                    if (isClientMessageInvalid(gasStation, messageFromClient)) {
                        messageToUser.setCurrentMenu((short) 2);
                        messageToUser.setMessageText(getSecondMenu(gasStation, messageFromClient.getCurrentFuel()));
                        messageToUser = appendIncorrectInputMessage(messageToUser);
                    } else {
                        messageToUser.setCurrentMenu((short) 3);
                        messageToUser.setMessageText(getThirdMenu());
                        switch (messageFromClient.getMessageText()) {
                            case "1":
                                messageToUser.setOrder(true);
                                break;
                            case "2":
                                messageToUser.setOrder(false);
                                break;
                            case "0":
                                messageToUser.setCurrentMenu((short) 1);
                                messageToUser.setMessageText(getFirstMenu(gasStation));
                                break;
                        }
                    }


                } else if (messageFromClient.getCurrentMenu() == (short) 3) {
                    if (isClientMessageInvalid(gasStation, messageFromClient)) {
                        messageToUser.setCurrentMenu((short) 3);
                        messageToUser.setMessageText(getThirdMenu());
                        messageToUser = appendIncorrectInputMessage(messageToUser);
                    } else {
                        messageFromClient.setMessageText(messageFromClient.getMessageText().replace(",", "."));
                        messageFromClient.setMessageText(getCurrentDouble(messageFromClient.getMessageText()));
                        if (messageFromClient.isOrder()) {
                            int checkNumber = gasStation.orderFuel(
                                    messageFromClient.getCurrentFuel(),
                                    messageFromClient.getCarNumber(),
                                 BigDecimal.valueOf(Double.parseDouble(messageFromClient.getMessageText()))
                            );
                            if (checkNumber > 0) {
                                messageToUser.setCurrentMenu((short) 1);
                                messageToUser.setMessageText(Message.DELIMITER + "Thanks, its your check " + Message.DELIMITER +
                                        gasStation.getCheckByNumber(checkNumber).getStringForPrintToConsole(Message.DELIMITER));

                                messageToUser.setOrder(null);
                                messageToUser.setCurrentFuel(null);
                                messageToUser.setMessageText(messageToUser.getMessageText() +
                                        getFirstMenu(gasStation));
                            } else {
                                messageToUser.setCurrentMenu((short) 3);
                                messageToUser.setMessageText(Message.DELIMITER + "Not enough fuel, " +
                                        Message.DELIMITER +
                                        "Remainder liters: " +
                                        gasStation.fuelQuantity(messageToUser.getCurrentFuel()) + " liters " +
                                        Message.DELIMITER +
                                        "enter the quantity you need");
                            }
                        } else {
                            int checkNumber = gasStation.bookFuel(
                                    messageFromClient.getCurrentFuel(),
                                    messageFromClient.getCarNumber(),
                                    BigDecimal.valueOf(Double.parseDouble(messageFromClient.getMessageText()))
                            );
                            if (checkNumber > 0) {
                                messageToUser.setCurrentMenu((short) 1);
                                messageToUser.setMessageText(Message.DELIMITER + "Thanks, its your check " + Message.DELIMITER +
                                        gasStation.getCheckByNumber(checkNumber).getStringForPrintToConsole(Message.DELIMITER) +
                                        "you can continue shopping ");

                                messageToUser.setOrder(null);
                                messageToUser.setCurrentFuel(null);
                                messageToUser.setMessageText(messageToUser.getMessageText() +
                                        getFirstMenu(gasStation));
                            } else {
                                messageToUser.setCurrentMenu((short) 3);
                                messageToUser.setMessageText(Message.DELIMITER + "Not enough fuel, " +
                                        Message.DELIMITER +
                                        "Remainder liters: " +
                                        gasStation.fuelQuantity(messageToUser.getCurrentFuel()) + " liters " +
                                        Message.DELIMITER +
                                        "enter the quantity you need");
                            }
                        }
                    }
                } else if (messageFromClient.getCurrentMenu() == (short) 4) {
                    if (isClientMessageInvalid(gasStation, messageFromClient)) {
                        messageToUser.setCurrentMenu((short) 4);
                        messageToUser = getFourthMenu(gasStation, messageFromClient);
                        messageToUser = appendIncorrectInputMessage(messageToUser);
                    } else {
                        if (messageFromClient.getMessageText().equals("1")) {
                            messageToUser.setCurrentMenu((short) 1);
                            Check clientCheck = gasStation.getCheckByCarNumber(messageFromClient.getCarNumber());
                            messageToUser.setMessageText(Message.DELIMITER +
                                    "Its your check " + Message.DELIMITER +
                                    clientCheck.getStringForPrintToConsole(Message.DELIMITER) +
                                    "Thank you for order  " + Message.DELIMITER +
                                    getFirstMenu(gasStation));
                            clientCheck.setClose(true);
                        } else if (messageFromClient.getMessageText().equals("0")) {
                            messageToUser.setCurrentMenu((short) 1);
                            messageToUser.setMessageText(
                                    getFirstMenu(gasStation));
                        }
                    }
                }

                outToClient.writeBytes(JsonUtil.toJson(messageToUser) + "\n");
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public String getFirstMenu(GasStation gasStation) {
        StringBuilder result = new StringBuilder(Message.DELIMITER + "Menu:" + Message.DELIMITER);
        for (Fuel fuel : gasStation.getFuelList()) {
            result.append(fuel.getSortWeight()).append(". ")
                    .append(fuel.getName()).append(" : ");
            if (fuel.getLitersAvailable().equals(BigDecimal.valueOf(0))) {
                result.append("00.00");
            } else {
                result.append(fuel.getPrice().setScale(2));
            }
            result.append(" UAH ")
                    .append(Message.DELIMITER);
        }
        result
                .append(0).append(". ")
                .append("Basket")
                .append(Message.DELIMITER);
        return result.toString();
    }

    public String getSecondMenu(GasStation gasStation, short currentFuel) {
        return Message.DELIMITER +
                "Remainder liters: " +
                gasStation.fuelQuantity(currentFuel) + " liters. " +
                Message.DELIMITER +
                (gasStation.fuelQuantity(currentFuel).compareTo(BigDecimal.valueOf(0)) > 0 ?
                        "1. Order " + Message.DELIMITER :
                        "") +
                (gasStation.fuelQuantity(currentFuel).compareTo(BigDecimal.valueOf(0)) > 0 ?
                        "2. Book " + Message.DELIMITER :
                        "") +
                "0. Get out " + Message.DELIMITER;
    }

    public String getThirdMenu() {
        return Message.DELIMITER + "enter the quantity you need";
    }

    public Message getFourthMenu(GasStation gasStation, Message message) {
        Check clientCheck = gasStation.getCheckByCarNumber(message.getCarNumber());
        if (clientCheck == null) {

            message.setCurrentMenu((short) 1);
            message.setMessageText(Message.DELIMITER +
                    "You dont have open check, choice some product :" +
                    getFirstMenu(gasStation));
        } else {
            message.setCurrentMenu((short) 4);
            message.setMessageText(Message.DELIMITER +
                    "Its your check " + Message.DELIMITER +
                    clientCheck.getStringForPrintToConsole(Message.DELIMITER) +
                    "1. for order  " + Message.DELIMITER +
                    "0. back to main menu"
            );
        }
        return message;
    }

    public String[] getFirstMenuNumbers(GasStation gasStation) {
        StringBuilder result = new StringBuilder("0" + Message.DELIMITER);

        for (Fuel item : gasStation.getFuelList()) {
            result.append(item.getSortWeight()).append(Message.DELIMITER);
        }

        return result.toString().split(Message.DELIMITER);
    }

    public String[] getSecondMenuNumbers(GasStation gasStation, short currentFuel) {
        StringBuilder result = new StringBuilder("0" + Message.DELIMITER);
        gasStation.fuelQuantity(currentFuel);
        if (gasStation.fuelQuantity(currentFuel).compareTo(BigDecimal.valueOf(0)) > 0) {
            result
                    .append(1).append(Message.DELIMITER)
                    .append(2).append(Message.DELIMITER);
        }
        return result.toString().split(Message.DELIMITER);
    }

    public Boolean getThirdMenuNumbers(Message message) {
        String text = message.getMessageText();
        text = text.replace(",", ".");
        Pattern pattern = Pattern.compile("^[0-9]+[.]?[0-9]+$");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return false;
        } else {
            pattern = Pattern.compile("^[0-9]+$");
            matcher = pattern.matcher(text);
            return !matcher.find();
        }
    }

    public String[] getFourthMenuNumbers() {
        String result = "0" + Message.DELIMITER +
                1 + Message.DELIMITER;
        return result.split(Message.DELIMITER);
    }

    public Message appendIncorrectInputMessage(Message message) {
        message.setMessageText(Message.DELIMITER + "----- Incorrect input -----" + message.getMessageText());
        return message;
    }

    public Boolean isClientMessageInvalid(GasStation gasStation, Message message) {
        boolean invalidInput = true;

        String[] menuNumbers;
        switch (message.getCurrentMenu()) {
            case (1):
                menuNumbers = getFirstMenuNumbers(gasStation);
                break;
            case (2):
                menuNumbers = getSecondMenuNumbers(gasStation, message.getCurrentFuel());
                break;
            case (3):
                return getThirdMenuNumbers(message);
            case (4):
                menuNumbers = getFourthMenuNumbers();
                break;
            default:
                menuNumbers = new String[0];
        }
        for (String menuNumber : menuNumbers) {
            if (message.getMessageText().equals(menuNumber)) {
                invalidInput = false;
                break;
            }
        }
        return invalidInput;
    }

   private String getCurrentDouble(String input) {
       if (input.contains(".")) {
           if (input.substring(input.indexOf(".")).length() > 3) {
               return input.substring(0, input.indexOf(".") + 3);
           } else {
               return input;
           }
       } else {
           return input;
       }
   }
}