/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.event.solution.dummy;

import es.um.multigraph.conf.SecuritySolutionsPriorityEnum;
import es.um.multigraph.event.solution.Solution;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public enum DummySolution implements Solution {
    GENERIC("Generic Dummy Solution", SecuritySolutionsPriorityEnum.VOID, 0),
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
    private DummySolution() {
        this.description = "Generic Access Control Enum";
        this.priority = SecuritySolutionsPriorityEnum.VOID;
        this.max_enhancement_level = 0;
    }
        
    
    
    DummySolution(String desc, SecuritySolutionsPriorityEnum pr, int max_lvl) {
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
        DefaultListModel<DummySolution> result = new DefaultListModel<>();
        for(DummySolution x : this.getClass().getEnumConstants()) {
            result.addElement(x);
        }
        return result;
    }
}
