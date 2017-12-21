package threejsFileGen;

public class Centroid {
	private double x;
	private double y;
	
	Centroid(Double xIn, Double yIn){
		x = xIn;
		y = yIn;
	}
	// x getter/setter
	public double getX(){
		return x;
	}
	public void setX(double xIn){
		x = xIn;
	}
	
	// y getter/setter
	public Double getY(){
		return y;
	}
	public void setY(double yIn){
		y = yIn;
	}
	
	// add x / add y
	public void addX(double xIn){
		x += xIn;
	}
	public void addY(double yIn){
		y += yIn;
	}
}
