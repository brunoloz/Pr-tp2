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
	
	private Controller _ctr;
	private String[] _headers = {"Time", "Description"};
	private List<Event> _miInfo = new ArrayList<Event>();
	
	
	public String getColumnName(int columnIndex) {
		return _headers[columnIndex];
	}
	
	public EventsTableModel(Controller ctr) {
		_ctr = ctr;
		this._ctr.addObserver(this);
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
	    Event e = _miInfo.get(rowIndex);

	    switch (columnIndex) {
	        case 0: return e.getTime();  
	        case 1: return e.toString(); 
	        default: return null;        // Caso por defecto para evitar errores
	    }
	}


	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		_miInfo = new ArrayList<>(events); 
		fireTableDataChanged();
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		_miInfo = new ArrayList<>(events); 
		fireTableDataChanged(); // Refrescamos la tabla
		
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		_miInfo = new ArrayList<>(events);  // Guardamos la lista actualizada de eventos
		fireTableDataChanged();
	}
	

}
