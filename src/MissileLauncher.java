package src;
//enemy:  surface to surface missile launcher
public class MissileLauncher extends SolidObject{
	//polygons for SSML
	private Polygon3D[] polygons;
	
	//polygons for  body 
	private Polygon3D[] body;
	
	//total angle that the body has rotated from the initial position. (in the x-z plane)
	private int bodyAngle;
	
	//the centre of the body in camera coordinate
	private Vector bodyCenter;
	
	//polygons for missile launcher turret
	private Polygon3D[] turret;

	//the shadow of  body
	private Polygon3D shadowBody;
	
	//the shadow of  turret
	private Polygon3D shadowTurret;
	
	//the centre of the turret (pivot point for rotation)
	private Vector turretCenter;
	
	//total angle that the turret has rotated from the initial position. (in the x-z plane)
	private int turretAngle;

	//movement flag
	private boolean forward, aimRight, aimLeft, firing;

	//time needed before a weapon can be fired again
	private int coolDown = 80;
	
	//mini cool down between 2 successive missiles launch
	private int coolDownA = 10;
	
	//cool down between 2 successive round of missiles launch
	private int coolDownB = 60;
	
	//current coolDown
	private int currentCoolDown = 0;
	
	//change SSML's position of each frame
	private Vector displacement = new Vector(0,0,0);
	
	//degrees body has rotated in a frame
	private int bodyAngleDelta;
	
	//degrees the turreet has rotated in a frame
	private int turretAngleDelta;
	
	//The position index of the tank in the grid map
	private int position;
	
	//whether the tank is visible in the previous frame
	private boolean isVisiblePreviousFrame;
	
	//a smoke tail
	private Smoke Smoke;
	
	//distance from player tank
	private double distance;
	
	//angle between player tank and turret centre
	private int targetAngle;
	
	//angle between a target location and body centre
	private int targetAngleBody;
	
	//targetAngleBody of the previous frame
	private int previousTargetAngleBody;
	
	//temporary vectors which will be used for vector arithmetic
	private Vector tempVector1 = new Vector(0,0,0);
	private Vector tempVector2 = new Vector(0,0,0);
	
	// a flag which indicate whether the take will interact with player at all. (i.e some enemy only get activtied at a certain stage of the game)
	public boolean active = true;
	
	//an AI flag  shows whether it has engaged with player tank
	private boolean engaged;
	
	//an AI flag indicates whether there is a type 2 obstacle between the SSML and player tank
	private boolean clearToShoot;
	
	//a count down for death after hp = 0
	private int countDownToDeath;
	
	private int stuckCount;
	
	//random number 
	private int randomNumber1, randomNumber2;
	
	public MissileLauncher(double x, double y, double z, int angle){
		//define the center point of this model(also the centre point of tank body)
		startPointInWorld = new Vector(x,y,z);
		iDirection = new Vector(1,0,0);
		jDirection = new Vector(0,1,0);
		kDirection = new Vector(0,0,1);
		
		//adjust orientation of the model
		iDirection.rotate_XZ(angle);
		kDirection.rotate_XZ(angle);
		bodyAngle = angle;
		turretAngle = angle;
		
		//boundary of this model has a cubic shape (ie l = w)
		modelType = 2;  
		makeBoundary(0.1, 0.25, 0.1);
		
		//create 2D boundary
		boundaryModel2D = new Rectangle2D(x - 0.1, z + 0.1, 0.2, 0.2);
		position = (int)(x*4) + (129-(int)(z*4))*80;
		ObstacleMap.registerObstacle2(this, position);
		
		//find centre of the model in world coordinate
		findCentre();
		
		bodyCenter = centreModel;
		
		polygons = new Polygon3D[19];
		makeBody();
		makeTurret();
		
		randomNumber1 = GameData.getRandom();
		
		//SSML has 15 hitpoints
		HP = 15;
		
		lifeSpanObject = 1;
		
	}
	
	
	
