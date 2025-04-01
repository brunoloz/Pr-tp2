package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetWeatherEvent extends Event {

	private List<Pair<String, Weather>> ws;

    public SetWeatherEvent(int time, List<Pair<String, Weather>> ws) {
        super(time);
        this.ws = ws;
    }

    @Override
    public void execute(RoadMap roadMap) {
        for (Pair<String, Weather> pair : ws) {
            Road road = roadMap.getRoad(pair.getFirst());
            if (road == null) {
                throw new IllegalArgumentException("La carretera " + pair.getFirst() + " no existe en el mapa");
            }
            road.setWeather(pair.getSecond());
        }
    }
}
