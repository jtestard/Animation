package comp559.a1;

public class RK4 implements Integrator {
    
    @Override
    public String getName() {
        return "RK4";
    }

	double [] dydt;
	double [] k1;
	double [] k2;
	double [] k3;
	double [] k4;
	
	//This int checks that if the dimension did change (which is a rare occurrence event).
	int old_n;
	
	public RK4 () {
		old_n = -1;
	}
    
    @Override
    public void step(double[] y, int n, double t, double h, double[] yout, Function derivs) {
        //Avoid useless memory allocation if size did not change
    	if (old_n!=n) {
        	dydt = new double[n];
        	k1 = new double[n];
        	k2 = new double[n];
        	k3 = new double[n];
        	k4 = new double[n];
        	old_n = n;
    	}
    	
    	//Compute k1
       	derivs.derivs(t, y, k1);
    	for (int i = 0; i < n; i++) {
    		k1[i] *= h;
    	}
    	
    	//Compute k2
    	for (int i = 0; i < n; i++) {
    		k2[i] = y[i] + k1[i]*0.5;
    	}
    	derivs.derivs(t + h/2.0, k2, k2);
    	for (int i = 0; i < n; i++) {
    		k2[i] *= h;
    	}

    	//Compute k3
    	for (int i = 0; i < n; i++) {
    		k3[i] = y[i] + k2[i] * 0.5;
    	}
    	derivs.derivs(t + h/2.0, k3, k3);
    	for (int i = 0; i < n; i++) {
    		k3[i] *= h;
    	}
    
    	//Compute k4
    	for (int i = 0; i < n; i++) {
    		k4[i] = y[i] + k3[i];
    	}
    	derivs.derivs(t + h, k4, k4);
    	for (int i = 0; i < n; i++) {
    		k4[i] *= h;
    	}
    	
    	//Compute total
    	for (int i = 0; i < n; i++) {
    		yout[i] = y[i] + k1[i]/6.0 + k2[i]/3.0 + k3[i]/3.0 + k4[i]/6.0;
    	}
     }
    
    private void printArray(double[] array, String name) {
    	System.out.println("Printing contents of " + name + ":");
    	for (double d : array) {
    		
    	}
    }
}