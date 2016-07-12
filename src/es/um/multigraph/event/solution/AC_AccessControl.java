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
 * @see <a href="https://web.nvd.nist.gov/view/800-53/Rev4/family?familyName=Access%20Control">Access Control</a>
 * @see <a href="https://web.nvd.nist.gov/view/800-53/Rev4/home">NIST Special Publication 800-53 (Rev. 4)</a>
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public enum AC_AccessControl implements Solution {
    
    GENERIC("Generic Access Control", SecuritySolutionsPriorityEnum.VOID, 0),
    /**
     * ACCESS CONTROL POLICY AND PROCEDURES
     */
    AC_01("ACCESS CONTROL POLICY AND PROCEDURES" , SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * ACCOUNT MANAGEMENT
     */
    AC_02("ACCOUNT MANAGEMENT", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * ACCESS ENFORCEMENT
     */
    AC_03("ACCESS ENFORCEMENT", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * INFORMATION FLOW ENFORCEMENT
     */
    AC_04("INFORMATION FLOW ENFORCEMENT", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * SEPARATION OF DUTIES
     */
    AC_05("SEPARATION OF DUTIES", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * LEAST PRIVILEGE
     */
    AC_06("LEAST PRIVILEGE", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * UNSUCCESSFUL LOGON ATTEMPTS
     */
    AC_07("UNSUCCESSFUL LOGON ATTEMPTS", SecuritySolutionsPriorityEnum.P2, 0),
    /**
     * SYSTEM USE NOTIFICATION
     */
    AC_08("SYSTEM USE NOTIFICATION", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * PREVIOUS LOGON (ACCESS) NOTIFICATION
     */
    AC_09("PREVIOUS LOGON (ACCESS) NOTIFICATION", SecuritySolutionsPriorityEnum.P0, 0),
    /**
     * CONCURRENT SESSION CONTROL
     */
    AC_10("CONCURRENT SESSION CONTROL", SecuritySolutionsPriorityEnum.P3, 0),
    /**
     * SESSION LOCK
     */
    AC_11("SESSION LOCK", SecuritySolutionsPriorityEnum.P3, 0),
    /**
     * SESSION TERMINATION
     */
    AC_12("SESSION TERMINATION", SecuritySolutionsPriorityEnum.P2, 0),
    /**
     * SUPERVISION AND REVIEW - ACCESS CONTROL
     */
    AC_13("SUPERVISION AND REVIEW - ACCESS CONTROL", SecuritySolutionsPriorityEnum.VOID, 0),
    /**
     * PERMITTED ACTIONS WITHOUT IDENTIFICATION OR AUTHENTICATION
     */
    AC_14("PERMITTED ACTIONS WITHOUT IDENTIFICATION OR AUTHENTICATION", SecuritySolutionsPriorityEnum.P3, 0),
    /**
     * AUTOMATED MARKING
     */
    AC_15("AUTOMATED MARKING", SecuritySolutionsPriorityEnum.VOID, 0),
    /**
     * SECURITY ATTRIBUTES
     */
    AC_16("SECURITY ATTRIBUTES", SecuritySolutionsPriorityEnum.P0, 0),
    /**
     * REMOTE ACCESS
     */
    AC_17("REMOTE ACCESS", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * WIRELESS ACCESS
     */
    AC_18("WIRELESS ACCESS", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * ACCESS CONTROL FOR MOBILE DEVICES
     */
    AC_19("ACCESS CONTROL FOR MOBILE DEVICES", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * USE OF EXTERNAL INFORMATION SYSTEMS
     */
    AC_20("USE OF EXTERNAL INFORMATION SYSTEMS", SecuritySolutionsPriorityEnum.P1, 0),
    /**
     * INFORMATION SHARING
     */
    AC_21("INFORMATION SHARING", SecuritySolutionsPriorityEnum.P2, 0),
    /**
     * PUBLICLY ACCESSIBLE CONTENT
     */
    AC_22("PUBLICLY ACCESSIBLE CONTENT", SecuritySolutionsPriorityEnum.P3, 0),
    /**
     * DATA MINING PROTECTION
     */
    AC_23("DATA MINING PROTECTION", SecuritySolutionsPriorityEnum.P0, 0),
    /**
     * ACCESS CONTROL DECISIONS
     */
    AC_24("ACCESS CONTROL DECISIONS", SecuritySolutionsPriorityEnum.P0, 0),
    /**
     * REFERENCE MONITOR
     */
    AC_25("REFERENCE MONITOR", SecuritySolutionsPriorityEnum.P0, 0),
    ;

    private AC_AccessControl() {
        this.description = "Generic Access Control Enum";
        this.priority = SecuritySolutionsPriorityEnum.VOID;
        this.max_enhancement_level = 0;
    }
        
    
    
    AC_AccessControl(String desc, SecuritySolutionsPriorityEnum pr, int max_lvl) {
        description = desc;
        priority = pr;
        max_enhancement_level = max_lvl;
        
        
    }

    private final String description;
    private final SecuritySolutionsPriorityEnum priority;
    private final int max_enhancement_level;
    private final String name = "AC_AccessControl";
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
        DefaultListModel<AC_AccessControl> result = new DefaultListModel<>();
        for(AC_AccessControl x : this.getClass().getEnumConstants()) {
            result.addElement(x);
        }
        return result;
    }
}
