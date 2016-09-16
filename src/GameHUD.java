package src;
//this class produce text images
public class GameHUD {
	
	//background window frame
	public static int[] leftSide;
	public static int[] rightSide;
	
	//string represented in number format
	public static int[] Health, Ammo, BattleTank2, NewGame, Controls, AboutAuthor, controlDescriPlayerTankion1,
	controlDescriPlayerTankion2, controlDescriPlayerTankion3, controlDescriPlayerTankion4, controlDescriPlayerTankion5, controlDescriPlayerTankion6,
	controlDescriPlayerTankion7, controlDescriPlayerTankion8, controlDescriPlayerTankion9, author1, author2,author3,author4,author5
	,author6, author7, author8, author9, author10, game,over, You, Won, cheat1, cheat2, cheat3;

	public static int menuOPlayerTankionStatus;
	
	public static int gameoverMessagePosition, winMessagePosition; 
	
	public static int loadingScreenPosition;
	
	public static boolean escapePressed, downPressed, upPressed, enterPressed; 
	
	public static int[] randomDisplacement = new int[640];
	
	public static boolean mousePressed;
	
	public static int mouseXpos, mouseYpos;
	
	
	public static void init(){
		menuOPlayerTankionStatus = 1;
		gameoverMessagePosition = 0;
		winMessagePosition = 0;
		loadingScreenPosition = 1234567;
		
		
		for(int i = 0; i < 640; i+=2){
			int random = GameData.getRandom()/10;
			randomDisplacement[i] = random;
			randomDisplacement[i+1] = random;
			
		}
		
		
		leftSide = new int[]{54,53,52,51, 51,52,53,54};
		rightSide = new int[]{196,197,198,199, 199,198,197,196};
		
		Health = new int[]{17, 40,36, 47,55,43};
		Ammo = new int[]{10, 48, 48, 50};
		BattleTank2 = new int[]{11, 36, 55, 55, 47,40, -1, 29,36,49,46, -1, 2};
		NewGame = new int[]{23,40,58, -1, 16, 36,48,40};
		Controls = new int[]{12, 50, 49,55,53,50,47,54};
		AboutAuthor = new int[]{10,37,50,56,55, -1, 36,56,55,43,50,53};
		
		//Press w s a d to move around.
		//Change camera view  
		//by moving the mouse
		//cursor.
		//Click mouse button to fire.
		//press 1 2 3 4 to switch guns.
		
		controlDescriPlayerTankion1 = new int[]{25,53,40,54,54, -1, 58, -1, 54, -1, 36, -1, 39, -1, 55,50, -1};
		controlDescriPlayerTankion2 = new int[]{48,50,57,40, -1, 36,53,50,56,49,39, 63};
		controlDescriPlayerTankion3 = new int[]{12,43,36,49,42,40, -1, 38,36,48,40,53,36, -1, 57,44,40,58};
		controlDescriPlayerTankion4 = new int[]{37,60, -1, 48,50,57,44,49,42, -1, 55,43,40, -1, 48,50,56,54,40};
		controlDescriPlayerTankion5 = new int[]{38,56,53,54,50,53, 63};
		controlDescriPlayerTankion6 = new int[]{12,47,44,38,46, -1, 48,50,56,54,40, -1,37,56,55,55,50,49};
		controlDescriPlayerTankion7 = new int[]{55,50, -1, 41, 44,53,40, 63};
		controlDescriPlayerTankion8 = new int[]{25,53,40,54,54, -1, 1, -1, 2, -1, 3, -1, 4, -1, 55,50};
		controlDescriPlayerTankion9 = new int[]{54,58,44,55,38,43, -1, 42,56,49,54, 63};
		
		//My name is Hu Pan. 
		//I am interested in
		//developing games 
		//and cool animation
		//in java. If you 
		//find any bugs
		//or if you have any
		//suggestions please
		//let me know.
		//hupan84@hotmail.com
		
		author1 = new int[]{22, 60, -1, 49, 36, 48, 40, -1, 44, 54, -1, 17, 56, -1, 25, 36, 49, 63};
		author2 = new int[]{18, -1, 36, 48, -1, 44,49,55,40,53,40,54,55,40,39, -1, 44,49};
		author3 = new int[]{39,40,57,40, 47, 50, 51, 44,49,42, -1, 42, 36, 48, 40, 54};
		author4 = new int[]{36, 49, 39, -1, 38, 50,50, 47, -1, 36,49,44,48,36,55,44,50,49};
		author5 = new int[]{44, 49, -1, 45,36,57,36, -1, 63, 18, 41, -1, 60, 50, 56};
		author6 = new int[]{41,44,49, 39, -1, 36, 49, 60, -1, 37,56,42,54};
		author7 = new int[]{50, 53, -1, 44, 41, -1, 60, 50, 56, -1, 43,36,57, 40, -1, 36, 49, 60};
		author8 = new int[]{54,56,42,42,40,54,55,44,50,49,54, -1, 51, 47, 40,36,54,40};
		author9 = new int[]{47, 40,55, -1, 48, 40, -1, 46, 49, 50, 58, 63};
		author10 = new int[]{43, 56, 51,36,49,8,4,62,43,50,55,48,36,44,47,63,38,50,48};
	
		//Game Over
		game = new int[]{16,36,48,40};
		over = new int[]{24,57,40,53};
		
		//wining message
		You  = new int[]{34,50,56};
		Won = new int[]{32, 50, 49};
		
		//cheat message
		//Thanks for playing, next time hold 
		//I, L, M keys at the same time for 
		//unlimited ammo and health.
		cheat1 = new int[]{29,43,36,49,46,54,-1,41,50,53,-1,51,47,36,60,44,49,42,63, 23,40,59,55,-1,55,44,48,40,-1,43,50,47,39};
		cheat2 = new int[]{18, -1,21,-1,22,-1,46,40,60,54,-1,36,55,-1,55,43,40,-1,54,36,48,40,-1,55,44,48,40,-1,41,50,53};
		cheat3 = new int[]{56,49,47,44,48,44,55,40,39,-1,36,48,48,50,-1,36,49,39,-1,43,40,36,47,55,43};
	}
	
