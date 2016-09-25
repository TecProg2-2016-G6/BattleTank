package src;

import java.awt.*;

public class Camera {
	
	protected static Vector cameraPosition = new Vector(10, 0.25, 1.5);;

	protected static Vector absoluteCameraPosition = new Vector(10, 0.25, 1.5);

	private Vector thirdPersonDisplacement = null;

	protected static Vector viewDirection = new Vector(0, 0, 1);
	
	// The YZ_angle is 315 degrees, and it does not change,
	protected static int YZ_angle = 319;
	
	protected static int XZ_angle = 0;

	protected static final Rectangle screen = new Rectangle(0, 0, 640, 480);

	protected static boolean restartCameraPosition = false;

	private int flyThroughTimer = 0;

	public Camera() {

		this.thirdPersonDisplacement = new Vector(0, 0, 0);
		this.thirdPersonDisplacement.set(viewDirection.x, 0, -viewDirection.z);
	}

	public void update() {

		if (Main.gameOver) {
			return;
		}

		if (Main.gameNotStart) {
			this.flyThroughTimer++;
		}

		if (!Main.gameNotStart) {

			cameraPosition.subtract(this.thirdPersonDisplacement);

			absoluteCameraPosition.set(cameraPosition);

			this.flyThroughTimer = 0;

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
			 
			this.thirdPersonDisplacement.set(viewDirection.x * 0.9, 0, -viewDirection.z * 0.9);
			cameraPosition.add(this.thirdPersonDisplacement);

		} else {
				
			// Fly through this entire level when the game isn't started.
			if (this.flyThroughTimer > 0 && this.flyThroughTimer <= 60) {
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 60 && this.flyThroughTimer <= 130) {

				XZ_angle -= 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 130 && this.flyThroughTimer <= 430) {
				cameraPosition.add(-0.01, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 430 && this.flyThroughTimer <= 480) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 480 && this.flyThroughTimer <= 800) {
				cameraPosition.add(-0.005, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 800 && this.flyThroughTimer <= 825) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(-0.005, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 825 && this.flyThroughTimer <= 1100) {
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1100 && this.flyThroughTimer <= 1130) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1130 && this.flyThroughTimer <= 1250) {
				cameraPosition.add(0.005, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1250 && this.flyThroughTimer <= 1290) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, 0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1290 && this.flyThroughTimer <= 1550) {
				cameraPosition.add(0.01, 0, 0.005);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1550 && this.flyThroughTimer <= 1567) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.01, 0, 0.005);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1567 && this.flyThroughTimer <= 1867) {
				cameraPosition.add(0.012, 0, 0);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1867 && this.flyThroughTimer <= 1900) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.01, 0, 0.005);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 1900 && this.flyThroughTimer <= 2100) {
				cameraPosition.add(0.007, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2100 && this.flyThroughTimer <= 2130) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.007, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2130 && this.flyThroughTimer <= 2330) {
				cameraPosition.add(0.003, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2330 && this.flyThroughTimer <= 2360) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0.003, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2360 && this.flyThroughTimer <= 2560) {
				cameraPosition.add(0, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2560 && this.flyThroughTimer <= 2590) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(0, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2590 && this.flyThroughTimer <= 2900) {
				cameraPosition.add(-0.005, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2900 && this.flyThroughTimer <= 2920) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.add(-0.007, 0, -0.01);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 2920 && this.flyThroughTimer <= 3255) {
				cameraPosition.add(-0.009, 0, -0.011);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer > 3255 && this.flyThroughTimer <= 3380) {
				XZ_angle += 1;
				XZ_angle = (XZ_angle + 360) % 360;

				viewDirection.set(0, 0, 1);
				viewDirection.rotate_YZ(YZ_angle);
				viewDirection.rotate_XZ(XZ_angle);
				viewDirection.unit();
				cameraPosition.set(10, 0.25, 1.5);
				absoluteCameraPosition.set(cameraPosition);
			}

			if (this.flyThroughTimer == 3380)
				this.flyThroughTimer = 0;

		}

	}
}
