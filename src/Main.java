package src;
//made by Hu Pan
//at 9/8/2008
//for more games visit www.freewebs.com/phu004

import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.awt.image.*;

public class Main extends Applet implements KeyListener, ActionListener, MouseMotionListener, MouseListener{
	
	private static final long serialVersionUID = 1L;
	public Ticker ticker;
	public int sleePlayerTankime;
	public static int[] screen;
	public static int[] stencilBuffer;
	public static int[] stencilBuffer2;
	public static short[] lightMap;
	public BufferedImage doubleBuffer;
	public camera Camera;
	public InputHandler myInputHandler;
	public static Texture[] textures;
	public static boolean[] terrainBuffer;
	public static boolean terrainBufferFlag;
	public static int polyCount;
	
	public static Terrain Terrain;
	
	public static int timer;
	public static long lastTime;
	public static long tm;
	
	public static PlayerTank PlayerTank;
	
	//flag which indicate whether the user has terminated the current applet
	public static boolean appletDestoried;
	
	//game status
	public static boolean gameNotStart, gamePaused, gameOver, win;
	
	public void init(){
		gameNotStart = true;
		appletDestoried = false;
		screen = null;
		stencilBuffer = null;
		lightMap = null;
		textures = null;
		
		Camera = null;
		Terrain = null;
		GameData.destory();
		terrainBuffer = null;
		System.gc();
		
		//Make an array of int which holds screen pixels data
		doubleBuffer =  new BufferedImage(640, 480, BufferedImage.TYPE_INT_RGB);
		DataBuffer dest = doubleBuffer.getRaster().getDataBuffer();
		screen = ((DataBufferInt)dest).getData();
		
		//create buffers which will temporally hold a proportion of the screen buffer
		stencilBuffer = new int[640*480];
		stencilBuffer2 = new int[640*480];
		
		//make an array of short that hold all the lighting information on the terrain
		lightMap = new short[640*480];
		
		//The terrain buffer indicates whether a particular pixel is already filled by a terrain
		//polygon during a game frame
		terrainBuffer = new boolean[640*480];
	
		//Load textures
		String imageFolder = "Image" + System.getProperty("file.separator");
		textures = new Texture[63];
		textures[0] = new Texture(getImage(getDocumentBase(), imageFolder + "1.jpg"), 9, 9, "normal");
		textures[1] = new Texture(getImage(getDocumentBase(), imageFolder + "3.jpg"), 8, 8, "normal");
		textures[2] = new Texture(getImage(getDocumentBase(), imageFolder + "1.jpg"), 9, 9, "beachSlope");
		textures[3] = new Texture(getImage(getDocumentBase(), imageFolder + "1.jpg"), 9, 9, "oceanFloor");
		textures[4] = new Texture(getImage(getDocumentBase(), imageFolder + "5.jpg"), 7, 7, "water");
		textures[5] = new Texture(getImage(getDocumentBase(), imageFolder + "4.jpg"), 7, 7, "normal");
		textures[6] = new Texture(getImage(getDocumentBase(), imageFolder + "6.jpg"), 8, 8, "normal");
		textures[7] = new Texture(getImage(getDocumentBase(), imageFolder + "7.jpg"), 8, 8, "normal");
		textures[8] = new Texture(getImage(getDocumentBase(), imageFolder + "8.jpg"), 7, 7, "shadow");
		textures[9] = new Texture(getImage(getDocumentBase(), imageFolder + "9.jpg"), 5, 7, "normal");
		textures[10] = new Texture(getImage(getDocumentBase(), imageFolder + "10.jpg"), 7, 7, "shadow");
		textures[11] = new Texture(getImage(getDocumentBase(), imageFolder + "11.jpg"), 6, 6, "normal");
		textures[12] = new Texture(getImage(getDocumentBase(), imageFolder + "12.jpg"), 8, 6, "normal");
		textures[13] = new Texture(getImage(getDocumentBase(), imageFolder + "13.jpg"), 6, 6, "normal");
		textures[14] = new Texture(getImage(getDocumentBase(), imageFolder + "14.jpg"), 7, 7, "shadow");
		textures[15] = new Texture(getImage(getDocumentBase(), imageFolder + "15.jpg"), 7, 7, "shadow");
		textures[16] = new Texture(getImage(getDocumentBase(), imageFolder + "16.jpg"), 1, 1, "normal");
		textures[17] = new Texture(getImage(getDocumentBase(), imageFolder + "17.jpg"), 8, 8, "explosion");
		textures[18] = new Texture(getImage(getDocumentBase(), imageFolder + "18.jpg"), 8, 8, "explosion");
		textures[19] = new Texture(getImage(getDocumentBase(), imageFolder + "19.jpg"), 8, 8, "explosion");
		textures[20] = new Texture(getImage(getDocumentBase(), imageFolder + "20.jpg"), 8, 8, "explosion");
		textures[21] = new Texture(getImage(getDocumentBase(), imageFolder + "2.jpg"), 8, 8, "light");
		textures[22] = new Texture(getImage(getDocumentBase(), imageFolder + "21.jpg"), 6, 6, "normal");
		textures[23] = new Texture(getImage(getDocumentBase(), imageFolder + "22.jpg"), 6, 6, "normal");
		textures[24] = new Texture(getImage(getDocumentBase(), imageFolder + "23.jpg"), 6, 6, "normal");
		textures[25] = new Texture(getImage(getDocumentBase(), imageFolder + "24.jpg"), 1, 1, "normal");
		textures[26] = new Texture(getImage(getDocumentBase(), imageFolder + "25.jpg"), 1, 1, "normal");
		textures[27] = new Texture(getImage(getDocumentBase(), imageFolder + "26.jpg"), 6, 6, "normal");
		textures[28] = new Texture(getImage(getDocumentBase(), imageFolder + "27.jpg"), 6, 6, "normal");
		textures[29] = new Texture(getImage(getDocumentBase(), imageFolder + "28.jpg"), 6, 6, "normal");
		textures[30] = new Texture(getImage(getDocumentBase(), imageFolder + "29.jpg"), 6, 6, "normal");
		textures[31] = new Texture(getImage(getDocumentBase(), imageFolder + "30.jpg"), 6, 6, "normal");
		textures[32] = new Texture(getImage(getDocumentBase(), imageFolder + "31.jpg"), 1, 1, "normal");
		textures[33] = new Texture(getImage(getDocumentBase(), imageFolder + "32.jpg"), 6, 6, "normal");
		textures[34] = new Texture(getImage(getDocumentBase(), imageFolder + "33.jpg"), 1, 1, "normal");
		textures[35] = new Texture(getImage(getDocumentBase(), imageFolder + "34.jpg"), 6, 6, "normal");
		textures[36] = new Texture(getImage(getDocumentBase(), imageFolder + "35.jpg"), 7, 7, "shadow");
		textures[37] = new Texture(getImage(getDocumentBase(), imageFolder + "36.jpg"), 7, 7, "shadow");
		textures[38] = new Texture(getImage(getDocumentBase(), imageFolder + "37.jpg"), 6, 6, "normal");
		textures[39] = new Texture(getImage(getDocumentBase(), imageFolder + "38.jpg"), 1, 1, "normal");
		textures[40] = new Texture(getImage(getDocumentBase(), imageFolder + "40.jpg"), 8, 8, "normal");
		textures[41] = new Texture(getImage(getDocumentBase(), imageFolder + "41.jpg"), 7, 7, "normal");
		textures[42] = new Texture(getImage(getDocumentBase(), imageFolder + "42.jpg"), 7, 7, "shadow");
		textures[43] = new Texture(getImage(getDocumentBase(), imageFolder + "43.jpg"), 7, 7, "shadow");
		textures[44] = new Texture(getImage(getDocumentBase(), imageFolder + "44.jpg"), 7, 7, "shadow");
		textures[45] = new Texture(getImage(getDocumentBase(), imageFolder + "45.jpg"), 5, 5, "shadow");
		textures[46] = new Texture(getImage(getDocumentBase(), imageFolder + "46.jpg"), 5, 5, "shadow");
		textures[47] = new Texture(getImage(getDocumentBase(), imageFolder + "47.jpg"), 7, 7, "shadow");
		textures[48] = new Texture(getImage(getDocumentBase(), imageFolder + "48.jpg"), 6, 6, "normal");
		textures[49] = new Texture(getImage(getDocumentBase(), imageFolder + "49.jpg"), 1, 1, "normal");
		textures[50] = new Texture(getImage(getDocumentBase(), imageFolder + "50.jpg"), 7, 7, "shadow");
		textures[51] = new Texture(getImage(getDocumentBase(), imageFolder + "51.jpg"), 6, 6, "normal");
		textures[52] = new Texture(getImage(getDocumentBase(), imageFolder + "52.jpg"), 1, 1, "normal");
		textures[53] = new Texture(getImage(getDocumentBase(), imageFolder + "53.jpg"), 7, 7, "normal");
		textures[54] = new Texture(getImage(getDocumentBase(), imageFolder + "54.jpg"), 5, 5, "normal");
		textures[55] = new Texture(getImage(getDocumentBase(), imageFolder + "55.jpg"), 5, 5, "normal");
		textures[56] = new Texture(getImage(getDocumentBase(), imageFolder + "56.jpg"), 7, 7, "shadow");
		textures[57] = new Texture(getImage(getDocumentBase(), imageFolder + "57.jpg"), 6, 6, "smoke");
		textures[58] = new Texture(getImage(getDocumentBase(), imageFolder + "58.jpg"), 6, 6, "normal");
		textures[59] = new Texture(getImage(getDocumentBase(), imageFolder + "59.jpg"), 6, 6, "normal");
		textures[60] = new Texture(getImage(getDocumentBase(), imageFolder + "60.jpg"), 6, 6, "normal");
		textures[61] = new Texture(getImage(getDocumentBase(), imageFolder + "61.jpg"), 7, 7, "shadow");
		textures[62] = new Texture(getImage(getDocumentBase(), imageFolder + "62.jpg"), 8, 8, "normal");
		
		//Create look up tables
		GameData.makeData();
		
		//init text factory
		TextFactory.init();
		
		//init game hud
		GameHUD.init();
		
		//init camera
		Camera = new camera();
	
		//init terrain
		ObstacleMap.init();
		PowerUps.init();
		Terrain = new Terrain();
		
		//init projectiles, powerups
		Projectiles.init();
		
		//init player tank
		PlayerTank = new PlayerTank(1000,-0.975,2.5);	
		
		//init enemies
		Enemies.init();
		
		
		//Add key handler
		addKeyListener(this);
		addMouseMotionListener(this);
		addMouseListener(this);
		requestFocus();

		//Add ticker
		timer = 0;
		sleePlayerTankime = 35;
		tm = System.currentTimeMillis();
		ticker = new Ticker(sleePlayerTankime);
		ticker.addActionListener(this);
		
		ticker.start();
		
		System.out.println("Started!");
		System.gc();
	}
	
