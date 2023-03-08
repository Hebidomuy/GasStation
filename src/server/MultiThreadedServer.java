package server;

import gas_station.GasStation;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedServer {

    public static void start(GasStation gasStation) throws IOException {
        ServerSocket serverSocket = new ServerSocket(90);
        while (true) {
            Socket socket = serverSocket.accept();
            Thread t = new Thread(new ServerThread(socket, gasStation));
            t.start();
        }
    }
}


