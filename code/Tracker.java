import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Tracker {

    public ArrayList<Player> players;
    boolean playersExist;
    String playerFile = "players.txt";

    public Tracker() {
        players = new ArrayList<>();
        loadPlayers();

    }

    public void addPlayer(Player newPlayer) {
        players.add(newPlayer);
    }

    public void loadPlayers() {

        try (
                FileReader fr = new FileReader(playerFile);
                BufferedReader reader = new BufferedReader(fr)) {
            String line = reader.readLine();
            Scanner sc = null;
            while (line != null) {

                sc = new Scanner(line);

                Player playerRecord = new Player(sc.next());

                playerRecord.succesfulGames = sc.nextInt();
                playerRecord.succesfulGuesses = sc.nextInt();
                playerRecord.totalGames = sc.nextInt();
                playerRecord.totalGuesses = sc.nextInt();
                playerRecord.mappingSeed = sc.nextLong();
                //line = reader.readLine();
                /*if(sc.hasNextLine()) {
                playerRecord.guesses = sc.nextLine();
                }*/
                addPlayer(playerRecord);

                line = reader.readLine();

            }
            if (sc != null) {
            	sc.close();
            }
        } catch (FileNotFoundException e) {
            System.out.println("The specified file could not be found.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Something went wrong with reading or closing the file.");
        }


    }

    public void savePlayers() {
    

        BufferedWriter writer;

        try {	
        PrintWriter clear = new PrintWriter(playerFile);
    	clear.print("");
    	clear.close();
            writer = new BufferedWriter(new FileWriter(playerFile));

            for (int i = 0; i < players.size(); i++) {

                Player currentPlayer = players.get(i);

                writer.write(currentPlayer.name + " " + currentPlayer.succesfulGames +
                        " " + currentPlayer.succesfulGuesses + " " + currentPlayer.totalGames
                        + " " + currentPlayer.totalGuesses + " " + currentPlayer.getGameSeed());
                //writer.newLine();
                //writer.write(currentPlayer.guesses.toString());

                if (i < players.size() - 1) {
                    writer.newLine();
                }

            }

            writer.close();

        } catch (IOException e) {
            System.out.println("Something went wrong with writing to or closing the file.");
        }
    }

    public void printPlayerStats(Player p) {

        System.out.println("Current player name: " + p.getName());
        System.out.println("Number of total games played: " + p.getTotalGames());
        System.out.println("Number of completed games: " + p.getSuccesfulGames());
        System.out.println("Number of correct guesses: " + p.getCorrectGuesses());
        System.out.println("Percentage of complete games: " + p.getAverageGames());
        System.out.println("Percentage of correct guesses: " + p.getAverageGuesses());

    }

    public void printLeaderboard() {

        System.out.println("#		NAME		COMPLETION SUCCESS %");
        ArrayList<Integer> playerPositions = new ArrayList<>();

        for (int i = 0; i < players.size(); i++) {

            playerPositions.add(i);

        }


        for (int i = 0; i < players.size() - 1; i++) {

            for (int n = 1; n < players.size(); n++) {

                if (players.get(n).getAverageGames() > players.get(i).getAverageGames()) {

                    playerPositions.set(i, i + 1);
                    playerPositions.set(n, n - 1);
                }

            }


        }
        int position = 1;
        for (int i = 0; i < players.size(); i++) {
        	for( int j = 0; j < 10; j++) {
	            if (playerPositions.get(i) == j) {
	
	                System.out.println(position + "		" + players.get(i).name + "		" + players.get(i).getAverageGames());
	                position++;
	            }
        	}    
        }
        if(position ==1) {
        	System.out.println("Error. No users stored.");
        }

       
    }

}
