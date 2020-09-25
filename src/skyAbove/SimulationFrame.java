package skyAbove;

import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.formdev.flatlaf.FlatLightLaf;
import org.dyn4j.world.World;

public abstract class SimulationFrame {
	public static final Color BleuCiel = new Color(193, 229, 247);
	public static final Color DarkerCiel = new Color(187, 223, 241);

	/** The conversion factor from nano to base */
	public static final double NANO_TO_BASE = 1.0e9;

	static BufferedImage backgroundImg;

	// Création du bureau et de la fenetre interne du menu
	public static JDesktopPane desktop = new JDesktopPane();
	public static JInternalFrame menuFrame = new JInternalFrame("Menu");
	public static JInternalFrame gameFrame = new JInternalFrame();

    public static JPanel PanelsContainerGame = new JPanel();
    public static JPanel gamePanel = new JPanel();
	public static JPanel landscapePanel = new JPanel();
	public static Canvas landscapeCanvas = new Canvas();
	public static CardLayout CardGame = new CardLayout();
	public static GridBagLayout gridGame = new GridBagLayout();
	
	public static String messageLabel1;
	public static String messageLabel2;
	public static int messageLabel3;
	public static int messageLabel4;
	
	/** The dynamics engine */
	protected final static World<SimulationBody> world = new World<SimulationBody>();

	/** The pixels per meter scale factor */
	protected final double scale;
	
	private static boolean stopped;
	private static boolean paused;
 	
	/** The time stamp for the last iteration */
	private long last;

	public SimulationFrame(String name, double scale) throws IOException {
		this.scale = scale; // set the scale

		menu.PanelsContainer.setVisible(false);

		gameFrame.setSize(1280, 768);
		gameFrame.setVisible(true);
		gameFrame.setBorder(null);
		menuFrame.setSize(682, 550);
		menuFrame.setVisible(true);
		menuFrame.setLocation(new Point(400, 100));
		menuFrame.setBorder(null);
		menuFrame.setBackground(BleuCiel);
		
	    PanelsContainerGame.setLayout(CardGame);
		PanelsContainerGame.setBackground(BleuCiel);
	    landscapePanel.setLayout(gridGame);
		landscapeCanvas.setSize(1280, 768);
		
		// la internalFrame du jeu
		landscapePanel.add(landscapeCanvas);
		gameFrame.add(gamePanel);
		gamePanel.add(landscapePanel);

		// la internalFrame du menu
		menuFrame.add(PanelsContainerGame);
		desktop.add(menuFrame);
		desktop.add(gameFrame);
		menu.frame.add(desktop);

	    Robot robot = null;
		try {robot = new Robot();} catch (AWTException e) {e.printStackTrace();}
	    robot.mousePress(InputEvent.BUTTON1_MASK);
	    robot.mouseRelease(InputEvent.BUTTON1_MASK);

		this.initializeWorld(); // setup the world
	}

	// Creates fixed game objects and adds them to the world.
	protected abstract void initializeWorld() throws IOException;
	
	/**
	 * Start active rendering the simulation.
	 * This should be called after the JFrame has been shown.
	 */
	private void start() {
		// initialize the last update time
		this.last = System.nanoTime();
		// don't allow AWT to paint the canvas since we are
		landscapeCanvas.setIgnoreRepaint(true);
		// enable double buffering (the JFrame has to be
		// visible before this can be done)
		landscapeCanvas.createBufferStrategy(2);
		// run a separate thread to do active rendering
		// because we don't want to do it on the EDT
		Thread thread = new Thread() {
			public void run() {
				// perform an infinite loop stopped
				// render as fast as possible
				while (!isStopped()) {
					try { gameLoop(); } catch (IOException e1) {e1.printStackTrace();}
					// you could add a Thread.yield(); or
					// Thread.sleep(long) here to give the
					// CPU some breathing room
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {}
				}
			}
		};
		// set the game loop thread to a daemon thread so that
		// it cannot stop the JVM from exiting
		thread.setDaemon(true);
		// start the game loop
		thread.start();
	}
	
