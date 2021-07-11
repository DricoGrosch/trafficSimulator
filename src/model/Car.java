package model;

import controller.MeshController;

import java.util.*;


public class Car implements Runnable {

    private Queue<RoadItem> route;
    private RoadItem matrix[][];
    private int currentX;
    private int currentY;
    private Queue<RoadItem> positions;

    private Random random;
    private RoadItem currentRoad;
    private MeshController meshController;
    private int velocity;
    private int amountOfChoices;

    public Car() {
        this.route = new LinkedList<>();
        this.meshController = MeshController.getInstance();
        this.matrix = meshController.getMatrix();
        this.positions = new LinkedList<>();

        this.currentRoad = null;
        this.amountOfChoices = 0;
        this.random = new Random();
        this.velocity = random.nextInt(100) + 200;

    }

    @Override
    public void run() {
        boolean free = false;

        while (!route.isEmpty()) {
            boolean moved = false;
            RoadItem item = route.remove();
            if (item.direction <= 4) {
                free = false;
            }
            try {
                Thread.sleep(velocity);
            } catch (Exception ie) {
                ie.printStackTrace();
            }
            do {
                Random rand = new Random();
                try {

                    if (item.direction > 4 && !free) {
                        positions.clear();
                        positions.add(item);
                        for (RoadItem roadItem : route) {
                            if (roadItem.direction > 4) {
                                if (!positions.contains(roadItem))
                                    positions.add(roadItem);
                            } else {
                                break;
                            }
                        }
                        Queue<RoadItem> auxPositions = positions;
                        if (this.meshController.tryAcquire(positions)) {
                            free = true;
                        } else {
                            free = false;
                            Thread.sleep(200 + rand.nextInt(400));
                        }
                        this.meshController.release(auxPositions);
                        moved = false;
                    } else {
                        this.meshController.addCar(this, item.getX(), item.getY());
                        this.meshController.removeCar(currentRoad.getX(), currentRoad.getY());
                        this.currentRoad = item;
                        moved = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(100);
                    } catch (Exception ie) {
                        ie.printStackTrace();
                    }
                }
            } while (!moved);
        }
        this.meshController.removeThread(currentRoad.getCar());
        this.meshController.removeCar(currentRoad.getX(), currentRoad.getY());
    }

    public void defineRoute(int x, int y) {
        boolean isExitPoint = false;
        this.currentX = x;
        this.currentY = y;

        while (!isExitPoint) {
            switch (matrix[currentX][currentY].getDirection()) {
                case 1:
                    this.currentX--;
                    break;
                case 2:
                    this.currentY++;
                    break;
                case 3:
                    this.currentX++;
                    break;
                case 4:
                    this.currentY--;
                    break;
                default:
                    chooseCrossroad(matrix[currentX][currentY].getDirection());
                    break;
            }
            route.add(matrix[currentX][currentY]);
            if (matrix[currentX][currentY].isExitPoint())
                isExitPoint = true;
        }
    }

    private void chooseCrossroad(int direction) { // QUANTIDADE DE OPÇÕES DE CAMINHO QUE O CARRO PODE SEGUIR
        Random random = new Random();
        int num;
        switch (direction) {
            case 5:
                this.currentX--;
                this.amountOfChoices++;
                break;
            case 6:
                this.currentY++;
                this.amountOfChoices++;
                break;
            case 7:
                this.currentX++;
                this.amountOfChoices++;
                break;
            case 8:
                this.currentY--;
                this.amountOfChoices++;
                break;
            case 9:
                if (amountOfChoices == 3) {
                    this.currentY++;
                    this.amountOfChoices = 0;
                } else {
                    num = random.nextInt(2);
                    if (num == 0)
                        this.currentX--;
                    else
                        this.currentY++;

                    this.amountOfChoices++;
                }
                break;
            case 10:
                if (amountOfChoices == 3) {
                    this.currentX--;
                    this.amountOfChoices = 0;
                } else {
                    num = random.nextInt(2);
                    if (num == 0)
                        this.currentX--;
                    else
                        this.currentY--;

                    this.amountOfChoices++;
                }
                break;
            case 11:
                if (amountOfChoices == 3) {
                    this.currentX++;
                    this.amountOfChoices = 0;
                } else {
                    num = random.nextInt(2);
                    if (num == 0)
                        this.currentY++;
                    else
                        this.currentX++;

                    this.amountOfChoices++;
                }
                break;
            case 12:
                if (amountOfChoices == 3) {
                    this.currentY--;
                    this.amountOfChoices = 0;
                } else {
                    num = random.nextInt(2);
                    if (num == 0)
                        this.currentX++;
                    else
                        this.currentY--;

                    this.amountOfChoices++;
                }
                break;
        }

    }


    public RoadItem getCurrentRoad() {
        return currentRoad;
    }

    public void setCurrentRoad(RoadItem currentRoad) {
        this.currentRoad = currentRoad;
    }


}