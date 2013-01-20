package comp559.a1;

import javax.vecmath.Vector2d;

import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.Vector;

/**
 * Spring class for 599 assignment 1
 * @author kry
 */
public class Spring {

    Particle p1 = null;
    Particle p2 = null;
    
    public static double k = 1;
    public static double c = 1;
    
    double l0 = 0;
    
    /**
     * This vector represents the difference p1-p2 (vector between the two particles).
     */
    Vector2d l = null;
    Vector2d fp1 = null;
    Vector2d fp2 = null;
    
    /**
     * Creates a spring between two particles
     * @param p1
     * @param p2
     */
    public Spring( Particle p1, Particle p2 ) {
        this.p1 = p1;
        this.p2 = p2;
        this.l = new Vector2d();
        this.fp1 = new Vector2d();
        this.fp2 = new Vector2d();
        recomputeRestLength();
        p1.springs.add(this);
        p2.springs.add(this);
    }
    
    /**
     * Computes and sets the rest length based on the original position of the two particles 
     */
    public void recomputeRestLength() {
        l0 = p1.p0.distance( p2.p0 );
    }
    
    /**
     * Applies the spring force by adding a force to each particle
     */
    public void apply() {
        // TODO: FINISH THIS CODE!
    	//Set the line vector.
    	double lx = p1.p.x-p2.p.x;
    	double ly = p1.p.y-p2.p.y;
    	l.set(lx,ly);
    	//Set up the line velocity
    	double vx = p1.v.x-p2.v.x;
    	double vy = p2.v.y-p2.v.y;
    	
    	//Set up spring for vector for particle 1.
    	double fp1x = - (k * (l.length() - l0) + c * ((l.x * vx)/l.length()) ) * (l.x/l.length());
    	double fp1y = - (k * (l.length() - l0) + c * ((l.y * vy)/l.length()) ) * (l.y/l.length());
    	
    	//Set up spring for vector for particle 2.
    	double fp2x = - fp1x;
    	double fp2y = - fp1y;
    	
    	//Add forces to particles
    	fp1.set(fp1x,fp1y);
    	p1.addForce(fp1);    	
    	fp2.set(fp2x,fp2y);
    	p2.addForce(fp2);     
    }
   
    /**
     * Computes the force and adds it to the appropriate components of the force vector.
     * @param f
     */
    public void addForce(Vector f) {
        // TODO: FINISH THIS CODE (probably very similar to what you did above)
    	double lx = p1.p.x-p2.p.x;
    	double ly = p1.p.y-p2.p.y;
    	l.set(lx,ly);
    	//Set up the line velocity
    	double vx = p1.v.x-p2.v.x;
    	double vy = p2.v.y-p2.v.y;
    	
    	//Set up spring for vector for particle 1.
    	double fp1x = - (k * (l.length() - l0) + c * ((l.x * vx)/l.length()) ) * (l.x/l.length());
    	double fp1y = - (k * (l.length() - l0) + c * ((l.y * vy)/l.length()) ) * (l.y/l.length());
    	
    	//Set up spring for vector for particle 2.
    	double fp2x = - fp1x;
    	double fp2y = - fp1y;    	
    	
    	f.add(p1.index*2,fp1x);
    	f.add(p1.index*2+1,fp1y);
    	f.add(p2.index*2,fp2x);
    	f.add(p2.index*2+1,fp2y);    	
    }
    
    /**
     * Adds this springs contribution to the stiffness matrix
     * @param dfdx
     */
    public void addDfdx( Matrix dfdx ) {
        // TODO: FINISH THIS CODE... necessary for backward euler integration
        
    }   
 
    /**
     * Adds this springs damping contribution to the implicit damping matrix
     * @param dfdv
     */
    public void addDfdv( Matrix dfdv ) {
        // TODO: FINISH THIS CODE... necessary for backward euler integration
        
    } 
    
}