	//letters are stored in this order:
	//0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17 18 19 20 21 22 23 24 25 26 27 28 29 30 31 32 33 34 35 
	//0 1 2 3 4 5 6 7 8 9 A  B  C  D  E  F  G  H  I  J  K  L  M  N  O  P  Q  R  S  T  U  V  W  X  Y  Z

	//36 37 38 39 40 41 42 43 44 45 46 47 48 49 50 51 52 53 54 55 56 57 58 59 60 61 62 63
	//a  b  c  d  e  f  g  h  i  j  k  l  m  n  o  p  q  r  s  t  u  v  w  x  y  z  @  .
	
	public static void draw(){
		if(Main.gameOver){
			//draw game over message
			drawGameoverMessage();
		}
		
		if(Main.win){
			//draw wining message
			drawWinMessage();
		}
		
		if(Main.gamePaused == false && !Main.gameNotStart)
			drawInfo();
		
		if(Main.gamePaused == true || (Main.gameNotStart && menuOPlayerTankionStatus != 0)){
			drawMenu();
			Main.copyScreen();
		}
		
		
		
		
		if(loadingScreenPosition < 800){
			
			
			int startPosition = loadingScreenPosition * 640;
			int currentPosition = 0;
			for(int i = 0; i < 640; i++){
				for(int j = 0; j < 480; j++){
					currentPosition = startPosition  + (j+ randomDisplacement[i])*640 + i;
					if(currentPosition >= 640 * 480){
						currentPosition = 640*479 + i;
					}
					Main.screen[currentPosition] = Main.stencilBuffer2[j*640 + i];
				}
			}
			
			loadingScreenPosition+=15;
		}
		
		
		
	}
	
