package se.swedsoft.bookkeeping.gui.vouchertemplate;

import se.swedsoft.bookkeeping.data.SSVoucherTemplate;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.components.SSButton;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSQueryDialog;
import se.swedsoft.bookkeeping.gui.util.filechooser.SSExcelFileChooser;
import se.swedsoft.bookkeeping.gui.util.frame.SSDefaultTableFrame;
import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.gui.vouchertemplate.util.SSVoucherTemplateTableModel;
import se.swedsoft.bookkeeping.importexport.excel.SSVoucherTemplateExporter;
import se.swedsoft.bookkeeping.importexport.excel.SSVoucherTemplateImporter;
import se.swedsoft.bookkeeping.importexport.util.SSExportException;
import se.swedsoft.bookkeeping.importexport.util.SSImportException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * User: Fredrik Stigsson
 * Date: 2006-feb-07
 * Time: 15:36:58
 */
public class SSVoucherTemplateFrame extends SSDefaultTableFrame {


    private SSTable iTable;

    private  SSVoucherTemplateTableModel iModel;

    private static SSVoucherTemplateFrame cInstance;

    public static void showFrame(SSMainFrame iMainFrame) {
        if (cInstance == null || cInstance.isClosed()) {
            cInstance = new SSVoucherTemplateFrame(iMainFrame, 400, 300);
        }
        cInstance.setInCenter(iMainFrame);
        cInstance.setVisible(true);
    }

    public static SSVoucherTemplateFrame getInstance() {
        return cInstance;
    }
    /**
     * Default constructor.
     * @param frame
     * @param width
     * @param height
     */
    public SSVoucherTemplateFrame(SSMainFrame frame, int width, int height) {
        super(frame, SSBundle.getBundle().getString("vouchertemplateframe.title"), width, height);
    }


