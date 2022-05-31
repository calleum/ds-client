
class App {
    public static void main(String[] args) {
        Client c = null;
        try {
            c = new Client("127.0.0.1", 50000);
            c.run();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (c != null)
                c.stopConnection();
        }

    }
}
