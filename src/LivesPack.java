import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class LivesPack
{
	private int LivesPackWidth;
	private int LivesPackHeight;
	private int LivesPackXPos;
	private int LivesPackYPos;

	private ImageIcon imgLivePacks = new ImageIcon(getClass().getResource("life.png"));
	private JLabel LivesPackLabel = new JLabel(imgLivePacks);


	public LivesPack(int xPos, int yPos)
	{
		LivesPackWidth = imgLivePacks.getIconWidth();
		LivesPackHeight = imgLivePacks.getIconHeight();
		LivesPackXPos = xPos;
		LivesPackYPos = yPos;
	}


	public JLabel getLivesPackImage()
	{
		return LivesPackLabel;
	}
	
	public int getWidth()
	{
		return LivesPackWidth;
	}

	public int getHeight()
	{
		return LivesPackHeight;
	}

	public int getX()
	{
		return LivesPackXPos;
	}

	public int getY()
	{
		return LivesPackYPos;
	}
}