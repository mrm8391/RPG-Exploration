/*
 * CellarView.java
 * 
 * Version:
 * 		$Id: CellarView.java,v 1.4 2014/12/02 02:19:29 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Id: CellarView.java,v 1.4 2014/12/02 02:19:29 mrm8391 Exp $
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.*;


/**
 * The VIEW. Will be reading data from the model and 
 * displaying information and buttons on the gui.
 * 
 * @author Maximillian McMullen
 */

public class CellarView extends Thread{
	
	
	ArrayList<JButton> infoButtons;
	Dungeon game;
	JList<Item> inventoryDisplay,roomInventoryDisplay;
	JLabel amuletLabel,monsterLabel,swordLabel,
			playerHealthLabel,playerLevelLabel,
			travelLabel;
	
	JFrame cellarFrame;
	JPanel travelPane;
	
	public CellarView(Dungeon game){
		super();
		infoButtons=new ArrayList<JButton>();
		this.game=game;
	}
	
	public void run(){
		cellarFrame=new JFrame();
		//window listeners
		cellarFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		cellarFrame.setTitle("The Cellar by Maximillian McMullen 319002xxx");
		cellarFrame.setLayout(new BorderLayout());
		
		//will read information from player inventory,scrollbar wraps jlist
		inventoryDisplay=new JList<Item>(game.getPlayer().
								getInventory().toArray(new Item[]{}));
		inventoryDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane inventoryScrollBar=new JScrollPane(inventoryDisplay);
		
		//button to drop items from inventory
		JButton dropButton=new JButton(new AbstractAction("Drop"){
			public void actionPerformed(ActionEvent e){
				int item=inventoryDisplay.getSelectedIndex();
				if(item==-1){}//if something is actually selected
				else{
					//have player drop item and refresh displays
					game.getPlayer().drop(inventoryDisplay.getSelectedValue());
					refreshInventoryDisplay();
					refreshRoomInformation();
				}
			}
		});
		
		//reads from room items
		roomInventoryDisplay=new JList<Item>(game.getPlayerRoom().
								lootList().toArray(new Item[]{}));
		roomInventoryDisplay.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane roomInventoryScrollBar=new JScrollPane(roomInventoryDisplay);
		
		//bascially a copypaste from the drop code, only with picking up
		JButton pickupButton=new JButton(new AbstractAction("Pickup"){
			public void actionPerformed(ActionEvent e){
				int item=roomInventoryDisplay.getSelectedIndex();
				if(item==-1){}//if something is actually selected
				else{
					//have player pick up item and refresh display
					
					game.getPlayer().pickup(roomInventoryDisplay.getSelectedValue());
					refreshInventoryDisplay();
					refreshRoomInformation();
				}
			}
		});
		
		
		amuletLabel=new JLabel("Amulet: ");
		monsterLabel=new JLabel("Monster: ");
		swordLabel=new JLabel("Sword: ");
		playerHealthLabel=new JLabel("HP: ");
		playerLevelLabel=new JLabel("Level: ");
		travelLabel=new JLabel("Click a hallway to travel: ");
		
		JPanel southPanel=new JPanel();
		southPanel.setLayout(new GridLayout(1,5));
		southPanel.add(inventoryScrollBar);
		southPanel.add(dropButton);
		southPanel.add(swordLabel);
		southPanel.add(playerHealthLabel);
		southPanel.add(playerLevelLabel);
		
		
		JPanel eastPanel=new JPanel();
		eastPanel.setLayout(new GridLayout(4,1));
		eastPanel.add(roomInventoryScrollBar);
		eastPanel.add(pickupButton);
		eastPanel.add(amuletLabel);
		eastPanel.add(monsterLabel);
		
		travelPane=new JPanel();
		travelPane.setLayout(new FlowLayout());
		refreshRoomInformation();
		
		cellarFrame.add(travelPane);
		cellarFrame.add(southPanel,BorderLayout.SOUTH);
		cellarFrame.add(eastPanel,BorderLayout.EAST);
		
		cellarFrame.pack();
		cellarFrame.setVisible(true);
	}
	
	public void refreshInventoryDisplay(){
		inventoryDisplay.setListData(
				game.getPlayer().getInventory().toArray(new Item[]{}));
	}
	
	public void refreshRoomInformation(){
		roomInventoryDisplay.setListData(game.getPlayerRoom().lootList().toArray(new Item[]{}));
		travelPane.removeAll();
		travelPane.add(travelLabel);
		
		for(Hallway hall:game.getPlayerRoom().getNeighbors()){
			//System.out.println(hall.getName());
			travelPane.add(generate_travel_button(hall));
		}
		
		if(!game.getPlayerRoom().hasAmulet()){
			amuletLabel.setText("Amulet: Keep Looking");
		}
		else{
			amuletLabel.setText("Amulet: IT'S HERE");
		}
		
		if(game.getPlayer().hasSword()){
			swordLabel.setText("Sword: Yes, let's kick some ass!");
		}
		else{
			swordLabel.setText("Sword: No");
		}
		
		playerHealthLabel.setText("   HP: "+game.getPlayer().getHealth());
		playerLevelLabel.setText("Level: "+game.getPlayer().getLevel());
		
		if(game.monsterDead()){
			monsterLabel.setText("Monster: Slain");
			swordLabel.setText("Yes, but it's all bloody and icky");
		}
		else{
			monsterLabel.setText("Monster: Somewhere not here");
		}
		
		
		
		travelPane.validate();
	}
	
