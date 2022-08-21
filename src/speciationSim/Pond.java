package speciationSim;

import ec.util.MersenneTwisterFast;
import sim.engine.Schedule;
import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.continuous.Continuous2D;
import sim.util.Double2D;

@SuppressWarnings("serial")
final class Pond extends SimState
{
	
	static double selectionFactor = 0.70; //Value between 0 and 1, the percentage of time that grey fish aren't eaten
										  //IE how often are blue fish eaten over grey fish
	
	
	private static MersenneTwisterFast   rand;
	private static Schedule            sched;
	private static boolean graphOn     =false;


	private static double width        = 600d;
	private static double height       = 600d;
	private static double neighborhood = 20;

	private static int numRedFish      = 0;
	private static int numPreyFish     = 0;
	private static int initRedFish     = 100;
	private static int initPreyFish    = 1000;
	private static int stepsToRedBreed = 1000;
	private static int stepsToPreyBreed = 1000;
	private static int numRedRecruits   = 2;
	private static int numPreyRecruits  = 4;
	private static int maxRedFish   = 4000;
	private static int maxPreyFish   = 5000;
	private static int redStarve   = 68;

	static Continuous2D redSpace = new Continuous2D(neighborhood/1.5, width, height);
	static Continuous2D preySpace = new Continuous2D(neighborhood/1.5, width, height);
	//static TimeSeriesPlotter plotter

	Pond (long seed) {
		super(seed);
		rand = this.random;
		sched = this.schedule;
		//AngleToCoords.fillArrays();
	}

	@Override
	public void start() {
		super.start();
		sched.clear();
		numRedFish  =0;
		numPreyFish =0;
		redSpace = new Continuous2D(neighborhood/1.5, width, height);
		preySpace = new Continuous2D(neighborhood/1.5, width, height);
		

		recruitRedFish(initRedFish);
		recruitPreyFish(initPreyFish);

		if(graphOn) {
			//plotter = new TimeSeriesPlotter ("Fish Abundance" );

		}
	}

	static void recruitRedFish (int num) {
		for (int i = 0; i <num; i++) {
			if(numRedFish < maxRedFish) {
				makeOneRedFish();
			}
			else {
				System.out.println( "Red fish capacity reached" );
			}
		}
	}

	private static void makeOneRedFish() {
		double x = rand.nextDouble () * width;
		double y = rand.nextDouble () * height;
		int stepsSinceBreeding = rand.nextInt(stepsToRedBreed);
		int hunger = rand.nextInt(redStarve) ;
		RedFish fish =new RedFish(new Double2D(x, y), stepsSinceBreeding, hunger);
		Stoppable stop= sched.scheduleRepeating(fish);
		fish.setStopper(stop);
		numRedFish++;

	}

	static void recruitPreyFish (int num) {
		for (int i = 0; i <num; i++) {
			if(numPreyFish < maxPreyFish) {
				makeOnePreyFish();
			}
			else {
				//System.out.println( "Prey fish capacity reached" );
			}
		}
	}

	private static void makeOnePreyFish() {
		double x = rand.nextDouble () * width;
		double y = rand.nextDouble () * height;
		int stepsSinceBreeding = rand.nextInt(stepsToPreyBreed);

		int genotype = rand.nextInt(2);
		boolean alleleOne;
		boolean alleleTwo;

		PreyFish fish;
		
		if(genotype == 0) {
			alleleOne = true;
			alleleTwo = true;
			fish = new BlueFish (new Double2D(x, y), stepsSinceBreeding, alleleOne, alleleTwo);
		}
		else {
			alleleOne = false;
			alleleTwo = false;
			fish = new GreyFish (new Double2D(x, y), stepsSinceBreeding, alleleOne, alleleTwo);
		}
		
		Stoppable stop= sched.scheduleRepeating(fish);
		fish.setStopper(stop);
		numPreyFish++;

	}

