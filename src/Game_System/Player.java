/**
 * 
 */
package Game_System;

import java.util.*;

/**
 * Description
 *
 * @author James C. Cote
 * @author	Tyrone Lagore
 * @version v1.0 - Mar 25, 2014
 */
public class Player 
{
	private ArrayList<Item> m_Inventory;
	
	public Player() {	m_Inventory = new ArrayList<Item>();	}

	/**
	 * Adds an item to the players inventory if they do not already have it.
	 * @param toAdd The item to add
	 */
	public void addItem( Item toAdd )
	{   
		if (!m_Inventory.contains(toAdd))
			m_Inventory.add(toAdd); 	
	}
	
	/* Getters/Setters */
	public ArrayList<Item> getInventory() 	{	return m_Inventory;			}		
	public boolean inventoryContains( Item itemCheck )	
											{	return m_Inventory.contains(itemCheck); }
	
	/*  Clears the players inventory for a fresh run through of the game. */
	public void clearPlayerInventory()		{	m_Inventory.clear();		}
}
