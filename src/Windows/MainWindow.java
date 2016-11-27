package Windows;

import java.awt.*;
import javax.swing.JFrame;
import Panels.ItemManagerPanel;
import Panels.SceneManagerPanel;
import Scene_Manager.SceneManager;
import UserIO.WindowComm;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.*;

/**
 * Main Window object for displaying and implementing the majority of the game editor
 * functionality.
 * 
 * @author Team Smart Water
 * @version v1.0 - Mar 25, 2014
 */
@SuppressWarnings( "serial" )
public class MainWindow extends JFrame{

	/* Private Variables for Main Window */
	
	private SceneManager m_MainSystem;
	private SceneManagerPanel m_SceneMngrPnl;
	private ItemManagerPanel m_ItemMngrPnl;
	private WindowComm m_WindowComm;
	private JTabbedPane m_MainTabbedPane;
	private JMenu mnFile;
	private JMenuItem mntmSave;
	private JMenuItem mntmLoad;
	private JMenuItem mntmQuit;
	private JMenuItem mntmRun;

	/**
	 * Inner class button handler for handling button clicked events.
	 *
	 * @author	James C. Coté
	 * @version v1.0 - Apr 8, 2014
	 */
	public class MenuHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource().equals(mntmSave))
				saveProject();
			else if (e.getSource().equals(mntmLoad))
				loadProject();
			else if (e.getSource().equals(mntmQuit))
				verifyQuit();
			else if (e.getSource().equals(mntmRun))
				runGame();
		}
	}

	/**
	 * Runs the Window.
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
	 * Create the Window and initialize its parameters.
	 */
	public MainWindow( SceneManager mainSystem ) {
		m_MainSystem = mainSystem;
		initialize();
	}
	
	/**
	 * Name: verifyQuit
	 * 
	 * Purpose: Verifies that the user wants to exit, and asks if they
	 *  would like to save.
	 */
	public void verifyQuit()
	{
		if (m_WindowComm.getYesNo("Would you like to save before quitting?", "Quit") == 0)
			saveProject();
		
		closeWindow();
	}
	
	/**
	 * Name: loadProject
	 * Purpose: Load has been selected, opens the window explorer for the user
	 * 	to select a file to load.
	 * 
	 */
	public void loadProject()
	{
		String fileName = m_WindowComm.getFileFromExplorer(FileDialog.LOAD);
		m_MainSystem.loadSceneManager(fileName);
	}
	
	/**
	 * Name: saveProject
	 * Purpose: Save has been selected, opens the window explorer for the user
	 * 	to select a file (or enter a filename) that they would like to save as.
	 * 
	 */
	public void saveProject()
	{
		String fileName = m_WindowComm.getFileFromExplorer(FileDialog.SAVE);
		if (!fileName.matches("null") && !fileName.matches("nullnull"))
		{
			if ( m_MainSystem.saveSceneManager(fileName))
				m_WindowComm.displayMessage("Successfully saved to \"" + fileName + "\".");
			else 
				m_WindowComm.displayMessage("Error saving file.");
		}
	}
	
	/**
	 * Function that makes the main window visible after the Run window closes.
	 * - Called from Run Window.
	 */
	public void runWindowHasClosed()
	{
		setVisible(true);
	}

	/**
	 * Launches the run window and runs the game in its current state.
	 */
	public void runGame()
	{
		if (m_MainSystem.getEndScene().getSceneIsConnected())
		{
			RunGameWindow rgw = new RunGameWindow (this, m_MainSystem );
			setVisible(false);
			rgw.run();
		}else
			m_WindowComm.displayMessage("The \"Beginning\" scene must connect to the \"End\" scene.");
	}
	
	/**
	 * Initialize the contents of the Window.
	 */
	private void initialize() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				verifyQuit();
			}
		});
		
		m_WindowComm = new WindowComm(this);
		
		MenuHandler btnHandler = new MenuHandler( );
		
		setBounds(100, 100, 799, 569);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmSave = new JMenuItem("Save");
		mntmSave.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK ) );
		mntmSave.addActionListener(btnHandler);
		mnFile.add(mntmSave);

		mntmLoad = new JMenuItem("Load");
		mntmLoad.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_L, ActionEvent.CTRL_MASK ) );
		mntmLoad.addActionListener(btnHandler);
		mnFile.add(mntmLoad);
		
		mntmRun = new JMenuItem("Run");
		mntmRun.setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_R, ActionEvent.CTRL_MASK ) );
		mntmRun.addActionListener(btnHandler);
		mnFile.add(mntmRun);

		mntmQuit = new JMenuItem("Quit");
		mntmQuit.addActionListener(btnHandler);
		mnFile.add(mntmQuit);
		

		m_MainTabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(m_MainTabbedPane, BorderLayout.CENTER);
		
		m_SceneMngrPnl = new SceneManagerPanel(m_MainSystem.getSceneModel(), this,
													m_MainSystem);
		
		m_MainTabbedPane.addTab("Scenes", null, m_SceneMngrPnl, null);

		m_ItemMngrPnl = new ItemManagerPanel(m_MainSystem.getItemModel(), this,
												m_MainSystem);
		m_MainTabbedPane.addTab("Items", null, m_ItemMngrPnl, null);
	}
	
	/**
	 * Functionality for closing a window.
	 */
	private void closeWindow()
	{
		setVisible(false);
		dispose();
	}
}
