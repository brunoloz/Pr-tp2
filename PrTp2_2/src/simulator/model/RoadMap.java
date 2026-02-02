package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class RoadMap {

	private List<Junction> _junctions;
	private List<Road> _roads;
	private List<Vehicle> _vehicles;
	private Map<String, Junction> _juncMap;
	private Map<String, Road> _roadMap;
	private Map<String, Vehicle> _vehicleMap;

	protected RoadMap() {

		this._junctions = new ArrayList<Junction>();
		this._roads = new ArrayList<Road>();
		this._vehicles = new ArrayList<Vehicle>();
		this._juncMap = new HashMap<String, Junction>();
		this._roadMap = new HashMap<String, Road>();
		this._vehicleMap = new HashMap<String, Vehicle>();
	}

	public void addJunction(Junction j) throws IllegalArgumentException {
		if(_juncMap.containsKey(j.getId()))
				throw new IllegalArgumentException("Ya existe un cruce con este id");
			
		
		_junctions.add(j);
		_juncMap.put(j.getId(), j);
	}

	public void addRoad(Road r) throws IllegalArgumentException {
		
			if (_roadMap.containsKey(r.getId())) {
				throw new IllegalArgumentException("Ya existe una carretera con este id");
			}
		

		if (!_juncMap.containsKey(r.getSrc().getId()) || !_juncMap.containsKey(r.getDest().getId())) {
			throw new IllegalArgumentException("No existe esta carretera en el mapa");
		}

		_roads.add(r);
		_roadMap.put(r.getId(), r);
	}

	public void addVehicle(Vehicle v) {
		if (_vehicleMap.containsKey(v.getId())) {
			throw new IllegalArgumentException("Ya existe un vehiculo con este id");
		}
		List<Junction> itinerary = v.getItinerary();
		int i;
		boolean b = true;
		for (i = 1; i < itinerary.size(); i++) {

			if (itinerary.get(i - 1).roadTo(itinerary.get(i)) == null)
				b = false;

		}
		if (!b)
			throw new IllegalArgumentException("El itinerario de cruces no es válido");

		_vehicles.add(v);
		_vehicleMap.put(v.getId(), v);
	}

	public Junction getJunction(String id) {
		if (_juncMap.containsKey(id)) {
			return _juncMap.get(id);
		} else {
			return null;
		}
	}

	public Road getRoad(String id) {
		if (_roadMap.containsKey(id)) {
			return _roadMap.get(id);
		} else {
			return null;
		}
	}

	public Vehicle getVehicle(String id) {
		if (_vehicleMap.containsKey(id)) {
			return _vehicleMap.get(id);
		} else {
			return null;
		}

	}

	public List<Junction> getJunctions() {
		return Collections.unmodifiableList(_junctions);

	}

	public List<Road> getRoads() {
		return Collections.unmodifiableList(_roads);

	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(_vehicles);

	}

	public void reset() {
		_juncMap.clear();
		_junctions.clear();
		_roadMap.clear();
		_roads.clear();
		_vehicleMap.clear();
		_vehicles.clear();
	}

	public JSONObject report() {
		JSONObject report = new JSONObject();

		// Convertir junctions a JSON
		JSONArray junctionsArray = new JSONArray();
		for (Junction j : _junctions) {
			junctionsArray.put(j.report());
		}
		report.put("junctions", junctionsArray);

		// Convertir roads a JSON
		JSONArray roadsArray = new JSONArray();
		for (Road r : _roads) {
			roadsArray.put(r.report());
		}
		report.put("roads", roadsArray);

		// Convertir vehicles a JSON
		JSONArray vehiclesArray = new JSONArray();
		for (Vehicle v : _vehicles) {
			vehiclesArray.put(v.report());
		}
		report.put("vehicles", vehiclesArray);

		return report;

	}

}
