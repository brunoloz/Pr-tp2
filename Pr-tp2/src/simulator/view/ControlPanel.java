package simulator.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Collection;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;

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
	private JButton openButton;
	
	public ControlPanel(Controller ctr) {
		_ctr = ctr;
		initGUI();
	}
	
	private void initGUI() {
		setLayout(new BorderLayout());
        _toolaBar = new JToolBar();
        add(_toolaBar, BorderLayout.PAGE_START);
        
        
	}
	
	
	
	private JButton openButton() {
		JButton open = new JButton(new ImageIcon("resources/icons/open.png"));
        open.addActionListener(l -> {
            var sel = _fc.showOpenDialog(ViewUtils.getWindow(this));

            if (sel == JFileChooser.APPROVE_OPTION) {
                try {
                    JSONObject inputJson = new JSONObject(new JSONTokener(new FileInputStream(_fc.getSelectedFile())));
                    _ctrl.reset(inputJson.getInt("cols"), inputJson.getInt("rows"), inputJson.getInt("width"), inputJson.getInt("height"));
                    _ctrl.load_data(inputJson);
                } catch (JSONException e) {
                    ViewUtils.showErrorMsg(open, "El archivo no tiene el formato correcto");
                } catch (FileNotFoundException ignored) {
                    ViewUtils.showErrorMsg(open, "El archivo no se ha encontrado");
                }
            }
        });
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
