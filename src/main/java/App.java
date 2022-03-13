import java.io.IOException;

class App {
    public static void main(String[] args) {
        Client c = null;
        try {
            c = new Client("localhost", 50000);
            c.makeHandshake();
            while (true) {

            }
        } catch (IOException e) {
            System.out.println("Exception: " + e);
        } finally {
            if (c != null)
                c.stopConnection();
        }

    }
}
