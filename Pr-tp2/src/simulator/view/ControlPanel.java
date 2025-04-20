package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
//CANCION
import javax.sound.sampled.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import simulator.control.Controller;
import simulator.model.Event;
import simulator.model.Road;
import simulator.model.RoadMap;
import simulator.model.TrafficSimObserver;
import simulator.model.Vehicle;
import simulator.model.Weather;

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
	private JButton _soundButton;
	private JComboBox<Vehicle> vehicleComboBox = new JComboBox<Vehicle>();;
	private boolean _stopped;
	
	//CANCION
	private static Clip _clip;
	private static boolean isMuted = false;
	
	public ControlPanel(Controller ctr) {
		_ctr = ctr;
		_ctr.addObserver(this);
		_ticksSpinner = new JSpinner();
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
        _toolaBar = new JToolBar();
        add(_toolaBar, BorderLayout.PAGE_START);
        
        
        _openButton = openButton();
        _toolaBar.add(_openButton);
        
        _toolaBar.addSeparator();
        
        ChangeCO2Dialog co2Dialog = new ChangeCO2Dialog(ViewUtils.getWindow(this), _ctr);
        _co2ClassButton = co2Button(co2Dialog);
        _toolaBar.add(_co2ClassButton);
        
        
        ChangeRoadWeatherDialog weatherDialog = new ChangeRoadWeatherDialog(ViewUtils.getWindow(this), _ctr);
        _weatherButton = weatherButton(weatherDialog);
        _toolaBar.add(_weatherButton);
        
        _toolaBar.addSeparator();
        
        _runButton = runButton();
        _toolaBar.add(_runButton);
        
        _stopButton = stopButton();
        _toolaBar.add(_stopButton);
        
        
        _ticksLabel = new JLabel(" Ticks: ");
        _toolaBar.add(_ticksLabel);
        
        
        _ticksSpinner = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 10));
        _ticksSpinner.setToolTipText("Simulation steps to run: 1-10000");
        _ticksSpinner.setMaximumSize(new Dimension(80, 40));
        _ticksSpinner.setMinimumSize(new Dimension(80, 40));
        _ticksSpinner.setPreferredSize(new Dimension(80, 40));
        _toolaBar.add(_ticksSpinner);
        
        _toolaBar.add(Box.createGlue());
		_toolaBar.addSeparator();
        
		//CANCION
        _soundButton = soundButton();
        _toolaBar.add(_soundButton);
        playMusic("resources/icons/Atleti.wav");
        
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
                    FileInputStream inputStream = new FileInputStream(_fc.getSelectedFile());
                    _ctr.reset();
                    _ctr.loadEvents(inputStream);
                
                } catch (JSONException e) {
                    ViewUtils.showErrorMsg(open, "El archivo no tiene el formato correcto");
                } catch (FileNotFoundException ignored) {
                    ViewUtils.showErrorMsg(open, "El archivo no se ha encontrado");
                }
            }
        });

		return open;
	}
	
	private JButton co2Button(ChangeCO2Dialog co2Dialog) {
		JButton open = new JButton(new ImageIcon("resources/icons/co2class.png"));
		open.addActionListener(e ->{
			co2Dialog.setLocationRelativeTo(ViewUtils.getWindow(this));
			co2Dialog.open();
		});
		return open;
	}

	private JButton weatherButton(ChangeRoadWeatherDialog weatherDialog) {
		JButton open = new JButton(new ImageIcon("resources/icons/weather.png"));
		open.addActionListener(e ->{
			weatherDialog.setLocationRelativeTo(ViewUtils.getWindow(this));
			weatherDialog.open();
		});
		return open;
	}
	
	private void enableToolBar(boolean enabled) {
	    _openButton.setEnabled(enabled);
	    _co2ClassButton.setEnabled(enabled);
	    _weatherButton.setEnabled(enabled);
	    _runButton.setEnabled(enabled);
	    _stopButton.setEnabled(!enabled); // El botón de parar sólo se habilita cuando corre la simulación
	    _ticksSpinner.setEnabled(enabled);
	}


	private JButton runButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/run.png"));
		
		open.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int tck = (int) _ticksSpinner.getValue();
		        _stopped = false;
		        enableToolBar(false);
				run_sim(tck);
			}
			
		});

		return open;
	}
	
	private void run_sim(int n) {
	    if (n > 0 && !_stopped) {
	        try {
	            _ctr.run(1); // Ejecutar un solo paso
	            SwingUtilities.invokeLater(() -> run_sim(n - 1)); // Repetir después
	        } catch (Exception e) {
	            _stopped = true;
	            JOptionPane.showMessageDialog(this, "Error durante la simulación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	            enableToolBar(true);
	        }
	    } else {
	        _stopped = true;
	        enableToolBar(true);
	    }
	}


	private JButton stopButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/stop.png"));
		
		open.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
		        _stopped = true;
			}
			
		});

		return open;
	}
	
	
	//CANCION
	private JButton soundButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/sound.png"));
		open.addActionListener(l ->{
			 if (_clip != null) {
				 if(!isMuted) {
					 ImageIcon iconOff = new ImageIcon("resources/icons/soundM.png");
					 open.setIcon(iconOff);
				 }
				 else {
					 ImageIcon iconOn = new ImageIcon("resources/icons/sound.png");
					 open.setIcon(iconOn);
				 }
				isMuted = !isMuted; // Cambiar estado
				BooleanControl muteControl = (BooleanControl) _clip.getControl(BooleanControl.Type.MUTE);
				muteControl.setValue(isMuted); // Activar/desactivar mute
	            }
		});

		return open;
	}
	
	public static void playMusic(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                System.out.println("El archivo no existe: " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
            _clip = AudioSystem.getClip();
            _clip.open(audioStream);
            _clip.loop(Clip.LOOP_CONTINUOUSLY); // Reproducir en bucle
            _clip.start();
            System.out.println("Reproduciendo música...");
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.out.println("Error musica");
        }
    }

	@Override
	public void onAdvance(RoadMap map, Collection<Event> events, int time) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEventAdded(RoadMap map, Collection<Event> events, Event e, int time) {
		// TODO Auto-generated method stub
		for(int i = 0; i < map.getVehicles().size(); i++) {
			vehicleComboBox.addItem(map.getVehicles().get(i));
		}
		
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
