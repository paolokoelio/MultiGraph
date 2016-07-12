
package es.um.multigraph.event;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Base event class
 * @author Juanjo Andreu
 */
public abstract class Event {

    private Calendar timestamp;
    
    public Event (){
        timestamp = new GregorianCalendar();
    }
}
