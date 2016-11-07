package src;

/**
 * This class hold algorithm that handles geometry problems, such as sorting
 * polygons, find line polygon intersection
 */

public class Geometry {

	public static boolean compareModels(Model modelA, Model modelB) {

		return modelA.getTankDepth() > modelB.getTankDepth();

	}

	public final static void sortPolygons(Polygon3D[] polygons, int start) {

		int length = polygons.length;

		//
		for (int i = 1; i < length; i++) {
			for (int j = start; j < length - i; j++) {
				if (comparePolygons(polygons[j], polygons[j + 1])) {
					Polygon3D tempPolygon = polygons[j + 1];
					polygons[j + 1] = polygons[j];
					polygons[j] = tempPolygon;
				} else {
					// Do nothing
				}
			}
		}
	}

	// Compare polygons
	public final static boolean comparePolygons(Polygon3D polygonA, Polygon3D polygonB) {

		Vector tempVector = new Vector(0, 0, 0);
		boolean isInside = true;

		if (!polygonA.isVisible) {
			return false;
		} else if (!polygonB.isVisible) {
			return true;
		} else if (polygonA.tempVertex[0].z < polygonB.tempVertex[0].z
				&& polygonA.tempVertex[1].z < polygonB.tempVertex[1].z
				&& polygonA.tempVertex[2].z < polygonB.tempVertex[2].z
				&& polygonA.tempVertex[3].z < polygonB.tempVertex[3].z) {
			return true;
		} else {

			for (int i = 0; i < polygonB.tempVertex.length; i++) {
				tempVector.set(polygonB.tempVertex[i]);
				tempVector.subtract(polygonA.centre);
				tempVector.unit();

				if (tempVector.dot(polygonA.normal) > 0.0001) {
					isInside = false;
					break;
				} else {
					// Do nothing
				}

			}
			if (isInside) {
				return true;
			} else {
				// Do nothind
			}

			isInside = true;

			for (int i = 0; i < polygonA.tempVertex.length; i++) {
				tempVector.set(polygonA.tempVertex[i]);
				tempVector.subtract(polygonB.centre);
				tempVector.unit();

				if (tempVector.dot(polygonB.normal) < -0.0001) {
					isInside = false;
					break;
				} else {
					// Do nothing
				}
			}

			if (isInside) {
				return true;
			} else {
				// Do nothing
			}
			return false;
		}
	}
}
