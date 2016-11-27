/**
 * 
 */
package Scene_Manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.*;

import javax.swing.ImageIcon;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

import Game_System.*;
import TableModels.ItemTableModel;
import TableModels.SceneTableModel;
import Windows.MainWindow;

/**
 * Name: SceneManager
 * 
 * Handles all scene related inquiries and maintains the scene graph and item list
 * for use by the main game.  Maintains the start scene, end scene, and current scene.
 * 
 *  For edit mode, the current scene and player remain unused.
 *
 * @author	Tyrone Lagore
 * @version v1.0 - Mar 25, 2014
 */
public class SceneManager 
{
	private MainWindow m_MainWindow;
	private ItemTableModel m_ItemTableModel;
	private SceneTableModel m_SceneTableModel;
	private ArrayList<Scene> m_SceneGraph;
	private ArrayList<Item> m_ItemList;
	private ArrayList<Scene> m_ScenesVisited;
	private ArrayList<SceneImage> m_Images;
	private Scene m_StartScene;
	private Scene m_CurrentScene;
	private Scene m_EndScene;
	private Player m_Player;
	private static final String m_ImageDocument = "images\\images.txt";

	
	public SceneManager()
	{
		m_StartScene = new Scene ("Beginning", "<Description>");
		m_EndScene = new Scene ("End", "<Description>");
		m_Images = new ArrayList<SceneImage>();
		
		m_SceneGraph = new ArrayList<Scene>();
		m_SceneGraph.add(m_StartScene);
		m_SceneGraph.add(m_EndScene);
		
		m_ItemList = new ArrayList<Item>();
		m_ItemTableModel = new ItemTableModel ( m_ItemList );
		m_SceneTableModel = new SceneTableModel ( m_SceneGraph );
		m_Player = new Player();
		
		m_ScenesVisited = new ArrayList<Scene>();
		
		m_MainWindow = new MainWindow(this);
		
		parseImages();
		updateSceneConnections();
	}
	
	public void run() 				{ m_MainWindow.run(); }

	// Returns a new blank scene.
	public Scene addScene() { return new Scene("<Enter a Unique Title>", "<Description>"); }
	
	/**
	 * Name: addVisitedScene
	 * 
	 * Adds a scene to the visited list.  Used in run mode.
	 * @param toAdd The scene to add
	 */
	public void addVisitedScene (Scene toAdd)
	{
		if (!m_ScenesVisited.contains(toAdd))
			m_ScenesVisited.add(toAdd);
	}
	
	/**
	 * Name: removeScene
	 * Purpose: Attempts to remove a given scene from the graph.  If it is the start scene
	 * 		or end scene, it is not removed, as these are protected scenes.
	 * 
	 * @param toRemove The scene for which removal is being requested
	 */
	public void removeScene (Scene toRemove)
	{
		if (toRemove != m_StartScene && toRemove != m_EndScene)
		{
			m_SceneGraph.remove(toRemove);
			updateSceneConnections();
			m_SceneTableModel.fireTableDataChanged();
		}
	}
	
	/**
	 * Attempts to save a scene into the graph.  If it is not saved due to a name conflict,
	 * the caller is informed via a boolean.
	 * 
	 * @param toSave The scene being requested to add to the list
	 * @return sceneAdded true if the scene was added or false if there was a name conflict
	 */
	public boolean saveScene(Scene toSave, String newTitle)
	{
		boolean sceneAdded = false;
		
		if (!isSceneNameConflict(toSave, newTitle))
		{
			toSave.setTitle(newTitle);
			if (!m_SceneGraph.contains(toSave))
				m_SceneGraph.add(toSave);
			sceneAdded = true;
			updateSceneConnections();
		}
		
		m_SceneTableModel.fireTableDataChanged();
		return sceneAdded;
	}
	
	/***
	 * Name: constains
	 * Purpose: checks to see if a given scene is already kept within the graph
	 * 
	 * @param checkScene The scene being checked if it is within the graph
	 * @return boolean True if the scene is contained within the graph, false if not.
	 */
	public boolean contains (Scene checkScene)		{	return m_SceneGraph.contains(checkScene); }
	

	/**
	 * Name: getNotes
	 * Purpose: Returns the entirety of the notes in a formatted string.
	 * 		Used for journal display while the game is running.
	 * 
	 * @return notes A formatted String containing all the notes
	 */
	public String getNotes()
	{
		String notes = "";
		for ( Scene o_Scene : m_ScenesVisited )
			if (o_Scene.getNote().length() > 0)
				notes += o_Scene.getTitle() + " - " + o_Scene.getNote() + "\n";
		
		return notes;
	}
	