	//create polygons for  SSML body
	public void makeBody(){
		Vector[] v;
		
		startPointInWorld = bodyCenter.myClone();
		iDirection = new Vector(1,0,0);
		jDirection = new Vector(0,0.8,0);
		kDirection = new Vector(0,0,1);
		
		iDirection.rotate_XZ(bodyAngle);
		kDirection.rotate_XZ(bodyAngle);
		
		body = new Polygon3D[14];
		v = new Vector[]{put(-0.07, 0.055, 0.07), put(0.07, 0.055, 0.07), put(0.07, 0.055, -0.13), put(-0.07, 0.055, -0.13)};
		body[0] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[27], 1,1,6);
	
		v = new Vector[]{put(-0.069, 0.055, 0.13), put(-0.069, 0.055, -0.13), put(-0.069, 0.02, -0.13), put(-0.069, 0.02, 0.13)};
		body[1] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[27], 1,0.1,6);
		
		v = new Vector[]{put(0.069, 0.02, 0.13), put(0.069, 0.02, -0.13), put(0.069, 0.055, -0.13), put(0.069, 0.055, 0.13)};
		body[2] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[27], 1,0.1,6);
		
		v = new Vector[]{put(0.07, 0.1, 0.13), put(-0.07, 0.1, 0.13), put(-0.07, 0.02, 0.13), put(0.07, 0.02, 0.13)};
		body[3] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[27], 1,0.3,6);
		
		v = new Vector[]{put(0.07, 0.14, 0.11), put(-0.07, 0.14, 0.11), put(-0.07, 0.1, 0.13), put(0.07, 0.1, 0.13)};
		body[4] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[28], 1,0.3,6);
		
		v = new Vector[]{put(0.07, 0.14, 0.07), put(0.07, 0.14, 0.11), put(0.07, 0.1, 0.13),  put(0.07, 0.055, 0.13), put(0.07, 0.055, 0.07)};
		body[5] = new Polygon3D(v, v[0], v[1], v[4], Main.textures[27], 0.4,0.3,6);
		
		v = new Vector[]{put(-0.07, 0.055, 0.07),  put(-0.07, 0.055, 0.13), put(-0.07, 0.1, 0.13),put(-0.07, 0.14, 0.11), put(-0.07, 0.14, 0.07), };
		body[6] = new Polygon3D(v, v[0], v[1], v[4], Main.textures[27], 0.4,0.3,6);
		
		v = new Vector[]{put(-0.07, 0.14, 0.11), put(0.07, 0.14, 0.11), put(0.07, 0.14, 0.07), put(-0.07, 0.14, 0.07)};
		body[7] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[27], 1,0.3,6);
		
		v = new Vector[]{put(-0.07, 0.14, 0.07), put(0.07, 0.14, 0.07), put(0.07, 0.055, 0.07), put(-0.07, 0.055, 0.07)};
		body[8] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[27], 1,0.3,6);
		
		v = new Vector[]{put(-0.07, 0.055, -0.13), put(0.07, 0.055, -0.13), put(0.07, 0.02, -0.13), put(-0.07, 0.02, -0.13)};
		body[9] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[27], 1,0.3,6);
		
		v = new Vector[]{put(-0.07, 0.02, 0.13), put(-0.07, 0.02, -0.13), put(-0.07, -0.03, -0.11), put(-0.07, -0.03, 0.11)};
		body[10] = new Polygon3D(v, v[0], v[1], put(-0.07, -0.03, 0.13), Main.textures[12], 1,1,6);
		
		v = new Vector[]{put(0.07, -0.03, 0.11), put(0.07, -0.03, -0.11), put(0.07, 0.02, -0.13), put(0.07, 0.02, 0.13)};
		body[11] = new Polygon3D(v, v[2], v[3], put(0.07, -0.03, -0.13), Main.textures[12], 1,1,6);
		
		v = new Vector[]{put(0.07, 0.02, 0.13), put(0.04, 0.02, 0.13), put(0.04, -0.03, 0.11),  put(0.07, -0.03, 0.11)};
		body[12] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[12], 1,1,6);
		
		v = new Vector[]{put(-0.04, 0.02, 0.13), put(-0.07, 0.02, 0.13), put(-0.07, -0.03, 0.11),  put(-0.04, -0.03, 0.11)};
		body[13] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[12], 1,1,6);
		
		for(int i = 0; i < body.length; i++)
			polygons[i] = body[i];
		
		turretCenter = put(0, 0.08, -0.05);
		
		//create shadow for SSML body
		startPointInWorld.add(-0.015, 0, -0.017);
		startPointInWorld.y = -1;
		
		v = new Vector[]{put(-0.23, 0, 0.23), put(0.23, 0, 0.23), put(0.23, 0, -0.23), put(-0.23, 0, -0.23)};
		shadowBody = new Polygon3D(v, v[0], v[1], v[3], Main.textures[14], 1, 1, 2);
		
	}
	
	//create polygons for  SSML turret
	public void makeTurret(){
		startPointInWorld = turretCenter.myClone();
		Vector[] v;
		turret = new Polygon3D[5];
		
		iDirection = new Vector(1,0,0);
		jDirection = new Vector(0,1,0);
		kDirection = new Vector(0,0,1);
		
	
		
		//iDirection.rotate_YZ(10);
		jDirection.rotate_YZ(350);
		kDirection.rotate_YZ(350);
		
		iDirection.rotate_XZ(turretAngle);
		kDirection.rotate_XZ(turretAngle);
		jDirection.rotate_XZ(turretAngle);
		
		
		
		v = new Vector[]{put(-0.06, 0.055, 0.09), put(0.06, 0.055, 0.09), put(0.06, 0.055, -0.08), put(-0.06, 0.055, -0.08)};
		turret[0] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[29], 1,1,6);
		
		v = new Vector[]{put(-0.06, 0.055, 0.09), put(-0.06, 0.055, -0.08), put(-0.06, 0.01, -0.08), put(-0.06, 0.01, 0.09)};
		turret[1] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[29], 1,0.3,6);
		
		v = new Vector[]{put(0.06, 0.01, 0.09), put(0.06, 0.01, -0.08), put(0.06, 0.055, -0.08), put(0.06, 0.055, 0.09)};
		turret[2] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[29], 1,0.3,6);
		
		v = new Vector[]{put(0.06, 0.055, 0.09), put(-0.06, 0.055, 0.09), put(-0.06, 0.01, 0.09),  put(0.06, 0.01, 0.09)};
		turret[3] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[30], 1,0.5,6);
		
		v = new Vector[]{put(0.06, 0.01, -0.08), put(-0.06, 0.01, -0.08), put(-0.06, 0.055, -0.08) , put(0.06, 0.055, -0.08)};
		turret[4] = new Polygon3D(v, v[0], v[1], v[3], Main.textures[29], 1,0.5,6);
		
		//create shadow for tank turret
		startPointInWorld.add(-0.05, 0, -0.04);
		iDirection = new Vector(1,0,0);
		jDirection = new Vector(0,1,0);
		kDirection = new Vector(0,0,1);
		
		iDirection.rotate_XZ(turretAngle);
		kDirection.rotate_XZ(turretAngle);
		jDirection.rotate_XZ(turretAngle);
		
		startPointInWorld.y = -1;
		v = new Vector[]{put(-0.18, 0, 0.18), put(0.18, 0, 0.18), put(0.18, 0, -0.18), put(-0.18, 0, -0.18)};
		shadowTurret = new Polygon3D(v, v[0], v[1], v[3], Main.textures[14], 1, 1, 2);
		
		
		for(int i = 0; i < turret.length; i++)
			polygons[body.length + i] = turret[i];
	}
	
	//update SSML
	public void update(){
		//retrive a random number every 1000 game frame
		if((Main.timer+randomNumber1*3)%3000 == 0){
			if(randomNumber2 > 50)
				randomNumber2 = 50;
			else
				randomNumber2 = 51;
		}
		
		//process AI
		if(countDownToDeath <= 0 && active && !Main.gamePaused){
			processAI();
			
		}
		
		//perform actions
		if(aimLeft){
			if(Math.abs(turretAngle - targetAngle) <=3){
				turretAngleDelta = targetAngle - turretAngle;
				turretAngle+=turretAngleDelta;
				if(turretAngleDelta < 0)
					turretAngleDelta+=360;
			}else{
				turretAngleDelta=3;
				turretAngle+=3;
			}
			if(turretAngle >= 360)
				turretAngle-=360;
		}else if(aimRight){
			if(Math.abs(turretAngle - targetAngle) <=3){
				turretAngleDelta = targetAngle - turretAngle;
				turretAngle+=turretAngleDelta;
				if(turretAngleDelta < 0)
					turretAngleDelta+=360;
			}else{
				turretAngleDelta=357;
				turretAngle-=3;
			}
			if(turretAngle < 0)
				turretAngle+=360;
		}
		
		
		
		if(forward){
			//move foward
			int delta = targetAngleBody - bodyAngle;
			if(Math.abs(delta) < 5 || Math.abs(delta) > 355){
				bodyAngle = targetAngleBody;
				bodyAngleDelta = (delta+720)%360;
				displacement.set(0,0,0.01);
				displacement.rotate_XZ(bodyAngle);
			}else{
				displacement.set(0,0,0);
				if(delta > 0){
					if(delta < 180)
						bodyAngleDelta = 5;
					else
						bodyAngleDelta = 355;
				}	
				if(delta < 0){
					if(delta > -180)
						bodyAngleDelta = 355;
					else
						bodyAngleDelta = 5;
				}
				
				bodyAngle = (bodyAngle+bodyAngleDelta)%360;
			}
		}
		
		
		//update location in the 2d tile map
		//validating movement is already done in  process AI part
		int newPosition = (int)(boundaryModel2D.xPos*4) + (129-(int)(boundaryModel2D.yPos*4))*80;
		if(!ObstacleMap.isOccupied(newPosition)){
			ObstacleMap.removeObstacle2(position);
			ObstacleMap.registerObstacle2(this, newPosition);
			position = newPosition;
		}
		
		//update centre
		centreModel.add(displacement);
		
		//update bundary2D
		boundaryModel2D.update(displacement);
		
		//update 3D boundary
		//for(int i = 0; i < 5; i++){
		//	for(int j = 0; j < 4; j++)
		//		boundary[i].vertex3D[j].add(displacement);
		//	boundary[i].update();
		//}
		
		//	find centre in camera coordinate
		cantreModelInCamera.set(centreModel);
		cantreModelInCamera.y = -1;
		cantreModelInCamera.subtract(Camera.position);
		cantreModelInCamera.rotate_XZ(Camera.XZ_angle);
		cantreModelInCamera.rotate_YZ(Camera.YZ_angle);
		cantreModelInCamera.updateLocation();
		
		//test whether the model is visible by comparing the 2D position of its centre point with the screen area
		isVisible = true;
		if(cantreModelInCamera.z < 0.9 || cantreModelInCamera.screenY < -10 || cantreModelInCamera.screenX < -400 || cantreModelInCamera.screenX >800){
			isVisible = false;
			isVisiblePreviousFrame = false;
		}
		
		//if tank is not visible in the previous frame, it needs to be reconstructed
		if(isVisible){
			if(isVisiblePreviousFrame == false){
				//recreate body and turret polygons
				makeBody();
				makeTurret();
				isVisiblePreviousFrame = true;
			}
		}
		
		//if visible then update the geometry to camera coordinate
		if(isVisible){
			ModelDrawList.register(this);
			if(countDownToDeath <3){
				
				//update body polygons
				for(int i = 0; i < body.length; i++){
					//perform vertex updates in world coordinate
					body[i].origin.add(displacement);
					body[i].origin.subtract(centreModel);
					body[i].origin.rotate_XZ(bodyAngleDelta);
					body[i].origin.add(centreModel);
					
					body[i].bottomEnd.add(displacement);
					body[i].bottomEnd.subtract(centreModel);
					body[i].bottomEnd.rotate_XZ(bodyAngleDelta);
					body[i].bottomEnd.add(centreModel);
					
					body[i].rightEnd.add(displacement);
					body[i].rightEnd.subtract(centreModel);
					body[i].rightEnd.rotate_XZ(bodyAngleDelta);
					body[i].rightEnd.add(centreModel);
					
					for(int j = 0; j < body[i].vertex3D.length; j++){
						body[i].vertex3D[j].add(displacement);
						body[i].vertex3D[j].subtract(centreModel);
						body[i].vertex3D[j].rotate_XZ(bodyAngleDelta);
						body[i].vertex3D[j].add(centreModel);
					}
					
					body[i].findRealNormal();
					body[i].findDiffuse();
					
					//transform the polygon into camera coordinate
					body[i].update();
				}
				
				//update shadow for SSML body
				tempVector1.set(centreModel);
				tempVector1.add(-0.03, 0, -0.02);
				shadowBody.origin.add(displacement);
				shadowBody.origin.subtract(tempVector1);
				shadowBody.origin.rotate_XZ(bodyAngleDelta);
				shadowBody.origin.add(tempVector1);
				
				shadowBody.bottomEnd.add(displacement);
				shadowBody.bottomEnd.subtract(tempVector1);
				shadowBody.bottomEnd.rotate_XZ(bodyAngleDelta);
				shadowBody.bottomEnd.add(tempVector1);
				
				shadowBody.rightEnd.add(displacement);
				shadowBody.rightEnd.subtract(tempVector1);
				shadowBody.rightEnd.rotate_XZ(bodyAngleDelta);
				shadowBody.rightEnd.add(tempVector1);
				
				for(int j = 0; j < shadowBody.vertex3D.length; j++){
					shadowBody.vertex3D[j].add(displacement);
					shadowBody.vertex3D[j].subtract(tempVector1);
					shadowBody.vertex3D[j].rotate_XZ(bodyAngleDelta);
					shadowBody.vertex3D[j].add(tempVector1);
				}
				
				shadowBody.update();
				Rasterizer.rasterize(shadowBody);
				
				//update turret center
				turretCenter.add(displacement);
				tempVector1.set(turretCenter);
				turretCenter.subtract(centreModel);
				turretCenter.rotate_XZ(bodyAngleDelta);
				turretCenter.add(centreModel);
				tempVector2.set(turretCenter);
				tempVector2.subtract(tempVector1);
				
				//update turret polygons
				for(int i = 0; i < turret.length; i++){
					//perform vertex updates in world coordinate
					turret[i].origin.add(displacement);
					turret[i].origin.add(tempVector2);
					turret[i].origin.subtract(turretCenter);
					turret[i].origin.rotate_XZ(turretAngleDelta);
					turret[i].origin.add(turretCenter);
					
					turret[i].bottomEnd.add(displacement);
					turret[i].bottomEnd.add(tempVector2);
					turret[i].bottomEnd.subtract(turretCenter);
					turret[i].bottomEnd.rotate_XZ(turretAngleDelta);
					turret[i].bottomEnd.add(turretCenter);
					
					turret[i].rightEnd.add(displacement);
					turret[i].rightEnd.add(tempVector2);
					turret[i].rightEnd.subtract(turretCenter);
					turret[i].rightEnd.rotate_XZ(turretAngleDelta);
					turret[i].rightEnd.add(turretCenter);
					
					for(int j = 0; j < turret[i].vertex3D.length; j++){
						turret[i].vertex3D[j].add(displacement);
						turret[i].vertex3D[j].add(tempVector2);
						turret[i].vertex3D[j].subtract(turretCenter);
						turret[i].vertex3D[j].rotate_XZ(turretAngleDelta);
						turret[i].vertex3D[j].add(turretCenter);
					}
					
					turret[i].findRealNormal();
					turret[i].findDiffuse();
					
					//transform the polygon into camera coordinate
					turret[i].update();
				}
				
				//update shadow for SSML turret
				tempVector1.set(turretCenter);
				tempVector1.add(-0.05, 0, -0.04);
				
				shadowTurret.origin.add(displacement);
				shadowTurret.origin.add(tempVector2);
				shadowTurret.origin.subtract(tempVector1);
				shadowTurret.origin.rotate_XZ(turretAngleDelta);
				shadowTurret.origin.add(tempVector1);
				
				shadowTurret.bottomEnd.add(displacement);
				shadowTurret.bottomEnd.add(tempVector2);
				shadowTurret.bottomEnd.subtract(tempVector1);
				shadowTurret.bottomEnd.rotate_XZ(turretAngleDelta);
				shadowTurret.bottomEnd.add(tempVector1);
				
				shadowTurret.rightEnd.add(displacement);
				shadowTurret.rightEnd.add(tempVector2);
				shadowTurret.rightEnd.subtract(tempVector1);
				shadowTurret.rightEnd.rotate_XZ(turretAngleDelta);
				shadowTurret.rightEnd.add(tempVector1);
				
				for(int j = 0; j < shadowTurret.vertex3D.length; j++){
					shadowTurret.vertex3D[j].add(displacement);
					shadowTurret.vertex3D[j].add(tempVector2);
					shadowTurret.vertex3D[j].subtract(tempVector1);
					shadowTurret.vertex3D[j].rotate_XZ(turretAngleDelta);
					shadowTurret.vertex3D[j].add(tempVector1);
				}
				shadowTurret.update();
				Rasterizer.rasterize(shadowTurret);
			}
			
			Geometry.sortPolygons(polygons, 1);
		}
		
		//handle attack event
		if(currentCoolDown > 0 && !Main.gamePaused)
			currentCoolDown--;
		
		
		if(firing){
			if(currentCoolDown == 0){
				if(coolDown == coolDownA)
					coolDown = coolDownB;
				else{
					coolDown = coolDownA;
				}
				
				currentCoolDown = coolDown;
				//calculate shell direction
				Vector direction = new Vector(0,0,1);
				direction.rotate_XZ(turretAngle);
				direction.scale(0.06);
				Projectiles.register(new Rocket(centreModel.x+direction.x, centreModel.y + 0.05,centreModel.z+direction.z, turretAngle ,true));
			}
		}
		
		
		if(HP <= 8){
			if(Smoke == null){
				Smoke = new Smoke(this);
			}else{
				if(isVisible)
					Smoke.update();
			}
		}
		
		if(HP <= 0){
			countDownToDeath++;
			if(countDownToDeath >= 3){
				if(countDownToDeath == 3){
					Projectiles.register(new Explosion(centreModel.x, centreModel.y, centreModel.z, 1.7));
					PowerUps.register(new PowerUp(centreModel.x, -0.875, centreModel.z, 2));
				}
				ObstacleMap.removeObstacle2(position);
				Smoke.stopped = true;
			}
			if(countDownToDeath >=40)
				lifeSpanObject = 0;
		}
		
		
		
		//reset action flag
		forward = false;
		aimRight = false;
		aimLeft = false;
		bodyAngleDelta = 0;
		turretAngleDelta = 0;	
		displacement.reset();
		firing = false;
		if(Main.timer%10 == 0)
			isBlockingOtherModel = false;
	}
	
	
	
	
	
	//process AI
	private void processAI(){

		//calculate distance from player's tank
		tempVector1.set(centreModel);
		tempVector1.subtract(PlayerTank.bodyCenter);
		distance = tempVector1.getLength();
		
		//SSML become aware of player's tank when the distance is less than 2
		if(distance < 2.5)
			engaged = true;
		
		//SSML will stop chasing the player when the distance is greater than 4.5
		if(distance > 4.5){
			engaged = false;
			//rotate the turret to the same angle as the body
			targetAngle = bodyAngle;
			int AngleDelta = turretAngle - targetAngle;
			if(AngleDelta > 0){
				if(AngleDelta < 180)
					aimRight = true;
				else
					aimLeft = true;
			}
			else if(AngleDelta < 0){
				if(AngleDelta > -180)
					aimLeft = true;
				else 
					aimRight = true;
			}
			return;
		}
		
		if(engaged){
			//if medium tank is engaged with player, it will send alert to nearby tanks
			if((Main.timer)%5 == 0 )
				ObstacleMap.alertNearbyTanks(position);
			
			//test whether there is a type obstacle 2 between medium tank and player tank
			//firing a vision ray from medium tank toward player tank
			tempVector1.set(bodyCenter);
			tempVector2.set(PlayerTank.bodyCenter);
			tempVector2.subtract(tempVector1);
			tempVector2.unit();
			tempVector2.scale(0.125);
			
			clearToShoot = true;
			double d = 0;
			int obstacleType = -1;
			for(int i = 0; i < 40 && d < distance;  i++, tempVector1.add(tempVector2), d+=0.125){
				Model temp = ObstacleMap.isOccupied2(tempVector1);
				if(temp == null)
					continue;
				obstacleType = temp.getTypeOfModel();
				if(obstacleType == 1){
					break;
				}else{
					clearToShoot = false;
					break;
				}
				
			}
			
			//find the angle between target and itself
			if(clearToShoot){
				targetAngle = 90 + (int)(180 * Math.atan((turretCenter.z - PlayerTank.bodyCenter.z)/(turretCenter.x - PlayerTank.bodyCenter.x)) / Math.PI);
				if(PlayerTank.bodyCenter.x > turretCenter.x  && targetAngle <= 180)
					targetAngle+=180;

			}else{
				targetAngle = bodyAngle;
				
			}
			
			//cauculate the difference between those 2 angles
			int AngleDelta = turretAngle - targetAngle;
			if(Math.abs(AngleDelta) < 3 && clearToShoot && distance < 2.5)
				firing = true;
			
			
			//aim at a target angle
			if(AngleDelta > 0){
				if(AngleDelta < 180)
					aimRight = true;
				else
					aimLeft = true;
			}
			else if(AngleDelta < 0){
				if(AngleDelta > -180)
					aimLeft = true;
				else 
					aimRight = true;
			}
			
			//move to a  target location 
			//SSML tank will move towards player tank's position until distance is less than 2.5, or it detects 
			//a type 2 obstacle between itself and the player's tank
			forward = true;
			if(clearToShoot && distance <3){
				if(distance < 2.49)
					forward = false;
				
			}
			
			if(isBlockingOtherModel && distance > 0.8){
				forward = true;
				
			}
			
			if(forward){
				targetAngleBody = 90 + (int)(180 * Math.atan((centreModel.z - PlayerTank.bodyCenter.z)/(centreModel.x - PlayerTank.bodyCenter.x)) / Math.PI);
				if(PlayerTank.bodyCenter.x > centreModel.x  && targetAngleBody <= 180)
					targetAngleBody+=180;
				
				//the enemy tank will occasionly (~once every 10 secs)perfom a 90 degree change in moving angle if:
				//1. it cant see the target tank and the target thatis within 1.2 unit away
				//2. it got stucked and the target that is within 1.2 units away
				//3. blocked by a wall
				
				if(!clearToShoot && (distance < 1.2 || (obstacleType == 6 && distance < 2.5)) || stuckCount ==10){
					
					if(stuckCount == 10){
						if(randomNumber2 > 50)
							randomNumber2 = 50;
						else
							randomNumber2 = 51;
						stuckCount=0;
					}
						
					if(randomNumber2 > 50)
						targetAngleBody += 90;
					else
						targetAngleBody -= 90;
					
					
					targetAngleBody = (targetAngleBody + 360)%360;
				}
				
				//check whether the next move will embed into obstacles
				displacement.set(0,0,0.01);
				displacement.rotate_XZ(targetAngleBody);
				boundaryModel2D.update(displacement);
				int newPosition = (int)(boundaryModel2D.xPos*4) + (129-(int)(boundaryModel2D.yPos*4))*80;
				boolean canMove = true;
				//test againt type 1 & 2 obstacles
				if(ObstacleMap.collideWithObstacle1(this, newPosition)){
					forward = false;
					canMove = false;
				}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
					forward = false;
					canMove = false;
				}
				displacement.scale(-1);
				boundaryModel2D.update(displacement);
				displacement.reset();
				
				
				if(!canMove){
					if(isBlockingOtherModel){
						ObstacleMap.giveWay(this ,position);
					}
					
					
					//change direction if unable to move with current direction
					targetAngleBody = targetAngle;
					//generate 2 new directions
					int angle1 = targetAngleBody - targetAngleBody%90;
					int angle2 = angle1 + 90;
					
					
					angle2 = angle2%360;
				
					
					
					boolean canMoveAngle1 = true;
					boolean canMoveAngle2 = true;
				
				
					
					//check if tank is able to move freely at angle 1
					displacement.set(0,0,0.01);
					displacement.rotate_XZ(angle1);
					boundaryModel2D.update(displacement);
					newPosition = (int)(boundaryModel2D.xPos*4) + (129-(int)(boundaryModel2D.yPos*4))*80;
					//test againt type 1 & 2 obstacles
					if(ObstacleMap.collideWithObstacle1(this, newPosition)){
						canMoveAngle1 = false;
					}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
						canMoveAngle1 = false;
					}
					displacement.scale(-1);
					boundaryModel2D.update(displacement);
					displacement.reset();
					
					//check if tank is able to move freelly at angle 2
					displacement.set(0,0,0.01);
					displacement.rotate_XZ(angle2);
					boundaryModel2D.update(displacement);
					newPosition = (int)(boundaryModel2D.xPos*4) + (129-(int)(boundaryModel2D.yPos*4))*80;
					//test againt type 1 & 2 obstacles
					if(ObstacleMap.collideWithObstacle1(this, newPosition)){
						canMoveAngle2 = false;
					}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
						canMoveAngle2 = false;
					}
					displacement.scale(-1);
					boundaryModel2D.update(displacement);
					displacement.reset();
					
				
					
					//move to a valid direction.  
					//if both directions are valid,  then move to the direction that is closer to  targetAngleBody
					//if neither direction is valid, then update the AI status to "stucked"
					if(canMoveAngle1 && !canMoveAngle2){
						targetAngleBody = angle1;
						forward = true;
						stuckCount = 0;
						ObstacleMap.giveWay(this, position);
					}else if(!canMoveAngle1 && canMoveAngle2){
						targetAngleBody = angle2;
						forward = true;
						stuckCount = 0;
						ObstacleMap.giveWay(this, position);
					}else if(canMoveAngle1 && canMoveAngle2){
						if(Math.abs(angle1 - targetAngleBody) < Math.abs(angle2 - targetAngleBody)){
							targetAngleBody = angle1;
							
						}else{
							targetAngleBody = angle2;
							
						}		
						forward = true;
						stuckCount = 0;
						
					}else{
						
						//got stucked!!
						stuckCount++;
						
						//tell surrounding units to move away
						ObstacleMap.giveWay(this, position);
						
						
						
					}
					
					if((previousTargetAngleBody + 180)%360 == targetAngleBody){
						targetAngleBody = previousTargetAngleBody;
					}
					
					
				}
				//double check whether the move is valid
				displacement.set(0,0,0.01);
				displacement.rotate_XZ(targetAngleBody);
				boundaryModel2D.update(displacement);
				newPosition = (int)(boundaryModel2D.xPos*4) + (129-(int)(boundaryModel2D.yPos*4))*80;
				
				//test againt type 1 & 2 obstacles
				if(ObstacleMap.collideWithObstacle1(this, newPosition)){
					forward = false;
					
				}else if(ObstacleMap.collideWithObstacle2(this, newPosition)){
					forward = false;
					
				}
				displacement.scale(-1);
				boundaryModel2D.update(displacement);
				displacement.reset();
			}
		}
		previousTargetAngleBody = targetAngleBody;
	}
	
	//draw model
	public void draw(){
		if(countDownToDeath <3){
			for(int i = 0; i < polygons.length; i++)
				polygons[i].draw();
		}
		
		//draw smoke tail
		if(Smoke != null && isVisible)
			Smoke.draw();
	}
	
	//return the 2D boundary of this model
	public Rectangle2D getBoundary2D(){
		return boundaryModel2D;
	}
	
	public void damage(int damagePoint){
		
		if(damagePoint == -1){
			active = true;
			return;
		}
		HP-=damagePoint;
		engaged = true;
	}
}
