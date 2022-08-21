package speciationSim;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Double2D;

@SuppressWarnings("serial")
class PreyFish implements Steppable
{
	protected Stoppable stopper;
	protected Double2D loc;
	protected int stepsSinceBreeding = 0;

	//Used to determine color, TRUE = Prey allele, prey is dominant
	boolean alleleOne = true;
	boolean alleleTwo = true;

	PreyFish() {};

	PreyFish  (Double2D loc, int stepsSinceBreeding, boolean alleleOne, boolean alleleTwo) { //adds location w/ two double numbers (x,y)
		setLoc(loc);
		this.stepsSinceBreeding = stepsSinceBreeding;  //this is diff stepsSinceBreeding than above


		this.alleleOne = alleleOne;
		this.alleleTwo = alleleTwo;

	}


	@Override
	public void step(SimState state) {
		this.move();
		stepsSinceBreeding++;     //when thing steps, everything happens
		if (stepsSinceBreeding > Pond.getStepsToPreyBreed ())
		{
			this.tryBreed();
		}
	}

	private void move() { //private void means its not going to return anything but its going to DO something 

		double x = Pond.preySpace.stx(loc.x + (Pond.getRand(). nextInt(3)-1)); //stx is simple torroidal in x
		double y = Pond.preySpace.sty(loc.y + (Pond.getRand(). nextInt(3)-1));

		Double2D locNew = new Double2D(x, y);
		setLoc(locNew);
	}

	void die() {
		int num = Pond.getNumPreyFish();
		num--;
		Pond.setNumPreyFish(num);
		if (num < 1) {
			Pond.getSched().clear();
		}
		stopper.stop();


		Pond.preySpace.remove(this);

	}

	boolean tryBreed() {
		Bag neighbors =  Pond.preySpace.getNeighborsExactlyWithinDistance(loc, Pond.getNeighborhood(), true);
		if (neighbors != null && neighbors.size() > 0 )
		{
			neighbors.shuffle(Pond.getRand() );
			for (int i=0; i <neighbors.size(); i++)
			{
				PreyFish preyFish = (PreyFish) neighbors.get(i); //looking for a preyfish

				if ((preyFish != null) && (this.loc != preyFish.loc) )
				{
					Pond.breedPreyFish(preyFish, this);
					this.stepsSinceBreeding = 0;
					preyFish.stepsSinceBreeding = 0;
					return true;

				}
			}
		}
		return false;
	}

	void setLoc (Double2D loc) {
		this.loc = loc;
		Pond.preySpace.setObjectLocation(this, loc);
	}
	void setStopper(Stoppable stopper)
	{
		this.stopper = stopper;

	}

}
