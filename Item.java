/*
 * Item.java
 * 
 * Version:
 * 		$Id: Item.java,v 1.4 2014/12/02 01:51:39 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Item.java,v $
 * 		Revision 1.4  2014/12/02 01:51:39  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.3  2014/12/01 14:22:19  mrm8391
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
 * 		Revision 1.2  2014/11/29 06:42:58  mrm8391
 * 		Added hallway,monster, and room classes
 *
 * 		Revision 1.1  2014/11/29 06:02:34  mrm8391
 * 		Changed Item back from being Observable and implemented a different way to destroy items.
 * 		Added other skeleton (for now) classes such as player, trap, etc...
 *
 */

import java.util.Observable;

/**
 * Represents an item, but doesn't do much else. The Dungeon class handles
 * situations differently depending on what items the Player has, the item
 * doesn't actually perform any actions.
 * 
 * @author Maximillian McMullen
 */

public class Item{
	//item types, determines their purpose
	public static final String PROTECTION="Protection",
							   SWORD="Sword",
							   AMULET="Amulet";
	
	private String name,type,description;
	
	private boolean destroyed;
	
	public Item(String name,String type){
		this.name=name;
		this.type=type;
		destroyed=false;
	}
	
	public Item(String name,String type,String description){
		this(name,type);
		this.description=description;
	}
	
	/**
	 * Returns whether or not the item can be destroyed
	 */
	
	public boolean destroyable(){
		return !type.equals(Item.SWORD);
	}
	
	
	/**
	 * Item name
	 * 
	 * @return Item name
	 */
	
	public String getName(){
		return name;
	}
	
	/**
	 * Item type
	 * 
	 * @return Item type
	 */
	
	public String getType(){
		return type;
	}
	
	/**
	 * Item description
	 * 
	 * @return Item description
	 */
	
	public String getDescription(){
		return description;
	}
	
	/**
	 * Checks for equality
	 * 
	 * @param other Other item
	 */
	public boolean equals(Object other){
		return this.name.equalsIgnoreCase(((Item)other).name)&&
				this.type.equals(((Item)other).getType());
	}
	
	/**
	 * String representation of an item
	 * 
	 * @return String
	 */
	
	public String toString(){
		return type+" "+name;
	}
}
