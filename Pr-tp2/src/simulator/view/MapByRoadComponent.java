package simulator.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Junction;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.VehicleStatus;
import simulator.model.Weather;

public class MapByRoadComponent extends JPanel implements TrafficSimObserver{

	private static final long serialVersionUID = 1L;

	private static final int _JRADIUS = 10;

	private static final Color _BG_COLOR = Color.WHITE;
	private static final Color _JUNCTION_COLOR = Color.BLUE;
	private static final Color _JUNCTION_LABEL_COLOR = new Color(200, 100, 0);
	private static final Color _GREEN_LIGHT_COLOR = Color.GREEN;
	private static final Color _RED_LIGHT_COLOR = Color.RED;

	private RoadMap _map;

	private Image _car;
	private Image _weather;
	private Image _cont;

	MapByRoadComponent(Controller ctrl) {
		setPreferredSize(new Dimension (300, 200));
		initGUI();
		ctrl.addObserver(this);
	}

	private void initGUI() {
		_car = loadImage("car.png");
	}
	
	private void loadWeatherImg(Weather w) {
		switch(w) {
		case SUNNY:
			_weather = loadImage("sun.png");
			break;
		case CLOUDY:
			_weather = loadImage("cloud.png");
			break;
		case RAINY:
			_weather = loadImage("rain.png");
			break;
		case WINDY:
			_weather = loadImage("wind.png");
			break;
		case STORM:
			_weather = loadImage("storm.png");
			break;
		}
	}
	
	private void loadContImg(int contLimit, int totalCO2) {
		int c = (int) Math.floor(Math.min((double) totalCO2/(1.0 + (double) contLimit),1.0) / 0.19);
		_cont = loadImage("cont_" + c + ".png");
			
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		// clear with a background color
		g.setColor(_BG_COLOR);
		g.clearRect(0, 0, getWidth(), getHeight());

		if (_map == null || _map.getJunctions().size() == 0) {
			g.setColor(Color.red);
			g.drawString("No map yet!", getWidth() / 2 - 50, getHeight() / 2);
		} else {
			updatePrefferedSize();
			drawMap(g);
		}
	}

	private void drawMap(Graphics g) {
		drawRoads(g);
		drawVehicles(g);
	}

	private void drawRoads(Graphics g) {
		int i = 0;
		for (Road r : _map.getRoads()) {

			// the road goes from (x1,y1) to (x2,y2)
			int x1 = 50;
			int x2 = getWidth() - 100;
			int y = (i+1)*50;

			// choose a color for the arrow depending on the traffic light of the road
			Color arrowColor = _RED_LIGHT_COLOR;
			int idx = r.getDest().getGreenLightIndex();
			if (idx != -1 && r.equals(r.getDest().getInRoads().get(idx))) { //getInRoads?????????
				arrowColor = _GREEN_LIGHT_COLOR;
			}

			Color roadColor = Color.BLACK;

			// draw line from (x1,y1) to (x2,y2) with line of
			// color roadColor.
			drawLine(g, x1, y, x2, y, 15, 5, roadColor);
			
			//Dibuja el id de la carretera
			g.drawString(r.getId(), x1 - 30, y);
			
			//Dibuja el cruce origen en azul
			g.setColor(_JUNCTION_COLOR);
			g.fillOval(x1 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_RED_LIGHT_COLOR);
			g.drawString(r.getSrc().getId(), x1, y - 6);

			//Dibuja el cruce destino en rojo o verde
			g.setColor(arrowColor);
			g.fillOval(x2 - _JRADIUS / 2, y - _JRADIUS / 2, _JRADIUS, _JRADIUS);
			g.setColor(_RED_LIGHT_COLOR);
			g.drawString(r.getDest().getId(), x2, y - 6);

			
			//Dibuja las condiciones meteorológicas de la carretera
			loadWeatherImg(r.getWeather());
			g.drawImage(_weather, getWidth()- 80, y- 15, 32, 32, this);
			
			//Dibuja el nivel de contaminación de la carretera
			loadContImg(r.getContLimit(), r.getTotalCO2());
			g.drawImage(_cont, getWidth()- 40, y- 15, 32, 32, this);
			
			
			i++;
		}

	}

	private void drawVehicles(Graphics g) {
		for (Vehicle v : _map.getVehicles()) {
			if (v.getStatus() != VehicleStatus.ARRIVED) {

				Road r = v.getRoad();

				// The calculation below compute the coordinate (vX,vY) of the vehicle on the
				// corresponding road. It is calculated relatively to the length of the road, and
				// the location on the vehicle.
				int x1 = 50;
				int x2 = getWidth() - 100;
				int i = _map.getRoads().indexOf(r);
				int y = (i+1)*50;

				double f = ((float)v.getLocation()) / r.getLength();
				int vX = x1 + (int) ((x2 - x1) * ((double) v.getLocation() / (double) r.getLength()));
				int vY = y;

				// draw an image of a car and it identifier
				g.drawImage(_car, vX, vY - 6, 16, 16, this);
				g.setColor(_GREEN_LIGHT_COLOR);
				g.drawString(v.getId(), vX, vY - 6);
			}
		}
	}

	// this method is used to update the preffered and actual size of the component,
	// so when we draw outside the visible area the scrollbars show up
	private void updatePrefferedSize() {
		int maxW = 200;
		int maxH = 200;
		for (Junction j : _map.getJunctions()) {
			maxW = Math.max(maxW, j.getX());
			maxH = Math.max(maxH, j.getY());
		}
		maxW += 20;
		maxH += 20;
		if (maxW > getWidth() || maxH > getHeight()) {
			setPreferredSize(new Dimension(maxW, maxH));
			setSize(new Dimension(maxW, maxH));
		}
	}

	// This method draws a line from (x1,y1) to (x2,y2).
	private void drawLine(//
			Graphics g, //
			int x1, int y1, //
			int x2, int y2, //
			int w, int h, //
			Color lineColor) {

		int dx = x2 - x1, dy = y2 - y1;
		double D = Math.sqrt(dx * dx + dy * dy);
		double xm = D - w, xn = xm, ym = h, yn = -h, x;
		double sin = dy / D, cos = dx / D;

		x = xm * cos - ym * sin + x1;
		ym = xm * sin + ym * cos + y1;
		xm = x;

		x = xn * cos - yn * sin + x1;
		yn = xn * sin + yn * cos + y1;
		xn = x;

		int[] xpoints = { x2, (int) xm, (int) xn };
		int[] ypoints = { y2, (int) ym, (int) yn };

		g.setColor(lineColor);
		g.drawLine(x1, y1, x2, y2);

	}

	// loads an image from a file
	private Image loadImage(String img) {
		Image i = null;
		try {
			return ImageIO.read(new File("resources/icons/" + img));
		} catch (IOException e) {
		}
		return i;
	}

	public void update(RoadMap map) {
		SwingUtilities.invokeLater(() -> {
			_map = map;
			repaint();
		});
	}
	
	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onReset(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}

	@Override
	public void onRegister(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		update(map);
	}
	
	
}
