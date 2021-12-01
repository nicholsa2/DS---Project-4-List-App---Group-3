package edu.wit.scds.comp2000.list.app;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import edu.wit.scds.comp2000.list.app.pile.Deck;
import edu.wit.scds.comp2000.list.app.Player;
import edu.wit.scds.comp2000.list.app.card.Card;
import edu.wit.scds.comp2000.list.app.card.CardType;


/**
 * 
 *
 * @author Audrey Nichols
 * @version 1.0.0 2021-11-30 Initial Implementation
 *
 */
public class UnoGame 
    {

    /**
     * 
     *
     * @param args
     */
	
	private static List< Player > players = new ArrayList<>() ;
	private static Deck unoDeck = new Deck() ;
	private static Player currentPlayer ;
	private static int numPlayers ;
	private static int playerTurn ;
	private static boolean turnOrder = true ;
	
	private static boolean gameOver = false ;
	private static String winner ;
	
	private static boolean cardActive = true ;
		
	
    public static void main( final String[] args ) 
    {
    	Scanner search = new Scanner( System.in ) ;
    	
    	getPlayers( search ) ;	
    	currentPlayer = players.get( 0 ) ;
    	
    	initializeGame() ;
    	
    	while( gameOver == false )
    	{
    		round( search ) ;
    		newRound() ;
    	}
    	
    	System.out.println( String.format( " % has reached 500 points and won! ", winner ) ) ;
    	
    	
  
        // TODO Auto-generated method stub
    	
    }
    
    
    /*
     * deal initial hands to players
     */
    public static void initializeGame() 
    {
    	Card playerCard;
    	
    	for ( int i = 0; i < 7; i++ ) 
    	{
        	for ( Player tempPlayer : players ) 
        	{
        		playerCard = unoDeck.deal() ;
        		tempPlayer.getHand().receiveCard( playerCard ) ;
        	} // end for
    	} // end for
    	
    	Card firstDiscard = unoDeck.deal() ;
    	unoDeck.discard( firstDiscard ) ;    	
    } // end initializeGame
    
    /*
     * Takes player name
     * 
     * Creates type player with that name, and adds them to players list
     */
    public static void createPlayer( String name ) 
    {
    	Player newPlayer = new Player( name );
    	players.add( newPlayer );
    }
    
    public static void draw( int numCards )
    {
    	Card newCard ;
    	
    	for ( int i = 0; i < numCards; i++ )
    	{
    		newCard = unoDeck.deal();
    		currentPlayer.getHand().receiveCard( newCard ) ;
    	}
    }
    
    /*
     * Asks user for number of players, if too high or too low, user is reprompted.
     * Once valid number of players is given, players may input their names.
     */
    public static void getPlayers( Scanner search )
    {
    	
    	System.out.println( "Input number of players (2-10): " ) ;
    	
    	numPlayers = search.nextInt() ;
    	
    	search.nextLine() ;
    	
    	for ( int i = 1; i <= numPlayers; i++ )
    	{
    		System.out.println( String.format( "Player %d, input your name: ", i ) ) ;
    		
    		String newName = search.nextLine() ;
    		createPlayer( newName ) ;
    	} // end for
    	   
    }
    
    
    /*
     * Takes
     * 
     * @return current deck
     */
    public Deck getDeck() 
    {
    	return this.unoDeck;
    }
    
    
    /*
     * Player turns occur until one player has no cards in hand
     */
    public static void round( Scanner search ) {
    	Card discard = unoDeck.lastPlayed();
    	boolean handsFull = true ;
    	
    	
    	playerTurn = 1 ;
    	
    	while( handsFull ) 
    	{
    		turn( currentPlayer, discard, search ) ;
    		// check hands to see if round is over
    		// currentPlayer goes to next player
    	}
    	
    	
    	
    	
    }
    
    
    
    public static void turn( Player player, Card lastCard, Scanner search ) 
    {
    	
    	System.out.println( String.format( "%s, your hand contains the following cards: %s" , 
    			player.getName(), player.getHand() ) ) ;
    	System.out.println( String.format( "The last card played was %s.", lastCard.toString() ) ) ;
    	
    	if ( lastCard.getType() == CardType.WILD_DRAW_FOUR ) 
    	{
    		if( cardActive )
    		{
    			System.out.println( "You draw four cards, and your turn is over." ) ;
    			draw( 4 ) ;
    		} // end if
    		else
    		{
    			play( lastCard, search ) ;
    		} // end else
    		// next turn
    		
    	} // end if
    	else if ( lastCard.getType() == CardType.DRAW_TWO ) 
    	{
    		if( cardActive )
    		{
    			System.out.println( "You draw two cards, and your turn is over." ) ;
    			draw( 2 ) ;
    		} // end if
    		else
    		{ 
    			play( lastCard, search ) ;
    		} // end else
    		
    		// next turn
    	} // end else if
    	else if ( lastCard.getType() == CardType.SKIP ) 
    	{
    		if( cardActive )
    		{
    			System.out.println( "Your turn has been skipped." ) ;
    		} // end if
    		
    		// next turn
    	} // end else if 
    	else if ( lastCard.getType() == CardType.WILD ) 
    	{
    		System.out.println( String.format( "This wild card has been given the color %s.", lastCard.getType() ) ) ;
    		
    		play( lastCard, search ) ;
    	
    		// next turn
    	} // end else if
    	else if ( lastCard.getType() == CardType.REVERSE ) 
    	{
    		if( cardActive )
    		{
    			Collections.reverse( players ) ;
    			System.out.println( "The current player is now " ) ;
    		} // end if
    		else
    		{
    			play( lastCard, search ) ;
    		}
    		
    		// next turn
    	} // end else if 
    	else 
    	{
    		play( lastCard, search ) ;
    		// next turn
    	} // end else
    	
    	
    	
    	
    	
    }
    
    /*
     * Check points of all players to see if one has reached 500
     * If so, the game is over and this player has won
     */
    public static void checkPoints() 
    {
    	for( Player name: players ) {
    		if( name.getScore() >= 500 )
    		{
    			gameOver = true ;
    			winner = name.getName() ;
    			break ;
    		} // end if
    		else
    		{
    			System.out.println( String.format( "%s has %d points.", name.getName(), name.getScore() ) ) ;
    		} // end else
    	} // end for
    } // end checkPoints
    
    
    public static void play( Card discard, Scanner search )
    {
    	int color ;
    	int choice ;
    	Card testCard = null ;
    	boolean validChoice = false ;
    	
    	System.out.println( "You must play a card that matches the color or type of the last card played." ) ;
    	System.out.println( "What card do you want to play from your hand? Please give the index of the card." ) ;
    	
    	choice = search.nextInt() ;
    	
    	
    	
    	while ( validChoice == false )
    	{
    		testCard = currentPlayer.getHand().pile.get( choice - 1 ) ;
    	
    		if ( testCard.getColor() == discard.getColor() 
    				|| testCard.getType() == discard.getType() )
    		{  			
    			validChoice = true ;
    			
    			currentPlayer.getHand().playCard( testCard ) ;
    			unoDeck.discard( testCard ) ;
    			
    			cardActive = true ;
    		} // end if
    		else
    		{
    			System.out.println( "You must play a card that matches the color or type of the last card played." ) ;
    			System.out.println( "Please try again with a new index." ) ;
    			
    			choice = search.nextInt() ;
    		} // end else
    		
    	} // end while	
    	
    	System.out.println( String.format( "You have chosen to play %s.", testCard ) ) ;
    	
    	if ( testCard.getType() == CardType.WILD || testCard.getType() == CardType.WILD_DRAW_FOUR )
    	{
    		System.out.println( "What color would you like to designate this wild card?" ) ;
    		System.out.println( "Red, Yellow, Green, or Blue? (Type 1-4)." ) ;
    		
    		color = search.nextInt() ;
    		
    		if ( color < 1 || color > 4 )
    		{
    			System.out.println( "Please try again." ) ;
    			color = search.nextInt() ;
    		}
    		
    		// TODO change card color to this color??
    	}
    	
    } // end method play
    
    
    
    /*
     * Initialize next round of the game
     * First check points to make sure no player has won yet
     * Next perform same steps as initializeGame
     */
    public static void newRound() 
    {
    	// TODO clear player hands
    	checkPoints(); 
    	initializeGame();
    } // end method newRound
    
    }
	// end class UnoGame
