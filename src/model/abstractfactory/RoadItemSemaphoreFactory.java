package model.abstractfactory;

import model.RoadItem;
import model.RoadSemaphore;


public class RoadItemSemaphoreFactory extends AbstractRoadItemFactory {

    @Override
    public RoadItem createRoad(int x, int y) {
        return new RoadSemaphore(x, y);
    }

}
