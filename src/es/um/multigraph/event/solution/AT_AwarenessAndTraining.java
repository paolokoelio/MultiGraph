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
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 * 
 * @see <a href="https://web.nvd.nist.gov/view/800-53/Rev4/home">NIST Special Publication 800-53 (Rev. 4)</a>
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public enum AT_AwarenessAndTraining implements Solution {   
    
    GENERIC("Generic Awareness and Training", SecuritySolutionsPriorityEnum.VOID, 0),
    
    ;
    private boolean enabled = false;
    @Override
    public void setEnabled(boolean e) {
        this.enabled = e;
    }
    @Override
    public boolean isEnabled() {
        return this.enabled;
    }
    @Override
    public String getControl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SecuritySolutionsPriorityEnum getPriority() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List getLowList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List getMediumList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public List getHighList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setParams(Object[] params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object[] getParams() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    Double cachedCost = Double.NaN;
    @Override
    public Double getCost() {
        return this.cachedCost;
    }
    @Override
    public void setCost(Double cost) {
        this.cachedCost = cost;
    }
    
    private AT_AwarenessAndTraining() {
        this.description = "Generic Access Control Enum";
        this.priority = SecuritySolutionsPriorityEnum.VOID;
        this.max_enhancement_level = 0;
    }
        
    
    
    AT_AwarenessAndTraining(String desc, SecuritySolutionsPriorityEnum pr, int max_lvl) {
        description = desc;
        priority = pr;
        max_enhancement_level = max_lvl;
        
        
    }

    private final String description;
    private final SecuritySolutionsPriorityEnum priority;
    private final int max_enhancement_level;
    private final String name = "AT_AwarenessAndTraining";
    private int enhancement_level = 0;
    
    private Object[] params;
    
    public String toString() {
        if(this == GENERIC)
            return name() + ": " +name;
        return name() + ": " +description;
    }
    
    @Override
    public ListModel getListModel() {
        DefaultListModel<AT_AwarenessAndTraining> result = new DefaultListModel<>();
        for(AT_AwarenessAndTraining x : this.getClass().getEnumConstants()) {
            result.addElement(x);
        }
        return result;
    }
}
