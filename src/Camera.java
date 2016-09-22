package src;

import java.awt.*;

public class Camera {
	
	public static Vector cameraPosition = null;

	public static Vector absoluteCameraPosition = null;

	public Vector thirdPersonDisplacement = null;

	public static Vector viewDirection = null;
	
	// The YZ_angle is 315 degrees, and it does not change,
	public static int YZ_angle = 319;
	
	public static int XZ_angle = 0;

	public static final Rectangle screen = new Rectangle(0, 0, 640, 480);

	public static boolean restartCameraPosition;

	public int flyThroughTimer;

	public Camera() {

		XZ_angle = 0;
		cameraPosition = new Vector(10, 0.25, 1.5);
		absoluteCameraPosition = new Vector(10, 0.25, 1.5);
		viewDirection = new Vector(0, 0, 1);
		thirdPersonDisplacement = new Vector(0, 0, 0);
		thirdPersonDisplacement.set(viewDirection.x, 0, -viewDirection.z);
	}

	public void update() {

		if (Main.gameOver) {
			return;
		}

		if (Main.gameNotStart) {
			flyThroughTimer++;
		}

		if (!Main.gameNotStart) {

			cameraPosition.subtract(thirdPersonDisplacement);

			absoluteCameraPosition.set(cameraPosition);

			flyThroughTimer = 0;

			if (!restartCameraPosition) {
				double d_x = (PlayerTank.bodyCenter.x - cameraPosition.x) / 5;
				double d_z = (PlayerTank.bodyCenter.z - cameraPosition.z) / 5;
				cameraPosition.x += d_x;
				cameraPosition.z += d_z;
			} else {
				double d_x = (PlayerTank.bodyCenter.x - cameraPosition.x);
				double d_z = (PlayerTank.bodyCenter.z - cameraPosition.z);
				cameraPosition.x += d_x;
				cameraPosition.z += d_z;

				restartCameraPosition = false;
			}

			viewDirection.set(0, 0, 1);
			viewDirection.rotate_YZ(YZ_angle);
			viewDirection.rotate_XZ(XZ_angle);
			viewDirection.unit();

			/**
			 * Move the camera back a little bit, so the view becomes more like
			 * Third person rather than first person.
			 */
			 
			thirdPersonDisplacement.set(viewDirection.x * 0.9, 0, -viewDirection.z * 0.9);
			cameraPosition.add(thirdPersonDisplacement);

		} else {
				
			// Fly through this entire level when the game isn't started.
			if (flyThroughTimer > 0 && flyThroughTimer <= 60) {
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 60 && flyThroughTimer <= 130) {

				XZ_angle -= 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 130 && flyThroughTimer <= 430) {
				cameraPosition.add(-0.01, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 430 && flyThroughTimer <= 480) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 480 && flyThroughTimer <= 800) {
				cameraPosition.add(-0.005, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 800 && flyThroughTimer <= 825) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(-0.005, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 825 && flyThroughTimer <= 1100) {
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1100 && flyThroughTimer <= 1130) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1130 && flyThroughTimer <= 1250) {
				cameraPosition.add(0.005, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1250 && flyThroughTimer <= 1290) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1290 && flyThroughTimer <= 1550) {
				cameraPosition.add(0.01, 0, 0.005);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1550 && flyThroughTimer <= 1567) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.01, 0, 0.005);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1567 && flyThroughTimer <= 1867) {
				cameraPosition.add(0.012, 0, 0);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1867 && flyThroughTimer <= 1900) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.01, 0, 0.005);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 1900 && flyThroughTimer <= 2100) {
				cameraPosition.add(0.007, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2100 && flyThroughTimer <= 2130) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.007, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2130 && flyThroughTimer <= 2330) {
				cameraPosition.add(0.003, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2330 && flyThroughTimer <= 2360) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.003, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2360 && flyThroughTimer <= 2560) {
				cameraPosition.add(0, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2560 && flyThroughTimer <= 2590) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2590 && flyThroughTimer <= 2900) {
				cameraPosition.add(-0.005, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2900 && flyThroughTimer <= 2920) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(-0.007, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 2920 && flyThroughTimer <= 3255) {
				cameraPosition.add(-0.009, 0, -0.011);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer > 3255 && flyThroughTimer <= 3380) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.set(10, 0.25, 1.5);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (flyThroughTimer == 3380)
				flyThroughTimer = 0;

		}

	}
}
