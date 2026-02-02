package simulator.model;

import java.util.List;

import simulator.misc.Pair;

public class SetContClassEvent extends Event {

	private List<Pair<String, Integer>> cs;

    public SetContClassEvent(int time, List<Pair<String, Integer>> cs) {
        super(time);
        this.cs = cs;
    }

    @Override
    public void execute(RoadMap roadMap) {
        for (Pair<String, Integer> pair : cs) {
            Vehicle vehicle = roadMap.getVehicle(pair.getFirst());
            if (vehicle == null) {
                throw new IllegalArgumentException("El vehículo " + pair.getFirst() + " no existe en el mapa");
            }
            vehicle.setContClass(pair.getSecond());
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Change CO2 Class: [");
        for (int i = 0; i < cs.size(); i++) {
            Pair<String, Integer> pair = cs.get(i);
            sb.append("(").append(pair.getFirst()).append(", ").append(pair.getSecond()).append(")");
            if (i < cs.size() - 1) {
                sb.append(", "); // Agregar coma solo entre elementos
            }
        }
        sb.append("]");
        return sb.toString();
    }

}
