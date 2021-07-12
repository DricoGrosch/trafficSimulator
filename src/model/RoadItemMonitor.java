package model;


public class RoadItemMonitor extends AbstractRoadItem {

    @Override
    public RoadItem createRoad(int x, int y) {
        return new RoadMonitor(x, y);
    }

}
