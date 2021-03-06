package xyz.gnas.elif.core.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Contains information about an operation
 */
public class Operation {
    private String name;

    private StringProperty suboperationName = new SimpleStringProperty();

    private BooleanProperty paused = new SimpleBooleanProperty();

    private BooleanProperty stopped = new SimpleBooleanProperty();

    private BooleanProperty complete = new SimpleBooleanProperty();

    private DoubleProperty completedAmount = new SimpleDoubleProperty();

    public String getName() {
        return name;
    }

    public String getSuboperationName() {
        return suboperationName.get();
    }

    public StringProperty suboperationNameProperty() {
        return suboperationName;
    }

    public void setSuboperationName(String suboperationName) {
        this.suboperationName.set(suboperationName);
    }

    public boolean isPaused() {
        return paused.get();
    }

    public BooleanProperty pausedProperty() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused.set(paused);
    }

    public boolean isStopped() {
        return stopped.get();
    }

    public BooleanProperty stoppedProperty() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped.set(stopped);

        if (stopped) {
            this.paused.set(false);
        }
    }

    public boolean isComplete() {
        return complete.get();
    }

    public BooleanProperty completeProperty() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete.set(complete);
    }

    /**
     * @return the completed amount of the operation, from 0 to 1
     */
    public double getCompletedAmount() {
        return completedAmount.get();
    }

    public DoubleProperty completedAmountProperty() {
        return completedAmount;
    }

    public void setCompletedAmount(double completedAmount) {
        this.completedAmount.set(completedAmount);
    }

    public Operation(String name) {
        this.name = name;
    }
}
