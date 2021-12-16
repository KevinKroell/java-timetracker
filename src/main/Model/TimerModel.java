package main.Model;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class TimerModel extends Observable{ // Or: implements IModel

	private boolean timerRunning;
	private boolean timerPaused;
	private int timerSeconds;
	private int timerMinutes;
	private int timerHours;
	private Timer taskTimer;
	//private IView[] watchingViews;
	
	
	// Constructor
	public TimerModel() {
		super();
		this.timerHours = 0;
		this.timerMinutes = 0;
		this.timerHours = 0;
	}

	// Getter/Setter
	public boolean isTimerRunning() {
		return timerRunning;
	}

	public void setTimerRunning(boolean timerRunning) {
		this.timerRunning = timerRunning;
	}
	
	public boolean isTimerPaused() {
		return timerPaused;
	}

	public void setTimerPaused(boolean timerPaused) {
		this.timerPaused = timerPaused;
	}

	public int getTimerSeconds() {
		return timerSeconds;
	}

	public void setTimerSeconds(int timerSeconds) {
		this.timerSeconds = timerSeconds;
	}

	public int getTimerMinutes() {
		return timerMinutes;
	}

	public void setTimerMinutes(int timerMinutes) {
		this.timerMinutes = timerMinutes;
	}

	public int getTimerHours() {
		return timerHours;
	}

	public void setTimerHours(int timerHours) {
		this.timerHours = timerHours;
	}

//	public IView[] getWatchingViews() {
//		return watchingViews;
//	}
//
//	public void setWatchingViews(IView[] watchingViews) {
//		this.watchingViews = watchingViews;
//	}

	// Additional Methods
	public void startTimer() {
		if (!timerRunning) {
			this.setTimerRunning(true);
			this.setTimerPaused(false);
			taskTimer = new Timer();
			taskTimer.schedule(new TimerTask() {
	            @Override
	            public void run() {
	            	updateTimer();
	            }
	        }, 1000, 1000); // delay one second, one second interval	
		}
	}
	
	public void pauseTimer() {
		if (timerRunning) {
			taskTimer.cancel();
			this.setTimerRunning(false);
			this.setTimerPaused(true);
			setChanged();
			notifyObservers(this);			
		}
	}
	
	public void stopAndResetTimer() {
		if (timerRunning  || timerPaused) {
			taskTimer.cancel();
			this.setTimerRunning(false);
			this.setTimerRunning(false);
			this.setTimerHours(0);
			this.setTimerMinutes(0);
			this.setTimerSeconds(0);
			setChanged();
			notifyObservers(this);			
		}
	}
	
	public void updateTimer() {
		if (timerRunning) {
			timerSeconds += 1;
			if (timerSeconds == 60) {
				timerSeconds = 0;
				timerMinutes += 1;
				if (timerMinutes == 60) {
					timerMinutes = 0;
					timerHours += 1;
				}
			}
		}
		setChanged();
		notifyObservers(this);
	}
	
	public String timerToString() {
		String seconds = Integer.toString(timerSeconds);
		String minutes = Integer.toString(timerMinutes);
		String hours = Integer.toString(timerHours);
		if (timerSeconds < 10) {
			seconds = "0" + timerSeconds;
		}
		if (timerMinutes < 10) {
			minutes = "0" + timerMinutes;
		}
		if (timerHours < 10) {
			hours = "0" + timerHours;
		}
		return hours + ":" + minutes + ":" + seconds;
	}

}