	//handle game menu interactions
	public static void update(){
		if(escapePressed){
			if(menuOPlayerTankionStatus == 0){
				Main.gamePaused = true;
				menuOPlayerTankionStatus = 1;
			}else if(menuOPlayerTankionStatus == 1 || menuOPlayerTankionStatus == 2 || menuOPlayerTankionStatus == 3){
				Main.gamePaused = false;
				menuOPlayerTankionStatus = 0;
			}else if(menuOPlayerTankionStatus == 4 || menuOPlayerTankionStatus == 5){
				menuOPlayerTankionStatus = 1;
			}
			
		}
		
		if(downPressed){
			if(menuOPlayerTankionStatus == 1)
				menuOPlayerTankionStatus = 2;
			else if(menuOPlayerTankionStatus == 2)
				menuOPlayerTankionStatus = 3;
			else if(menuOPlayerTankionStatus == 3)
				menuOPlayerTankionStatus = 1;
			
		}
		
		if(upPressed){
			if(menuOPlayerTankionStatus == 1)
				menuOPlayerTankionStatus = 3;
			else if(menuOPlayerTankionStatus == 2)
				menuOPlayerTankionStatus = 1;
			else if(menuOPlayerTankionStatus == 3)
				menuOPlayerTankionStatus = 2;
		}
		
		if(enterPressed){
			if(menuOPlayerTankionStatus == 1){
				
				menuOPlayerTankionStatus = 0;
				gameoverMessagePosition = 0;
				winMessagePosition = 0;
				loadingScreenPosition = 0;
				ObstacleMap.clear();
				
				Camera.viewDirection = new Vector(0,0,1);
				Camera.XZ_angle = 0;
				Camera.restartCameraPosition = true;
				
				PowerUps.init();
				Projectiles.init();
				Enemies.init();
				ObstacleMap.removeObstacle2(Main.PlayerTank.position);
				Main.PlayerTank = new PlayerTank(10,-0.975,2.5);	
				Main.gameNotStart = false;
				Main.gamePaused = false;
				Main.gameOver = false;
				Main.win = false;
				
			}else if(menuOPlayerTankionStatus == 2){
				menuOPlayerTankionStatus = 4;
			}else if(menuOPlayerTankionStatus == 3){
				menuOPlayerTankionStatus = 5;
			}
		}
		
		if(mousePressed){
			
			if(menuOPlayerTankionStatus == 4 || menuOPlayerTankionStatus == 5){
				
				menuOPlayerTankionStatus = 1;
				
				mouseXpos = -1234567;
				mouseYpos = -1234567;
				return;
			}
			
				
			if(mouseXpos > 264 && mouseXpos < 385 && mouseYpos > 120 && mouseYpos < 144 && !(!Main.gamePaused && !Main.gameNotStart)){
				menuOPlayerTankionStatus = 0;
				gameoverMessagePosition = 0;
				winMessagePosition = 0;
				loadingScreenPosition = 0;
				ObstacleMap.clear();
				
				Camera.viewDirection = new Vector(0,0,1);
				Camera.XZ_angle = 0;
				Camera.restartCameraPosition = true;
				
				PowerUps.init();
				Projectiles.init();
				Enemies.init();
				ObstacleMap.removeObstacle2(Main.PlayerTank.position);
				Main.PlayerTank = new PlayerTank(10,-0.975,2.5);	
				Main.gameNotStart = false;
				Main.gamePaused = false;
				Main.gameOver = false;
				Main.win = false;
			}
			
			if(mouseXpos > 262 && mouseXpos < 385 && mouseYpos > 161 && mouseYpos < 183)
				menuOPlayerTankionStatus = 2;
				
			if(mouseXpos > 234 && mouseXpos < 412 && mouseYpos > 197 && mouseYpos < 222)
				menuOPlayerTankionStatus = 3;
			
				
			if(menuOPlayerTankionStatus == 2){
				menuOPlayerTankionStatus = 4;
			}else if(menuOPlayerTankionStatus == 3){
				menuOPlayerTankionStatus = 5;
			}
			
		}
		
		
		escapePressed = false;
		downPressed = false;
		upPressed = false;
		enterPressed = false;
		mousePressed = false;
		
		
	}
	
	public static void drawGameoverMessage(){
	
		
		if(gameoverMessagePosition < 500)
			gameoverMessagePosition+=6;
		
		if(gameoverMessagePosition > 310){
		
			TextFactory.draw(game, gameoverMessagePosition -310, 190, 2, 0x00eec64d);
			TextFactory.draw(over, 527 - gameoverMessagePosition + 310, 190, 2, 0x00eec64d);
		}
	}
	
	public static void drawWinMessage(){
		if(winMessagePosition < 500)
			winMessagePosition+=6;
		else{
			TextFactory.draw(cheat1, 80, 220, 1, 0x005d07f7);
			TextFactory.draw(cheat2, 80, 240, 1, 0x005d07f7);
			TextFactory.draw(cheat3, 80, 260, 1, 0x005d07f7);
		}
		
		if(winMessagePosition > 310){
			TextFactory.draw(You, winMessagePosition -310, 170, 2, 0x00ed07b7);
			TextFactory.draw(Won, 527 - winMessagePosition + 310, 170, 2, 0x00ed07b7);
		}
		
	}
	
