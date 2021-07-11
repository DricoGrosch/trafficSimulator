package model;

import java.util.concurrent.Semaphore;


public abstract class RoadItem {

    protected Semaphore semaphore;
    protected String imagePath;
    protected boolean isEntryPoint;
    protected boolean isExitPoint;
    protected int direction;
    protected Car car;
    protected int x;
    protected int y;

    public RoadItem(int x, int y) {
        this.car = null;
        this.direction = 0;
        this.x = x;
        this.y = y;
        this.semaphore = new Semaphore(1);
    }

    public abstract boolean tryAcquire();

    public abstract void release();

    public abstract void addCar(Car car);

    public abstract void removeCar();

    public void setCarImage() {
        if (direction > 4) {
            setImagePath("assets/stone.png");
            if (direction >= 5) {
                setImagePath("assets/car.png");
            }

        } else {
            setImagePath("assets/car.png");
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public boolean isEntryPoint() {
        return isEntryPoint;
    }

    public void setEntryPoint(boolean entryPoint) {
        isEntryPoint = entryPoint;
    }

    public boolean isExitPoint() {
        return isExitPoint;
    }

    public void setExitPoint(boolean exitPoint) {
        isExitPoint = exitPoint;
    }

    public Car getCar() {
        return car;
    }

}
