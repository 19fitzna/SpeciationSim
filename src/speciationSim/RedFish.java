package speciationSim;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Double2D;

@SuppressWarnings("serial")
class RedFish implements Steppable {
	private Stoppable stopper;
	private Double2D loc;
	private int hunger = 0;
	private int stepsSinceBreeding = 0;

	RedFish (Double2D loc, int stepsSinceBreeding, int hunger) { //this must be in exact order as above 
		setLoc(loc);
		this.stepsSinceBreeding = stepsSinceBreeding;  //this is diff stepsSinceBreeding than above
		this.hunger = hunger;
		init();
	}

	void init() {
		hunger = 0;
	}


	@Override
	public void step(SimState state) {
		this.move();
		hunger++;
		this.tryFeed();
		checkStarve();
		stepsSinceBreeding++;     //when thing steps, everything happens
		if (stepsSinceBreeding > Pond.getStepsToRedBreed ()) {
			Pond.recruitRedFish (Pond.getNumRedRecruits()); //a fish doesnt know how many fish there are, but it knows where itself is.
			stepsSinceBreeding =0;
		}
	}

	private void move() { //private void means its not going to return anything but its going to DO something
		double x = Pond.redSpace.stx(loc.x + (Pond.getRand(). nextInt(3)-1)); //stx is simple torroidal in x
		double y = Pond.redSpace.sty(loc.y + (Pond.getRand(). nextInt(3)-1));
		
		Double2D locNew = new Double2D(x, y);
		setLoc(locNew);
	}

	//the prey is in bluespace so all space refs in tryfeed are for bluespace
	void tryFeed() {
		Bag neighbors  = Pond.preySpace.getNeighborsExactlyWithinDistance(loc, Pond.getNeighborhood(), true);
		//neighbors.addAll(Pond.greySpace.getNeighborsExactlyWithinDistance(loc, Pond.getNeighborhood(), true));
		if (neighbors != null && neighbors.size() > 0 ) {
			neighbors.shuffle(Pond.getRand() );
			for (int i=0; i <neighbors.size(); i++) {
				PreyFish preyFish = (PreyFish) neighbors.get(i); //its looking for a bluefish

				if (preyFish != null) {
					if(!(preyFish.alleleOne || preyFish.alleleTwo)) {
						if(Pond.getRand().nextDouble() < Pond.selectionFactor) return;
					}
					preyFish.die();
					this.hunger =0;
					return;

				}
			}
		}
	}

	void checkStarve() {
		if (hunger > Pond.getRedStarve() ) {
			int num = Pond.getNumRedFish();
			num--;
			if (num < 1) Pond.getSched().clear(); // this ends the simulation if all the red predator fish die
			Pond.setNumRedFish(num);
			stopper.stop();
			Pond.redSpace.remove(this);
		}

	}

	void setLoc (Double2D loc) {
		this.loc =loc;
		Pond.redSpace.setObjectLocation(this, loc);
	}



	void setStopper(Stoppable stopper) {
		this.stopper = stopper;
	}



	void die() {
		int num = Pond.getNumRedFish();
		num--;
		Pond.setNumRedFish(num);
		if (num < 1) Pond.getSched().clear();
		stopper.stop();
		Pond.redSpace.remove(this);

	}
	
}