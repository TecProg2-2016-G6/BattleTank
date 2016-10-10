package src;

public class Explosion extends SolidObject {
	
	private double sizeOfExplosion = 0;
	
	private int spriteExplosionIndex = 0;
	
	private int currentFrameIndex = 0;
	
	private int currentAuraIndex = 0;
	
	// Default damage = 5;
	private int damage = 5;
	
	// Type of explosion 0 = normal   1 = plasma
	private int typeOfExplosion = 0;
	
	private int centreOfExplosionInTileMap = 0;
	
	// Enable this boolean if this explosion has be to drawn explicitly
	private boolean explicitDrawing = false;
	
	private Polygon3D explosionAura = null;
	
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
		centreOfExplosionInTileMap = (int)(xAxis*4) + (129-(int)(zAxys*4))*80;
		 
		if(random >= 75 ) {			
			spriteExplosionIndex = 17;
		}else if(random >= 50 ) {			
			spriteExplosionIndex = 18;
		}else if(random >= 25) {
			spriteExplosionIndex = 19;
		}else{
			spriteExplosionIndex = 20;
		}
		
		if(size > 1){
			spriteExplosionIndex = 18;
		}
		
		Vector[] v = new Vector[]{put(-0.3, 0, 0.3), put(0.3, 0, 0.3), put(0.3, 0, -0.3),put(-0.3, 0, -0.3)};
		
		if(size > 3){
			v = new Vector[]{put(-0.12, 0, 0.12), put(0.12, 0, 0.12), put(0.12, 0, -0.12),put(-0.12, 0, -0.12)};
		} else{
			// Do nothing
		}
		
		explosionAura = new Polygon3D(v, v[0], v[1], v[3], Main.textures[21], 1, 1, 2);
		
		this.sizeOfExplosion = size;
		
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
				explosionAura.myTexture.Texture = explosionAura.myTexture.lightMapData[currentAuraIndex];
				Rasterizer.rasterize(explosionAura);
			}
		}
		currentAuraIndex++;
		
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
			ObstacleMap.damageType2Obstacles(damage, boundaryModel2D, centreOfExplosionInTileMap);
		}
		
		lifeSpanObject--;
		
		if(lifeSpanObject == 0){
			lifeSpanObject = -1;
			return;
		}
	}
	
	//Draw  explosion scene
	public void drawExplosion() {
		
		//Calculate explosion size
		cantreModelInCamera.updateLocation();
		double ratio = sizeOfExplosion*2/cantreModelInCamera.z;
		
		
		//Drawing sprite	
		Rasterizer.temp = this.typeOfExplosion; 
		Rasterizer.renderExplosionSprite(Main.textures[spriteExplosionIndex].explosions[currentFrameIndex],ratio, cantreModelInCamera.screenX, cantreModelInCamera.screenY, 64, 64);
		
		currentFrameIndex++;
		
	}
	
	//Return the 2D boundary of this model
	public Rectangle2D getBoundary2D() {
		return boundaryModel2D;
	}
}
