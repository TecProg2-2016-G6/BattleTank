package src;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.directory.InvalidAttributeValueException;

//The tank that controlled by user
public class PlayerTank extends SolidObject{
	
	private static final Logger LOGGER = Logger.getLogger( PlayerTank.class.getName() );
	
	//polygons that made up the tank body
	private Polygon3D[] body;
	
	//total angle that the body has rotated from the initial position. (in the x-z plane)
	private int bodyAngle;
	
	//the centre of the body in camera coordinate
	public static Vector bodyCenter;
	
	//polygons that made up the tank turret
	private Polygon3D[] turret;
	
	//the shadow of tank body
	private Polygon3D shadowBody;
	
	//the shadow of tank turret
	private Polygon3D shadowTurret;
	
	//the centre of the turret (pivot point for rotation)
	public static Vector turretCenter;
	
	//total angle that the turret has rotated from the initial position. (in the x-z plane)
	public static int turretAngle = 0;
	
	//the angle that nuke cannon has rotated along its Z axis
	private int nukeCannonAngle;
	
	//the angular frequency of the nuke cannon rotation
	private int angularSpeed;
	
	//polygons of nuke Cannon
	private Polygon3D[] nukeCannonRear, nukeCannonMiddle, nukeCannonFront; 
	
	//movement flag
	public static boolean forward, backward, turnRight, turnLeft, moveRight, moveLeft, firing;
	
	//weapon flag
	//1 = cannon turret, 2 = rocket launcher turret, 3 = railgun turret 4 = nuke cannon turret
	//defult is cannon turret
	public int currentWeapon = 1; 
	
	//time needed before a weapon can be fired again
	public int[] coolDowns = new int[]{0, 16, 30, 40, 5}; 
	//plan
	// tonight make up terrains and create pototype power plant
	
	//current coolDown
	public int currentCoolDown = 0;
	
	//ammunition counts
	public static int shells = 50;
	public static int rockets = 0;
	public static int slugs = 0;
	public static int plasma = 0;
	public static int currentAmmo;
	
	//an indication of ammunition status
	public boolean outOfAmmo;
	
	//change in tank's position of each frame
	public static Vector displacement = new Vector(0,0,0);
	
	//degrees the tank body has rotated in a frame
	private int bodyAngleDelta;
	
	//the speed of tank;
	public static double speed = 0;
	
	//degrees the tank turret has rotated in a frame
	private int turretAngleDelta;
	
	//The position index of the tank in the tile map
	int position;
	
	//temporary vectors which will be used for vector arithmetic
	private Vector tempVector1 = new Vector(0,0,0);
	private Vector tempVector2 = new Vector(0,0,0);
	
	//time that the  player is underattack
	private int underAttackCount;
	
	public PlayerTank(double x, double y, double z){		
		
		//define the center point of this model(also the centre point of tank body)
		this.startPointInWorld = new Vector(x,y,z);
		this.iDirection = new Vector(1,0,0);
		this.jDirection = new Vector(0,0.95,0);
		this.kDirection = new Vector(0,0,1.05);
		
		this.bodyAngle = 0;
		
		//boundary of this model has a cubic shape (ie l = w)
		this.modelType = 1;  
		makeBoundary(0.1, 0.25, 0.1);
		
		//create 2D boundary
		this.boundaryModel2D = new Rectangle2D(x - 0.07, z + 0.07, 0.14, 0.14);
		this.position = (int)(x*4) + (129-(int)(z*4))*80;
		ObstacleMap.registerObstacle2(this, this.position);
		
		
		
		//find centre of the model in world coordinate
		findCentre();
		
		bodyCenter = this.centreModel;
	
		makeBody();
		makeTurretCannon();
		
		
		//player Tank has a max of 100 hit points
		this.HP = 150;
	}
	
	//return the 2D boundary of this model
	@Override
	public Rectangle2D getBoundary2D(){
		return this.boundaryModel2D;
	}
	
	//create polygons for the tank body
	public void makeBody(){
		Vector[] v;
		
		this.body = new Polygon3D[11];
		
		v = new Vector[]{put(-0.07, 0.025, 0.11), put(-0.07, 0.025, -0.11), put(-0.07, 0.005, -0.11), put(-0.07, -0.025, -0.08), put(-0.07, -0.025, 0.07), put(-0.07, 0.005, 0.11)};
		this.body[0] = new Polygon3D(v, put(-0.07, 0.027, 0.11), put(-0.07, 0.027, -0.11), put(-0.07, -0.025, 0.11), Main.textures[12], 1,1,6);
		
		v = new Vector[]{put(0.07, 0.005, 0.11), put(0.07, -0.025, 0.07), put(0.07, -0.025, -0.08), put(0.07, 0.005, -0.11), put(0.07, 0.025, -0.11), put(0.07, 0.025, 0.11)};
		this.body[1] = new Polygon3D(v, put(0.07, 0.027, -0.11),put(0.07, 0.027, 0.11), put(0.07, -0.025, -0.11), Main.textures[12], 1,1,6);
		
		v = new Vector[]{put(-0.06, 0.055, 0.05), put(0.06, 0.055, 0.05), put(0.06, 0.055, -0.1), put(-0.06, 0.055, -0.1)};
		this.body[2] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[11], 1,0.9,6);
		
		v = new Vector[]{put(-0.07, 0.04, 0.11), put(0.07, 0.04, 0.11), put(0.06, 0.055, 0.05), put(-0.06, 0.055, 0.05)};
		this.body[3] = new Polygon3D(v, v[2], v[3], v [1], Main.textures[11], 1,0.3,6);
	
		v = new Vector[]{put(-0.06, 0.055, 0.05),put(-0.06, 0.055, -0.1), put(-0.07, 0.04, -0.11), put(-0.07, 0.04, 0.11)};
		this.body[4] = new Polygon3D(v, v[2], v[3], v [1], Main.textures[11], 1,0.3,6);
	
