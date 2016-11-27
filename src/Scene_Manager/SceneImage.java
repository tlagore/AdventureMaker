package Scene_Manager;

import javax.swing.ImageIcon;
/**
 * SceneImage
 * 
 * Stores an ImageIcon and the name of the image.
 * 
 * @author Tyrone Lagore
 * @version April 8, 2014, 9:29 PM >_<
 */
public class SceneImage 
{
	private String m_ImageName;
	private ImageIcon m_Image;
	
	public SceneImage (String filename, String imageName)
	{
		m_Image = new ImageIcon (filename);
		m_ImageName = imageName;
	}

	/* getters */
	public String getImageName() 	{	return m_ImageName;		}
	public ImageIcon getImage() 	{	return m_Image;			}
}
