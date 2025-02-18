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
//	public int compareTo(Event o) {
//		
//		if(o.getTime() == _time) {
//			if(o._time_stamp > _time_stamp) {
//				return -1;
//			}
//			else if(o._time_stamp < _time_stamp) {
//				return 1;
//			}
//			
//			return 0;
//		}
//		else if(o.getTime()> _time) {
//			return -1;
//		}
//		else return 1;
//		
//		// TODO complete the method to compare events according to their _time, and when
//		// _time is equal it compares the _time_stamp;
//	}
	
	public int compareTo(Event other) {
	    if (this._time != other._time) {
	        return Integer.compare(this._time, other._time);
	    }
	    return Long.compare(this._time_stamp, other._time_stamp);
	}

	abstract void execute(RoadMap map);
}
