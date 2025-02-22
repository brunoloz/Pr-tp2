package simulator.model;

public class CityRoad extends Road {

	CityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length, Weather weather)
			throws IllegalArgumentException {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);
		
//		addContamination(contLimit);
//		setWeather(weather);

	}

	public void reduceTotalContamination() {

		if (this.getWeather() == Weather.WINDY || this.getWeather() == Weather.STORM)
			set_totalCont(this.getTotalCO2() - 10);
		else
			set_totalCont(this.getTotalCO2() - 2);

		if (this.getTotalCO2() < 0)
			set_totalCont(0);
	}

	@Override
	void updateSpeedLimit() {
	}

	@Override
	int calculateVehicleSpeed(Vehicle v) {
		// TODO Auto-generated method stub

		return (((11 - v.getContClass()) * this.getSpeedLimit()) / 11);
	}
}