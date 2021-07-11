package controller;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import model.Car;
import model.RoadItem;


public class Simulation extends Thread {

    private Queue<Car> cars;
    private MeshController meshController;
    private RoadItem[][] matrix;
    private volatile boolean running;


    public Simulation(MeshController meshController) {
        this.meshController = meshController;
        this.cars = new LinkedList<>();
    }

    @Override
    public void run() {
        if (running) {
            this.loadCarsInQueue();
            this.matrix = this.meshController.getMatrix();
        }

        while (running) {
            outerloop:
            for (int i = 0; i < this.meshController.getLines(); i++) {
                for (int j = 0; j < this.meshController.getColumns(); j++) {
                    if (matrix[i][j].isEntryPoint() && !cars.isEmpty()) {
                        try {
                            sleep(this.meshController.getTimeInterval() * 1000);
                            if (!running) {
                                verifyTerminated();
                                break outerloop;
                            } else {
                                RoadItem item = matrix[i][j];
                                this.meshController.addCar(cars.remove(), item.getX(), item.getY());
                                this.meshController.defineRouteAndStartThread(item.getX(), item.getY());
                                this.meshController.notifyRoadMeshUpdate();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void terminate() {
        running = false;
    }

    public void setRunning() {
        this.running = true;

    }

    public void verifyTerminated() {
        this.meshController.getExecutorService().shutdown();
        boolean finished = false;
        while (!finished) {
            try {
                finished = this.meshController.getExecutorService().awaitTermination(10, TimeUnit.SECONDS);
                if (finished) {
                    this.meshController.notifyMessage("Simulation finished!");
                    this.meshController.setTerminate();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void addCar() {
        Random random = new Random();
        cars.add(new Car());
    }

    public void loadCarsInQueue() {
        Random random = new Random();
        for (int i = 0; i < this.meshController.getNumberOfCars(); i++) {
            cars.add(new Car());
        }
    }

}
