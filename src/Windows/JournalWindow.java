package Windows;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

import java.awt.SystemColor;

import javax.swing.JButton;

import java.awt.Font;
/**
 * Allows for the viewing of all notes within the system
 * Also doubles as a simple display of a formatted string to the user.
 * 
 * @author Tyrone
 * @version April 4, 2014
 */
@SuppressWarnings("serial")
public class JournalWindow extends JFrame
{
	private RunGameWindow m_Parent;
	/**
	 * Handles all button related actions to this scene
	 */
	public class ButtonHandler implements ActionListener 
	{	
		@Override
		public void actionPerformed(ActionEvent e) 
		{
			closeWindow();
		}
	}
	
	/**
	 * Runs this instance
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
	
	
	public JournalWindow (RunGameWindow parent, String notes, String font, int fontSize)
	{
		m_Parent = parent;
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeWindow();
			}


		});

		ButtonHandler btnHandler = new ButtonHandler();
		setBounds(400, 100, 529, 388);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 361, 328);
		getContentPane().add(scrollPane);
		
		JTextArea m_NoteTextArea = new JTextArea();
		m_NoteTextArea.setLineWrap(true);
		m_NoteTextArea.setWrapStyleWord(true);
		m_NoteTextArea.setFont(new Font(font, Font.PLAIN, fontSize));
		m_NoteTextArea.setBackground(SystemColor.control);
		scrollPane.setViewportView(m_NoteTextArea);
		m_NoteTextArea.setEditable(false);
		
		m_NoteTextArea.setText(notes);
		
		JButton btnReturn = new JButton("Return");
		btnReturn.addActionListener(btnHandler);
		btnReturn.setBounds(381, 13, 122, 23);
		getContentPane().add(btnReturn);

	}
	
	/**
	 * Overridden close operation
	 */
	public void closeWindow() 
	{
		m_Parent.aWindowHasClosed();
		dispose();	
	}
}
