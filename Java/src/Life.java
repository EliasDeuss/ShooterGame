import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Life
{
	private int LifeWidth;
	private int LifeHeight;
	private int LifeXPos;
	private int LifeYPos;

	private ImageIcon imgLife = new ImageIcon(getClass().getResource("life.png"));
	private JLabel LifeLabel = new JLabel(imgLife);


	public Life(int xPos, int yPos)
	{
		LifeWidth = imgLife.getIconWidth();
		LifeHeight = imgLife.getIconHeight();
		LifeXPos = xPos;
		LifeYPos = yPos;
	}


	public JLabel getLivesPackImage()
	{
		return LifeLabel;
	}
	
	public int getWidth()
	{
		return LifeWidth;
	}

	public int getHeight()
	{
		return LifeHeight;
	}

	public int getX()
	{
		return LifeXPos;
	}

	public int getY()
	{
		return LifeYPos;
	}
}