package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Road extends SimulatedObject {

	private Junction _srcJunc;
	private Junction _destJunc;
	private int _length;
	private int _maxSpeed;
	private int _speedLimit;
	private int _contLimit;
	private Weather _weather;
	private int _totalCont;
	private List<Vehicle> _vehicles;

	protected Road(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) throws IllegalArgumentException {
		super(id);
		// ...
		if (maxSpeed < 1) {
			throw new IllegalArgumentException("MAXSPEED DEBE SER POSITIVO");
		}
		if (contLimit < 1) {
			throw new IllegalArgumentException("CONTLIMIT DEBE SER POSITIVO");
		}
		if (length < 1) {
			throw new IllegalArgumentException("LENGTH DEBE SER POSITIVO");
		}
		if (srcJunc == null || destJunc == null || weather == null) {
			throw new IllegalArgumentException("NO PUEDE HABER UN VALOR NULL");
		}
		_srcJunc = srcJunc;
		_destJunc = destJunc;
		_srcJunc.addOutGoingRoad(this);
		_destJunc.addIncommingRoad(this);

		_maxSpeed = maxSpeed;
		_speedLimit = maxSpeed;
		_contLimit = contLimit;
		_length = length;
		_weather = weather;
		_vehicles = new ArrayList<Vehicle>();
	}

	public void enter(Vehicle v) {
		if (v.getLocation() != 0 || v.getSpeed() != 0) {
			throw new IllegalArgumentException("NO SE PUEDE AÑADIR EL VEHICULO A LA CARRETERA");
		}
			_vehicles.addLast(v);
			
	}

	public void exit(Vehicle v) {
		_vehicles.remove(v);
	}

	public void setWeather(Weather w) throws IllegalArgumentException {
		if (w == null) {
			throw new IllegalArgumentException("WEATHER NO PUEDE SER NULL");
		}
		_weather = w;
	}

	public void addContamination(int c) {
		if (c < 0) {
			throw new IllegalArgumentException("CONTAMINATION NO PUEDE SER NEGATIVA");
		}
		_totalCont += c;
	}

	abstract void reduceTotalContamination();

	abstract void updateSpeedLimit();

	abstract int calculateVehicleSpeed(Vehicle v);

	public void advance(int currTime) {
		reduceTotalContamination();
		updateSpeedLimit();
		
		for (Vehicle v : _vehicles) {
			v.setSpeed(calculateVehicleSpeed(v));
			v.advance(currTime);

		}

		// Ordena la lista de vehículos por su localizacion
		Collections.sort(_vehicles, new Comparator<Vehicle>() { 
			@Override
			public int compare(Vehicle v1, Vehicle v2) {
				
				return Integer.compare(v2.getLocation(), v1.getLocation());
			}

		});

	}

	public JSONObject report() {
		JSONObject j = new JSONObject();

		j.put("id", super.getId().toString());
		j.put("speedlimit", _speedLimit);
		j.put("weather", _weather.toString());
		j.put("co2", _totalCont);

		JSONArray ja = new JSONArray();
		for (Vehicle v : _vehicles) {
			ja.put(v.getId().toString());
		}

		j.put("vehicles", ja);

		return j;
	}

	public Junction getSrc() {
		return _srcJunc;
	}

	public Junction getDest() {
		return _destJunc;
	}

	public int getLength() {
		return _length;
	}

	public int getMaxSpeed() {
		return _maxSpeed;
	}

	public int getSpeedLimit() {
		return _speedLimit;
	}

	public int getContLimit() {
		return _contLimit;
	}

	public Weather getWeather() {
		return _weather;
	}

	public int getTotalCO2() {
		return _totalCont;
	}

	public List<Vehicle> getVehicles() {
		return Collections.unmodifiableList(_vehicles);
	}

	int get_maxSpeed() {
		return _maxSpeed;
	}

	void set_maxSpeed(int _maxSpeed) {
		this._maxSpeed = _maxSpeed;
	}

	void set_srcJunc(Junction _srcJunc) {
		this._srcJunc = _srcJunc;
	}

	void set_destJunc(Junction _destJunc) {
		this._destJunc = _destJunc;
	}

	void set_length(int _length) {
		this._length = _length;
	}

	void set_speedLimit(int _speedLimit) {
		this._speedLimit = _speedLimit;
	}

	void set_contLimit(int _contLimit) {
		this._contLimit = _contLimit;
	}

	void set_weather(Weather _weather) {
		this._weather = _weather;
	}

	void set_totalCont(int _totalCont) {
		this._totalCont = _totalCont;
	}

	void set_vehicles(List<Vehicle> _vehicles) {
		this._vehicles = _vehicles;
	}

}
