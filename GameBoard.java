import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Describes the game board.
 * @author Christian Kusan
 */
public class GameBoard extends JFrame implements Observer {

    /** The Controller. */
    final private Controller _CONTROL;
    
    /** The board field. */
    final private JLabel[] _FIELDS;
    
    /** The tic tac toe game or the model. */
    final private TicTacToe _GAME;
    
    /** The model for the statistics. */
    final private Statistics _STATISTIC;
    
    /** The labels for the statistic. */
    private JLabel[] _statLabels;
    
    /**
     * Describes the controller instance between game board view and model. The 
     * controller handles all action or mouse events on the given user interface.
     */
    private class Controller extends MouseAdapter implements ActionListener {
    
        /**
         * Handles all thrown button events on the ui.
         * @param ae the catched action event
         */
        @Override
        public void actionPerformed(ActionEvent ae) {

            String sourceName = ((Component) ae.getSource()).getName();
            
            switch(sourceName){
                case "newGameBtn": startNewGame();
                    break;
                case "resetBtn": setCounters(0);
                    break;
                case "compVsCompBtn": playCompVsComp();
            }
        }
        
        /**
         * Handles the mouse click events on the game board fields.
         * @param me the catched mouse event
         */
        @Override
        public void mouseReleased(MouseEvent me) {
        
            if(me.getSource() instanceof JLabel){
                
                JLabel sender = (JLabel) me.getSource();
                int gameOver = _GAME.isGameOver();
            
                if(0 == gameOver && _GAME.move(Integer.valueOf(sender.getName()), 1)) {
                    sender.setForeground(Color.GREEN);
                    sender.setText("X");
                    computerMove(-1, 0, Color.RED);
                    
                    if (0 != (gameOver = _GAME.isGameOver()))
                        setCounters(gameOver);
                }
            }
        }
    }
    
    
    // static
    
    /**
     * Starts the application.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GameBoard game = new GameBoard();
    }

    
    // initializer
    
    /**
     * Initializes the game board view.
     */
    public GameBoard() {
        
        _GAME = new TicTacToe();
        _FIELDS = new JLabel[9];
        
        _STATISTIC = new Statistics();
        _STATISTIC.addObserver(GameBoard.this);
        
        _CONTROL = new Controller();
        
        init();
        
        pack();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 500);
        setTitle("TicTacToe");
        setVisible(true);
    }
    
    /**
     * Initializes the view layout and his components.
     */
    private void init(){
            
        setLayout(new BorderLayout());
        
        JLabel l1 = new JLabel("a");
        JLabel l2 = new JLabel("b");
        JLabel l3 = new JLabel("c");
        l1.setBackground(Color.red);
        l2.setBackground(Color.yellow);
        l3.setBackground(Color.blue);
        
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(3, 3));
        
        for(int i = 0; _FIELDS.length > i; ++i){
            
            _FIELDS[i] = new JLabel();
            _FIELDS[i].addMouseListener(_CONTROL);
            _FIELDS[i].setBackground(new Color(0, 0, 0));
            _FIELDS[i].setFont(new Font("Tahoma", Font.BOLD, 50));
            _FIELDS[i].setForeground(Color.GREEN);
            _FIELDS[i].setHorizontalAlignment(JLabel.CENTER);
            _FIELDS[i].setName(String.valueOf((1+i)));
            _FIELDS[i].setOpaque(true);
            
            boardPanel.add(_FIELDS[i]);
        }
        
        JPanel statPanel = new JPanel();
        statPanel.setLayout(new GridLayout(4, 2));
        
        JButton newGameBtn = new JButton("New game");
        newGameBtn.addActionListener(_CONTROL);
        newGameBtn.setName("newGameBtn");
        
        String[] statLabelTexts = {"X wins:", "O wins:", "Draws:"};
        _statLabels = new JLabel[3];
        
        for(int i = 0; statLabelTexts.length > i; ++i){
            
            JLabel label = new JLabel(statLabelTexts[i]);
            label.setFont(new Font("Tahoma", Font.BOLD, 17));
            
            _statLabels[i] = new JLabel("0");
            _statLabels[i].setFont(new Font("Tahoma", Font.BOLD, 17));
            
            statPanel.add(label);
            statPanel.add(_statLabels[i]);
        }

        JButton resetBtn = new JButton("Reset");
        resetBtn.addActionListener(_CONTROL);
        resetBtn.setName("resetBtn");
        statPanel.add(resetBtn);

        JButton compVsCompBtn = new JButton("Computer vs Computer");
        compVsCompBtn.addActionListener(_CONTROL);
        compVsCompBtn.setName("compVsCompBtn");
        statPanel.add(compVsCompBtn);
        
        add(statPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(newGameBtn, BorderLayout.SOUTH);
    }

    
    // methods
    
    /**
     * Generates a computer move
     * @param player the player
     * @param move the move
     * @param color the mark color
     */
    private void computerMove(int player, int move, Color color) {
        
        if(0 == move) 
            move = (0 == _GAME.isGameOver()) ? _GAME.generateMove(player) : 0;
        
        String playerSign = (1 == player) ? "X" : "O";

        for(JLabel label : _FIELDS)
            if(label.getName().equals(String.valueOf(move))){
               label.setForeground(color);
               label.setText(playerSign);
               _GAME.move(move, player);
               break;
            }
    }
    
    /**
     * Starts a game between computer vs computer.
     */
    private void playCompVsComp() {
        
        int p = 1;
        int gameOver = 0;
            
        startNewGame();
        
        Random rnd = new Random();
        int firstMove = 1 + rnd.nextInt(9);
            
        computerMove(1, firstMove, Color.GREEN);
        
        while(0 == gameOver){
            
            if(0 == (p++ % 2))
                computerMove(1, 0, Color.GREEN);
            else
                computerMove(-1, 0, Color.RED);
            
            gameOver = _GAME.isGameOver();
        }
            
        setCounters(gameOver);
    }

    /**
     * Sets the counter.
     * @param num the counter value
     */
    public void setCounters(int num) {
        
        switch(num) {
            case 0: 
                _STATISTIC.setDraws(num);
                _STATISTIC.setOWins(num);
                _STATISTIC.setXWins(num);
                break;
            case -1:
                _STATISTIC.setOWins(1 + _STATISTIC.getOWins());
                break;
            case 1:
                _STATISTIC.setXWins(1 + _STATISTIC.getXWins());
                break;
            case 2:
                _STATISTIC.setDraws(1 + _STATISTIC.getDraws());
                break;
        }
    }

    /**
     * Starts a new game.
     */
    private void startNewGame() {
        
        for(JLabel label : _FIELDS)
            label.setText("");
            
        _GAME.initNewGame();
    }
    
    /**
     * Updates the game board view if the subject changed.
     * @param subject the observed subject
     * @param arg the sent argument
     */
    @Override
    public void update(Observable subject, Object arg){
    
        if(null != subject && subject instanceof Statistics)
            if(null != arg && arg instanceof String)
                switch(arg.toString()){
                    case "d": _statLabels[2].setText(String.valueOf(_STATISTIC.getDraws()));
                        break;
                    case "o": _statLabels[1].setText(String.valueOf(_STATISTIC.getOWins()));
                        break;
                    case "x": _statLabels[0].setText(String.valueOf(_STATISTIC.getXWins()));
                }
    }
}