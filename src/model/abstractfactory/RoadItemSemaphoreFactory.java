package br.udesc.dsd.rmts.model.abstractfactory;

import br.udesc.dsd.rmts.model.RoadItem;
import br.udesc.dsd.rmts.model.RoadSemaphore;


public class RoadItemSemaphoreFactory extends AbstractRoadItemFactory {

    @Override
    public RoadItem createRoad(int x, int y) {
        return new RoadSemaphore(x, y);
    }

}
