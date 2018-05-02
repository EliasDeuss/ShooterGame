import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Bomb
{
	// Constant
	private final int BOMB_SPEED = 2;

	private int bombWidth;
	private int bombHeight;
	private int bombXPos;
	private int bombYPos;

	private ImageIcon bombImage = new ImageIcon(getClass().getResource("bomb-1.png"));
	private JLabel bombLabel = new JLabel(bombImage);

	// Constructor
	public Bomb(int xPos, int yPos)
	{
		bombWidth = bombImage.getIconWidth();
		bombHeight = bombImage.getIconHeight();
		bombXPos = xPos;
		bombYPos = yPos;
	}

	// Move the missile 'MISSILE_SPEED' pixels up the playing field
	public void moveBomb()
	{
		bombYPos =- BOMB_SPEED;
	}

	public JLabel getBombImage()
	{
		return bombLabel;
	}

	public int getWidth()
	{
		return bombWidth;
	}

	public int getHeight()
	{
		return bombHeight;
	}

	public int getX()
	{
		return bombXPos;
	}

	public int getY()
	{
		return bombYPos;
	}
}
