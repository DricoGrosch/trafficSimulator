package model;


public class RoadItemSemaphore extends AbstractRoadItem {

    @Override
    public RoadItem createRoad(int x, int y) {
        return new RoadSemaphore(x, y);
    }

}
