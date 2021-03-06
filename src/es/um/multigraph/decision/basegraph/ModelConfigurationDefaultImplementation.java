/**
 * MULTIGRAPH
 *
 * Project for Mattia Zago Master Thesis
 *
 * (C) 2015 - Mattia Zago
 *
 */
package es.um.multigraph.decision.basegraph;

import es.um.multigraph.core.MainClass;
import es.um.multigraph.decision.DecisionInterface;
import java.awt.Desktop;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Mattia Zago <a href="mailto:dev@zagomattia.it">dev@zagomattia.it</a>
 */
public class ModelConfigurationDefaultImplementation extends JFrame {

    MainClass parent;
    DecisionInterface model;
    
    /**
     * Creates new form ModelConfiguration
     * @param parent
     * @param model
     */
    public ModelConfigurationDefaultImplementation(MainClass parent, DecisionInterface model) {
        this.parent = parent;
        this.model = model;
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Panel_ModelData = new javax.swing.JPanel();
        ModelData_Name_Label = new javax.swing.JLabel();
        ModelData_Authors_Label = new javax.swing.JLabel();
        ModelData_Authors_Field = new javax.swing.JTextField();
        ModelData_DOI_Label = new javax.swing.JLabel();
        ModelData_DOI_Field = new javax.swing.JTextField();
        ModelData_TITLE_value = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        BTN_Node_Edit = new javax.swing.JButton();
        BTN_Node_Delete = new javax.swing.JButton();
        BTN_Node_Add = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        Nodes_List = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        BTN_Edge_Edit = new javax.swing.JButton();
        BTN_Edge_Delete = new javax.swing.JButton();
        BTN_Edge_Add = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        Edges_List = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(this.model.toString()+" CONFIGURATION FRAME");

        Panel_ModelData.setBorder(javax.swing.BorderFactory.createTitledBorder("Model Data"));

        ModelData_Name_Label.setText("Name");

        ModelData_Authors_Label.setText("Authors");

        ModelData_Authors_Field.setEditable(false);
        ModelData_Authors_Field.setText(this.model.getPaperAuthors());
        ModelData_Authors_Field.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        ModelData_DOI_Label.setText("DOI");

        ModelData_DOI_Field.setEditable(false);
        ModelData_DOI_Field.setText(this.model.getPaperDOI().toString());
        ModelData_DOI_Field.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ModelData_DOI_Field.setDisabledTextColor(new java.awt.Color(0, 0, 0));
        ModelData_DOI_Field.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ModelData_DOI_FieldMouseClicked(evt);
            }
        });

        ModelData_TITLE_value.setEditable(false);
        ModelData_TITLE_value.setText(this.model.getPaperName());
        ModelData_TITLE_value.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        ModelData_TITLE_value.setDisabledTextColor(new java.awt.Color(0, 0, 0));

        javax.swing.GroupLayout Panel_ModelDataLayout = new javax.swing.GroupLayout(Panel_ModelData);
        Panel_ModelData.setLayout(Panel_ModelDataLayout);
        Panel_ModelDataLayout.setHorizontalGroup(
            Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ModelData_Authors_Label, javax.swing.GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
                    .addComponent(ModelData_Name_Label, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                        .addComponent(ModelData_Authors_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ModelData_DOI_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ModelData_DOI_Field, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(ModelData_TITLE_value))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        Panel_ModelDataLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ModelData_Authors_Label, ModelData_DOI_Label, ModelData_Name_Label});

        Panel_ModelDataLayout.setVerticalGroup(
            Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(Panel_ModelDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ModelData_Name_Label)
                    .addComponent(ModelData_TITLE_value))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(Panel_ModelDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ModelData_Authors_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ModelData_Authors_Field)
                    .addComponent(ModelData_DOI_Label, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ModelData_DOI_Field))
                .addContainerGap())
        );

        Panel_ModelDataLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {ModelData_Authors_Label, ModelData_DOI_Label, ModelData_Name_Label});

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Model Data - Nodes"));

        BTN_Node_Edit.setText("Edit Node");
        BTN_Node_Edit.setEnabled(false);

        BTN_Node_Delete.setText("Delete Node");
        BTN_Node_Delete.setEnabled(false);
        BTN_Node_Delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_Node_DeleteActionPerformed(evt);
            }
        });

        BTN_Node_Add.setText("Add Node");
        BTN_Node_Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BTN_Node_AddActionPerformed(evt);
            }
        });

        Nodes_List.setModel(new ListModelNodes(model.getNodes()));
        Nodes_List.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                Nodes_ListValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(Nodes_List);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(BTN_Node_Add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTN_Node_Delete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTN_Node_Edit))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BTN_Node_Add, BTN_Node_Delete, BTN_Node_Edit});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTN_Node_Add)
                    .addComponent(BTN_Node_Delete)
                    .addComponent(BTN_Node_Edit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BTN_Node_Add, BTN_Node_Delete, BTN_Node_Edit});

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Model Data - Edges"));

        BTN_Edge_Edit.setText("Edit Edge");
        BTN_Edge_Edit.setEnabled(false);

        BTN_Edge_Delete.setText("Delete Edge");
        BTN_Edge_Delete.setEnabled(false);

        BTN_Edge_Add.setText("Add Edge");

        Edges_List.setModel(new ListModelEdges(model.getEdges()));
        Edges_List.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                Edges_ListValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(Edges_List);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(BTN_Edge_Add)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTN_Edge_Delete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BTN_Edge_Edit)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {BTN_Edge_Add, BTN_Edge_Delete, BTN_Edge_Edit});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BTN_Edge_Add)
                    .addComponent(BTN_Edge_Delete)
                    .addComponent(BTN_Edge_Edit))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2)
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {BTN_Edge_Add, BTN_Edge_Delete, BTN_Edge_Edit});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(Panel_ModelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Panel_ModelData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ModelData_DOI_FieldMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ModelData_DOI_FieldMouseClicked
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) 
                try {
                    desktop.browse(this.model.getPaperDOI());
        } catch (IOException ex) {
            parent.log(ex.getMessage(),this);
        }
    }//GEN-LAST:event_ModelData_DOI_FieldMouseClicked

    private void BTN_Node_AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_Node_AddActionPerformed
        JDialogNode x = Node.getJDialog(model.getNodesClass());
        x.setVisible(true);
        if(x.getResult() != null) {
            this.model.addNode(x.getResult());
            this.BTN_Node_Delete.setEnabled(true);
            this.BTN_Node_Edit.setEnabled(true);
            
            this.Nodes_List.setModel(new ListModelNodes(this.model.getNodes()));
            this.Nodes_List.setSelectedIndex(0);
            
            this.Nodes_List.repaint();
        }
        
    }//GEN-LAST:event_BTN_Node_AddActionPerformed

    private void BTN_Node_DeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BTN_Node_DeleteActionPerformed
        Node selected = (Node)this.Nodes_List.getSelectedValue();
        int response = JOptionPane.showConfirmDialog(null, "Do you want to delete this node?\n"+selected.toString(), "Confirm delete",
        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (response == JOptionPane.YES_OPTION) {
            this.model.delNode(selected);
            this.Nodes_List.setModel(new ListModelNodes(model.getNodes()));
            // TODO: delete node from graph 
        }  
        
    }//GEN-LAST:event_BTN_Node_DeleteActionPerformed

    private void Nodes_ListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_Nodes_ListValueChanged
        if(this.Nodes_List.getModel().getSize() > 0) {
            this.BTN_Node_Delete.setEnabled(true);
            this.BTN_Node_Edit.setEnabled(true);
        } else {
            this.BTN_Node_Delete.setEnabled(false);
            this.BTN_Node_Edit.setEnabled(false);
        }
    }//GEN-LAST:event_Nodes_ListValueChanged

    private void Edges_ListValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_Edges_ListValueChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_Edges_ListValueChanged


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BTN_Edge_Add;
    private javax.swing.JButton BTN_Edge_Delete;
    private javax.swing.JButton BTN_Edge_Edit;
    private javax.swing.JButton BTN_Node_Add;
    private javax.swing.JButton BTN_Node_Delete;
    private javax.swing.JButton BTN_Node_Edit;
    private javax.swing.JList Edges_List;
    private javax.swing.JTextField ModelData_Authors_Field;
    private javax.swing.JLabel ModelData_Authors_Label;
    private javax.swing.JTextField ModelData_DOI_Field;
    private javax.swing.JLabel ModelData_DOI_Label;
    private javax.swing.JLabel ModelData_Name_Label;
    private javax.swing.JTextField ModelData_TITLE_value;
    private javax.swing.JList Nodes_List;
    private javax.swing.JPanel Panel_ModelData;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    // End of variables declaration//GEN-END:variables
}
