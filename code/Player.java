

public class Player {
    public String name;
    public int succesfulGames;
    public int totalGames;
    public int succesfulGuesses;
    public int totalGuesses;
    public long mappingSeed;
    public String guesses;

    public Player(String usernameIn) {
        name = usernameIn;
        totalGames = 0;
        succesfulGames = 0;
        succesfulGuesses = 0;
        totalGuesses = 0;
        mappingSeed = 0L;
        guesses = null;
    }

    public String getName() {
        return this.name;
    }

    public void addGameStat(boolean gameComplete) {
        if (gameComplete) {
            this.succesfulGames++;
        }
        this.totalGames++;
    }

    public int getSuccesfulGames() {
        return this.succesfulGames;
    }

    public Double getAverageGames() {
        Double average;
        Double total = Double.valueOf(this.totalGames);
        Double succesful = Double.valueOf(this.succesfulGames);        
        if (this.totalGames > 0) {
            average =  (succesful / total) * 100.0;
        } else {
            average = 0.0;
        }
        return average;
    }

    public int getTotalGames() {
        return this.totalGames;
    }

    public void addGuess(boolean correctGuess) {
        if (correctGuess) {
            this.succesfulGuesses++;
        }
        this.totalGuesses++;
    }

    public int getCorrectGuesses() {
        return this.succesfulGuesses;
    }

    public int getTotalGuesses() {
        return this.totalGuesses;
    }

    public Double getAverageGuesses() {
        Double average;
        Double total = Double.valueOf(this.totalGuesses);
        Double succesful = Double.valueOf(this.succesfulGuesses);
        if (this.totalGuesses > 0) {
            average = (succesful / total) * 100.0;
        } else {
            average = 0.0;
        }
        return average;
    }
    public long getGameSeed() {
        return this.mappingSeed;
    }

    public char[] getPrevGameGuesses() {
        return this.guesses.toCharArray();
    }
}
