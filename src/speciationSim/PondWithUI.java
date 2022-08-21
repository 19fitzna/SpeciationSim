package speciationSim;


import java.awt.Color;

import javax.swing.JFrame;
import sim.display.Console;
import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.continuous.ContinuousPortrayal2D;


public final class PondWithUI extends GUIState
{
	public Display2D display;
	public JFrame displayFrame;

	ContinuousPortrayal2D redPortrayal = new ContinuousPortrayal2D();
	ContinuousPortrayal2D preyPortrayal = new ContinuousPortrayal2D();

	public static void main (String[] args) {
		PondWithUI simGUI = new PondWithUI();
		Console c= new Console(simGUI);  //imported the Console with simdisplay NOT the java one
		c.setVisible(true);

	}

	public PondWithUI() {
		super (new Pond( (long) System.currentTimeMillis() ) );
	}

	public static String getName() {
		return "Wator Model";
	}

	public void start () {
		super.start();
		setupPortrayals();

	}

	public void load (SimState state) {
		super.load(state);
		//we now have new grids. Sets up the portrayals to reflect that.
	}

	public void setupPortrayals() {
		GreyFish asdf = new GreyFish();
		BlueFish fdsa = new BlueFish();
		
		preyPortrayal.setField(Pond.preySpace);
		preyPortrayal.setPortrayalForClass(asdf.getClass(), new sim.portrayal.simple.RectanglePortrayal2D(Color.gray, 5.1));
		preyPortrayal.setPortrayalForClass(fdsa.getClass(), new sim.portrayal.simple.RectanglePortrayal2D(Color.blue, 5.1));

		redPortrayal.setField(Pond.redSpace);
		redPortrayal.setPortrayalForAll
		(new sim.portrayal.simple.RectanglePortrayal2D(Color.red, 5.1) );


		//Reschedules the displayer 
		display.reset();

	}

	public void init( Controller c) {
		super.init(c);
		//Builds the portrayals
		preyPortrayal.setField(Pond.preySpace);
		redPortrayal.setField(Pond.redSpace);
		
		
		display =new Display2D(600, 600, this);
		display.attach(preyPortrayal, "PreyFish");
		display.attach(redPortrayal, "RedFish");

		displayFrame= display.createFrame();
		c.registerFrame(displayFrame);
		displayFrame.setVisible(true);

	}

	public void quit () {
		super.quit ();
		if (displayFrame != null) displayFrame.dispose();
		displayFrame =null;
		display =null;
	}

}

