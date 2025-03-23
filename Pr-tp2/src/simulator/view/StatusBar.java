package simulator.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.Collection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class StatusBar extends JPanel implements TrafficSimObserver{
	
	private Controller _ctr;
	private JLabel _timeLabel;
	private JLabel _eventLabel;
	
	//Dudas sobre el _roadMap (ns para que se utiliza en esta clase) y _events
	private RoadMap _roadMap;
	private Event _event;
	private Collection<Event> _events;
	private int _time;

	
	public StatusBar(Controller ctr) {
		initGUI();
		ctr.addObserver(this);
	}
	
	private void initGUI() {
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.setBorder(BorderFactory.createBevelBorder(1));

        _timeLabel = new JLabel();
        this.add(_timeLabel);

        JSeparator s = new JSeparator(JSeparator.VERTICAL);
        s.setPreferredSize(new Dimension(10, 20));
        this.add(s);

        _eventLabel = new JLabel();
        this.add(_eventLabel);
	}
	

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(()->{
			this._roadMap = map;
			this._events = events;
			this._time = time;
			this._timeLabel.setText("Time:  " + time);
		});
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(()->{
			this._roadMap = map;
			this._events = events;
			this._event = e;
			this._time = time;
			this._timeLabel.setText("Time:  " + time);
			this._eventLabel.setText(e.toString());
		});
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		this._roadMap = map;
		this._events = events;
		this._time = time;
		this._timeLabel.setText("Time:  " + time);
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		this._roadMap = map;
		this._events = events;
		this._time = time;
		this._timeLabel.setText("Time:  " + time);
	}

}
