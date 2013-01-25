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
    Vector2d v = null;
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
        this.v = new Vector2d();
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
    	//TODO : optimize memory allocation
    	//Set the line vector.
    	double lx = p1.p.x-p2.p.x;
    	double ly = p1.p.y-p2.p.y;
    	l.set(lx,ly);
    	//Set up the line velocity
    	double vx = p1.v.x-p2.v.x;
    	double vy = p2.v.y-p2.v.y;
    	v.set(vx,vy);
    	
    	double scalar = - (k * (l.length() - l0) + c * ((v.dot(l))/l.length()) );
    	fp1.set(l.x,l.y);
    	fp1.normalize();
    	fp1.scale(scalar);
    	
    	fp2.set(fp1.x,fp1.y);
    	fp2.negate();
    	
    	//Add forces to particles
    	p1.f.x+=fp1.x;
    	p1.f.y+=fp1.y;
    	p2.f.x+=fp2.x;
    	p2.f.y+=fp2.y;    	
    }
   
    /**
     * Computes the force and adds it to the appropriate components of the force vector.
     * @param f
     */
    public void addForce(Vector f) {
    	//TODO : rewrite this method.
    	//Set the line vector.
    	double lx = p1.p.x-p2.p.x;
    	double ly = p1.p.y-p2.p.y;
    	l.set(lx,ly);
    	//Set up the line velocity
    	double vx = p1.v.x-p2.v.x;
    	double vy = p2.v.y-p2.v.y;
    	v.set(vx,vy);
    	
    	double scalar = - (k * (l.length() - l0) + c * ((v.dot(l))/l.length()) );
    	fp1.set(l.x,l.y);
    	fp1.normalize();
    	fp1.scale(scalar);
    	
    	fp2.set(fp1.x,fp1.y);
    	fp2.negate();
    	
    	//Add forces to particles
//    	f.set(0,f.get(0)+fp1.x);
//    	f.set(1,f.get(0)+fp1.x);    	
//    	f.y+=fp1.y;
//    	p2.f.x+=fp2.x;
    	p2.f.y+=fp2.y;    	
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
