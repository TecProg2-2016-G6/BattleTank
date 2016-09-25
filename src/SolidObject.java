package src;

public abstract class SolidObject implements Model {

	protected Vector startPointInWorld = new Vector(0, 0, 0);

	protected Vector iDirection = new Vector(0, 0, 0);

	protected Vector jDirection = new Vector(0, 0, 0);

	protected Vector kDirection = new Vector(0, 0, 0);

	protected Polygon3D[] boundaryModel = null;

	protected Rectangle2D boundaryModel2D = null;

	protected boolean isVisible = false;

	protected Vector centreModel = new Vector(0, 0, 0);

	protected Vector cantreModelInCamera = new Vector(0, 0, 0);

	// The object is expired if the value is less than 0
	protected int lifeSpanObject = 0;

	protected int HP = 0;

	protected boolean isBlockingOtherModel = false;

	// Type 1 = boundary generally has a cubic shape, type 2 = other
	protected int modelType = 0;

	public final Polygon3D[] getBoundaryModelInCameraCoordinate() {
		return boundaryModel;
	}

	public final Vector getCentreModelInCameraCoordinate() {
		return cantreModelInCamera;
	}

	public final Vector getRealCentreModelWorld() {
		return centreModel;
	}

	public final int getTypeOfModel() {
		return modelType;
	}

	public final void unstuck() {
		isBlockingOtherModel = true;

	}

	/*
	 * Create a rough 3D cuboid boundary for this model. It consists 5 polygons,
	 * they are facing front, right, back, left, top respectively. (no bottom)
	 */
	public final void makeBoundary(double l, double h, double w) {

		boundaryModel = new Polygon3D[5];
		Vector[] front = new Vector[] { put(l, h, w), put(-l, h, w), put(-l, -h, w), put(l, -h, w) };
		boundaryModel[0] = new Polygon3D(front, null, null, null, null, 0, 0, 0);
		Vector[] right = new Vector[] { put(l, h, -w), put(l, h, w), put(l, -h, w), put(l, -h, -w) };
		boundaryModel[1] = new Polygon3D(right, null, null, null, null, 0, 0, 0);
		Vector[] back = new Vector[] { put(-l, h, -w), put(l, h, -w), put(l, -h, -w), put(-l, -h, -w) };
		boundaryModel[2] = new Polygon3D(back, null, null, null, null, 0, 0, 0);
		Vector[] left = new Vector[] { put(-l, h, w), put(-l, h, -w), put(-l, -h, -w), put(-l, -h, w) };
		boundaryModel[3] = new Polygon3D(left, null, null, null, null, 0, 0, 0);
		Vector[] top = new Vector[] { put(-l, h, w), put(l, h, w), put(l, h, -w), put(-l, h, -w) };
		boundaryModel[4] = new Polygon3D(top, null, null, null, null, 0, 0, 0);
	}

	/*
	 * If all the boundary polygons are rendered not visible, then the model
	 * will not be drawn
	 */
	public final boolean testVisibility() {

		for (int i = 0; i < boundaryModel.length; i++) {

			if (boundaryModel[i].visible) {
				return true;
			}
		}
		return false;
	}

	public final void findCentre() {

		centreModel = new Vector(0, 0, 0);
		for (int i = 0; i < 4; i++) {
			centreModel.add(boundaryModel[i].centre);
		}
		centreModel.scale(1.0 / 4);
	}

	public final Vector put(double i, double j, double k) {
		Vector temp = startPointInWorld.myClone();
		temp.add(iDirection, i);
		temp.add(jDirection, j);
		temp.add(kDirection, k);
		return temp;
	}

	public final void change(double i, double j, double k, Vector v) {

		v.set(startPointInWorld);
		v.add(iDirection, i);
		v.add(jDirection, j);
		v.add(kDirection, k);
	}

	public final double getTankDepth() {
		return cantreModelInCamera.z;
	}

	public final int getLifeSpan() {
		return lifeSpanObject;
	}

	public void damage(int damagePoint) {
		HP -= damagePoint;
	}

}