import client.Client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        Client client = new Client("localhost", 9889);
        client.runClient();
    }
}
