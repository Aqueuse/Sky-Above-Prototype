package skyAbove;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.atomic.AtomicBoolean;

// Custom key adapter to listen for key events
class CustomkeyListener extends KeyAdapter {
	public static AtomicBoolean leftPressed = new AtomicBoolean(false);
	public static AtomicBoolean rightPressed = new AtomicBoolean(false);
	public static AtomicBoolean downPressed = new AtomicBoolean(false);
	public static AtomicBoolean upPressed = new AtomicBoolean(false);
	public static AtomicBoolean spacePressed = new AtomicBoolean(false);
	public static AtomicBoolean MPressed = new AtomicBoolean(false);

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				leftPressed.set(true);
				break;
			case KeyEvent.VK_RIGHT:
				rightPressed.set(true);
				break;
			case KeyEvent.VK_DOWN:
				downPressed.set(true);
				break;
			case KeyEvent.VK_UP:
				upPressed.set(true);
				break;
			case KeyEvent.VK_SPACE:
				spacePressed.set(true);
				break;
			case KeyEvent.VK_M:
				MPressed.set(true);
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				DrawImageOnBody.srcSprite=0;
				leftPressed.set(false);
				break;
			case KeyEvent.VK_RIGHT:
				DrawImageOnBody.srcSprite=0;
				rightPressed.set(false);
				break;
			case KeyEvent.VK_DOWN:
				DrawImageOnBody.srcSprite=0;
				downPressed.set(false);
				break;
			case KeyEvent.VK_UP:
				DrawImageOnBody.srcSprite=0;
				upPressed.set(false);
				break;
			case KeyEvent.VK_SPACE:
				DrawImageOnBody.srcSprite=0;
				spacePressed.set(false);
				break;
			case KeyEvent.VK_M:
				MPressed.set(false);
				break;
		}
	}
}