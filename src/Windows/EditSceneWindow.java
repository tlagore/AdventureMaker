package Windows;

import java.awt.Choice;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import Game_System.Item;
import Panels.SceneManagerPanel;
import Scene_Manager.Scene;
import Scene_Manager.SceneImage;
import Scene_Manager.SceneManager;
import UserIO.WindowComm;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.ScrollPaneConstants;
import javax.swing.ListSelectionModel;

import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

/**
 * EditSceneWindow
 * Allows for the editing of an individual scene, passed in upon creation.
 * 
 * @author Tyrone Lagore
 * @version April 4, 2014
 */

@SuppressWarnings("serial")
public class EditSceneWindow extends JFrame {

	private Scene m_Scene;
	private SceneManagerPanel m_Parent;
	private WindowComm m_WindowComm;
	private JLabel lblLargeTitle;
	private JButton btnSaveScene;
	private JButton btnRemoveConnection;
	private JButton btnConnectScene;
	private JLabel lblDescription;
	private JLabel lblConnectedScenes;
	private JTextArea m_DescTextArea;
	private JScrollPane m_DescScrollPane;
	private JScrollPane m_ConnectedScenesScrollPane;
	private JList <Scene>m_ConnectedScenesJList;
	private DefaultListModel<Scene> m_ConnectedScenesModel;
	private JLabel lblTitle;
	private JTextField m_TitleTextField;
	private JTextField []m_ConnectionLabels;
	private ArrayList<Scene> m_Connections;
	private JLabel lblConnectionLabel;
	private JTextField m_ItemDropsTextField;
	private JTextField m_ItemUnlocksTextField;
	private JButton btnRmvDropItem;
	private JLabel lblItemRequiredTo;
	private SceneManager m_SceneManager;
	private JButton btnAddItem;
	private JButton btnRmvUnlockItem;
	private Choice m_ImageOnScene;
	
	private static final String m_UnlockString = "Item unlocks this scene";
	@SuppressWarnings("unused")
	private static final String m_DropString = "Item drops on this scene";
	
