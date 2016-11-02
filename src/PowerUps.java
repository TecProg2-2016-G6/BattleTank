package src;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.directory.InvalidAttributeValueException;

//This class stores powerUp objects
public class PowerUps {
	
	public static PowerUp[] PU;
	
	private static final Logger LOGGER = Logger.getLogger( PowerUps.class.getName() );
	
	public static void init(){
		
		PU = new PowerUp[100];
		
		// Register Power Ups 
		register(new PowerUp(18.15,-0.875, 16.575, 4));
		register(new PowerUp(18.6,-0.875, 16.575, 4));
		register(new PowerUp(18.6,-0.875, 16.15, 4));
		register(new PowerUp(18.15,-0.875, 16.15, 4));
		register(new PowerUp(18.37,-0.875, 16.37, 4));
		register(new PowerUp(4.15,-0.875, 14.175, 4));
		register(new PowerUp(4.6,-0.875, 14.175, 4));
		register(new PowerUp(4.6,-0.875, 13.65, 4));
		register(new PowerUp(4.15,-0.875, 13.65, 4));
		register(new PowerUp(4.37,-0.875, 13.87, 4));
	}
	
	public static void update(){
		
		// PowerUp 
		for(int i = 0; i < PU.length; i++){
			if(PU[i] != null){
				PU[i].update();
				if(Rectangle2D.testIntersection(PU[i].boundaryModel2D, Main.PlayerTank.boundaryModel2D)){
					
					// PowerUp for shell type
					if(PU[i].type == 1){
						if(Main.PlayerTank.shells == 0){
							Main.PlayerTank.shells +=10;
							try {
								Main.PlayerTank.changeWeapon(1);
							} catch (InvalidAttributeValueException e) {
								LOGGER.log(Level.WARNING, "Invalid Weapon Value");
							}
						}else{
							Main.PlayerTank.shells +=10;
						}
					}
					
					// PowerUp for rocket type
					if(PU[i].type == 2){
						if(Main.PlayerTank.rockets == 0){
							Main.PlayerTank.rockets +=10;
							try {
								Main.PlayerTank.changeWeapon(2);
							} catch (InvalidAttributeValueException e) {
								LOGGER.log(Level.WARNING, "Invalid Weapon Value");
							}
							
						}else{
							Main.PlayerTank.rockets +=10;
						}
					}
					
					// PowerUp for slug type
					if(PU[i].type == 3){
						if(Main.PlayerTank.slugs == 0){
							Main.PlayerTank.slugs +=10;
							try {
								Main.PlayerTank.changeWeapon(3);
							} catch (InvalidAttributeValueException e) {
								LOGGER.log(Level.WARNING, "Invalid Weapon Value");
							}
						}else{
							Main.PlayerTank.slugs +=10;
						}
					}
					
					// PowerUp for plasma type
					if(PU[i].type == 4){
						if(Main.PlayerTank.plasma == 0){
							Main.PlayerTank.plasma +=10;
							try {
								Main.PlayerTank.changeWeapon(4);
							} catch (InvalidAttributeValueException e) {
								LOGGER.log(Level.WARNING, "Invalid Weapon Value");
							}
						}else{
							Main.PlayerTank.plasma +=10;
						}
					}
						
						
					PU[i] = null;
					
				}
			}
		}
	}
	
	// Add new projectile to the PowerUp Array
	public static void register(PowerUp P){
		for(int i = 0; i < PU.length; i++){
			if(PU[i] == null){
				PU[i] = P;
				return;
			}
		}
	}
}
