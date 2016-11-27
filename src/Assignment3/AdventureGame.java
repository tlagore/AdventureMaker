package Assignment3;

import Scene_Manager.SceneManager;

/**
 * Main Game function for the Adventure Game.
 *
 * @author	Team Smart Water
 * @version v1.0 - Apr 8, 2014
 */
public class AdventureGame {

	/**
	 * Main -- Create game system and run.
	 */
	public static void main( String[] args ) 
	{
		SceneManager gameSystem = new SceneManager();
		gameSystem.run();
	}

}
