package speciationSim;

import sim.util.Double2D;

@SuppressWarnings("serial")
public class GreyFish extends PreyFish {

	GreyFish(Double2D loc, int stepsSinceBreeding, boolean alleleOne, boolean alleleTwo) {
		super(loc, stepsSinceBreeding, alleleOne, alleleTwo);
	}

	public GreyFish() {}

}
