package src;

public class Explosion extends SolidObject {
	
	private double sizeOfExplosion = 0;
	
	private int spriteExplosionIndex = 0;
	
	private int currentFrameIndex = 0;
	
	private int currentAuraIndex = 0;
	
	private int centreOfExplosionInTileMap = 0;
	
	private Polygon3D explosionAura = null;
	
	// Type of explosion 0 = normal   1 = plasma
	protected int typeOfExplosion = 0;
	
	// Default damage = 5;
	protected int damage = 5;
	
	// Enable this boolean if this explosion has be to drawn explicitly
	protected boolean explicitDrawing = false;
	
	public Explosion(double xAxis, double yAxys, double zAxys, double size) {
		
		int random = GameData.getRandomNumber();
		
		this.startPointInWorld = new Vector(xAxis,yAxys,zAxys);
		this.iDirection = new Vector(1,0,0);
		this.jDirection = new Vector(0,1,0);
		this.kDirection = new Vector(0,0,1);
		this.lifeSpanObject = 16;
		
		//Create 2D boundary
		this.boundaryModel2D = new Rectangle2D(xAxis - 0.1, zAxys + 0.1, 0.2, 0.2);
		
		//Find centre of explosion
		this.centreOfExplosionInTileMap = (int)(xAxis*4) + (129-(int)(zAxys*4))*80;
		 
		if(random >= 75 ) {			
			this.spriteExplosionIndex = 17;
		}else if(random >= 50 ) {			
			this.spriteExplosionIndex = 18;
		}else if(random >= 25) {
			this.spriteExplosionIndex = 19;
		}else{
			this.spriteExplosionIndex = 20;
		}
		
		if(size > 1){
			this.spriteExplosionIndex = 18;
		}
		
		Vector[] v = new Vector[]{put(-0.3, 0, 0.3), put(0.3, 0, 0.3), put(0.3, 0, -0.3),put(-0.3, 0, -0.3)};
		
		if(size > 3){
			v = new Vector[]{put(-0.12, 0, 0.12), put(0.12, 0, 0.12), put(0.12, 0, -0.12),put(-0.12, 0, -0.12)};
		} else{
			// Do nothing
		}
		
		this.explosionAura = new Polygon3D(v, v[0], v[1], v[3], Main.textures[21], 1, 1, 2);
		
		this.sizeOfExplosion = size;
		
		//Boundary of this model has a cubic shape (ie length ~= width ~= height)		
		makeBoundary(0.001, 0.001, 0.001);
		
		//Find centre of the model in world coordinate
		findCentre();
		
		
	}
	
	//Animate explosion scene
	@Override
	public void update() {
		//Make always visible
		this.isVisible = true;
		
		if(this.explosionAura != null && this.damage != 0) {
			this.explosionAura.update();
			
			if(this.explosionAura.isVisible) {
				this.explosionAura.myTexture.Texture = this.explosionAura.myTexture.lightMapData[this.currentAuraIndex];
				Rasterizer.rasterize(this.explosionAura);
			}
		}
		this.currentAuraIndex++;
		
		//Send to draw list
		if(!this.explicitDrawing)
			ModelDrawList.register(this);
		
		//Find centre in camera coordinate
		
		this.cantreModelInCamera.set(this.centreModel);
		this.cantreModelInCamera.subtract(Camera.cameraPosition);
		this.cantreModelInCamera.rotate_XZ(Camera.XZ_angle);
		this.cantreModelInCamera.rotate_YZ(Camera.YZ_angle);
		
		
		//Damage nearby units
		if(this.lifeSpanObject == 15 && this.damage != 0) {
			ObstacleMap.damageType2Obstacles(this.damage, this.boundaryModel2D, this.centreOfExplosionInTileMap);
		}
		
		this.lifeSpanObject--;
		
		if(this.lifeSpanObject == 0){
			this.lifeSpanObject = -1;
			return;
		}
	}
	
	//Draw  explosion scene
	@Override
	public void drawExplosion() {
		
		//Calculate explosion size
		this.cantreModelInCamera.updateLocation();
		double ratio = this.sizeOfExplosion*2/this.cantreModelInCamera.z;
		
		
		//Drawing sprite	
		Rasterizer.temp = this.typeOfExplosion; 
		Rasterizer.renderExplosionSprite(Main.textures[this.spriteExplosionIndex].explosions[this.currentFrameIndex],ratio, this.cantreModelInCamera.screenX, this.cantreModelInCamera.screenY, 64, 64);
		
		this.currentFrameIndex++;
		
	}
	
	//Return the 2D boundary of this model
	@Override
	public Rectangle2D getBoundary2D() {
		return this.boundaryModel2D;
	}
	
	
}
