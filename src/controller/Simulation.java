package controller;

import model.Car;
import model.RoadItem;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class Simulation extends Thread {
    Queue<Car> cars;
    private MeshController meshController;
    private RoadItem[][] matrix;
    private boolean running;


    public Simulation(MeshController meshController) {
        this.meshController = meshController;
        this.cars = new LinkedList<>();
    }

    @Override
    public void run() {
        if (running) {
            this.cars = this.loadCars();
            this.matrix = this.meshController.getMatrix();
        }
        while (running) {
            for (int i = 0; i < this.meshController.getLines(); i++) {
                for (int j = 0; j < this.meshController.getColumns(); j++) {
                    if (matrix[i][j].isEntryPoint() && !cars.isEmpty()) {
                        try {
                            sleep(this.meshController.getTimeInterval() * 1000);
                            RoadItem item = matrix[i][j];
                            this.meshController.addCar(cars.remove(), item.getX(), item.getY());
                            this.meshController.defineRouteAndStartThread(item.getX(), item.getY());
                            this.meshController.notifyRoadMeshUpdate();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    public void setRunning() {
        this.running = true;

    }


    public void addCar() {
        this.cars.add(new Car());
    }

    public Queue<Car> loadCars() {
        Queue<Car> cars = new LinkedList<>();
        for (int i = 0; i < this.meshController.getNumberOfCars(); i++) {
            cars.add(new Car());
        }
        return cars;
    }

}
