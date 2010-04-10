/*
 * 2005-2010
 * $Id$
 */
package se.swedsoft.bookkeeping.gui.project.panel;

import se.swedsoft.bookkeeping.data.SSNewProject;
import se.swedsoft.bookkeeping.gui.util.datechooser.SSDateChooser;

import javax.swing.*;
import java.awt.event.*;
import java.util.Date;

/**
 * @author
 */
public class SSProjectPanel{

    private SSNewProject iProject;

    protected JPanel iPanel;

    private JButton iOkButton;

    private JButton iCancelButton;


    protected JTextArea iDescription;

    protected JTextField iName;

    protected JFormattedTextField iNumber;


    protected JCheckBox iConcluded;

    protected SSDateChooser iConcludedDate;

    /**
     * Default constructor.
     * @param iEdit
     */
    public SSProjectPanel(boolean iEdit) {
        iNumber.setEnabled(!iEdit);
        iNumber.setValue("");

        iConcluded.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent e) {
                iConcludedDate.setEnabled( iConcluded.isSelected() );
            }
        });

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(iNumber.isEnabled())
                    iNumber.requestFocusInWindow();
                else
                    iName.requestFocusInWindow();

            }
        });

        iNumber.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iName.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iName.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iDescription.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iDescription.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iConcluded.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iConcluded.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            if(iConcluded.isSelected())
                                iConcludedDate.getEditor().getComponent(0).requestFocusInWindow();
                            else
                                iOkButton.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iConcludedDate.getEditor().getComponent(0).addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOkButton.requestFocusInWindow();
                        }
                    });
                }
            }
        });

        iOkButton.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_RIGHT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iCancelButton.requestFocusInWindow();
                        }
                    });
                }
                else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            for(ActionListener al : iOkButton.getActionListeners()){
                                al.actionPerformed(null);
                            }
                        }
                    });
                }
            }
        });

        iCancelButton.addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_LEFT){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            iOkButton.requestFocusInWindow();
                        }
                    });
                }
                else if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            for(ActionListener al : iCancelButton.getActionListeners()){
                                al.actionPerformed(null);
                            }
                        }
                    });
                }
            }
        });
    }


    /**
     *
     * @return
     */
    public JPanel getPanel() {
        return iPanel;
    }


    /**
     *
     * @param pActionListener
     */

    public void addOkAction(ActionListener pActionListener) {
        iOkButton.addActionListener(pActionListener);
    }

    /**
     *
     * @param pActionListener
     */
    public void addCancelAction(ActionListener pActionListener) {
        iCancelButton.addActionListener(pActionListener);
    }

    /**
     *
     * @param iProject
     */
    public void setProject(SSNewProject iProject) {
        this.iProject = iProject;

        iNumber       .setValue   (iProject.getNumber());
        iName         .setText    (iProject.getName());
        iDescription  .setText    (iProject.getDescription());
        iConcluded    .setSelected(iProject.getConcluded() );
        iConcludedDate.setDate    (iProject.getConcludedDate() != null ? iProject.getConcludedDate() : new Date() );
    }

    /**
     *
     * @return
     */
    public SSNewProject getProject() {
        iProject.setNumber       (iNumber.getText());
        iProject.setName         (iName.getText());
        iProject.setDescription  (iDescription.getText());
        iProject.setConcluded    (iConcluded.isSelected() );
        iProject.setConcludedDate(iConcludedDate.getDate());

        return iProject;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.project.panel.SSProjectPanel");
        sb.append("{iCancelButton=").append(iCancelButton);
        sb.append(", iConcluded=").append(iConcluded);
        sb.append(", iConcludedDate=").append(iConcludedDate);
        sb.append(", iDescription=").append(iDescription);
        sb.append(", iName=").append(iName);
        sb.append(", iNumber=").append(iNumber);
        sb.append(", iOkButton=").append(iOkButton);
        sb.append(", iPanel=").append(iPanel);
        sb.append(", iProject=").append(iProject);
        sb.append('}');
        return sb.toString();
    }
}
