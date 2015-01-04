import java.awt.Graphics;
import java.util.ArrayList;


public interface IParticle {
	public void draw(Graphics g,  double centerMassX, double centerMassY, double zoomFactor, int XDIM, int YDIM );
	public void move(double dt);
	public void accelerate(ArrayList<IParticle> particles, double dt);	
	public boolean collide(IParticle p2);

	public double getX();
	public double getDx();
	public double getY();
	public double getDy();
	public double getMass();
	public double getRadius();
}
