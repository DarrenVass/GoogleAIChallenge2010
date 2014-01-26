import java.util.*;

public class MyBot {
    // The DoTurn function is where your code goes. The PlanetWars object
    // contains the state of the game, including information about all planets
    // and fleets that currently exist. Inside this function, you issue orders
    // using the pw.IssueOrder() function. For example, to send 10 ships from
    // planet 3 to planet 8, you would say pw.IssueOrder(3, 8, 10).
    //
    // There is already a basic strategy in place here. You can use it as a
    // starting point, or you can throw it out entirely and replace it with
    // your own. Check out the tutorials and articles on the contest website at
    // http://www.ai-contest.com/resources.
    public static void DoTurn(PlanetWars pw) {
    	if(turnNumber==0){ //First Turn
    		System.err.println("First");
    		//Smart Expand Preventing a mass first move attack against my home.
    		
    		//Do up distance calculations, not much to do on first turn anyhow.
    		pw.calcDistances();
    		
    		int shipsTotal = pw.NumShips(1);
    		int enemyShips = pw.NumShips(2);
    		Planet home = pw.MyPlanets().get(0);
    		Planet enemy = pw.EnemyPlanets().get(0);    		
    		int bufferTime = pw.getDistance(home.PlanetID(), enemy.PlanetID());    		
    		int sendableShips = (shipsTotal + home.GrowthRate() * bufferTime) - (enemyShips);
    		
    		if(sendableShips>home.NumShips()) sendableShips = home.NumShips();//You can send at max what you start with.    		
    		
    		List<Planet> goldItems = new ArrayList<Planet>();
    		for(Planet p: pw.NeutralPlanets()){
    			if (p.NumShips() < sendableShips){
    				goldItems.add(p);
    			}
    		}
    		
    		//Weed out planets closer/equidistant to enemy or ones with 0 growth rate.
    		for(Iterator<Planet> i = goldItems.iterator(); i.hasNext();){
    			Planet x= i.next();
    			if(pw.getDistance(home.PlanetID(), x.PlanetID()) >= pw.getDistance(enemy.PlanetID(), x.PlanetID()) || x.GrowthRate()==0){
    				i.remove();
    			}
    		}

    		//Find the best choices and issue orders.
    		List<Planet> attacks = MyBot.knapSack(1, sendableShips, goldItems, pw.MyPlanets());
    		for(Planet p: attacks){
    			pw.IssueOrder(home, p, (p.NumShips()+1));
    		}

    	}else { //Early to Mid Game

    		if(pw.EnemyPlanets().size()>0 && pw.MyPlanets().size()>0){
    			for(Planet p: pw.MyPlanets()){
    				int numShips = p.NumShips();
    				pw.IssueOrder(p, pw.EnemyPlanets().get(0), numShips);
    			}
    		}
    	}    	
		return;
    }//End of DoTurn
    
    /** Used for finding the best Planet combination to match a problem
     * given by a trigger (trigger). 
     * 1 - First move Optimization. Maximum Growth Rate for available ships.
     * 2 - Frontier Planet Finder
     * 
     * Greedy - Growth / Cost in order then put in highest.
    **/
    public static List<Planet> knapSack(int trigger, int maxShips, List<Planet> choices, List<Planet> mine){
    	List<Planet> best = new ArrayList<Planet>();

    	if(trigger ==1){ //First Move
    		
    		System.err.println("Ships avail: " + maxShips);
    		Planet home = mine.get(0);
	    	List<Integer> ordered = new ArrayList<Integer>();
	    	
	    	//For each choice find out how the Growth Rates
	    	for(int i=0; i < choices.size(); i++){
	    		ordered.add(choices.get(i).GrowthRate());
	    	}
	    	System.err.println("Values: " + ordered.toString());
	    		    	
	    	//Pick out the best Growth rates, if GR = GR, then take closer one first.
	    	
	    	int chooseMe, currentValue;
	    	do{
	    		chooseMe = -1;
	    		currentValue = Integer.MIN_VALUE;
	    		for(int i=0;i<ordered.size();i++){
	    			if(choices.get(i).NumShips() < maxShips && ordered.get(i) >= currentValue){
	    				//Same Growth Rate, but closer.
	    				if(choices.get(i).GrowthRate() == currentValue && choices.get(i).ROI(home)<choices.get(chooseMe).ROI(home)){
	    					chooseMe = i;
	    					currentValue = choices.get(i).GrowthRate();
	    				//Growth Rate is larger.
	    				} else if(choices.get(i).GrowthRate() > currentValue){
	    					chooseMe = i;
	    					currentValue = choices.get(i).GrowthRate();
	    				}
	    			}
	    		}
	    		
	    		//Add new ship
	    		if(chooseMe != -1){
		    		System.err.println("Add Planet: " + chooseMe + " GR = " + currentValue);
		    		maxShips -= choices.get(chooseMe).NumShips();
		    		System.err.println("Ships Left = " + maxShips);
		    		best.add(choices.get(chooseMe));
		    		ordered.set(chooseMe, Integer.MIN_VALUE);
	    		}
	    	}while(chooseMe != -1);
	    	
	    	
	    	
    	}else if(trigger == 2){ //Find frontier Planets.
    		
    	}
    	
    	
    	
    	return best;
    }

    public static void main(String[] args) {
		String line = "";
		String message = "";
		int c;
		try {
		    while ((c = System.in.read()) >= 0) {
			switch (c) {
			case '\n':
			    if (line.equals("go")) {
				PlanetWars pw = new PlanetWars(message);
				DoTurn(pw);
					turnNumber++;
			        pw.FinishTurn();
				message = "";
			    } else {
				message += line + "\n";
			    }
			    line = "";
			    break;
			default:
			    line += (char)c;
			    break;
			}
		    }
		} catch (Exception e) {
		    // Owned.
		}
    }//Main End
    
    private static int turnNumber;
    
}

