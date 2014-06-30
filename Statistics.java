import java.util.Observable;

/**
 * This class manages the game statistics.
 * @author Christian Kusan
 */
public class Statistics extends Observable {
    
    
    // attributes
    
    /** The number of draws. */
    private int _draws;
    
    /** The number of O triumphs. */
    private int _oWins;
    
    /** The number of X triumphs. */
    private int _xWins;
    
    
    // initializer
    
    /**
     * Initializes an instance.
     */
    public Statistics(){
        _draws = _oWins = _xWins = 0;
    }
    
    
    // getter and setter
    
    /**
     * Gets the number of draws.
     * @return an integer number
     */
    public int getDraws(){
        return _draws;
    }
    
    /**
     * Gets the number of O triumphs.
     * @return an integer number
     */
    public int getOWins(){
        return _oWins;
    }
    
    /**
     * Gets the number of X triumphs.
     * @return an integer number
     */
    public int getXWins(){
        return _xWins;
    }
    
    /**
     * Sets the number of draws.
     * @param draws the number of draws
     */
    public void setDraws(int draws){
        _draws = draws;
        setChanged();
        notifyObservers("d");
    }
    
    /**
     * Sets the number of O triumphs.
     * @param oWins the number of O triumphs
     */
    public void setOWins(int oWins){
        _oWins = oWins;
        setChanged();
        notifyObservers("o");
    }
    
    /**
     * Sets the number of X triumphs.
     * @param xWins the number of X triumphs
     */
    public void setXWins(int xWins){
        _xWins = xWins;
        setChanged();
        notifyObservers("x");
    }
}