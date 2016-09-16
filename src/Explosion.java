package src;

public class Explosion extends SolidObject {
	
	// Size of the explosion 
	double size = 0;
	
	// Which explosion sprite to use
	public int spriteIndex = 0;
	
	// Current frame Index;
	public int frameIndex = 0;
	
	// Current aura index;
	public int auraIndex = 0;
	
	// Default damage = 5;
	public int damage = 5;
	
	// Type of explosion 0 = normal   1 = plasma
	public int type;
	
	// Centre of explosion in tile map
	public int groundZero;
	
	// Enable this boolean if this explosion has be to drawn explicitly
	public boolean explicitDrawing;
	
	public Polygon3D explosionAura;
	
	public Explosion(double xAxis, double yAxys, double zAxys, double size) {
		
		int random = GameData.getRandom();
		
		startPointInWorld = new Vector(xAxis,yAxys,zAxys);
		iDirection = new Vector(1,0,0);
		jDirection = new Vector(0,1,0);
		kDirection = new Vector(0,0,1);
		lifeSpanObject = 16;
		
		//Create 2D boundary
		boundaryModel2D = new Rectangle2D(xAxis - 0.1, zAxys + 0.1, 0.2, 0.2);
		
		//Find centre of explosion
		groundZero = (int)(xAxis*4) + (129-(int)(zAxys*4))*80;
		 
		if(random >= 75 ) {			
			spriteIndex = 17;
		}else if(random >= 50 ) {			
			spriteIndex = 18;
		}else if(random >= 25) {
			spriteIndex = 19;
		}else{
			spriteIndex = 20;
		}
		
		if(size > 1){
			spriteIndex = 18;
		}
		
		Vector[] v = new Vector[]{put(-0.3, 0, 0.3), put(0.3, 0, 0.3), put(0.3, 0, -0.3),put(-0.3, 0, -0.3)};
		
		if(size > 3){
			v = new Vector[]{put(-0.12, 0, 0.12), put(0.12, 0, 0.12), put(0.12, 0, -0.12),put(-0.12, 0, -0.12)};
		}
		
		explosionAura = new Polygon3D(v, v[0], v[1], v[3], Main.textures[21], 1, 1, 2);
		
		this.size = size;
		
		//Boundary of this model has a cubic shape (ie length ~= width ~= height)		
		makeBoundary(0.001, 0.001, 0.001);
		
		//Find centre of the model in world coordinate
		findCentre();
		
		
	}
	
	//Animate explosion scene
	public void update() {
		//Make always visible
		isVisible = true;
		
		if(explosionAura != null && damage != 0) {
			explosionAura.update();
			
			if(explosionAura.visible) {
				explosionAura.myTexture.Texture = explosionAura.myTexture.lightMapData[auraIndex];
				Rasterizer.rasterize(explosionAura);
			}
		}
		auraIndex++;
		
		//Send to draw list
		if(!explicitDrawing)
			ModelDrawList.register(this);
		
		//Find centre in camera coordinate
		
		cantreModelInCamera.set(centreModel);
		cantreModelInCamera.subtract(Camera.cameraPosition);
		cantreModelInCamera.rotate_XZ(Camera.XZ_angle);
		cantreModelInCamera.rotate_YZ(Camera.YZ_angle);
		
		
		//Damage nearby units
		if(lifeSpanObject == 15 && damage != 0) {
			ObstacleMap.damageType2Obstacles(damage, boundaryModel2D, groundZero);
		}
		
		lifeSpanObject--;
		
		if(lifeSpanObject == 0){
			lifeSpanObject = -1;
			return;
		}
	}
	
	//Draw  explosion scene
	public void draw() {
		
		//Calculate explosion size
		cantreModelInCamera.updateLocation();
		double ratio = size*2/cantreModelInCamera.z;
		
		
		//Drawing sprite	
		Rasterizer.temp = this.type; 
		Rasterizer.renderExplosionSprite(Main.textures[spriteIndex].explosions[frameIndex],ratio, cantreModelInCamera.screenX, cantreModelInCamera.screenY, 64, 64);
		
		frameIndex++;
		
	}
	
	//Return the 2D boundary of this model
	public Rectangle2D getBoundary2D() {
		return boundaryModel2D;
	}
}
