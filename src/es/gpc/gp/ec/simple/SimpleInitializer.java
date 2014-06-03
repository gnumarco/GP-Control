/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package es.gpc.gp.ec.simple;
import es.gpc.gp.ec.Initializer;
import es.gpc.gp.ec.EvolutionState;
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.Population;

/* 
 * SimpleInitializer.java
 * 
 * Created: Tue Aug 10 21:07:42 1999
 * By: Sean Luke
 */

/**
 * SimpleInitializer is a default Initializer which initializes a Population
 * by calling the Population's populate(...) method.  For most applications,
 * this should suffice.
 *
 * @author Sean Luke
 * @version 1.0 
 */

public class SimpleInitializer extends Initializer
    {
    private static final long serialVersionUID = 1;

    public void setup(final EvolutionState state, final Parameter base)
        { 
        }

    /** Creates, populates, and returns a new population by making a new
        population, calling setup(...) on it, and calling populate(...)
        on it, assuming an unthreaded environment (thread 0).
        Obviously, this is an expensive method.  It should only
        be called once typically in a run. */

    public Population initialPopulation(final EvolutionState state, int thread)
        {
        Population p = setupPopulation(state, thread); 
        p.populate(state, thread);
        return p;
        }
                
    public Population setupPopulation(final EvolutionState state, int thread)
        {
        Parameter base = new Parameter(P_POP);
        Population p = (Population) state.parameters.getInstanceForParameterEq(base,null,Population.class);  // Population.class is fine
        p.setup(state,base);
        return p;
        }
    }