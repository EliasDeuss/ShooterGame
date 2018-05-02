import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Missile
{
	// Constant
	private final int MISSILE_SPEED = 2;

	private int missileWidth;
	private int missileHeight;
	private int missileXPos;
	private int missileYPos;

	private ImageIcon missileImage = new ImageIcon(getClass().getResource("missile.png"));
	private JLabel missileLabel = new JLabel(missileImage);

	// Constructor
	public Missile(int xPos, int yPos)
	{
		missileWidth = missileImage.getIconWidth();
		missileHeight = missileImage.getIconHeight();
		missileXPos = xPos;
		missileYPos = yPos;
	}

	// Move the missile 'MISSILE_SPEED' pixels up the playing field
	public void moveMissile()
	{
		missileYPos -= MISSILE_SPEED;
	}

	public JLabel getMissileImage()
	{
		return missileLabel;
	}

	public int getWidth()
	{
		return missileWidth;
	}

	public int getHeight()
	{
		return missileHeight;
	}

	public int getX()
	{
		return missileXPos;
	}

	public int getY()
	{
		return missileYPos;
	}
}
