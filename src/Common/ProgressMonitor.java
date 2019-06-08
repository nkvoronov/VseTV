package Common;

import javax.swing.event.*;
import java.util.Vector;

public class ProgressMonitor {
    private int total;
    private int current;
    private boolean indeterminate;
    private int milliSecondsToWait = 500;
    private String status;

    private Vector<ChangeListener> listeners = new Vector<>();
    private ChangeEvent ce = new ChangeEvent(this);

    public ProgressMonitor(int total, boolean indeterminate, int milliSecondsToWait){
        this.total = total;
        this.indeterminate = indeterminate;
        this.milliSecondsToWait = milliSecondsToWait;
    }

    public ProgressMonitor(int total, boolean indeterminate){
        this.total = total;
        this.indeterminate = indeterminate;
    }

    public void start(String status){
        this.status = status;
        current = 0;
        ProgressChangeEvent();
    }

    public void addChangeListener(ChangeListener listener){
        listeners.add(listener);
    }

    public void removeChangeListener(ChangeListener listener){
        listeners.remove(listener);
    }

    private void ProgressChangeEvent(){
        for (ChangeListener listener : listeners) {
            (listener).stateChanged(ce);
        }
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(String status, int current) {
        this.current = current;
        this.status = status;
        ProgressChangeEvent();
    }

    public boolean isIndeterminate() {
        return indeterminate;
    }

    public void setIndeterminate(boolean indeterminate) {
        this.indeterminate = indeterminate;
    }

    public int getMilliSecondsToWait() {
        return milliSecondsToWait;
    }

    public void setMilliSecondsToWait(int milliSecondsToWait) {
        this.milliSecondsToWait = milliSecondsToWait;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
