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
	private JDialog _co2Dialog;
	
	//CANCION
	private static Clip _clip;
	private static boolean isMuted = false;
	
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
        playMusic("resources/icons/c418.wav");
        
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
                    //JSONObject inputJson = new JSONObject(new JSONTokener(new FileInputStream(_fc.getSelectedFile())));
                    FileInputStream inputStream = new FileInputStream(_fc.getSelectedFile());
                    _ctr.loadEvents(inputStream);
                
                    
                    //ViewUtils.showInfoMsg(open, "Archivo cargado correctamente.");
                    //System.out.println(inputJson.toString(2));
                } catch (JSONException e) {
                    ViewUtils.showErrorMsg(open, "El archivo no tiene el formato correcto");
                } catch (FileNotFoundException ignored) {
                    ViewUtils.showErrorMsg(open, "El archivo no se ha encontrado");
                }
            }
        });

		return open;
	}

//	private JButton co2Button() {
//		JButton open = new JButton(new ImageIcon("resources/icons/co2class.png"));
//		open.addActionListener(l ->{
//			
//			
//			ImageIcon iconco2 = new ImageIcon("resources/icons/co2class.png");
//			Object[] options = {"Aceptar", "Cancelar"};
//			int n = JOptionPane.showOptionDialog(this, "<html><b>Schedule an event to change the CO2 class of a vehicle after "
//					+ "a given number of simulation ticks from now.", "Change C02 lass", 
//					JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, iconco2, options, options[1]);
//			
//		});
//
//		return open;
//	}
	
	
	private JButton co2Button() {
	    JButton open = new JButton(new ImageIcon("resources/icons/co2class.png"));
	    open.addActionListener(l -> showCo2Dialog());
	    return open;
	}

	private void showCo2Dialog() {
	    // Crear el diálogo
	    JDialog co2Dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Change CO₂ Class", true);
	    co2Dialog.setLayout(new BorderLayout());
	    co2Dialog.setSize(800, 200);
	    co2Dialog.setLocationRelativeTo(this);

	    // Panel superior con texto descriptivo
	    JPanel topPanel = new JPanel();
	    JLabel label = new JLabel("<html><b>Schedule an event to change the CO2 class of a vehicle after"
	    		+ "a given number of simulation ticks from now.</b></html>");
	    topPanel.add(label);
	    
	    // Panel central con JComboBox
	    JPanel centerPanel = new JPanel();
	    JComboBox<String> vehicleComboBox = new JComboBox<>();
	    JComboBox<Integer> co2ClassComboBox = new JComboBox<>(new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9});
	    JSpinner ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 10));
	    centerPanel.add(new JLabel("Vehicle: "));
	    centerPanel.add(vehicleComboBox);
	    centerPanel.add(new JLabel("CO₂ Class: "));
	    centerPanel.add(co2ClassComboBox);
	    centerPanel.add(new JLabel("Ticks: "));
	    centerPanel.add(ticks);
	    
	    // Panel inferior con botones
	    JPanel bottomPanel = new JPanel();
	    JButton acceptButton = new JButton("Accept");
	    JButton cancelButton = new JButton("Cancel");
	    
	    acceptButton.addActionListener(e -> {
	        String selectedVehicle = (String) vehicleComboBox.getSelectedItem();
	        int selectedCo2 = (int) co2ClassComboBox.getSelectedItem();
	        System.out.println("Changing " + selectedVehicle + " to CO₂ class " + selectedCo2);
	        co2Dialog.dispose();
	    });

	    cancelButton.addActionListener(e -> co2Dialog.dispose());

	    bottomPanel.add(acceptButton);
	    bottomPanel.add(cancelButton);

	    // Agregar paneles al diálogo
	    co2Dialog.add(topPanel, BorderLayout.NORTH);
	    co2Dialog.add(centerPanel, BorderLayout.CENTER);
	    co2Dialog.add(bottomPanel, BorderLayout.SOUTH);

	    // Mostrar el diálogo
	    co2Dialog.setVisible(true);
	}

	private JButton weatherButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/weather.png"));
		open.addActionListener(l -> showWeatherDialog());

		return open;
	}
	
	private void showWeatherDialog() {
	    // Crear el diálogo
	    JDialog co2Dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Change Road Weather", true);
	    co2Dialog.setLayout(new BorderLayout());
	    co2Dialog.setSize(800, 200);
	    co2Dialog.setLocationRelativeTo(this);

	    // Panel superior con texto descriptivo
	    JPanel topPanel = new JPanel();
	    JLabel label = new JLabel("<html><b>Schedule an event to change the weather of a road after"
	    		+ "a given number of simulation ticks from now.</b></html>");
	    topPanel.add(label);
	    
	    // Panel central con JComboBox
	    JPanel centerPanel = new JPanel();
	    JComboBox<Road> roadComboBox = new JComboBox<>(); //??????????????????
	    JComboBox<Weather> weatherComboBox = new JComboBox<>(Weather.values());
	    JSpinner ticks = new JSpinner(new SpinnerNumberModel(10, 1, 10000, 10));
	    centerPanel.add(new JLabel("Road: "));
	    centerPanel.add(roadComboBox);
	    centerPanel.add(new JLabel("Weather: "));
	    centerPanel.add(weatherComboBox);
	    centerPanel.add(new JLabel("Ticks: "));
	    centerPanel.add(ticks);
	    
	    // Panel inferior con botones
	    JPanel bottomPanel = new JPanel();
	    JButton acceptButton = new JButton("Accept");
	    JButton cancelButton = new JButton("Cancel");
	    
	    acceptButton.addActionListener(e -> {
	        Road selectedVehicle = (Road) roadComboBox.getSelectedItem();
	        Weather selectedCo2 = (Weather) roadComboBox.getSelectedItem();
	        System.out.println("Changing " + selectedVehicle.toString() + " to CO₂ class " + selectedCo2);
	        co2Dialog.dispose();
	    });

	    cancelButton.addActionListener(e -> co2Dialog.dispose());

	    bottomPanel.add(acceptButton);
	    bottomPanel.add(cancelButton);

	    // Agregar paneles al diálogo
	    co2Dialog.add(topPanel, BorderLayout.NORTH);
	    co2Dialog.add(centerPanel, BorderLayout.CENTER);
	    co2Dialog.add(bottomPanel, BorderLayout.SOUTH);

	    // Mostrar el diálogo
	    co2Dialog.setVisible(true);
	}

	private JButton runButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/run.png"));

		return open;
	}

	private JButton stopButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/stop.png"));

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
				// muteButton.setText(isMuted ? "🔇 Unmute" : "🔊 Mute");
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
