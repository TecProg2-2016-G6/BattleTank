//Stone model 
public class Stone extends SolidObject{
	//the polygons of the model
	private Polygon3D[] polygons; 
	
	//the shadow map generated by the model
	public Polygon3D shadow;
	
	//this parameter will affect the size of the model
	private double scale;
	
	//whether the stone will undergo collision test
	private boolean testCollision;
	
	//orientation of the stone
	public int angle;
	
	//type of the stone
	public int type;
	
	//the texture index for the stone
	public int textureIndex;
	
	public Stone(double x, double y, double z, int angle, int type, double scale, int textureIndex, boolean testCollision){
		this.scale = scale;
		this.angle = angle;
		this.type = type;
		this.textureIndex = textureIndex;
		this.testCollision = testCollision;
		
		start = new Vector(x,y,z);
		iDirection = new Vector(1,0,0);
		jDirection = new Vector(0,1,0);
		kDirection = new Vector(0,0,1);
		
		
		
		iDirection.scale(scale);
		jDirection.scale(scale);
		kDirection.scale(scale);
		
		//3D boundary of this model has a cubic shape (ie l = w)
		modelType = 3;  
		makeBoundary(0.25*scale, 0.5*scale, 0.25*scale);
		
		//create 2D boundary
		boundary2D = new Rectangle2D(x - 0.2*scale, z + 0.2*scale, 0.4*scale, 0.4*scale);
		//underwater rocks are excluded from collision tests,because the player will never reach them
		if(testCollision){
			if(scale != 1)
				ObstacleMap.registerObstacle2(this, (int)(x*4) + (129-(int)(z*4))*80);
			else{
				//rocks with scale = 1 occupy 4 tiles
				ObstacleMap.registerObstacle2(this, (int)((x-0.125)*4) + (129-(int)((z+0.125)*4))*80);
				ObstacleMap.registerObstacle2(this, (int)((x+0.125)*4) + (129-(int)((z+0.125)*4))*80);
				ObstacleMap.registerObstacle2(this, (int)((x-0.125)*4) + (129-(int)((z-0.125)*4))*80);
				ObstacleMap.registerObstacle2(this, (int)((x+0.125)*4) + (129-(int)((z-0.125)*4))*80);
			}
		}
		
		//adjust orientation of the model
		iDirection.rotate_XZ(angle);
		kDirection.rotate_XZ(angle);
		
		//find centre of the model in world coordinate
		findCentre();
		
		makePolygons(type, textureIndex);
	}
	
	//return the 2D boundary of this model
	public Rectangle2D getBoundary2D(){
		return boundary2D;
	}
	
	//Construct polygons for this model.
	//The polygon data is hard-coded here
	private void makePolygons(int type, int textureIndex){
		Vector[] v;
		
		double x = start.x;
		double y = start.y;
		double z = start.z;
		
		if(type == 1){
			polygons = new Polygon3D[8];
		
			v = new Vector[]{put(-0.07, -0.1, 0.01), put(0.085, -0.1, 0.01), put(0.125, -0.25, -0.125), put(-0.1, -0.25, -0.125)};
			polygons[0] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(-0.08, -0.1, 0.07), put(-0.07, -0.1, 0.01), put(-0.1, -0.25, -0.125), put(-0.125, -0.25, 0.08)};
			polygons[1] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[textureIndex], 0.5,0.5,6);
		
