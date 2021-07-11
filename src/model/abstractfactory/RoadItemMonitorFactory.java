package model.abstractfactory;

import model.RoadItem;
import model.RoadMonitor;


public class RoadItemMonitorFactory extends AbstractRoadItemFactory {

    @Override
    public RoadItem createRoad(int x, int y) {
        return new RoadMonitor(x, y);
    }

}
