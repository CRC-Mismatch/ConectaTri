package br.com.wemind.marketplacetribanco.utils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class provides methods for easily handling a single-task Timer which performs many
 * TimerTask cancellations.
 */
public class TimerManager {

    private Timer timer;
    private String timerName = "unnamed-timer";
    private TimerTask currentTask;
    private boolean isDaemon = false;
    private TimerTask timerTask;
    private int cancelledTasks = 0;
    private int maxCancelledTasks;

    public TimerManager(String timerName) {
        this(timerName, false, 100);
    }

    public TimerManager(String timerName, boolean isDaemon, int maxCancelledTasks) {
        this.timerName = timerName;
        this.isDaemon = isDaemon;
        this.maxCancelledTasks = maxCancelledTasks;
        timer = newTimer();
    }

    public Timer getTimer() {
        return timer;
    }

    /**
     * Schedule new TimerTask to replace the current one.
     *
     * @param timerDelay the delay before the new task is executed
     * @param newTask    the new TimerTask
     */
    public void schedule(int timerDelay, TimerTask newTask) {
        // Cancel ongoing task
        if (timerTask != null) {
            timerTask.cancel();
            cancelledTasks++;
            if (cancelledTasks >= maxCancelledTasks) {
                // Purge timer every 100 cancelled tasks
                timer.purge();
                cancelledTasks = 0;
            }
        }

        timerTask = newTask;

        // Schedule new task
        timer.schedule(timerTask, timerDelay);
    }

    /**
     * Cancels current Timer object via its Timer.cancel() method
     */
    public void cancel() {
        timer.cancel();
    }

    /**
     * Call this to recreate the Timer object.
     * This should always be called before calling schedule() for the first time
     * after having cancelled the current Timer.
     */
    public void restart() {
        timer = newTimer();
    }

    private Timer newTimer() {
        return new Timer(timerName, isDaemon);
    }

}
