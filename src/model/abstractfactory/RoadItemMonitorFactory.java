package br.udesc.dsd.rmts.model.abstractfactory;

import br.udesc.dsd.rmts.model.RoadItem;
import br.udesc.dsd.rmts.model.RoadMonitor;


public class RoadItemMonitorFactory extends AbstractRoadItemFactory {

    @Override
    public RoadItem createRoad(int x, int y) {
        return new RoadMonitor(x, y);
    }

}