    /**
     * This method should return a toolbar if the sub-class wants one. <P>
     * Otherwise, it may return null.
     *
     * @return A JToolBar or null.
     */
    @Override
    public JToolBar getToolBar() {
        JToolBar iToolBar = new JToolBar();


        // Importera
        // ***************************
        SSButton iButton = new SSButton("ICON_IMPORT", "vouchertemplateframe.importbutton", new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                if (iFilechooser.showOpenDialog(getMainFrame()) == JFileChooser.APPROVE_OPTION) {
                    SSVoucherTemplateImporter iImporter = new SSVoucherTemplateImporter(iFilechooser.getSelectedFile());

                    try {
                        iImporter.Import();

                    } catch (IOException ex) {
                        SSErrorDialog.showDialog(getMainFrame(), "", ex.getLocalizedMessage());
                    } catch (SSImportException ex) {
                        SSErrorDialog.showDialog(getMainFrame(), "", ex.getLocalizedMessage());
                    }
                    iModel.fireTableDataChanged();
                }

            }
        });
        iToolBar.add(iButton);

        // Exportera
        // ***************************
        iButton = new SSButton("ICON_EXPORT", "vouchertemplateframe.exportbutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                SSExcelFileChooser iFilechooser = SSExcelFileChooser.getInstance();

                List<SSVoucherTemplate> iItems;
                List<SSVoucherTemplate> iSelected = iModel.getSelectedRows(iTable);

                if( iSelected != null ) {
                    int select = SSQueryDialog.showDialog(getMainFrame(), JOptionPane.YES_NO_CANCEL_OPTION, getTitle(), SSBundle.getBundle().getString("vouchertemplateframe.import.allorselected"));
                    switch (select) {
                        case JOptionPane.YES_OPTION:
                            iItems = getVoucherTemplates(iSelected);
                            break;
                        case JOptionPane.NO_OPTION :
                            iItems = SSDB.getInstance().getVoucherTemplates();
                            break;
                        default:
                            return;
                    }
                } else {
                    iItems = SSDB.getInstance().getVoucherTemplates();
                }


                iFilechooser.setSelectedFile(new File("Konteringsmallar.xls"));

                if( iFilechooser.showSaveDialog( getMainFrame() ) == JFileChooser.APPROVE_OPTION  ){

                    SSVoucherTemplateExporter iExporter = new SSVoucherTemplateExporter( iFilechooser.getSelectedFile(), iItems );

                    try {
                        iExporter.export();
                    } catch (IOException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    } catch (SSExportException ex) {
                        SSErrorDialog.showDialog( getMainFrame(), "", ex.getLocalizedMessage() );
                    }
                }

            }
        });
        iToolBar.add(iButton);
        iToolBar.addSeparator();

        // Ta bort verifikation
        // ***************************
        iButton = new SSButton("ICON_DELETEITEM", "vouchertemplateframe.deletebutton", new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                int[] selected = iTable.getSelectedRows();
                List<SSVoucherTemplate> toDelete = iModel.getObjects(selected);
                deleteSelectedTemplates(toDelete);
            }
        });
        iToolBar.add(iButton);
        iTable.addSelectionDependentComponent(iButton);




        return iToolBar;
    }

    /**
     * This method should return the main content for the frame. <P>
     * Such as an object table.
     *
     * @return The main content for this frame.
     */
    @Override
    public JComponent getMainContent() {
        iTable = new SSTable();

        iModel = new SSVoucherTemplateTableModel();
        iModel.addColumn(SSVoucherTemplateTableModel.COLUMN_DESCRIPTION);
        iModel.addColumn(SSVoucherTemplateTableModel.COLUMN_DATE);
        iModel.setupTable(iTable);


        JPanel iPanel = new JPanel();
        iPanel.setLayout( new BorderLayout() );
        iPanel.add( new JScrollPane(iTable), BorderLayout.CENTER);
        iPanel.setBorder( BorderFactory.createEmptyBorder(2,2,2,2));

        return iPanel;
    }

    /**
     * This method should return the status bar content, if any. <P>
     *
     * @return The content for the status bar or null if none is wanted.
     */
    @Override
    public JComponent getStatusBar() {
        return null;
    }

    /**
     * Indicates whether this frame is a company data related frame. <P>
     *
     * @return A boolean value.
     */
    @Override
    public boolean isCompanyFrame() {
        return true;
    }

    /**
     * Indicates whether this frame is a year data related frame. <P>
     *
     * @return A boolean value.
     */
    @Override
    public boolean isYearDataFrame() {
        return true;
    }


    /**
     *
     * @return
     */
    private SSVoucherTemplate getSelected(){
        int selected = iTable.getSelectedRow();

        if (selected >= 0) return iModel.getObject(selected);

        return null;
    }


    /**
     *
     * @param delete
     */
    private void deleteSelectedTemplates(List<SSVoucherTemplate> delete) {
        if (delete.isEmpty()) {
            return;
        }

        SSQueryDialog iDialog = new SSQueryDialog(getMainFrame(), "vouchertemplateframe.delete");
        int iResponce = iDialog.getResponce();
        if(iResponce == JOptionPane.YES_OPTION) {
            for (SSVoucherTemplate iVoucherTemplate : delete) {
                SSDB.getInstance().deleteVoucherTemplate(iVoucherTemplate);
            }
        }
    }

    private List<SSVoucherTemplate> getVoucherTemplates(List<SSVoucherTemplate> iVoucherTemplates) {
        return SSDB.getInstance().getVoucherTemplates(iVoucherTemplates);
    }
    public void updateFrame() {
        iModel.setObjects(SSDB.getInstance().getVoucherTemplates());
    }
    public void actionPerformed(ActionEvent e)
    {

        iTable=null;
        iModel=null;
        cInstance = null;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.gui.vouchertemplate.SSVoucherTemplateFrame");
        sb.append("{iModel=").append(iModel);
        sb.append(", iTable=").append(iTable);
        sb.append('}');
        return sb.toString();
    }
}




