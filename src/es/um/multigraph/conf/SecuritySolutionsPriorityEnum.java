/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.conf;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public enum SecuritySolutionsPriorityEnum {
    VOID,
    P0,
    P1,
    P2,
    P3
    ;
    
    @Override
    public String toString() {
        switch(this) {
            case VOID: return "";
            case P0: return "P0";
            case P1: return "P1";
            case P2: return "P2";
            case P3: return "P3";
                
            default: throw new UnsupportedOperationException("Enum to string not configured");
        }
    }
}
