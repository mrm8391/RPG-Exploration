/*
 * Cellar.java
 * 
 * Version:
 * 		$Id: Cellar.java,v 1.1 2014/11/29 00:30:32 mrm8391 Exp $
 * 
 * Revisions:
 * 		$Log: Cellar.java,v $
 * 		Revision 1.1  2014/11/29 00:30:32  mrm8391
 * 		Changed Main class name to Cellar and changed error messages
 *
 * 		Revision 1.1  2014/11/28 23:17:13  mrm8391
 * 		Made entry point separate from game to make code more clear.
 *
 */

import java.io.File;

/**
 * Entry point for game The Cellar
 * 
 * @author Maximillian McMullen
 */

public class Cellar {

	/**
	 * Initializes program from config class
	 * 
	 * @param args Specify the config file from args[0]
	 * 			   Not expecting any other parameters
	 */
	
	public static void main(String[] args) {
		//no config file specified
		if(args.length<1){
			System.err.println("Error, no input config specified. Proper startup:");
			System.err.println("Usage: java Cellar config_file_name");
			System.exit(1);
		}
		
		//gets data from config text file and validates it. terminates if invalid
		Config config=new Config(new File(args[0]));
		
		Dungeon cellar=new Dungeon(config);
	}

}
