package simulator.model;

import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator {

	private RoadMap _roadMap;
	private Queue<Event> _priorityQueue;
	private int _time;

	public TrafficSimulator(RoadMap _roadMap, Queue<Event> _priorityQueue, int _time) {
		this._roadMap = _roadMap;
		this._priorityQueue = _priorityQueue;
		this._time = _time;
	}

	public void addEvent(Event e) {

	}

	public void advance() {

	}

	public void reset() {

	}

	public JSONObject report() {
		return null;

	}

}
