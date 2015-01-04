import java.awt.Color;
import java.awt.Graphics;
import java.awt.Dimension;
import java.util.ArrayList;

public class Particle {
	
	public static final int TRAIL_LENGTH = 500;
	public static final int TRAIL_INTERVAL = 3;
	
	protected double x;
	protected double dx;
	protected double y;
	protected double dy;
	protected double mass;
	protected double radius;
	protected Color c;
	protected ArrayList<Dimension> trail;
	protected long count;
	
	public Particle(double x, double y, double dx, double dy, double mass, Color c) {
		this.x = x;
		this.y = y;
		this.dx = dx;
		this.dy = dy;
		this.mass = mass;
		this.c = c;
		this.radius = Math.pow((0.75*Math.PI*mass), (1/3.0));
		this.trail = new ArrayList<Dimension>();
		this.count = 0;		
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getDx() {
		return this.dx;
	}
	
	public void setDx(double dx) {
		this.dx = dx;
	}
	
	public double getY() {
		return this.y;
	}
	
	public double getDy() {
		return this.y;
	}
	
	public void setDy(double dy) {
		this.dy = dy;
	}
	
	public double getMass() {
		return this.mass;
	}
	
	public double getRadius() {
		return this.radius;
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
		if (++count%TRAIL_INTERVAL == 0) trail.add(0,new Dimension((int) x , (int) y));
		x += dx*dt;
		y += dy*dt;
	}
	

	public void accelerate(ArrayList<Particle> particles, double dt) {	
		for (Particle p : particles ) {
			if (p == this) continue;
			
			double deltaX = p.getX() - this.x;
			double deltaY = p.getY() - this.y;
			double r = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
			double fij = (MainFrame.G * p.getMass() * this.mass) / (r*r);
			
			double fijx = fij*deltaX/r;
			double fijy = fij*deltaY/r;
			
			double aijx = fijx/this.mass;
			double aijy = fijy/this.mass;
			
			dx = dx + aijx*dt;
			dy = dy + aijy*dt;
		}		
	}
	
	
	public boolean collide(Particle p2) {

		double p1Xt1 = this.x;			double p2Xt1 = p2.getX();
		double p1Xt2 = p1Xt1 + dx;		double p2Xt2 = p2.getX() + p2.getDx();
		double p1Yt1 = this.y;			double p2Yt1 = p2.getY();
		double p1Yt2 = p1Yt1 + dy;		double p2Yt2 = p2.getY() + p2.getDy();

		double m = (this.dy - p2.getDy()) / (this.dx - p2.getDx());
		double c = this.y - m*this.x;
		double R = this.mass + p2.getMass();
		
		double A = m*m+1;
		double B = 2*(m*c - m*p2.getY() - p2.getX());
		double C = p2.getY()*p2.getY() - R*R + p2.getX() - 2*c*p2.getY() + c*c;
		
		double Z = B*B - 4*A*C;
		
		if (Double.compare(Z,0.0) < 0) {
			return false;
		}
		else {
			double x1 = (-B+Math.sqrt(Z))/(2*A);
			double x2 = (-B-Math.sqrt(Z))/(2*A);			
			return true;
		}		
	}
}
