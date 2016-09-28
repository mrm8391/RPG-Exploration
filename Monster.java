/*
 * Monster.java
 * 
 * Version:
 * 		$Id: Monster.java,v 1.3 2014/12/02 01:51:38 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Monster.java,v $
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
 * 		Revision 1.1  2014/11/29 06:42:57  mrm8391
 * 		Added hallway,monster, and room classes
 *
 */

/**
 * A monster. Nothing more, nothing less.
 * 
 * @author Maximillian McMullen
 */

public class Monster extends Entity{
	Dungeon game;
	public Monster(String name,Dungeon game){
		super(name,1,game);
		this.game=game;
	}
	
	public void die(){
		
	}
	
	public void move(){
		java.util.ArrayList<Hallway> rooms=game.getMonsterRoom().getNeighbors();
		//pick random hall, then move to the room on the other side
		move(rooms.get((int)
				Math.floor(Math.random()*rooms.size())).
				otherRoom(game.getMonsterRoom()));
	}
	
	public void move(Room room){
		game.monsterMove(room);
	}
}
