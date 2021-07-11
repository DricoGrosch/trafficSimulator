package controller;

import controller.observer.Observed;
import model.Car;
import model.RoadItem;
import model.abstractfactory.AbstractRoadItemFactory;

import java.io.File;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;


public interface IMeshController extends Observed {

    void readAndCreateMatrix();

    void runSimulation();


    void addCar(Car car, int x, int y);

    void removeCar(int x, int y);

    void removeThread(Car car);

    void defineRouteAndStartThread(int x, int y);

    void checkCrossPont(int x, int y, int direction);

    void checkEntryPointOnTop(int x, int y, int direction);

    void checkEntryPointOnLeft(int x, int y, int direction);

    void checkEntryPointOnRight(int x, int y, int direction);

    void checkEntryPointOnBottom(int x, int y, int direction);

    String getMatrixPosition(int rowIndex, int columnIndex);

    ExecutorService getExecutorService();

    File getFile();

    int getLines();

    int getColumns();

    RoadItem[][] getMatrix();

    boolean isTerminate();

    void setPathName(File file);

    void setTerminate();

    void setFactory(AbstractRoadItemFactory factory);

    int getTimeInterval();


    int getNumberOfCars();

    void setPositions(int x, int y, List<RoadItem> positions);

    void setNumberOfCars(int numberOfCars);

    void setTimeInterval(int timeInterval);

    boolean tryAcquire(Queue<RoadItem> size);

    void release(Queue<RoadItem> positions);
}
