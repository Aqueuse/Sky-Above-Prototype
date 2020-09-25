package skyAbove;

import java.util.Timer;
import java.util.TimerTask;

public class Notifications {
	// show a message in the upper right corner
	// who disappear after 3 secondes
	public static String messageNotification = "";
	public static void setNotificationMessage(String message) {
		messageNotification = message;
		timeUp();
	}
	
	public static void timeUp() {
	    Timer timer = new Timer();
	    long delay = 3000L;
	    TimerTask task = new TimerTask() {
		    public void run() {
	        	messageNotification = "";
	        }
	    };
	    timer.schedule(task, delay);
	}
}