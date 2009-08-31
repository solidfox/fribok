/*
 * @(#)SSNewResultUnitPanel.java                v 1.0 2005-nov-08
 *
 * Time-stamp: <2005-nov-08 20:07:54 Hasse>
 *
 * Copyright (c) Trade Extensions TradeExt AB, Sweden.
 * www.tradeextensions.com, info@tradeextensions.com
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Trade Extensions ("Confidential Information"). You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Trade Extensions.
 */
package se.swedsoft.bookkeeping.gui.resultunit.panel;

import se.swedsoft.bookkeeping.data.SSNewResultUnit;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


/**
 */
public class SSResultUnitPanel {

    private SSNewResultUnit iResultUnit;

    protected JPanel iPanel;

    private JButton iOkButton;

    private JButton iCancelButton;

    protected JTextField iName;

    protected JFormattedTextField iNumber;

    protected JTextArea iDescription;


    /**
     * Default constructor.
     */
    public SSResultUnitPanel(boolean iEdit) {
        iNumber.setEnabled(!iEdit);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(iNumber.isEnabled())
                    iNumber.requestFocusInWindow();
                else
                    iName.requestFocusInWindow();
            }
        });

        iNumber.addKeyListener(new KeyAdapter(){
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
     * @param iResultUnit
     */
    public void setResultUnit(SSNewResultUnit iResultUnit) {
        this.iResultUnit = iResultUnit;

        iNumber     .setValue(iResultUnit.getNumber());
        iName       .setText (iResultUnit.getName());
        iDescription.setText (iResultUnit.getDescription());
    }

    /**
     *
     * @return
     */
    public SSNewResultUnit getResultUnit() {
        iResultUnit.setNumber       (iNumber.getValue().toString());
        iResultUnit.setName         (iName.getText());
        iResultUnit.setDescription  (iDescription.getText());
        return iResultUnit;
    }



}