	/**
	 * Name: getImageByName
	 * Purpose: Returns the ImageIcon kept within the list of ImageIcon's that 
	 * 		corresponds to that name.  It is assumed that the maker of the image
	 * 		file being read has not duplicated names.  If this happens, the last
	 * 		occurrence of the ImageIcon will be returned.
	 * 
	 * @param name The name of the ImageIcon being looked for
	 * @return toReturn The imageIcon associated with the name.  Null if none found.
	 */
	public SceneImage getImageByName(String name)
	{
		SceneImage toReturn = null;
		for (SceneImage o_SceneImage : m_Images )
			if (o_SceneImage.getImageName().matches(name))
				toReturn = o_SceneImage;
		
		return toReturn;
	}
	
	
	/* Getters */
	public ArrayList<SceneImage> getImages()	{ 	return m_Images;		  }
	public Player getPlayer()					{ 	return m_Player;		  }
	public ItemTableModel getItemModel () 		{	return m_ItemTableModel;  }
	public SceneTableModel getSceneModel ()		{	return m_SceneTableModel; }
	public Scene getStartScene()				{	return m_StartScene;	  }
	public Scene getEndScene()					{	return m_EndScene;		  }
	public Scene getCurrentScene()				{ 	return m_CurrentScene;	  }
	public ArrayList<Scene> getSceneGraph()		{ 	return m_SceneGraph;	  }
	public ArrayList<Scene> getVisitedScenes()	{	return m_ScenesVisited;	  }
	public ArrayList<Item>	getItemList()		{	return m_ItemList;		  }
	
	
	/**
	 * Validates that a chosen name for a scene does not already within the scene graph.
	 * if it is, the caller is informed. 
	 * @param name
	 * @return
	 */
	private boolean isSceneNameConflict(Scene scene, String newTitle)
	{
		boolean conflict = false;
		for (Scene o_Scene : m_SceneGraph)
		{
			if (o_Scene.getTitle().toLowerCase().equals(newTitle.toLowerCase())
			     && o_Scene != scene)
				conflict = true;
		}
		
		return conflict;
	}
	
	/**
	 * Name: updateSceneConnections
	 * Purpose: Updates the scenes connections to inform the user of nodes that are
	 * 		unreachable with the current scene connections.
	 */
	private void updateSceneConnections()
	{
		for (Scene o_CurrentScene : m_SceneGraph)
			o_CurrentScene.setSceneIsConnected(false);
		
		findConnections(m_StartScene);
	}
	
	/**
	 * Name: fineConnections
	 * Purpose: Traverses the graph from the given node, finding all connections to it
	 * 		and sets them as visited.
	 * 
	 *  - Recursive
	 * 
	 * @param root The Scene start node for the traversal
	 */
	public void findConnections(Scene root)
	{
		ArrayList<Scene> connections = root.getConnections();
		root.setSceneIsConnected(true);
		if (connections.size() == 0)
			return;
		else
			for (Scene o_Connection : connections)
				if (!o_Connection.getSceneIsConnected())
					findConnections(o_Connection);
	}
	
	/**
	 * Name: parseImages
	 * Purpose: Parses the images found within the file ~/images/images.txt
	 * 		Assumed to be in the format:
	 * 		
	 * 		"Unique Image Name" (Can be whatever the user chooses)
	 * 		"imageName.extension" (Must match the image exactly)
	 * 
	 * Creates a list of SceneImage's to be attached to each scene.
	 */
	public void parseImages()
	{
		Scanner inFile;
        try
        {
        	inFile = new Scanner( new File (m_ImageDocument) );
        	
        	while (inFile.hasNextLine())
        	{
        		String imageName = inFile.nextLine();
        		String fileName = "images\\" + inFile.nextLine();
        		
        		m_Images.add (new SceneImage(fileName, imageName));
        	}
        	
        }catch(Exception ex){}
	}
	
	/**
	 * Name: loadSceneManager
	 * Purpose: Loads a SceneManager from a specified file
	 * 
	 * @param fileName The name of the file to load from
	 */
	@SuppressWarnings("unchecked")
	public void loadSceneManager( String fileName ) 
	{
		String sInput = "";
		String sXmlSceneInput = "";
		String sXmlItemInput = "";
        try
        {
            XStream xstream = new XStream(new StaxDriver() );
            Scanner input = new Scanner( new File( fileName ) );
            
            while (input.hasNextLine() && !sInput.matches("\0\0\0r\0\0\0\n"))
            {
            	sXmlSceneInput += sInput;
            	sInput = input.nextLine() + "\n";
            }
            
            sXmlSceneInput.substring(0, sXmlSceneInput.length() - 1);
            
            while (input.hasNextLine())
            	sXmlItemInput += input.nextLine() + "\n";
            
            sXmlSceneInput.substring(0, sXmlSceneInput.length() - 1);
            
            input.close( );
            
            m_SceneGraph = ( ArrayList<Scene> ) xstream.fromXML(sXmlSceneInput);
            m_ItemList = (ArrayList<Item>) xstream.fromXML (sXmlItemInput);
            
    		m_StartScene = m_SceneGraph.get(0); // A SceneManager is instantiated with beginning and end
    		m_EndScene = m_SceneGraph.get(1);	// at 0 and 1.  These scenes are not removable.
            
            m_SceneTableModel.setNewSceneGraph(m_SceneGraph);
            m_ItemTableModel.setNewItemList(m_ItemList);

        }
        catch(Exception ex) {} 
	}
	
	/**
	 * Name: saveSceneManager
	 * Purpose: Attempts to save the SceneManager's details (SceneGraph and ItemList) to file.
	 * 
	 * @param fileName The name of the file to save to
	 * @return boolean True if the file saved, false if an exception was caught
	 */
	public boolean saveSceneManager( String fileName )
	{
		XStream xstream = new XStream( new StaxDriver() );
		PrintStream outFile;
		boolean saved = true;

        try
        {
            outFile = new PrintStream( new FileOutputStream( fileName ) );  
            outFile.println( xstream.toXML( m_SceneGraph ) );  
            
            outFile.println("\0\0\0r\0\0\0");
            outFile.print (xstream.toXML( m_ItemList ));
            outFile.close();
        }
        catch(Exception ex) { saved = false; }
        
        return saved;
	}

	/**
	 * Name: resetGame
	 * Purpose: Resets the game state for run through by clearing the players inventory, 
	 * 		scenes visited, and all notes attached to scenes.
	 */
	public void resetGame() 
	{
		m_Player.clearPlayerInventory();
		m_ScenesVisited.clear();
		for (Scene o_Scene : m_SceneGraph )
			o_Scene.setNote("");
	}
}
