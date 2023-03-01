package client;

import dto.Message;
import utils.JsonUtil;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private String serverUrl;
    private int port;

    public Client(String serverUrl, int port) {
        this.serverUrl = serverUrl;
        this.port = port;
    }

    public void runClient() throws IOException {
        Socket clientSocket = new Socket("localhost", 80);
        Scanner userConsoleScanner = new Scanner(System.in);
        Scanner inFromServer = new Scanner(clientSocket.getInputStream());
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        Message messageToServer = new Message();
        Message messageFromServer;
        String request, response;


        while (true) {
            response = inFromServer.nextLine();
            messageFromServer = JsonUtil.fromJson(response);
            messageToServer.setCurrentMenu(messageFromServer.getCurrentMenu());
            System.out.println("Server's response: " + messageFromServer.getMessageText().replace(Message.DELIMITER, "\n"));

            System.out.print("Input message: ");
            request = userConsoleScanner.next();
            if (request.equals("exit")) {
                messageToServer.setMessageText("exit");
                outToServer.writeBytes(JsonUtil.toJson(messageToServer) + "\n");
                break;
            }

            messageToServer.setMessageText(request);
            outToServer.writeBytes(JsonUtil.toJson(messageToServer) + "\n");

            messageToServer = new Message();
        }
        clientSocket.close();
    }
}