	/**
	 * Generates a button that will let the player travel to a room
	 * when it's clicked on.
	 * 
	 * @param destination The destination room
	 * @return The button
	 */
	
	private JButton generate_travel_button(Hallway destination){
		return new JButton(new AbstractAction(destination.getName()){
			public void actionPerformed(ActionEvent e){
				game.getPlayer().move(destination.otherRoom(game.getPlayerRoom()));
			}
		});
	}
	
	/**
	 * Shows a dialog box explaining the encounter with a trap. This doesn't
	 * actually apply the effects of the trap, it just shows what happens,
	 * 
	 * @param trap The trap that was set off
	 * @param didProtect True if the player had the correct protection
	 */
	
	public void trapDialog(Trap trap,boolean didProtect){
		if(didProtect)
			JOptionPane.showMessageDialog(cellarFrame, "You encountered a ("
			   +trap.getName()
			   +") trap, but thankfully your "
			   +trap.getCounter()
			   +" protected you from it. It was destroyed after its use,"
			   + " unfortunately. You also feel a little more dextrous "
			   + "after using the protection so skillfully. \n(You gained "
			   + "a level!!!!!!!!!!!!!!!!!!!!!1)");
		else{
			String trapText="You hear a faint "
					+ "click, and before you know it you're blinded by a "
					+ "trap. ";
			
			switch(trap.getType()){
			case Trap.WEAKEN:
				trapText+="In your exposed state, you couldn't defend "
						+ "yourself from the dozens of arrow traps mounted "
						+ "on the opposite walls. You take "
						+ trap.getControl()
						+" damage. \n(You activated a weaken trap)";
				break;
			case Trap.WARP:
				trapText+="As the light begins to fade, you rub your eyes and"
						+ " realise you're not in the same room anymore."
						+ " \n(You activated a warp trap)";
				break;
			case Trap.VANISH:
				trapText+="You grit your teeth, awaiting the deadly effects "
						+ "of the trap, but nothing appears to happen. You recover from "
						+ "the blinding light and, as you begin to move, notice "
						+ "that you're much lighter now. The trap destroyed all "
						+ "of your protections! \n(You activated a vanish trap)";
			}
			
			JOptionPane.showMessageDialog(cellarFrame, trapText);
		}	
	}
	
	/**
	 * Shows the results of a monster encounter
	 * 
	 * @param damage Damage dealt to the player, 0 assumes that the player beat the monster
	 */
	
	public void monsterEncounter(int damage){
		String monsterText="You've encountered the scary monster "
					+game.getMonster().getName()
					+"! ";
		
		if(damage==0){
			monsterText+="Thankfully, you were prepared for this. You pull "
					+ "out your sword and plunge it into "
					+ game.getMonster().getName()
					+ "'s chest, fatally wounding them. You feel a little "
					+ "stronger after this encounter. \n(You gained a level"
					+ "!!!!!!!!!!!!!!!!!!!!!1)";
		}
		else{
			monsterText+="With nothing to defend yourself with, you try to "
					+ "escape, but the monster was too fast and clawed you"
					+ " before you could react. Lucky for you "
					+ game.getMonster().getName()
					+ " wasn't very hungry today, and they ran off. "
					+ "\n(You took "
					+ damage
					+ " damage from the strike)";
		}
		
		JOptionPane.showMessageDialog(cellarFrame, monsterText);
	}
	
	/**
	 * Tells the player that they need more levels to pick up an item
	 */
	
	public void tooManyItems(){
		JOptionPane.showMessageDialog(cellarFrame, "You can't carry that! "
							+ "To carry more protections, you'll need to gain "
							+ "more experience levels.");
	}
	
	/**
	 * Wins the game and terminates
	 */
	
	public void win(){
		JOptionPane.showMessageDialog(cellarFrame, "YOU WON THE GAME");
		cellarFrame.dispatchEvent(new WindowEvent(cellarFrame,WindowEvent.WINDOW_CLOSING));
	}
	
	/**
	 * Loses the game and terminates
	 */
	
	public void lose(){
		JOptionPane.showMessageDialog(cellarFrame, "YOU LOST. GET OUT OF MY GAME");
		cellarFrame.dispatchEvent(new WindowEvent(cellarFrame,WindowEvent.WINDOW_CLOSING));
	}
}



























