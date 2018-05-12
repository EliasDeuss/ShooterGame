import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.Timer;

public class Game implements ActionListener, KeyListener
{
	// Global Constants
	public static final int FIELD_WIDTH = 900;
	public static final int FIELD_HEIGHT = 600;

	// Local Constants
	private final int TIMER_SPEED = 10;
	private final int SHOOTER_SPEED = 2;
	private int NUM_SMALL_ALIENS = 8;
	private int NUM_LARGE_ALIENS = 3;
	private int NUM_BUNKERS = 7;
	private int PLAYER_SCORE = 0;
	private int PLAYER_LIVES = 3;
	private int PLAYER_TIME_LEFT = 5000;
	private int TOP_SCORE;
	private int DIF_BOMBS = 250;
	private int PLAYER_LEVEL = 1;
	private int BOMB_DROP = 0;
	private int LifeX, LifeY;
	private int shooterX, shooterY, AlienX, AlienY;

	private JFrame gameFrame;
	private Timer timer;

	//Images
	private ImageIcon imgBackground = new ImageIcon(getClass().getResource("space.gif"));
	private ImageIcon imgShooter = new ImageIcon(getClass().getResource("shooter.png"));
	private ImageIcon imgLife = new ImageIcon(getClass().getResource("life.png")); //*
	private JLabel LifeLabel = new JLabel(imgLife);

	private boolean play = true;
	private boolean pressedLeft = false, pressedRight = false, pressedSpace = false;
	private boolean controlKeyPressed = false, missileFired = false, bombFired = false;
	private JLabel lblShooter = new JLabel(imgShooter);
	private JLabel lblGameOverScore = new JLabel("Score: " + PLAYER_SCORE);
	private JLabel lblGameScore = new JLabel("Score: " + PLAYER_SCORE);
	private JLabel lblGameTopScore = new JLabel("Top Score: " + PLAYER_SCORE);
	private JLabel lblPlayerLevel = new JLabel("Level: " + PLAYER_LEVEL);
	private JLabel lblPlayerTime = new JLabel("Time Left: " + PLAYER_TIME_LEFT);
	private JLabel lblPlayerLives = new JLabel("Lives: " + PLAYER_LIVES);
	private JLabel lblGamePaused = new JLabel("Pause");
	
	private JLabel lblGameOver = new JLabel("Game Over!");
	private Font fontGameOver = new Font("Helvetica", Font.BOLD, 24);
	private int textWidth = lblGameOver.getFontMetrics(fontGameOver).stringWidth(lblGameOver.getText());
	
	private JRadioButtonMenuItem rbMenuItem1, rbMenuItem2;

	//Array Lists
	ArrayList<Alien> aliens = new ArrayList<Alien>();
	ArrayList<Missile> missiles = new ArrayList<Missile>();
	ArrayList<Bomb> bombs = new ArrayList<Bomb>();
	ArrayList<Bunker> bunkers = new ArrayList<Bunker>(); 
	ArrayList<Life> lifes = new ArrayList<Life>();

	public static void main(String[] args) 
	{
		new Game();
	}

	// Constructor
	public Game()
	{
		setUpFrame();
		setUpMenuBar();
		setUpHighScore();
		setUpShooter();
		setUpLargeAliens();
		setUpSmallAliens();
		setUpBunkers();
		
		//setUpLife();
		
		gameFrame.addKeyListener(this);
		gameFrame.setVisible(true);

		//Set up / start timer
		timer = new Timer(TIMER_SPEED, this);
		//timer.setInitialDelay(TIMER_DELAY);
		timer.start();
	}

	// Set the size and starting position of the player's shooter
	public void setUpShooter()
	{
		// Set the size of the JLabel that contains the shooter image
		lblShooter.setSize(imgShooter.getIconWidth(), imgShooter.getIconHeight());

		// Set the shooter's initial position 
		shooterX = (FIELD_WIDTH / 2) - (lblShooter.getWidth() / 2);
		shooterY = FIELD_HEIGHT - lblShooter.getHeight() - 48;
		lblShooter.setLocation(shooterX, shooterY);

		// Add the shooter JLabel to the JFrame
		gameFrame.add(lblShooter);
	}