	public static void drawMenu(){
		int[] screen = Main.screen;
		int yPos = 50;
		int index = 0;
		int pixel;
		int r,g,b;
		
		//draw menu back ground
		for(int i = 0; i < 4; i++){
			for(int j = leftSide[i]+ 150; j < rightSide[i] + 290; j++){
				index = yPos*640 + j - 20;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		int loweryPos = yPos + 330;
		for(int i = yPos; i < loweryPos; i++){
			for(int j = 200; j < 490; j++){
				index = yPos*640 + j - 20;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		for(int i = 4; i < 8; i++){
			for(int j = leftSide[i] + 150; j < rightSide[i] + 290; j++){
				index = yPos*640 + j - 20;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		int color  = 0x00d0d0d0;
		TextFactory.draw(BattleTank2, 222, 57, 1, color);
		
		if(menuOPlayerTankionStatus > 0 && menuOPlayerTankionStatus <=3){
			
			if(menuOPlayerTankionStatus == 1)
				color =  0x0055ffaa;
			else
				color  = 0x00d0d0d0;
			TextFactory.draw(NewGame, 265, 120, 1, color);
			
			if(menuOPlayerTankionStatus == 2)
				color =  0x0055ffaa;
			else
				color  = 0x00d0d0d0;
			TextFactory.draw(Controls, 265, 160, 1, color);
			
			if(menuOPlayerTankionStatus == 3)
				color =  0x0055ffaa;
			else
				color  = 0x00d0d0d0;
			TextFactory.draw(AboutAuthor, 235, 200, 1, color);	
		}
		
		if(menuOPlayerTankionStatus == 4){
			color =  0x0055ffaa;
			TextFactory.draw(controlDescriPlayerTankion1, 183, 100, 1, color);	
			TextFactory.draw(controlDescriPlayerTankion2, 183, 122, 1, color);	
			TextFactory.draw(controlDescriPlayerTankion3, 183, 160, 1, color);
			TextFactory.draw(controlDescriPlayerTankion4, 183, 177, 1, color);
			TextFactory.draw(controlDescriPlayerTankion5, 183, 199, 1, color);	
			TextFactory.draw(controlDescriPlayerTankion6, 183, 237, 1, color);
			TextFactory.draw(controlDescriPlayerTankion7, 183, 259, 1, color);
			TextFactory.draw(controlDescriPlayerTankion8, 183, 297, 1, color);
			TextFactory.draw(controlDescriPlayerTankion9, 183, 319, 1, color);
		}
		
		if(menuOPlayerTankionStatus == 5){
			color =  0x0055ffaa;
			TextFactory.draw(author1, 183, 100, 1, color);	
			TextFactory.draw(author2, 183, 122, 1, color);	
			TextFactory.draw(author3, 183, 144, 1, color);	
			TextFactory.draw(author4, 183, 166, 1, color);	
			TextFactory.draw(author5, 183, 188, 1, color);	
			TextFactory.draw(author6, 183, 210, 1, color);	
			TextFactory.draw(author7, 183, 232, 1, color);	
			TextFactory.draw(author8, 183, 254, 1, color);	
			TextFactory.draw(author9, 183, 276, 1, color);
			TextFactory.draw(author10, 183, 320, 1, color);	
			
		}
		
		
	}
	
	public static void drawInfo(){
		int[] screen = Main.screen;
		int yPos = 420;
		int index = 0;
		int pixel;
		int r,g,b;
		
		//draw health info
		for(int i = 0; i < 4; i++){
			for(int j = leftSide[i]; j < rightSide[i]; j++){
				index = yPos*640 + j - 20;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		int loweryPos = yPos + 30;
		for(int i = yPos; i < loweryPos; i++){
			for(int j = 50; j < 200; j++){
				index = yPos*640 + j - 20;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		for(int i = 4; i < 8; i++){
			for(int j = leftSide[i]; j < rightSide[i]; j++){
				index = yPos*640 + j - 20;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		Integer integer = new Integer((int)((double)Main.PlayerTank.HP *100/150));
		TextFactory.drawString(integer.toString(), 134, 427, 1, 0x00d0d0d0);
		TextFactory.draw(Health, 32, 427, 1, 0x00d0d0d0);
		
		//draw ammo info
		yPos = 420;
		for(int i = 0; i < 4; i++){
			for(int j = leftSide[i]; j < rightSide[i]; j++){
				index = yPos*640 + j + 415;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		loweryPos = yPos + 30;
		for(int i = yPos; i < loweryPos; i++){
			for(int j = 50; j < 200; j++){
				index = yPos*640 + j + 415;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		for(int i = 4; i < 8; i++){
			for(int j = leftSide[i]; j < rightSide[i]; j++){
				index = yPos*640 + j + 415;
				pixel = screen[index];
				r = (pixel & 0x00ff0000) >> 17;
				g = (pixel & 0x0000ff00) >> 9;
				b = (pixel & 0x000000ff) >> 1;
				pixel = b+ (g << 8) + (r << 16);
				screen[index] = pixel;
			}
			yPos++;
		}
		
		
		integer = new Integer(Main.PlayerTank.currentAmmo);
		TextFactory.drawString(integer.toString(), 554, 427, 1, 0x00d0d0d0);
		TextFactory.draw(Ammo, 482, 427, 1, 0x00d0d0d0);
		
		
	}
	
	
	
	
	
}
