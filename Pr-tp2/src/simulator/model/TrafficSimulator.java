package simulator.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator {

	private RoadMap _roadMap;
	private Queue<Event> _priorityQueue;
	private int _time;

	public TrafficSimulator() {
		this._roadMap = new RoadMap();
		this._priorityQueue = new PriorityQueue<Event>();
		this._time = 0;
	}

	public void addEvent(Event e) {
		if(e.getTime() <= _time)
			throw new IllegalArgumentException("No puedes añadir eventos en el pasado");
		
		_priorityQueue.add(e);

	}

	public void advance() {
		_time++;
//		Iterator<Event> i = _priorityQueue.iterator();
//		while(i.hasNext()){
//			Event e = i.next();
//			if(e.getTime() == _time) {
//				e.execute(_roadMap);
//				i.remove();
//			}
//		}
		
	    while (!_priorityQueue.isEmpty() && _priorityQueue.peek().getTime() == _time) {
	        Event e = _priorityQueue.poll(); // Elimina el evento de la cola
	        e.execute(_roadMap);  // Ejecuta el evento
	    }
		
		List<Junction> junctions = new ArrayList<>(_roadMap.getJunctions());
		for(Junction j : junctions) {
			j.advance(_time);
		}
		
		List<Road> roads = new ArrayList<>(_roadMap.getRoads());
		for(Road r : roads) {
			r.advance(_time);
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
