package simulator.model;

import java.util.List;

public class MostCrowdedStrategy implements LightSwitchingStrategy {

	private int _timeSlot;

	public MostCrowdedStrategy(int timeSlot) {
		_timeSlot = timeSlot;
	}

	@Override
	public int chooseNextGreen(List<Road> roads, List<List<Vehicle>> qs, int currGreen, int lastSwitchingTime,
			int currTime) {
		// TODO Auto-generated method stub
		if (roads.isEmpty()) {
			return -1;
		}

		if (currGreen != -1 && (currTime - lastSwitchingTime) < _timeSlot) {
			return currGreen;
		}

		// Find the most crowded road in a circular manner
		return findMostCrowded(qs, (currGreen + 1) % qs.size());
	}

	private int findMostCrowded(List<List<Vehicle>> qs, int startIndex) {
		int maxIndex = startIndex;
		int maxQueueSize = qs.get(startIndex).size();
		int n = qs.size();

		for (int i = 1; i < n; i++) {
			int index = (startIndex + i) % n; // Búsqueda circular
			if (qs.get(index).size() > maxQueueSize) {
				maxQueueSize = qs.get(index).size();
				maxIndex = index;
			}
		}

		return maxIndex;
	}

}
