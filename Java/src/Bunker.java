import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bunker
{
	private int bunkerWidth;
	private int bunkerHeight;
	private int bunkerXPos;
	private int bunkerYPos;

	private ImageIcon imgBunker = new ImageIcon(getClass().getResource("asteroid.png"));
	private JLabel bunkerLabel = new JLabel(imgBunker);


	public Bunker(int xPos, int yPos)
	{
		bunkerWidth = imgBunker.getIconWidth();
		bunkerHeight = imgBunker.getIconHeight();
		bunkerXPos = xPos;
		bunkerYPos = yPos;
	}

	// Move the missile 'MISSILE_SPEED' pixels up the playing field

	public JLabel getBunkerImage()
	{
		return bunkerLabel;
	}
	
	public int getWidth()
	{
		return bunkerWidth;
	}

	public int getHeight()
	{
		return bunkerHeight;
	}

	public int getX()
	{
		return bunkerXPos;
	}

	public int getY()
	{
		return bunkerYPos;
	}
}