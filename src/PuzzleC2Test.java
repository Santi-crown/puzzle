import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * The test class PuzzleC2Test.
 *
 * @author Andersson David Sánchez Méndez
 * @author Cristian Santiago Pedraza Rodríguez
 * @version 2024
 */
public class PuzzleC2Test
{   
    // Elementary attributes to make tests
    private Puzzle puzzle;
    private char[][] starting;
    private char[][] ending;
    
    /**
     * Sets up the test fixture.
     *
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp()
    {
        starting = new char[][]{
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'},
            {'*', 'r', 'g', 'b', 'y', '*'},
            {'r', 'g', 'b', 'y', '*', 'r'},
            {'g', 'b', 'y', '*', 'r', 'g'},
            {'b', 'y', '*', 'r', 'g', 'b'},
            {'y', '*', 'r', 'g', 'b', 'y'}
        };
        
        ending = new char[][]{
            {'y', '*', 'b', 'r', 'g', 'y'},
            {'*', 'r', 'y', 'g', 'b', '*'},
            {'b', 'g', '*', 'y', 'r', 'b'},
            {'r', 'y', 'g', '*', 'b', 'r'},
            {'g', 'b', 'r', 'y', '*', 'g'},
            {'y', '*', 'b', 'r', 'g', 'y'},
            {'*', 'r', 'y', 'g', 'b', '*'},
            {'b', 'g', '*', 'y', 'r', 'b'},
            {'r', 'y', 'g', '*', 'b', 'r'}
        };

        //puzzleI = new Puzzle(9,6);
        puzzle = new Puzzle(starting,ending);
        //puzzleF = new Puzzle(ending);
        puzzle.makeInvisible();
    }

    /**
     * Test
     *
     * We define different tests to every method in Cycle 2 about should and shouldn't do
     */
    
    // exchange method
    
    @Test 
    public void accordingPSshouldExchangeTilesBoards(){
        puzzle.exchange();
        assertTrue(puzzle.ok(),"Boards have been exchanged. Now, you're editing the board that was the reference board before.");
    }
    
    // makeHole method
    
    @Test 
    public void accordingPSshouldMakeHole(){
        puzzle.makeHole(4,0);
        assertTrue(puzzle.ok(),"You can make a hole because the space is empty, fulfills all the conditions.");
    }
    
    @Test 
    public void accordingPSshouldntMakeHoleOutOfRange(){
        puzzle.makeHole(70,3);
        assertFalse(puzzle.ok(),"You cannnot make a hole because you have exceeded the puzzle space.");
    }
    
    @Test 
    public void accordingPSshouldntMakeHoleNegativePositions(){
        puzzle.makeHole(44,-5);
        assertFalse(puzzle.ok(),"You cannot make a hole in a non-existent tile with negative position.");
    }
    
    @Test 
    public void accordingPSshouldntMakeHoleExistent(){
        puzzle.makeHole(4,0);
        puzzle.makeHole(4,0);
        assertFalse(puzzle.ok(),"This tile already has a hole.");
    }
    
    @Test 
    public void accordingPSshouldntMakeHoleFull(){
        puzzle.makeHole(0,0);
        assertFalse(puzzle.ok(),"You can only make a hole in an empty tile.");
    }
    
    
    // fixedTiles method
    
    @Test 
    public void accordingPSshouldFixedTiles(){
        puzzle.fixedTiles();
        assertTrue(puzzle.ok(),"Returns matrix indicating 0 (as tiles that cannot move) and 1 (as tiles that can move in some direction(or all))");
    }
    
    
    // misplacedTiles method
    
    @Test
    public void accordingPSshouldMisplacedTiles(){
        puzzle.misplacedTiles();
        assertTrue(puzzle.ok(),"Returns the number that counts of misplaced tiles compared with the reference board");
    }
}
