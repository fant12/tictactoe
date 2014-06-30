/**
 * Describes the game logic of a tic tac toe game. For this solution 
 * the negamax algorithm is used.
 * @author Christian Kusan
 */
public class TicTacToe {

    /*
    think about.. the board:

        001-002-004
        008-016-032
        064-128-256
    */

    
    // attributes
    
    /** The player O. */
    private int O;
    
    /** The player X. */
    private int X;
    
    
    // initializer
    
    /**
     * Initializes a TicTacToe instance.
     */
    public TicTacToe(){
        TicTacToe.this.initNewGame();
    }
    
    /**
     * Initializes a new game.
     */
    public void initNewGame() {
        X = O = 0;
    }
    
    // getter and setter, is and has
    
    /**
     * Checks whether the game is over or not.
     * @return a number
     * <ul>
     *  <li>-1: player O won</li>
     *  <li>0: game still goes on</li>
     *  <li>1: player X won</li>
     *  <li>2: drawn</li>
     * </ul>
     */
    public int isGameOver() {
        return check(X) ? 1 : check(O) ? -1 : 511 == (511 & (X | O)) ? 2 : 0;
    } 
    

    // methods
    
    /**
     * Converts a bit number to position.
     * @param bit the bit number
     * @return the assigned position field, a number between 0 and 9
     */
    private int bitToPos(int bit) {

        int result = 1;
        while(0 < (bit = bit>>1)) ++result;
    
        return result;
    }
    
    /**
     * Checks whether a player won.
     * @param player the player
     * @return true if player won, otherwise false
     */
    private boolean check(int player) {
        
        // check the winning bits
        
        return (player & 0x007) == 7 ||         // row 1
                (player & 0x038) == 0x038 ||    // row 2
                (player & 0x1C0) == 0x1C0 ||    // row 3
                (player & 0x049) == 0x49 ||     // col 1
                (player & 0x092) == 0x92 ||     // col 2
                (player & 0x124) == 0x124 ||    // col 3
                (player & 0x111) == 0x111 ||    // diagonal left
                (player & 0x054) == 0x54;       // diagonal right
    }
    
    /**
     * Clears the board on specified a field position.
     * @param pos the position
     */
    private void clear(int pos) {
        O = O & ~pos;
        X = X & ~pos; //~ not
    }
    
    /**
     * Checks whether game is over or not.
     * @return a number that defines the game state
     * <ul>
     *  <li>0: game still goes on</li>
     *  <li>512: O won</li>
     *  <li>1024: drawn</li>
     *  <li>2048: X won</li>
     * </ul>
     */
    private int gameOver() {
        return check(X) ? 2048 : check(O) ? 512 : 511 == (511 & (X | O)) ? 1024 : 0;
    }

    /**
     * Generates a move.
     * @param player the player
     * @return the field position for move
     */
    public int generateMove(int player) {
        return bitToPos(511 & (negamax(player)));
    }
    
    /**
     * A game move.
     * @param pos the position between 1 and 9
     * @param player the player (-1 or 1)
     * @return true if successful, otherwise false
     */
    public boolean move(int pos, int player) {
    
        int p = posToBit(pos);
        
        if(0 != p && (-1 == player || 1 == player) && 0 == ((X | O) & p)) {
            set(p * player);
            return true;
        } 
        return false; 
    }

    /**
     * The negamax algorithm calculates the best move.
     * @param player the player
     * @return a number that defines the best value
     */
    private int negamax(int player) {

        /*
        best:   score            move
                2048 1024  512 - 256 128 64  32  16   8   4   2   1
                  0    0    0  -  0   0   0   0   0   0   0   0   0
        win:      X    0    O
        */
        
        int end = gameOver();
        
        if(0 != end)
            return end;
        
        // worst case for X: bestValue = 512 (O wins)
        // worst case for O: bestValue = 2048 (X wins)
        int bestValue = (1 == player) ? 512 : 2048;
        
        // CLEAR_MOVE & bestValue to get the score bits (clear move bit)
        final int CLEAR_MOVE = 0xfffffe00; 
        	
        for(int i = 1; 256 >= i; i = i << 1) {
            
            int move = (~(X | O) & i);
        
            if(0 != move) {
                set(player * move);
                int s = negamax(-player);
                bestValue = player * (CLEAR_MOVE & s) > player * (CLEAR_MOVE & bestValue) 
                        ? ((CLEAR_MOVE & s) | move)
                        : bestValue;
                    clear(move);
            }
        }
        
        return bestValue;
    }
    
    /**
     * Converts a position to bit number.
     * @param pos the position
     * @return a bit number
     * <ul>
     *  <li>1: row 1, col 1</li>
     *  <li>2: row 1, col 2</li>
     *  <li>4: row 1, col 3</li>
     *  <li>8: row 2, col 1</li>
     *  <li>16: row 2, col 2</li>
     *  <li>32: row 2, col 3</li>
     *  <li>64: row 3, col 1</li>
     *  <li>128: row 3, col 2</li>
     *  <li>256: row 3, col 3</li>
     * </ul>
     */
    private int posToBit(int pos) {
        return 1 <= pos && 9 >= pos ? 1<<(pos-1) : 0;
    }
    
    /**
     * Sets a field.
     * @param pos defines move and field position
     * <ul>
     *  <li>pos &lt; 0: O move</li>
     *  <li>pos &gt; 0: X move </li>
     * </ul>
     */
    private void set(int pos) {
        X = X | pos & -((pos >> 31) + 1) & ~O;
        O = O | -pos & (pos >> 31) & ~X;
    }

}    