	// Create and randomly place the appropriate number of SMALL aliens on the playing field
	public void setUpSmallAliens()
	{
		// Determine the width and height of each alien being placed
		Alien tempAlien = new SmallAlien(0, 0);
		int alienWidth = tempAlien.getWidth();
		int alienHeight = tempAlien.getHeight();
		
		AlienX = (FIELD_WIDTH / 2) - 3;
		AlienY = FIELD_HEIGHT - lblShooter.getHeight() - 48;

		for (int i = 0; i < NUM_SMALL_ALIENS; i++)
		{
			// Set the starting positions of each of the aliens being placed
			int x = (int) (Math.random() * (FIELD_WIDTH - alienWidth - 7) + 1);
			int y = (int) (Math.random() * (FIELD_HEIGHT*3/4 - alienHeight - 26 - lblShooter.getHeight() - 60));

			// Create a new 'Alien' object and add it to the 'aliens' ArrayList 
			aliens.add(new SmallAlien(x, y));
		}
	}

	// Create and randomly place the appropriate number of LARGE aliens on the playing field
	public void setUpLargeAliens()
	{
		// Determine the width and height of each alien being placed
		Alien tempAlien = new LargeAlien(0, 0);
		int alienWidth = tempAlien.getWidth();
		int alienHeight = tempAlien.getHeight();

		for (int i = 0; i < NUM_LARGE_ALIENS; i++)
		{
			// Set the starting positions of each of the aliens being placed
			int x = (int) (Math.random() * (FIELD_WIDTH - alienWidth - 7) + 1);
			int y = (int) (Math.random() * (FIELD_HEIGHT*3/4 - alienHeight - 26 - lblShooter.getHeight() - 60));

			// Create a new 'Alien' object and add it to the 'aliens' ArrayList 
			aliens.add(new LargeAlien(x, y));
		}
	}
	
	
	public void setUpBunkers()
	{
		Bunker tempBunker = new Bunker(0, 0);
		int bunkerWidth = tempBunker.getWidth();
		int bunkerHeight = tempBunker.getHeight();

		for (int i = 0; i < NUM_BUNKERS; i++)
		{
			// Set the starting positions of each of the bunkers being placed
			int x = (int) (Math.random() * (FIELD_WIDTH - bunkerWidth - 7) + 1);
			int y = (int) ((Math.random() * (FIELD_HEIGHT/3 - bunkerHeight - 26 - lblShooter.getHeight() - 60))) + FIELD_HEIGHT*2/3;

			// Create a new 'Bunker' object and add it to the 'bunker' ArrayList 
			bunkers.add(new Bunker(x, y));
		}
	}
	
	public void setUpLife()
	{
		Life tempLivesPacks = new Life(0, 0);
		int LivesPacksWidth = tempLivesPacks.getWidth();
		int LivesPacksHeight = tempLivesPacks.getHeight();

		for (int i = 0; i < 1; i++)
		{
			// Set the starting positions of each of the bunkers being placed
		int x = (int) (Math.random() * (FIELD_WIDTH - LivesPacksWidth - 7) + 1);
		int y = (int) (Math.random() * (FIELD_HEIGHT*3/4 - LivesPacksHeight - 26 - lblShooter.getHeight() - 60));
		
		LifeX = x;
		LifeY = y;
		
			// Create a new 'Bunker' object and add it to the 'bunker' ArrayList 
			LifeLabel.setVisible(true);
			LifeLabel.setSize(textWidth, 40);
			LifeLabel.setLocation(x,y);
			gameFrame.add(LifeLabel);
		}
	}
	