	private static final int NUM_JLABELS = 4;
	private JLabel lblStartOrEnd;
	private JLabel m_ImageLabel;
	
	
	/**
	 * Handles all button interaction for this window
	 */
	public class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if( e.getSource( ).equals( btnAddItem ) )
				addItemButtonClicked();
			else if( e.getSource( ).equals( btnConnectScene ) )
				connectSceneButtonClicked();
			else if (e.getSource().equals(btnRemoveConnection))
				removeConnectionButtonClicked();
			else if (e.getSource().equals(btnRmvDropItem))
				removeDropItemButtonClicked();
			else if (e.getSource().equals(btnRmvUnlockItem))
				removeUnlockItemButtonClicked();
			else if (e.getSource().equals(btnSaveScene))
				saveScene();
				
		}
	}

	/**
	 * Handles all item interaction for this scene
	 */
	public class ItemHandler implements ItemListener 
	{
		@Override
		public void itemStateChanged(ItemEvent e) 
		{
			if (e.getSource().equals(m_ImageOnScene))
				setImageToSelected();
		}
	}
	
	/**
	 * Create the application.
	 */
	public EditSceneWindow( Scene scene, SceneManagerPanel parent, SceneManager sceneManager)
	{
		m_WindowComm = new WindowComm(this);
		m_SceneManager = sceneManager;

		m_Connections = scene.getConnections();

		m_Parent = parent;
		m_Scene = scene;
		initialize( );
	}
	
	/**
	 *  Runs the window
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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize( )
	{
		getContentPane().setLayout(null);
		
		m_ConnectionLabels = new JTextField[NUM_JLABELS];
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				closeWindow();
			}
		});
		
		ButtonHandler btnHandler = new ButtonHandler();
		ItemHandler itemHandler = new ItemHandler();
		
		setBounds( 100, 100, 830, 548 );
		Point parentLocation = m_Parent.getLocation();
		double parentX = parentLocation.getX();
		double parentY = parentLocation.getY();
		
		setLocation((int)(parentX + 125),(int)(parentY + 125));
		
		int x = 563;
		int y = 333;
		int height = 20;
		int width = 238;
		
		// Used to allow for individual manipulation of text fields for variable number
		// of connected scenes
		for (int i = 0; i < NUM_JLABELS; i++)
		{
			m_ConnectionLabels[i] = new JTextField();
			m_ConnectionLabels[i].setBounds(x, y, width, height);
			getContentPane().add(m_ConnectionLabels[i]);
			m_ConnectionLabels[i].setEditable(false);
			y+=25;
		}
		
		m_TitleTextField = new JTextField();
		m_TitleTextField.setText(m_Scene.getTitle());
		m_TitleTextField.setBounds(10, 50, 288, 27);
		getContentPane().add(m_TitleTextField);
		m_TitleTextField.setColumns(10);
		
		lblTitle = new JLabel("Title");
		lblTitle.setBounds(10, 35, 46, 14);
		getContentPane().add(lblTitle);
		
		m_ConnectedScenesScrollPane = new JScrollPane();
		m_ConnectedScenesScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		m_ConnectedScenesScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		m_ConnectedScenesScrollPane.setBounds(346, 333, 208, 98);
		getContentPane().add(m_ConnectedScenesScrollPane);
		
		m_ConnectedScenesModel = new DefaultListModel<Scene>();
		m_ConnectedScenesJList = new JList<Scene>();
		m_ConnectedScenesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_ConnectedScenesJList.setFixedCellHeight(22);
		m_ConnectedScenesJList.setModel(m_ConnectedScenesModel);
		m_ConnectedScenesScrollPane.setViewportView(m_ConnectedScenesJList);
		
		populateConnectedScenes();
		
		m_DescScrollPane = new JScrollPane();
		m_DescScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		m_DescScrollPane.setBounds(346, 162, 440, 117);
		getContentPane().add(m_DescScrollPane);
		
		m_DescTextArea = new JTextArea();
		m_DescTextArea.setWrapStyleWord(true);
		m_DescTextArea.setLineWrap(true);
		m_DescTextArea.setText(m_Scene.getDesc());
		m_DescScrollPane.setViewportView(m_DescTextArea);
		
		lblConnectedScenes = new JLabel("Connected Scenes");
		lblConnectedScenes.setBounds(349, 308, 109, 14);
		getContentPane().add(lblConnectedScenes);
		
		lblDescription = new JLabel("Description");
		lblDescription.setBounds(349, 137, 109, 14);
		getContentPane().add(lblDescription);
		
		btnConnectScene = new JButton("Connect A Scene");
		btnConnectScene.addActionListener(btnHandler);
		btnConnectScene.setBounds(345, 442, 209, 23);
		getContentPane().add(btnConnectScene);
		
		btnRemoveConnection = new JButton("Remove Connection");
		btnRemoveConnection.addActionListener(btnHandler);
		btnRemoveConnection.setBounds(345, 476, 209, 23);
		getContentPane().add(btnRemoveConnection);
		
		btnSaveScene = new JButton("Save Scene");
		btnSaveScene.addActionListener(btnHandler);
		btnSaveScene.setBounds(592, 476, 209, 23);
		getContentPane().add(btnSaveScene);
		
		lblLargeTitle = new JLabel("");
		lblLargeTitle.setText(m_Scene.getTitle());
		lblLargeTitle.setHorizontalAlignment(SwingConstants.CENTER);
		lblLargeTitle.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLargeTitle.setBounds(10, 11, 794, 27);
		getContentPane().add(lblLargeTitle);
		
		lblConnectionLabel = new JLabel("Connection Label");
		lblConnectionLabel.setBounds(571, 308, 196, 14);
		getContentPane().add(lblConnectionLabel);
		
		JLabel lblItemConnectedTo = new JLabel("Item dropped on scene\r\n");
		lblItemConnectedTo.setBounds(578, 35, 153, 14);
		getContentPane().add(lblItemConnectedTo);
		
		btnRmvUnlockItem = new JButton("X\r\n");
		btnRmvUnlockItem.setFont(new Font("Courier New", Font.PLAIN, 10));
		btnRmvUnlockItem.addActionListener(btnHandler);
		btnRmvUnlockItem.setBounds(760, 94, 45, 23);
		getContentPane().add(btnRmvUnlockItem);
		btnRmvUnlockItem.setEnabled(false);
		
		btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(btnHandler);
		btnAddItem.setBounds(592, 442, 209, 23);
		getContentPane().add(btnAddItem);
		
		m_ItemDropsTextField = new JTextField();
		m_ItemDropsTextField.setBounds(578, 50, 173, 23);
		getContentPane().add(m_ItemDropsTextField);
		m_ItemDropsTextField.setColumns(10);
		m_ItemDropsTextField.setEditable(false);
		
		m_ItemUnlocksTextField = new JTextField();
		m_ItemUnlocksTextField.setColumns(10);
		m_ItemUnlocksTextField.setBounds(578, 94, 173, 23);
		getContentPane().add(m_ItemUnlocksTextField);
		m_ItemUnlocksTextField.setEditable(false);
		
		btnRmvDropItem = new JButton("X");
		btnRmvDropItem.setFont(new Font("Courier New", Font.PLAIN, 10));
		btnRmvDropItem.addActionListener(btnHandler);
		btnRmvDropItem.setBounds(760, 50, 45, 23);
		getContentPane().add(btnRmvDropItem);
		
		btnRmvDropItem.setEnabled(false);
		
		lblItemRequiredTo = new JLabel("Item required to enter scene");
		lblItemRequiredTo.setBounds(578, 79, 208, 14);
		getContentPane().add(lblItemRequiredTo);
		
		lblStartOrEnd = new JLabel("");
		lblStartOrEnd.setHorizontalAlignment(SwingConstants.CENTER);
		lblStartOrEnd.setBounds(308, 63, 208, 14);
		getContentPane().add(lblStartOrEnd);
		
		m_ImageLabel = new JLabel();
		m_ImageLabel.setBounds(10, 137, 325, 362);
		
		getContentPane().add(m_ImageLabel);
		
		
		m_ImageOnScene = new Choice();
		m_ImageOnScene.addItemListener(itemHandler);
		m_ImageOnScene.setBounds(10, 112, 210, 20);
		getContentPane().add(m_ImageOnScene);	
		
		JLabel lblSelectAnImage = new JLabel("Scene Image");
		lblSelectAnImage.setBounds(10, 88, 228, 14);
		getContentPane().add(lblSelectAnImage);
		
		for (SceneImage o_SceneImage : m_SceneManager.getImages())
			m_ImageOnScene.add(o_SceneImage.getImageName());
		
		
		// If there is no image connected to the selected image and the image file was not empty
		// set the image to the first image within the image list.
		if (m_SceneManager.getImages().size() > 0 && m_Scene.getImage() == null )
		{
			m_ImageLabel.setIcon(m_SceneManager.getImages().get(0).getImage());
			m_Scene.addImageToScene(m_SceneManager.getImages().get(0));
		}else if (m_Scene.getImage() != null)
		{
			m_ImageLabel.setIcon(m_SceneManager.getImageByName(m_Scene.getImage()).getImage());
			m_ImageOnScene.select(m_Scene.getImage());
		}

		
		
		if (m_Scene.getUnlockItem() != null)
		{
			//If there is an item on the scene already, set the visible text of it
			m_ItemUnlocksTextField.setText(m_Scene.getUnlockItem().getName());
			btnRmvUnlockItem.setEnabled(true);
		}
		
		if (m_Scene.getDropItem() != null)
		{
			m_ItemDropsTextField.setText(m_Scene.getDropItem().getName());
			btnRmvDropItem.setEnabled(true);
		}
		
		
		/* End and beginning scenes may not be removed, but they may be eddited */
		if (m_Scene.equals(m_SceneManager.getEndScene()))
		{
			lblStartOrEnd.setText("End Scene");
			lblStartOrEnd.setToolTipText("This scene is the end scene.  It must be reachable from the beginning scene to play.");
		}
		else if (m_Scene.equals(m_SceneManager.getStartScene()))
		{
			lblStartOrEnd.setText("Beginning Scene");
			lblStartOrEnd.setToolTipText("This scene is the beginning scene.  It must eventually lead to the end scene to play.");
		}
		else
			lblStartOrEnd.setVisible(false);
		
		m_TitleTextField.grabFocus();
		m_TitleTextField.selectAll();
		

	}
	
	/**
	 * Name: populateConnectedScenes
	 * Purpose: When new scenes are connected or removed from the current scene, the JList 
	 * 		displaying the connections is updated to show it and the corresponding labels that
	 * 		lead to the scenes are made to be editable.
	 * 
	 * 
	 */
	private void populateConnectedScenes()
	{	
		ArrayList<String> connectionLabels = m_Scene.getConnectionLabels();
		int i = 0;
		
		//Remove all instances from model
		m_ConnectedScenesModel.removeAllElements();
		
		//Add only the ones found within the connections
		for (Scene o_Scene : m_Connections)
			m_ConnectedScenesModel.addElement(o_Scene);
		
		//Allow for edditing of the same number of labels
		for (String o_Label : connectionLabels)
		{
			m_ConnectionLabels[i].setText(o_Label);
			m_ConnectionLabels[i].setEditable(true);
			i++;
		}
		
		//Disable all remaining and clear text (in case one was removed)
		while (i < NUM_JLABELS)
		{
			m_ConnectionLabels[i].setEditable(false);
			m_ConnectionLabels[i].setText("");
			i++;
		}
		
	}
	

	/**
	 * Handles the event that a connection has been requested to be removed.
	 */
	private void removeConnectionButtonClicked()
	{
		int connectionToRemove = m_ConnectedScenesJList.getSelectedIndex();

		if (connectionToRemove != -1)
		{
			m_Scene.removeConnection(connectionToRemove);
			populateConnectedScenes();
		}
	}
	
	/**
	 * Handles the event that a connection has been requested to be added.
	 */
	private void connectSceneButtonClicked()
	{
		btnConnectScene.setEnabled(false);
		
		AddConnectionWindow acw = new AddConnectionWindow(m_SceneManager.getSceneModel(), this, "Connect Scene");
		acw.run();
	}
	
	/**
	 * A new image has been selected from the dropdown menu of images.  Image is obtained and
	 * set as the new image for the scene.
	 */
	private void setImageToSelected()
	{
		SceneImage imageToAdd = m_SceneManager.getImageByName(m_ImageOnScene.getSelectedItem());
		m_ImageLabel.setIcon(imageToAdd.getImage());
		m_Scene.addImageToScene(imageToAdd);
	}
	
	/**
	 * Attempts to save the scene, grabbing each label associated to each connection and 
	 * saving them into the scene.  Gives the scene to the scene manager along with the requested
	 * new title.  If there is no name conflict, the scene is saved into the database.
	 */
	private void saveScene()
	{
		m_Scene.setDesc(m_DescTextArea.getText());
		for (int i = 0; i < m_Scene.getConnections().size(); i++)
			m_Scene.modifyLabelByIndex(i, m_ConnectionLabels[i].getText());
				
		if (m_Parent.saveEdittedScene( m_Scene, m_TitleTextField.getText() ))
			closeWindow();
		else
			m_WindowComm.displayMessage("Name conflict. Please enter a unique title for the scene.");
	}
	
	/**
	 * Handles the event that an item has been requested to be added to the scene.
	 */
	public void addItemButtonClicked()
	{
		AddItemWindow aiw = new AddItemWindow(m_SceneManager.getItemModel(), this);
		btnAddItem.setEnabled(false);
		aiw.run();
	}
	
	/*
	 * Functions to inform the window that a child window has been closed
	 */
	public void addItemWidnowHasClosed() 		{ btnAddItem.setEnabled(true); 		}
	public void addConnectionWindowHasClosed()	{ btnConnectScene.setEnabled(true); }
	
	/**
	 * Connects another scene to the current scene.
	 * @param selectedScene The scene that has been requested to be added as a connection
	 */
	public void connectScene(Scene selectedScene) 
	{
		m_Scene.addConnection(selectedScene, "");
		populateConnectedScenes();
	}
	
	/**
	 * Overridden close method 
	 */
	private void closeWindow()
	{
		m_Parent.editSceneWindowHasClosed();
		dispose();
	}

	/**
	 * Handles the event that the user has requested to remove the current drop item from the scene
	 */
	public void removeDropItemButtonClicked()
	{
		if (m_WindowComm.getYesNo("Are you sure you want to remove " + m_Scene.getDropItem() + " from this scene?", "Remove Item") == 0)
		{
			m_Scene.getDropItem().removeSceneDrop();
			m_Scene.removeDropItem();
			m_ItemDropsTextField.setText("");
			btnRmvDropItem.setEnabled(false);
		}
	}
	
	/**
	 * Handles the event that the user has requested to remove the current unlock item from the scene
	 */
	public void removeUnlockItemButtonClicked()
	{
		if (m_WindowComm.getYesNo("Are you sure you want to remove " + m_Scene.getUnlockItem() + " from this scene?", "Remove Item") == 0)
		{
			m_Scene.getUnlockItem().removeSceneUnlock();
			m_Scene.removeUnlockItem();
			m_ItemUnlocksTextField.setText("");
			btnRmvUnlockItem.setEnabled(false);
		}
	}

	/**
	 * Connects an item to the scene.
	 * 		The error flag returned corresponds as such:
	 * 		0 = item added
	 * 		1 = Requesting item to drop in two places,
	 *		2 = Item drops on same scene it unlocks
	 * 
	 * @param toConnect The item being requested to be added to the scene
	 * @param type Whether the item is an unlock item or a dropped item
	 * 
	 * @return An error flag for the caller.  
	 */
	public int connectItem(Item toConnect, String type) 
	{
		int connectFlag = 0;
		if (type.matches(m_UnlockString))
		{
			if (toConnect.equals(m_Scene.getDropItem()))	//If trying to set an unlock
				connectFlag = 2;							//and the scene current drops the item, inform caller
			else
			{
				m_Scene.connectUnlockItem(toConnect);		//else add the item to unlock, as items can unlock multiple scenes
				toConnect.setSceneUnlock(m_Scene);			//and connect the item to the scene
				m_ItemUnlocksTextField.setText(toConnect.getName());
				btnRmvUnlockItem.setEnabled(true);
			}
		}else
		{
			if (toConnect.equals(m_Scene.getUnlockItem()))	//If trying to set a drop and the item currently drops
				connectFlag = 2;							// on the scene, inform caller
			else if (toConnect.getDropScene() != null)		//If the item already has a connected scene to drop at,
				connectFlag = 1;							//inform caller.  Items can only drop once.
			else
			{
				m_Scene.connectDropItem(toConnect);			//else connect the item to drop at the scene
				toConnect.setSceneDrop(m_Scene);
				m_ItemDropsTextField.setText(toConnect.getName());
				btnRmvDropItem.setEnabled(true);
			}
			
		}

		return connectFlag;
	}
}
