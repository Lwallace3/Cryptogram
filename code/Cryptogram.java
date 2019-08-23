

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;


public class Cryptogram {
    static final int asciiNum = 97;
    static final int alphabetSize = 26;
    //static final int zero = 0;
    static final int crypSize = 40;
    char[][] mapping = new char[2][26];
    char[][] cryptogram = new char[3][40];
    long mappingSeed;

    public Cryptogram() {
        mappingSeed = 0L;
        int i = 0;
        for (char c = 'a'; c <= 'z'; c++) {
            mapping[0][i] = c;
            mapping[1][i] = c;
            i++;
        }
    }

    //generates the mapping for the cryptogram for either a number or a letter depending on the value of type
    public void createMapping() {
        boolean selfMapping = true;
        while (selfMapping) {
            Random r = new Random();
            if (mappingSeed == 0L) {
                mappingSeed = r.nextLong();
            } else {
                r.setSeed(mappingSeed);
            }
            for (int i = alphabetSize - 1; i >= 0; i--) {
                int index = r.nextInt(i + 1);
                char j = mapping[1][index];
                mapping[1][index] = mapping[1][i];
                mapping[1][i] = j;
            }
            selfMapping = false;
            for (int i = alphabetSize - 1; i >= 0; i--) {
                if (mapping[0][i] == mapping[1][i]) {
                    selfMapping = true;
                }
            }
        }
    }

    //returns the mapping to be used with the cryptogram. There are 26 entries for each letter, one column for the
    //original letter, one for the mapped letter and one for the current guess
    public char[][] getMapping() {
        return mapping;
    }

    //selects a cryptogram from all of the possible cryptograms
    public boolean selectCryptogram() {
        ArrayList<String> cryp = new ArrayList<String>();
        cryp.add("aaaaa aaa aaaaa aa".toLowerCase());
        cryp.add("This is a test".toLowerCase());
        cryp.add("yet another example".toLowerCase());
        if (cryp.size() != 0) {
            Random rand = new Random();
            rand.setSeed(mappingSeed);
            int crypNo = rand.nextInt(cryp.size());
            cryptogram[0] = cryp.get(crypNo).toCharArray();
            cryptogram[1] = cryp.get(crypNo).toCharArray();
            return true;
        } else {
            System.out.println("Error. No cryptogram phrases available");
        }
        return false;
    }

    //returns the encrypted cryptogram puzzle
    public char[][] getCryptogram() {
        for (int i = 0; i < cryptogram[0].length; i++) {
            char c = cryptogram[0][i];
            for (int j = 0; j < alphabetSize; j++) {
                if (mapping[0][j] == c) {
                    cryptogram[1][i] = mapping[1][j];
                }
            }
        }
        return cryptogram;
    }

    public void initialiseMapping() {
        for (int i = 0; i < cryptogram[0].length; i++) {
            char c = cryptogram[0][i];
            if (c != ' ') {
                cryptogram[2][i] = '-';
            } else {
                cryptogram[2][i] = ' ';
            }
        }
    }

    //returns the char array with the correct solutions to the mapping
    public char[] getSolution() {
        return cryptogram[0];
    }


    public char[] getEncrypted() {
        return cryptogram[1];
    }

    //getHint():char[3]
    //returns a single letter, and its mapping
    //setSolved(Cryptogram: String)
    //sets the status of the cryptogram in the text file to be solved
    public boolean checkSolved(Cryptogram puzzle) {
        boolean quit = true;
        boolean allEntered = true;

        if (puzzle.cryptogram[0][0] == '&') {

            quit = false;

        }

        for (int i = 0; i < puzzle.cryptogram[0].length; i++) {
            if (puzzle.cryptogram[0][i] != puzzle.cryptogram[2][i]) {
                quit = false;
            }
            if (puzzle.cryptogram[2][i] == '-') {
                allEntered = false;
            }
        }
        if (quit) {
            System.out.println("Strings Match!");
        } else if (allEntered) {
            System.out.println("Incorrect! Try changing some Mappings...");
        }
        return quit;
    }

    //checks whether the users guess is correct
    public Double[] getFrequency() {
        int[] frequencyCount = new int[alphabetSize];
        Double[] frequency = new Double[alphabetSize];
        int letterCount = 0;
        for (int i = 0; i < cryptogram[1].length; i++) {
            if (cryptogram[1][i] != ' ') {
                frequencyCount[cryptogram[1][i] - asciiNum]++;
                letterCount++;
            }
        }
        for(int i =0; i<alphabetSize;i++) {
        	Double count = Double.valueOf(frequencyCount[i]);
        	Double letCount = Double.valueOf(letterCount);
        	frequency[i] = ((count/letCount)*100);
        }
        return frequency;
    }
    public long getMappingSeed() {
        return mappingSeed;
    }

    public void setMappingSeed(long Seed) {
        mappingSeed = Seed;
    }
}
