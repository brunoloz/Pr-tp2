package simulator.model;

public class InterCityRoad extends Road {

	InterCityRoad(String id, Junction srcJunc, Junction destJunc, int maxSpeed, int contLimit, int length,
			Weather weather) {
		super(id, srcJunc, destJunc, maxSpeed, contLimit, length, weather);

	}

	public void reduceTotalContamination() {

		int x = calculateX(this.getWeather());
		this.set_totalCont(((100 - x) * this.getTotalCO2()) / 100);
	}

	private int calculateX(Weather w) {
		int x = 0;
		switch (w) {
		case SUNNY:
			x = 2;
			break;
		case CLOUDY:
			x = 3;
			break;
		case RAINY:
			x = 10;
			break;
		case WINDY:
			x = 15;
			break;
		case STORM:
			x = 20;
			break;
		}
		return x;
	}

	public void updateSpeedLimit() {

		if (this.getTotalCO2() > this.getContLimit())
			this.set_speedLimit(getMaxSpeed() / 2);
		else
			this.set_speedLimit(getMaxSpeed());

	}

	public int calculateVehicleSpeed(Vehicle v) {

		int s = this.getSpeedLimit();
		if (this.getWeather() == Weather.STORM) {
			s = (this.getSpeedLimit() * 8) / 10;
			// v.setSpeed(s);???
		}

		return s;
	}

}
