package simulator.model;

public abstract class Event implements Comparable<Event> {

	private static long _counter = 0;

	protected int _time;
	protected long _time_stamp;

	Event(int time) {
		if (time < 1)
			throw new IllegalArgumentException("Invalid time: " + time);
		else {
			_time = time;
			_time_stamp = _counter++;
		}
	}

	int getTime() {
		return _time;
	}

	@Override
	public int compareTo(Event other) {
	    if (this._time != other._time) {
	        return Integer.compare(this._time, other._time);
	    }
	    return Long.compare(this._time_stamp, other._time_stamp);
	}

	abstract void execute(RoadMap map);
}
