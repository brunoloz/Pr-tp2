package simulator.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class EventsTableModel extends AbstractTableModel implements TrafficSimObserver{
	
//	private Controller _ctr;
	private String[] _headers = {"Time", "Description"};
	private List<Event> _miInfo = new ArrayList<Event>();
	
	
	public String getColumnName(int columnIndex) {
		return _headers[columnIndex];
	}
	
	public EventsTableModel(Controller ctr) {
		ctr.addObserver(this);
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return this._miInfo.size();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return _headers.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		// TODO Auto-generated method stub
		Event e = _miInfo.get(rowIndex);
		String val = "";
		
		switch(columnIndex) {
		case 0:
			int t = e.getTime();
			val = Integer.toString(t);
			break;
		case 1:
			val = e.toString();
			break;
		}
		return val;
	}

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}
	

}
