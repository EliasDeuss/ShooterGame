import javax.swing.JLabel;

public interface Bomb
{
	public String getType();

	public void moveBomb();

	public JLabel getBombImage();

	public int getWidth();
	public int getHeight();

	public int getX();
	public int getY();
}
