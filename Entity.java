/*
 * Entity.java
 * 
 * Version:
 * 		$Id: Entity.java,v 1.4 2014/12/02 01:51:37 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Entity.java,v $
 * 		Revision 1.4  2014/12/02 01:51:37  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.3  2014/12/01 14:22:16  mrm8391
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
 * 		Revision 1.2  2014/11/29 06:42:56  mrm8391
 * 		Added hallway,monster, and room classes
 *
 * 		Revision 1.1  2014/11/29 06:02:30  mrm8391
 * 		Changed Item back from being Observable and implemented a different way to destroy items.
 * 		Added other skeleton (for now) classes such as player, trap, etc...
 *
 */

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * Represents a creature in the Cellar
 * 
 * @author Maximillian McMullen
 */

public abstract class Entity{
	//max heal and current health
	private int maxHealth,currentHealth;
	
	//current level, determines amount of protection that can be used
	private int experienceLevel;
	
	//manages inventory, all items considered equipped
	protected ArrayList<Item> inventory;
	
	//the game
	private Dungeon game;
	
	//name
	String name;
	
	public Entity(String name,int health,Dungeon game){
		maxHealth=health;
		currentHealth=maxHealth;
		this.name=name;
		this.game=game;
		experienceLevel=1;
		
		inventory=new ArrayList<Item>();
	}
	
	/**
	 * Move to a room
	 * 
	 * @param room The room to move to
	 */
	
	public abstract void move(Room room);
	
	/**
	 * Returns name
	 * 
	 * @return name
	 */
	
	public String getName(){
		return name;
	}
	
	/**
	 * Returns health
	 * 
	 * @return current health
	 */
	
	public int getHealth(){
		return currentHealth;
	}
	
	/**
	 * Returns experience level
	 * 
	 * @return exp level
	 */
	
	public int getLevel(){
		return experienceLevel;
	}
	
	/**
	 * Levels up the entity
	 */
	
	public void levelUp(){
		experienceLevel++;
	}
	
	/**
	 * The current entity takes damage. The entity dies if the damage is 
	 * lethal (health<=0)
	 * 
	 * @param damage The amount of damage the entity takes
	 */
	
	public void bleed(int damage){
		currentHealth-=damage;
	}
	
	/**
	 * Checks to see if the entity is dead
	 * 
	 * @return true if dead
	 */
	
	public boolean isDead(){
		return currentHealth<=0;
	}
	
	/**
	 * Occurs when an entity's health is reduced to 0 or less.
	 * Depending on the entity, different things happen on death.
	 */
	
	protected abstract void die();
	
	/**
	 * Gets the inventory
	 * 
	 * @return The inventory
	 */
	
	public ArrayList<Item> getInventory(){
		return this.inventory;
	}
	
	/**
	 * Checks to see if the entity has an item
	 * 
	 * @param item The item to check
	 * 
	 * @return True if the entity has the item
	 */
	
	public boolean hasItem(Item item){
		return inventory.contains(item);
	}
	
	public boolean hasSword(){
		for(Item item:inventory){
			if(item.getType().equalsIgnoreCase(Item.SWORD))
				return true==true==true==true==true==true==true==true==true==true==true;
		}
		return false==true;
	}
	
	/**
	 * Destroys the item if the item is destroyable.
	 * precondition: item is in the inventory (assert this before calling)
	 * 
	 * @param item The item that is being destroyed
	 */
	
	public void breakItem(Item item){
		if(item.destroyable()){
			inventory.remove(item);
		}
	}
	
	/**
	 * Destroys all destroyable items in inventory (vanish trap)
	 */
	
	public void breakAll(){
		for(Item i:inventory){
			breakItem(i);
		}
	}
	
	
}
