package src;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.directory.InvalidAttributeValueException;

//This class stores powerUp objects
public class PowerUps {
	
	public static PowerUp[] powerUpVector;
	
	private static final Logger LOGGER = Logger.getLogger( PowerUps.class.getName() );
	
	public static void init(){
		
		powerUpVector = new PowerUp[100];
		
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
		for(int i = 0; i < powerUpVector.length; i++){
			
			if(powerUpVector[i] != null){
				powerUpVector[i].update();
				upUnitPower(powerUpVector[i]);
				powerUpVector[i] = null;
			}
		}
	}
	
	private static void upUnitPower(PowerUp powerUp){
		
		if(Rectangle2D.testIntersection(powerUp.boundaryModel2D, Main.PlayerTank.boundaryModel2D)){
			
			switch (powerUp.type) {
			// PowerUp for shell type
			case 1:
				if(PlayerTank.shells == 0){
					PlayerTank.shells +=10;
					try {
						Main.PlayerTank.changeWeapon(1);
					} catch (InvalidAttributeValueException invalidAttributeValueException) {
						LOGGER.log(Level.WARNING, "Invalid Weapon Value", invalidAttributeValueException); //$NON-NLS-1$
					}
				}else{
					PlayerTank.shells +=10;
				}
				break;
			// PowerUp for rocket type
			case 2:
				if(PlayerTank.rockets == 0){
					PlayerTank.rockets +=10;
					try {
						Main.PlayerTank.changeWeapon(2);
					} catch (InvalidAttributeValueException invalidAttributeValueException) {
						LOGGER.log(Level.WARNING, "Invalid Weapon Value", invalidAttributeValueException); //$NON-NLS-1$
					}
					
				}else{
					PlayerTank.rockets +=10;
				}
				break;
			// PowerUp for slug type
			case 3:
				if(PlayerTank.slugs == 0){
					PlayerTank.slugs +=10;
					try {
						Main.PlayerTank.changeWeapon(3);
					} catch (InvalidAttributeValueException invalidAttributeValueException) {
						LOGGER.log(Level.WARNING, "Invalid Weapon Value", invalidAttributeValueException); //$NON-NLS-1$
					}
				}else{
					PlayerTank.slugs +=10;
				}
				break;
			// PowerUp for plasma type	
			case 4:
				if(PlayerTank.plasma == 0){
					PlayerTank.plasma +=10;
					try {
						Main.PlayerTank.changeWeapon(4);
					} catch (InvalidAttributeValueException invalidAttributeValueException) {
						LOGGER.log(Level.WARNING, "Invalid Weapon Value", invalidAttributeValueException); //$NON-NLS-1$
					}
				}else{
					PlayerTank.plasma +=10;
				}
				break;
			default:
				break;
			}
							
			
		}
	}
	
	// Add new projective to the PowerUp Array
	public static void register(PowerUp powerUp){
		for(int i = 0; i < powerUpVector.length; i++){
			if(powerUpVector[i] == null){
				powerUpVector[i] = powerUp;
				return;
			}
		}
	}
}
