/*
 * Dungeon.java
 * 
 * Version:
 * 		$Id: Dungeon.java,v 1.6 2014/12/02 01:51:38 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Dungeon.java,v $
 * 		Revision 1.6  2014/12/02 01:51:38  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.5  2014/12/01 14:22:18  mrm8391
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
 * 		Revision 1.4  2014/11/29 06:42:58  mrm8391
 * 		Added hallway,monster, and room classes
 *
 * 		Revision 1.3  2014/11/29 06:02:33  mrm8391
 * 		Changed Item back from being Observable and implemented a different way to destroy items.
 * 		Added other skeleton (for now) classes such as player, trap, etc...
 *
 * 		Revision 1.2  2014/11/28 23:17:13  mrm8391
 * 		Made entry point separate from game to make code more clear.
 *
 * 		Revision 1.1  2014/11/28 23:11:14  mrm8391
 * 		Started and finished program entry point including dungeon and config classes
 * 		-Checks for proper parameters and config locations
 * 		-doesn't check for proper config formatting yet
 *
 */

import java.io.File;

import javax.swing.JOptionPane;

/**
 * The manager of The Cellar game.
 * Serves as a graph (for the rooms) and dungeon management.
 * 
 * @author Maximillian McMullen
 */
public class Dungeon {
	
	//will contain game properties from config file
	Config properties;
	
	//the dumbplayer, who is dumb
	DumbPlayer player;
	//player position
	int playerPosition;
	//player room
	Room playerRoom;
	//the main display
	CellarView display;
	
	//the monster, who is superior to the dumb player
	Monster superiorMonster;
	//monster position
	int monsterPosition;
	//monster room reference
	Room monsterRoom;
	
	
	public Dungeon(Config config){
		properties=config;
		
		superiorMonster=new Monster(properties.monsterName,this);
		monsterPosition=properties.monsterRoom;
		monsterRoom=properties.rooms[monsterPosition];
		
		player=new DumbPlayer("DumbPlayer",this);
		playerRoom=properties.rooms[0];
		playerPosition=playerRoom.getId();
		
		display=new CellarView(this);
		display.start();
	}
	
	/**
	 * Moves the monster
	 * 
	 * @param to The room he is moving to
	 */
	
	public void monsterMove(Room to){
		monsterRoom=to;
		monsterPosition=to.getId();
	}
	
	/**
	 * Moves the player
	 * 
	 * @param to The room the player is moving to
	 */
	
	public void playerMove(Room to){
		playerRoom=to;
		playerPosition=to.getId();
		superiorMonster.move();
		
		display.refreshRoomInformation();
		enterRoom();
	}
	
	/**
	 * What happens when the player enters a room
	 */
	
	public void enterRoom(){
		if(playerRoom.hasTrap())
			trapSprung(playerRoom.getTrap());
		
		checkMonster();
	}
	
	/**
	 * Checks to see if the monster is in the same room and starts the battle
	 * if he is
	 */
	
	public void checkMonster(){
		if(!superiorMonster.isDead()&&playerPosition==monsterPosition)
			monsterFight();
	}
	
	/**
	 * What happens when a player encounters the monster.
	 * Either deals 50%/10 points damage, or is slain if the
	 * player wields the sword
	 */
	
	private void monsterFight(){
		if(player.hasSword()){
			superiorMonster.bleed(1);
			player.levelUp();
			display.monsterEncounter(0);
		}
		else{
			int before=player.getHealth();
			if(player.getHealth()/2>10)
				player.bleed(player.getHealth()/2);
			else
				player.bleed(10);
			
			display.monsterEncounter(before-player.getHealth());
			testDead();
		}
	}
	
	/**
	 * Springs a trap, affecting the player accordingly
	 * 
	 * @param trap The trap that was sprung
	 */
	
	private void trapSprung(Trap trap){
		if(!trap.isActive()) return;
		trap.spring();
		if(player.hasItem(trap.getCounter())){
			player.breakItem(trap.getCounter());
			player.levelUp();
			display.refreshRoomInformation();
			display.trapDialog(trap, true);
		}
		else{
			//the screen/dialog are displayed after the warp to prevent
			//another trap from being set off before this one finishes
			switch(trap.getType()){
			case Trap.WEAKEN:
				player.bleed(trap.getControl());
				display.refreshRoomInformation();
				display.trapDialog(trap, false);
				testDead();
				break;
			case Trap.WARP:
				display.refreshRoomInformation();
				display.trapDialog(trap, false);
				playerMove(properties.rooms[
				         (int)Math.floor(Math.random()*properties.rooms.length)]);
				break;
			case Trap.VANISH:
				player.breakAll();
				display.refreshRoomInformation();
				display.trapDialog(trap, false);
				break;
			}
		}
	}
	
	private void testDead(){
		if(player.isDead()){
			endGame(false);
		}
	}
	
	/**
	 * Returns the player
	 * 
	 * @return The player
	 */
	public DumbPlayer getPlayer(){
		return player;
	}
	
	/**
	 * Lets the player drop an item,
	 * 
	 * @param item The item that is being dropped
	 */
	
	public void playerDrop(Item item){
		this.playerRoom.addItem(item);
		superiorMonster.move();
		checkMonster();
	}
	
	/**
	 * Lets the player pick up an item
	 * 
	 * @param item The item to pick up
	 * @return True if the item is a valid item to pick up
	 */
	
	public boolean playerPickup(Item item){
		if(playerRoom.lootList().contains(item)){
			if(item.getType().equals(Item.AMULET))
				endGame(true);
			
			if(item.getType().equals(Item.PROTECTION)&&
					player.totalProtections()>=player.getLevel()){
				display.tooManyItems();
				return false;
			}
			
			playerRoom.removeItem(item);
			superiorMonster.move();
			checkMonster();
			return true;
		}
		superiorMonster.move();
		checkMonster();
		return false;
	}
	
	public Room getPlayerRoom(){
		return playerRoom;
	}
	
	public Monster getMonster(){
		return superiorMonster;
	}
	
	public Room getMonsterRoom(){
		return monsterRoom;
	}
	
	/**
	 * True if the monster is dead
	 */
	
	public boolean monsterDead(){
		return superiorMonster.isDead();
	}
	
	/**
	 * Ends the game with either a victory or game over
	 * 
	 * @param didWin If the player won, a victory is displayed, otherwise a game over
	 */
	public void endGame(boolean didWin){
		if(didWin){
			display.win();
		}
		else{
			display.lose();
		}
	}
}



















