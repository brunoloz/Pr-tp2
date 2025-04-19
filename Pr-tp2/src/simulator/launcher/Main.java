package simulator.launcher;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import simulator.control.Controller;
import simulator.factories.Builder;
import simulator.factories.BuilderBasedFactory;
import simulator.factories.Factory;
import simulator.factories.NewCityRoadEventBuilder;
import simulator.factories.RoundRobinStrategyBuilder;
import simulator.factories.NewJunctionEventBuilder;
import simulator.factories.MostCrowdedStrategyBuilder;
import simulator.factories.MoveAllStrategyBuilder;
import simulator.factories.MoveFirstStrategyBuilder;
import simulator.factories.NewInterCityRoadEventBuilder;
import simulator.factories.NewVehicleEventBuilder;
import simulator.factories.SetContClassEventBuilder;
import simulator.factories.SetWeatherEventBuilder;
import simulator.model.DequeuingStrategy;
import simulator.model.Event;
import simulator.model.LightSwitchingStrategy;
import simulator.model.TrafficSimulator;
import simulator.view.MainWindow;

public class Main {

	private static String _inFile = null;
	private static String _outFile = null;
	private static Factory<Event> _eventsFactory = null;
	private static int _timeLimit = 0;
	private static String _mode;

	private static void parseArgs(String[] args) {

		// define the valid command line options
		//
		Options cmdLineOptions = buildOptions();

		// parse the command line as provided in args
		//
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine line = parser.parse(cmdLineOptions, args);
			parseHelpOption(line, cmdLineOptions);
			parseInFileOption(line);
			parseOutFileOption(line);
			parseTicksFileOption(line);
			parseModeOption(line);

			// if there are some remaining arguments, then something wrong is
			// provided in the command line!
			//
			String[] remaining = line.getArgs();
			if (remaining.length > 0) {
				String error = "Illegal arguments:";
				for (String o : remaining)
					error += (" " + o);
				throw new ParseException(error);
			}

		} catch (ParseException e) {
			System.err.println(e.getLocalizedMessage());
			System.exit(1);
		}

	}

	private static Options buildOptions() {
		Options cmdLineOptions = new Options();

		cmdLineOptions.addOption(Option.builder("i").longOpt("input").hasArg().desc("Events input file").build());
		cmdLineOptions.addOption(
				Option.builder("o").longOpt("output").hasArg().desc("Output file, where reports are written.").build());
		cmdLineOptions.addOption(Option.builder("h").longOpt("help").desc("Print this message").build());
		cmdLineOptions.addOption(Option.builder("t").longOpt("ticks").hasArg().desc("Ticks to the simulator's main loop (default value is 10)").build());
		cmdLineOptions.addOption(Option.builder("m").longOpt("mode").hasArg().desc("").build());
		
		return cmdLineOptions;
	}
	
	
	private static void parseModeOption(CommandLine line) {
		if(line.getOptionValue("m") == null) {
			_mode = "gui";
		}
		else {
			String s = line.getOptionValue("m");
			switch(s) {
			case "":
				_mode = "gui";
				break;
			case "gui":
				_mode = "gui";
				break;
			case "console":
				_mode = "console";
				break;
			default:
				_mode = "gui";
				break;
			}
		}
		
		
		
	}

	private static void parseHelpOption(CommandLine line, Options cmdLineOptions) {
		if (line.hasOption("h")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(Main.class.getCanonicalName(), cmdLineOptions, true);
			System.exit(0);
		}
	}

	private static void parseInFileOption(CommandLine line) throws ParseException {
		_inFile = line.getOptionValue("i");
		if (_mode == "batch" && _inFile == null) {
			throw new ParseException("An events file is missing");
		}
	}

	private static void parseOutFileOption(CommandLine line) throws ParseException {
		_outFile = line.getOptionValue("o");
	}
	
	private static void parseTicksFileOption(CommandLine line){
		String s = line.getOptionValue("t");
		int t; 
		if(s == null) {
			_timeLimit = 10;
		}
		else {
			t = Integer.parseInt(s);
			_timeLimit = t;
		}
	}

	private static void initFactories() {
		ArrayList<Builder<LightSwitchingStrategy>> lsbs = new ArrayList<>();
		lsbs.add( new RoundRobinStrategyBuilder() );
		lsbs.add( new MostCrowdedStrategyBuilder() );
		Factory<LightSwitchingStrategy> lssFactory = new BuilderBasedFactory<>(lsbs);
		
		List<Builder<DequeuingStrategy>> dqbs = new ArrayList<>();
		dqbs.add( new MoveAllStrategyBuilder() );
		dqbs.add( new MoveFirstStrategyBuilder() );
		Factory<DequeuingStrategy> dqsFactory = new BuilderBasedFactory<>(dqbs);
		
		// initialize the events factory
		List<Builder<Event>> ebs = new ArrayList<>();
		ebs.add( new NewJunctionEventBuilder(lssFactory,dqsFactory) );
		ebs.add( new NewCityRoadEventBuilder() );
		ebs.add( new NewInterCityRoadEventBuilder() );
		ebs.add( new NewVehicleEventBuilder() );
		ebs.add( new SetContClassEventBuilder());
		ebs.add( new SetWeatherEventBuilder());
		// ...
		_eventsFactory = new BuilderBasedFactory<>(ebs);
		
	}

	private static void startBatchMode() throws IOException {
		try {
	        InputStream in = new FileInputStream(_inFile);
	        
	        OutputStream out = (_outFile != null) ? new FileOutputStream(_outFile) : System.out;
	        
	        TrafficSimulator sim = new TrafficSimulator();
	        
	        Controller controller = new Controller(sim, _eventsFactory);
	        
	        controller.loadEvents(in);
	        	       
		    in.close();
	        
	        controller.run(_timeLimit, out);

	        if (_outFile != null) {
	            out.close();
	        }
	    } catch (IOException e) {
	        System.err.println("Error de lectura/escritura: " + e.getMessage());
	    } catch (Exception e) {
	        System.err.println("Error en la simulación: " + e.getMessage());
	    }
	}
	
	private static void startGuiMode() throws IOException {

		TrafficSimulator sim = new TrafficSimulator();
		Controller ctrl = new Controller(sim, _eventsFactory);

		SwingUtilities.invokeLater(new Runnable() {	
			@Override
			public void run() {
				new MainWindow(ctrl);
			}
		}); 

	}

	private static void start(String[] args) throws IOException {
		initFactories();
		parseArgs(args);
		switch (_mode) {
        case "console":
            startBatchMode();
            break;
        case "gui":
            startGuiMode();
            break;
    }
	}

	// example command lines:
	//
	// -i resources/examples/ex1.json
	// -i resources/examples/ex1.json -t 300
	// -i resources/examples/ex1.json -o resources/tmp/ex1.out.json
	// --help

	public static void main(String[] args) {
		try {
			start(args);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
