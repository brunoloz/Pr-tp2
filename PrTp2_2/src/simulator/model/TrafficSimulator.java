package simulator.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.json.JSONObject;

public class TrafficSimulator implements  Observable<TrafficSimObserver>{

	private RoadMap _roadMap;
	private Queue<Event> _priorityQueue;
	private int _time;
	private List<TrafficSimObserver> _observer;
	

	public TrafficSimulator() {
		this._roadMap = new RoadMap();
		this._priorityQueue = new PriorityQueue<Event>();
		this._observer = new ArrayList<TrafficSimObserver>();
		this._time = 0;
	}

	
	public void addEvent(Event e) {
		if(e.getTime() <= _time)
			throw new IllegalArgumentException("No puedes añadir eventos en el pasado");
		
		_priorityQueue.add(e);
		for(TrafficSimObserver t : _observer) {
			t.onEventAdded(_roadMap, _priorityQueue, e, _time);
		}
		//_observer.onEventAdded(_roadMap, _priorityQueue, e, _time);

	}

	public void advance() {
		_time++;
		
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
		
		for(TrafficSimObserver t : _observer) {
			t.onAdvance(_roadMap, _priorityQueue, _time);
		}
		
		
	}

	public void reset() {
		_roadMap.reset();
		_priorityQueue.clear();
		_time = 0;
		
		for(TrafficSimObserver t : _observer) {
			t.onReset(_roadMap, _priorityQueue, _time);
		}

	}

	public JSONObject report() {
		
		JSONObject j = new JSONObject();
		j.put("time", _time);
		j.put("state", _roadMap.report());
		
		return j;

	}


	@Override
	public void addObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		if (o == null) {
	        throw new IllegalArgumentException("Observer cannot be null");
	    }
	   // this._observer = o; // Asignar el observador
	    o.onRegister(_roadMap, _priorityQueue, _time);
	}


	@Override
	public void removeObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		
	}


}
