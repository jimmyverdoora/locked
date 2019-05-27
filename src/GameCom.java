import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

class GameCom {

    // keyboard input
    private String input;
    private Scanner scanner = new Scanner(System.in);

    GameCom(Socket client) {
        // client constructor

        // receives the first player information
        String first = receive(client);

        // confirms reception to server
        if (first.equals("client") || first.equals("server")) {
            send("OK", client);
        }
        else {
            send("ERROR", client);
        }

        // receives the initial game state
        String state = receive(client);

        if (state.equals("ERROR")) {
            System.out.println("Error in receiving game state. Aborting.");
            return;
        }
        else {
            System.out.print(state);
        }

        // if the first player is the client it has the first move
        if (first.equals("server")) {

            // receives the game state after server move
            state = receive(client);
            if (state.equals("ERROR")) {
                System.out.println("Error in receiving game state. Aborting.");
                return;
            }
            else {
                System.out.print(state);
            }
        }

        while (true) {

            // sends the input
            while (true) {
                System.out.print("\nMove: ");
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                    return;
                }

                if (input.equalsIgnoreCase("help") || input.equalsIgnoreCase("h")) {
                    printHelp();
                }

                if (send(input, client)) {
                    System.out.println("Error sending move. Aborting.");
                    return;
                }

                String response = receive(client);
                if (response.equals("OK")) {
                    break;
                }
            }

            // receives the game states after client's move
            state = receive(client);
            if (state.equals("ERROR")) {
                System.out.println("Error in receiving game state. Aborting.");
                return;
            }
            else {
                System.out.print(state);
            }

            // checks victory
            if (state.toCharArray()[0] == "G".toCharArray()[0]) {
                return;
            }

            // receives the game state after server move
            state = receive(client);
            if (state.equals("ERROR")) {
                System.out.println("Error in receiving game state. Aborting.");
                return;
            }
            else {
                System.out.print(state);
            }

            // checks victory
            if (state.toCharArray()[0] == "G".toCharArray()[0]) {
                return;
            }
        }
    }

    GameCom(ServerSocket server) {
        // server constructor
        Socket client;

        try{
            client = server.accept();
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }

        // decides the first player
        String first;
        if (Math.random() > 0.5) {
            first = "server";
        }
        else {
            first = "client";
        }

        // sends the first player
        if (send(first, client)) {
            System.out.println("Error sending state. Aborting.");
            return;
        }

        // checks the client "OK"
        input = receive(client);
        if (!input.equals("OK")) {
            System.out.println("Client error. Aborting.");
        }

        // creates the game
        Game game = new Game(first);

        // send the game states
        String state = game.printState();
        if (send(state, client)) {
            System.out.println("Error sending state. Aborting.");
            return;
        }

        // prints the game state
        System.out.print(state);

        if (first.equals("server")) {
            // take move
            do {
                System.out.print("\nMove: ");
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                    return;
                }

                if (input.equalsIgnoreCase("help") || input.equalsIgnoreCase("h")) {
                    printHelp();
                }

            } while (game.checkMove(input));  // returns true if the move is wrong

            // if it's ok makes the move
            game.makeMove(input);

            // prints game state
            state = game.printState();
            System.out.print(state);

            // send the state
            if (send(state, client)) {
                System.out.print("Error sending state. Aborting.");
                return;
            }
        }

        // ---------- GAME LOOP ----------
        while (true) {

            // receives the move
            while (true) {
                input = receive(client);

                if (input.equals("ERROR")) {
                    System.out.println("Error receiving command. Aborting.");
                }
                if (game.checkMove(input)) {
                    if (send("NO", client)) {
                        System.out.println("Error sending response. Aborting.");
                        return;
                    }
                }
                else {
                    if (send("OK", client)) {
                        System.out.println("Error sending response. Aborting.");
                        return;
                    }
                    break;
                }
            }

            // make client's move
            game.makeMove(input);

            // prints game state
            state = game.printState();
            System.out.print(state);

            // send the state
            if (send(state, client)) {
                System.out.println("Error sending state. Aborting.");
                return;
            }

            // checks victory
            if (state.toCharArray()[0] == "G".toCharArray()[0]) {
                return;
            }

            // take move
            do {
                System.out.print("\nMove: ");
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("q")) {
                    return;
                }

                if (input.equalsIgnoreCase("help") || input.equalsIgnoreCase("h")) {
                    printHelp();
                }

            } while (game.checkMove(input));  // returns true if the move is wrong

            // if it's ok makes the move
            game.makeMove(input);

            // prints game state
            state = game.printState();
            System.out.print(state);

            // send the state
            if (send(state, client)) {
                System.out.println("Error sending state. Aborting.");
                return;
            }

            // checks victory
            if (state.toCharArray()[0] == "G".toCharArray()[0]) {
                return;
            }

        }
    }

    private boolean send(String cmd, Socket client) {
        // returns true if it fails!

        // every "\n" becomes "n" to be passed in one buffered line

        PrintWriter out;
        try {
            out = new PrintWriter(client.getOutputStream(), true);
        }
        catch (IOException e) {
            return true;
        }
        out.println(cmd.replace("\n", "N"));
        out.flush();
        return false;
    }

    private String receive(Socket client) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            return reader.readLine().replace("N", "\n");
        }
        catch (IOException e) {
            return "ERROR";
        }
    }

    private void printHelp() {
        System.out.println("To move a piece, enter the number of your piece," +
                "followed by the direction (u, d, l, r).");
    }

}
