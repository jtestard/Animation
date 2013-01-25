package comp559.a1;

public class SymplecticEuler implements Integrator {

	double [] dydt;
	int old_n = -1;
    @Override
    public String getName() {
        return "symplectic Euler";
    }

    @Override
    public void step(double[] y, int n, double t, double h, double[] yout, Function derivs) {
        //Avoid useless memory allocation if particle dimension does not change.
        if (old_n!=n) {
        	dydt = new double[n];
        	old_n = n;
        }
        
        //Compute accelerations
        derivs.derivs(t, y, dydt);
        for (int i = 0; i < n; i+=4) {
     	   dydt[i+2] *= h;
     	   dydt[i+3] *= h;
        }
        //Compute output from accelerations.
        for (int i = 0; i < n; i+=4)
        {
     	   yout[i+2] = y[i+2] + dydt[i+2];
     	   yout[i+3] = y[i+3] + dydt[i+3];
     	   yout[i] = y[i] + yout[i+2] * h;
     	   yout[i+1] = y[i+1] + yout[i+3] * h;
        }
    }

}
