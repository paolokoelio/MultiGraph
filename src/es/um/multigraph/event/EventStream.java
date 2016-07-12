/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.event;

import es.um.multigraph.core.MainClass;
import es.um.multigraph.event.dummy.DummyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Event source, like new vulnerabilities etc.
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class EventStream implements Runnable {

    /**
     * Main handler (like a ping back)
     */
    private final MainClass parent;
    private boolean stopStream = false;

    public boolean isStopped() {
        return stopStream;
    }

    public void stopStream() {
        parent.log("Stop requested.\n", this);
        this.stopStream = true;
    }
    
    public EventStream(MainClass main) {
        this.parent = main;
    }
    
    public Event getNextQueuedEvent() {
        Event result = new DummyEvent();
        this.parent.log("Fire new Event: "+result+"\n", this);
        return result;
    }

    
    
    @Override
    public void run() {
        this.stopStream = false;
        parent.log("Started.\n", this);
        try {
            int i = 0;
            while(!this.isStopped()) {
                Thread.sleep(500);
                // throw the new event (vuln)
                parent.setEventPrivateCounter(++i);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(EventStream.class.getName()).log(Level.SEVERE, null, ex);
        }
        parent.log("Stopped.\n", this);
        parent.setEventPrivateCounter(null);
    }

    @Override
    public String toString() {
        return "Event Stream";
    }
    
    
    
}
