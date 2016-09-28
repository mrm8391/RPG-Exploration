/*
 * Room.java
 * 
 * Version:
 * 		$Id: Room.java,v 1.3 2014/12/02 01:51:37 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Room.java,v $
 * 		Revision 1.3  2014/12/02 01:51:37  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.2  2014/12/01 14:22:15  mrm8391
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
 * 		Revision 1.1  2014/11/29 06:42:56  mrm8391
 * 		Added hallway,monster, and room classes
 *
 */

import java.util.ArrayList;

/**
 * Represents a room in the dungeon
 * 
 * @author Maximillian McMullen
 */

public class Room {
	private ArrayList<Hallway> hallways;
	private ArrayList<Item> loot;
	private Trap trap;
	private Hallway hallway;
	
	private static int id=0;
	private int roomNumber;
	
	/**
	 * Initializes room properties
	 * 
	 * @param trap Room trap, null if none
	 */
	
	public Room(Trap trap){
		hallways=new ArrayList<Hallway>();
		loot=new ArrayList<Item>();
		this.trap=trap;
		roomNumber=id++;
	}
	
	/**
	 * initializes room with no trap (null)
	 */
	
	public Room(){
		this(null);
	}
	
	/**
	 * Adds a hallway to this room, serves as a point to leave the room.
	 * Overwrites previous (if any) hallway in this room
	 */
	
	public void addHall(Hallway hall){
		hallways.add(hall);
	}
	
	/**
	 * Gets the rooms connected to this one via hallways
	 * 
	 * @return arraylist with other rooms
	 */
	
	public ArrayList<Hallway> getNeighbors(){
		return hallways;
	}
	
	/**
	 * Checks room for trap, there can only be one
	 * 
	 * @return true if there is indeed a trap
	 */
	
	public boolean hasTrap(){
		return trap!=null;
	}
	
	/**
	 * Adds item(s)
	 * 
	 * @param items Item or items to be added
	 */
	
	public void addItem(Item... items){
		for(Item item:items)
			loot.add(item);
	}
	
	/**
	 * Removes an item if it exists
	 * 
	 * @param item The item
	 */
	
	public void removeItem(Item item){
		if(loot.contains(item))
			loot.remove(item);
	}
	
	/**
	 * Returns a list of all the items in the room
	 * 
	 * @return A list of all the items in the room
	 */
	
	public ArrayList<Item> lootList(){
		return loot;
	}
	
	/**
	 * Returns room id
	 * 
	 * @return room id
	 */
	
	public int getId(){
		return this.roomNumber;
	}
	
	/**
	 * Returns the room's trap.
	 * 
	 * @return The room's trap 
	 */
	public Trap getTrap(){
		return trap;
	}
	
	/**
	 * Checks for the amulet
	 * 
	 * @return true if it's there
	 */
	
	public boolean hasAmulet(){
		for(Item item:loot){
			if(item.getType().equals(Item.AMULET))
				return true;
		}
		
		return false;
	}
	
	/**
	 * Checks for equality by checking room number
	 * 
	 * @return true if the ids are the same
	 */
	
	public boolean equals(Object other){
		if(other==null||(!(other instanceof Room))) return false;
		
		return this.roomNumber==((Room)other).roomNumber;
	}
}

















