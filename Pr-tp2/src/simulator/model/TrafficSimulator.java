package simulator.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator {

	private RoadMap _roadMap;
	private Queue<Event> _priorityQueue;
	private int _time;

	public TrafficSimulator() {
		this._roadMap = new RoadMap();
		this._priorityQueue = new LinkedList<Event>();
		this._time = 0;
	}

	public void addEvent(Event e) {
		if(e.getTime() <= _time)
			throw new IllegalArgumentException("No puedes añadir eventos en el pasado");
		
		_priorityQueue.add(e);
		
	}

	public void advance() {
		_time++;
		for(Event e : _priorityQueue) {
			if(e.getTime() == _time) {
				e.execute(_roadMap);
				_priorityQueue.remove(e);
			}
		}
		
		List<Road> roads = _roadMap.getRoads();
		for(Road r : roads) {
			r.advance(_time);
		}
		
		List<Junction> junctions = _roadMap.getJunctions();
		for(Junction j : junctions) {
			j.advance(_time);
		}
	}

	public void reset() {
		_roadMap.reset();
		_priorityQueue.clear();
		_time = 0;
	}

	public JSONObject report() {
		
		JSONObject j = new JSONObject();
		j.put("time", _time);
		j.put("state", _roadMap.report());
		
		return j;

	}

}