	private static void makeOnePreyFish(boolean alleleOne, boolean alleleTwo) {
		double x = rand.nextDouble () * width;
		double y = rand.nextDouble () * height;
		int stepsSinceBreeding = rand.nextInt(stepsToPreyBreed);

		PreyFish fish;
		
		if(alleleOne || alleleTwo) {
			fish = new BlueFish (new Double2D(x, y), stepsSinceBreeding, alleleOne, alleleTwo);
		}
		else {
			fish = new GreyFish (new Double2D(x, y), stepsSinceBreeding, alleleOne, alleleTwo);
		}
		
		Stoppable stop= sched.scheduleRepeating(fish);
		fish.setStopper(stop);
		numPreyFish++;

	}
	/**Breeds two prey fish. For each offspring, randomly passes 1 allele from each parent to offspring */
	static void breedPreyFish(PreyFish fishOne, PreyFish fishTwo) {
		
		if(numPreyFish > maxPreyFish) return;
		
		for(int i = 0; i < numPreyRecruits; i++) {

			int chooseAllele = rand.nextInt(2);
			boolean alOne;
			boolean alTwo;
			if(chooseAllele == 0) alOne = fishOne.alleleOne;
			else alOne = fishOne.alleleTwo;

			chooseAllele = rand.nextInt(2);
			if(chooseAllele == 0) alTwo = fishTwo.alleleOne;
			else alTwo = fishTwo.alleleTwo;

			makeOnePreyFish(alOne, alTwo);

		}

	}

	static MersenneTwisterFast getRand() {
		return rand;
	}

	static void setRand(MersenneTwisterFast rand) {
		Pond.rand = rand;
	}

	static Schedule getSched() {
		return sched;
	}

	static void setSched(Schedule sched) {
		Pond.sched = sched;
	}

	static boolean isGraphOn() {
		return graphOn;
	}

	static void setGraphOn(boolean graphOn) {
		Pond.graphOn = graphOn;
	}

	static double getWidth() {
		return width;
	}

	static void setWidth(double width) {
		Pond.width = width;
	}

	static double getHeight() {
		return height;
	}

	static void setHeight(double height) {
		Pond.height = height;
	}

	static double getNeighborhood() {
		return neighborhood;
	}

	static void setNeighborhood(double neighborhood) {
		Pond.neighborhood = neighborhood;
	}

	static int getNumRedFish() {
		return numRedFish;
	}

	static void setNumRedFish(int numRedFish) {
		Pond.numRedFish = numRedFish;
	}

	static int getNumPreyFish() {
		return numPreyFish;
	}

	static void setNumPreyFish(int numPreyFish) {
		Pond.numPreyFish = numPreyFish;
	}

	static int getInitRedFish() {
		return initRedFish;
	}

	static void setInitRedFish(int initRedFish) {
		Pond.initRedFish = initRedFish;
	}

	static int getInitBLueFish() {
		return initPreyFish;
	}

	static void setInitBLueFish(int initBLueFish) {
		Pond.initPreyFish = initBLueFish;
	}

	static int getStepsToRedBreed() {
		return stepsToRedBreed;
	}

	static void setStepsToRedBreed(int stepsToRedBreed) {
		Pond.stepsToRedBreed = stepsToRedBreed;
	}

	static int getStepsToPreyBreed() {
		return stepsToPreyBreed;
	}

	static void setStepsToPreyBreed(int stepsToPreyBreed) {
		Pond.stepsToPreyBreed = stepsToPreyBreed;
	}

	static int getNumRedRecruits() {
		return numRedRecruits;
	}

	public static void setNumRedRecruits(int numRedRecruits) {
		Pond.numRedRecruits = numRedRecruits;
	}

	static int getNumPreyRecruits() {
		return numPreyRecruits;
	}

	static void setNumPreyRecruits(int numPreyRecruits) {
		Pond.numPreyRecruits = numPreyRecruits;
	}

	static int getMaxRedFish() {
		return maxRedFish;
	}

	static void setMaxRedFish(int maxRedFish) {
		Pond.maxRedFish = maxRedFish;
	}

	static int getMaxPreyFish() {
		return maxPreyFish;
	}

	static void setMaxPreyFish(int maxPreyFish) {
		Pond.maxPreyFish = maxPreyFish;
	}

	static int getRedStarve() {
		return redStarve;
	}

	static void setRedStarve(int redStarve) {
		Pond.redStarve = redStarve;
	}

	static Continuous2D getRedSpace() {
		return redSpace;
	}

	static void setRedSpace(Continuous2D redSpace) {
		Pond.redSpace = redSpace;
	}

	static Continuous2D getBlueSpace() {
		return preySpace;
	}

	static void setBlueSpace(Continuous2D preySpace) {
		Pond.preySpace = preySpace;
	}
}
