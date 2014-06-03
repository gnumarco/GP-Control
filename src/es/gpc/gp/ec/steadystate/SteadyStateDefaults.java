/*
  Copyright 2006 by Sean Luke
  Licensed under the Academic Free License version 3.0
  See the file "LICENSE" for more information
*/


package es.gpc.gp.ec.steadystate;
import es.gpc.gp.ec.util.Parameter;
import es.gpc.gp.ec.DefaultsForm;

/* 
 * SteadyStateDefaults.java
 * 
 * Created: Thu Jan 20 16:49:57 2000
 * By: Sean Luke
 */

/**
 * @author Sean Luke
 * @version 1.0 
 */

public final class SteadyStateDefaults implements DefaultsForm
    {
    public static final String P_STEADYSTATE = "steady";

    /** Returns the default base. */
    public static final Parameter base()
        {
        return new Parameter(P_STEADYSTATE);
        }    
    }
