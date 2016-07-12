/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.event.solution;

import es.um.multigraph.conf.SecuritySolutionsPriorityEnum;
import java.util.List;
import javax.swing.ListModel;


/**
 * Generic Class for the output of the decision module
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 * @see <a href="https://web.nvd.nist.gov/view/800-53/home">NIST 800-53</a>
 */
public interface Solution {

    /**
     * FIXME
     * @return FIXME
     */
    public String getControl();
    
    /**
     * FIXME
     * @return FIXME
     */
    public SecuritySolutionsPriorityEnum getPriority();
    
    /**
     * FIXME
     * @return FIXME
     */
    public List getLowList();
    
    /**
     * FIXME
     * @return FIXME
     */
    public List getMediumList();
    
    /**
     * FIXME
     * @return FIXME
     */
    public List getHighList();
    
    /**
     * FIXME
     * @param params FIXME
     */
    public void setParams(Object[] params);
    
    /**
     * FIXME
     * @return FIXME
     */
    public Object[] getParams();
    
    /**
     * Return true if this CM is enabled.
     * @return true if this CM is enabled. 
     */
    public boolean isEnabled();
    
    public void setEnabled(boolean e);
    
    public Double getCost();
    public void setCost(Double cost);
    
    public ListModel getListModel();
}
