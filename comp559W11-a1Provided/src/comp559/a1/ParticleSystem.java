package comp559.a1;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.vecmath.Point2d;
import javax.vecmath.Vector2d;

import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.DenseVector;
import no.uib.cipr.matrix.Vector;

import mintools.parameters.BooleanParameter;
import mintools.parameters.DoubleParameter;
import mintools.parameters.IntParameter;
import mintools.swing.VerticalFlowPanel;
import mintools.viewer.SceneGraphNode;

/**
 * Implementation of a simple particle system
 * 
 * @author kry
 */
public class ParticleSystem implements SceneGraphNode, Function, Filter {

	private List<Particle> particles = new LinkedList<Particle>();

	private List<Spring> springs = new LinkedList<Spring>();

	/**
	 * Creates an empty particle system
	 */
	public ParticleSystem() {
		// do nothing
	}

	/**
	 * Creates one of a number of simple test systems.
	 * 
	 * @param which
	 */
	public void createSystem(int which) {
		System.out.println("which:"+which);
		if (which == 1) {
			Point2d p = new Point2d(100, 100);
			Vector2d d = new Vector2d(20, 0);
			Particle p1, p2, p3, p4;
			p1 = new Particle(p.x - d.y, p.y + d.x, 0, 0);
			p1.index = particles.size();
			particles.add(p1);
			p2 = new Particle(p.x + d.y, p.y - d.x, 0, 0);
			p2.index = particles.size();
			particles.add(p2);
			springs.add(new Spring(p1, p2));
			p1.pinned = true;
			p2.pinned = true;
			p.add(d);
			p.add(d);
			int N = 10;
			for (int i = 1; i < N; i++) {
				// d.set( 20*Math.cos(i*Math.PI/N), 20*Math.sin(i*Math.PI/N) );
				p3 = new Particle(p.x - d.y, p.y + d.x, 0, 0);
				p3.index = particles.size();
				particles.add(p3);
				p4 = new Particle(p.x + d.y, p.y - d.x, 0, 0);
				p4.index = particles.size();
				particles.add(p4);
				springs.add(new Spring(p3, p1));
				springs.add(new Spring(p3, p2));
				springs.add(new Spring(p4, p1));
				springs.add(new Spring(p4, p2));
				springs.add(new Spring(p4, p3));
				p1 = p3;
				p2 = p4;
				p.add(d);
				p.add(d);
			}
		} else if (which == 2) {
			Particle p1 = new Particle(320, 100, 0, 0);
			p1.index = particles.size();
			particles.add(p1);
			Particle p2 = new Particle(320, 200, 0, 0);
			p2.index = particles.size();
			particles.add(p2);
			p1.pinned = true;
			springs.add(new Spring(p1, p2));
		} else if (which == 3) {
			int ypos = 100;
			Particle p0 = null;
			Particle p1, p2;
			p1 = new Particle(320, ypos, 0, 0);
			p1.index = particles.size();
			p1.pinned = true;
			particles.add(p1);
			int N = 10;
			for (int i = 0; i < N; i++) {
				ypos += 20;
				p2 = new Particle(320, ypos, 0, 0);
				p2.index = particles.size();
				particles.add(p2);
				springs.add(new Spring(p1, p2));
				// Hum.. this is not great in comparison to a proper bending
				// energy...
				// use Maple to generate some code though, as it is painful to
				// write by hand! :(
				if (p0 != null)
					springs.add(new Spring(p2, p0));
				p0 = p1;

				p1 = p2;
			}
		} else if (which == 4) { // Used for particle systems scene.
			BufferedReader br = null;
			springStiffness.setValue(5000);
			HashMap<String,Particle> particleMap = new HashMap<String,Particle>();
			ArrayList<String> springp1 = new ArrayList<String>();
			ArrayList<String> springp2 = new ArrayList<String>();			
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(new File("").getAbsolutePath()+"/src/COMP559/a1/customscene.csv"));
				while ((sCurrentLine = br.readLine()) != null) {
					String tokens[] = sCurrentLine.split(",");
					if (tokens[0].equalsIgnoreCase("particle")) {
						Particle p = new Particle(Double.parseDouble(tokens[2]),
								Double.parseDouble(tokens[3]),
								Double.parseDouble(tokens[4]),
								Double.parseDouble(tokens[5]));
						p.pinned = Boolean.parseBoolean(tokens[6]);
						p.index = particles.size();
						particles.add(p);
						particleMap.put(tokens[1], p);
					} else if (tokens[0].equalsIgnoreCase("Spring")) {
						springp1.add(tokens[1]);
						springp2.add(tokens[2]);
					} else {
						//Do nothing
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			//Create the springs
			for (int i = 0 ; i < springp1.size() ; i++) {
				String p1s = springp1.get(i);
				String p2s = springp2.get(i);				
				Particle p1 = particleMap.get(p1s);
				Particle p2 = particleMap.get(p2s);
				System.out.println("Creating spring between : "+ p1s + " and " + p2s);
				if (p1!=null)
					if (p2!=null) 
						springs.add(new Spring(p1,p2));
			}
		}
	}

	/**
	 * Gets the particles in the system
	 * 
	 * @return the particle set
	 */
	public List<Particle> getParticles() {
		return particles;
	}

	/**
	 * Resets the positions of all particles
	 */
	public void resetParticles() {
		for (Particle p : particles) {
			p.reset();
		}
		time = 0;
	}

	/**
	 * Deletes all particles
	 */
	public void clearParticles() {
		particles.clear();
		springs.clear();
	}

	/**
	 * Gets the phase space state of the particle system
	 * 
	 * @param y
	 */
	public void getPhaseSpace(double[] y) {
		int count = 0;
		for (Particle p : particles) {
			y[count++] = p.p.x;
			y[count++] = p.p.y;
			y[count++] = p.v.x;
			y[count++] = p.v.y;
		}
	}

	/**
	 * Gets the dimension of the phase space state (particles * 2 dimensions * 2
	 * for velocity and position)
	 * 
	 * @return dimension
	 */
	public int getPhaseSpaceDim() {
		return particles.size() * 4;
	}

	/**
	 * Sets the phase space state of the particle system
	 * 
	 * @param y
	 */
	public void setPhaseSpace(double[] y) {
		int count = 0;
		for (Particle p : particles) {
			if (p.pinned) {
				p.p.set(p.p0);
				p.v.set(p.v0);
				count += 4;
			} else {
				p.p.x = y[count++];
				p.p.y = y[count++];
				p.v.x = y[count++];
				p.v.y = y[count++];
			}
		}
	}

	/**
	 * Fixes positions and velocities after a step to deal with collisions
	 */
	public void postStepFix() {
		for (Particle p : particles) {
			if (p.pinned) {
				p.v.set(0, 0);
			}
		}
		// do wall collisions
		double r = restitution.getValue();
		for (Particle p : particles) {
			if (p.p.x <= 0) {
				p.p.x = 0;
				if (p.v.x < 0)
					p.v.x = -p.v.x * r;
				if (p.f.x < 0)
					p.f.x = 0;
			}
			if (p.p.x >= width) {
				p.p.x = width;
				if (p.v.x > 0)
					p.v.x = -p.v.x * r;
				if (p.f.x > 0)
					p.f.x = 0;
			}

			if (p.p.y >= height) {
				p.p.y = height;
				if (p.v.y > 0)
					p.v.y = -p.v.y * r;
				if (p.f.y > 0)
					p.f.y = 0;
			}
			if (p.p.y <= 0) {
				p.p.y = 0;
				if (p.v.y < 0)
					p.v.y = -p.v.y * r;
				if (p.f.y < 0)
					p.f.y = 0;
			}
		}
	}

	/** Elapsed simulation time */
	public double time = 0;

	/**
	 * The explicit integrator to use, if not performing backward Euler implicit
	 * integration
	 */
	public Integrator integrator;

	public double[] state = new double[1];
	public double[] stateOut = new double[1];

	// these get created in init() and are probably useful for Backward Euler
	// computations
	private ConjugateGradientMTJ CG;
	private DenseMatrix A;
	private DenseMatrix dfdx;
	private DenseMatrix dfdv;
	private DenseVector b;
	private DenseVector f;
	
	private DenseMatrix I;
	private DenseVector dfdvv0;
	private DenseVector x0;
	private DenseVector v0;	
	private DenseVector deltax;	
	private DenseVector deltav;	
	private DenseVector dfdt;
	private DenseVector xnew;
	private DenseVector vnew;
	/**
	 * Initializes the system Allocates the arrays and vectors necessary for the
	 * solve of the full system
	 */
	public void init() {
		int N = particles.size();
		// create matrix and vectors for solve
		CG = new ConjugateGradientMTJ(2 * N);
		CG.setFilter(this);
		A = new DenseMatrix(2 * N, 2 * N);
		dfdx = new DenseMatrix(2 * N, 2 * N);
		dfdv = new DenseMatrix(2 * N, 2 * N);
		deltav = new DenseVector(2 * N);
		b = new DenseVector(2 * N);
		f = new DenseVector(2 * N);
		deltax = new DenseVector(2 * N);
		
		
		//The identity matrix
		I = new DenseMatrix(2*N,2*N);
		for (int i = 0; i < 2*N ; i++) {
			I.set(i, i, 1.0);
		}
		dfdvv0 = new DenseVector(2*N);
		v0 = new DenseVector(2*N);
		x0 = new DenseVector(2*N);
		dfdt = new DenseVector(2*N);
		xnew = new DenseVector(2*N);
		vnew = new DenseVector(2*N);
	}

	/**
	 * Fills in the provided vector with the particle velocities.
	 * 
	 * @param xd
	 */
	private void getVelocities(DenseVector xd) {
		for (Particle p : particles) {
			int j = p.index * 2;
			if (p.pinned) {
				xd.set(j, 0);
				xd.set(j + 1, 0);
			} else {
				xd.set(j, p.v.x);
				xd.set(j + 1, p.v.y);
			}
		}
	}

	/**
	 * Sets the velocities of the particles given a vector
	 * 
	 * @param xd
	 */
	private void setVelocities(DenseVector xd) {
		for (Particle p : particles) {
			int j = p.index * 2;
			if (p.pinned) {
				p.v.set(0, 0);
			} else {
				p.v.x = xd.get(j);
				p.v.y = xd.get(j + 1);
			}
		}
	}

	/**
	 * Evaluates derivatives for ODE integration.
	 * 
	 * @param t
	 *            time
	 * @param y
	 *            state
	 * @param dydt
	 *            to be filled with the derivative
	 */
	@Override
	public void derivs(double t, double[] y, double[] dydt) {
		// set particle positions to given values
		setPhaseSpace(y);

		for (Particle p : particles) {
			p.clearForce();
		}

		// Include gravity force (if being used)
		if (useGravity.getValue()) {
			// Should be gx=0 and gy=-1 (if orientation obvious).

			for (Particle p : particles) {
				p.f.y += gravity.getFloatValue() * p.mass;
			}
		}

		// Include viscous damping
		for (Particle p : particles) {
			p.f.x += -viscousDamping.getFloatValue() * p.v.x;
			p.f.y += -viscousDamping.getFloatValue() * p.v.y;

		}

		// Include spring forces
		for (Spring spring : springs) {
			spring.apply();
		}

		// set dydt
		int i = 0;
		for (Particle p : particles) {
			dydt[i++/* p.index*4 */] = p.v.x; // x1_dot = v1
			dydt[i++/* p.index*4 */] = p.v.y; // x2_dot = v2
			dydt[i++/* p.index*4 */] = p.f.x / p.mass; // v1_dot = f1/m
			dydt[i++/* p.index*4 */] = p.f.y / p.mass; // v2_dot = f2/m
		}
	}

	/** Time in seconds that was necessary to advance the system */
	public double computeTime;

	/**
	 * Advances the state of the system
	 * 
	 * @param elapsed
	 */
	public void advanceTime(double elapsed) {
		Spring.k = springStiffness.getValue();
		Spring.c = springDamping.getValue();

		int n = getPhaseSpaceDim();

		long now = System.nanoTime();

		if (explicit.getValue()) {
			if (n != state.length) {
				state = new double[n];
				stateOut = new double[n];
			}
			getPhaseSpace(state);
			integrator.step(state, n, time, elapsed, stateOut, this);
			setPhaseSpace(stateOut);
		} else {
			if (f == null || f.size() != n) {
				init();
			}
			if (n != state.length) {
				state = new double[n];
				stateOut = new double[n];
			}			
			getPhaseSpace(state);
			backwardEuler(state,n,elapsed,stateOut);
			setPhaseSpace(stateOut);
		}
		time = time + elapsed;
		postStepFix();
		computeTime = (System.nanoTime() - now) / 1e9;
	}
	
	/**
	 * This algorithm and the notations used come directly
	 * from the PBM notes. 
	 * After the state is loaded, it is sent to the v0 and x0 vectors
	 * through the 
	 */	
	public void backwardEuler(double[] state, int n, double h, double[] stateOut) {
		int numIts = iterations.getValue();
		
		//Get phase space.
		setupV0X0();
		
		//Compute f
		
		//Compute dfdx
		
		//Compute dfdv
		
		//Compute dfdt
		
		
		//Compute A = I - h*dfdv - h*h*dfdx
		dfdv.scale(h);
		dfdx.scale(h*h);			
		A.add(I);
		A.add(-1.0, dfdv);
		A.add(-1.0,dfdx);
		
		//Compute b = h ( f + h * dfdv * v0 + dfdt);
		b.add(f);
		dfdv.mult(v0, dfdvv0);
		dfdvv0.scale(h);
		b.add(dfdvv0);
		b.add(dfdt);
		b.scale(h);
		
		//Solve matrix
		CG.solve(A, b, deltav, numIts);
		
		//Compute output phase space.
		
		//vnew = v0+deltav
		vnew.add(v0);
		vnew.add(deltav);
		
		//deltax = h*vnew
		deltax.add(vnew);
		deltax.scale(h);
		
		//xnew = x0 + deltax
		xnew.add(x0);
		xnew.add(deltax);
		
		getStateOut();
	}
	
	public void setupV0X0() {
		for (int i = 0 ; i < state.length ; i+=4) {
			int j = i/2;
			x0.set(j,state[i]);
			x0.set(j+1,state[i+1]);
			v0.set(j,state[i+2]);
			v0.set(j,state[i+3]);
		}
	}
	
	public void getStateOut() {
		for (int i = 0 ; i < stateOut.length ; i+=4) {
			int j = i/2;
			stateOut[i]=xnew.get(j);
			stateOut[i+1]=xnew.get(j+1);
			stateOut[i+2]=vnew.get(j);
			stateOut[i+3]=vnew.get(j+1);			
		}
	}

	@Override
	public void filter(Vector v) {
		for (Particle p : particles) {
			if (!p.pinned)
				continue;
			v.set(p.index * 2 + 0, 0);
			v.set(p.index * 2 + 1, 0);
		}
	}

	/**
	 * Creates a new particle and adds it to the system
	 * 
	 * @param x
	 * @param y
	 * @param vx
	 * @param vy
	 * @return the new particle
	 */
	public Particle createParticle(double x, double y, double vx, double vy) {
		Particle p = new Particle(x, y, vx, vy);
		p.index = particles.size();
		particles.add(p);
		return p;
	}

	public void remove(Particle p) {
		for (Spring s : p.springs) {
			Particle other = s.p1 == p ? s.p2 : s.p1;
			other.springs.remove(s);
			springs.remove(s);
		}
		p.springs.clear(); // not really necessary
		particles.remove(p);
		// reset indices of each particle :(
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).index = i;
		}
	}

	/**
	 * Creates a new spring between two particles and adds it to the system.
	 * 
	 * @param p1
	 * @param p2
	 * @return the new spring
	 */
	public Spring createSpring(Particle p1, Particle p2) {
		Spring s = new Spring(p1, p2);
		springs.add(s);
		return s;
	}

	/**
	 * Removes a spring between p1 and p2 if it exists, does nothing otherwise
	 * 
	 * @param p1
	 * @param p2
	 * @return true if the spring was found and removed
	 */
	public boolean removeSpring(Particle p1, Particle p2) {
		Spring found = null;
		for (Spring s : springs) {
			if ((s.p1 == p1 && s.p2 == p2) || (s.p1 == p2 && s.p2 == p1)) {
				found = s;
				break;
			}
		}
		if (found != null) {
			found.p1.springs.remove(found);
			found.p2.springs.remove(found);
			springs.remove(found);
			return true;
		}
		return false;
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		// do nothing
	}

	private int height;
	private int width;

	@Override
	public void display(GLAutoDrawable drawable) {
		GL2 gl = drawable.getGL().getGL2();

		// update the width and the height for wall collisions
		height = drawable.getHeight();
		width = drawable.getWidth();

		gl.glPointSize(10);
		gl.glBegin(GL.GL_POINTS);
		for (Particle p : particles) {
			double alpha = 0.5;
			if (p.pinned) {
				gl.glColor4d(1, 0, 0, alpha);
			} else {
				gl.glColor4d(p.color.x, p.color.y, p.color.z, alpha);
			}
			gl.glVertex2d(p.p.x, p.p.y);
		}
		gl.glEnd();

		gl.glColor4d(0, .5, .5, .5);
		gl.glLineWidth(2f);
		gl.glBegin(GL.GL_LINES);
		for (Spring s : springs) {
			gl.glVertex2d(s.p1.p.x, s.p1.p.y);
			gl.glVertex2d(s.p2.p.x, s.p2.p.y);
		}
		gl.glEnd();
	}

	public BooleanParameter useGravity = new BooleanParameter("use gravity",
			true);
	public DoubleParameter gravity = new DoubleParameter("gravity", 9.8, 0.01,
			1000);
	public DoubleParameter springStiffness = new DoubleParameter(
			"spring stiffness", 100, 0, 10000);
	public DoubleParameter springDamping = new DoubleParameter(
			"spring damping", 0, 0, 50);
	public DoubleParameter viscousDamping = new DoubleParameter(
			"viscous damping", 0, 0, 10);
	public DoubleParameter restitution = new DoubleParameter("r", 0, 0, 1);
	public JTextArea comments = new JTextArea("enter comments in control panel");
	public IntParameter iterations = new IntParameter("iterations", 100, 1, 100);
	/** controls weather explicit or implicit integration is used */
	public BooleanParameter explicit = new BooleanParameter("explicit", true);

	@Override
	public JPanel getControls() {
		VerticalFlowPanel vfp = new VerticalFlowPanel();
		vfp.add(comments);
		vfp.add(useGravity.getControls());
		vfp.add(gravity.getSliderControls(true));
		vfp.add(springStiffness.getSliderControls(false));
		vfp.add(springDamping.getSliderControls(false));
		vfp.add(viscousDamping.getSliderControls(false));
		vfp.add(restitution.getSliderControls(false));
		vfp.add(iterations.getSliderControls());
		vfp.add(explicit.getControls());
		return vfp.getPanel();
	}

	@Override
	public String toString() {
		String ret = "JULES TESTARD\n" + comments.getText() + "\n"
				+ "particles = " + particles.size() + "\n";
		if (explicit.getValue()) {
			ret += "integrator = " + integrator.getName() + "\n";
		} else {
			ret += "integrator = Backward Euler\n";
		}
		ret += "k = " + springStiffness.getValue() + "\n" + "b = "
				+ springDamping.getValue() + "\n" + "c = "
				+ viscousDamping.getValue() + "\n" + "time = " + time;
		return ret;
	}

}