		v = new Vector[]{put(0.07, 0.04, 0.11), put(0.07, 0.04, -0.11), put(0.06, 0.055, -0.1),put(0.06, 0.055, 0.05)};
		this.body[5] = new Polygon3D(v, v[2], v[3], v [1], Main.textures[11], 1,0.3,6);
		
		v = new Vector[]{put(-0.06, 0.055, -0.1), put(0.06, 0.055, -0.1), put(0.07, 0.04, -0.11), put(-0.07, 0.04, -0.11)};
		this.body[6] = new Polygon3D(v, v[2], v[3], v [1], Main.textures[11], 1,0.3,6);
		
		v = new Vector[]{put(0.07, 0.04, 0.11), put(-0.07, 0.04, 0.11), put(-0.07, 0.01, 0.11), put(0.07, 0.01, 0.11)};
		this.body[7] = new Polygon3D(v, v[2], v[3], v [1], Main.textures[11], 1,0.3,6);
	
		v = new Vector[]{put(-0.07, 0.04, 0.11), put(-0.07, 0.04, -0.11), put(-0.07, 0.015, -0.11), put(-0.07, 0.005, -0.09), put(-0.07, 0.005, 0.09),put(-0.07, 0.015, 0.11)};
		this.body[8] = new Polygon3D(v, put(-0.07, 0.04, 0.11), put(-0.07, 0.04, -0.11), put(-0.07, 0.025, 0.11), Main.textures[11], 1,0.3,6);
	
		v = new Vector[]{put(0.07, 0.015, 0.11), put(0.07, 0.005, 0.09), put(0.07, 0.005, -0.09), put(0.07, 0.015, -0.11), put(0.07, 0.04, -0.11),put(0.07, 0.04, 0.11)};
		this.body[9] = new Polygon3D(v, put(0.07, 0.04, 0.11), put(0.07, 0.04, -0.11), put(0.07, 0.025, 0.11), Main.textures[11], 1,0.3,6);
	
