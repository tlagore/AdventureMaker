package Panels;

import javax.swing.*;
import Game_System.Item;
import Scene_Manager.SceneManager;
import TableModels.ItemTableModel;
import UserIO.WindowComm;
import Windows.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;

/**
 * Panel used for monitoring/managing Items.
 *
 * @author	Team Smart Water
 * @version v1.0 - Mar 25, 2014
 */
@SuppressWarnings( "serial" )
public class ItemManagerPanel extends JPanel implements MouseMotionListener {
	/* Private Variables */
	private JTable 			m_ItemTable;
	private JButton 		m_AddItmBtn;
	private JButton 		m_RmvItmBtn;
	private JButton 		m_EdtItmBtn;
	private JScrollPane 	scrollPane;
	private WindowComm 		m_WindowComm;
	private ItemTableModel 	m_ItemTableModel;
	private JTextField 		m_ItemTitleTextField;
	private JTextArea 		m_ItemDescriptionTextArea;
	private JTextField 		m_ItemUnlocksTextField;
	private JTextField 		m_ItemDropsTextField;
	private JLabel 			lblItemDropsAt;
	
	// Constants
	private static final int YES	= 0;
	
	/**
	 * Button Handler for Item Manager Panel.
	 */
	public class ItemManagerButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource().equals(m_RmvItmBtn))			 { removeItem( ); }
			else if( e.getSource( ).equals( m_AddItmBtn ) )	 { addItem( );	  }
			else if( e.getSource( ).equals( m_EdtItmBtn ) )  { editItem( );	  }
		}
	}

	/**
	 * Create the ItemManagerPanel.
	 * 
	 * @param itemsTable	The Model to load the Item Table with.
	 * @param mWindow		The parent window.
	 * @param sManager		The SceneManager to load the Item List from if a new Game is being loaded.
	 */
	public ItemManagerPanel( ItemTableModel itemsTable, MainWindow mWindow, SceneManager sManager )
	{
		ItemManagerButtonHandler btnHandler = new ItemManagerButtonHandler( );
		m_WindowComm = new WindowComm(mWindow);
		m_ItemTableModel = itemsTable;
		
		m_AddItmBtn = new JButton("Add");
		m_AddItmBtn.setBounds(456, 8, 103, 23);
		m_AddItmBtn.addActionListener(btnHandler);
		setLayout(null);
		add(m_AddItmBtn);

		m_RmvItmBtn = new JButton("Remove");
		m_RmvItmBtn.setBounds(456, 41, 103, 23);
		m_RmvItmBtn.addActionListener(btnHandler);
		add(m_RmvItmBtn);
		
		m_EdtItmBtn = new JButton("Edit");
		m_EdtItmBtn.setToolTipText("Edit the selected scene.");
		m_EdtItmBtn.setBounds(456, 75, 103, 23);
		m_EdtItmBtn.addActionListener(btnHandler);
		add(m_EdtItmBtn);
		
		m_ItemTableModel = itemsTable;
		m_ItemTable = new JTable (m_ItemTableModel);
		m_ItemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_ItemTable.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		m_ItemTable.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		m_ItemTable.setFillsViewportHeight(true);
		m_ItemTable.setColumnSelectionAllowed(false);
		m_ItemTable.addMouseMotionListener(this);
		
		scrollPane = new JScrollPane(m_ItemTable);
		scrollPane.setBounds(10, 11, 436, 432);
		add(scrollPane);
		scrollPane.setViewportView(m_ItemTable);	
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(null, "Item Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(569, 11, 202, 359);
		add(panel);
		
		m_ItemTitleTextField = new JTextField();
		m_ItemTitleTextField.setEditable(false);
		m_ItemTitleTextField.setColumns(10);
		m_ItemTitleTextField.setBackground(SystemColor.menu);
		m_ItemTitleTextField.setBounds(16, 45, 170, 23);
		panel.add(m_ItemTitleTextField);
		
		JLabel lblItemTitle = new JLabel("Item Name");
		lblItemTitle.setBounds(16, 29, 84, 14);
		panel.add(lblItemTitle);
		
		m_ItemDescriptionTextArea = new JTextArea();
		m_ItemDescriptionTextArea.setWrapStyleWord(true);
		m_ItemDescriptionTextArea.setLineWrap(true);
		m_ItemDescriptionTextArea.setEditable(false);
		m_ItemDescriptionTextArea.setColumns(10);
		m_ItemDescriptionTextArea.setBackground(SystemColor.menu);
		m_ItemDescriptionTextArea.setBounds(16, 93, 170, 145);
		panel.add(m_ItemDescriptionTextArea);
		
		JLabel m_DescriptionLabel = new JLabel("Description");
		m_DescriptionLabel.setBounds(16, 79, 84, 14);
		panel.add(m_DescriptionLabel);
		
		m_ItemUnlocksTextField = new JTextField();
		m_ItemUnlocksTextField.setEditable(false);
		m_ItemUnlocksTextField.setBackground(SystemColor.menu);
		m_ItemUnlocksTextField.setBounds(16, 270, 170, 20);
		panel.add(m_ItemUnlocksTextField);
		
		JLabel lblItemUnlocks = new JLabel("Item Unlocks");
		lblItemUnlocks.setBounds(16, 249, 170, 14);
		panel.add(lblItemUnlocks);
		
		m_ItemDropsTextField = new JTextField();
		m_ItemDropsTextField.setEditable(false);
		m_ItemDropsTextField.setColumns(10);
		m_ItemDropsTextField.setBackground(SystemColor.menu);
		m_ItemDropsTextField.setBounds(14, 322, 172, 20);
		panel.add(m_ItemDropsTextField);
		
		lblItemDropsAt = new JLabel("Item Drops At");
		lblItemDropsAt.setBounds(16, 301, 170, 14);
		panel.add(lblItemDropsAt);
	}
	
	
	/*********************************************************************\
	 * Contains all functionality of the Item Manager Panel				 *
	\*********************************************************************/
	
	/**
	 * Gets the currently selected Item.
	 * 
	 * @return	The Currently Selected Item in the table.
	 */
	private Item getSelectedItem( )
	{
		return m_ItemTableModel.getItemAt( m_ItemTable.getSelectedRow( ) );
	}
	
	/** 
	 * Generates a new item and opens the edit item window to modify the item.
	 */
	private void addItem( )
	{
		Item m_NewItem = new Item( "<Item Title>", "<Item Description>" );
		openEditItemWindow( m_NewItem, true );
	}
	
	/**
	 * After prompting the user, removes the item and disconnects it from associated scenes.
	 */
	private void removeItem( )
	{
		Item m_ItemToRemove = getSelectedItem( );
		
		if( null != m_ItemToRemove )
		{
			int iResult = m_WindowComm.getYesNo( "Are you sure you want to remove \"" + 
												 m_ItemToRemove.getName( ) + "?\"", "Remove" );
			
			if( YES == iResult )				
				m_ItemTableModel.removeItem( m_ItemToRemove.getName( ) );
		}
		else
			m_WindowComm.displayMessage( "No Item selected!" );
	}
	
	/**
	 * Opens up the EditItemWindow with a flag to say that it's not a new item being added.
	 * This allows the user to modify the Item with the ability to leave the name the same.
	 * If no item is selected, this function fails.
	 */
	private void editItem( )
	{
		Item m_ItemToEdit = m_ItemTableModel.getItemAt( m_ItemTable.getSelectedRow( ) );
		
		if( null != m_ItemToEdit )
			openEditItemWindow( m_ItemToEdit, false );
		else
			m_WindowComm.displayMessage( "No Item selected!" );
	}
	
	/**
	 * Function called externally to save an item.
	 * If the name is unique, it will skip adding the item and just fire
	 * changes on the model.
	 * 
	 * @param itemToSave The Item to save.
	 */
	public void saveItem( Item itemToSave )
	{
		if( m_ItemTableModel.isNameUnique( itemToSave.getName( ) ) )
			m_ItemTableModel.addItem( itemToSave );
		else
			m_ItemTableModel.fireTableDataChanged( );
	}
	
	/**
	 * Function to check to see if an Item can be added.
	 * 
	 * @param m_ItemToAdd	The Item to check.
	 * @return				True if the name of the item to add is unique.
	 */
	public boolean canAddItem( Item m_ItemToAdd )
	{
		return m_ItemTableModel.isNameUnique( m_ItemToAdd.getName( ) );
	}
	
	/**
	 * Function to launch and run the edit item window.  This also sets
	 * the buttons of this window to disabled so they can't be clicked again.
	 * 
	 * @param itemToEdit	The Item to load the edit item window with.
	 * @param bNewItem		Flag to determine if the Item is a new item or not.  This
	 * 						changes how the edit window handles the saving of the item.
	 */
	private void openEditItemWindow( Item itemToEdit, boolean bNewItem )
	{
		toggleSaveEditEnabled(false);
		EditItemWindow esw = new EditItemWindow ( itemToEdit, this, bNewItem );
		esw.run();
	}
	
	/**
	 * Essentially sets the buttons to be enabled
	 */
	public void editItemWindowHasClosed()
	{
		toggleSaveEditEnabled(true);
	}
	
	/** 
	 * Enables or Disables the buttons depending on the boolean flag passed in.
	 * 
	 * @param b_Toggle	The Boolean flag to set the buttons with.
	 */
	private void toggleSaveEditEnabled(boolean b_Toggle)
	{
		m_AddItmBtn.setEnabled(b_Toggle);
		m_RmvItmBtn.setEnabled(b_Toggle);
		m_EdtItmBtn.setEnabled(b_Toggle);
	}	

	/**
	 * Mouse Event that triggers when a mouse is clicked and dragged.
	 * -- NOT IMPLEMENTED --
	 */
	@Override
	public void mouseDragged(MouseEvent arg0) { }

	/**
	 * Functionality that loads up side-inspection with the item that the mouse is over.
	 */
	@Override
	public void mouseMoved(MouseEvent event) 
	{
		Point p = event.getPoint();
		int row = m_ItemTable.rowAtPoint(p);
		if (row != -1)
		{
			Item mouseOverItem = m_ItemTableModel.getItemAt(row);
			m_ItemTitleTextField.setText(mouseOverItem.getName());
			m_ItemDescriptionTextArea.setText(mouseOverItem.getDesc());
			m_ItemDropsTextField.setText(mouseOverItem.getDropScene() != null ? mouseOverItem.getDropScene().getTitle() : "None");
			m_ItemUnlocksTextField.setText(mouseOverItem.getUnlockScene() != null ? mouseOverItem.getUnlockScene().getTitle() : "None");
		}else
			clearSceneFields();
	}
	
	/**
	 * Function to clear all the fields on the side-inspection.
	 */
	private void clearSceneFields()
	{
		m_ItemTitleTextField.setText("");
		m_ItemDescriptionTextArea.setText("");
		m_ItemDropsTextField.setText("");
		m_ItemUnlocksTextField.setText("");
	}
}
