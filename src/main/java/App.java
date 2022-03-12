import java.io.IOException;

class App {
    public static void main(String[] args){
        Client c = null;
        try {
           c = new Client(null, 50000);
            while (true) {
                c.sendMsg(CmdConstants.HELO);
                String str = c.recvMsg();
                System.out.println(str);

            }
        } catch(IOException e) {
            System.out.println("Exception: " + e);
        } finally {
            if (c != null)
                c.stopConnection();
        }


    }
}
