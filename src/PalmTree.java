package src;
//palmTree model 
public class PalmTree extends SolidObject{
	//the polygons of the model
	private  Polygon3D[] polygons; 
	
	//the shadow map generated by the model
	public Polygon3D shadow;
	
	public int angle;
	
	
	public PalmTree(double x, double y, double z, int angle){
		this.angle = angle;
		
		startPointInWorld = new Vector(x,y,z);
		
		iDirection = new Vector(0.7+0.3*1.1,0,0);
		jDirection = new Vector(0,0.8+0.3,0);
		kDirection = new Vector(0,0,0.7+0.3*1.1);
		
		//boundary of this model has a cubic shape (ie l = w)
		modelType = 7;  
		makeBoundary(0.1, 0.25, 0.1);
		
		//create 2D boundary
		boundaryModel2D = new Rectangle2D(x - 0.005, z + 0.005, 0.01, 0.01);
		ObstacleMap.registerObstacle1(this, (int)(x*4) + (129-(int)(z*4))*80);
		
		
		
		//adjust orientation of the model
		iDirection.rotate_XZ(angle);
		kDirection.rotate_XZ(angle);
		
		//find centre of the model in world coordinate
		findCentre();
		
		makePolygons();
	}
	
	//return the 2D boundary of this model
	public Rectangle2D getBoundary2D(){
		return boundaryModel2D;
	}
	
	//Construct polygons for this model.
	//The polygon data is hard-coded here
	private void makePolygons(){
		Vector[] v;
		double x = startPointInWorld.x;
		double y = startPointInWorld.y;
		double z = startPointInWorld.z;
		
		startPointInWorld.add(0,-0.25,0);
		polygons = new Polygon3D[38]; 
		
		//body
		v = new Vector[]{put(-0.001, 0.1, -0.01), put(0.016, 0.1, -0.01), put(0.01, 0, -0.01), put(-0.014, 0, -0.01)};
		polygons[0] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		v = new Vector[]{put(-0.001, 0.1, 0.01), put(-0.001, 0.1, -0.01), put(-0.014, 0, -0.01), put(-0.014, 0, 0.014)};
		polygons[1] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		v = new Vector[]{put(0.016, 0.1, 0.01), put(-0.001, 0.1, 0.01), put(-0.014, 0, 0.014), put(0.01, 0, 0.014)};
		polygons[2] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		v = new Vector[]{put(0.016, 0.1, -0.01), put(0.016, 0.1, 0.01), put(0.01, 0, 0.014), put(0.01, 0, -0.01)};
		polygons[3] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		v = new Vector[]{put(0.002, 0.3, -0.008), put(0.013, 0.3, -0.008), put(0.016, 0.1, -0.01), put(-0.001, 0.1, -0.01)};
		polygons[4] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		v = new Vector[]{put(0.002, 0.3, 0.006), put(0.002, 0.3, -0.008), put(-0.001, 0.1, -0.01),put(-0.001, 0.1, 0.01)};
		polygons[5] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		v = new Vector[]{put(0.013, 0.3, 0.006), put(0.002, 0.3, 0.006), put(-0.001, 0.1, 0.01),put(0.016, 0.1, 0.01)};
		polygons[6] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		v = new Vector[]{put(0.013, 0.3, -0.008), put(0.013, 0.3, 0.006), put(0.016, 0.1, 0.01), put(0.016, 0.1, -0.01)};
		polygons[7] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[5], 0.1,0.5,6);
		
