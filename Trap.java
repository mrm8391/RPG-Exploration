/*
 * Trap.java
 * 
 * Version:
 * 		$Id: Trap.java,v 1.3 2014/12/02 01:51:37 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Trap.java,v $
 * 		Revision 1.3  2014/12/02 01:51:37  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.2  2014/12/01 14:22:17  mrm8391
 * 		Did the entirety of the file reading code in Config
 * 		Also repurposed Config to also generate game data (rooms, items...)
 * 		alongside storing the configurations
 *
 * 		Figured out movement between rooms and did the monster
 * 		encounter code.
 *
 * 		Got started on the view component
 *
 * 		Player inventory display is being worked on
 * 		(commit comments are merged into one commit because I was working offline for a long time)
 *
 * 		Revision 1.1  2014/11/29 06:02:31  mrm8391
 * 		Changed Item back from being Observable and implemented a different way to destroy items.
 * 		Added other skeleton (for now) classes such as player, trap, etc...
 *
 */

/**
 * Represents a trap, containing relevent effect information
 * 
 * @author Maximillian McMullen
 */
public class Trap {
	//different trap types
	public static final String WEAKEN="weaken", //deals damage
							   WARP="warp",     //warps to a room
							   VANISH="vanish"; //destroys all protection items
	
	//trap name
	private String name;
	
	//trap type
	private String type;
	
	//protective item
	private Item counter;
	
	//controls the effect
	private int control;
	
	//false if the trap has already been sprung
	private boolean active;
	
	/**
	 * Constructs a trap with properties
	 * 
	 * @param name Name of the trap
	 * @param type Type of trap
	 * @param counter Item that protects against the trap
	 * @param control Int parameter
	 */
	public Trap(String name,String type,String counter,int control){
		this.name=name;
		this.type=type;
		this.counter=new Item(counter,Item.PROTECTION);
		this.control=control;
		active=true;
	}
	
	/**
	 * Springs the trap. Effects are handled in the dungeon class
	 */
	
	public void spring(){
		active=false;
	}
	
	/**
	 * Tests if the trap is active
	 * 
	 * @return True if it is
	 */
	
	public boolean isActive(){
		return active;
	}
	
	/**
	 * Returns trap name
	 * 
	 * @return Trap name
	 */
	
	public String getName(){
		return name;
	}
	
	/**
	 * Returns trap type
	 * 
	 * @return Trap type
	 */
	
	public String getType(){
		return type;
	}
	
	/**
	 * Trap counter
	 * 
	 * @return Trap counter
	 */
	
	public Item getCounter(){
		return counter;
	}
	
	/**
	 * Trap control
	 * 
	 * @return Trap control
	 */
	
	public int getControl(){
		return control;
	}
}

















