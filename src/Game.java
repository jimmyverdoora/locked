class Game {

    private String current;
    private Board board;

    Game(String first) {
        this.current = first;
        this.board = new Board();
    }

    void makeMove(String cmd) {

        char direction = cmd.toCharArray()[1];
        int number = Character.getNumericValue(cmd.toCharArray()[0]);

        board.makeMove(current, number, Character.toString(direction));

        // changes player
        if (current.equals("server")) {
            current = "client";
        }
        else {
            current = "server";
        }
    }

    private String checkVictory() {
        return board.checkVictory();
    }

    boolean checkMove(String cmd) {
        // returns true if the move is WRONG!

        char direction;
        int number;

        if (cmd.length() != 2) {
            return true;
        }
        try {
            direction = cmd.toCharArray()[1];
            number = Character.getNumericValue(cmd.toCharArray()[0]);
        }
        catch (Exception e) {
            return true;
        }

        if (number < 1 || number > 3) {
            return true;
        }
        // takes the piece which is going to be moved
        Piece piece = board.getReals(current)[number - 1];

        if (Character.toString(direction).equals("u")) {
            // there is the border -> no
            if (piece.getRow() == 0) {
                return true;
            }
            // there is nothing -> yes
            else if (board.getPieces()[piece.getRow() - 1][piece.getCol()].getColor() == 0) {
                return false;
            }
            // the other possible option is having a different color above and empty above that
            else {
                return (!(piece.getRow() > 1 &&
                        board.getPieces()[piece.getRow() - 1][piece.getCol()].getColor() != piece.getColor() &&
                        board.getPieces()[piece.getRow() - 2][piece.getCol()].getColor() == 0));
            }
        }

        else if (Character.toString(direction).equals("d")) {
            // there is the border -> no
            if (piece.getRow() == board.getPieces().length - 1) {
                return true;
            }
            // there is nothing -> yes
            else if (board.getPieces()[piece.getRow() + 1][piece.getCol()].getColor() == 0) {
                return false;
            }
            // the other possible option is having a different color above and empty above that
            else {
                return (!(piece.getRow() < board.getPieces().length - 2 &&
                        board.getPieces()[piece.getRow() + 1][piece.getCol()].getColor() != piece.getColor() &&
                        board.getPieces()[piece.getRow() + 2][piece.getCol()].getColor() == 0));
            }
        }

        else if (Character.toString(direction).equals("l")) {
            // there is the border -> no
            if (piece.getCol() == 0) {
                return true;
            }
            // there is nothing -> yes
            else if (board.getPieces()[piece.getRow()][piece.getCol() - 1].getColor() == 0) {
                return false;
            }
            // the other possible option is having a different color above and empty above that
            else {
                return (!(piece.getCol() > 1 &&
                        board.getPieces()[piece.getRow()][piece.getCol() - 1].getColor() != piece.getColor() &&
                        board.getPieces()[piece.getRow()][piece.getCol() - 2].getColor() == 0));
            }
        }

        else if (Character.toString(direction).equals("r")) {
            // there is the border -> no
            if (piece.getCol() == board.getPieces()[0].length) {
                return true;
            }
            // there is nothing -> yes
            else if (board.getPieces()[piece.getRow()][piece.getCol() + 1].getColor() == 0) {
                return false;
            }
            // the other possible option is having a different color above and empty above that
            else {
                return (!(piece.getCol() < board.getPieces()[0].length - 2 &&
                        board.getPieces()[piece.getRow()][piece.getCol() + 1].getColor() != piece.getColor() &&
                        board.getPieces()[piece.getRow()][piece.getCol() + 2].getColor() == 0));
            }
        }

        else {
            return true;
        }
    }

    String printState() {
        // returns the current game state and all the info
        String result = board.print() + "\nCurrent player: " + current + "\n";
        String victory = checkVictory();
        if (!victory.equals("NO")) {
            result = "GAME OVER. Winner is " + victory + "!\n\n" + result;
        }
        return result;
    }

}

class Board {
    // pieces 0-2 are blue and belongs to the server
    // pieces 3-5 are red and belongs to the client
    private Piece[][] pieces;

    Piece[][] getPieces() {
        return pieces;
    }

    Piece[] getReals(String who) {
        if (who.equals("server")) {
            return servers;
        }
        else {
            return clients;
        }
    }

    private Piece[] servers;
    private Piece[] clients;

