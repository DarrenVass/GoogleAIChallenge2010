public class Planet implements Cloneable {
    // Initializes a planet.
    public Planet(int planetID,
                  int owner,
		  int numShips,
		  int growthRate,
		  double x,
		  double y) {
	this.planetID = planetID;
	this.owner = owner;
	this.numShips = numShips;
	this.growthRate = growthRate;
	this.x = x;
	this.y = y;
    }

    // Accessors and simple modification functions. These should be mostly
    // self-explanatory.
    public int PlanetID() {
    	return planetID;
    }

    public int Owner() {
    	return owner;
    }

    public int NumShips() {
    	return numShips;
    }

    public int GrowthRate() {
    	return growthRate;
    }

    public double X() {
    	return x;
    }

    public double Y() {
    	return y;
    }

    public void Owner(int newOwner) {
    	this.owner = newOwner;
    }

    public void NumShips(int newNumShips) {
    	this.numShips = newNumShips;
    }

    public void AddShips(int amount) {
    	numShips += amount;
    }

    public void RemoveShips(int amount) {
    	numShips -= amount;
    }
    
    /**
     * The Rate of Return of the planet, or how long it will take to send those ships back.
     * Where ships = number on the planet + 1;.
     * The lower the value the better
     * @param p starting planet
     * @return value or how long it will take the ships to be returned.
     */
    public int ROR(Planet p){
    	double dx = x - p.X();
    	double dy = y - p.Y();
    	int distance = (int)Math.ceil(Math.sqrt(dx * dx + dy * dy));  
    	int value = distance + distance + (int)Math.ceil((numShips +1) / growthRate);
    	return value;
    }
    
    /**
     * Return on Investment, # of turns to get to planet and remake the ships sent.
     * @param p
     * @return
     */
    public int ROI(Planet p){
    	double dx = x - p.X();
    	double dy = y - p.Y();
    	int distance = (int)Math.ceil(Math.sqrt(dx * dx + dy * dy));  
    	int value = distance + (int)Math.ceil((numShips +1) / growthRate);
    	return value;
    }
    
   
    private int planetID;
    private int owner;
    private int numShips;
    private int growthRate;
    private double x, y;

    private Planet (Planet _p) {
	planetID = _p.planetID;
	owner = _p.owner;
	numShips = _p.numShips;
	growthRate = _p.growthRate;
	x = _p.x;
	y = _p.y;
	
    }

    public Object clone() {
    	return new Planet(this);
    }
}
