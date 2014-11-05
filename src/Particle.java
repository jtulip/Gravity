import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.ArrayList;

public class Particle {
	
	public static final int TRAIL_LENGTH = 1000;
	
	public double x;
	double dx;
	public double y;
	double dy;
	public double mass;
	double radius;
	Color c;
	ArrayList<Dimension> trail;
	
	public Particle(double x, double y, double dx, double dy, double mass, Color c) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.mass = mass;
		this.c = c;
		this.radius = Math.pow((0.75*Math.PI*mass), (1/3.0));
		this.trail = new ArrayList<Dimension>();
		
	}
	
	public void draw(Graphics g,  double centerMassX, double centerMassY, double zoomFactor, int XDIM, int YDIM ) {
		
		g.setColor(c);
		g.drawOval((int) ((x-radius-centerMassX)*zoomFactor + XDIM/2.0), (int) ((y-radius-centerMassY)*zoomFactor + YDIM/2.0), 
				(int) (radius*2*zoomFactor), (int) (radius*2*zoomFactor));
		for (Dimension d : trail) {
			g.drawOval((int) ((d.width-centerMassX)*zoomFactor + XDIM/2.0), (int) ((d.height-centerMassY)*zoomFactor + YDIM/2.0), 1, 1);
		}
	}

	public void move(double dt) {
		if (trail.size() > TRAIL_LENGTH) trail.remove(trail.size()-1);
		trail.add(0,new Dimension((int) x , (int) y));
		x += dx*dt;
		y += dy*dt;
	}
	
	public void accelerate(ArrayList<Particle> particles, double dt) {
		
		double f = 0;
		for (Particle p : particles ) {
			if (p == this) continue;
			
			double deltaX = p.x - this.x;
			double deltaY = p.y - this.y;
			double r = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			double fij = (MainFrame.G * p.mass * this.mass) / (r*r);
			
			double fijx = fij*deltaX/r;
			double fijy = fij*deltaY/r;
			
			double aijx = fijx/this.mass;
			double aijy = fijy/this.mass;
			
			dx = dx + aijx*dt;
			dy = dy + aijy*dt;
		}
		
	}
}
