import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class SmallAlien implements Alien
{
	// Constant
	private final int ALIEN_SPEED = 1;

	private int alienWidth;
	private int alienHeight;
	private int alienXPos;
	private int alienYPos;
	private int alienMoveDirection = 1; // 1 = move right, 2 = move left

	public ImageIcon alienImage = new ImageIcon(getClass().getResource("ship-small.png"));
	public JLabel alienLabel = new JLabel(alienImage);

	// Constructor
	public SmallAlien(int xPos, int yPos)
	{
		alienWidth = alienImage.getIconWidth();
		alienHeight = alienImage.getIconHeight();
		alienXPos = xPos;
		alienYPos = yPos;
	}

	public String getType()
	{
		return "Small";
	}

	// Move the alien 'ALIEN_SPEED' pixels in the appropriate direction; if the
	//  alien has reached the left or right edge of the field, reverse the
	//  direction of the alien
	public void moveAlien()
	{
		if ((alienXPos <= 0) || (alienXPos + alienWidth + 6 >= Game.FIELD_WIDTH))
			alienMoveDirection = 3 - alienMoveDirection;

		if (alienMoveDirection == 1)
			alienXPos += ALIEN_SPEED;
		else
			alienXPos -= ALIEN_SPEED;
	}

	public JLabel getAlienImage()
	{
		return alienLabel;
	}

	public int getWidth()
	{
		return alienWidth;
	}

	public int getHeight()
	{
		return alienHeight;
	}

	public int getX()
	{
		return alienXPos;
	}

	public int getY()
	{
		return alienYPos;
	}
}
