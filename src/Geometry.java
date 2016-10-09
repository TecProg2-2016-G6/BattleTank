package src;
/**
 * This class hold algorithm that handles geometry problems, 
 * such as sorting polygons, find line polygon intersection
 */

public class Geometry {
	
	public static boolean compareModels(Model a, Model b) {
		
		//Compare centre Z depth values
			if(a.getTankDepth() > b.getTankDepth()){
				return true;
			}else{
				return false;
			}
		
	}
	
	//Sort polygons
	public final static void sortPolygons(Polygon3D[] polygons, int start) {
		
		int length = polygons.length;
		
		for(int i = 1; i < length; i++) {
			
			for(int j = start; j <length - i; j++) {
				
				if(Geometry.comparePolygons(polygons[j], polygons[j+1])) {
					Polygon3D temp = polygons[j+1];
					polygons[j+1] = polygons[j];
					polygons[j] = temp;
				}else{
					// Do nothing
				}
			}
		}
	}
	
	//Compare polygons
	public final static boolean comparePolygons(Polygon3D a, Polygon3D b) {
		
		Vector tempVector = new Vector(0,0,0);
		boolean inside = true;
		
		if(!a.visible){
			return false;
		}else if(!b.visible){
			return true;
		}else if(a.tempVertex[0].z < b.tempVertex[0].z && a.tempVertex[1].z < b.tempVertex[1].z && a.tempVertex[2].z < b.tempVertex[2].z && a.tempVertex[3].z < b.tempVertex[3].z){
			return true;	
		}else{
		
			for(int i = 0; i < b.tempVertex.length; i++) {
				tempVector.set(b.tempVertex[i]);
				tempVector.subtract(a.centre);
				tempVector.unit();
	
				if(tempVector.dot(a.normal) > 0.0001 ) {
					inside = false;
					break;
				}else{
					// Do nothing
				}
	
			}
			if(inside){
				return true;
			} else{
				// Do nothind
			}
	
			inside = true;
			
			for(int i = 0; i < a.tempVertex.length; i++) {
				tempVector.set(a.tempVertex[i]);
				tempVector.subtract(b.centre);
				tempVector.unit();
	
				if(tempVector.dot(b.normal) < -0.0001 ) {
					inside = false;
					break;
				}else {
					// Do nothing
				}
			}
	
			if(inside){
				return true;
			}else{
				// Do nothing
			}
			return false;
		}
	}
}