	//free resources when the browser decides to end the applet
	public void destroy(){
		ticker.stop();
		System.gc();
		
		appletDestoried = true;
	}
	
	//This method is called every time the ticker ticks (game loop)
	public final void actionPerformed(ActionEvent e){	
		if(appletDestoried){
			System.gc();
			return;
		}
		
		
		if(timer ==2)
			requestFocus();
		
		polyCount = 0;
		
		//cap frame rate to around 30
		timer++;
		tm+=sleePlayerTankime;	
		long temp = Math.max(0, tm - System.currentTimeMillis());
		if(temp == 0)
			temp = (long)(lastTime*0.5);
		if(temp > 33)
			temp = 33;
		ticker.setDelay((int)temp);
		lastTime = temp;
		
		//handle input
		InputHandler.handleInput();
		

		Camera.update();
		
		//update game components,process game logic, move things around etc...
		ModelDrawList.makeList();
		
		Terrain.update();
		PlayerTank.update();
		Enemies.update();
		Projectiles.update();
		PowerUps.update();
		GameEventHandler.processEvent();
	
		
		
	
		
		//draw terrain
		if(terrainBufferFlag == true)
			terrainBufferFlag = false;
		else
			terrainBufferFlag = true;
		Terrain.draw();
		
		//draw game components
		ModelDrawList.sort();
		ModelDrawList.draw();
		
	
		
		
		
		GameHUD.update();
		//draw game HUD
		GameHUD.draw();
		
		
	
		
		
		
		//copy the screen buffer to video memory
		if(this.getGraphics() != null)
			myPaint(this.getGraphics());
		
		
		
	}
	
