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
 * @see <a href="https://nvd.nist.gov/800-53/Rev4/family/System%20and%20Information%20Integrity">System Integrity</a>
 * @see <a href="https://web.nvd.nist.gov/view/800-53/Rev4/home">NIST Special Publication 800-53 (Rev. 4)</a>
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>, updated by Pavlo Burda <a href="mailto:p.burda@tue.nl">p.burda@tue.nl</a>
 */
public enum SI_SystemInformationIntegrity implements Solution {
    
    GENERIC("Generic System Integrity Control", SecuritySolutionsPriorityEnum.VOID, 0),
    /**
     * SYSTEM AND INFORMATION INTEGRITY POLICY AND PROCEDURES
     */
    SI_01("SYSTEM AND INFORMATION INTEGRITY POLICY AND PROCEDURES" , SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * FLAW REMEDIATION (e.g. CVE)
     */
    SI_02("FLAW REMEDIATION", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     *  MALICIOUS CODE PROTECTION
     */
    SI_03("MALICIOUS CODE PROTECTION", SecuritySolutionsPriorityEnum.P1, 0),
    ;

    private SI_SystemInformationIntegrity() {
        this.description = "Generic System Integrity Enum";
        this.priority = SecuritySolutionsPriorityEnum.VOID;
        this.max_enhancement_level = 0;
    }
        
    
    
    SI_SystemInformationIntegrity(String desc, SecuritySolutionsPriorityEnum pr, int max_lvl) {
        description = desc;
        priority = pr;
        max_enhancement_level = max_lvl;
        
        
    }

    private final String description;
    private final SecuritySolutionsPriorityEnum priority;
    private final int max_enhancement_level;
    private final String name = "SI_SystemIntegrity";
    private int enhancement_level = 0;
    
    private Object[] params;
    
    

    public int getEnhancement_level() {
        return enhancement_level;
    }

    public void setEnhancement_level(int enhancement_level) {
        this.enhancement_level = enhancement_level;
    }

    @Override
    public String getControl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SecuritySolutionsPriorityEnum getPriority() {
        return this.priority;
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
        this.params = params;
    }

    @Override
    public Object[] getParams() {
        return this.params;
    }

    private boolean enabled = false;
    
    @Override
    public void setEnabled(boolean e) {
        this.enabled = e;
    }
    
    @Override
    public boolean isEnabled() {
        return this.enabled;
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
    
    public String toString() {
        if(this == GENERIC)
            return name() + ": " +name;
        return name() + ": " +description;
    }
    
    @Override
    public ListModel getListModel() {
        DefaultListModel<SI_SystemInformationIntegrity> result = new DefaultListModel<>();
        for(SI_SystemInformationIntegrity x : this.getClass().getEnumConstants()) {
            result.addElement(x);
        }
        return result;
    }
}
