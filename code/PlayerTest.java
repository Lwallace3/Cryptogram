
import static org.junit.Assert.*;

import org.junit.Test;

public class PlayerTest {

	@Test
	public void testAddGuessTrue() {
		Player testPlayer = new Player("test");
		assertEquals(testPlayer.getTotalGames(),0,0.0);
		assertEquals(testPlayer.getTotalGuesses(),0,0.0);
		assertEquals(testPlayer.getSuccesfulGames(),0,0.0);
		assertEquals(testPlayer.getCorrectGuesses(),0,0.0);
		assertEquals(testPlayer.getAverageGames(),0,0.0);
		assertEquals(testPlayer.getAverageGuesses(),0,0.0);
		
		testPlayer.addGuess(true);
		assertEquals(testPlayer.getTotalGuesses(),1,0.0);
		assertEquals(testPlayer.getCorrectGuesses(),1,0.0);
	}
	@Test
	public void testAddGuessFalse() {
		Player testPlayer = new Player("test");
		testPlayer.addGuess(false);
		assertEquals(testPlayer.getTotalGuesses(),1,0.0);
		assertEquals(testPlayer.getCorrectGuesses(),0,0.0);
	}
	@Test
	public void testAverageGuess() {
		Player testPlayer = new Player("test");
		for(int i = 0; i<50;i++) {
			testPlayer.addGuess(false);
		}
		for(int i =0;i<50;i++) {
			testPlayer.addGuess(true);
		}
		assertEquals(testPlayer.getAverageGuesses(),50.0,0.0);
	}
	@Test
	public void testAddGameTrue() {
		Player testPlayer = new Player("test");
		testPlayer.addGameStat(true);
		assertEquals(testPlayer.getTotalGames(),1,0.0);
		assertEquals(testPlayer.getSuccesfulGames(),1,0.0);
	}	
	@Test
	public void testAddGameFalse() {
		Player testPlayer = new Player("test");
		testPlayer.addGameStat(false);
		assertEquals(testPlayer.getTotalGames(),1,0.0);
		assertEquals(testPlayer.getSuccesfulGames(),0,0.0);		
	}
	@Test
	public void testAverageGames() {
		Player testPlayer = new Player("test");
		for(int i = 0; i<50;i++) {
			testPlayer.addGameStat(false);
		}
		for(int i =0;i<50;i++) {
			testPlayer.addGameStat(true);
		}
		assertEquals(testPlayer.getAverageGames(),50.0,0.0);
	}

}
