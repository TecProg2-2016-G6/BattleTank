package src;
//The interface which represents 3D models.

import javax.naming.directory.InvalidAttributeValueException;

public interface Model{
	//Update the model, perform AI actions if there is any.
	public void update();
	
	//Get a rough 3D boundary of the model in camera coordinate.
	public Polygon3D[] getBoundaryModelInCameraCoordinate();
	
	//Get  2D boundary of this model (will be used for collision detection).
	public Rectangle2D getBoundary2D();
	
	//Get centre of this model in camera coordinate.
	public Vector getCentreModelInCameraCoordinate();
	
	//Get centre of this model in world coordinate.
	public Vector getRealCentreModelWorld();
	
	//Draw the polygons of the model.
	public void drawExplosion();
	
	/**return the type of the model
	  * To be more specific:
	  * type 1 = player tank
	  * type 2 = enemy tank
	  * type 3 = rocks, and most other object that will interact with projectiles.
	  * type 4 = projectiles
	  * type 5 = powerups
	  * type 6 = walls
	  * type 7 = trees
	  */
	
	public int getTypeOfModel(); 
	  
	//Get the depth value of the tank.
	public double getTankDepth();
	
	//Get the current life span of the model.
	public int getLifeSpan();
	
	//Damage the object. (ie, reduce its hitpoint).
	public void damage(int damagePoint) throws InvalidAttributeValueException;
	
	//Give way when it has blocked other model.
	public void unstuck();
}