	//Sets up JFrame and all JLabels
	public void setUpFrame()
	{
		gameFrame = new JFrame();

		// Set the background image and other JFrame properties
		gameFrame.setContentPane(new JLabel(imgBackground));
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setSize(FIELD_WIDTH, FIELD_HEIGHT);
		gameFrame.setTitle("Simple Shooter Game");
		gameFrame.setLocationRelativeTo(null);
		gameFrame.setResizable(false);
		gameFrame.setFocusable(true);
		
		// Set up the "Game Over" JLabel
		lblGameOver.setVisible(false);
		lblGameOver.setSize(textWidth, 50);
		lblGameOver.setLocation(FIELD_WIDTH / 2 - textWidth / 2, FIELD_HEIGHT / 2 - 50);
		lblGameOver.setFont(fontGameOver);
		lblGameOver.setForeground(Color.YELLOW);
		
		//Set up the "Game Score" JLabel
		lblGameScore.setVisible(true);
		lblGameScore.setSize(textWidth, 30);
		lblGameScore.setLocation(765,1);
		lblGameScore.setFont(fontGameOver);
		lblGameScore.setForeground(Color.WHITE);
		gameFrame.add(lblGameScore);
		
		//Set up the time JLabel
		lblPlayerTime.setVisible(true);
		lblPlayerTime.setSize(250, 30);
		lblPlayerTime.setLocation(2,35);
		lblPlayerTime.setFont(fontGameOver);
		lblPlayerTime.setForeground(Color.WHITE);
		gameFrame.add(lblPlayerTime);
		
		//Set up the lives JLabel
		lblPlayerLives.setVisible(true);
		lblPlayerLives.setSize(200,30);
		lblPlayerLives.setLocation(2,1);
		lblPlayerLives.setFont(fontGameOver);
		lblPlayerLives.setForeground(Color.WHITE);
		gameFrame.add(lblPlayerLives);
		
		//Set up the level JLabel
		lblPlayerLevel.setVisible(true);
		lblPlayerLevel.setSize(textWidth, 30);
		lblPlayerLevel.setLocation(400,1);
		lblPlayerLevel.setFont(fontGameOver);
		lblPlayerLevel.setForeground(Color.WHITE);
		gameFrame.add(lblPlayerLevel);
		
		//Set up the top score JLabel
		lblGameTopScore.setVisible(false);
		lblGameTopScore.setSize(200, 50);
		lblGameTopScore.setLocation(FIELD_WIDTH / 2 - textWidth / 2, (FIELD_HEIGHT / 2) + 10);
		lblGameTopScore.setFont(fontGameOver);
		lblGameTopScore.setForeground(Color.YELLOW);
		gameFrame.add(lblGameTopScore);
		
		//Game Over Score
		lblGameOverScore.setVisible(false);
		lblGameOverScore.setSize(textWidth, 50);
		lblGameOverScore.setLocation(FIELD_WIDTH / 2 - textWidth / 2, FIELD_HEIGHT / 2 - 20);
		lblGameOverScore.setFont(fontGameOver);
		lblGameOverScore.setForeground(Color.WHITE);
		
		//Pause Menu
		lblGamePaused.setVisible(false);
		lblGamePaused.setSize(textWidth, 40);
		lblGamePaused.setLocation(410,250);
		lblGamePaused.setFont(fontGameOver);
		lblGamePaused.setForeground(Color.WHITE);
		gameFrame.add(lblGamePaused);
	}
	
