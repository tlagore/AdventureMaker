package Windows;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Game_System.Item;
import Game_System.Player;
import Scene_Manager.Scene;
import Scene_Manager.SceneManager;
import UserIO.WindowComm;

import javax.swing.*;

import java.awt.EventQueue;
import java.awt.SystemColor;

import javax.swing.border.MatteBorder;

import java.awt.Color;
import java.util.ArrayList;
import java.awt.Font;

/**
 * Runs the game.  The user can run through their created game through this window.
 * 
 * This window also doubles as a debug option for the user, where they can view what the
 * scene will look like before they decide to run it.
 * 
 * @author Tyrone Lagore
 * @version April 6, 2014
 */

@SuppressWarnings("serial")
public class RunGameWindow extends JFrame
{
	private JFrame m_Parent;
	private Player m_Player;
	private SceneManager m_SceneManager;
	private WindowComm m_WindowComm;
	private JLabel m_lblTitle;
	private JTextArea m_SceneDescTextArea;
	private Scene m_CurrentScene;
	private JRadioButton []m_Choices;
	private ButtonGroup m_ButtonGroup;
	private static final int NUM_CHOICES = 4;
	private JButton btnGo;
	private JButton btnInventory;
	private JButton btnJournal;
	private JButton btnBacktrack;
	private JTextArea m_NoteTextArea;
	private boolean m_bDebug;
	private JLabel m_lblNote;
	private JScrollPane scrollPane;
	private JLabel m_ImageLabel;
	private JScrollPane scrollPane_1;
	
