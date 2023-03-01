package server;

import dto.Message;
import utils.JsonUtil;

import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerThread implements Runnable {
    private Socket socket;

    ServerThread(Socket s) {
        socket = s;
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
            messageToUser.setMessageText(
                    Message.DELIMITER + "Menu:  " + Message.DELIMITER +
                            "1. A-95 : 47.00 UAH " + Message.DELIMITER +
                            "2. A-92 : 43.00 UAH " + Message.DELIMITER +
                            "3. Gas : 24.00 UAH " + Message.DELIMITER +
                            "4. Diesel : 49.00 UAH " + Message.DELIMITER
            );
            outToClient.writeBytes(JsonUtil.toJson(messageToUser) + "\n");

            while (true) {
                request = inFromClient.nextLine();
                messageFromClient = JsonUtil.fromJson(request);
                System.out.println("Log: " + messageFromClient.getMessageText());

                if (messageFromClient.getCurrentMenu() == (short) 1) {
                    switch (messageFromClient.getMessageText()) {
                        case "1":
                            messageToUser.setMessageText("In stock 100 l. ");
                            break;
                        case "2":
                            messageToUser.setMessageText("In stock 50 l. ");
                            break;
                        case "3":
                            messageToUser.setMessageText("In stock 25 l.");
                            break;
                        case "4":
                            messageToUser.setMessageText("In stock 10 l. ");
                            break;
                    }
                    messageToUser.setCurrentMenu((short) 2);
                    messageToUser.setMessageText(
                            messageToUser.getMessageText() + Message.DELIMITER +
                                    "1. Замовити " + Message.DELIMITER +
                                    "2. Забронювати " + Message.DELIMITER +
                                    "0. Вийти " + Message.DELIMITER
                    );
                } else if (messageFromClient.getCurrentMenu() == (short) 2) {
                    switch (messageFromClient.getMessageText()) {
                        case "1":
                            messageToUser.setMessageText("Замовив");
                            break;
                        case "2":
                            messageToUser.setMessageText("Забронював ");
                            break;
                        case "0":
                            messageToUser.setCurrentMenu((short) 1);
                            messageToUser.setMessageText(
                                    Message.DELIMITER + "Menu:  " + Message.DELIMITER +
                                            "1. A-95 : 47.00 UAH " + Message.DELIMITER +
                                            "2. A-92 : 43.00 UAH " + Message.DELIMITER +
                                            "3. Gas : 24.00 UAH " + Message.DELIMITER +
                                            "4. Diesel : 49.00 UAH " + Message.DELIMITER
                            );
                            break;
                    }
                }


                outToClient.writeBytes(JsonUtil.toJson(messageToUser) + "\n");

                messageToUser = new Message();
            }


        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }


    //String fff =
    //        " _  _ _  " +
    //        " | || |  " +
    //        " | || |_ " +
    //        " |__   _ " +
    //        "    |_|    ";

    //        "     _         ___  ____        _  _ _____ ___   ___    _   _   _    _   _ ?" +
    //        "    / \\       / _ \\| ___|   _  | || |___  / _ \\ / _ \\  | | | | / \\  | | | |?" +
    //        "   / _ \\ ____| (_) |___ \\  (_) | || |_ / / | | | | | | | | | |/ _ \\ | |_| |?" +
    //        "  / ___ \\_____\\__, |___) |  _  |__   _/ /| |_| | |_| | | |_| / ___ \\|  _  |?" +
    //        " /_/   \\_\\      /_/|____/  (_)    |_|/_(_)\\___/ \\___/   \\___/_/   \\_\\_| |_|?" +
    //        "    _         ___ ____        _  _  _____  ___   ___    _   _   _    _   _ ?" +
    //        "    / \\       / _ \\___ \\   _  | || ||___ / / _ \\ / _ \\  | | | | / \\  | | | |?" +
    //        "   / _ \\ ____| (_) |__) | (_) | || |_ |_ \\| | | | | | | | | | |/ _ \\ | |_| |?" +
    //        "  / ___ \\_____\\__, / __/   _  |__   _|__) | |_| | |_| | | |_| / ___ \\|  _  |?" +
    //        " /_/   \\_\\      /_/_____| (_)    |_||____(_)___/ \\___/   \\___/_/   \\_\\_| |_|?" +
    //        "  ____                 ____  _  _    ___   ___    _   _   _    _   _ ?" +
    //        "  / ___| __ _ ___   _  |___ \\| || |  / _ \\ / _ \\  | | | | / \\  | | | |?" +
    //        " | |  _ / _` / __| (_)   __) | || |_| | | | | | | | | | |/ _ \\ | |_| |?" +
    //        " | |_| | (_| \\__ \\  _   / __/|__   _| |_| | |_| | | |_| / ___ \\|  _  |?" +
    //        "  \\____|\\__,_|___/ (_) |_____|  |_|(_)___/ \\___/   \\___/_/   \\_\\_| |_|?" +
    //        "   ____  _                _       _  _   ___   ___   ___    _   _   _    _   _ ?" +
    //        " |  _ \\(_) ___  ___  ___| |  _  | || | / _ \\ / _ \\ / _ \\  | | | | / \\  | | | |?" +
    //        " | | | | |/ _ \\/ __|/ _ \\ | (_) | || || (_) | | | | | | | | | | |/ _ \\ | |_| |?" +
    //        " | |_| | |  __/\\__ \\  __/ |  _  |__   _\\__, | |_| | |_| | | |_| / ___ \\|  _  |?" +
    //        " |____/|_|\\___||___/\\___|_| (_)    |_|   /_(_)___/ \\___/   \\___/_/   \\_\\_| |_|?" +
    //        "
}