	public void setUpMenuBar()
	{
		//Menu

		JMenuBar menuBar;
		JMenu menu;
		JMenuItem menuItem;

		//Create the menu bar.
		menuBar = new JMenuBar();

		//Build the first menu.
		menu = new JMenu("File");
		menu.setMnemonic(KeyEvent.VK_A);
		menu.getAccessibleContext().setAccessibleDescription("File");
		menuBar.add(menu);
				
		//a group of JMenuItems
		menuItem = new JMenuItem("Reset");
		menuItem.setActionCommand("reset");
		menuItem.addActionListener(
		  new ActionListener() 
				{
			    public void actionPerformed(ActionEvent e) 
			    {
			    	if (e.getActionCommand().equals("reset"))
					{
			    		gameFrame.getContentPane().removeAll();
						missiles.removeAll(missiles);
						aliens.removeAll(aliens);
						bunkers.removeAll(bunkers);
						bombs.removeAll(bombs);
						lblGameOver.setVisible(false);
						lblGameOverScore.setVisible(false);
						lblGameTopScore.setVisible(false);
						
						setUpShooter();
						setUpLargeAliens();
						setUpSmallAliens();
						setUpBunkers();
						
						rbMenuItem1.setSelected(true);
						
						PLAYER_SCORE = 0;
						PLAYER_LIVES = 3;
						PLAYER_LEVEL = 1;
						PLAYER_TIME_LEFT = 5000;
						
						lblGameScore.setText("Score: " + PLAYER_SCORE);
						lblPlayerLives.setText("Lives: " + PLAYER_LIVES);
						lblPlayerLevel.setText("Level: " + PLAYER_LEVEL);
						lblGameScore.setVisible(true);
						lblPlayerTime.setVisible(true);
						lblPlayerLives.setVisible(true);
						lblPlayerLevel.setVisible(true);
						
						NUM_SMALL_ALIENS = 8;
						NUM_LARGE_ALIENS = 3;
						NUM_BUNKERS = 7;
						
						timer.start();
						gameFrame.repaint();
						gameFrame.add(lblGameScore);
						gameFrame.add(lblPlayerTime);
						gameFrame.add(lblPlayerLives);
						gameFrame.add(lblPlayerLevel);
					}
				}
		  }
		);
		menu.add(menuItem);

		//Difficulty Selector
		menu.addSeparator();
		ButtonGroup group = new ButtonGroup();
		rbMenuItem1 = new JRadioButtonMenuItem("Normal");
		rbMenuItem1.setSelected(true);
		rbMenuItem1.setMnemonic(KeyEvent.VK_R);
		group.add(rbMenuItem1);
				rbMenuItem1.setActionCommand("normal");
				rbMenuItem1.addActionListener(
						  new ActionListener() 
						  {
						    public void actionPerformed(ActionEvent e) 
						    {
						    	if (e.getActionCommand().equals("normal"))
								{
						    		NUM_SMALL_ALIENS = 8;
									NUM_LARGE_ALIENS = 3;
									
						    		gameFrame.getContentPane().removeAll();
									missiles.removeAll(missiles);
									aliens.removeAll(aliens);
									bombs.removeAll(bombs);
									bunkers.removeAll(bunkers);
									lblGameOver.setVisible(false);
									lblGameOverScore.setVisible(false);
									lblGameTopScore.setVisible(false);
									
									setUpShooter();
									setUpLargeAliens();
									setUpSmallAliens();
									setUpBunkers();
									
									PLAYER_SCORE = 0;
									PLAYER_TIME_LEFT = 5000;
									PLAYER_LEVEL = 1;
									DIF_BOMBS = 250;
									
									lblGameScore.setText("Score: " + PLAYER_SCORE);
									lblPlayerLives.setText("Lives: " + PLAYER_LIVES);
									lblPlayerLevel.setText("Level: " + PLAYER_LEVEL);
									lblGameScore.setVisible(true);
									lblPlayerTime.setVisible(true);
									lblPlayerLives.setVisible(true);
									lblPlayerLevel.setVisible(true);
									
									timer.start();
									gameFrame.repaint();
									gameFrame.add(lblGameScore);
									gameFrame.add(lblPlayerLives);
									gameFrame.add(lblPlayerTime);
									gameFrame.add(lblPlayerLevel);
								}
						    }
						  }
						);
		menu.add(rbMenuItem1);

		rbMenuItem2 = new JRadioButtonMenuItem("Hard");
		rbMenuItem2.setMnemonic(KeyEvent.VK_O);
		group.add(rbMenuItem2);
		rbMenuItem2.setActionCommand("hard");
		rbMenuItem2.addActionListener(
						  new ActionListener() 
						  {
						    public void actionPerformed(ActionEvent e) 
						    {
						    	if (e.getActionCommand().equals("hard"))
								{
						    		NUM_SMALL_ALIENS = 13;
									NUM_LARGE_ALIENS = 6;
									
						    		gameFrame.getContentPane().removeAll();
									missiles.removeAll(missiles);
									aliens.removeAll(aliens);
									bombs.removeAll(bombs);
									bunkers.removeAll(bunkers);
									lblGameOver.setVisible(false);
									lblGameOverScore.setVisible(false);
									lblGameTopScore.setVisible(false);
									
									setUpShooter();
									setUpLargeAliens();
									setUpSmallAliens();
									setUpBunkers();
									
									PLAYER_SCORE = 0;
									PLAYER_TIME_LEFT = 5000;
									PLAYER_LEVEL = 1;
									DIF_BOMBS = 50;
									
									lblGameScore.setText("Score: " + PLAYER_SCORE);
									lblPlayerLives.setText("Lives: " + PLAYER_LIVES);
									lblPlayerLevel.setText("Level: " + PLAYER_LEVEL);
									lblGameScore.setVisible(true);
									lblPlayerTime.setVisible(true);
									lblPlayerLives.setVisible(true);
									lblPlayerLevel.setVisible(true);
									
									timer.start();
									gameFrame.repaint();
									gameFrame.add(lblGameScore);
									gameFrame.add(lblPlayerLives);
									gameFrame.add(lblPlayerTime);
									gameFrame.add(lblPlayerLevel);
								}
						    }
						  }
						);
		menu.add(rbMenuItem2);
		
		//Exit Menu
		menu.addSeparator();
		menuItem = new JMenuItem("Exit");
		menuItem.setActionCommand("Exit");
		menuItem.addActionListener(
						  new ActionListener() 
						  {
						    public void actionPerformed(ActionEvent e)
						    {
						    	if (e.getActionCommand().equals("Exit"))
								{
						    		gameFrame.dispose();
									System.exit(0);
								}
						    }
						  }
						);
		menu.add(menuItem);
			
		gameFrame.setJMenuBar(menuBar);
		
		rbMenuItem2.setEnabled(true);
	}

