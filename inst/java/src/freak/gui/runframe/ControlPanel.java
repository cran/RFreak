/*
 * This file is part of RFrEAK. For licensing and copyright information
 * please see the file COPYING in the root directory of this
 * distribution or contact <robin.nunkesser@udo.edu>.
 * 
 * This file is a copy of the original file distributed with
 * FrEAK (http://sourceforge.net/projects/freak427/).
 */

/*
 * ControlPanel.java
 *
 * Created on 6. September 2003, 18:07
 */

package freak.gui.runframe;

/**
 *
 * @author  stefan
 */
public class ControlPanel extends javax.swing.JPanel {
	
	/** Creates new form ControlPanel */
	public ControlPanel() {
		initComponents();
		//jLabelBatch.setPreferredSize(jLabelGeneration.getPreferredSize());
		//jLabelRun.setPreferredSize(jLabelGeneration.getPreferredSize());
	}
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        jSliderGeneration = new javax.swing.JSlider();
        jSliderSpeed = new javax.swing.JSlider();
        jLabelSpeed = new javax.swing.JLabel();
        jTextFieldSpeed = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jButtonSkipToStart = new javax.swing.JButton();
        jButtonSkipBack = new javax.swing.JButton();
        jButtonSuspend = new javax.swing.JButton();
        jButtonStart = new javax.swing.JButton();
        jButtonSkipForward = new javax.swing.JButton();
        jButtonSkipToEnd = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabelBatch = new javax.swing.JLabel();
        jSpinner2 = new javax.swing.JSpinner();
        jLabelRun = new javax.swing.JLabel();
        jSpinner3 = new javax.swing.JSpinner();
        jLabelGeneration = new javax.swing.JLabel();
        jSpinner1 = new javax.swing.JSpinner();

        setLayout(new java.awt.GridBagLayout());

        setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(6, 6, 6, 6)));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        add(jSliderGeneration, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(jSliderSpeed, gridBagConstraints);

        jLabelSpeed.setText("Speed:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(12, 12, 0, 6);
        add(jLabelSpeed, gridBagConstraints);

        jTextFieldSpeed.setText("1.566545");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 6);
        add(jTextFieldSpeed, gridBagConstraints);

        jPanel1.setLayout(new java.awt.GridBagLayout());

        jButtonSkipToStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Rewind16.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonSkipToStart, gridBagConstraints);

        jButtonSkipBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/StepBack16.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonSkipBack, gridBagConstraints);

        jButtonSuspend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Pause16.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonSuspend, gridBagConstraints);

        jButtonStart.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/Play16.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonStart, gridBagConstraints);

        jButtonSkipForward.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/StepForward16.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonSkipForward, gridBagConstraints);

        jButtonSkipToEnd.setIcon(new javax.swing.ImageIcon(getClass().getResource("/toolbarButtonGraphics/media/FastForward16.gif")));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(0, 2, 0, 0);
        gridBagConstraints.weightx = 1.0;
        jPanel1.add(jButtonSkipToEnd, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(12, 0, 0, 0);
        add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabelBatch.setText("Batch:");
        jPanel2.add(jLabelBatch, new java.awt.GridBagConstraints());

        jSpinner2.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel2.add(jSpinner2, gridBagConstraints);

        jLabelRun.setText("Run:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel2.add(jLabelRun, gridBagConstraints);

        jSpinner3.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel2.add(jSpinner3, gridBagConstraints);

        jLabelGeneration.setText("Generation:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(0, 12, 0, 0);
        jPanel2.add(jLabelGeneration, gridBagConstraints);

        jSpinner1.setPreferredSize(new java.awt.Dimension(70, 22));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 0);
        jPanel2.add(jSpinner1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        add(jPanel2, gridBagConstraints);

    }//GEN-END:initComponents
	
	
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButtonSkipToStart;
    private javax.swing.JButton jButtonSkipBack;
    private javax.swing.JSpinner jSpinner3;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jButtonSkipToEnd;
    private javax.swing.JTextField jTextFieldSpeed;
    private javax.swing.JButton jButtonSuspend;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JButton jButtonSkipForward;
    private javax.swing.JSlider jSliderSpeed;
    private javax.swing.JLabel jLabelGeneration;
    private javax.swing.JLabel jLabelBatch;
    private javax.swing.JSpinner jSpinner2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton jButtonStart;
    private javax.swing.JLabel jLabelRun;
    private javax.swing.JLabel jLabelSpeed;
    javax.swing.JSlider jSliderGeneration;
    // End of variables declaration//GEN-END:variables
	
}