	/**
	 * The method calling the necessary methods to update
	 * the game, graphics, and poll for input.
	 * @throws IOException 
	 */
	private void gameLoop() throws IOException {
		// get the graphics object to render to
		Graphics2D g = (Graphics2D) landscapeCanvas.getBufferStrategy().getDrawGraphics();
		
		// by default, set (0, 0) to be the center of the screen with the positive x axis
		// pointing right and the positive y axis pointing up
		this.transform(g);
		
		this.clear(g);

        long time = System.nanoTime(); // get the current time
        // get the elapsed time from the last iteration
        long diff = time - this.last;
        this.last = time; // set the last time
    	// convert from nanoseconds to seconds
    	double elapsedTime = (double)diff / NANO_TO_BASE;

		// render anything about the simulation (will render the World objects)
		this.render(g, elapsedTime);

		if (!paused) { // update the World
	        this.update(g, elapsedTime);
		}

		g.dispose(); // dispose of the graphics object

		// blit/flip the buffer
		BufferStrategy strategy = landscapeCanvas.getBufferStrategy();
		if (!strategy.contentsLost()) {
			strategy.show();
		}

		// Sync the display on some systems.
        // (on Linux, this fixes event queue problems)
        Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * Performs any transformations to the graphics.
	 * By default, this method puts the origin (0,0) in the center of the window
	 * and points the positive y-axis pointing up.
	 * @param g the graphics object to render to
	 */
	protected void transform(Graphics2D g) {
	}
	
	/**
	 * Clears the previous frame.
	 * @param g the graphics object to render to
	 * @throws IOException 
	 */
	protected void clear(Graphics2D g) {
		g.drawImage(SimulationFrame.backgroundImg,  // image generee de la zone
		0, 0,       // dx1, dy1 - x,y destination 1st corner
		1280, 768,   // dx2, dy2 - x,y destination 2nd corner
		BackgoundTheater.incr, 0, // sx1, sy1 - x,y source 1st corner
		BackgoundTheater.incr+1280, 768, // sy1, sy2 - x,y source 2nd corner
		SimplePlatformer.BleuCiel, // bgcolor
		null);     // observer - object to get image modifications 

		g.setFont(new Font("Arial",Font.BOLD,30));
		g.setColor(DrawMapZone.bleuArdoise);
		g.drawString(Notifications.messageNotification, 1000, 40);
	}
	
	public static void updateBackground() throws IOException {
		String background = "usr/background/currentTableau.png";
		backgroundImg = ImageIO.read(new File(background));

	    try { backgroundImg = ImageIO.read(new File(background));} catch (IOException e) { e.printStackTrace(); }
	}

	/**
	 * Renders the example.
	 * @param g the graphics object to render to
	 * @param elapsedTime the elapsed time from the last update
	 */
	protected void render(Graphics2D g, double elapsedTime) {
		g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		// draw all the objects in the world
		for (int i = 0; i < SimulationFrame.world.getBodyCount(); i++) {
			// get the object
			SimulationBody body = (SimulationBody) SimulationFrame.world.getBody(i);
			this.render(g, elapsedTime, body);
		}
	}
	
	/**
	 * Renders the body.
	 * @param g the graphics object to render to
	 * @param elapsedTime the elapsed time from the last update
	 * @param body the body to render
	 */
	protected void render(Graphics2D g, double elapsedTime, SimulationBody body) {
		// draw the object
		body.render(g, this.scale);
	}
	
	/**
	 * Updates the world.
	 * @param g the graphics object to render to
	 * @param elapsedTime the elapsed time from the last update
	 * @throws IOException 
	 */
	// update the world with the elapsed time
	protected void update(Graphics2D g, double elapsedTime) throws IOException {
        SimulationFrame.world.update(elapsedTime);
	}
	
	// Stops the simulation.
	public synchronized void stop() {
		SimulationFrame.stopped = true;
	}
	
	/**
	 * Returns true if the simulation is stopped.
	 * @return boolean true if stopped
	 */
	public boolean isStopped() {
		return SimulationFrame.stopped;
	}
	
	// Pauses the simulation
	public synchronized void pause() {
		SimulationFrame.paused = true;
	}
	
	// Resumes the simulation
	public synchronized void resume() {
		SimulationFrame.paused = false;
	}
	
	/**
	 * Returns true if the simulation is paused.
	 * @return boolean true if paused
	 */
	public boolean isPaused() {
		return SimulationFrame.paused;
	}
	
	// Starts the simulation.
	public void run() {
		// set the look and feel to the system look and feel
		try { UIManager.setLookAndFeel( new FlatLightLaf() ); } catch (UnsupportedLookAndFeelException e) {	e.printStackTrace(); }
		
		// start it
		this.start();
	}
	
}