	public void setUpHighScore()
	{
		File file = new File("highscore.txt");
		
		try 
		{	
			BufferedReader input2 = new BufferedReader(new FileReader(file));
			TOP_SCORE = Integer.parseInt(input2.readLine());
			input2.close();
			
			if (PLAYER_SCORE > TOP_SCORE)
			{
				// Create a 'BufferedWriter' object from a 'FileWriter' object
				BufferedWriter output2 = new BufferedWriter(new FileWriter(file));
				output2.write("" + PLAYER_SCORE);
				output2.close(); 
				
				BufferedReader input3 = new BufferedReader(new FileReader(file));
				TOP_SCORE = Integer.parseInt(input3.readLine());
				input3.close();
			}
		}
		catch (Exception error)
		{
		}	
	}
	
	public void actionPerformed(ActionEvent event)
	{
		
//		if (PLAYER_LIVES < 1)
//		{
//			setUpLife();
//		}
//		
//		if (PLAYER_LEVEL == 6)
//		{
//			setUpLife();
//		}
		
		//Time Count down
		PLAYER_TIME_LEFT = PLAYER_TIME_LEFT - 1;
		lblPlayerTime.setText("Time Left: " + PLAYER_TIME_LEFT);
		
		//Stop's Game if time = 0
		if (PLAYER_TIME_LEFT == 0)
		{
			lblGameOver.setVisible(true);
			lblGameOverScore.setVisible(true);
			
			timer.stop();
		}
		
		//Game Over Actions
		if (PLAYER_LIVES == 0 || PLAYER_TIME_LEFT == 0)
		{
			setUpHighScore();
			timer.stop();
			gameFrame.getContentPane().removeAll();
			missiles.removeAll(missiles);
			
			// Display the "Game Over" JLabel
			gameFrame.add(lblGameOver);
			gameFrame.add(lblGameOverScore);
			gameFrame.add(lblGameTopScore);
			lblGameOverScore.setText("Score: " + PLAYER_SCORE);
			lblGameTopScore.setText("Top Score " + TOP_SCORE);
			lblGameOver.setVisible(true);
			lblGameOverScore.setVisible(true);
			lblGameTopScore.setVisible(true);
		}
		
		// Change the shooter's position if the player is pressing the left
		// or right arrow keys
		if (pressedLeft && shooterX > 4)
			shooterX -= SHOOTER_SPEED;
		if (pressedRight && shooterX < FIELD_WIDTH - lblShooter.getWidth() - 6 - 4)
			shooterX += SHOOTER_SPEED;
		lblShooter.setLocation(shooterX, shooterY);
		
		//Dropping Bombs
		BOMB_DROP = BOMB_DROP + 1;
		
		if (BOMB_DROP == DIF_BOMBS)
		{
			BOMB_DROP = 0;
		}

		// Move the remaining aliens across the playing field
		for (int i = 0; i < aliens.size(); i++)
		{
			Alien alien = aliens.get(i);
			alien.moveAlien();
		}

		// Move the existing missiles up the playing field
		for (int j = 0; j < missiles.size(); j++)
		{
			Missile missile = missiles.get(j);
			missile.moveMissile();

			// If the missile gets past the top of the playing field, remove it
			if (missile.getY() < 0 - missile.getHeight())
			{
				gameFrame.getContentPane().remove(missile.getMissileImage());
				missiles.remove(j);
			}
		}
		
		// Move the existing Bombs down the playing field
		for (int j = 0; j < bombs.size(); j++)
		{
			Bomb bomb = bombs.get(j);
			bomb.moveBomb();

			// If the bomb gets past the bottom of the playing field, remove it
			if (bomb.getY() < 0 - bomb.getHeight())
			{
				gameFrame.getContentPane().remove(bomb.getBombImage());
				bombs.remove(j);
			}
		}

		//Prevents user from pressing and holding space bar
		if (pressedSpace && !missileFired)
		{
			// Determine the width and height of the missile being launched
			Missile tempMissile = new Missile(0, 0);
			int missileWidth = tempMissile.getWidth();
			int missileHeight = tempMissile.getHeight();

			// Set the starting position of the missile being launched 
			int x = shooterX + (lblShooter.getWidth() / 2) - (missileWidth / 2);
			int y = FIELD_HEIGHT - lblShooter.getHeight() - 30 - missileHeight;

			// Create a new 'Missile' object and add it to the 'missiles' ArrayList 
			missiles.add(new Missile(x, y));

			missileFired = true;
		}
		
		//Randomly drops bombs & increases difficulty 
		if (BOMB_DROP == DIF_BOMBS - 1)
		{
			// Set the starting position of the bomb being launched 
			Random rand = new Random();
			
			int alienDATA = 0;
			
			alienDATA = rand.nextInt(aliens.size());
			
			int x = aliens.get(alienDATA).getX();
			int y = aliens.get(alienDATA).getY();

			// Create a new 'Bomb' object and add it to the 'Bomb' ArrayList 
			bombs.add(new Bomb(x, y));

			bombFired = true;
		}

		// Draw the aliens (all types)
		for (int i = 0; i < aliens.size(); i++)
		{
			Alien alien = aliens.get(i);
			JLabel aLabel = alien.getAlienImage();
			aLabel.setLocation(alien.getX(), alien.getY());
			aLabel.setSize(alien.getWidth(), alien.getHeight());
			gameFrame.add(aLabel);
		}
		
		// Draw the Bombs
		for (int i = 0; i < bombs.size(); i++)
		{
			Bomb bomb = bombs.get(i);
			JLabel mLabel = bomb.getBombImage();
			mLabel.setLocation(bomb.getX(), bomb.getY());
			mLabel.setSize(bomb.getWidth(), bomb.getHeight());
			gameFrame.add(mLabel);
			}
		
		// Draw the missiles
		for (int i = 0; i < missiles.size(); i++)
		{
			Missile missile = missiles.get(i);
			JLabel mLabel = missile.getMissileImage();
			mLabel.setLocation(missile.getX(), missile.getY());
			mLabel.setSize(missile.getWidth(), missile.getHeight());
			gameFrame.add(mLabel);
		}
		
		//Draw the Bunkers
		for (int i = 0; i < bunkers.size(); i++)
		{
			Bunker bunker = bunkers.get(i);
			JLabel pLabel = bunker.getBunkerImage();
			pLabel.setLocation(bunker.getX(), bunker.getY());
			pLabel.setSize(bunker.getWidth(), bunker.getHeight());
			gameFrame.add(pLabel);
		}

		// Redraw/Update the playing field
		gameFrame.repaint();

		checkCollisions();

		// This line synchronizes the graphics state by flushing buffers containing
		// graphics events and forcing the frame drawing to happen now; otherwise,
		// it can sometimes take a few extra milliseconds for the drawing to take
		// place, which can result in jerky graphics movement; this line ensures
		// that the display is up-to-date; it is useful for animation, since it can
		// reduce or eliminate flickering
		Toolkit.getDefaultToolkit().sync();
	}