			v = new Vector[]{put(-0.08, -0.1, 0.07), put(0.085, -0.1, 0.01), put(-0.07, -0.1, 0.01)};
			polygons[2] = new Polygon3D(v, v[2], v[0], v [1], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(-0.08, -0.1, 0.07), put(-0.125, -0.25, 0.08), put(-0.05, -0.25, 0.125)};
			polygons[3] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.5,0.5,6);
		
			v = new Vector[]{put(0.085, -0.1, 0.01), put(0.085, -0.1, 0.09), put(0.125, -0.25, 0.125), put(0.125, -0.25, -0.125)};
			polygons[4] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(0.085, -0.1, 0.09), put(-0.05, -0.25, 0.125), put(0.125, -0.25, 0.125)};
			polygons[5] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(0.085, -0.1, 0.09), put(0.085, -0.1, 0.01), put(-0.08, -0.1, 0.07)};
			polygons[6] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(-0.08, -0.1, 0.07), put(-0.05, -0.25, 0.125), put(0.085, -0.1, 0.09)};
			polygons[7] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.5,0.5,6);
		}
		
		if(type == 2){
			
			
			polygons = new Polygon3D[24];
			v = new Vector[]{put(-0.1, -0.1, -0.2), put(0.1, -0.1, -0.2), put(0.15, -0.25, -0.25), put(-0.15, -0.25, -0.25)};
			polygons[0] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(-0.1, -0.1, -0.2), put(-0.15, -0.25, -0.25), put(-0.22, -0.25, -0.1), put(-0.15, -0.1, -0.1)};
			polygons[1] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(-0.15, -0.1, -0.1), put(-0.22, -0.25, -0.1), put(-0.25, -0.25, 0.1), put(-0.18, -0.1, 0.1)};
			polygons[2] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.5,0.5,6);
		
			v = new Vector[]{put(-0.18, -0.1, 0.1), put(-0.25, -0.25, 0.1), put(-0.17, -0.25, 0.2), put(-0.12, -0.1, 0.17)};
			polygons[3] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.5,0.5,6);
		
			v = new Vector[]{put(-0.12, -0.1, 0.17), put(-0.17, -0.25, 0.2), put(0.02, -0.25, 0.25), put(0.02, -0.1, 0.2)};
			polygons[4] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.5,0.5,6);
		
			v = new Vector[]{put(0.02, -0.1, 0.2), put(0.02, -0.25, 0.25), put(0.2, -0.25, 0.15), put(0.15, -0.1, 0.13)};
			polygons[5] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.5,0.5,6);
			
			v = new Vector[]{put(0.15, -0.1, 0.13), put(0.2, -0.25, 0.15), put(0.25, -0.25, 0), put(0.2, -0.1, 0)};
			polygons[6] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(0.2, -0.1, 0), put(0.25, -0.25, 0), put(0.22, -0.25, -0.15),put(0.18, -0.1, -0.12)};
			polygons[7] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.2,0.5,6);
			
			v = new Vector[]{put(0.18, -0.1, -0.12), put(0.22, -0.25, -0.15), put(0.15, -0.25, -0.25),put(0.1, -0.1, -0.2)};
			polygons[8] = new Polygon3D(v, v[3], v[0], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(0, 0, -0.1), put(0.1, -0.1, -0.2), put(-0.1, -0.1, -0.2)};
			polygons[9] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
		
			v = new Vector[]{put(0, 0, -0.1), put(-0.1, -0.1, -0.2),put(-0.15, -0.1, -0.1)};
			polygons[10] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.15, -0.1, -0.1), put(-0.08, 0, -0), put(0, 0, -0.1)};
			polygons[11] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.08, 0, -0), put(-0.15, -0.1, -0.1), put(-0.18, -0.1, 0.1)};
			polygons[12] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.18, -0.1, 0.1), put(-0.09, 0, 0.1),put(-0.08, 0, -0)};
			polygons[13] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.09, 0, 0.1), put(-0.18, -0.1, 0.1), put(-0.12, -0.1, 0.17)};
			polygons[14] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.09, 0, 0.1), put(-0.12, -0.1, 0.17), put(0.02, -0.1, 0.2)};
			polygons[15] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.02, -0.1, 0.2), put(0.06, 0, 0.06), put(-0.09, 0, 0.1)};
			polygons[16] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.06, 0, 0.06), put(0.02, -0.1, 0.2), put(0.15, -0.1, 0.13)};
			polygons[17] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.06, 0, 0.06), put(0.15, -0.1, 0.13), put(0.2, -0.1, 0)};
			polygons[18] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.06, 0, 0.06), put(0.2, -0.1, 0)};
			polygons[19] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.2, -0.1, 0), put(0.18, -0.1, -0.12)};
			polygons[20] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.18, -0.1, -0.12), put(0.1, -0.1, -0.2)};
			polygons[21] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.1, -0.1, -0.2), put(0, 0, -0.1)};
			polygons[22] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0, 0, -0.1), put(-0.08, 0, -0), put(-0.09, 0, 0.1), put(0.06, 0, 0.06),put(0.11, 0, -0.09)};
			polygons[23] = new Polygon3D(v, put(-1, 0, 0), put(0, 0, 0), put(-1,0, -1), Main.textures[textureIndex], 3,3,6);
		
			
			//shadow
			iDirection = new Vector(0.9,0,0);
			jDirection = new Vector(0,1,0);
			kDirection = new Vector(0,0,0.9);
			
			iDirection.scale(scale);
			jDirection.scale(scale);
			kDirection.scale(scale);
			
			v = new Vector[]{put(-0.5, -0.25, 0.4), put(0.4, -0.25, 0.4), put(0.4, -0.25, -0.5),put(-0.5, -0.25, -0.5)};
			shadow = new Polygon3D(v, v[0], v[1],v[3], Main.textures[8], 1,1,2);
		}
		
		if(type == 4){
			jDirection.scale(1.3);
			start.add(0,0.03,0);
			
			polygons = new Polygon3D[15];
			v = new Vector[]{put(0, 0, -0.1), put(0.1, -0.1, -0.2), put(-0.1, -0.1, -0.2)};
			polygons[0] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
		
			v = new Vector[]{put(0, 0, -0.1), put(-0.1, -0.1, -0.2),put(-0.15, -0.1, -0.1)};
			polygons[1] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.15, -0.1, -0.1), put(-0.08, 0, -0), put(0, 0, -0.1)};
			polygons[2] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.08, 0, -0), put(-0.15, -0.1, -0.1), put(-0.18, -0.1, 0.1)};
			polygons[3] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.18, -0.1, 0.1), put(-0.09, 0, 0.1),put(-0.08, 0, -0)};
			polygons[4] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.09, 0, 0.1), put(-0.18, -0.1, 0.1), put(-0.12, -0.1, 0.17)};
			polygons[5] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.4,0.5,6);
			
			v = new Vector[]{put(-0.09, 0, 0.1), put(-0.12, -0.1, 0.17), put(0.02, -0.1, 0.2)};
			polygons[6] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.02, -0.1, 0.2), put(0.06, 0, 0.06), put(-0.09, 0, 0.1)};
			polygons[7] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.06, 0, 0.06), put(0.02, -0.1, 0.2), put(0.15, -0.1, 0.13)};
			polygons[8] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.06, 0, 0.06), put(0.15, -0.1, 0.13), put(0.2, -0.1, 0)};
			polygons[9] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.06, 0, 0.06), put(0.2, -0.1, 0)};
			polygons[10] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.2, -0.1, 0), put(0.18, -0.1, -0.12)};
			polygons[11] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.18, -0.1, -0.12), put(0.1, -0.1, -0.2)};
			polygons[12] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0.11, 0, -0.09), put(0.1, -0.1, -0.2), put(0, 0, -0.1)};
			polygons[13] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[textureIndex], 0.7,0.7,6);
			
			v = new Vector[]{put(0, 0, -0.1), put(-0.08, 0, -0), put(-0.09, 0, 0.1), put(0.06, 0, 0.06),put(0.11, 0, -0.09)};
			polygons[14] = new Polygon3D(v, put(-1, 0, 0), put(0, 0, 0), put(-1,0, -1), Main.textures[textureIndex], 3,3,6);
			
			start.add(0,-0.03,0);
		}
		
		start = new Vector(x,y,z);
		iDirection = new Vector(1,0,0);
		jDirection = new Vector(0,1,0);
		kDirection = new Vector(0,0,1);
		
		iDirection.scale(scale);
		jDirection.scale(scale);
		kDirection.scale(scale);
		
		iDirection.rotate_XZ(angle);
		kDirection.rotate_XZ(angle);
	}
	
	//update the model 
	public void update(){
		tempCentre.set(centre);
		tempCentre.y = 0.25;
		tempCentre.subtract(camera.absolutePosition);
		if(tempCentre.getLength() > 5.5){
			polygons = null;
			visible = false;
			return;
		}
		
		
		//find centre in camera coordinate
		tempCentre.set(centre);
		tempCentre.y = -1;
		tempCentre.subtract(camera.position);
		tempCentre.rotate_XZ(camera.XZ_angle);
		tempCentre.rotate_YZ(camera.YZ_angle);
		tempCentre.updateLocation();
		
		//test whether the model is visible by comparing the 2D position of its centre point and the screen area
		if(tempCentre.z < 0.9 || tempCentre.screenY < -10 || (tempCentre.screenX < -60 && tempCentre.z > 3) || (tempCentre.screenX >700 &&  tempCentre.z > 3)){
			
			visible = false;
			return;
		}
		visible = true;
		if(testCollision)
			ModelDrawList.register(this);
		
		if(polygons == null){
			makePolygons(type, textureIndex);
			
		}
		
		//update polygons
		for(int i = 0; i < polygons.length; i++){
			polygons[i].update();
		}
		
		if(shadow != null){
			shadow.update();
			if(shadow.visible){
				Rasterizer.rasterize(shadow);
			}
		}
	}
	
	public void draw(){
		if(visible){
			for(int i = 0; i < polygons.length; i++){
				polygons[i].draw();
			}
		}
	}
}
