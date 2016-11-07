package src;
//Class responsable for enemy Annihilator. 

public class Annihilator extends SolidObject{
	
	static final double height = 0.25;
	
	static final double lenght = 0.1;
	
	static final double weight = 0.1;
	
	static final double rectangleLenght = 0.23;
	
	static final double rectangleHeight = 0.23;
	
	static final double halfCircumference = 180;
	
	static final double circumference = 360;
	
	//Polygons for tank body.
	private Polygon3D[] body;
	
	//Total angle that the body has rotated from the initial position. (in the x-z plane).
	private int bodyAngle;
	
	//The centre of the body in camera coordinate.
	private Vector bodyCenter;
	
	//Polygons for tank turret.
	private Polygon3D[] turret;
	
	//The shadow of tank body.
	private Polygon3D shadowBody;
	
	//The shadow of tank turret.
	private Polygon3D shadowTurret;
	
	//The centre of the turret (pivot point for rotation).
	private Vector turretCenter;
	
	//Total angle that the turret has rotated from the initial position. (in the x-z plane).
	private int turretAngle;
	
	//Movement flag.
	private boolean forward, aimRight, aimLeft, firingShell, firingRocket;
	
	//Time needed before a weapon can be fired again.
	private int coolDownShell = 33;
	private int coolDownRocket = 33;
	
	//Change in tank's position of each frame.
	private Vector displacement = new Vector(0,0,0);
	
	//Degrees the tank body has rotated in a frame.
	private int bodyAngleDelta;
	
	//Degrees the tank turreet has rotated in a frame.
	private int turretAngleDelta;
	
	//The position index of the tank in the grid map.
	private int position, desiredPosition;
	
	//Whether the tank is visible in the previous frame.
	private boolean isVisiblePreviousFrame;
	
	//A smoke tail will be visible if the tank's health is dropped to half.
	private Smoke Smoke;
	
	//Distance from player tank.
	private double distance;
	
	//Angle between player tank and turret centre.
	private int targetAngle;
	
	//Angle between a target location and body centre.
	private int targetAngleBody;
	
	//TargetAngleBody of the previous frame.
	private int previousTargetAngleBody;

	//Temporary vectors which will be used for vector arithmetic.
	private Vector tempVector1 = new Vector(0,0,0);
	private Vector tempVector2 = new Vector(0,0,0);
	
	//A flag which indicate whether the take will interact with player at all. (i.e some enemy only get activtied at a certain stage of the game).
	public boolean active = true;
	
	//An AI flag  indicates whether it has engaged with player tank.
	private boolean engaged;
	
	//An AI flag indicates whether there is a type 2 obstacle between medium tank and player tank.
	private boolean clearToShoot;
	
	//A count down for death after hp = 0.
	private int countDownToDeath;
	
	//Represent the time that medium tank has been in stuck status.
	private int stuckCount;
	
	//Random numbers. 
	private int randomNumber1, randomNumber2;
	
	public Annihilator(double x, double y, double z, int angle) {
		
		//Define the center point of the tank.
		this.startPointInWorld = new Vector(x,y,z);
		this.iDirection = new Vector(1,0,0);
		this.jDirection = new Vector(0,1,0);
		this.kDirection = new Vector(0,0,1);
		
		//Boundary of this model has a cubic shape (ie l = w).
		this.modelType = 2;  
		makeBoundary(lenght, height, weight);
		
		//Create 2D rectangle boundary.
		this.boundaryModel2D = new Rectangle2D(x - 0.115, z + 0.115, rectangleLenght, rectangleHeight);
		this.position = (int)(x * 4) + (129 - (int)(z * 4)) * 80;
		this.desiredPosition = this.position;
		ObstacleMap.registerObstacle2(this, this.position);
		
		//Find centre of the model in world coordinate.
		findCentre();
		
		this.bodyCenter = this.centreModel;
		this.bodyAngle = angle;
		this.turretAngle = angle;
		
		makeBody();
		makeTurret();
		
		this.randomNumber1 = GameData.getRandomNumber();
		
		//Annihilator tank has 400 hit points.
		this.HP = 400;
		
		this.lifeSpanObject = 1;
	}
	