	/**
	 * ButtonHandler
	 * Handles the button interaction for this window
	 */
	public class ButtonHandler implements ActionListener 
	{
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource().equals(btnGo))
			{
				if( !m_bDebug )
					getNextScene();
				else
					closeWindow( );
			}
			else if (e.getSource().equals(btnInventory))
				openInventory();
			else if (e.getSource().equals(btnJournal))
				openJournal();
			else if (e.getSource().equals(btnBacktrack))
				backTrackSelected();

		}
	}
	
	/**
	 * Runs this instance of the RunGameWindow
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
	 * Name: initStatics
	 * Purpose: initializes the statics of this window. These are the parts of the window that need
	 * 		to be instantiated regardless of whether the user is using it to run the game
	 * 		or simply view a scene.
	 */
	private void initStatics( )
	{
		getContentPane().setLayout(null);
		m_ButtonGroup = new ButtonGroup();
		m_Choices = new JRadioButton[NUM_CHOICES];
		m_WindowComm = new WindowComm(this);
		ButtonHandler btnHandler = new ButtonHandler();
		setBounds(100, 100, 592, 607);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
		
		btnBacktrack = new JButton("Backtrack");
		btnBacktrack.addActionListener(btnHandler);
		btnBacktrack.setBounds(447, 535, 108, 23);
		getContentPane().add(btnBacktrack);
		
		m_lblTitle = new JLabel();
		m_lblTitle.setFont(new Font("Tahoma", Font.PLAIN, 23));
		m_lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		m_lblTitle.setBounds(10, 11, 504, 39);
		getContentPane().add(m_lblTitle);
		
		btnInventory = new JButton("Inventory");
		btnInventory.addActionListener(btnHandler);
		btnInventory.setBounds(447, 501, 108, 23);
		getContentPane().add(btnInventory);
		
		btnGo = new JButton("Go");
		btnGo.addActionListener(btnHandler);
		btnGo.setBounds(10, 535, 115, 23);
		getContentPane().add(btnGo);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(353, 330, 202, 82);
		getContentPane().add(scrollPane);
		
		m_NoteTextArea = new JTextArea();
		m_NoteTextArea.setFont(new Font("Andalus", Font.PLAIN, 14));
		scrollPane.setViewportView(m_NoteTextArea);
		m_NoteTextArea.setWrapStyleWord(true);
		m_NoteTextArea.setLineWrap(true);
		m_NoteTextArea.setText("");
		
		m_lblNote = new JLabel("Note");
		m_lblNote.setFont(new Font("Andalus", Font.PLAIN, 14));
		m_lblNote.setHorizontalAlignment(SwingConstants.CENTER);
		m_lblNote.setBounds(393, 308, 108, 14);
		getContentPane().add(m_lblNote);
		
		btnJournal = new JButton("Journal");
		btnJournal.addActionListener(btnHandler);
		btnJournal.setBounds(447, 468, 108, 23);
		getContentPane().add(btnJournal);
		
		m_ImageLabel = new JLabel("\r\n");
		m_ImageLabel.setBounds(10, 50, 325, 362);
		getContentPane().add(m_ImageLabel);
		m_lblNote.setVisible( false );
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(353, 50, 202, 225);
		getContentPane().add(scrollPane_1);
		
		m_SceneDescTextArea = new JTextArea();
		scrollPane_1.setViewportView(m_SceneDescTextArea);
		m_SceneDescTextArea.setFont(new Font("Andalus", Font.PLAIN, 14));
		m_SceneDescTextArea.setEditable(false);
		m_SceneDescTextArea.setBackground(SystemColor.control);
		m_SceneDescTextArea.setWrapStyleWord(true);
		m_SceneDescTextArea.setLineWrap(true);
		
		generateChoices( );
	}
	
	/**
	 * Debug constructor.
	 * Used when viewing a scene.
	 * @wbp.parser.constructor
	 * 
	 * @param parent The EditItemWindow that is calling the window
	 * @param sceneToLoad The scene to view.
	 * 
	 */
	public RunGameWindow( EditItemWindow parent, Scene sceneToLoad )
	{
		m_Parent = parent;
		m_bDebug = true;
		m_CurrentScene = sceneToLoad;
		
		initStatics( );
		
		m_NoteTextArea.setVisible( false );
		
		btnGo.setText( "Exit" );
		btnJournal.setEnabled( false );
		btnInventory.setEnabled( false );
		btnBacktrack.setEnabled( false );
		
		loadSceneInfo( m_CurrentScene );
		
		for( int i = 0; i < m_ButtonGroup.getButtonCount( ); ++i )
			m_Choices[ i ].setEnabled( false );
	}
	
	/**
	 * Constructor for running the game
	 * @param parent The MainWindow that is calling this window
	 * @param sceneManager The SceneManager that contains the scene info for running the game
	 * 
	 */
	public RunGameWindow (MainWindow parent, SceneManager sceneManager )
	{
		m_SceneManager = sceneManager;
		m_Player = m_SceneManager.getPlayer();
		m_Parent = parent;
		m_bDebug = false;		
		m_CurrentScene = m_SceneManager.getStartScene();
		m_SceneManager.resetGame();
		
		initStatics( );
		
		m_lblNote.setVisible(true);
		
		loadSceneInfo (m_CurrentScene);
	}
	
	/**
	 * The overridden close operation calls this function uponn exitting.
	 */
	private void closeWindow()
	{
		if( !m_bDebug )
			( (MainWindow)m_Parent ).runWindowHasClosed( );
		else
			( (EditItemWindow)m_Parent ).runWindowHasClosed( );
		dispose();
	}
	
	/**
	 * Retrieves the inventory from the player and formats it into a String for display
	 * 
	 * Uses the JournalWindow functionality instead of creating a whole other window
	 */
	private void openInventory()
	{
		String inventory = "";
		for (Item o_Item : m_Player.getInventory())
			inventory += o_Item.getName().toUpperCase() + "\n";
			
		
		JournalWindow jw = new JournalWindow(this, inventory, "Andalus", 20);
		toggleButtons(false);
		jw.run();
	}
	
	/**
	 * Retrieves a formatted String of journal entries from the SceneManager for display
	 */
	private void openJournal()
	{
		JournalWindow jw = new JournalWindow(this, m_SceneManager.getNotes(), "Lucida Handwriting", 13);
		toggleButtons(false);
		jw.run();
		
	}
	
	/**
	 * Name: getNextScene
	 * Purpose: Gets the selected scene connection and calls loadSceneInfo on the window to ensure
	 * 		the user is allowed to enter and update details if they are.
	 */
	private void getNextScene()
	{
		if (m_CurrentScene.equals(m_SceneManager.getEndScene()))
			closeWindow();
		
		int selected = -1;
		for (int i = 0; i < m_CurrentScene.getConnections().size(); i++)
			if (m_Choices[i].isSelected())
				selected = i;
		
		if (selected != -1)
			loadSceneInfo(  m_CurrentScene.getConnections().get( selected) );
	}
	
	/**
	 * The backtrack button allows the player to return to scenes that have already been visited
	 * 
	 * This allows the user to have more flexibility in their design choices (dead ends are allowed)
	 */
	private void backTrackSelected()
	{
		BacktrackWindow btw = new BacktrackWindow(this, m_SceneManager.getVisitedScenes());
		toggleButtons(false);
		btw.run();
	}
	
	/**
	 * Name: loadSceneInfo
	 * Purpose: Attempts to load the next scene's details into the window.
	 * 		If the user requires an item, they will be informed of the item required and a note
	 * 		will be automatically generated for the scene.  The user can choose to remove this note,
	 * 		but for larger game systems, it would be wise to maintain the note for future backtracks.
	 * 
	 * @param nextScene The scene that the game is attempting to move to
	 */
	private void loadSceneInfo( Scene nextScene )
	{
		Item unlockItem = nextScene.getUnlockItem();
		String addedText = "";
		if (!m_bDebug)
			addedText = nextScene.getDropItem() != null && !m_Player.inventoryContains(nextScene.getDropItem()) ? 
					"You receive loot: " + nextScene.getDropItem().getName() + "\n\n" : "";

		if(unlockItem == null || m_bDebug || m_Player.inventoryContains(unlockItem))
		{
			m_CurrentScene.setNote(m_NoteTextArea.getText());
			
			m_CurrentScene = nextScene;
			if( null != m_SceneManager )
				m_SceneManager.addVisitedScene(m_CurrentScene);
			
			m_NoteTextArea.setText(m_CurrentScene.getNote());
			
			if (m_CurrentScene.getImage() != null)
				m_ImageLabel.setIcon (m_SceneManager.getImageByName(m_CurrentScene.getImage()).getImage());

			if (null != m_Player && addedText.length() > 0)
				m_Player.addItem(nextScene.getDropItem());

			m_lblTitle.setText(m_CurrentScene.getTitle());

			String desc = addedText + m_CurrentScene.getDesc();
			m_SceneDescTextArea.setText(desc);
			generateChoices();
			
			if( !m_bDebug )
			{
				if (m_CurrentScene.equals(m_SceneManager.getEndScene()))
					btnGo.setText("Exit");
				else
					btnGo.setText("Go");
			}
				
		}else
		{
			m_CurrentScene.setNote("Needed item: " + unlockItem.getName() 
					+ " to enter " + nextScene.getTitle() + "\n\n" + m_NoteTextArea.getText());
			
			m_NoteTextArea.setText(m_CurrentScene.getNote());
			m_WindowComm.displayMessage("You require a " + unlockItem.getName() + " to enter that"
					+ " scene!.");
		}
	}
	
	/**
	 * Name: generateChoices
	 * Purpose: Generates a ButtonGroup of RadioButtons that the user can select and attempt to move to
	 */
	private void generateChoices()
	{
		int x = 15;
		int y = 417;
		int width = 380;
		int height = 20;
		int numButtons = m_ButtonGroup.getButtonCount();
		
		ArrayList<String> connectionLabels = m_CurrentScene.getConnectionLabels();

		//Remove all current buttons
		for (int i = 0; i < numButtons; i++)
		{
			getContentPane().remove(m_Choices[i]);
			m_ButtonGroup.remove(m_Choices[i]);
		}
		
		//Clear any removed buttons from display
		revalidate();
		repaint();
		
		//Add a button corresponding to each connection
		for(int i = 0; i < m_CurrentScene.getConnections().size(); i++)
		{
			m_Choices[i] = new JRadioButton(connectionLabels.get(i));
			m_Choices[i].setBounds(x, y, width, height);
			getContentPane().add(m_Choices[i]);
			m_ButtonGroup.add(m_Choices[i]);
			
			y+= 25;
		}	
		
		//If there is an available choice, select 
		if (m_CurrentScene.getConnections().size() > 0)
			m_Choices[0].setSelected(true);
	}

	/**
	 * Informs the RunGameWindow that a window that was opened by the current RunGameWindow(this)
	 * 		has closed, and buttons must be toggled back to usable.
	 */
	public void aWindowHasClosed() 
	{
		toggleButtons(true);
	}
	
	/**
	 * When a window is opened from the RunGameWindow, buttons are disabled until the window closes
	 * @param bToggle true for enabled false for disabled
	 */
	public void toggleButtons(boolean bToggle)
	{
		btnBacktrack.setEnabled(bToggle);
		btnGo.setEnabled(bToggle);
		btnInventory.setEnabled(bToggle);
		btnJournal.setEnabled(bToggle);
	}

	/**
	 * Used in backtracking to go to a scene that was previously visited
	 * @param whereToGo The scene to go to
	 */
	public void goToScene(Scene whereToGo) 
	{
		loadSceneInfo ( whereToGo );
	}
}