		v = new Vector[]{put(-0.07, 0.04, -0.11), put(0.07, 0.04, -0.11), put(0.07, 0.015, -0.11), put(-0.07, 0.015, -0.11)};
		this.body[10] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[11], 1,0.3,6);
		
		turretCenter = put(0, 0.055, -0.02);
		
		//create shadow for tank body
		this.startPointInWorld.add(-0.025, 0, -0.02);
		this.startPointInWorld.y = -1;
		v = new Vector[]{put(-0.2, 0, 0.2), put(0.2, 0, 0.2), put(0.2, 0, -0.2), put(-0.2, 0, -0.2)};
		this.shadowBody = new Polygon3D(v, v[0], v[1], v[3], Main.textures[14], 1, 1, 2);
	}
	
	//create polygons for the tank turret (cannon turret)
	public void makeTurretCannon(){
		this.startPointInWorld = turretCenter.myClone();
		Vector[] v;
		this.turret = new Polygon3D[10];
		
		this.iDirection.set(1,0,0);
		this.jDirection.set(0,0.95,0);
		this.kDirection.set(0,0,1.05);
		
		//adjust orientation of the turret
		this.iDirection.rotate_XZ(turretAngle);
		this.kDirection.rotate_XZ(turretAngle);
		
	
		v = new Vector[]{put(0, 0.025, 0.18), put(0.004, 0.022, 0.18), put(0.007, 0.022, 0.06), put(0, 0.025, 0.06)};
		this.turret[0] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.1,1,6);
		
		v = new Vector[]{ put(0, 0.025, 0.06), put(-0.007, 0.022, 0.06), put(-0.004, 0.022, 0.18),put(0, 0.025, 0.18)};
		this.turret[1] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.1,1,6);
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(0.04, 0.035, 0.06), put(0.05, 0.035, 0.04), put(0.05, 0.035, -0.03), put(0.03, 0.035, -0.07),  put(-0.03, 0.035, -0.07),put(-0.05, 0.035, -0.03), put(-0.05, 0.035, 0.04)};
		this.turret[2] = new Polygon3D(v, put(-0.04, 0.035, 0.19), put(0.04, 0.035, 0.19), put(-0.04, 0.035, 0.09), Main.textures[13], 0.8,0.8,6);
		
		v = new Vector[]{put(0.03, 0, -0.07), put(-0.03, 0, -0.07),  put(-0.03, 0.035, -0.07),   put(0.03, 0.035, -0.07)};
		this.turret[3] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(0.03, 0.035, -0.07), put(0.05, 0.035, -0.03), put(0.05, 0, -0.03), put(0.03, 0, -0.07)};
		this.turret[4] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(-0.03, 0, -0.07), put(-0.05, 0, -0.03), put(-0.05, 0.035, -0.03), put(-0.03, 0.035, -0.07)};
		this.turret[5] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v  = new Vector[]{put(-0.05, 0.035, 0.04), put(-0.05, 0.035, -0.03), put(-0.05, 0, -0.03), put(-0.05, 0, 0.04)};
		this.turret[6] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v  = new Vector[]{put(0.05, 0, 0.04), put(0.05, 0, -0.03), put(0.05, 0.035, -0.03), put(0.05, 0.035, 0.04)};
		this.turret[7] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(-0.05, 0.035, 0.04), put(-0.05, 0, 0.04), put(-0.04, 0, 0.06)};
		this.turret[8] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		v = new Vector[]{put(0.04, 0, 0.06), put(0.05, 0, 0.04), put(0.05, 0.035, 0.04), put(0.04, 0.035, 0.06)};
		this.turret[9] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		//create shadow for tank turret (cannon)
		this.startPointInWorld.add(-0.04, 0, -0.04);
		this.startPointInWorld.y = -1;
		v = new Vector[]{put(-0.2, 0, 0.2), put(0.2, 0, 0.2), put(0.2, 0, -0.2), put(-0.2, 0, -0.2)};
		this.shadowTurret = new Polygon3D(v, v[0], v[1], v[3], Main.textures[15], 1, 1, 2);
	}
	
	//create polygons for the tank turret (rocket turret)
	public void makeTurretRocket(){
		this.startPointInWorld = turretCenter.myClone();
		Vector[] v;
		this.turret = new Polygon3D[35];
		
		this.iDirection.set(1,0,0);
		this.jDirection.set(0,0.95,0);
		this.kDirection.set(0,0,1.05);
		
		//adjust orientation of the turret
		this.iDirection.rotate_XZ(turretAngle);
		this.kDirection.rotate_XZ(turretAngle);
		
		
		v  = new Vector[]{put(-0.05, 0.035, 0.04), put(-0.05, 0.035, -0.03), put(-0.05, 0, -0.03), put(-0.05, 0, 0.04)};
		this.turret[0] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v  = new Vector[]{put(0.05, 0, 0.04), put(0.05, 0, -0.03), put(0.05, 0.035, -0.03), put(0.05, 0.035, 0.04)};
		this.turret[1] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(-0.05, 0.035, 0.04), put(-0.05, 0, 0.04), put(-0.04, 0, 0.06)};
		this.turret[2] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		v = new Vector[]{put(0.04, 0, 0.06), put(0.05, 0, 0.04), put(0.05, 0.035, 0.04), put(0.04, 0.035, 0.06)};
		this.turret[3] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		
		double r = 0.015;
		double theta = Math.PI/8;
		this.tempVector1.set(this.startPointInWorld);
		this.startPointInWorld = put(0.068,0,0);
		
		for(int i = 0; i < 16; i++){
			v = new Vector[]{put(r*Math.cos(i*theta), r*Math.sin(i*theta), -0.03),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), -0.03),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), 0.03),
							 put(r*Math.cos(i*theta), r*Math.sin(i*theta), 0.03)
							};
			this.turret[i + 4] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[25], 0.1,0.1,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(0.068,0,0.01);
		
		for(int i = 0; i < 8; i++){
			v = new Vector[]{put(r*Math.cos(i*theta), r*Math.sin(i*theta), -0.004),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), -0.004),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), 0.004),
							 put(r*Math.cos(i*theta), r*Math.sin(i*theta), 0.004)
							};
			this.turret[i + 20] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[26], 0.1,0.1,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(0.068,0,0);
		
		v = new Vector[16];
		for(int i = 1; i < 17; i++)
			v[16 - i] = put(r*Math.cos(i*theta), r*Math.sin(i*theta), -0.03);
		this.turret[28] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[25], 1,1,6);
		
		
		
		this.startPointInWorld.set(this.tempVector1);
		v = new Vector[]{put(0, 0.025, 0.18), put(0.004, 0.022, 0.18), put(0.007, 0.022, 0.06), put(0, 0.025, 0.06)};
		this.turret[29] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.1,1,6);
		
		v = new Vector[]{ put(0, 0.025, 0.06), put(-0.007, 0.022, 0.06), put(-0.004, 0.022, 0.18),put(0, 0.025, 0.18)};
		this.turret[30] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.1,1,6);
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(0.04, 0.035, 0.06), put(0.05, 0.035, 0.04), put(0.05, 0.035, -0.03), put(0.03, 0.035, -0.07),  put(-0.03, 0.035, -0.07),put(-0.05, 0.035, -0.03), put(-0.05, 0.035, 0.04)};
		this.turret[31] = new Polygon3D(v, put(-0.04, 0.035, 0.19), put(0.04, 0.035, 0.19), put(-0.04, 0.035, 0.09), Main.textures[13], 0.8,0.8,6);
		
		v = new Vector[]{put(0.03, 0, -0.07), put(-0.03, 0, -0.07),  put(-0.03, 0.035, -0.07),   put(0.03, 0.035, -0.07)};
		this.turret[32] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(0.03, 0.035, -0.07), put(0.05, 0.035, -0.03), put(0.05, 0, -0.03), put(0.03, 0, -0.07)};
		this.turret[33] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(-0.03, 0, -0.07), put(-0.05, 0, -0.03), put(-0.05, 0.035, -0.03), put(-0.03, 0.035, -0.07)};
		this.turret[34] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		//create shadow for tank turret (cannon)
		this.startPointInWorld.add(-0.04, 0, -0.04);
		this.startPointInWorld.y = -1;
		v = new Vector[]{put(-0.2, 0, 0.2), put(0.2, 0, 0.2), put(0.2, 0, -0.2), put(-0.2, 0, -0.2)};
		this.shadowTurret = new Polygon3D(v, v[0], v[1], v[3], Main.textures[15], 1, 1, 2);
	}
	
	//create polygons for the tank turret (railgun turret)
	public void makeTurretRailgun(){
		this.startPointInWorld = turretCenter.myClone();
		Vector[] v;
		this.turret = new Polygon3D[72];
		
		this.iDirection.set(1,0,0);
		this.jDirection.set(0,0.95,0);
		this.kDirection.set(0,0,1.05);
		
		//adjust orientation of the turret
		this.iDirection.rotate_XZ(turretAngle);
		this.kDirection.rotate_XZ(turretAngle);
		
		double r = 0.025;
		double theta = Math.PI/16;
		this.tempVector1.set(this.startPointInWorld);
		this.startPointInWorld = put(0,0,0.12);
		
		for(int i = 0; i < 32; i++){
			v = new Vector[]{put(r*Math.cos(i*theta), r*Math.sin(i*theta), -0.06),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), -0.06),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), 0.06),
							 put(r*Math.cos(i*theta), r*Math.sin(i*theta), 0.06)
							};
			this.turret[i] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		for(int i = 32; i < 64; i++){
			v = new Vector[]{put(r*Math.cos(i*theta), r*Math.sin(i*theta), 0.05),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), 0.05),
							 put(r*Math.cos((i+1)*theta), r*Math.sin((i+1)*theta), 0.06),
							 put(r*Math.cos(i*theta), r*Math.sin(i*theta), 0.06)
							};
			this.turret[i] = new Polygon3D(v, v[0], v[1], v[3],  Main.textures[23], 0.01,0.01,6);
		}
		
		
	
		
		
		
		
		this.startPointInWorld.set(this.tempVector1);
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(0.04, 0.035, 0.06), put(0.05, 0.035, 0.04), put(0.05, 0.035, -0.03), put(0.03, 0.035, -0.07),  put(-0.03, 0.035, -0.07),put(-0.05, 0.035, -0.03), put(-0.05, 0.035, 0.04)};
		this.turret[64] = new Polygon3D(v, put(-0.04, 0.035, 0.19), put(0.04, 0.035, 0.19), put(-0.04, 0.035, 0.09), Main.textures[13], 0.8,0.8,6);
		
		v = new Vector[]{put(0.03, 0, -0.07), put(-0.03, 0, -0.07),  put(-0.03, 0.035, -0.07),   put(0.03, 0.035, -0.07)};
		this.turret[65] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(0.03, 0.035, -0.07), put(0.05, 0.035, -0.03), put(0.05, 0, -0.03), put(0.03, 0, -0.07)};
		this.turret[66] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(-0.03, 0, -0.07), put(-0.05, 0, -0.03), put(-0.05, 0.035, -0.03), put(-0.03, 0.035, -0.07)};
		this.turret[67] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v  = new Vector[]{put(-0.05, 0.035, 0.04), put(-0.05, 0.035, -0.03), put(-0.05, 0, -0.03), put(-0.05, 0, 0.04)};
		this.turret[68] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v  = new Vector[]{put(0.05, 0, 0.04), put(0.05, 0, -0.03), put(0.05, 0.035, -0.03), put(0.05, 0.035, 0.04)};
		this.turret[69] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(-0.05, 0.035, 0.04), put(-0.05, 0, 0.04), put(-0.04, 0, 0.06)};
		this.turret[70] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		v = new Vector[]{put(0.04, 0, 0.06), put(0.05, 0, 0.04), put(0.05, 0.035, 0.04), put(0.04, 0.035, 0.06)};
		this.turret[71] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		//create shadow for tank turret (railgun)
		this.startPointInWorld.add(-0.04, 0, -0.04);
		this.startPointInWorld.y = -1;
		v = new Vector[]{put(-0.5, 0, 0.2), put(0.5, 0, 0.2), put(0.5, 0, -0.2), put(-0.5, 0, -0.2)};
		this.shadowTurret = new Polygon3D(v, v[0], v[1], v[3], Main.textures[15], 1, 1, 2);
	}
	
	public void makeTurretNuke(){
		this.startPointInWorld = turretCenter.myClone();
		Vector[] v;
		this.turret = new Polygon3D[171];
		int polyIndex = 0;
		
		
		//turret gun
		this.iDirection.set(1,0,0);
		this.jDirection.set(0,1,0);
		this.kDirection.set(0,0,1);
		
		if(!firing){
			this.angularSpeed--;
			if(this.angularSpeed < 0)
				this.angularSpeed = 0;
		}
		
		//adjust orientation of the turret
		this.nukeCannonAngle+=this.angularSpeed;
		this.nukeCannonAngle%=360;
		this.iDirection.rotate_XY(this.nukeCannonAngle);
		this.jDirection.rotate_XY(this.nukeCannonAngle);
		
		//adjust orientation of the turret
		int angle = (turretAngle - this.turretAngleDelta + 360)%360;
		this.iDirection.rotate_XZ(angle);
		this.kDirection.rotate_XZ(angle);
		this.jDirection.rotate_XZ(angle);
		
		
		//turet gun
		double r = 0.012;
		int theta = 20;
		
		this.tempVector1.set(this.startPointInWorld);
		
		this.startPointInWorld = put(0.017,0.017,0.1);
		this.nukeCannonRear = new Polygon3D[72];
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.06),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.06)
							};
			this.nukeCannonRear[i] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(-0.017,0.017,0.1);
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.06),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.06)
							};
			this.nukeCannonRear[i + 18] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(-0.017,-0.017,0.1);
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.06),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.06)
							};
			this.nukeCannonRear[i + 36] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(0.017,-0.017,0.1);
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], -0.04),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.06),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.06)
							};
			this.nukeCannonRear[i + 54] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		r = 0.045;
		this.nukeCannonMiddle = new Polygon3D[19];
		
		v = new Vector[18];
		for(int i = 1; i < 19; i++)
			v[18 - i] = put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.16);
		this.nukeCannonMiddle[0] = new Polygon3D(v, put(-0.2, 0.2, 0), put(0.2, 0.2, 0), put(-0.2, -0.2, 0),  Main.textures[13], 0.001,0.01,6);
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.16),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.16),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.167),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.167)
							};
			this.nukeCannonMiddle[i + 1] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		
		
		r = 0.012;
		this.tempVector1.set(this.startPointInWorld);
		this.startPointInWorld = put(0.017,0.017,0.1);
		this.nukeCannonFront = new Polygon3D[72];
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta],0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.1),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.1)
							};
			this.nukeCannonFront[i] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(-0.017,0.017,0.1);
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.1),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.1)
							};
			this.nukeCannonFront[i + 18] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(-0.017,-0.017,0.1);
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.1),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.1)
							};
			this.nukeCannonFront[i + 36] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		this.startPointInWorld.set(this.tempVector1);
		this.startPointInWorld = put(0.017,-0.017,0.1);
		for(int i = 0; i < 18; i++){
			v = new Vector[]{put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.07),
							 put(r*GameData.cos[(i+1)*theta], r*GameData.sin[(i+1)*theta], 0.1),
							 put(r*GameData.cos[i*theta], r*GameData.sin[i*theta], 0.1)
							};
			this.nukeCannonFront[i + 54] = new Polygon3D(v, v[0], v[1], v [3],  Main.textures[13], 0.001,0.01,6);
		}
		
		
		
		for(int i = 0; i < 72; i ++){
			this.turret[i] = this.nukeCannonRear[i];
		}
		
		for(int i = 0; i < 19; i ++){
			this.turret[72 + i] = this.nukeCannonMiddle[i];
		}
		
		for(int i = 0; i < 72; i ++){
			this.turret[i + 91] = this.nukeCannonFront[i];
		}
		
		
		
		
		polyIndex = 163;
		
		//turret body
		this.startPointInWorld = turretCenter.myClone();
		
		this.iDirection.set(1,0,0);
		this.jDirection.set(0,0.95,0);
		this.kDirection.set(0,0,1.05);
		
		//adjust orientation of the turret
		this.iDirection.rotate_XZ(angle);
		this.kDirection.rotate_XZ(angle);
		
		
		
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(0.04, 0.035, 0.06), put(0.05, 0.035, 0.04), put(0.05, 0.035, -0.03), put(0.03, 0.035, -0.07),  put(-0.03, 0.035, -0.07),put(-0.05, 0.035, -0.03), put(-0.05, 0.035, 0.04)};
		this.turret[0 + polyIndex] = new Polygon3D(v, put(-0.04, 0.035, 0.19), put(0.04, 0.035, 0.19), put(-0.04, 0.035, 0.09), Main.textures[13], 0.8,0.8,6);
		
		v = new Vector[]{put(0.03, 0, -0.07), put(-0.03, 0, -0.07),  put(-0.03, 0.035, -0.07),   put(0.03, 0.035, -0.07)};
		this.turret[1 + polyIndex] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(0.03, 0.035, -0.07), put(0.05, 0.035, -0.03), put(0.05, 0, -0.03), put(0.03, 0, -0.07)};
		this.turret[2 + polyIndex] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v = new Vector[]{put(-0.03, 0, -0.07), put(-0.05, 0, -0.03), put(-0.05, 0.035, -0.03), put(-0.03, 0.035, -0.07)};
		this.turret[3 + polyIndex] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.6,0.3,6);
		
		v  = new Vector[]{put(-0.05, 0.035, 0.04), put(-0.05, 0.035, -0.03), put(-0.05, 0, -0.03), put(-0.05, 0, 0.04)};
		this.turret[4 + polyIndex] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v  = new Vector[]{put(0.05, 0, 0.04), put(0.05, 0, -0.03), put(0.05, 0.035, -0.03), put(0.05, 0.035, 0.04)};
		this.turret[5 + polyIndex] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.5,0.2,6);
		
		v = new Vector[]{put(-0.04, 0.035, 0.06), put(-0.05, 0.035, 0.04), put(-0.05, 0, 0.04), put(-0.04, 0, 0.06)};
		this.turret[6 + polyIndex] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		v = new Vector[]{put(0.04, 0, 0.06), put(0.05, 0, 0.04), put(0.05, 0.035, 0.04), put(0.04, 0.035, 0.06)};
		this.turret[7 + polyIndex] = new Polygon3D(v, v[0], v[1], v [3], Main.textures[13], 0.2,0.2,6);
		
		this.startPointInWorld.add(-0.05, 0, -0.02);
		this.startPointInWorld.y = -1;
		v = new Vector[]{put(-0.7, 0, 0.2), put(0.7, 0, 0.2), put(0.7, 0, -0.2), put(-0.7, 0, -0.2)};
		this.shadowTurret = new Polygon3D(v, v[0], v[1], v[3], Main.textures[15], 1, 1, 2);
	}
	
	//update model 
	@Override
	public void update(){
		//don't update player tank when game is finished
		if(Main.gameOver)
			return;
		
		
		if(this.underAttackCount > 0)
			this.underAttackCount--;
		
		//slowly regenerate hp
		if(this.HP < 150 && this.HP > 0 && this.underAttackCount == 0 && !Main.gamePaused){
			if(Main.timer%2 == 0)
				this.HP+=1;
		}
		
	
		
		//System.out.println(position);
		
		//the player tank is always rendered visible		
		this.isVisible = true;
		ModelDrawList.register(this);
		
		//Process user's commands:
		//Rotate tank turret to right/left
		if(turnLeft && turnRight){
			this.turretAngleDelta = 0;
		}else if(turnLeft){
			this.turretAngleDelta=4;
			turretAngle+=4;
			if(turretAngle >= 360)
				turretAngle-=360;
		}else if(turnRight){
			this.turretAngleDelta=356;
			turretAngle-=4;
			if(turretAngle < 0)
				turretAngle+=360;
		}else{
			this.turretAngleDelta = 0;
		}
		
		//Move forward and backward.
		//prior to move, the tank body must rotate to the same angle as the tank turret.
		//Calculate the angle difference between body and turret
		if(forward && backward){
			//if forward and backward keys pressed at the same time, nothing happens
			speed = 0;
			displacement.set(0,0,0);
		}else if(forward){
			//move forward
			
				if(speed < 0.015)
					speed+=0.001;

		}else if(backward){
			//move back
			
			if(speed > -0.01)
				speed-=0.001;
			
		}else{
			if(speed > 0 || speed < 0){
				if(speed > 0)
					speed -=0.002;
				if(speed < 0)
					speed+=0.002;
				if(speed > -0.002 && speed < 0.002)
					speed = 0;
			}
		}
		this.tempVector1.set(0,0,1);
		this.tempVector1.rotate_XZ(this.bodyAngle);
		displacement.set(this.tempVector1.x*speed,0, this.tempVector1.z*speed);
		
		if(moveLeft && moveRight){
			//if move left and move right keys pressed at the same time, nothing happens
			speed = 0;
			displacement.set(0,0,0);
		}else if(moveLeft){
			//move left
			
			this.bodyAngleDelta = 5;
			this.bodyAngle = (this.bodyAngle+this.bodyAngleDelta)%360;
			
		}else if(moveRight){
			//move right
			this.bodyAngleDelta = 355;
			this.bodyAngle = (this.bodyAngle+this.bodyAngleDelta)%360;
			
		}
		
		
		//update centre
		this.centreModel.add(displacement);
		
		//update bundary2D
		this.boundaryModel2D.update(displacement);
		
		//Test if the tank  with other objects.
		//If the result is positive, then move back to last known good location.
		int newPosition = (int)(this.boundaryModel2D.xPos*4) + (129-(int)(this.boundaryModel2D.yPos*4))*80;
		
		if(ObstacleMap.collideWithObstacle1(this, newPosition)){
			displacement.scale(-1);
			this.boundaryModel2D.update(displacement);
			this.centreModel.add(displacement);
			displacement.set(0,0,0);
		}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
			displacement.scale(-1);
			this.boundaryModel2D.update(displacement);
			this.centreModel.add(displacement);
			displacement.set(0,0,0);
		}else if(outSideBorder()){
			//test tank position against island border
			displacement.scale(-1);
			this.boundaryModel2D.update(displacement);
			this.centreModel.add(displacement);
			displacement.set(0,0,0);
		}else{
			//if no collision is detected, update tile index
			if(!ObstacleMap.isOccupied(newPosition)){
				ObstacleMap.removeObstacle2(this.position);
				ObstacleMap.registerObstacle2(this, newPosition);
				this.position = newPosition;
			
			}
		}
		
		
		//update 3D boundary
		for(int i = 0; i < 5; i++){
			for(int j = 0; j < 4; j++)
				this.boundaryModel[i].vertex3D[j].add(displacement);
			this.boundaryModel[i].update();
		}
		
		
		
		//find centre in camera coordinate
		this.cantreModelInCamera.set(this.centreModel);
		this.cantreModelInCamera.y = -1;
		this.cantreModelInCamera.subtract(Camera.cameraPosition);
		this.cantreModelInCamera.rotate_XZ(Camera.XZ_angle);
		this.cantreModelInCamera.rotate_YZ(Camera.YZ_angle);
		
		//update body polygons
		for(int i = 0; i < this.body.length; i++){
			//perform vertex updates in world coordinate
			this.body[i].origin.add(displacement);
			this.body[i].origin.subtract(this.centreModel);
			this.body[i].origin.rotate_XZ(this.bodyAngleDelta);
			this.body[i].origin.add(this.centreModel);
			
			this.body[i].bottomEnd.add(displacement);
			this.body[i].bottomEnd.subtract(this.centreModel);
			this.body[i].bottomEnd.rotate_XZ(this.bodyAngleDelta);
			this.body[i].bottomEnd.add(this.centreModel);
			
			this.body[i].rightEnd.add(displacement);
			this.body[i].rightEnd.subtract(this.centreModel);
			this.body[i].rightEnd.rotate_XZ(this.bodyAngleDelta);
			this.body[i].rightEnd.add(this.centreModel);
			
			for(int j = 0; j < this.body[i].vertex3D.length; j++){
				this.body[i].vertex3D[j].add(displacement);
				this.body[i].vertex3D[j].subtract(this.centreModel);
				this.body[i].vertex3D[j].rotate_XZ(this.bodyAngleDelta);
				this.body[i].vertex3D[j].add(this.centreModel);
			}
			
			this.body[i].findRealNormal();
			this.body[i].findDiffuse();
			
			//transform the polygon into camera coordinate
			this.body[i].update();
		}
		
		//update shadow for tank body
		this.tempVector1.set(this.centreModel);
		this.tempVector1.add(-0.03, 0, -0.02);
		this.shadowBody.origin.add(displacement);
		this.shadowBody.origin.subtract(this.tempVector1);
		this.shadowBody.origin.rotate_XZ(this.bodyAngleDelta);
		this.shadowBody.origin.add(this.tempVector1);
		
		this.shadowBody.bottomEnd.add(displacement);
		this.shadowBody.bottomEnd.subtract(this.tempVector1);
		this.shadowBody.bottomEnd.rotate_XZ(this.bodyAngleDelta);
		this.shadowBody.bottomEnd.add(this.tempVector1);
		
		this.shadowBody.rightEnd.add(displacement);
		this.shadowBody.rightEnd.subtract(this.tempVector1);
		this.shadowBody.rightEnd.rotate_XZ(this.bodyAngleDelta);
		this.shadowBody.rightEnd.add(this.tempVector1);
		
		for(int j = 0; j < this.shadowBody.vertex3D.length; j++){
			this.shadowBody.vertex3D[j].add(displacement);
			this.shadowBody.vertex3D[j].subtract(this.tempVector1);
			this.shadowBody.vertex3D[j].rotate_XZ(this.bodyAngleDelta);
			this.shadowBody.vertex3D[j].add(this.tempVector1);
		}
		
		this.shadowBody.update();
		Rasterizer.rasterize(this.shadowBody);
		
		
		
		if(this.currentWeapon == 4){
			makeTurretNuke();
		}
		
		//update turret center
		turretCenter.add(displacement);
		this.tempVector1.set(turretCenter);
		turretCenter.subtract(this.centreModel);
		turretCenter.rotate_XZ(this.bodyAngleDelta);
		turretCenter.add(this.centreModel);
		this.tempVector2.set(turretCenter);
		this.tempVector2.subtract(this.tempVector1);
		
		//update turret polygons
		for(int i = 0; i < this.turret.length; i++){
			//perform vertex updates in world coordinate
			this.turret[i].origin.add(displacement);
			this.turret[i].origin.add(this.tempVector2);
			this.turret[i].origin.subtract(turretCenter);
			this.turret[i].origin.rotate_XZ(this.turretAngleDelta);
			this.turret[i].origin.add(turretCenter);
			
			this.turret[i].bottomEnd.add(displacement);
			this.turret[i].bottomEnd.add(this.tempVector2);
			this.turret[i].bottomEnd.subtract(turretCenter);
			this.turret[i].bottomEnd.rotate_XZ(this.turretAngleDelta);
			this.turret[i].bottomEnd.add(turretCenter);
			
			this.turret[i].rightEnd.add(displacement);
			this.turret[i].rightEnd.add(this.tempVector2);
			this.turret[i].rightEnd.subtract(turretCenter);
			this.turret[i].rightEnd.rotate_XZ(this.turretAngleDelta);
			this.turret[i].rightEnd.add(turretCenter);
			
			for(int j = 0; j < this.turret[i].vertex3D.length; j++){
				this.turret[i].vertex3D[j].add(displacement);
				this.turret[i].vertex3D[j].add(this.tempVector2);
				this.turret[i].vertex3D[j].subtract(turretCenter);
				this.turret[i].vertex3D[j].rotate_XZ(this.turretAngleDelta);
				this.turret[i].vertex3D[j].add(turretCenter);
			}
			
			this.turret[i].findRealNormal();
			this.turret[i].findDiffuse();
			
			//transform the polygon into camera coordinate
			this.turret[i].update();
		}
		
		if(this.currentWeapon == 4){
			Geometry.sortPolygons(this.nukeCannonRear, 0);
			Geometry.sortPolygons(this.nukeCannonFront, 0);
			
			int index = 0;
			
			for(int i = 0; i < 72; i++){
				this.turret[i] = this.nukeCannonFront[i];
			}
			
			index+=this.nukeCannonFront.length;
			
			for(int i = 0; i < this.nukeCannonMiddle.length; i++){
				this.turret[i + index] = this.nukeCannonMiddle[i];
			}
			
			index+=this.nukeCannonMiddle.length;
			
			for(int i = 0; i < 72; i++){
				this.turret[i + index] = this.nukeCannonRear[i];
			}
			
			
		}
		
		
		
		//update shadow for tank turret
		this.tempVector1.set(turretCenter);
		this.tempVector1.add(-0.04, 0, -0.04);
		
		this.shadowTurret.origin.add(displacement);
		this.shadowTurret.origin.add(this.tempVector2);
		this.shadowTurret.origin.subtract(this.tempVector1);
		this.shadowTurret.origin.rotate_XZ(this.turretAngleDelta);
		this.shadowTurret.origin.add(this.tempVector1);
		
		this.shadowTurret.bottomEnd.add(displacement);
		this.shadowTurret.bottomEnd.add(this.tempVector2);
		this.shadowTurret.bottomEnd.subtract(this.tempVector1);
		this.shadowTurret.bottomEnd.rotate_XZ(this.turretAngleDelta);
		this.shadowTurret.bottomEnd.add(this.tempVector1);
		
		this.shadowTurret.rightEnd.add(displacement);
		this.shadowTurret.rightEnd.add(this.tempVector2);
		this.shadowTurret.rightEnd.subtract(this.tempVector1);
		this.shadowTurret.rightEnd.rotate_XZ(this.turretAngleDelta);
		this.shadowTurret.rightEnd.add(this.tempVector1);
		
		for(int j = 0; j < this.shadowTurret.vertex3D.length; j++){
			this.shadowTurret.vertex3D[j].add(displacement);
			this.shadowTurret.vertex3D[j].add(this.tempVector2);
			this.shadowTurret.vertex3D[j].subtract(this.tempVector1);
			this.shadowTurret.vertex3D[j].rotate_XZ(this.turretAngleDelta);
			this.shadowTurret.vertex3D[j].add(this.tempVector1);
		}
		
		this.shadowTurret.update();
		Rasterizer.rasterize(this.shadowTurret);
		
		
		
		
		
		//handle attack event
		if(this.currentCoolDown > 0 && !Main.gamePaused)
			this.currentCoolDown--;
		if(firing){
			if(this.currentCoolDown == 0){
				if(this.currentWeapon == 1 && shells > 0){
					shells--;
					this.currentCoolDown = this.coolDowns[this.currentWeapon];
					//calculate shell direction
					Vector direction = new Vector(0,0,1);
					direction.rotate_XZ(turretAngle);
					direction.scale(0.02);
					direction.add(turretCenter);
					Projectiles.register(new Shell(direction.x, direction.y,direction.z, turretAngle,false, 0));
					if(shells == 0)
						this.outOfAmmo = true;
					
					
				}else if(this.currentWeapon == 2 && rockets > 0){
					rockets--;
					this.currentCoolDown = this.coolDowns[this.currentWeapon];
					//calculate initial rocket direction
					Vector tempVector1 = new Vector(0,0,1);
					tempVector1.rotate_XZ((turretAngle+270)%360);
					tempVector1.scale(0.08);
					Vector direction = new Vector(0,0,1);
					direction.rotate_XZ(turretAngle);
					direction.scale(0.05);
					direction.add(turretCenter);
					direction.add(tempVector1);
					Projectiles.register(new Rocket(direction.x, direction.y,direction.z, turretAngle ,false));
					if(rockets == 0)
						this.outOfAmmo = true;
				}else if(this.currentWeapon == 3 && slugs > 0){
					slugs--;
					this.currentCoolDown = this.coolDowns[this.currentWeapon];
					//calculate slug direction
					Vector direction = new Vector(0,0,1);
					direction.rotate_XZ(turretAngle);
					direction.scale(0.1);
					direction.add(turretCenter);
					new RailgunTail(direction, turretAngle, false);
					if(slugs == 0)
						this.outOfAmmo = true;
					
				}else if(this.currentWeapon == 4 && plasma > 0){
					if(this.angularSpeed >= 30){
						plasma--;
						this.currentCoolDown = this.coolDowns[this.currentWeapon];
						//calculate shell direction
						Vector direction = new Vector(0,0,1);
						direction.rotate_XZ(turretAngle);
						direction.scale(0.02);
						direction.add(turretCenter);
						Projectiles.register(new Shell(direction.x, direction.y,direction.z, (turretAngle + Main.timer%8 +356)%360 ,false, 1));
						
					}else{
						
						this.angularSpeed+=1;
						if(this.angularSpeed > 30)
							this.angularSpeed = 30;
					}
					
					if(plasma == 0)
						this.outOfAmmo = true;
				}
				
				//change weapon when run out of ammo for current weapon
				if(this.outOfAmmo){
					try {
					
						if(shells > 0 && this.currentWeapon != 1)
							changeWeapon(1);
						else if(rockets > 0 && this.currentWeapon != 2)
							changeWeapon(2);
						else if(slugs > 0 && this.currentWeapon != 3)
							changeWeapon(3);
						else if(plasma > 0 && this.currentWeapon != 4)
							changeWeapon(4);
					
					} catch (InvalidAttributeValueException e) {
						LOGGER.log(Level.WARNING, "Invalid Weapon Value");
					}
				}
				
			}
		}
		
		if(this.currentWeapon == 1)
			currentAmmo = shells;
		if(this.currentWeapon == 2)
			currentAmmo = rockets;
		if(this.currentWeapon == 3)
			currentAmmo = slugs;
		if(this.currentWeapon == 4)
			currentAmmo = plasma;
		
		//reset action flag
		forward = false;
		backward = false;
		turnRight = false;
		turnLeft = false;
		moveRight = false;
		moveLeft = false;
		this.bodyAngleDelta = 0;
		this.turretAngleDelta = 0;	
		displacement.reset();
		firing = false;
		this.outOfAmmo = false;
		
	}
	
	//draw model
	@Override
	public void drawExplosion(){
		//dont draw play tank when game is finished
		if(Main.gameOver)
			return;
		
		//draw body
		for(int i = 0; i < this.body.length; i++){
			this.body[i].draw();
		}
		
		//draw turret
		
		for(int i = 0; i < this.turret.length; i++){
			this.turret[i].draw();
		}
		
	}
	
	//This method tests whether the tank is within the island border which is described by a Polygon.
	//If the tank is inside this border, the sum of the angles formed by tank centre point 
	//and each edge of the polygon will approximately equal to 360 degree.
	public boolean outSideBorder(){
		double angle = 0;
		int length = Terrain.border.vertex3D.length;
		for(int i = 0; i < length; i++){
			this.tempVector1.set(Terrain.border.vertex3D[i]);
			this.tempVector1.subtract(this.centreModel);
			this.tempVector2.set(Terrain.border.vertex3D[(i+1+length)%length]);
			this.tempVector2.subtract(this.centreModel);
			double dot = this.tempVector1.dot(this.tempVector2);
			dot = dot/(this.tempVector1.getLength())/(this.tempVector2.getLength());
			angle+=Math.acos(dot);
		}
		return Math.PI*2 - angle > 0.01;
		
	}
	
	//change weapon
	public void changeWeapon(int weapon) throws InvalidAttributeValueException{
		
		if(weapon > 4 && weapon < -1){
			throw new InvalidAttributeValueException();
		}
		
		if(weapon == -1){
			weapon = (this.currentWeapon+1)%5;
			if(weapon == 0)
				weapon = 1;
		}
		
		if(weapon == 1 && shells > 0){
			this.currentWeapon = 1;
			makeTurretCannon();
		}
		if(weapon == 1 && shells == 0){
			weapon = 2;
		}
		
		if(weapon == 2 && rockets > 0){
			this.currentWeapon = 2;
			makeTurretRocket();
		}
		if(weapon == 2 && rockets == 0){
			weapon = 3;
		}
		
		if(weapon == 3 && slugs > 0){
			this.currentWeapon = 3;
			makeTurretRailgun();
		}
		if(weapon == 3 && slugs == 0){
			weapon = 4;
			
		}
		
		if(weapon == 4 && plasma > 0){
			if(this.currentWeapon != 4){
				this.currentWeapon = 4;
				makeTurretNuke();
				this.angularSpeed = 0;
			}
			
			
			
			
		}if(weapon == 4 && plasma == 0){
			this.currentWeapon = 1;
			makeTurretCannon();
		}
			
	}
	
	//damage the object. (ie, reduce its hitpoint)
	//the tank will stop  regenerating after got hit
	@Override
	public void damage(int damagePoint) throws InvalidAttributeValueException{
		
		if(damagePoint < 0){
			throw new InvalidAttributeValueException();
		}
		
		//apply damage mulitplier 
		this.HP-=(damagePoint * 0.8);
		if(this.HP <= 0){
			this.HP = 0;
			Projectiles.register(new Explosion(this.centreModel.x, this.centreModel.y, this.centreModel.z, 1.7));
			this.centreModel.x = 1000;
			
			Main.gameOver =  true;
		}
		if(damagePoint > 0)
			this.underAttackCount = 100;
		
	}
	
}
