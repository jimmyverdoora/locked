import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {

        if (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("h")) {
            printHelp();
        }

        else if (args[0].equals("c") || args[0].equals("client")) {
            Socket link;
            try {
                int portNumber;
                if (args.length < 3) {
                    portNumber = 8888;
                }
                else {
                    portNumber = Integer.parseInt(args[2]);
                }
                String host = args[1];
                link = new Socket(host, portNumber);
            }
            catch (Exception e) {
                System.out.println(e.toString());
                return;
            }
            new GameCom(link);
        }

        else if (args[0].equals("s") || args[0].equals("server")) {
            ServerSocket link;
            try {
                int portNumber;
                if (args.length < 2) {
                    portNumber = 8888;
                }
                else {
                    portNumber = Integer.parseInt(args[2]);
                }
                link = new ServerSocket(portNumber);
            }
            catch (Exception e) {
                System.out.println(e.toString());
                return;
            }
            new GameCom(link);
        }

    }

    private static void printHelp() {
        System.out.println("--- Welcome to the swaggest game EUW! ---\n" +
                "To play, decide whether you are the client or server.\n" +
                "If you are the server, just run:\n" +
                "\n" +
                "   java -jar locked.jar server [port (optional, default: 8888)]\n" +
                "\n" +
                "If you are the client, you run:\n" +
                "\n" +
                "   java -jar locked.jar client [serverAddress] [port (optional)]" +
                "\n");
    }
}