	// For every alien and missile currently on the playing field, create a
	// "rectangle" around both the alien and the missile, and then check to
	// see if the two rectangles intersect each other
	public void checkCollisions()
	{
		// The 'try-catch' exception trapping is needed to prevent an error from
		// occurring when an element is removed from the 'aliens' and 'missiles'
		// ArrayLists, causing the 'for' loops to end prematurely 
		
		//Missiles hit alien
		for (int i = 0; i < aliens.size(); i++)
			for (int j = 0; j < missiles.size(); j++)
			{
				try
				{
					Rectangle rAlien = new Rectangle(aliens.get(i).getX(), aliens.get(i).getY(),
																aliens.get(i).getWidth(), aliens.get(i).getHeight());
					Rectangle rMissile = new Rectangle(missiles.get(j).getX(), missiles.get(j).getY(),
																  missiles.get(j).getWidth(), missiles.get(j).getHeight());
//					Rectangle rLife = new Rectangle(LifeX, LifeY,
//							  bombs.get(j).getWidth(), bombs.get(j).getHeight());

					// If an alien and a missile intersect each other, remove both
					// of them from the playing field and the ArrayLists
					if (rAlien.intersects(rMissile))
					{
						gameFrame.getContentPane().remove(aliens.get(i).getAlienImage());
						aliens.remove(i);
						gameFrame.getContentPane().remove(missiles.get(j).getMissileImage());
						missiles.remove(j);
						
						PLAYER_SCORE = PLAYER_SCORE + 5;
						
						lblGameScore.setText("Score: " + PLAYER_SCORE);
					}
					
//					if (rLife.intersects(rMissile))
//					{
//						LifeLabel.setVisible(false);
//						
//						PLAYER_LIVES = PLAYER_LIVES + 1;
//						
//						lblPlayerLives.setText("Lives: " + PLAYER_LIVES);
//					}
				}
				catch (Exception error)
				{
				}
			}
		
		//Gets rid of bomb if hits player
		for (int i = 0; i < 1; i++)
			for (int j = 0; j < bombs.size(); j++)
			{
				try
				{
					Rectangle rBomb = new Rectangle(bombs.get(j).getX(), bombs.get(j).getY(),
							  									bombs.get(j).getWidth(), bombs.get(j).getHeight());
					Rectangle rPlayer = new Rectangle(shooterX, shooterY, 15, 10);
					
					//If a Bomb Hits a player it will remove Health
					if (rBomb.intersects(rPlayer))
					{
						gameFrame.getContentPane().remove(bombs.get(j).getBombImage());
						bombs.remove(j);
						
						PLAYER_LIVES = PLAYER_LIVES - 1;
						lblPlayerLives.setText("Lives: " + PLAYER_LIVES);
					}
				}
				catch (Exception error)
				{
				}
			}
		
		//Bombs hit bunker
		for (int i = 0; i < bunkers.size(); i++)
			for (int j = 0; j < bombs.size(); j++)
			{
				try
				{
					Rectangle rBomb = new Rectangle(bombs.get(j).getX(), bombs.get(j).getY(),
									bombs.get(j).getWidth(), bombs.get(j).getHeight());
					
					Rectangle rBunker = new Rectangle(bunkers.get(i).getX(), bunkers.get(i).getY(),
									bunkers.get(i).getWidth(), bunkers.get(i).getHeight());

					// If an alien and a missile intersect each other, remove both
					// of them from the playing field and the ArrayLists
					
					//Removes Bomb if hits Bunker
					if (rBomb.intersects(rBunker))
					{
						gameFrame.getContentPane().remove(bombs.get(j).getBombImage());
						bombs.remove(j);
						
						gameFrame.getContentPane().remove(bunkers.get(i).getBunkerImage());
						bunkers.remove(i);
					}
				}
				catch (Exception error)
				{
				}
			}
		
		//Missiles Hit Bunker
		for (int i = 0; i < bunkers.size(); i++)
			for (int j = 0; j < missiles.size(); j++)
			{
				try
				{
					Rectangle rBunker = new Rectangle(bunkers.get(i).getX(), bunkers.get(i).getY(),
																bunkers.get(i).getWidth(), bunkers.get(i).getHeight());
					Rectangle rMissile = new Rectangle(missiles.get(j).getX(), missiles.get(j).getY(),
																  missiles.get(j).getWidth(), missiles.get(j).getHeight());

					// If a bunker and a missile intersect each other, remove the
					// missile from the playing field and the ArrayList
					if (rBunker.intersects(rMissile))
					{
						gameFrame.getContentPane().remove(missiles.get(j).getMissileImage());
						missiles.remove(j);
					}
				}
				catch (Exception error)
				{
				}
			}
		
		// If all of the aliens have been destroyed, the game is over, so stop
		// the Timer and remove any remaining missiles from the playing field 
		if (aliens.size() == 0)
		{
			PLAYER_LEVEL = PLAYER_LEVEL + 1;
			gameFrame.getContentPane().removeAll();
			missiles.removeAll(missiles);
			aliens.removeAll(aliens);
			bunkers.removeAll(bunkers);
			bombs.removeAll(bombs);
			
			lblGameOver.setVisible(false);
			lblGameOverScore.setVisible(false);
			lblGameTopScore.setVisible(false);
			
			lblGameScore.setText("Score: " + PLAYER_SCORE);
			lblPlayerLevel.setText("Level: " + PLAYER_LEVEL);
			
			PLAYER_TIME_LEFT = PLAYER_TIME_LEFT + 1000;
			NUM_SMALL_ALIENS = NUM_SMALL_ALIENS + 3;
			NUM_LARGE_ALIENS = NUM_LARGE_ALIENS + 1;
			
			if (DIF_BOMBS >= 50)
			{
				DIF_BOMBS = DIF_BOMBS - 25;
			}
			
			if (PLAYER_LEVEL == 5)
				PLAYER_LIVES = PLAYER_LIVES + 1; 
			
			if (PLAYER_LEVEL == 10)
				PLAYER_LIVES = PLAYER_LIVES + 1;
			
			if (PLAYER_LEVEL == 15)
				PLAYER_LIVES = PLAYER_LIVES + 1;
			
			setUpShooter();
			setUpLargeAliens();
			setUpSmallAliens();
			setUpBunkers();
			
			
			for (int i = 0; i < aliens.size(); i++)
				{
					Alien alien = aliens.get(i);
					alien.changeSpeed(alien.getSpeed() + 1);
				}
					
			lblGameScore.setVisible(true);
			lblPlayerLevel.setVisible(true);
			lblPlayerTime.setVisible(true);
			lblPlayerLives.setVisible(true);
			
			gameFrame.add(lblGameScore);
			gameFrame.add(lblPlayerLevel);
			gameFrame.add(lblPlayerTime);
			gameFrame.add(lblPlayerLives);
			
			gameFrame.repaint();
		}
	}

