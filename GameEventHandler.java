/** This class processes in game events one example of events 
 *  can be activate/disactivate an enemy, open a gate etc...
 */
public class GameEventHandler {
	
	public static void processEvent() {
		
		//Open the West energy gates when the power plants in the north West are destroyed 
		if(main.Terrain.powerPlants[0] == null && main.Terrain.powerPlants[1] == null && main.Terrain.fences[26] !=null) {
			for(int i = 26; i < 38; i++){
				main.Terrain.fences[i].destory();
				main.Terrain.fences[i] = null;
			}
		}
		
		//Open the east energy gates when the power plants in the north east are destroyed 
		if(main.Terrain.powerPlants[5] == null && main.Terrain.powerPlants[6] == null && main.Terrain.fences[14] !=null) {
			for(int i = 14; i < 26; i++){
				main.Terrain.fences[i].destory();
				main.Terrain.fences[i] = null;
			}
		}
		
		//Open the energy gates of the main base when all the outer power plants are destroyed
		//Also activate the tanks inside the base
		if(main.Terrain.powerPlants[0] == null && main.Terrain.powerPlants[1] == null 
		   && main.Terrain.powerPlants[5] == null && main.Terrain.powerPlants[6] == null
		   && main.Terrain.fences[0] != null) {
			for(int i = 0; i < 14; i++) {
				main.Terrain.fences[i].destory();
				main.Terrain.fences[i] = null;
			}
			
			for(int i = 38; i < 43; i++) {
				Enemies.enemy[i].damage(-1);
			}
			
			for(int i = 96; i < 107; i++) {
				Enemies.enemy[i].damage(-1);
			}
			
		}
		
		//Open the inner energy gates when all the inner power plants are destoryed. 
		//Also activate boss tanks
		if(main.Terrain.powerPlants[2] == null && main.Terrain.powerPlants[3] == null 
		   && main.Terrain.powerPlants[4] == null && main.Terrain.fences[39] != null) {
			for(int i = 38; i < 62; i++) {
				main.Terrain.fences[i].destory();
				main.Terrain.fences[i] = null;
			}
			Enemies.enemy[107].damage(-1);
			Enemies.enemy[108].damage(-1);
			
		}
		
		//The game is beaten when both annihilators are destroyed
		if(main.gameNotStart == false) {
			if(Enemies.enemy[107] == null && Enemies.enemy[108] == null) {
				main.win = true;
				main.PT.HP = 100;
			}
				
		}
		
	
	}
	
}
