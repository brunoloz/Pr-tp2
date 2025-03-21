package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;

public class ControlPanel extends JPanel implements TrafficSimObserver {
	
	private Controller _ctr;
	private JFileChooser _fc;
	private JToolBar _toolaBar;
	private JButton _openButton;
	private JButton _co2ClassButton;
	private JButton _weatherButton;
	private JButton _runButton;
	private JButton _stopButton;
	private JLabel _ticksLabel;
	private JSpinner _ticksSpinner;
	private JButton _quitButton;
	
	public ControlPanel(Controller ctr) {
		_ctr = ctr;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
        _toolaBar = new JToolBar();
        add(_toolaBar, BorderLayout.PAGE_START);
        
        
        _openButton = openButton();
        _toolaBar.add(_openButton);
        
        _toolaBar.addSeparator();
        
        _co2ClassButton = co2Button();
        _toolaBar.add(_co2ClassButton);
        
        _weatherButton = weatherButton();
        _toolaBar.add(_weatherButton);
        
        _toolaBar.addSeparator();
        
        _runButton = runButton();
        _toolaBar.add(_runButton);
        
        _stopButton = stopButton();
        _toolaBar.add(_stopButton);
        
        _ticksLabel = new JLabel(" Ticks: ");
        _toolaBar.add(_ticksLabel);
        
        
        _ticksSpinner = new JSpinner(new SpinnerNumberModel(5000, 1, 10000, 100));
        _ticksSpinner.setToolTipText("Simulation steps to run: 1-10000");
        _ticksSpinner.setMaximumSize(new Dimension(80, 40));
        _ticksSpinner.setMinimumSize(new Dimension(80, 40));
        _ticksSpinner.setPreferredSize(new Dimension(80, 40));
        _toolaBar.add(_ticksSpinner);
        
        _toolaBar.add(Box.createGlue());
		_toolaBar.addSeparator();
        
		_quitButton = new JButton(new ImageIcon("resources/icons/exit.png"));
		_quitButton.setToolTipText("Exit");
		_quitButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				quit();
			}
		});
		_toolaBar.add(_quitButton);
		
		
		
        _fc = new JFileChooser();
        _fc.setCurrentDirectory(new File(System.getProperty("user.dir") + "/resources/examples"));
        
	}
	
	protected void quit() {
		int n = JOptionPane.showOptionDialog(this.getParent(), "Are sure you want to quit?", "Quit",
				JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

		if (n == 0) {
			System.exit(0);
		}
	}
	
	private JButton openButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/open.png"));
		open.addActionListener(l -> {
            var sel = _fc.showOpenDialog(ViewUtils.getWindow(this));

            if (sel == JFileChooser.APPROVE_OPTION) {
                try {
                    JSONObject inputJson = new JSONObject(new JSONTokener(new FileInputStream(_fc.getSelectedFile())));
                
                } catch (JSONException e) {
                    ViewUtils.showErrorMsg(open, "El archivo no tiene el formato correcto");
                } catch (FileNotFoundException ignored) {
                    ViewUtils.showErrorMsg(open, "El archivo no se ha encontrado");
                }
            }
        });

		return open;
	}

	private JButton co2Button() {
		JButton open = new JButton(new ImageIcon("resources/icons/co2class.png"));

		return open;
	}

	private JButton weatherButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/weather.png"));

		return open;
	}

	private JButton runButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/run.png"));

		return open;
	}

	private JButton stopButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/stop.png"));

		return open;
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
