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
        Socket clientSocket = new Socket(this.serverUrl, this.port);
        Scanner userConsoleScanner = new Scanner(System.in);
        Scanner inFromServer = new Scanner(clientSocket.getInputStream());
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        Message message;
        String request, response;

        while (true) {
            response = inFromServer.nextLine();
            message = JsonUtil.fromJson(response);
            System.out.println("Server's response: " + message.getMessageText().replace(Message.DELIMITER, "\n"));

            System.out.print("Input message: ");
            request = userConsoleScanner.next();
            if (request.equals("exit")) {
                message.setMessageText("exit");
                outToServer.writeBytes(JsonUtil.toJson(message) + "\n");
                break;
            }

            message.setMessageText(request);
            outToServer.writeBytes(JsonUtil.toJson(message) + "\n");

        }
        clientSocket.close();
    }
}