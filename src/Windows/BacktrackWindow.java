package Windows;

import java.awt.EventQueue;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import Scene_Manager.*;
/**
 * Allows the user to backtrack through a list of visited scenes.
 * 
 * @author Tyrone
 * @version April 8, 2014
 *
 */
@SuppressWarnings("serial")
public class BacktrackWindow extends JFrame 
{
	private RunGameWindow m_Parent;
	
	private JList <Scene>m_ScenesJList;
	private JScrollPane m_ScenesScrollPane;
	private JButton btnCancel;
	private DefaultListModel<Scene> m_SceneListModel;
	private JButton btnGoToScene;
	
	
	public class ButtonHandler implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (e.getSource().equals(btnGoToScene))
				goToScene();
			else if (e.getSource().equals(btnCancel))
				closeWindow();
		}
	}
	/**
	 * Constructor of the window
	 * 
	 * @param parent RunGameWindow - The window that instantiated this window 
	 * @param visitedScenes The list of visited scenes.
	 */
	public BacktrackWindow ( RunGameWindow parent, ArrayList<Scene> visitedScenes )
	{
		getContentPane().setLayout(null);

		m_Parent = parent;
		
		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}
		});
		ButtonHandler btnHandler = new ButtonHandler();
		
		setBounds( 100, 100, 390, 552 );
		Point parentLocation = m_Parent.getLocation();
		double parentX = parentLocation.getX();
		double parentY = parentLocation.getY();
		
		setLocation((int)(parentX + 125),(int)(parentY + 125));
		
		m_SceneListModel = new DefaultListModel<Scene>();
		
		for (Scene o_Scene : visitedScenes )
			m_SceneListModel.addElement(o_Scene);
		
		m_ScenesScrollPane = new JScrollPane();
		m_ScenesScrollPane.setBounds(10, 34, 210, 415);
		getContentPane().add(m_ScenesScrollPane);
		
		m_ScenesJList = new JList<Scene>();
		m_ScenesJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		m_ScenesJList.setModel(m_SceneListModel);
		m_ScenesScrollPane.setViewportView(m_ScenesJList);
		
		btnGoToScene = new JButton("Go to scene");
		btnGoToScene.addActionListener(btnHandler);
		btnGoToScene.setBounds(230, 32, 130, 23);
		getContentPane().add(btnGoToScene);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(btnHandler);
		btnCancel.setBounds(230, 66, 130, 23);
		getContentPane().add(btnCancel);

	}
	
	/**
	 * runs this instance
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
	 * Name: goToScene
	 * Purpose: Returns the selected scene to the parent window
	 */
	private void goToScene()
	{
		Scene whereToGo = m_ScenesJList.getSelectedValue();
		if (whereToGo != null)
		{
			m_Parent.goToScene(whereToGo);
			closeWindow();
		}
	}
	
	/**
	 * Overridden close operation for when the window closes
	 */
	private void closeWindow()
	{
		m_Parent.aWindowHasClosed();
		dispose();
	}
}
