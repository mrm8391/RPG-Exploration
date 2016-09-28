/*
 * Hallway.java
 * 
 * Version:
 * 		$Id: Hallway.java,v 1.3 2014/12/02 01:51:37 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Hallway.java,v $
 * 		Revision 1.3  2014/12/02 01:51:37  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.2  2014/12/01 14:22:16  mrm8391
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
 * 		Revision 1.1  2014/11/29 06:42:57  mrm8391
 * 		Added hallway,monster, and room classes
 *
 */

import java.util.ArrayList;

/**
 * A hallway connecting two rooms
 * 
 * @author Maximillian McMullen
 */

public class Hallway {
	//all rooms in this hallways
	private ArrayList<Room> rooms;
	private String name;
	
	/**
	 * Constructs a hallway with a variable amount of rooms. Assumes it's > 1
	 * 
	 * @param rooms All rooms in the hall, varargs
	 */
	
	public Hallway(String name,Room... rooms){
		this.name=name;
		
		this.rooms=new ArrayList<Room>();
		
		for(Room room:rooms){
			addRoom(room);
		}
	}
	
	/**
	 * Adds a room to the hallway and adds this hall to the room.
	 * 
	 * @param room The room to add
	 */
	
	public void addRoom(Room room){
		rooms.add(room);
		
		//so that the room knows there is a hallway
		room.addHall(this);
	}
	
	/**
	 * Return available rooms to go to from a given room. Serves to aid in
	 * finding "neighbors" of a room.
	 * 
	 * @param room The starting room
	 * @return All rooms through the hall that come from room.
	 */
	
	public Room otherRoom(Room room){
		for(Room current:rooms){
			if(!current.equals(room))
				return current;
		}
		return null;
	}
	
	/**
	 * Gets hall name
	 * 
	 * @return hall name
	 */
	
	public String getName(){
		return name;
	}
}




