    private static final String RESET = "\u001B[0m\u001b[0m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String BOLD = "\u001b[1m";

    Board() {
        pieces = new Piece[7][8];
        for (int i=0; i < 7; i++) {
            for (int j=0; j < 8; j++ ) {
                pieces[i][j] = new Piece(0, 0);
            }
        }
        servers = new Piece[3];
        servers[0] = new Piece(1, 1, 3, 2);
        servers[1] = new Piece(1, 2, 2, 4);
        servers[2] = new Piece(1, 3, 4, 4);
        pieces[3][2] = servers[0];
        pieces[2][4] = servers[1];
        pieces[4][4] = servers[2];

        clients = new Piece[3];
        clients[0] = new Piece(2, 1, 3, 5);
        clients[1] = new Piece(2, 2, 4, 3);
        clients[2] = new Piece(2, 3, 2, 3);
        pieces[3][5] = clients[0];
        pieces[4][3] = clients[1];
        pieces[2][3] = clients[2];

    }

    String checkVictory() {
        boolean server = false;
        boolean client = false;
        if ((servers[0].getCol() == servers[1].getCol() && servers[0].getCol() == servers[2].getCol()) ||
                (servers[0].getRow() == servers[1].getRow() && servers[0].getRow() == servers[2].getRow())) {
            server = true;
        }
        if ((clients[0].getCol() == clients[1].getCol() && clients[0].getCol() == clients[2].getCol()) ||
                (clients[0].getRow() == clients[1].getRow() && clients[0].getRow() == clients[2].getRow())) {
            client = true;
        }
        if (client && server) {
            return "both";
        }
        else if (client) {
            return "client";
        }
        else if (server) {
            return "server";
        }
        else {
            return "NO";
        }
    }

    void makeMove(String player, int number, String direction) {
        // feasibility must be assured before
        Piece piece = getReals(player)[number - 1];

        if (direction.equals("u")) {

            Piece other = pieces[piece.getRow() - 1][piece.getCol()];
            pieces[piece.getRow()][piece.getCol()] = new Piece(0, 0);
            pieces[piece.getRow() - 1][piece.getCol()] = piece;
            piece.setRow(piece.getRow() - 1);
            if (other.getColor() != 0) {
                pieces[piece.getRow() - 1][piece.getCol()] = other;
                other.setRow(other.getRow() - 1);
            }
        }

        else if (direction.equals("d")) {

            Piece other = pieces[piece.getRow() + 1][piece.getCol()];
            pieces[piece.getRow()][piece.getCol()] = new Piece(0, 0);
            pieces[piece.getRow() + 1][piece.getCol()] = piece;
            piece.setRow(piece.getRow() + 1);
            if (other.getColor() != 0) {
                pieces[piece.getRow() + 1][piece.getCol()] = other;
                other.setRow(other.getRow() + 1);
            }
        }

        else if (direction.equals("l")) {

            Piece other = pieces[piece.getRow()][piece.getCol() - 1];
            pieces[piece.getRow()][piece.getCol()] = new Piece(0, 0);
            pieces[piece.getRow()][piece.getCol() - 1] = piece;
            piece.setCol(piece.getCol() - 1);
            if (other.getColor() != 0) {
                pieces[piece.getRow()][piece.getCol() - 1] = other;
                other.setCol(other.getCol() - 1);
            }
        }

        else {

            Piece other = pieces[piece.getRow()][piece.getCol() + 1];
            pieces[piece.getRow()][piece.getCol()] = new Piece(0, 0);
            pieces[piece.getRow()][piece.getCol() + 1] = piece;
            piece.setCol(piece.getCol() + 1);
            if (other.getColor() != 0) {
                pieces[piece.getRow()][piece.getCol() + 1] = other;
                other.setCol(other.getCol() + 1);
            }
        }
    }

    String print() {
        StringBuilder result = new StringBuilder();
        result.append("\n+-------+-------+-------+-------+-------+-------+-------+-------+\n");
        for (int k = 0; k < 7; k++) {
            result.append(printRow(k));
        }
        return result.toString();
    }

    private String printRow(int line) {
        StringBuilder result = new StringBuilder();
        for (int l = 0; l < 3; l++) {
            result.append("|");
            for (int m = 0; m < 8; m++) {
                result.append(printCol(line, m, l));
            }
            result.append("\n");
        }
        result.append("+-------+-------+-------+-------+-------+-------+-------+-------+\n");
        return result.toString();
    }

    private String printCol(int line, int col, int level) {
        if (level == 1) {
            if (pieces[line][col].getColor() == 0) {
                return "       |";
            }
            else if (pieces[line][col].getColor() == 1) {
                return BOLD + BLUE + " | " + pieces[line][col].getNumber() + " | " + RESET + "|";
            }
            else {
                return BOLD + RED + " | " + pieces[line][col].getNumber() + " | " + RESET + "|";
            }
        }
        else {
            if (pieces[line][col].getColor() == 0) {
                return "       |";
            }
            else if (pieces[line][col].getColor() == 1) {
                return BOLD + BLUE + " +---+ " + RESET + "|";
            }
            else {
                return BOLD + RED + " +---+ " + RESET + "|";
            }
        }
    }
}

class Piece {
    // 0 void, 1 blue, 2 red
    private final int color;
    private final int number;
    private int row;
    private int col;

    Piece(int color, int number) {
        this.color = color;
        this.number = number;
        this.row = -1;
        this.col = -1;
    }

    Piece(int color, int number, int row, int col) {
        this.color = color;
        this.number = number;
        this.row = row;
        this.col = col;
    }

    int getColor() {
        return color;
    }

    int getNumber() {
        return number;
    }

    int getRow() {
        return row;
    }

    int getCol() {
        return col;
    }

    void setRow(int row) {
        this.row = row;
    }

    void setCol(int col) {
        this.col = col;
    }
}
