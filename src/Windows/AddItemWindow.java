package Windows;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import Game_System.Item;
import TableModels.ItemTableModel;
import UserIO.WindowComm;
import java.awt.Choice;
import java.awt.Font;
/**
 * Name: AddItemWindow
 * Purpose: Handles the connecting of items to the scene that is currently being edited.
 * 		A scene may hold a single item that can be dropped on it, and a single item may be
 * 		required to unlock it.  These may not be the same item for a single scene.  An item
 * 		may only be dropped on one scene.
 * 
 * Known bugs:
 * 		None
 * 
 * Assumptions: Error checking for the addition of items to the scene is done outside of 
 * 		this window.  This window is simply a tool to return a selected item to the EditSceneWindow.
 * 		The error message for a given error, however, is displayed within this window.
 *
 * 
 * @author Tyrone Lagore
 * @version April 5, 2014
 */
@SuppressWarnings( "serial" )
public class AddItemWindow extends JFrame
{
	// Private Variables
	private WindowComm m_WindowComm;
	private EditSceneWindow m_Parent;
	private JList <Item>m_ItemList;
	private JScrollPane m_ScenesScrollPane;
	private JButton btnCancel;
	private DefaultListModel<Item> m_ItemListModel;
	private JButton btnConnectItem;
	private Choice m_ItemTypeSelection;
	
	/**
	 * Button Handler Class for handling button actions
	 */
	public class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource().equals(btnConnectItem))
				connectItemButtonClicked();
			else if (e.getSource().equals(btnCancel))
				closeWindow();
		}
	}
	
	
	/**
	 * AddItemWindow constructor - Initializes private variables and AddItemWindow functionality.
	 * 
	 * @param itemTable	ItemTableModel to load the List with.
	 * @param parent	The parent window to reference for setting position and bounds.
	 */
	public AddItemWindow( ItemTableModel itemTable, EditSceneWindow parent)
	{
		m_WindowComm = new WindowComm(this);
		getContentPane().setLayout(null);
		ButtonHandler btnHandler = new ButtonHandler();

		m_Parent = parent;
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
		
		setBounds( 100, 100, 390, 552 );
		Point parentLocation = m_Parent.getLocation();
		double parentX = parentLocation.getX();
		double parentY = parentLocation.getY();
		
		setLocation((int)(parentX + 125),(int)(parentY + 125));
		
		m_ItemListModel = new DefaultListModel<Item>();
		
		for (int i = 0; i < itemTable.getRowCount(); i++)
			m_ItemListModel.addElement(itemTable.getItemAt(i));
		
		m_ScenesScrollPane = new JScrollPane();
		m_ScenesScrollPane.setBounds(10, 54, 178, 415);
		getContentPane().add(m_ScenesScrollPane);
		
		m_ItemList = new JList<Item>();
		m_ItemList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_ItemList.setModel(m_ItemListModel);
		m_ScenesScrollPane.setViewportView(m_ItemList);
		
		btnConnectItem = new JButton("Connect Item");
		btnConnectItem.addActionListener(btnHandler);
		btnConnectItem.setBounds(198, 54, 166, 23);
		getContentPane().add(btnConnectItem);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(btnHandler);
		btnCancel.setBounds(198, 93, 166, 23);
		getContentPane().add(btnCancel);
		
		m_ItemTypeSelection = new Choice();
		m_ItemTypeSelection.setBounds(198, 132, 166, 20);
		getContentPane().add(m_ItemTypeSelection);
		
		JLabel lblConnectItemTo = new JLabel("Connect Item to Scene");
		lblConnectItemTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblConnectItemTo.setHorizontalAlignment(SwingConstants.CENTER);
		lblConnectItemTo.setBounds(10, 11, 354, 14);
		getContentPane().add(lblConnectItemTo);
		
		m_ItemTypeSelection.add("Item drops on this scene");
		m_ItemTypeSelection.add("Item unlocks this scene");
	}
	
	/**
	 * Functionality for the connectItemButton.
	 */
	public void connectItemButtonClicked() 
	{
		int connectFlag;
		Item toConnect = m_ItemList.getSelectedValue();
		if (toConnect != null)
		{
			connectFlag = m_Parent.connectItem(toConnect, m_ItemTypeSelection.getSelectedItem());
			switch (connectFlag)
			{
			case 0:
				closeWindow();
				break;
			case 1:
				m_WindowComm.displayMessage("That item is already dropped somewhere else.");
				break;
			case 2:
				m_WindowComm.displayMessage("You can't have an item drop on the scene unlocked by it.");
				break;
			}
		}
	}
	
	/**
	 * Closes the Window and disposes it to avoid memory leaks.
	 */
	private void closeWindow()
	{
		m_Parent.addItemWidnowHasClosed();
		dispose();
	}
	
	/**
	 * Runs the program.
	 */
	public void run()
	{
		EventQueue.invokeLater(new Runnable() 
		{
			public void run() 
			{
				try 
				{
					setVisible(true);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		});
	}
}
