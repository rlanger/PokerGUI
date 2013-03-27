import java.awt.*;import java.awt.event.*;import javax.swing.*;// This class contains the code for creating and managing the main window// in which the game is played out.public class MainPokerWindow extends JFrame implements ActionListener {	public static final int WIDTH = 600;	public static final int HEIGHT = 650;	public static final String DEFAULT_OPP = new String ("Bub");		// Opponent contains info on moods and graphics that make your animated	// cowboy foe run.	private Opponent opp;		// lots of static variables to keep track of the players, their hands, and the deck:	// betting pot to temporarily hold bets.	private static Player pot;	// user's cash.	private static Player user;	// computer's cash.	private static Player computer;	// communal table cards.	private static PokerHand table;	// user's hand.	private static PokerHand userHand;	// computer's hand.	private static PokerHand computerHand;	// deck.	private static PokerDeck deck;	// TopPanel is the panel containing the opponent's cards, money and	// animated portrait	private TopPanel topPanel;		// QueryPanel asks the user if they want to play another game	private QueryPanel queryPanel;		// MidPanel contains the info and graphics for the poker table	private MidPanel midPanel;		// BottomPanel contains the user's cards and money, the bet and fold	// buttons, and a JTextArea infoBox	private BottomPanel bottomPanel;		// Indicates what stage of the game the user is currently in	private int stage;		// Iterator makes it possible to progress through the deck	private Iterator deckIter;	// Constructor sets up everything one needs to start up the first	// game of poker.	MainPokerWindow () {		super ();			setSize (WIDTH, HEIGHT);		setTitle ("Poker Game");				Container content = getContentPane();		content.setLayout(new BorderLayout());				opp = new Opponent (DEFAULT_OPP);		stage = 0;				topPanel = new TopPanel (opp);		content.add (topPanel, BorderLayout.NORTH);				QueryPanel qPanel = new QueryPanel (this);		content.add (qPanel, BorderLayout.CENTER);				queryPanel = qPanel;					bottomPanel = new BottomPanel (this);		bottomPanel.buttonPanel.setBetting ();		bottomPanel.buttonPanel.makeInvisible ();		content.add (bottomPanel, BorderLayout.SOUTH);					WindowDestroyer closeListener = new WindowDestroyer();		addWindowListener(closeListener);									// Create JMenu  and then add JMenuItem		JMenu pokerMenu = new JMenu("Extras");			// create a JMenuItem variable 'm' to store the menu		// items as they are created and added to the JMenu		JMenuItem m;		// Create a JMenuItem and add it to the JMenu		m = new JMenuItem("Rules");		m.addActionListener(this);		pokerMenu.add(m);		// Create a JMenuItem and add it to the JMenu object		m = new JMenuItem("About");		m.addActionListener(this);		pokerMenu.add(m);		// Create a JMenuItem and add it to the JMenu object		m = new JMenuItem("Exit");		m.addActionListener(this);		pokerMenu.add(m);			// Create a JMemuBar and add the JMenu object (variable memoMenu)		JMenuBar mBar = new JMenuBar( );		mBar.add(pokerMenu);		// Add the JMenuBar to the JFrame		setJMenuBar(mBar);					setVisible (true);		    }        // This method responds to a user's menu selections.    public void actionPerformed(ActionEvent e) {    		String actionCommand = e.getActionCommand( );		if (actionCommand.equals("Rules")) {		   RulesWindow rules = new RulesWindow ();		}		else if (actionCommand.equals("About")) {		    AboutWindow about = new AboutWindow ();		}		else if (actionCommand.equals("Exit"))		    System.exit (0);		else		    System.out.println("Error in menu interface");    }			// This method sets up a new MidPanel and deals the first round of cards.	public void newGame () {			//System.out.println ("Switch to new mid frame");				midPanel = new MidPanel ();				getContentPane().add (midPanel, BorderLayout.CENTER);		queryPanel.setVisible (false);				// initializing deckIterator so cards can be dealt.		deckIter = deck.createIterator();		// dealing cards		userHand.deal(2, deckIter, bottomPanel, true);		computerHand.deal(2, deckIter, topPanel, false);				// makes 'bet' and 'fold' buttons accessable		bottomPanel.buttonPanel.makeVisible ();		bottomPanel.buttonPanel.activateButtons ();				// automatically pays $5 ante		bottomPanel.setText ( "Ante up!" );		computer.transferTo(pot, 5);		user.transferTo(pot, 5);		refreshMoney ();				// if the computer's top card is especially high or especially low		// the animated portrait will make a shocked face:		opp.analyzeHand(computerHand);		System.out.println ("Computer is:  " + opp.getMood() );		if (opp.getMood().equals ("pleased") || opp.getMood().equals ("worried"))			topPanel.oppPanel.flashShock ();			}			// This method decides whether or not the computer will fold.	public void compBetOrFold () {			// creates a random number between 1 and 24		int rndChoice = (int)((Math.random()*24)+1);			// decides whether to bet (note that if computer is worried it will		// be more likely to fold and if it is pleased it will never fold)		if (opp.getMood().equals( "pleased" ) || (opp.getMood().equals( "neutral" ) && rndChoice < 24) || (opp.getMood().equals( "worried" ) && rndChoice < 12)) {			computer.transferTo(pot);			refreshMoney ();			bottomPanel.appendText ( "The computer also bets $10." );					stage++;			moveToNextStage();		}			else {			bottomPanel.setText ("The computer folds!");			pot.transferAllTo(user);			refreshMoney ();			launchEndPanel ("The computer folds and forfeits the game so YOU WIN!  The pot goes to you.");		}		}			// This method runs when the user presses the 'bet' button.  It transfers	// $10 to the pot and then launches compBetOrFold to find out if the	// computer bets or folds.	public void userBet () {			user.transferTo(pot);		refreshMoney ();				bottomPanel.setText ("You bet $10.  ");						compBetOrFold ();		}			// This method runs when the user presses the 'fold' button.  It transfers	// all the money on the table to the computer and ends the game.	public void userFold () {			pot.transferAllTo(computer);		refreshMoney ();		launchEndPanel ("By folding you forfeit the game and the COMPUTER WINS!  The pot goes to it.");				}			// This method uses the class variable 'stage' to figure out what stage	// of the game the user is at, and causes the game to progress accordingly.	public void moveToNextStage () {				switch (stage) {						case 1 : 				bottomPanel.setText ("*** Part I:  THE FLOP ***");				System.out.println( "Dealing 3 community cards to the table...");				table.deal(3, deckIter, midPanel, true);				System.out.println( "The cards on the table are now " + table + "." );				bottomPanel.buttonPanel.activateButtons ();								break;			case 2 : 				bottomPanel.setText ("*** Part II:  THE TURN ***");								System.out.println( "Dealing 1 community card to the table...");				table.deal(1, deckIter, midPanel, true);				System.out.println( "The cards on the table are now " + table + "." );								bottomPanel.buttonPanel.activateButtons ();								break;			case 3 : 				bottomPanel.setText ("*** Part III:  THE RIVER ***");								System.out.println( "Dealing 1 community card to the table...");				table.deal(1, deckIter, midPanel, true);				System.out.println( "The cards on the table are now " + table + "." );								bottomPanel.buttonPanel.activateButtons ();								break;			case 4 : 				bottomPanel.setText ("End Game");				launchEndPanel (determineWinner());							break;		}			}			// Determines the winner of the game, redistributes wealth accordingly,	// and launches an EndPanel to tell the user what just happened.	public String determineWinner () {				System.out.println( "You have " + userHand + " in the hole." );			System.out.println( "The computer has " + computerHand + " in the hole." );			System.out.println( "On the table are " + table + "." );			// flips computer's cards to reveal their values		for (int i=0; i<2; i++){			topPanel.card[i].flipToFace ();			System.out.println ("flipping card");		}			// combines player hands with the communal cards on the table in order to		// determine who has the highest card.		userHand = (PokerHand) userHand.join(table);		computerHand = (PokerHand) computerHand.join(table);		// compares player hands to determine winner, and splits betting pot accordingly.		if (userHand.compareTo( computerHand ) > 0) {			pot.transferAllTo(user);			System.out.println( "YOU WIN!  The pot goes to you.");			refreshMoney ();			return ( "YOU WIN!  The pot goes to you.");		}			else if (userHand.compareTo( computerHand ) < 0) {			pot.transferAllTo(computer);			System.out.println( "THE COMPUTER WINS!  The pot goes to it.");			refreshMoney ();			return ( "THE COMPUTER WINS!  The pot goes to it.");		}		else {			pot.transferTo(computer, pot.getMoney()/2);			pot.transferAllTo(user);			System.out.println( "IT'S A TIE!  You and the computer split the pot.");			refreshMoney ();			return ( "IT'S A TIE!  You and the computer split the pot.");		}		}			// This method clears various variables, turns buttons and cards invisible,	// and replaces the current MidPanel with an EndPanel to let the user	// know how the game turned out.	public void launchEndPanel (String msg) {			bottomPanel.buttonPanel.makeInvisible ();		table = new PokerHand();		userHand = new PokerHand();		computerHand = new PokerHand();		deck = new PokerDeck();			System.out.println ("Switch to new mid frame");				EndPanel endPanel = new EndPanel (msg, this);			remove (midPanel);			getContentPane().add (endPanel, BorderLayout.CENTER);		midPanel.setVisible (false);			}			// This method launches a new QueryPanel to ask the user if they	// want to play another game of poker.	public void launchQueryPanel (EndPanel endPanel) {		System.out.println ("Switch to new Mid Frame");			//clearing cards:		for (int i = 0; i < 2; i++) {		topPanel.card[i].setVisible (false);		topPanel.card[i].clearFace ();			}		for (int i = 0; i < 2; i++) {			bottomPanel.card[i].setVisible (false);			bottomPanel.card[i].clearFace ();		}			stage = 0;			QueryPanel qPanel = new QueryPanel (this);		getContentPane().add (qPanel, BorderLayout.CENTER);				queryPanel = qPanel;			endPanel.setVisible (false);		}			// This method refreshes the JLabels indicating how much money	// each of the players and the table currently have.	public void refreshMoney () {			topPanel.setNewMoney (computer.getMoney ());		midPanel.setNewMoney (pot.getMoney ());		bottomPanel.setNewMoney (user.getMoney ());				//System.out.println ( "Pot Money:  $" + pot.getMoney () );		//System.out.println ( "Comp Money:  $" + computer.getMoney () );		//System.out.println ( "User Money:  $" + user.getMoney () );	}			// Main method launches this whole thing.  Should be the very first	// method called.	public static void main (String args[]) {		System.out.println( "**************** Program Start ****************" );		pot = new Player(0);		user = new Player();		computer = new Player();		table = new PokerHand();		userHand = new PokerHand();		computerHand = new PokerHand();		deck = new PokerDeck();		MainPokerWindow mainWindow = new MainPokerWindow ();			}	}