	//Create polygons for the tank body.
	private void makeBody(){
		Vector[] v;
		this.startPointInWorld = this.bodyCenter.myClone();
		
		this.iDirection = new Vector(0.95,0,0);
		this.jDirection = new Vector(0,1,0);
		this.kDirection = new Vector(0,0,1);
		
		this.iDirection.rotate_XZ(this.bodyAngle);
		this.kDirection.rotate_XZ(this.bodyAngle);
		
		this.body = new Polygon3D[19];
		
		v = new Vector[]{put(0.1, 0, 0.15), put(0.06, 0, 0.15), put(0.06, -0.04, 0.14), put(0.1, -0.04, 0.14)};
		this.body[0] = new Polygon3D(v,v[0], v[1],  v[3], Main.textures[12], 1,0.5,6);
		
		v = new Vector[]{put(-0.1, -0.04, 0.14), put(-0.06, -0.04, 0.14), put(-0.06, 0, 0.15), put(-0.1, 0, 0.15)};
		this.body[1] = new Polygon3D(v,v[0], v[1],  v[3], Main.textures[12], 1,0.5,6);
		
		v = new Vector[]{put(0.06, 0, -0.14), put(0.1, 0, -0.14), put(0.1, -0.04, -0.12), put(0.06, -0.04, -0.12)};
		this.body[2] = new Polygon3D(v,v[0], v[1],  v[3], Main.textures[12], 1,0.5,6);
		
		
		
		v = new Vector[]{ put(-0.06, -0.04, -0.12), put(-0.1, -0.04, -0.12), put(-0.1, 0, -0.14),put(-0.06, 0, -0.14)};
		this.body[3] = new Polygon3D(v,v[0], v[1],  v[3], Main.textures[12], 1,0.5,6);
		
		int i = 4;
		
		v = new Vector[]{put(0.06, 0.06, 0.13), put(0.06, 0.06, 0.08), put(0.06, -0.01, 0.08), put(0.06, -0.01, 0.15)};
		this.body[0 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,1.1,6);
		
		v = new Vector[]{put(-0.06, -0.01, 0.15), put(-0.06, -0.01, 0.08), put(-0.06, 0.06, 0.08), put(-0.06, 0.06, 0.13)};
		this.body[1 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,1.1,6);
		
		v = new Vector[]{put(-0.06, 0.06, 0.09), put(0.06, 0.06, 0.09), put(0.06, 0.06, -0.13), put(-0.06, 0.06, -0.13)};
		this.body[2 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,1.1,6);
		
		v = new Vector[]{put(0.06, 0.06, 0.09), put(-0.06, 0.06, 0.09), put(-0.06, 0, 0.15), put(0.06, 0, 0.15)};
		this.body[3 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,0.4,6);
		
		v = new Vector[]{put(-0.1, 0.06, -0.13), put(0.1, 0.06, -0.13), put(0.1, 0, -0.14),  put(-0.1, 0, -0.14)};
		this.body[4 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,0.3,6);
		
		v = new Vector[]{put(0.06, 0.06, 0.13), put(0.1, 0.06, 0.13), put(0.1, 0.06, -0.13), put(0.06, 0.06, -0.13)};
		this.body[5 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.3,0.8,6);
		
		v = new Vector[]{put(-0.06, 0.06, -0.13), put(-0.1, 0.06, -0.13), put(-0.1, 0.06, 0.13), put(-0.06, 0.06, 0.13)};
		this.body[6 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.3,0.8,6);
		
		v = new Vector[]{put(0.1, 0.06, 0.13), put(0.06, 0.06, 0.13), put(0.06, 0., 0.15), put(0.1, 0., 0.15)};
		this.body[7 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,1.1,6);
		
		v = new Vector[]{put(-0.1, 0., 0.15), put(-0.06, 0., 0.15), put(-0.06, 0.06, 0.13),put(-0.1, 0.06, 0.13)};
		this.body[8 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,1.1,6);
		
		v = new Vector[]{put(0.1, 0.06, -0.13), put(0.1, 0.06, 0.13), put(0.1, 0, 0.15), put(0.1, 0, -0.14)};
		this.body[9 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,0.2,6);
		
		v = new Vector[]{put(-0.1, 0, -0.14), put(-0.1, 0, 0.15), put(-0.1, 0.06, 0.13), put(-0.1, 0.06, -0.13)};
		this.body[10 + i] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[58], 0.8,0.2,6);
		
		v = new Vector[]{put(0.1, 0, 0.01), put(0.1, 0, 0.15), put(0.1, -0.04, 0.14), put(0.1, -0.04, 0.03)};
		this.body[11 + i] = new Polygon3D(v, put(0.1, 0.1, 0.03), put(0.1, 0.1, 0.13),  put(0.1, -0.04, 0.03), Main.textures[12], 1,0.5,6);
		
		v = new Vector[]{put(0.1, 0, -0.14), put(0.1, 0, -0.01), put(0.1, -0.04, -0.03), put(0.1, -0.04, -0.12)};
		this.body[12 +  i] = new Polygon3D(v, put(0.1, 0.1, -0.15), put(0.1, 0.1, -0.01),  put(0.1, -0.04, -0.15), Main.textures[12], 1,0.5,6);
		
		v = new Vector[]{put(-0.1, -0.04, 0.03), put(-0.1, -0.04, 0.14), put(-0.1, 0, 0.15), put(-0.1, 0, 0.01)};
		this.body[13 + i] = new Polygon3D(v, put(-0.1, 0.1, 0.03), put(-0.1, 0.1, 0.13),  put(-0.1, -0.04, 0.03), Main.textures[12], 1,0.5,6);
		
		v = new Vector[]{put(-0.1, -0.04, -0.12), put(-0.1, -0.04, -0.03), put(-0.1, 0, -0.01), put(-0.1, 0, -0.14)};
		this.body[14 + i] = new Polygon3D(v, put(-0.1, 0.1, -0.15), put(-0.1, 0.1, -0.01),  put(-0.1, -0.04, -0.15), Main.textures[12], 1,0.5,6);
		
		this.turretCenter = put(0, 0.07, -0);
		
