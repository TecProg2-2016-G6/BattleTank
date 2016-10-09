package src;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.directory.InvalidAttributeValueException;

/** This class processes in game events one example of events 
 *  can be activate/disactivate an enemy, open a gate etc...
 */
public class GameEventHandler {
	
	private static final Logger LOGGER = Logger.getLogger( GameEventHandler.class.getName() );
	
	public static void processEvent() {
		
		//Open the West energy gates when the power plants in the north West are destroyed 
		if(Main.Terrain.powerPlants[0] == null && Main.Terrain.powerPlants[1] == null && Main.Terrain.fences[26] !=null) {
			for(int i = 26; i < 38; i++){
				Main.Terrain.fences[i].destory();
				Main.Terrain.fences[i] = null;
			}
		}
		
		//Open the east energy gates when the power plants in the north east are destroyed 
		if(Main.Terrain.powerPlants[5] == null && Main.Terrain.powerPlants[6] == null && Main.Terrain.fences[14] !=null) {
			for(int i = 14; i < 26; i++){
				Main.Terrain.fences[i].destory();
				Main.Terrain.fences[i] = null;
			}
		}
		
		//Open the energy gates of the main base when all the outer power plants are destroyed
		//Also activate the tanks inside the base
		if(Main.Terrain.powerPlants[0] == null && Main.Terrain.powerPlants[1] == null 
		   && Main.Terrain.powerPlants[5] == null && Main.Terrain.powerPlants[6] == null
		   && Main.Terrain.fences[0] != null) {
			for(int i = 0; i < 14; i++) {
				Main.Terrain.fences[i].destory();
				Main.Terrain.fences[i] = null;
			}
			
			for(int i = 38; i < 43; i++) {
				try {
					Enemies.enemy[i].damage(-1);
				} catch (InvalidAttributeValueException e) {
					// TODO Auto-generated catch block
					LOGGER.log(Level.WARNING, "Invalid Damege Value");
				}
			}
			
			for(int i = 96; i < 107; i++) {
				try {
					Enemies.enemy[i].damage(-1);
				} catch (InvalidAttributeValueException e) {
					// TODO Auto-generated catch block
					LOGGER.log(Level.WARNING, "Invalid Damege Value");
				}
			}
			
		}
		
		//Open the inner energy gates when all the inner power plants are destoryed. 
		//Also activate boss tanks
		if(Main.Terrain.powerPlants[2] == null && Main.Terrain.powerPlants[3] == null 
		   && Main.Terrain.powerPlants[4] == null && Main.Terrain.fences[39] != null) {
			for(int i = 38; i < 62; i++) {
				Main.Terrain.fences[i].destory();
				Main.Terrain.fences[i] = null;
			}
			try {
				Enemies.enemy[107].damage(-1);
			} catch (InvalidAttributeValueException e) {
				// TODO Auto-generated catch block
				LOGGER.log(Level.WARNING, "Invalid Damege Value");			}
			try {
				Enemies.enemy[108].damage(-1);
			} catch (InvalidAttributeValueException e) {
				// TODO Auto-generated catch block
				LOGGER.log(Level.WARNING, "Invalid Damege Value");			}
			
		}
		
		//The game is beaten when both annihilators are destroyed
		if(Main.gameNotStart == false) {
			if(Enemies.enemy[107] == null && Enemies.enemy[108] == null) {
				Main.win = true;
				Main.PlayerTank.HP = 100;
			}
				
		}
		
	
	}
	
}
