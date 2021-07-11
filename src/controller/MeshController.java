package controller;

import controller.observer.TableObserver;
import model.Car;
import model.RoadItem;
import model.abstractfactory.AbstractRoadItemFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MeshController {

    private static MeshController instance;
    private List<TableObserver> observers;
    private RoadItem matrix[][];
    private File file = null;
    private Simulation simulation;
    private List<Car> threadList;
    private AbstractRoadItemFactory factory;
    private ExecutorService executorService;
    private int lines;
    private int columns;
    private int numberOfCars;
    private int timeInterval;
    private boolean terminate;

    public static MeshController getInstance() {
        if (instance == null) {
            instance = new MeshController();
        }

        return instance;
    }

    private MeshController() {
        this.observers = new ArrayList<>();
        this.threadList = new ArrayList<>();
        this.terminate = false;
    }

    public void readAndCreateMatrix() {
        try {
            Scanner input = new Scanner(file);
            while (input.hasNextInt()) {
                lines = input.nextInt();
                columns = input.nextInt();
                matrix = new RoadItem[lines][columns];
                for (int i = 0; i < lines; i++) {
                    for (int j = 0; j < columns; j++) {
                        matrix[i][j] = factory.createRoad(i, j);
                        int valueOfPositionOnMesh = input.nextInt();
                        //        verifica pontos de entrada e saída conforme o arquivo de da malha em questão
                        switch (valueOfPositionOnMesh) {
                            case 0:
                                matrix[i][j].setImagePath("assets/default.png");
                                break;
                            case 1:
                                checkEntryPointOnBottom(i, j, 1);
                                break;
                            case 2:
                                checkEntryPointOnLeft(i, j, 2);
                                break;
                            case 3:
                                checkEntryPointOnTop(i, j, 3);
                                break;
                            case 4:
                                checkEntryPointOnRight(i, j, 4);
                                break;
                            default: {
                                checkCrossPont(i, j, valueOfPositionOnMesh);
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void runSimulation() {
        this.terminate = false;
        this.simulation = new Simulation(this);
        this.executorService = Executors.newCachedThreadPool();
        this.simulation.setRunning();
        this.simulation.start();
    }

    public void addCar(Car car, int x, int y) {
        this.matrix[x][y].addCar(car);
        this.threadList.add(matrix[x][y].getCar());
        notifyRoadMeshUpdate();
    }

    public void removeCar(int x, int y) {
        this.matrix[x][y].removeCar();
        notifyRoadMeshUpdate();
    }

    public void checkCrossPont(int x, int y, int direction) {
        this.matrix[x][y].setImagePath("assets/stone.png");
        this.matrix[x][y].setDirection(direction);
    }

    public void defineRouteAndStartThread(int x, int y) {
        if (!terminate) {
            this.matrix[x][y].getCar().defineRoute(x, y);
            this.matrix[x][y].getCar().setCurrentRoad(matrix[x][y]);
            this.executorService.execute(matrix[x][y].getCar());
        }
    }

    public void removeThread(Car car) {
        this.simulation.addCar();
        this.threadList.remove(car);
    }

    public void checkEntryPointOnTop(int x, int y, int direction) {
        if (x - 1 < 0)
            this.matrix[x][y].setEntryPoint(true);
        else if (x + 1 >= this.lines)
            this.matrix[x][y].setExitPoint(true);
        this.matrix[x][y].setDirection(direction);
        this.matrix[x][y].setImagePath("assets/road" + direction + ".png");
    }

    public void checkEntryPointOnLeft(int x, int y, int direction) {
        if (y - 1 < 0)
            this.matrix[x][y].setEntryPoint(true);
        else if (y + 1 >= this.columns)
            this.matrix[x][y].setExitPoint(true);
        this.matrix[x][y].setDirection(direction);
        this.matrix[x][y].setImagePath("assets/road" + direction + ".png");
    }

    public void checkEntryPointOnRight(int x, int y, int direction) {
        if (y + 1 >= this.columns)
            this.matrix[x][y].setEntryPoint(true);
        else if (y - 1 < 0)
            this.matrix[x][y].setExitPoint(true);
        this.matrix[x][y].setDirection(direction);
        this.matrix[x][y].setImagePath("assets/road" + direction + ".png");
    }

    public void checkEntryPointOnBottom(int x, int y, int direction) {
        if (x + 1 >= this.lines)
            this.matrix[x][y].setEntryPoint(true);
        else if (x - 1 < 0)
            this.matrix[x][y].setExitPoint(true);
        this.matrix[x][y].setDirection(direction);
        this.matrix[x][y].setImagePath("assets/road" + direction + ".png");
    }

    public String getMatrixPosition(int rowIndex, int columnIndex) {
        return matrix[rowIndex][columnIndex].getImagePath();
    }

    public File getFile() {
        return file;
    }

    public int getLines() {
        return lines;
    }

    public int getColumns() {
        return columns;
    }

    public int getTimeInterval() {
        return this.timeInterval;
    }


    public ExecutorService getExecutorService() {
        return executorService;
    }


    public void setPathName(File file) {
        this.file = file;
    }


    public void setTerminate() {
        this.terminate = true;
    }


    public void setTimeInterval(int timeInterval) {
        this.timeInterval = timeInterval;
    }


    public boolean tryAcquire(Queue<RoadItem> positions) {
        int count = 0;
        int size = positions.size();
        for (int j = 0; j < size; j++) {
            if (!positions.isEmpty()) {
                RoadItem roadItem = positions.remove();
                if (this.matrix[roadItem.getX()][roadItem.getY()].tryAcquire()) {
                    count++;
                }
            }
        }
        return count == positions.size();
    }


    public void release(Queue<RoadItem> positions) {
        int size = positions.size();
        for (int j = 0; j < size; j++) {
            RoadItem roadItem = positions.remove();
            this.matrix[roadItem.getX()][roadItem.getY()].release();
        }
    }


    public void setNumberOfCars(int numberOfCars) {
        this.numberOfCars = numberOfCars;
    }


    public void setFactory(AbstractRoadItemFactory factory) {
        this.factory = factory;
    }


    public void addObserver(TableObserver observer) {
        this.observers.add(observer);
    }


    public void notifyRoadMeshUpdate() {
        for (TableObserver observer : observers) {
            observer.roadMeshUpdate();
        }
    }


    public int getNumberOfCars() {
        return this.numberOfCars;
    }


    public RoadItem[][] getMatrix() {
        return this.matrix;
    }

}
