package simulator.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class Vehicle extends SimulatedObject implements Comparable<Vehicle> {

	private List<Junction> _itinerary;
	private int _maxSpeed;
	private int _currentSpeed;
	private VehicleStatus _state;
	private Road _currentRoad;
	private int _location;
	private int _contClass;
	private int _totalCO2;
	private int _distance;
	private int _contJunctions;


	protected Vehicle(String id, int maxSpeed, int contClass, List<Junction> itinerary)
			throws IllegalArgumentException {
		super(id);
		// TODO complete

		if (maxSpeed < 1) {
			throw new IllegalArgumentException("NO SE PUEDE CREAR UN VEHICULO CON UNA MAXSPEED NEGATIVA");
		}

		if (contClass > 10 || contClass < 0) {
			throw new IllegalArgumentException("CONTCLASS TIENE QUE SER UN VALOR ENTRE 0 Y 10");
		}

		if (itinerary.size() < 2) {
			throw new IllegalArgumentException("LA LONGITUD DE ITINERARY TIENE QUE SER AL MENOS DE 2");
		}

		_maxSpeed = maxSpeed;
		_contClass = contClass;
		_itinerary = Collections.unmodifiableList(new ArrayList<>(itinerary));
		// _currentRoad = new CityRoad("r1", _itinerary.getFirst(), itinerary.get(1),
		// 100, 10, 1000, Weather.SUNNY);
		_currentRoad = null;
		_contJunctions = 0;
		_state = VehicleStatus.PENDING; // ?

	}

	void setSpeed(int s) throws IllegalArgumentException { // USAR ESTOS SETTERS EN EL CONSTRUCTOR Y PONERLOS COMO
															// PRIVADOS??
		if (s < 0)
			throw new IllegalArgumentException("NO SE PUEDE SETTEAR LA SPEED CON UN VALOR NEGATIVO");

		if (_state == VehicleStatus.TRAVELING) {
			if (s > _maxSpeed)
				_currentSpeed = _maxSpeed;
			else
				_currentSpeed = s;
		}
	}

	void setContClass(int c) throws IllegalArgumentException {
		if (c > 10 || c < 0)
			throw new IllegalArgumentException("CONTCLASS TIENE QUE SER UN VALOR ENTRE 0 Y 10");

		_contClass = c;

	}

	void advance(int currTime) {
		int v1 = 0;
		if (_state == VehicleStatus.TRAVELING) {
			// (a)
			v1 = _location + _currentSpeed;
			int previousLocation = _location;
			if (v1 < _currentRoad.getLength())
				_location = v1;
			else
				_location = _currentRoad.getLength();

			// (b)
			int c = (_location - previousLocation) * _contClass;
			_totalCO2 += c;
			_currentRoad.addContamination(c);
			_distance += (_location - previousLocation);

			// (c)
			if (_location >= _currentRoad.getLength()) {
				//_currentRoad.getSrc().enter(this);
				
				_state = VehicleStatus.WAITING;
				_currentSpeed = 0;
				_location = 0;
				_contJunctions++;
				_currentRoad.getDest().enter(this);

			}

		}
	}

	void moveToNextRoad() throws IllegalArgumentException {

		if (_state != VehicleStatus.PENDING && _state != VehicleStatus.WAITING)
			throw new IllegalArgumentException("SOLO PUEDE CAMBIAR DE CARRETERA EN ESTADO PENDING Y WAITING");

		if (_state == VehicleStatus.PENDING) { // Si no ha entrado en la primera carretera del itinerario
			Junction j = _itinerary.getFirst();
			Junction j1 = _itinerary.get(1);
			_currentRoad = j.roadTo(j1);
//			_currentRoad = new CityRoad("r1", _itinerary.getFirst(), _itinerary.get(1), 100, 10, 1000, Weather.SUNNY);
			_currentRoad.enter(this);
			_state = VehicleStatus.TRAVELING;

		} else {
			if (_currentRoad != null) { // Si está en una carretera
				_currentRoad.exit(this);
				if (_state == VehicleStatus.WAITING && _contJunctions == _itinerary.size()) { // Si está esperando en el
																								// ultimo cruce
					_state = VehicleStatus.ARRIVED;
					_currentRoad = null;
					_currentSpeed = 0;
				} else {
					Junction j = _itinerary.get(_contJunctions);
					Junction j1 = _itinerary.get(_contJunctions + 1);
					_currentRoad = j.roadTo(j1);
					_currentRoad.enter(this);
					_state = VehicleStatus.TRAVELING;

				}
			}
		}

	}

	public JSONObject report() {
		JSONObject jo1 = new JSONObject();

		if (_state == VehicleStatus.ARRIVED || _state == VehicleStatus.PENDING) {
			jo1.put("id", super.getId().toString());
			jo1.put("speed", _currentSpeed);
			jo1.put("distance", _distance);
			jo1.put("co2", _totalCO2);
			jo1.put("class", _contClass);
			jo1.put("status", _state.toString());
		} else {
			jo1.put("id", super.getId().toString());
			jo1.put("speed", _currentSpeed);
			jo1.put("distance", _distance);
			jo1.put("co2", _totalCO2);
			jo1.put("class", _contClass);
			jo1.put("status", _state.toString());
			jo1.put("road", _currentRoad.toString());
			jo1.put("location", _location);
		}

		return jo1;
	}

	int getLocation() {
		return _location;
	}

	int getSpeed() {
		return _currentSpeed;
	}

	int getMaxSpeed() {
		return _maxSpeed;
	}

	int getContClass() {
		return _contClass;
	}

	VehicleStatus getStatus() {
		return _state;
	}

	int getTotalCO2() {
		return _totalCO2;
	}

	List<Junction> getItinerary() {
		return _itinerary;
	}

	Road getRoad() {
		return _currentRoad;
	}

	@Override
	public int compareTo(Vehicle o) { // TODO para ordenar la lista de vehiculos en Road
		// TODO Auto-generated method stub
		return 0;
	}

}