		//Create shadow for tank body.
		this.startPointInWorld.add(-0.015, 0, -0.015);
		this.startPointInWorld.y = -1;
		v = new Vector[]{put(-0.3, 0, 0.3), put(0.3, 0, 0.3), put(0.3, 0, -0.3), put(-0.3, 0, -0.3)};
		this.shadowBody = new Polygon3D(v, v[0], v[1], v[3], Main.textures[14], 1, 1, 2);
		
		
	}
	
	//Create polygons for the tank turret.
	private void makeTurret() {
		this.startPointInWorld = this.turretCenter.myClone();
		Vector[] v;
		
		this.iDirection = new Vector(1.6,0,0);
		this.jDirection = new Vector(0,1.4,0);
		this.kDirection = new Vector(0,0,1.4);
		
		//Adjust orientation of the turret.
		this.iDirection.rotate_XZ(this.turretAngle);
		this.kDirection.rotate_XZ(this.turretAngle);
		
		this.turret = new Polygon3D[23];
		
		v = new Vector[]{put(0.04, 0.035, 0.06), put(-0.04, 0.035, 0.06), put(-0.04, 0, 0.06), put(0.04, 0, 0.06)};
		this.turret[0] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.6,0.3,6);
		
		v = new Vector[]{put(0.02, 0.025, 0.18), put(0.026, 0.015, 0.18), put(0.028, 0.015, 0.06), put(0.02, 0.025, 0.06)};
		this.turret[1] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[60], 0.1,1,6);
		
		v = new Vector[]{ put(0.02, 0.025, 0.06), put(-0.008 + 0.02, 0.015, 0.06), put(-0.006 + 0.02, 0.015, 0.18),put(0.02, 0.025, 0.18)};
		this.turret[2] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[60], 0.1,1,6);
		
		v = new Vector[]{put(-0.02, 0.025, 0.18), put(0.006 - 0.02, 0.015, 0.18), put(0.008-0.02, 0.015, 0.06), put(-0.02, 0.025, 0.06)};
		this.turret[3] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[60], 0.1,1,6);
		
		v = new Vector[]{ put(-0.02, 0.025, 0.06), put(-0.028, 0.015, 0.06), put(-0.026, 0.015, 0.18),put(-0.02, 0.025, 0.18)};
		this.turret[4] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[60], 0.1,1,6);
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(0.04, 0.035, 0.06), put(0.05, 0.035, 0.04), put(0.05, 0.035, -0.03), put(0.03, 0.035, -0.07),  put(-0.03, 0.035, -0.07),put(-0.05, 0.035, -0.03), put(-0.05, 0.035, 0.04)};
		this.turret[5] = new Polygon3D(v, put(-0.04, 0.035, 0.19), put(0.04, 0.035, 0.19), put(-0.04, 0.035, 0.09), Main.textures[59], 0.6,0.6,6);
		
		v = new Vector[]{put(0.03, 0, -0.07), put(-0.03, 0, -0.07),  put(-0.03, 0.035, -0.07),   put(0.03, 0.035, -0.07)};
		this.turret[6] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.4,0.2,6);
		
		v = new Vector[]{put(0.03, 0.035, -0.07), put(0.05, 0.035, -0.03), put(0.05, 0, -0.03), put(0.03, 0, -0.07)};
		this.turret[7] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.4,0.2,6);
		
		v = new Vector[]{put(-0.03, 0, -0.07), put(-0.05, 0, -0.03), put(-0.05, 0.035, -0.03), put(-0.03, 0.035, -0.07)};
		this.turret[8] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.4,0.2,6);
		
		v = new Vector[]{put(0.05, 0.035, -0.03), put(0.05, 0.035, 0.04), put(0.05, 0, 0.04), put(0.05, 0, -0.03)};
		this.turret[9] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.5,0.3,6);
		
		v = new Vector[]{put(-0.05, 0, -0.03), put(-0.05, 0, 0.04), put(-0.05, 0.035, 0.04), put(-0.05, 0.035, -0.03)};
		this.turret[10] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.5,0.3,6);
		
		v = new Vector[]{put(0.05, 0.035, 0.04), put(0.04, 0.035, 0.06), put(0.04, 0, 0.06), put(0.05, 0, 0.04)};
		this.turret[11] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.3,0.3,6);
		
		v = new Vector[]{put(-0.05, 0, 0.04), put(-0.04, 0, 0.06), put(-0.04, 0.035, 0.06), put(-0.05, 0.035, 0.04)};
		this.turret[12] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[59], 0.3,0.3,6);
		
		v = new Vector[]{put(-0.075, 0.05, 0.02), put(-0.05, 0.05, 0.02), put(-0.05, 0.05, -0.04), put(-0.075, 0.05, -0.04)};
		this.turret[13] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
		
		v = new Vector[]{put(-0.075, 0.05, 0.02), put(-0.075, 0.05, -0.04), put(-0.075, 0.02, -0.04), put(-0.075, 0.02, 0.02)};
		this.turret[14] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
		
		v = new Vector[]{put(-0.075, 0.05, -0.04), put(-0.05, 0.05, -0.04), put(-0.05, 0.02, -0.04), put(-0.075, 0.02, -0.04)};
		this.turret[15] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
		
		v = new Vector[]{put(-0.05, 0.05, -0.04), put(-0.05, 0.05, 0.02), put(-0.05, 0.035, 0.02),put(-0.05, 0.035, -0.04)};
		this.turret[16] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
		
		int r = 150 / 8;
		int g = 150 / 8;
		int b = 150 / 8;
		short color = (short)((int)r << 10 | (int)g << 5 | (int)b);
		
		v = new Vector[]{put(-0.075, 0.02, 0.02), put(-0.05, 0.02, 0.02), put(-0.05, 0.05, 0.02), put(-0.075, 0.05, 0.02)};
		this.turret[17] = new Polygon3D(v, v[0], v[1], v [3],null, 0.5,0.5,7);
		this.turret[17].color = color;
		
		v = new Vector[]{put(0.075, 0.05, -0.04), put(0.05, 0.05, -0.04), put(0.05, 0.05, 0.02), put(0.075, 0.05, 0.02)};
		this.turret[18] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
		
		v = new Vector[]{put(0.075, 0.02, 0.02), put(0.075, 0.02, -0.04), put(0.075, 0.05, -0.04), put(0.075, 0.05, 0.02)};
		this.turret[19] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
		
		v = new Vector[]{put(0.075, 0.02, -0.04), put(0.05, 0.02, -0.04), put(0.05, 0.05, -0.04), put(0.075, 0.05, -0.04)};
		this.turret[20] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
		
		v = new Vector[]{put(0.05, 0.035, -0.04), put(0.05, 0.035, 0.02), put(0.05, 0.05, 0.02),put(0.05, 0.05, -0.04)};
		this.turret[21] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[35], 0.5,0.5,6);
			
		v = new Vector[]{put(0.075, 0.05, 0.02), put(0.05, 0.05, 0.02), put(0.05, 0.02, 0.02), put(0.075, 0.02, 0.02)};
		this.turret[22] = new Polygon3D(v, v[0], v[1], v [3],null, 0.5,0.5,7);
		this.turret[22].color = color;
		
		//Create shadow for tank turret.
		this.startPointInWorld.add(-0.03, 0, -0.04);
		this.startPointInWorld.y = -1;
		v = new Vector[]{put(-0.18, 0, 0.18), put(0.18, 0, 0.18), put(0.18, 0, -0.18), put(-0.18, 0, -0.18)};
		this.shadowTurret = new Polygon3D(v, v[0], v[1], v[3], Main.textures[61], 1, 1, 2);
				
	}
	
	
	public void update() {
		
		//Retrieve a random number every 333 game frame.
		if((Main.timer + this.randomNumber1 * 3) % 1000 == 0) {
			if(this.randomNumber2 > 50){
				this.randomNumber2 = 50;
			}else{
				this.randomNumber2 = 51;
			}
		}
		
		//Process AI.
		if(this.countDownToDeath <= 0 && this.active && !Main.gamePaused) {
			processAI();
		} 
		//Perform actions.
		if(this.aimLeft) {
			if(Math.abs(this.turretAngle - this.targetAngle) <= 3) {
				this.turretAngleDelta = this.targetAngle - this.turretAngle;
				this.turretAngle += this.turretAngleDelta;
				if(this.turretAngleDelta < 0){
					this.turretAngleDelta += circumference;
				}
			}else {
				this.turretAngleDelta = 3;
				this.turretAngle += 3;
			}
			if(this.turretAngle >= circumference) {
				this.turretAngle -= circumference;
			}
		}else if(this.aimRight) {
			if(Math.abs(this.turretAngle - this.targetAngle) <= 3 ) {
				this.turretAngleDelta = this.targetAngle - this.turretAngle;
				this.turretAngle += this.turretAngleDelta;
				if(this.turretAngleDelta < 0) {
					this.turretAngleDelta += circumference;
				}
			}else {
				this.turretAngleDelta = 357;
				this.turretAngle -= 3;
			}
			if(this.turretAngle < 0) {
				this.turretAngle += circumference;
			}
		}
		
		if(this.forward) {
			//Move forward.
			int delta = this.targetAngleBody - this.bodyAngle;
			if(Math.abs(delta) < 5 || Math.abs(delta) > 355) {
				this.bodyAngle = this.targetAngleBody;
				this.bodyAngleDelta = (int) ((delta + 720) % circumference);
				this.displacement.set(0,0,0.01);
				this.displacement.rotate_XZ(this.bodyAngle);
			}else {
				this.displacement.set(0,0,0);
				if(delta > 0) {
					if(delta < halfCircumference) {
						this.bodyAngleDelta = 5;
					}else {
						this.bodyAngleDelta = 355;
					}
				}	
				if(delta < 0) {
					if(delta > -halfCircumference) {
						this.bodyAngleDelta = 355;
					}else{
						this.bodyAngleDelta = 5;
					}
				}
				
				this.bodyAngle = (int) ((this.bodyAngle+this.bodyAngleDelta) % circumference);
			}
		}
		
		
		//Update centre.
		this.centreModel.add(this.displacement);
		
		//Update bundary2D.
		this.boundaryModel2D.update(this.displacement);
		
		/**
		 * Update location in the 2d tile map
		 * Validating movement is already done in  process AI part.
		 */
		int newPosition = (int)(this.boundaryModel2D.xPos * 4) + (129 - (int)(this.boundaryModel2D.yPos * 4)) * 80;
		
		if(!ObstacleMap.isOccupied(newPosition)) {
			
			ObstacleMap.removeObstacle2(this.position);
			ObstacleMap.registerObstacle2(this, newPosition);
			this.position = newPosition;
			this.desiredPosition = newPosition;
			
		}else if(!ObstacleMap.isOccupied(this.desiredPosition)) {
			
			ObstacleMap.removeObstacle2(this.position);
			ObstacleMap.registerObstacle2(this, this.desiredPosition);
			this.position = this.desiredPosition;
			
		}else {
			this.desiredPosition = newPosition;
		}
		
		//Find centre in camera coordinate.
		this.cantreModelInCamera.set(this.centreModel);
		this.cantreModelInCamera.y = -1;
		this.cantreModelInCamera.subtract(Camera.cameraPosition);
		this.cantreModelInCamera.rotate_XZ(Camera.XZ_angle);
		this.cantreModelInCamera.rotate_YZ(Camera.YZ_angle);
		this.cantreModelInCamera.updateLocation();
		
		//Test whether the model is visible by comparing the 2D position of its centre point with the screen.
		this.isVisible = true;
		
		if(this.cantreModelInCamera.z < 0.9 || this.cantreModelInCamera.screenY < -10 || this.cantreModelInCamera.screenX < -400 || this.cantreModelInCamera.screenX > 800) {
			this.isVisible = false;
			this.isVisiblePreviousFrame = false;
		}
		
		//If tank is not visible in the previous frame, its need to be reconstructed.
		if(this.isVisible) {
			if(this.isVisiblePreviousFrame == false) {
				//Recreate body and turret polygons.
				makeBody();
				makeTurret();
				this.isVisiblePreviousFrame = true;
			}
		}
		
		//If visible then update the geometry to camera coordinate.
		if(this.isVisible) {
			ModelDrawList.register(this);
			
			if(this.countDownToDeath < 3) {
			
				//Update body polygons.
				for(int i = 0; i < this.body.length; i++) {
					
					//Perform vertex updates in world coordinate.
					this.body[i].origin.add(this.displacement);
					this.body[i].origin.subtract(this.centreModel);
					this.body[i].origin.rotate_XZ(this.bodyAngleDelta);
					this.body[i].origin.add(this.centreModel);
					
					this.body[i].bottomEnd.add(this.displacement);
					this.body[i].bottomEnd.subtract(this.centreModel);
					this.body[i].bottomEnd.rotate_XZ(this.bodyAngleDelta);
					this.body[i].bottomEnd.add(this.centreModel);
					
					this.body[i].rightEnd.add(this.displacement);
					this.body[i].rightEnd.subtract(this.centreModel);
					this.body[i].rightEnd.rotate_XZ(this.bodyAngleDelta);
					this.body[i].rightEnd.add(this.centreModel);
					
					for(int j = 0; j < this.body[i].vertex3D.length; j++){
						this.body[i].vertex3D[j].add(this.displacement);
						this.body[i].vertex3D[j].subtract(this.centreModel);
						this.body[i].vertex3D[j].rotate_XZ(this.bodyAngleDelta);
						this.body[i].vertex3D[j].add(this.centreModel);
					}
					
					this.body[i].findRealNormal();
					this.body[i].findDiffuse();
					
					//Transform the polygon into camera coordinate.
					this.body[i].update();
				}
				
				//Update shadow for tank body.
				this.tempVector1.set(this.centreModel);
				this.tempVector1.add(-0.03, 0, -0.04);
				this.shadowBody.origin.add(this.displacement);
				this.shadowBody.origin.subtract(this.tempVector1);
				this.shadowBody.origin.rotate_XZ(this.bodyAngleDelta);
				this.shadowBody.origin.add(this.tempVector1);
				
				this.shadowBody.bottomEnd.add(this.displacement);
				this.shadowBody.bottomEnd.subtract(this.tempVector1);
				this.shadowBody.bottomEnd.rotate_XZ(this.bodyAngleDelta);
				this.shadowBody.bottomEnd.add(this.tempVector1);
				
				this.shadowBody.rightEnd.add(this.displacement);
				this.shadowBody.rightEnd.subtract(this.tempVector1);
				this.shadowBody.rightEnd.rotate_XZ(this.bodyAngleDelta);
				this.shadowBody.rightEnd.add(this.tempVector1);
				
				for(int j = 0; j < this.shadowBody.vertex3D.length; j++) {
					this.shadowBody.vertex3D[j].add(this.displacement);
					this.shadowBody.vertex3D[j].subtract(this.tempVector1);
					this.shadowBody.vertex3D[j].rotate_XZ(this.bodyAngleDelta);
					this.shadowBody.vertex3D[j].add(this.tempVector1);
				}
				
				this.shadowBody.update();
				Rasterizer.rasterize(this.shadowBody);
			
				//Update turret center.
				this.turretCenter.add(this.displacement);
				
				//Update turret polygons.
				for(int i = 0; i < this.turret.length; i++) {
					
					//perform vertex updates in world coordinate
					this.turret[i].origin.add(this.displacement);
					this.turret[i].origin.subtract(this.turretCenter);
					this.turret[i].origin.rotate_XZ(this.turretAngleDelta);
					this.turret[i].origin.add(this.turretCenter);
					
					this.turret[i].bottomEnd.add(this.displacement);
					this.turret[i].bottomEnd.subtract(this.turretCenter);
					this.turret[i].bottomEnd.rotate_XZ(this.turretAngleDelta);
					this.turret[i].bottomEnd.add(this.turretCenter);
					
					this.turret[i].rightEnd.add(this.displacement);
					this.turret[i].rightEnd.subtract(this.turretCenter);
					this.turret[i].rightEnd.rotate_XZ(this.turretAngleDelta);
					this.turret[i].rightEnd.add(this.turretCenter);
					
					for(int j = 0; j < this.turret[i].vertex3D.length; j++) {
						this.turret[i].vertex3D[j].add(this.displacement);
						this.turret[i].vertex3D[j].subtract(this.turretCenter);
						this.turret[i].vertex3D[j].rotate_XZ(this.turretAngleDelta);
						this.turret[i].vertex3D[j].add(this.turretCenter);
					}
					
					this.turret[i].findRealNormal();
					this.turret[i].findDiffuse();
					
					//Transform the polygon into camera coordinate.
					this.turret[i].update();
				}
				
				//Update shadow for tank turret.
				this.tempVector1.set(this.turretCenter);
				this.tempVector1.add(-0.03, 0, -0.04);
				
				this.shadowTurret.origin.add(this.displacement);
				this.shadowTurret.origin.subtract(this.tempVector1);
				this.shadowTurret.origin.rotate_XZ(this.turretAngleDelta);
				this.shadowTurret.origin.add(this.tempVector1);
				
				this.shadowTurret.bottomEnd.add(this.displacement);
				this.shadowTurret.bottomEnd.subtract(this.tempVector1);
				this.shadowTurret.bottomEnd.rotate_XZ(this.turretAngleDelta);
				this.shadowTurret.bottomEnd.add(this.tempVector1);
				
				this.shadowTurret.rightEnd.add(this.displacement);
				this.shadowTurret.rightEnd.subtract(this.tempVector1);
				this.shadowTurret.rightEnd.rotate_XZ(this.turretAngleDelta);
				this.shadowTurret.rightEnd.add(this.tempVector1);
				
				for(int j = 0; j < this.shadowTurret.vertex3D.length; j++) {
					this.shadowTurret.vertex3D[j].add(this.displacement);
					this.shadowTurret.vertex3D[j].subtract(this.tempVector1);
					this.shadowTurret.vertex3D[j].rotate_XZ(this.turretAngleDelta);
					this.shadowTurret.vertex3D[j].add(this.tempVector1);
				}
				this.shadowTurret.update();
				Rasterizer.rasterize(this.shadowTurret);
			}
		}
		
		//Handle attack event.
		if(this.coolDownShell > 0 && this.coolDownShell != 92 && !Main.gamePaused) {
			this.coolDownShell--;
		}
		if(this.coolDownRocket > 0 && this.coolDownRocket != 90 && !Main.gamePaused) {
			this.coolDownRocket--;
		}
		if(this.firingShell) {
			if(this.coolDownShell == 0) {
				this.coolDownShell = 100;
				//Calculate laser direction.
				Vector tempVector1 = new Vector(0,0,1);
				tempVector1.rotate_XZ((int) ((this.turretAngle + 270) % circumference));
				tempVector1.scale(0.035);
				Vector direction = new Vector(0,0,1);
				direction.rotate_XZ(this.turretAngle);
				direction.scale(0.1);
				direction.add(this.turretCenter);
				direction.add(tempVector1);
				Projectiles.register(new Shell(direction.x, direction.y,direction.z, this.turretAngle, true, 1));
				
			}
			
			if(this.coolDownShell == 92) {
				this.coolDownShell = 25;
				//Calculate shell direction.
				Vector tempVector1 = new Vector(0,0,1);
				tempVector1.rotate_XZ((int) ((this.turretAngle + 270) % circumference));
				tempVector1.scale(-0.035);
				Vector direction = new Vector(0,0,1);
				direction.rotate_XZ(this.turretAngle);
				direction.scale(0.1);
				direction.add(this.turretCenter);
				direction.add(tempVector1);
				Projectiles.register(new Shell(direction.x, direction.y,direction.z, this.turretAngle, true, 1));
			}
		}
		
		if(this.firingRocket) {
			
			if(this.coolDownRocket == 0) {
				this.coolDownRocket = 100;
				//calculate laser direction
				Vector tempVector1 = new Vector(0,0,1);
				tempVector1.rotate_XZ((int) ((this.turretAngle + 270) % circumference));
				tempVector1.scale(0.095);
				Vector direction = new Vector(0,0,1);
				direction.rotate_XZ(this.turretAngle);
				direction.scale(0.05);
				direction.add(this.turretCenter);
				direction.add(tempVector1);
				
				Rocket r = new Rocket(direction.x, direction.y,direction.z, this.turretAngle ,true);
				Projectiles.register(r);
			}
			
			if(this.coolDownRocket == 90) {
				this.coolDownRocket = 45;
				//Calculate shell direction.
				Vector tempVector1 = new Vector(0,0,1);
				tempVector1.rotate_XZ((int) ((this.turretAngle + 270) % circumference));
				tempVector1.scale(-0.095);
				Vector direction = new Vector(0,0,1);
				direction.rotate_XZ(this.turretAngle);
				direction.scale(0.05);
				direction.add(this.turretCenter);
				direction.add(tempVector1);
				Rocket r = new Rocket(direction.x, direction.y,direction.z, this.turretAngle ,true);
				Projectiles.register(r);
			}
		}
		
		if(this.HP <= 200) {
			if(this.Smoke == null) {
				this.Smoke = new Smoke(this);
			}else {
				if(this.isVisible)
					this.Smoke.update();
			}
		}
		
		if(this.HP <= 0) {
			this.countDownToDeath++;
			if(this.countDownToDeath >= 3) {
				if(this.countDownToDeath == 3) {
					Projectiles.register(new Explosion(this.centreModel.x, this.centreModel.y, this.centreModel.z, 2));
				
				}
				ObstacleMap.removeObstacle2(this.position);
				this.Smoke.stopped = true;
			}
			if(this.countDownToDeath >= 40){
				this.lifeSpanObject = 0;
			}
		}
	
		//Reset action flag.
		this.forward = false;
		this.aimRight = false;
		this.aimLeft = false;
		this.bodyAngleDelta = 0;
		this.turretAngleDelta = 0;	
		this.displacement.reset();
		this.firingRocket = false;
		this.firingShell = false;
		if(Main.timer % 10 == 0)
			this.isBlockingOtherModel = false;
	
	}
	
	//Process AI.
	private void processAI() {
		//Calculate distance from player's tank.
		this.tempVector1.set(this.centreModel);
		this.tempVector1.subtract(PlayerTank.bodyCenter);
		this.distance = this.tempVector1.getLength();
		
		//Medium tank become aware of player's tank when the distance is less than 2.
		if(this.distance < 2){
			this.engaged = true;
		}
		//Medium tank will stop chasing the player when the distance is greater than 4.
		if(this.distance > 6){
			this.engaged = false;
			
			//Rotate the turret to the same angle as the body.
			this.targetAngle = this.bodyAngle;
			int AngleDelta = this.turretAngle - this.targetAngle;
			if(AngleDelta > 0){
				if(AngleDelta < halfCircumference)
					this.aimRight = true;
				else
					this.aimLeft = true;
			}
			else if(AngleDelta < 0){
				if(AngleDelta > -halfCircumference)
					this.aimLeft = true;
				else 
					this.aimRight = true;
			}
			return;
		}
		
		if(this.engaged) {
			//If medium tank is engaged with player, it will send alert to nearby tanks.
			if((Main.timer) % 5 == 0 )
				ObstacleMap.alertNearbyTanks(this.position);
			
			/**
			 * Test whether there is a type obstacle 2 between medium tank and player tank
			 * Firing a vision ray from medium tank toward player tank.
			 */
			this.tempVector1.set(this.bodyCenter);
			this.tempVector2.set(PlayerTank.bodyCenter);
			this.tempVector2.subtract(this.tempVector1);
			this.tempVector2.unit();
			this.tempVector2.scale(0.125);
			
			
			
			this.clearToShoot = true;
			int obstacleType = -1; 
			double d = 0;
			for(int i = 0; (d < this.distance) && (i < 30); i++, this.tempVector1.add(this.tempVector2), d += 0.125) {
				Model temp = ObstacleMap.isOccupied2(this.tempVector1);
				if(temp == null){
					continue;
				}
				obstacleType = temp.getTypeOfModel();
				if(obstacleType == 1) {
					break;
				}else {
					this.clearToShoot = false;
					break;
				}
				
			}
			
			
			
			//Find the angle between target and itself.
			if(this.clearToShoot) {
				this.targetAngle = 90 + (int)(halfCircumference * Math.atan((this.centreModel.z - PlayerTank.bodyCenter.z) / (this.centreModel.x - PlayerTank.bodyCenter.x)) / Math.PI);
				if(PlayerTank.bodyCenter.x > this.turretCenter.x  && this.targetAngle <= halfCircumference) {
					this.targetAngle += halfCircumference;
				}

			}else {
				this.targetAngle = this.bodyAngle;
				
			}
			
			//Cauculate the difference between those 2 angles.
			int AngleDelta = this.turretAngle - this.targetAngle;
			if(Math.abs(AngleDelta) < 3 && this.clearToShoot && this.distance < 1.7) {
				this.firingShell = true;
			}
			
			if(Math.abs(AngleDelta) < 3 && this.clearToShoot && this.distance < 3) {
				this.firingRocket = true;
			}
			
		
			
			
			//Aim at a target angle.
			if(AngleDelta > 0) {
				if(AngleDelta < halfCircumference) {
					this.aimRight = true;
				}else {
					this.aimLeft = true;
				}
			}
			else if(AngleDelta < 0) {
				if(AngleDelta > -halfCircumference) {
					this.aimLeft = true;
				}else { 
					this.aimRight = true;
				}
			}
			
			/**
			 * Move to a  target location 
			 * medium tank will move towards player tank's position until distance is less than 1.4, or it detects  
			 * A type 2 obstacle between itself and the player's tank.
			 */
			
			this.forward = true;
			if(this.clearToShoot && this.distance < 1.5) {
				if(this.distance < 1.4) {
					this.forward = false;
				}
				if(this.distance >= 1.4) {
					if(this.randomNumber2 > 50) {
						this.forward = false;
					}
				}
			}
			
			if(this.isBlockingOtherModel && this.distance > 0.8) {
				this.forward = true;
				ObstacleMap.giveWay(this, this.position);
				
			}
			
			if(this.forward) {
				this.targetAngleBody = 90 + (int)(halfCircumference * Math.atan((this.centreModel.z - PlayerTank.bodyCenter.z) / (this.centreModel.x - PlayerTank.bodyCenter.x)) / Math.PI);
				if(PlayerTank.bodyCenter.x > this.centreModel.x  && this.targetAngleBody <= halfCircumference) {
					this.targetAngleBody += halfCircumference;
				}
				
				/**
				 * The enemy tank will occasionly (~once every 10 secs)perfom a 90 degree change in moving angle if:
				 * 1. it cant see the target tank and the target is within 1.2 unit away.
				 * 2. it got stucked and the target is within 1.2 units away.
				 * 3. blocked by a wall and the target is within 3 units away.
				 */
				
				if(!this.clearToShoot && (this.distance < 1.2 || (obstacleType == 6 && this.distance < 2.5)) || this.stuckCount == 10){
					if(this.stuckCount == 10) {
						if(this.randomNumber2 > 50)
							this.randomNumber2 = 50;
						else
							this.randomNumber2 = 51;
						this.stuckCount = 0;
					}
						
					if(this.randomNumber2 > 50)
						this.targetAngleBody += 90;
					else
						this.targetAngleBody -= 90;
					
					
					this.targetAngleBody = (int) ((this.targetAngleBody + circumference) % circumference);
				}
				
				
				int newPosition = (int)(this.boundaryModel2D.xPos*4) + (129 - (int)(this.boundaryModel2D.yPos * 4)) * 80;
				
					
				//Check whether the next move will embed into obstacles.
				this.displacement.set(0,0,0.01);
				this.displacement.rotate_XZ(this.targetAngleBody);
				this.boundaryModel2D.update(this.displacement);
				
				boolean canMove = true;
				//Test againt type 1 & 2 obstacles.
				if(ObstacleMap.collideWithObstacle1(this, newPosition)){
					this.forward = false;
					canMove = false;
				}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
					this.forward = false;
					canMove = false;
				}
				this.displacement.scale(-1);
				this.boundaryModel2D.update(this.displacement);
				this.displacement.reset();
				
				
				if(!canMove){
					if(this.isBlockingOtherModel){
						ObstacleMap.giveWay(this ,this.position);
					}
					
					
					//Change direction if unable to move with current direction.
					this.targetAngleBody = this.targetAngle;
					//Generate 2 new directions.
					int angle1 = this.targetAngleBody - this.targetAngleBody%90;
					int angle2 = angle1 + 90;
					
					
					angle2 = (int) (angle2 % circumference);
				
					
					
					boolean canMoveAngle1 = true;
					boolean canMoveAngle2 = true;
				
				
					
					//Check if tank is able to move freely at angle 1.
					this.displacement.set(0,0,0.01);
					this.displacement.rotate_XZ(angle1);
					this.boundaryModel2D.update(this.displacement);
					newPosition = (int)(this.boundaryModel2D.xPos * 4) + (129 - (int)(this.boundaryModel2D.yPos * 4)) * 80;
					//Test againt type 1 & 2 obstacles.
					if(ObstacleMap.collideWithObstacle1(this, newPosition)){
						canMoveAngle1 = false;
					}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
						canMoveAngle1 = false;
					}
					this.displacement.scale(-1);
					this.boundaryModel2D.update(this.displacement);
					this.displacement.reset();
					
					//Check if tank is able to move freely at angle 2.
					this.displacement.set(0,0,0.01);
					this.displacement.rotate_XZ(angle2);
					this.boundaryModel2D.update(this.displacement);
					newPosition = (int)(this.boundaryModel2D.xPos * 4) + (129 - (int)(this.boundaryModel2D.yPos * 4)) * 80;
					//Test againt type 1 & 2 obstacles.
					if(ObstacleMap.collideWithObstacle1(this, newPosition)){
						canMoveAngle2 = false;
					}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
						canMoveAngle2 = false;
					}
					this.displacement.scale(-1);
					this.boundaryModel2D.update(this.displacement);
					this.displacement.reset();
					
				
					
					/**
					 * Move to a valid direction. 
					 * If both directions are valid,  then move to the direction that is closer to  targetAngleBody.
					 * If neither direction is valid, then update the AI status to "stucked"(which does nothing at this moment).
					 */
					
					if(canMoveAngle1 && !canMoveAngle2){
						this.targetAngleBody = angle1;
						this.forward = true;
						
						ObstacleMap.giveWay(this, this.position);
					}else if(!canMoveAngle1 && canMoveAngle2){
						this.targetAngleBody = angle2;
						this.forward = true;
						
						ObstacleMap.giveWay(this, this.position);
					}else if(canMoveAngle1 && canMoveAngle2){
						if(Math.abs(angle1 - this.targetAngleBody) < Math.abs(angle2 - this.targetAngleBody)){
							this.targetAngleBody = angle1;
							
						}else{
							this.targetAngleBody = angle2;
							
						}		
						this.forward = true;
						
						
						
					}else{
						
						//Tank get stucked.
						this.stuckCount = 10;
						
					
						
						//Tell surrounding units to move away.
						ObstacleMap.giveWay(this, this.position);
						
						
						
					}
					
					if(Math.abs((this.previousTargetAngleBody + halfCircumference) % circumference - this.targetAngleBody) <= 50){
						this.targetAngleBody = this.previousTargetAngleBody;
					}
					
					
				}
				//Double check whether the move is valid.
				this.displacement.set(0,0,0.01);
				this.displacement.rotate_XZ(this.targetAngleBody);
				this.boundaryModel2D.update(this.displacement);
				newPosition = (int)(this.boundaryModel2D.xPos * 4) + (129 - (int)(this.boundaryModel2D.yPos * 4)) * 80;
				
				//Test againt type 1 & 2 obstacles.
				if(ObstacleMap.collideWithObstacle1(this, newPosition)){
					this.forward = false;
					
				}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
					this.forward = false;
					
				}
				this.displacement.scale(-1);
				this.boundaryModel2D.update(this.displacement);
				this.displacement.reset();
			}
		}
		this.previousTargetAngleBody = this.targetAngleBody;
	}
	
	public void drawExplosion(){
		if(this.countDownToDeath < 3){
			//Draw body.
			for(int i = 0; i < this.body.length; i++){
				this.body[i].draw();
				
			}
			
			//Draw turret.
			for(int i = 0; i < this.turret.length; i++){
				this.turret[i].draw();
			}
		}
		
		

		//Draw smoke tail.
		if(this.Smoke != null && this.isVisible)
			this.Smoke.draw();
	}
	
	public void damage(int damagePoint){
		if(damagePoint == -1){
			this.active = true;
			this.engaged = true;
			return;
		}
		this.HP -= damagePoint;
		this.engaged = true;
	}
	
	//Return the 2D boundary of this model.
	public Rectangle2D getBoundary2D(){
		return this.boundaryModel2D;
	}

}
