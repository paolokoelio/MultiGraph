/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.lwang;

import java.awt.MenuContainer;
import java.awt.image.ImageObserver;
import java.io.Serializable;
import javax.accessibility.Accessible;
import javax.swing.JFrame;
import javax.swing.RootPaneContainer;
import javax.swing.TransferHandler;
import javax.swing.WindowConstants;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public interface ModelConfiguration extends  WindowConstants, ImageObserver, MenuContainer, Serializable, Accessible, RootPaneContainer {
    
}