		//leaves
		startPointInWorld.add(0.005, 0,0);
		v = new Vector[]{put(0.015, 0.3, 0.01), put(0, 0.3, 0), put(0, 0.34, 0.05), put(0.015, 0.32, 0.05)};
		polygons[8] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.3, 0), put(-0.015, 0.3, 0.01), put(-0.015, 0.32, 0.05), put(0, 0.34, 0.05)};
		polygons[9] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.34, 0.05), put(0, 0.33, 0.09), put(0.015, 0.31, 0.09), put(0.015, 0.32, 0.05)};
		polygons[10] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
	
		v = new Vector[]{put(0, 0.34, 0.05), put(-0.015, 0.32, 0.05), put(-0.015, 0.31, 0.09), put(0, 0.33, 0.09)};
		polygons[11] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.33, 0.09), put(-0.015, 0.31, 0.09), put(0, 0.29, 0.12)};
		polygons[12] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0.015, 0.31, 0.09), put(0, 0.33, 0.09), put(0, 0.29, 0.12)};
		polygons[13] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		iDirection.rotate_XZ(360/5);
		kDirection.rotate_XZ(360/5);
		
		v = new Vector[]{put(0.015, 0.3, 0.01), put(0, 0.3, 0), put(0, 0.34, 0.05), put(0.015, 0.32, 0.05)};
		polygons[14] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.3, 0), put(-0.015, 0.3, 0.01), put(-0.015, 0.32, 0.05), put(0, 0.34, 0.05)};
		polygons[15] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.34, 0.05), put(0, 0.33, 0.09), put(0.015, 0.31, 0.09), put(0.015, 0.32, 0.05)};
		polygons[16] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
	
		v = new Vector[]{put(0, 0.34, 0.05), put(-0.015, 0.32, 0.05), put(-0.015, 0.31, 0.09), put(0, 0.33, 0.09)};
		polygons[17] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.33, 0.09), put(-0.015, 0.31, 0.09), put(0, 0.29, 0.12)};
		polygons[18] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0.015, 0.31, 0.09), put(0, 0.33, 0.09), put(0, 0.29, 0.12)};
		polygons[19] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		iDirection.rotate_XZ(360/5);
		kDirection.rotate_XZ(360/5);
		
		v = new Vector[]{put(0.015, 0.3, 0.01), put(0, 0.3, 0), put(0, 0.34, 0.05), put(0.015, 0.32, 0.05)};
		polygons[20] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.3, 0), put(-0.015, 0.3, 0.01), put(-0.015, 0.32, 0.05), put(0, 0.34, 0.05)};
		polygons[21] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.34, 0.05), put(0, 0.33, 0.09), put(0.015, 0.31, 0.09), put(0.015, 0.32, 0.05)};
		polygons[22] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
	
		v = new Vector[]{put(0, 0.34, 0.05), put(-0.015, 0.32, 0.05), put(-0.015, 0.31, 0.09), put(0, 0.33, 0.09)};
		polygons[23] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.33, 0.09), put(-0.015, 0.31, 0.09), put(0, 0.29, 0.12)};
		polygons[24] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0.015, 0.31, 0.09), put(0, 0.33, 0.09), put(0, 0.29, 0.12)};
		polygons[25] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		iDirection.rotate_XZ(360/5);
		kDirection.rotate_XZ(360/5);
		
		v = new Vector[]{put(0.015, 0.3, 0.01), put(0, 0.3, 0), put(0, 0.34, 0.05), put(0.015, 0.32, 0.05)};
		polygons[26] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.3, 0), put(-0.015, 0.3, 0.01), put(-0.015, 0.32, 0.05), put(0, 0.34, 0.05)};
		polygons[27] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.34, 0.05), put(0, 0.33, 0.09), put(0.015, 0.31, 0.09), put(0.015, 0.32, 0.05)};
		polygons[28] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
	
		v = new Vector[]{put(0, 0.34, 0.05), put(-0.015, 0.32, 0.05), put(-0.015, 0.31, 0.09), put(0, 0.33, 0.09)};
		polygons[29] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.33, 0.09), put(-0.015, 0.31, 0.09), put(0, 0.29, 0.12)};
		polygons[30] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0.015, 0.31, 0.09), put(0, 0.33, 0.09), put(0, 0.29, 0.12)};
		polygons[31] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		iDirection.rotate_XZ(360/5);
		kDirection.rotate_XZ(360/5);
		
		v = new Vector[]{put(0.015, 0.3, 0.01), put(0, 0.3, 0), put(0, 0.34, 0.05), put(0.015, 0.32, 0.05)};
		polygons[32] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.3, 0), put(-0.015, 0.3, 0.01), put(-0.015, 0.32, 0.05), put(0, 0.34, 0.05)};
		polygons[33] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.34, 0.05), put(0, 0.33, 0.09), put(0.015, 0.31, 0.09), put(0.015, 0.32, 0.05)};
		polygons[34] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
	
		v = new Vector[]{put(0, 0.34, 0.05), put(-0.015, 0.32, 0.05), put(-0.015, 0.31, 0.09), put(0, 0.33, 0.09)};
		polygons[35] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0, 0.33, 0.09), put(-0.015, 0.31, 0.09), put(0, 0.29, 0.12)};
		polygons[36] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		v = new Vector[]{put(0.015, 0.31, 0.09), put(0, 0.33, 0.09), put(0, 0.29, 0.12)};
		polygons[37] = new Polygon3D(v, v[0], v[1], v [2], Main.textures[9], 1,1,6);
		
		iDirection = new Vector(0.4+0.3*0.4,0,0);
		jDirection = new Vector(0,0.5,0);
		kDirection = new Vector(0,0,0.7+0.3*0.7);
		
		startPointInWorld.add(0.03, 0, 0);
		
		v = new Vector[]{put(-0.5, 0, 0.4), put(0.4, 0, 0.4), put(0.4, 0, -0.5),put(-0.5, 0, -0.5)};
		shadow = new Polygon3D(v, v[0], v[1],v[3], Main.textures[10], 1,1,2);
		
		startPointInWorld.set(x,y,z);
		iDirection = new Vector(0.7+0.3*1.1,0,0);
		jDirection = new Vector(0,0.8+0.3,0);
		kDirection = new Vector(0,0,0.7+0.3*1.1);
		
		iDirection.rotate_XZ(angle);
		kDirection.rotate_XZ(angle);
	}
	
	//update the model 
	public void update(){
		
		cantreModelInCamera.set(centreModel);
		cantreModelInCamera.y = 0.25;
		cantreModelInCamera.subtract(Camera.absolutePosition);
		if(cantreModelInCamera.getLength() > 5.5){
			polygons = null;
			isVisible = false;
			return;
		}
		
		//find centre in camera coordinate
		cantreModelInCamera.set(centreModel);
		cantreModelInCamera.y = -1;
		cantreModelInCamera.subtract(Camera.cameraPosition);
		cantreModelInCamera.rotate_XZ(Camera.XZ_angle);
		cantreModelInCamera.rotate_YZ(Camera.YZ_angle);
		cantreModelInCamera.updateLocation();
		
		
		
		
		//test whether the model is visible by comparing the 2D position of its centre point with the screen area
		if(cantreModelInCamera.z < 0.9 || cantreModelInCamera.screenY < -10 || (cantreModelInCamera.screenX < -60 && cantreModelInCamera.z > 3) || (cantreModelInCamera.screenX >700 &&  cantreModelInCamera.z > 3)){
			
			isVisible = false;
			return;
		}
		isVisible = true;
		
		ModelDrawList.register(this);
		
		if(polygons == null){
			makePolygons();
			
		}
		
		//update polygons
		for(int i = 0; i < polygons.length; i++){
			polygons[i].update();
		}
		
		//Send the shadow polygon to shadow list which will get drawn before
		//terrain
		if(shadow != null){
			shadow.update();
			if(shadow.visible){
				Rasterizer.rasterize(shadow);
			}
		}
	}
	
	//draw model
	public void draw(){
		for(int i = 0; i < polygons.length; i++){
			polygons[i].draw();
		}
	}

}
