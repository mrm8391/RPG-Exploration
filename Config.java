/*
 * Config.java
 * 
 * Version:
 * 		$Id: Config.java,v 1.5 2014/12/02 01:51:38 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Config.java,v $
 * 		Revision 1.5  2014/12/02 01:51:38  mrm8391
 * 		Finished up the rest of the display functions (refresh/dialogs) and fine tuned some of the gameplay
 * 		Also fixed some bugs
 *
 * 		Revision 1.4  2014/12/01 14:22:19  mrm8391
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
 * 		Revision 1.3  2014/11/29 06:02:34  mrm8391
 * 		Changed Item back from being Observable and implemented a different way to destroy items.
 * 		Added other skeleton (for now) classes such as player, trap, etc...
 *
 * 		Revision 1.2  2014/11/29 00:30:31  mrm8391
 * 		Changed Main class name to Cellar and changed error messages
 *
 * 		Revision 1.1  2014/11/28 23:11:15  mrm8391
 * 		Started and finished program entry point including dungeon and config classes
 * 		-Checks for proper parameters and config locations
 * 		-doesn't check for proper config formatting yet
 *
 */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Reads and validates config settings for game options. Also serves to
 * generate and store game data, which can be easily accessed.
 * 
 * @author Maximillian McMullen
 */
public class Config {

	
	private File configFile;
	
	/*
	 * Bunch of public properties for ease of access outside of class. 
	 * Some of them help with room/item generation locally in this class,
	 * but only the ones marked are needed outside of the class.
	 */
	public int numTraps;
	public Trap[] trapTypes;
	public int numRooms;
	public int amuletRoom;//needed
	public Room[] rooms;//needed
	public int numHallways;
	public int isMonster;
	public String monsterName;//needed
	public int monsterRoom;//needed
	
	public Config(File config){
		configFile=config;
		
		BufferedReader configReader;

		try{
			configReader=new BufferedReader(new FileReader(configFile));
			
			String currentLine="";
			
			/*
			 * since a very specific pattern is expected, this isn't going
			 * to be very object oriented (it's also very lazy programming
			 * that doesn't check for errors since I'm low on time...) 
			 */
			
			
			
			numTraps=Integer.parseInt(configReader.readLine());
			trapTypes=new Trap[numTraps];
			
			for(int i=0;i<numTraps;i++){
				ArrayList<String> words=words_on_line(configReader.readLine());
				
				String trapName=words.get(0);
				
				String trapType=words.get(1);
				
				int parameter=-1;
				//if there was a parameter specified...
				if(words.size()>=3)
					parameter=Integer.parseInt(words.get(2));
				
				//the last word is a protection
				String trapProtection="none";
				if(words.size()>=4)
					trapProtection=words.get(3);
				
				
				trapTypes[i]=new Trap(
						trapName,trapType,trapProtection,parameter);
			}
			
			//block here to keep temporary variables separate from important ones
			{
				ArrayList<String> nums=words_on_line(configReader.readLine());
				numRooms=Integer.parseInt(nums.get(0));
				amuletRoom=Integer.parseInt(nums.get(1));
			}
			
			rooms=new Room[numRooms];
			
			for(int i=0;i<rooms.length;i++){
				rooms[i]=new Room(
						getTrap(configReader.readLine()));
				
				ArrayList<String> protections=words_on_line(configReader.readLine());
				
				for(String protection:protections){
					if(protection.equals("none"))
						continue;
					if(protection.equalsIgnoreCase("sword"))
						rooms[i].addItem(
								new Item(protection,Item.SWORD));
					else
						rooms[i].addItem(
								new Item(protection,Item.PROTECTION));
				}
				
				if(i==amuletRoom)
					rooms[i].addItem(
							new Item("The Amulet",Item.AMULET));
			}
			
			numHallways=Integer.parseInt(configReader.readLine());
			
			for(int i=0;i<numHallways;i++){
				ArrayList<String> hallNums=words_on_line(configReader.readLine());
				
				Room room1=rooms[Integer.parseInt(hallNums.get(1))];
				Room room2=rooms[Integer.parseInt(hallNums.get(2))];
				
				Hallway hall=new Hallway(hallNums.get(0),room1,room2);
				
				//don't need to keep track of hallways since the rooms keep references to them
			}
			
			isMonster=Integer.parseInt(configReader.readLine());
			
			if(isMonster==1){
				ArrayList<String> monsterStuff=words_on_line(configReader.readLine());
				monsterName=monsterStuff.get(0);
				monsterRoom=Integer.parseInt(monsterStuff.get(1));
			}
			
			
			
		}catch(FileNotFoundException e){
			System.err.println("Error, config file not found. \n"
					+ "Make sure the path is correct and try again.");
			System.exit(1);
		}catch(IOException e){
			//will be useful later when doing actual reading
		}catch(Exception e){
			System.err.println("Program fucked up with the config, and it's"
					+ " probably the programmer's fault so get on it.");
			e.printStackTrace();
			System.exit(1);
		}
		
		if(!validate()){
			System.err.println("Wrong format in config file. Proper format:\n\n");
			System.err.println("Usage: java Cellar config_file_name");
			proper_format();
			System.exit(1);
		}
	}
	
	/**
	 * A helper function to find words on a line of words. Used
	 * to find options on initialization
	 */
	
	private ArrayList<String> words_on_line(String line){
		ArrayList<String> words=new ArrayList<String>();
		
		for(int i=0;i<line.length();i++){
			for(int h=i;h<=line.length();h++){
				if(h==line.length()||line.charAt(h)==' '){
					words.add(line.substring(i,h));
					i=h;
					break;
				}
			}
		}
		
		return words;
	}
	
	/**
	 * A helper function to find the right trap
	 * based on name.
	 * 
	 * @param trapName Trap name that is desired
	 * @return The trap, if it exists
	 */
	
	private Trap getTrap(String trapName){
		if(trapName.equals("none")) return null;
		
		for(Trap trap:trapTypes){
			if(trap.getName().equals(trapName)){
				return trap;
			}
		}
		
		return null;
	}
	
	
	
	/**
	 * Checks to see if the config has the proper formatting
	 * Might not even flesh this out if I don't have time
	 * 
	 * @return True if it does, false otherwise
	 */
	public boolean validate(){
		boolean isValid=true;
		
		//do stuff
		
		return isValid;
	}
	
	private void proper_format(){
		System.err.println("<number of trap instances>\n\n"
				+ "<trap name> <trap type> [<parameter for this trap type, optional>] <protection needed>\n"
				+ "(additional traps)\n\n"
				+ "<number of rooms> <number of the room containing The Amulet>\n\n"
				+ "(room 0 trap)  <trap name in this room, \"none\" if none>\n"
				+ "(room 0 items) <item1> <item2> ... or \"none\" if none\n"
				+ "(repeat for all rooms)\n\n"
				+ "<number of hallways>\n"
				+ "<name of hallway> <number of room at one end> <number of room at other end>\n"
				+ "(repeat for all hallways)\n\n"
				+ "<the number of monsters (either 0 or 1)>\n"
				+ "<name of The Monster> <room where The Monster starts> (only include if \"1\" for number of monsters)");
	}
}
