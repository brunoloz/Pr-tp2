package simulator.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class Junction extends SimulatedObject {

	private List<Road> _enterRoads;
	private Map<Junction, Road> _outRoadsMap;
	private List<List<Vehicle>> _queue;
	private Map<Road, List<Vehicle>> _queueByRoad; // para busqueda en la cola de una carretera dada
	private int _greenLightIndex;
	private int _lightStepChange; // ultimo paso de cambio de semaforo
	private LightSwitchingStrategy _lsStrategy;
	private DequeuingStrategy _dqStrategy;
	private int xCoor;
	private int yCoor;

	protected Junction(String id, LightSwitchingStrategy lsStrategy, DequeuingStrategy dqStrategy, int xCoor, int yCoor)
			throws IllegalArgumentException {
		super(id);
		if (lsStrategy == null)
			throw new IllegalArgumentException("lsStrategy no puede ser null.");

		if (dqStrategy == null)
			throw new IllegalArgumentException("dqStrategy no puede ser null.");

		if (xCoor < 0)
			throw new IllegalArgumentException("xCoor no puede ser negativo.");

		if (yCoor < 0)
			throw new IllegalArgumentException("yCoor no puede ser negativo.");

		_greenLightIndex = -1; //inicialmente están todos en rojo
		_lightStepChange = 0;
		_enterRoads = new ArrayList<Road>();
		_outRoadsMap = new HashMap<Junction, Road>();
		_queue = new ArrayList<List<Vehicle>>();
		_queueByRoad = new HashMap<Road, List<Vehicle>>();
		_lsStrategy = lsStrategy;
		_dqStrategy = dqStrategy;
		this.xCoor = xCoor;
		this.yCoor = yCoor;

	}

	public void addIncommingRoad(Road r) throws IllegalArgumentException {

		if (r.getDest().equals(this)) {
			_enterRoads.add(r);
			List<Vehicle> queue = new ArrayList<Vehicle>();
			_queue.add(queue);
			_queueByRoad.put(r, queue);
		} else {
			throw new IllegalArgumentException("La carretera no tiene como destino el objeto actual.");
		}

	}

	public void addOutGoingRoad(Road r) throws IllegalArgumentException {
		if (r.getSrc().equals(this))
			_outRoadsMap.put(r.getDest(), r);
		else
			throw new IllegalArgumentException("La carretera no es saliente al cruce actual.");
	}

	public void enter(Vehicle v) {
		Road r = v.getRoad();
		_queueByRoad.get(r).add(v);
	}

	public Road roadTo(Junction j) {
		return _outRoadsMap.get(j);
	}

	public void advance(int currTime) {


		if (_greenLightIndex != -1) {
			Road r = _enterRoads.get(_greenLightIndex);
			List<Vehicle> q = _queueByRoad.get(r);
			List<Vehicle> q_to_advance = new ArrayList<>(_dqStrategy.dequeue(q));
						
			for (Vehicle v : q_to_advance) {
				
				v.moveToNextRoad();
				q.remove(v);
				
			}
		}

		int i = _lsStrategy.chooseNextGreen(_enterRoads, _queue, _greenLightIndex, _lightStepChange, currTime);
		if (i != _greenLightIndex) {
			_greenLightIndex = i;
			_lightStepChange = currTime; 
			
		}
	}

	public JSONObject report() {
		JSONObject j = new JSONObject();

		j.put("id", super.getId());
		if (_greenLightIndex == -1) {
			j.put("green", "none");
		} else {
			j.put("green", _enterRoads.get(_greenLightIndex).getId());
		}

		JSONArray queuesArray = new JSONArray();
		for (Road road : _enterRoads) {
			JSONObject queue = new JSONObject();
			queue.put("road", road.getId());

			// Obtener la lista de vehículos en la cola
			JSONArray vehiclesArray = new JSONArray();
			if (_queueByRoad.get(road) != null) {
				for (Vehicle v : _queueByRoad.get(road)) {
					
					vehiclesArray.put(v.getId());
					
				}
			}
			queue.put("vehicles", vehiclesArray);
			queuesArray.put(queue);
		}

		j.put("queues", queuesArray);
		return j;

	}
	
	
	public int getX() {
		return xCoor;
	}
	
	public int getY() {
		return yCoor;
	}
	
	
	public int getGreenLightIndex() {
		return _greenLightIndex;
	}

	public List<Road> getInRoads() {
		return _enterRoads;
	}
	
	public List<List<Vehicle>> getQueues(){
		return _queue;
	}
	
}