	public final void myPaint(Graphics g){		
		//copy the pixel information to the video memory
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(doubleBuffer, null, 0, 0);
	}
	
	//this method is called by the browser when the applet window needs to refresh itself
	public final void paint(Graphics g){
		if(timer > 0)
			return;
		screen[0] =-134250;
		
		for(int i = 1; i < 307200; i+=i){
			System.arraycopy(screen, 0, screen, i, 307200 - i >= i ? i : 307200 - i);
		}
		myPaint(this.getGraphics());	
	}
	
	//copy screen content to a second stencilBuffer
	public static void copyScreen(){
		for(int i = 0; i < 640 * 480; i++){
			stencilBuffer2[i] = screen[i];
			
		}
	}
	
	//read keyboard inputs
	public final void keyPressed(KeyEvent e){
		
		InputHandler.keyPressed(e);
	}
	
	public final void keyReleased(KeyEvent e){
		InputHandler.keyReleased(e);
		
	}
	
	//read mouse input
	public final void mouseMoved(MouseEvent e){
		InputHandler.mouseMoved(e);
	}
	
	 public final void mouseDragged(MouseEvent e){
		 InputHandler.mouseDragged(e);
	 }
	
	
	
	 public final void mousePressed(MouseEvent e){
		 InputHandler.mousePressed(e);
		
		 
	 }
	 public final void mouseReleased(MouseEvent e){
		 InputHandler.mouseReleased(e);
			
	 }
	
	 public final void mouseEntered(MouseEvent e){
		 InputHandler.mouseEntered(e);
	 }
	 public final void mouseExited(MouseEvent e){
		 InputHandler.mouseExited(e);
	 }
	 
	 public final void keyTyped(KeyEvent e){
		
		 InputHandler.keyTyped(e);
	 }
   
	
	
	//unused method
	public final void update(Graphics g){}
   
  
    public final void mouseClicked(MouseEvent e){}
  
		
	

}