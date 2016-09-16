package src;
//this class generate railgun  trails
public class RailgunTail {

	
	public RailgunTail(Vector startPoint, int angle, boolean isHostile){
		Vector direction = new Vector(0,0,0.1);
		direction.rotate_XZ(angle);
		for(int i = 0; i < 21; i++){
			Helix temp = new Helix(startPoint.myClone(), angle);
			int position = (int)(temp.boundaryModel2D.xPos*4) + (129-(int)(temp.boundaryModel2D.yPos*4))*80;
			Projectiles.register(temp);
			if(ObstacleMap.slugCollideObstacle2(temp, position, isHostile)){
				break;
			}
			
			startPoint.add(direction);
			
		}
		
		
			
	}
}
