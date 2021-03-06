package es.um.multigraph.gui;

import es.um.multigraph.decision.basegraph.ModelConfigurationDefaultImplementation;
import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.DecisionInterface;
import es.um.multigraph.decision.basegraph.ModelConfiguration;
import es.um.multigraph.event.EventListener;
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * Sample GUI design
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class GUI extends javax.swing.JFrame implements EventListener, Runnable {

    private final MainClass parent;
    private PrintStream console;
    private MemChartPanel chart;
    /**
     * Need to use integers because of null check in printing
     */
    private Integer event_private_counter;

    private List<Class<? extends DecisionInterface>> registered_decision_modules;

    /**
     * Creates new form GUI
     *
     * @param main
     */
    public GUI(MainClass main) {
        this.parent = main;
        
        initComponents();
        
        setLocationRelativeTo(null);

        this.console = new PrintStream(new TextAreaOutputStream(this.TabPanel_Console));
        this.registered_decision_modules = new ArrayList<>();
        
        
        initAllCharts();
        chart.start();
    }

    @Override
    public void setVisible(boolean v) {
        super.setVisible(v);
        //Graph.getGraph();
    }
    
    private void initAllCharts() {
        // MEMORY USED
        chart = new MemChartPanel(this.parent, "Memory usage of solution");
        JPanel chartpanel = chart.getPanel();
        chartpanel.setSize(TabPanel_Memory.getSize());
        
        TabPanel_Memory.add(chartpanel);
        TabPanel_Memory.getParent().validate();
        
        TabPanel.setSelectedComponent(TabPanel_Memory);
    }

    public void setEventPrivateCounter(Integer counter) {
        this.event_private_counter = counter;
        if (counter != null) {
            this.StatusBar_EventStreamStatus.setText("Event Stream is running (" + event_private_counter + ")");
        } else {
            this.StatusBar_EventStreamStatus.setText("Event Stream is NOT running");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuBar2 = new javax.swing.JMenuBar();
        jMenu3 = new javax.swing.JMenu();
        jMenu4 = new javax.swing.JMenu();
        Panel_QuickAction = new javax.swing.JPanel();
        BTN_ToggleEventSource = new javax.swing.JToggleButton();
        BTN_ToggleDecisionModule = new javax.swing.JToggleButton();
        BTN_FireEvent = new javax.swing.JButton();
        Panel_ModelData = new javax.swing.JPanel();
        ModelData_Name_Label = new javax.swing.JLabel();
        ModelData_Authors_Label = new javax.swing.JLabel();
        ModelData_Authors_Field = new javax.swing.JTextField();
        ModelData_DOI_Label = new javax.swing.JLabel();
        ModelData_DOI_Field = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        ModelData_Name_Value = new javax.swing.JComboBox();
        BTN_configure = new javax.swing.JButton();
        TabPanel = new javax.swing.JTabbedPane();
        TabPanel_Memory = new javax.swing.JPanel();
        TabPanel_Time = new javax.swing.JPanel();
        TabPanel_Graph = new javax.swing.JPanel();
        TabPanel_Status = new javax.swing.JTabbedPane();
        TabPanel_ScrollConsole = new javax.swing.JScrollPane();
        TabPanel_Console = new javax.swing.JTextArea();
        StatusBar = new javax.swing.JPanel();
        StatusBar_EventStreamStatus = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        StatusBar_DecisionModuleStatus = new javax.swing.JLabel();
        MenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();

        jMenu3.setText("File");
        jMenuBar2.add(jMenu3);

        jMenu4.setText("Edit");
        jMenuBar2.add(jMenu4);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("MultiGraph Main Window");

        Panel_QuickAction.setBorder(javax.swing.BorderFactory.createTitledBorder("Quick Action"));
        Panel_QuickAction.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BTN_ToggleEventSource.setText("Start Event Stream");
        BTN_ToggleEventSource.setEnabled(false);
        BTN_ToggleEventSource.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_ToggleEventSourceActionPerformed(evt);
            }
        });
        Panel_QuickAction.add(BTN_ToggleEventSource);

        BTN_ToggleDecisionModule.setText("Start Decision Module");
        BTN_ToggleDecisionModule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_ToggleDecisionModuleActionPerformed(evt);
            }
        });
        Panel_QuickAction.add(BTN_ToggleDecisionModule);

        BTN_FireEvent.setText("Fire Event");
        BTN_FireEvent.setEnabled(false);
        BTN_FireEvent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_FireEventActionPerformed(evt);
            }
        });
        Panel_QuickAction.add(BTN_FireEvent);

        Panel_ModelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Model Data"));

        ModelData_Name_Label.setText("Name");

        ModelData_Authors_Label.setText("Authors");

        ModelData_Authors_Field.setEditable(false);
        ModelData_Authors_Field.setText("consectetur adipiscing elit");
        ModelData_Authors_Field.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        ModelData_DOI_Label.setText("DOI");

        ModelData_DOI_Field.setEditable(false);
        ModelData_DOI_Field.setText("http://dx.doi.org/0000.00000");
        ModelData_DOI_Field.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ModelData_DOI_Field.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ModelData_DOI_Field.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ModelData_DOI_FieldMouseClicked(evt);
            }
        });

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        ModelData_Name_Value.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ModelData_Name_ValueActionPerformed(evt);
            }
        });

        BTN_configure.setText("Configure");
        BTN_configure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_configureActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout Panel_ModelDataLayout = new javax.swing.GroupLayout(Panel_ModelData);
        Panel_ModelData.setLayout(Panel_ModelDataLayout);
        Panel_ModelDataLayout.setHorizontalGroup(
            Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ModelData_Name_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ModelData_DOI_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ModelData_Authors_Label, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                        .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(ModelData_Authors_Field, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                            .addComponent(ModelData_DOI_Field, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTN_configure))
                    .addComponent(ModelData_Name_Value, javax.swing.GroupLayout.PREFERRED_SIZE, 508, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        Panel_ModelDataLayout.setVerticalGroup(
            Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ModelData_Name_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ModelData_Name_Value, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                        .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(ModelData_Authors_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ModelData_Authors_Field, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                                .addComponent(ModelData_DOI_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(ModelData_DOI_Field, javax.swing.GroupLayout.Alignment.TRAILING)))
                    .addComponent(jSeparator1)
                    .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                        .addComponent(BTN_configure)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        TabPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("History Chart"));

        javax.swing.GroupLayout TabPanel_MemoryLayout = new javax.swing.GroupLayout(TabPanel_Memory);
        TabPanel_Memory.setLayout(TabPanel_MemoryLayout);
        TabPanel_MemoryLayout.setHorizontalGroup(
            TabPanel_MemoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
        );
        TabPanel_MemoryLayout.setVerticalGroup(
            TabPanel_MemoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );

        TabPanel.addTab("Memory", TabPanel_Memory);

        TabPanel_Time.setName(""); // NOI18N

        javax.swing.GroupLayout TabPanel_TimeLayout = new javax.swing.GroupLayout(TabPanel_Time);
        TabPanel_Time.setLayout(TabPanel_TimeLayout);
        TabPanel_TimeLayout.setHorizontalGroup(
            TabPanel_TimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
        );
        TabPanel_TimeLayout.setVerticalGroup(
            TabPanel_TimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );

        TabPanel.addTab("Time", TabPanel_Time);

        TabPanel_Graph.setName(""); // NOI18N

        javax.swing.GroupLayout TabPanel_GraphLayout = new javax.swing.GroupLayout(TabPanel_Graph);
        TabPanel_Graph.setLayout(TabPanel_GraphLayout);
        TabPanel_GraphLayout.setHorizontalGroup(
            TabPanel_GraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 584, Short.MAX_VALUE)
        );
        TabPanel_GraphLayout.setVerticalGroup(
            TabPanel_GraphLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 399, Short.MAX_VALUE)
        );

        TabPanel.addTab("Graph Size", TabPanel_Graph);

        TabPanel_Status.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));

        TabPanel_Console.setColumns(20);
        TabPanel_Console.setRows(5);
        TabPanel_ScrollConsole.setViewportView(TabPanel_Console);

        TabPanel_Status.addTab("Console", TabPanel_ScrollConsole);

        StatusBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT);
        flowLayout1.setAlignOnBaseline(true);
        StatusBar.setLayout(flowLayout1);

        StatusBar_EventStreamStatus.setText("Event Stream is NOT running");
        StatusBar.add(StatusBar_EventStreamStatus);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        StatusBar.add(jSeparator2);

        StatusBar_DecisionModuleStatus.setText("Decision Module is NOT running");
        StatusBar.add(StatusBar_DecisionModuleStatus);

        jMenu1.setText("File");
        MenuBar.add(jMenu1);

        jMenu2.setText("Window");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setText("Show Graph");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_K, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Show Configuration");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_configureActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        MenuBar.add(jMenu2);

        setJMenuBar(MenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TabPanel_Status)
                    .addComponent(Panel_QuickAction, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(Panel_ModelData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TabPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(StatusBar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(TabPanel)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(Panel_QuickAction, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(Panel_ModelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TabPanel_Status, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(StatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        TabPanel.getAccessibleContext().setAccessibleName("Memory Track");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BTN_ToggleEventSourceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_ToggleEventSourceActionPerformed
        if (this.BTN_ToggleEventSource.isSelected()) {
            this.parent.startEventSource();
            this.BTN_ToggleEventSource.setText("ES Running");
            this.StatusBar_EventStreamStatus.setText("Event Stream is running");
            StatusBar_EventStreamStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/um/multigraph/resources/images/loader.gif")));

        } else {
            this.parent.stopEventSource();
            this.BTN_ToggleEventSource.setText("Start Event Stream");
            this.StatusBar_EventStreamStatus.setText("Event Stream is NOT running");
            StatusBar_EventStreamStatus.setIcon(null);

        }
    }//GEN-LAST:event_BTN_ToggleEventSourceActionPerformed

    private void ModelData_DOI_FieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ModelData_DOI_FieldMouseClicked
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            try {
                desktop.browse(new URI(ModelData_DOI_Field.getText()));
            } catch (URISyntaxException | IOException e) {
                log(e.getMessage());
            }
        }
    }//GEN-LAST:event_ModelData_DOI_FieldMouseClicked

    private void ModelData_Name_ValueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ModelData_Name_ValueActionPerformed
        if(this.isVisible()) {
            int response = JOptionPane.showConfirmDialog(null, "Do you really want to change model?", "Confirm change model", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                parent.updateSelectedDecisionModule(true);
                if(this.parent.isDecisionThreadRunning())
                    this.parent.stopDecisionModule();
                
                this.BTN_ToggleDecisionModule.setText("Start Decision Module");
                this.BTN_ToggleDecisionModule.setSelected(false);
                this.StatusBar_DecisionModuleStatus.setText("Decision Module is NOT running");
                StatusBar_DecisionModuleStatus.setIcon(null);
                this.updateModelDataFields();
            }
        }
    }//GEN-LAST:event_ModelData_Name_ValueActionPerformed

    private void BTN_ToggleDecisionModuleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_ToggleDecisionModuleActionPerformed
        if (this.BTN_ToggleDecisionModule.isSelected()) {
            this.parent.startDecisionModule();
            this.BTN_ToggleDecisionModule.setText("DM Running");
            this.StatusBar_DecisionModuleStatus.setText("Decision Module is running");
            StatusBar_DecisionModuleStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/es/um/multigraph/resources/images/loader.gif")));
        } else {
            this.parent.stopDecisionModule();
            this.BTN_ToggleDecisionModule.setText("Start Decision Module");
            this.StatusBar_DecisionModuleStatus.setText("Decision Module is NOT running");
            StatusBar_DecisionModuleStatus.setIcon(null);
        }
    }//GEN-LAST:event_BTN_ToggleDecisionModuleActionPerformed

    private void BTN_FireEventActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_FireEventActionPerformed
        this.parent.fireEvent();
    }//GEN-LAST:event_BTN_FireEventActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        parent.getGraph().setVisible(true);
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void BTN_configureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_configureActionPerformed
        if(parent.isDecisionThreadRunning()) {
            JFrame x = parent.getActiveDecisionModule().getModelConfigurationFrame();
            x.setLocationRelativeTo(this);
            x.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Please start decision module first", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BTN_configureActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_FireEvent;
    private javax.swing.JToggleButton BTN_ToggleDecisionModule;
    private javax.swing.JToggleButton BTN_ToggleEventSource;
    private javax.swing.JButton BTN_configure;
    private javax.swing.JMenuBar MenuBar;
    private javax.swing.JTextField ModelData_Authors_Field;
    private javax.swing.JLabel ModelData_Authors_Label;
    private javax.swing.JTextField ModelData_DOI_Field;
    private javax.swing.JLabel ModelData_DOI_Label;
    private javax.swing.JLabel ModelData_Name_Label;
    private javax.swing.JComboBox ModelData_Name_Value;
    private javax.swing.JPanel Panel_ModelData;
    private javax.swing.JPanel Panel_QuickAction;
    private javax.swing.JPanel StatusBar;
    private javax.swing.JLabel StatusBar_DecisionModuleStatus;
    private javax.swing.JLabel StatusBar_EventStreamStatus;
    private javax.swing.JTabbedPane TabPanel;
    private javax.swing.JTextArea TabPanel_Console;
    private javax.swing.JPanel TabPanel_Graph;
    private javax.swing.JPanel TabPanel_Memory;
    private javax.swing.JScrollPane TabPanel_ScrollConsole;
    private javax.swing.JTabbedPane TabPanel_Status;
    private javax.swing.JPanel TabPanel_Time;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void log(String txt) {
        this.console.append(txt);
    }

    @Override
    public void run() {
        this.setVisible(true);
    }

    public void addDecisionModule(Class<? extends DecisionInterface> toAdd) {
        this.registered_decision_modules.add(toAdd);
        populateDecisionModuleSwitcher();
    }

    public void delDecisionModule(int toDel) {
        Class<? extends DecisionInterface> result = this.registered_decision_modules.remove(toDel);
        populateDecisionModuleSwitcher();
    }

    public Class<? extends DecisionInterface> getSelectedDecisionModule() {
        return (Class<? extends DecisionInterface>) this.ModelData_Name_Value.getSelectedItem();
    }
    
    public int getSelectedDecisionModulePos() {
        return this.ModelData_Name_Value.getSelectedIndex();
    }

    public void setSelectedDecisionModule(int selected) throws NullPointerException {
        this.ModelData_Name_Value.setSelectedItem(selected);
        
        updateModelDataFields();
    }

    private void populateDecisionModuleSwitcher() {
        this.ModelData_Name_Value.removeAllItems();
        registered_decision_modules.stream().forEach((item) -> {
            this.ModelData_Name_Value.addItem(item);
        });
        this.ModelData_Name_Value.setSelectedIndex(0);
        updateModelDataFields();
    }

    private void updateModelDataFields() {
        
        if(this.parent.getActiveDecisionModule() != null) {
            this.ModelData_Authors_Field.setText(this.parent.getActiveDecisionModule().getPaperAuthors());
            this.ModelData_DOI_Field.setText(this.parent.getActiveDecisionModule().getPaperDOI().toString());
        } else {
            this.ModelData_Authors_Field.setText("");
            this.ModelData_DOI_Field.setText("");
        }
    }
}
