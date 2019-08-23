import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    static Tracker playerTracker = new Tracker();
    static final int asciiNum = 97;
    static final int alphabetSize = 26;
    static final int zero = 0;
    static final int crypSize = 40;
    static String path = "H:\\cis\\windows\\eclipse-workspace\\CryptogramAssignment\\src\\UserCryptograms";

    public Application() {

    }

    public static Player login() {
        Player currentPlayer = new Player("init");
        boolean accepted = false;
        while (!accepted) {
            System.out.println("Are you a new User? (yes/no)");
            String answer = getInput();
            if (answer.toLowerCase().equals("yes")) {
                accepted = true;
                currentPlayer = createPlayer();
            } else if (answer.toLowerCase().equals("no")) {
                accepted = true;
                System.out.println("What is your username?");
                answer = getInput();
                currentPlayer = setCurrentPlayer(answer,playerTracker);
            } else {
                System.out.println("Please enter 'yes' or 'no'. ");
            }
        }
        return currentPlayer;
    }

    public static void game(Player currentPlayer) {
        char[][] mapping = new char[2][alphabetSize];
        char[][] cryptogram = new char[3][crypSize];
        ArrayList<String> guesses = new ArrayList<String>();
        Cryptogram puzzle = new Cryptogram();
        boolean type = false;
        boolean started = false;
        boolean quit = false;
        puzzle.cryptogram[0][0] = '&';

        while (quit == false) {
            System.out.println();
            System.out.println("Select a function, enter 'help' to view all available commands.");
            String input = getInput();

            if (input.toLowerCase().equals("help")) {
                printHelp();
            } else if (input.toLowerCase().equals("guess") && started) {
                if (!type) {
                    System.out.print("You wish to change letter.. ");
                } else if (type) {
                    System.out.print("You wish to change number.. ");
                }
                boolean accepted = true;
                String mapLetter = getInput();
                char mapLetterActual = ' ';
                if (type) {
                    convertNumToStr(mapLetter, mapLetterActual);
                }
                boolean inputAcc = false;
                accepted = checkMapping(type,inputAcc,mapLetter,guesses);
                if (accepted) {
                    System.out.print("with the letter.. ");
                    String guessLetter = getInput();
                    System.out.println();
                    inputAcc = false;
                    accepted = checkGuess(inputAcc,guessLetter,guesses);
                    if (accepted) {
                        guesses = guess(puzzle, guesses, mapLetter, guessLetter, currentPlayer);
                        int guessLoc = guesses.get(guesses.size() - 1).charAt(1) - asciiNum;
                        mapping = puzzle.getMapping();
                        if (mapping[0][guessLoc] == guesses.get(guesses.size() - 1).charAt(1)) {
                            currentPlayer.addGuess(true);
                            //System.out.println("[FOR TESTING ONLY] Correct guess, number of correct guesses for user '" + currentPlayer.name + "' is " + currentPlayer.succesfulGuesses);
                        } else {
                            currentPlayer.addGuess(false);
                            //System.out.println("[FOR TESTING ONLY] Incorrect guess, number of correct guesses for user '" + currentPlayer.name + "' is " + currentPlayer.succesfulGuesses + " The correct guess was " + mapping[0][guessLoc]);
                        }
                    }
                }
                printMapping(puzzle, type);
            } else if (input.toLowerCase().equals("undo") && started) {
                guesses = undo(puzzle, guesses, type, currentPlayer);
            } else if (input.toLowerCase().equals("exit")) {
                if(started) {
            		currentPlayer.addGameStat(false);
            	}
                quit = true;
                break;
            } else if (input.toLowerCase().equals("load")) {
                loadCryptogram(puzzle, cryptogram, currentPlayer);
            } else if (input.toLowerCase().equals("start")) {
                guesses.clear();
                type = getType();
                puzzle.createMapping();
                currentPlayer.mappingSeed = puzzle.mappingSeed;
                started = puzzle.selectCryptogram();
                cryptogram = puzzle.getCryptogram();
                printCryptogram(type, cryptogram);
                puzzle.initialiseMapping();
            } else if (input.toLowerCase().equals("print mapping") && started) {
                printMapping(puzzle, type);
            } else if (input.toLowerCase().equals("frequency") && started) {
                printFrequency(puzzle,type);
            } else if (input.toLowerCase().equals("mystats")) {
                playerTracker.printPlayerStats(currentPlayer);
            } else if (input.toLowerCase().equals("leaderboard")) {
                playerTracker.printLeaderboard();
            } else if(input.toLowerCase().equals("correct guess")) {
            	currentPlayer.addGuess(true);
            } else if(input.toLowerCase().equals("solution")) {
            	System.out.println(puzzle.getSolution());
            	started = false;
            	currentPlayer.addGameStat(false);
            } else if(input.toLowerCase().equals("hint")){
            	String mapLetter;
            	String guessLetter;
            	int found = 0;
            	for(int i = 0; i< cryptogram[2].length; i++) {
            		if( cryptogram[2][i] == '-') {
            			found = 1;
            			mapLetter = String.valueOf(cryptogram[1][i]);
            			guessLetter = String.valueOf(cryptogram[0][i]);
            			guesses = guess(puzzle, guesses, mapLetter, guessLetter, currentPlayer);
                        int guessLoc = guesses.get(guesses.size() - 1).charAt(1) - asciiNum;
                        mapping = puzzle.getMapping();
                        i = cryptogram[2].length;
                        printMapping(puzzle, type);
            		}
            	}
            	if(found == 0) {
            		for(int i = 0; i< cryptogram[2].length; i++) {
                		if( cryptogram[2][i] != cryptogram[0][i] ) {
                			found = 1;
                			mapLetter = String.valueOf(cryptogram[1][i]);
                			guessLetter = String.valueOf(cryptogram[0][i]);
                			guesses = guess(puzzle, guesses, mapLetter, guessLetter, currentPlayer);
                            int guessLoc = guesses.get(guesses.size() - 1).charAt(1) - asciiNum;
                            System.out.println("Hint has corrected an incorrect guess.");
                            mapping = puzzle.getMapping();
                            i = cryptogram[2].length;
                            printMapping(puzzle, type);
                		}
                	}
            	}
            } else {
                System.out.println("This command is not recognised");
            }
            if (started) {
                started = !puzzle.checkSolved(puzzle);
                if(!started) {
                	currentPlayer.addGameStat(true);
                }
            }
        }
    }

    public static void main(String[] args) {
        Player currentPlayer = login();
        game(currentPlayer);
        playerTracker.savePlayers();
    }


    public static int loadCryptogram(Cryptogram puzzle, char[][] cryptogram, Player currentPlayer) {
        if (currentPlayer.getGameSeed() == 0L) {
            System.out.println("There is no saved cryptogram under your user name");
            return 0;
        } else {
            puzzle.setMappingSeed(currentPlayer.getGameSeed());    //sets the mapping seed
            //cryptogram[2] = currentPlayer.getPrevGameGuesses(); //loads in the player guesses
            System.out.println("Success! your previous game has been loaded! good luck!");
            return 1;
        }
    }

    public void loadPlayers() {

        playerTracker.loadPlayers();

        for (int i = 0; i < playerTracker.players.size(); i++) {

            System.out.println(playerTracker.players.get(i).name + " " + playerTracker.players.get(i).succesfulGames + " " + playerTracker.players.get(i).succesfulGuesses + " " + playerTracker.players.get(i).totalGames + " " + playerTracker.players.get(i).totalGuesses);

        }
    }

    public void savePlayers() {

        playerTracker.savePlayers();

    }

    public static Player setCurrentPlayer(String username, Tracker playerTracker) {
        for(int i = 0;i<playerTracker.players.size();i++){
            if(playerTracker.players.get(i).getName().equals(username)){
                return playerTracker.players.get(i);
            }
        }
        Player player = new Player(username);
        playerTracker.addPlayer(player);
        return player;
    }

    public static Player createPlayer() {
        System.out.println("Please enter a username");
        String username = getInput();
        Player currentPlayer = new Player(username.toLowerCase());
        playerTracker.addPlayer(currentPlayer);
        return currentPlayer;
    }


    public static String convertNumToStr(String mapLetter, char mapLetterActual){
        if (mapLetter.length() == 2) {
            if (mapLetter.charAt(1) > 47 && mapLetter.charAt(1) < 58) {
                if (mapLetter.charAt(0) == '1') {
                    mapLetterActual = (char) (mapLetter.charAt(0) + mapLetter.charAt(1) + 9);
                } else if (mapLetter.charAt(0) == '2') {
                    mapLetterActual = (char) (mapLetter.charAt(0) + mapLetter.charAt(1) + 18);
                }
            }
        } else {
            mapLetterActual = (char) (mapLetter.charAt(0) + 48);
        }
        mapLetter = String.valueOf(mapLetterActual);
        return mapLetter;
    }

    public static void printFrequency(Cryptogram puzzle,boolean type){
        Double[] frequency = puzzle.getFrequency();
        char[][] mapping = new char[2][alphabetSize];
        mapping = puzzle.getMapping();
        System.out.println("The frequency of the letters is as follows...");
        if (!type) {
            for (int i = 0; i < alphabetSize; i++) {
                if (frequency[i].compareTo(0.0) > 0) {
                    System.out.println(mapping[0][i] + " : " + Math.round(frequency[i]) + " %");
                }
            }
        } else if (type) {
            for (int i = 0; i < alphabetSize; i++) {
                if (frequency[i].compareTo(0.0) > 0) {
                    System.out.println((int) mapping[0][i] - asciiNum + " : " + Math.round(frequency[i]) + " %");
                }
            }
        }
    }
    public static void printMapping(Cryptogram puzzle, boolean type) {
        char[][] mapping = puzzle.getMapping();
        char[][] cryptogram = puzzle.getCryptogram();
        System.out.println();

        /*System.out.print("The letters are : ");
        for (int i = 0; i < alphabetSize; i++) {
            System.out.print(mapping[0][i] + " ");
        }

        System.out.println();
        System.out.print("The mapping is  : ");
        for (int i = 0; i < alphabetSize; i++) {
            if (!type) {
                System.out.print((mapping[1][i]) + " ");
            } else if (type) {
                System.out.print((mapping[1][i] - asciiNum + 1) + " ");
            }
        }*/

        System.out.println();
        System.out.print("            The guesses are : ");
        for (int i = 0; i < cryptogram[0].length; i++) {
            System.out.print(cryptogram[2][i]);
        }
/*
                System.out.println();
                System.out.println();
                System.out.print("The given cryptogram is : ");
                for (int i = 0; i < cryptogram[0].length; i++) {
                        System.out.print(cryptogram[0][i]);
                }
*/
        System.out.println();
        System.out.print("The encrypted cryptogram is : ");
        for (int i = 0; i < cryptogram[0].length; i++) {
            System.out.print(cryptogram[1][i]);
        }
        System.out.println("");

    }

    public static String getInput() {
        Scanner scan = new Scanner(System.in);


        String input = scan.nextLine();

        return input;

    }

    public static ArrayList<String> guess(Cryptogram puzzle, ArrayList<String> guesses, String mapLetter, String guessLetter, Player currentPlayer) {


        String element = mapLetter + guessLetter;
        guesses.add(element);
        Double[] frequency = puzzle.getFrequency();        char map = mapLetter.charAt(0);
        //System.out.println("map: " + map);
        char guess = guessLetter.charAt(0);
        //System.out.println("map: "+map);
        //System.out.println("guess: "+guess);
        char[][] cryptogram = puzzle.getCryptogram();

        if (frequency[map - asciiNum] == 0) {
            System.out.println("Sorry that mapping does not feature in this cryptogram! Try something else");
        } else {
            for (int i = 0; i < cryptogram[1].length; i++) {
                if (cryptogram[1][i] == map) {
                    cryptogram[2][i] = guess;
                } else if (cryptogram[1][i] == ' ') {
                    cryptogram[2][i] = ' ';
                }
            }
            currentPlayer.guesses = new String(cryptogram[2]);
        }
        return guesses;
    }

    public static ArrayList<String> undo(Cryptogram puzzle, ArrayList<String> guesses, boolean type, Player currentPlayer) {
        puzzle.getCryptogram();
        int i = guesses.size();
        char mapLetterActual = ' ';
        boolean found = false;
        if (!type) {
            System.out.println("Which letter would you like to remove from the Mapping?");
        } else if (type) {
            System.out.println("Which Number would you like to remove from the Mapping?");
        }
        String map = getInput();
        if (type) {
            if (map.length() == 2) {
                if (map.charAt(1) > 47 && map.charAt(1) < 58) {
                    if (map.charAt(0) == '1') {
                        mapLetterActual = (char) (map.charAt(0) + map.charAt(1) + 9);
                    } else if (map.charAt(0) == '2') {
                        mapLetterActual = (char) (map.charAt(0) + map.charAt(1) + 18);
                    }
                }
            } else {
                mapLetterActual = (char) (map.charAt(0) + 48);
            }
            map = String.valueOf(mapLetterActual);
        }

        //char map = guesses.get(i-1).charAt(0);
        //String mapIn = String.valueOf(map);
        //char guess = guesses.get(i).charAt(1);
        String guessIn = "-";
        //String.valueOf(guess);

        guesses = guess(puzzle, guesses, map, guessIn, currentPlayer);
        guesses.remove(i);
        for (i = 0; i < guesses.size(); i++) {
            if (guesses.get(i).charAt(0) == map.charAt(0)) {
                guesses.remove(i);
                found = true;
            }
        }
        if (!found) {
            if (!type) {
                System.out.println("This Letter has not been mapped to yet");
            } else if (type) {
                System.out.println("This Number has not been mapped to yet");
            }
        }
        return guesses;
    }

    public static void printHelp(){
        System.out.println("(1) Welcome to our Cryptogram, the following options are available to you..");
        System.out.println("(2) Enter 'undo' to undo a guess thats been made");
        System.out.println("(3) Enter 'guess' to make a guess on the letter");
        System.out.println("(4) Enter 'print mapping' to view the current progression");
        System.out.println("(5) Enter 'frequency' to view the letter frequency");
        System.out.println("(6) Enter 'exit' to exit the program");
        System.out.println("(7) Enter 'start' to start a new game");
        System.out.println("(8) Enter 'mystats' to view your individual player stats.");
        System.out.println("(9) Enter 'leaderboard' to view the top 10 players.");
        System.out.println("(10) Enter 'solution' to view the solution to the Cryptogram and end the game.");
        System.out.println("(11) Enter 'hint' to get a hint to help you complete the puzzle.");
    }
    public static void printCryptogram(boolean type, char[][] cryptogram){
        System.out.println("Your given Cryptogram is...");
        for (int i = 0; i < cryptogram[1].length; i++) {
            System.out.print(cryptogram[1][i]);
        }
        System.out.println();
        if (type) {
            for (int i = 0; i < cryptogram[1].length; i++) {
                if (type && cryptogram[1][i] != ' ') {
                    System.out.print((cryptogram[1][i] - asciiNum + 1) + ",");
                } else {
                    System.out.print(" ,");
                }
            }
        }
    }
    public static boolean getType(){
        boolean numLet = false;
        boolean type = false;
        System.out.println("Would you like to map to letters ('l') or numbers ('n')");
        while (!numLet) {
            String inputType = getInput();
            if (inputType.toLowerCase().equals("l")) {
                type = false;
                numLet = true;
            } else if (inputType.toLowerCase().equals("n")) {
                type = true;
                numLet = true;
            } else {
                System.out.println("Please specify either 'l' for letter or 'n' for number.");
            }
        }
        return type;
    }
    public static boolean validateLetter(boolean inputAcc){
        boolean accepted = false;
        while (!inputAcc) {
            String answer = getInput();
            if (answer.toLowerCase().equals("yes")) {
                accepted = true;
                inputAcc = true;
            } else if (answer.toLowerCase().equals("no")) {
                accepted = false;
                inputAcc = true;
            } else {
                System.out.println("Please enter either 'yes' or 'no'");
            }
        }
        return accepted;
    }
    public static boolean checkMapping(boolean type, boolean inputAcc,String mapLetter,ArrayList<String> guesses){
        boolean accepted = true;
        for (int i = asciiNum; i < alphabetSize + asciiNum; i++) {
            char extra = (char) i;
            String checkLetter = mapLetter + extra;
            if (guesses.contains(checkLetter) && !inputAcc) {
                if (!type) {
                    System.out.println("Error, you have already tried to guess that letter. do you want to change the mapping?");
                } else if (type) {
                    System.out.println("Error, you have already tried to guess that number. do you want to change the mapping?");
                }
                accepted = validateLetter(inputAcc);
            }
        }
        return accepted;
    }
    public static boolean checkGuess(boolean inputAcc,String guessLetter,ArrayList<String> guesses){
        boolean accepted = true;
        for (int i = asciiNum; i < asciiNum + alphabetSize; i++) {
            char extra = (char) i;
            String checkLetter = extra + guessLetter;
            if (guesses.contains(checkLetter) && !inputAcc) {
                System.out.println("Error, you have already guessed that letter. do you want to change the mapping?");
                accepted = validateLetter(inputAcc);
            }
        }
        return accepted;
    }
}
