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
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Change Weather: [");
        for (int i = 0; i < ws.size(); i++) {
            Pair<String, Weather> pair = ws.get(i);
            sb.append("(").append(pair.getFirst()).append(", ").append(pair.getSecond()).append(")");
            if (i < ws.size() - 1) {
                sb.append(", "); // Agregar coma solo entre elementos
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
