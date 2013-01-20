package comp559.a1;

import no.uib.cipr.matrix.Vector;

/**
 * Velocity filter to use with a conjugate gradients solve
 * @author kry
 */
public interface Filter {

    /**
     * removes disallowed parts of v by projection
     * @param v
     */
    public void filter( Vector v );
    
}
