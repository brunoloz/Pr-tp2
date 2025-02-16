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
}
