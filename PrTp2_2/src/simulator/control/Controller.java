package simulator.control;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.factories.Factory;
import simulator.model.Event;
import simulator.model.Observable;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.TrafficSimulator;

public class Controller {

	private TrafficSimulator _trafficSimulator;
	private Factory<Event> _eventsFactory;
	

	public Controller(TrafficSimulator sim, Factory<Event> eventsFactory) {
		if (sim == null || eventsFactory == null) {
			throw new IllegalArgumentException("TrafficSimulator and EventFactory cannot be null");
		}
		this._trafficSimulator = sim;
		this._eventsFactory = eventsFactory;
		

	}
	
	
	

	public void loadEvents(InputStream in) {

		if (in == null) {
            throw new IllegalArgumentException("Input stream cannot be null");
        }
        
        JSONObject jo = new JSONObject(new JSONTokener(in));
        if (!jo.has("events") || !jo.get("events").getClass().equals(JSONArray.class)) {
            throw new IllegalArgumentException("Invalid JSON format: missing 'events' array");
        }
        
        JSONArray eventsArray = jo.getJSONArray("events");
        for (int i = 0; i < eventsArray.length(); i++) {
            JSONObject eventObj = eventsArray.getJSONObject(i);
            Event event = _eventsFactory.create_instance(eventObj);
            _trafficSimulator.addEvent(event);
        }
	}

	
	

	
	public void run(int n, OutputStream out) {

		if (n < 0 || out == null) {
            throw new IllegalArgumentException("Invalid number of ticks or output stream is null");
        }
        
		PrintStream p = new PrintStream(out);
		// print "{" to 'p'
		// print "  \"states\": [" to 'p'
		
	    p.println("{");
	    p.println("  \"states\": [");

		// loop for the first n-1 states (to print comma after each state)
		for (int i = 0; i < n - 1; i++) {
		    _trafficSimulator.advance();
		    p.print(_trafficSimulator.report());
		    p.println(",");
		}

		// last step, only if 'n > 0'
		if (n > 0) {
		    _trafficSimulator.advance();
		    p.print(_trafficSimulator.report());
		}

		// print "]" to 'p'
		// print "}" to 'p'
		p.println();
	    p.println("]");
	    p.println("}");
	}

	public void reset() {
		_trafficSimulator.reset();
	}

	//@Override
	public void addObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		_trafficSimulator.addObserver(o);
		
	}

	//@Override
	public void removeObserver(TrafficSimObserver o) {
		// TODO Auto-generated method stub
		_trafficSimulator.removeObserver(o);
		
	}
	
	public void addEvent(Event e) {
		_trafficSimulator.addEvent(e);
	}
	
	public void run(int n) {
		
		for(int i = 0; i < n; i++) {
			_trafficSimulator.advance();
		}
		
	}
}
