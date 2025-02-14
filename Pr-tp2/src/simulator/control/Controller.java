package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator _trafficSimulator;
	private Factory<Event> _eventsFactory;

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		// TODO complete
	}

	public void loadEvents(InputStream in) {

	}

	public void run(int n, OutputStream out) {

	}

	public void reset() {

	}
}
