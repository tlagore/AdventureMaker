package TableModels;

import java.util.ArrayList;
import javax.swing.table.*;
import Scene_Manager.Scene;

/**
 * Model for Scene Tables.
 *
 * @author	Team Smart Water
 * @version v1.0 - Apr 8, 2014
 */
@SuppressWarnings( "serial" )
public class SceneTableModel extends AbstractTableModel
{ 
	// Private Variables
	private String[] headers = { "Name","Item Drops", "Item Unlocks","# Connected","Reachable" };
	private ArrayList<Scene> m_SceneGraph;
	
	// Constants
	private static final int NAME 				= 0;
	private static final int DROP 				= 1;
	private static final int UNLOCK				= 2;
	private static final int NUM_CONNECTIONS 	= 3;
	private static final int NO_PATH			= 4;
	
	/**
	 * Constructor - Takes in a starting array of scenes to set to the internal list.
	 * 
	 * @param sceneGraph	The List of scenes to set internally.
	 */
	public SceneTableModel ( ArrayList<Scene> sceneGraph)
	{
		m_SceneGraph = sceneGraph;
		fireTableDataChanged();
	}
	
	/**
	 * Sets a new sceneGraph to the internal scene graph.  Called when a new game is loaded.
	 * 
	 * @param sceneGraph	The new list to set the internal list to.
	 */
	public void setNewSceneGraph (ArrayList<Scene> sceneGraph )
	{ 
		m_SceneGraph = sceneGraph; 
		fireTableDataChanged();
	}
	
	/*******************************************************************\
	 * Functions Used by the Table									   *
	\*******************************************************************/
	@Override
	public String getColumnName(int column)		{ return headers[column]; }
	@Override
	public int getColumnCount() 				{ return headers.length;  }
	@Override
	public int getRowCount() 					{ return m_SceneGraph.size(); }
	@SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
	public Class getColumnClass(int arg0) 		{ return getValueAt(0, arg0).getClass(); }
	@Override
	public Object getValueAt(int row, int col) 
	{
		Scene indexedScene = null;
		Object returnObj = null;
		
		if (row >= 0 && row <= getRowCount())
		{
			indexedScene = m_SceneGraph.get(row);
			
			switch (col)
			{
			case NAME:
				returnObj = (Object) indexedScene.getTitle();
				break;
			case DROP:
				returnObj = (Object) indexedScene.getDropItem() == null ? "None" : 
					indexedScene.getDropItem().getName();
				break;
			case UNLOCK:
				returnObj = (Object) indexedScene.getUnlockItem() == null ? "None" :
							indexedScene.getUnlockItem().getName();
				break;
			case NUM_CONNECTIONS:
				returnObj = (Object) indexedScene.getConnections().size();
				break;
			case NO_PATH:
				returnObj = (Object) indexedScene.getSceneIsConnected();
				break;
			}
		}
		
		return returnObj;
	}
	/*******************************************************************\
	 * End - Functions Used by the Table							   *
	\*******************************************************************/
	
	/**
	 * Gets a scene from the specified index
	 * @param index 	The row in which the Scene is kept.
	 * @return Scene 	The indexed Scene.
	 */
	public Scene getSceneAt(int index)
	{
		return m_SceneGraph.get(index);
	}
}
