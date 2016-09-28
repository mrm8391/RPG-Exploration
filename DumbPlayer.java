/*
 * DumbPlayer.java
 * 
 * Version:
 * 		$Id: DumbPlayer.java,v 1.4 2014/12/02 02:15:45 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: DumbPlayer.java,v $
 * 		Revision 1.4  2014/12/02 02:15:45  mrm8391
 * 		Removed some inappropriate text
 *
 * 		Revision 1.3  2014/12/02 01:51:38  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.2  2014/12/01 14:22:18  mrm8391
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
 * 		Revision 1.1  2014/11/29 06:02:32  mrm8391
 * 		Changed Item back from being Observable and implemented a different way to destroy items.
 * 		Added other skeleton (for now) classes such as player, trap, etc...
 *
 */

/**
 * Represents the player and handles input sent from the view/control
 * 
 * @author Maximillian McMullen
 */

public class DumbPlayer extends Entity{
	//the game
	private Dungeon game;
	
	public DumbPlayer(String name,Dungeon game){
		super(name,100,game);
		this.game=game;
	}
	
	public void die(){
		
	}
	
	public void move(Room room){
		game.playerMove(room);
	}
	
	/**
	 * Drops an item
	 */
	
	public void drop(Item item){
		if(super.hasItem(item)){
			inventory.remove(item);
			game.playerDrop(item);
		}
	}
	
	/**
	 * Picks up item
	 * 
	 * @param Item
	 */
	
	public void pickup(Item item){
		//if the game lets the player pick up...
		if(game.playerPickup(item)){
			inventory.add(item);
		}
	}
	
	/**
	 * Returns the total amount of protections held
	 * 
	 * @return The number of protections
	 */
	
	public int totalProtections(){
		int total=0;
		for(Item item:super.getInventory()){
			if(item.getType().equals(Item.PROTECTION))
				total++;
		}
		
		return total;
	}
}

