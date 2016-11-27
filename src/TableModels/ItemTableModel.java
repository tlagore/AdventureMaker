package TableModels;

import java.util.ArrayList;
import javax.swing.table.*;
import Game_System.*;
import Scene_Manager.Scene;

/**
 * The Table Model for Items.  Contains a list of Items and monitors it using the
 * observer design pattern.
 *
 * @author	Team Smart Water
 * @version v1.0 - Apr 8, 2014
 */
@SuppressWarnings( "serial" )
public class ItemTableModel extends AbstractTableModel
{
	// private variables
	private String[] headers = { "Name","Drops In Scene", "Does Drop","Unlocks a Scene" };
	private ArrayList<Item> m_ItemList;
	
	// Constant Variables
	private static final int NAME 			= 0;
	private static final int DROPS_WHERE	= 1;
	private static final int DROPS 			= 2;
	private static final int UNLOCKS 		= 3;
	
	/**
	 * Constructor - Initializes the Internal Item list.
	 * 
	 * @param itemList	The item list to set the internal list to.
	 */
	public ItemTableModel( ArrayList<Item> itemList)
	{
		m_ItemList = itemList;
	}

	/**
	 * Adds a provided item to the item list and fires changes.
	 * 
	 * @param newItem	The Item to add to the model.
	 */
	public void addItem( Item newItem )
	{
		m_ItemList.add( newItem );
		fireTableDataChanged( );
	}
	
	/**
	 * Removes an Item from the internal list.  Also removes the desired item from
	 * the scenes it's connected to.
	 * 
	 * @param sItemName	The name of the item to remove.
	 */
	public void removeItem( String sItemName )
	{
		Item m_ItemToRemove = null;
		
		for( Item iIndex : m_ItemList )
			if( iIndex.getName( ).equals( sItemName ) )
				m_ItemToRemove = iIndex;
		
		if( null != m_ItemToRemove )
		{
			if( m_ItemToRemove.dropsInAScene( ) )
				m_ItemToRemove.getDropScene( ).removeDropItem( );
			
			if( m_ItemToRemove.unlocksAScene( ) )
				m_ItemToRemove.getUnlockScene( ).removeUnlockItem( );
			
			m_ItemList.remove( m_ItemToRemove );
		}
		
		fireTableDataChanged( );
	}

	/**
	 * Function to determine if a name passed in (String) is a unique name
	 * in the list of items.
	 * 
	 * @param sItemName	The Name to check for.
	 * @return			Returns true if the name is unique; otherwise, false.
	 */
	public boolean isNameUnique( String sItemName )
	{
		boolean bNameIsUnique = true;
		
		for( Item iIndex : m_ItemList )
			bNameIsUnique &= !iIndex.getName( ).equals( sItemName );
		
		return bNameIsUnique;
	}
	
	/** 
	 * Returns the Item at the specified Index.
	 * 
	 * @param iIndex	The index of the item to grab.
	 * @return			Returns null if the index is out of bounds.  Otherwise returns
	 * 					the Item at the specified Index.
	 */
	public Item getItemAt( int iIndex )
	{
		Item m_ReturnItem = null;
		
		if( iIndex >= 0 && iIndex < getRowCount( ) )
			m_ReturnItem = m_ItemList.get( iIndex );
		
		return m_ReturnItem;
	}

	/**
	 * Sets a new item list.  Called when a new game is loaded.
	 * 
	 * @param itemList	The item list to set internally.
	 */
	public void setNewItemList(ArrayList<Item> itemList)
	{
		m_ItemList = itemList ;
		fireTableDataChanged();
	}
	
	/*******************************************************************\
	 * Functions Used by the Table									   *
	\*******************************************************************/
	@Override
	public String getColumnName( int iColumn )
	{
		return headers[iColumn];
	}
	
	@Override
	public boolean isCellEditable( int rowIndex, int columnIndex )
	{
		return false;
	}
	
	@Override
	public int getColumnCount() {
		return headers.length;
	}

	@Override
	public int getRowCount() {
		return m_ItemList.size( );
	}
	
	@SuppressWarnings( { "rawtypes", "unchecked" } )
    @Override
	public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) 
	{
		Item aIndexedItem = null;
		Object oReturnObj = null;
		
		if( rowIndex >= 0 && rowIndex < getRowCount( ) )
		{
			aIndexedItem = m_ItemList.get( rowIndex );
			
			switch( columnIndex )
			{
			case NAME:
				oReturnObj = (Object) aIndexedItem.getName( );
				break;
			case DROPS_WHERE:
				Scene sPlaceDropped = aIndexedItem.getDropScene();
				oReturnObj = (Object) sPlaceDropped == null ? "None" :
						sPlaceDropped.getTitle();
				break;
			case DROPS:
				oReturnObj = (Object) aIndexedItem.dropsInAScene( );
				break;
			case UNLOCKS:
				oReturnObj = (Object) aIndexedItem.unlocksAScene( );
				break;
			default:
				break;
			};
		}
		 
		return oReturnObj;
	}
	
	/*******************************************************************\
	 * End - Functions Used by the Table							   *
	\*******************************************************************/

}
