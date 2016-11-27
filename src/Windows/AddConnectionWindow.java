package Windows;

import java.awt.EventQueue;
import javax.swing.*;
import Scene_Manager.*;
import TableModels.SceneTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Window for selecting and Adding a connection to a scene.
 *
 * @author	Team Smart Water
 * @version v1.0 - Apr 8, 2014
 */
@SuppressWarnings( "serial" )
public class AddConnectionWindow extends JFrame 
{
	private EditSceneWindow m_Parent;
	private JList <Scene>m_ScenesJList;
	private JScrollPane m_ScenesScrollPane;
	private JButton btnCancel;
	private DefaultListModel<Scene> m_SceneListModel;
	private JButton btnConnectScene;
	
	/**
	 * Button Handler for AddConnectionWindow Buttons
	 */
	public class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource().equals(btnConnectScene))
				connectSceneClicked();
			else if (e.getSource().equals(btnCancel))
				closeWindow();
		}
	}
	
	/**
	 * Constructor for AddConnectionWindow - Initializes private variables as well as window functionality.
	 * 
	 * @param sceneTable	The SceneTableModel to load the list from.
	 * @param parent		The parent window to set position and bounds from
	 * @param buttonName	Dynamic name of the Connect Scene Button
	 */
	public AddConnectionWindow( SceneTableModel sceneTable, EditSceneWindow parent, String buttonName)
	{
		getContentPane().setLayout(null);

		m_Parent = parent;
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
		ButtonHandler btnHandler = new ButtonHandler( );
		
		setBounds( 100, 100, 390, 552 );
		Point parentLocation = m_Parent.getLocation();
		double parentX = parentLocation.getX();
		double parentY = parentLocation.getY();
		
		setLocation((int)(parentX + 125),(int)(parentY + 125));
		
		m_SceneListModel = new DefaultListModel<Scene>();
		
		for (int i = 0; i < sceneTable.getRowCount(); i++)
			m_SceneListModel.addElement(sceneTable.getSceneAt(i));
		
		m_ScenesScrollPane = new JScrollPane();
		m_ScenesScrollPane.setBounds(10, 34, 210, 415);
		getContentPane().add(m_ScenesScrollPane);
		
		m_ScenesJList = new JList<Scene>();
		m_ScenesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_ScenesJList.setModel(m_SceneListModel);
		m_ScenesScrollPane.setViewportView(m_ScenesJList);
		
		btnConnectScene = new JButton(buttonName);
		btnConnectScene.addActionListener(btnHandler);
		btnConnectScene.setBounds(230, 32, 130, 23);
		getContentPane().add(btnConnectScene);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(btnHandler);
		btnCancel.setBounds(230, 66, 130, 23);
		getContentPane().add(btnCancel);

	}
	
	/**
	 * Function to run the Window
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
	 * Functionality to perform when the ConnectScene button is clicked.
	 */
	private void connectSceneClicked()
	{
		Scene toConnect = m_ScenesJList.getSelectedValue();
		if (toConnect != null)
		{
			m_Parent.connectScene(toConnect);
			closeWindow();
		}
	}
	
	/**
	 * Implements Close Window functionality.
	 */
	private void closeWindow()
	{
		m_Parent.addConnectionWindowHasClosed();
		dispose();
	}
}