	// See if the player has PRESSED a key
	public void keyPressed(KeyEvent event)
	{
		int key = event.getKeyCode();

		if (key == KeyEvent.VK_CONTROL) // CONTROL key
			controlKeyPressed = true;

		if ((key == 88) && (controlKeyPressed)) // CONTROL + X
		{
			gameFrame.dispose();
			System.exit(0);
		}
		
		if ((key == 78) && (controlKeyPressed)) // CONTROL + N
		{
			gameFrame.getContentPane().removeAll();
			missiles.removeAll(missiles);
			aliens.removeAll(aliens);
			bunkers.removeAll(bunkers);
			bombs.removeAll(bombs);
			lblGameOver.setVisible(false);
			lblGameOverScore.setVisible(false);
			lblGameTopScore.setVisible(false);
			
			setUpShooter();
			setUpLargeAliens();
			setUpSmallAliens();
			setUpBunkers();
			
			rbMenuItem1.setSelected(true);
			
			PLAYER_SCORE = 0;
			PLAYER_LIVES = 3;
			PLAYER_LEVEL = 1;
			PLAYER_TIME_LEFT = 5000;
			
			lblGameScore.setText("Score: " + PLAYER_SCORE);
			lblPlayerLives.setText("Lives: " + PLAYER_LIVES);
			lblPlayerLevel.setText("Level: " + PLAYER_LEVEL);
			lblGameScore.setVisible(true);
			lblPlayerTime.setVisible(true);
			lblPlayerLives.setVisible(true);
			lblPlayerLevel.setVisible(true);
			
			NUM_SMALL_ALIENS = 8;
			NUM_LARGE_ALIENS = 3;
			NUM_BUNKERS = 7;
			
			timer.start();
			gameFrame.repaint();
			gameFrame.add(lblGameScore);
			gameFrame.add(lblPlayerTime);
			gameFrame.add(lblPlayerLives);
			gameFrame.add(lblPlayerLevel);
		}
		
		if (key == 27) // ESC
		{	
			if (play == true)
			{
				timer.stop();
				play = false;
				lblGamePaused.setVisible(true);
			}
			else
			{
				timer.start();
				play = true;
				lblGamePaused.setVisible(false);
			}
			
		}

		if (key == KeyEvent.VK_LEFT) // LEFT arrow
			pressedLeft = true;
		if (key == KeyEvent.VK_RIGHT) // RIGHT arrow
			pressedRight = true;

		if (key == KeyEvent.VK_SPACE) // SPACE bar
			pressedSpace = true;
	}

	// See if the player has RELEASED a key
	public void keyReleased(KeyEvent event)
	{
		int key = event.getKeyCode();

		if (key == KeyEvent.VK_CONTROL) // CONTROL key
			controlKeyPressed = false;

		if (key == KeyEvent.VK_LEFT) // LEFT arrow
			pressedLeft = false;
		if (key == KeyEvent.VK_RIGHT) // RIGHT arrow
			pressedRight = false;

		if (key == KeyEvent.VK_SPACE) // SPACE bar
		{
			pressedSpace = false;
			missileFired = false;
		}
	}

	public void keyTyped(KeyEvent event)
	{
	}
}

//Points Count
// 5 - Pause / Unpause
//10 - Ctrl-X quit
//10 - implement scoring system
