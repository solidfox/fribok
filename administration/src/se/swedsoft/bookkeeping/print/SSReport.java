package se.swedsoft.bookkeeping.print;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.view.JasperViewer;
import se.swedsoft.bookkeeping.gui.SSMainFrame;
import se.swedsoft.bookkeeping.gui.util.dialogs.SSErrorDialog;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.print.util.SSReportCache;
import se.swedsoft.bookkeeping.print.view.SSJasperPreviewFrame;
import se.swedsoft.bookkeeping.util.SSException;

import javax.swing.*;
import javax.swing.event.InternalFrameListener;
import java.awt.*;
import java.lang.String;
import java.util.*;
import java.util.List;

/**
 * Date: 2006-feb-16
 * Time: 10:50:24
 */
public class SSReport {

    private static SSReportCache cReportCache = SSReportCache.getInstance();


    /**
     *
     */
    public enum ReportField{
        PAGE_HEADER,
        PAGE_FOOTER,
        COLUMN_HEADER,
        COLUMN_FOOTER,
        DETAIL,
        SUMMARY,
        BACKGROUND,
        LAST_PAGE_FOOTER

    }







    private JasperPrint iPrinter;

    private JasperReport iReport;

    private Insets iMargins;

    private Point iSize;

    private int iColumnCount;

    private int iColumnSpacing;

    private int iColumnWidth;

    private JasperDesign iDesign;

    private SSDefaultJasperDataSource iDataSource;

    protected Map<String, Object     > iParameters;

    protected Map<ReportField, String> iFields;



    public SSReport( ) {
        // Set the default SAX parser for the system.
        System.setProperty("javax.xml.parsers.SAXParserFactory", "org.apache.xerces.jaxp.SAXParserFactoryImpl");

        iDesign        = null;
        iPrinter       = null;
        iReport        = null;
        iDataSource    = null;
        iMargins       = new Insets(30,20,30,20);
        iSize          = new Point(595, 842);
        iColumnCount   = 1;
        iColumnSpacing = 0;
        iColumnWidth   = 555;
        iParameters    = new HashMap<String     , Object>();
        iFields        = new HashMap<ReportField, String>();
    }

    /**
     *
     * @param pModel
     */
    public void setModel(SSDefaultTableModel pModel){
        iDataSource = new SSDefaultJasperDataSource(pModel);
    }


    /**
     *
     * @param pName
     * @param pValue
     */
    public void addParameter(String pName, Object pValue) {
        iParameters.put(pName, pValue);
    }

    /**
     *
     * @param iParameters
     */
    public void addParameters(Map<String, Object> iParameters) {
        iParameters.putAll(iParameters);
    }

    /**
     *
     * @param pName
     * @param pField
     */
    public void setField( ReportField pField, String pName){
        iFields.put(pField, pName);

    }

    /**
     *
     * @param pMargins
     */
    protected void setMargins(Insets pMargins){
        iMargins = pMargins;
    }

    /**
     *
     * @param iSize
     */
    public void setSize(Point iSize) {
        this.iSize = iSize;
    }

    /**
     *
     * @param iColumnCount
     */
    public void setColumnCount(int iColumnCount){
        this.iColumnCount = iColumnCount;
    }
    /**
     *
     * @param iColumnSpacing
     */
    public void setColumnSpacing(int iColumnSpacing){
        this.iColumnSpacing = iColumnSpacing;
    }

    /**
     *
     * @param iColumnWidth
     */
    public void setColumnWidth(int iColumnWidth){
        this.iColumnWidth = iColumnWidth;
    }

    /**
     *
     * @throws SSException
     */
    public void generateReport()  throws SSException{
        if(iDesign == null){
            try{
                compileDesign();
            } catch(SSException ex){
                iPrinter = getEmptyReport();
                throw ex;
            }
        }
        try{
            iReport  = JasperCompileManager.compileReport(iDesign);

            iPrinter = JasperFillManager.fillReport(iReport, iParameters, iDataSource);

            if(iPrinter.getPages().isEmpty()) {
                iPrinter = getNoPagesReport();
            }

        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    /**
     *
     * @throws SSException
     */
    private void compileDesign() throws SSException{
        List<JRReportFont>    theFonts    = new LinkedList<JRReportFont>();

        // Contains the fields from all subreports
        List<JRField>     theFields     = new LinkedList<JRField>();
        // Contains the parameters from all subreport
        List<JRParameter> theParameters = new LinkedList<JRParameter>();

        List<JRVariable> theVariables = new LinkedList<JRVariable>();

        List<JRGroup>    theGroups    = new LinkedList<JRGroup>();

        JRBand iPageHeader   = null;
        JRBand iPageFooter   = null;
        JRBand iColumnHeader = null;
        JRBand iDetail       = null;
        JRBand iSummary      = null;
        JRBand iBackground   = null;
        JRBand iColumnFooter = null;
        JRBand iLastPageFooter = null;


        // Page header
        if( iFields.containsKey( ReportField.PAGE_HEADER ) ){
            iPageHeader = getField(ReportField.PAGE_HEADER, theFields, theParameters, theVariables, theGroups, theFonts);
        }
        // Page footer
        if( iFields.containsKey( ReportField.PAGE_FOOTER ) ){
            iPageFooter = getField(ReportField.PAGE_FOOTER, theFields, theParameters, theVariables, theGroups, theFonts);
        }

        // Column header
        if( iFields.containsKey( ReportField.COLUMN_HEADER ) ){
            iColumnHeader = getField(ReportField.COLUMN_HEADER, theFields, theParameters, theVariables, theGroups, theFonts);
        }
        // Column footer
        if( iFields.containsKey( ReportField.COLUMN_FOOTER ) ){
            iColumnFooter = getField(ReportField.COLUMN_FOOTER, theFields, theParameters, theVariables, theGroups, theFonts);
        }

        // Detail
        if( iFields.containsKey( ReportField.DETAIL ) ){
            iDetail = getField(ReportField.DETAIL, theFields, theParameters, theVariables, theGroups, theFonts);
        }

        // Summary
        if( iFields.containsKey( ReportField.SUMMARY ) ){
            iSummary = getField(ReportField.SUMMARY, theFields, theParameters, theVariables, theGroups, theFonts);
        }

        // Background
        if( iFields.containsKey( ReportField.BACKGROUND ) ){
            iBackground = getField(ReportField.BACKGROUND, theFields, theParameters, theVariables, theGroups, theFonts);
        }
        // Last page footer
        if( iFields.containsKey( ReportField.LAST_PAGE_FOOTER ) ){
            iLastPageFooter = getField(ReportField.LAST_PAGE_FOOTER, theFields, theParameters, theVariables, theGroups, theFonts);
        }



        iDesign = new JasperDesign();
        iDesign.setResourceBundle("book");
        iDesign.setName("JasperDocument");
        iDesign.setWhenNoDataType(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL); //JRReport.WHEN_NO_DATA_TYPE_NO_PAGES

        iDesign.setTopMargin    (iMargins.top   );
        iDesign.setBottomMargin (iMargins.bottom);
        iDesign.setLeftMargin   (iMargins.left  );
        iDesign.setRightMargin  (iMargins.right );

        iDesign.setPageWidth ( iSize.x );
        iDesign.setPageHeight( iSize.y );

        iDesign.setColumnCount  ( iColumnCount );
        iDesign.setColumnSpacing( iColumnSpacing);
        iDesign.setColumnWidth  ( iColumnWidth);

        iDesign.setPageHeader  (iPageHeader);
        iDesign.setPageFooter  (iPageFooter);
        iDesign.setColumnHeader(iColumnHeader);
        iDesign.setColumnFooter(iColumnFooter);
        iDesign.setDetail      (iDetail);
        iDesign.setSummary     (iSummary);
        iDesign.setBackground  (iBackground);
        iDesign.setLastPageFooter( iLastPageFooter );

        System.out.println("Report:");
        System.out.println("  Height: " + iSize.y );
        System.out.println("  Width : " + iSize.x );
        System.out.println("Margins:");
        System.out.println("  Top   : " + iMargins.top );
        System.out.println("  Bottom: " + iMargins.bottom );
        System.out.println("  Left  : " + iMargins.left );
        System.out.println("  Right : " + iMargins.right );
        System.out.println("Band heights:");
        System.out.println("  PageHeader    : " + (iPageHeader     == null ? 0 : iPageHeader.getHeight() ));
        System.out.println("  PageFooter    : " + (iPageFooter     == null ? 0 : iPageFooter.getHeight() ));
        System.out.println("  ColumnHeader  : " + (iColumnHeader   == null ? 0 : iColumnHeader.getHeight() ));
        System.out.println("  ColumnFooter  : " + (iColumnFooter   == null ? 0 : iColumnFooter.getHeight() ));
        System.out.println("  Detail        : " + (iDetail         == null ? 0 : iDetail.getHeight() ));
        System.out.println("  Summary       : " + (iSummary        == null ? 0 : iSummary.getHeight() ));
        System.out.println("  Background    : " + (iBackground     == null ? 0 : iBackground.getHeight() ));
        System.out.println("  LastPageFooter: " + (iLastPageFooter == null ? 0 : iLastPageFooter .getHeight() ));


        try{

            // Add all fields to the design
            for(JRReportFont iFont: theFonts ){
                try {
                    iDesign.addFont(iFont);
                } catch (JRException ignored) {}
            }

            // Add all fields to the design
            for(JRField iField: theFields ){
                try{
                    iDesign.addField(iField);
                } catch(JRException ignored){
                }
            }
            // Add all parameters to the design
            for(JRParameter iParameter: theParameters ){
                try{
                    iDesign.addParameter(iParameter);
                } catch(JRException ignored){
                }
            }

            // Add all groups to the desgin
            for(JRGroup iGroup: theGroups){
                try{
                    if ( iDesign.getMainDesignDataset().getGroupsMap().containsKey(iGroup.getName()) ) continue;

                    iDesign.getMainDesignDataset().getGroupsList().add(iGroup);
                    iDesign.getMainDesignDataset().getGroupsMap ().put(iGroup.getName(), iGroup);

                } catch(Exception ignored){
                }
            }

            // Add all variables to the design
            for(JRVariable iVariable: theVariables ){
                try{
                    if ( iDesign.getMainDesignDataset().getVariablesMap ().containsKey(iVariable.getName()) ) continue;

                    iDesign.getMainDesignDataset().getVariablesList().add(iVariable);
                    iDesign.getMainDesignDataset().getVariablesMap ().put(iVariable.getName(), iVariable);



                } catch(Exception ignored){
                }
            }


        } catch (Throwable t) {
            t.printStackTrace();

        }

    }


    /**
     *
     * @param pField
     * @param pFields
     * @param pParameters
     * @param pVariables
     * @param pGroups
     * @param pFonts
     * @return
     * @throws SSException
     */
    private JRBand getField(ReportField pField, List<JRField> pFields, List<JRParameter> pParameters, List<JRVariable> pVariables, List<JRGroup> pGroups, List<JRReportFont> pFonts) throws SSException{

        String pReportName = iFields.get(pField);

        try {
            // Get the report
            JasperReport iReport = cReportCache.getReport(pReportName);

            // Add all parameters
            if( iReport.getParameters() != null ){
                pParameters.addAll(Arrays.asList(iReport.getParameters()));
            }

            // Add all fields
            if( iReport.getFields() != null ){
                pFields.addAll(Arrays.asList(iReport.getFields()));
            }

            // Add all variables
            if( iReport.getVariables() != null ){
                pVariables.addAll(Arrays.asList(iReport.getVariables()));
            }

            // Add all groups
            if( iReport.getGroups() != null ){

                pGroups.addAll(Arrays.asList(iReport.getGroups()));
            }

            // Add all fonts
            if( iReport.getFonts() != null ){

                pFonts.addAll(Arrays.asList(iReport.getFonts()));
            }


            switch(pField){
                case PAGE_HEADER:
                    return iReport.getPageHeader();
                case PAGE_FOOTER:
                    return iReport.getPageFooter();
                case COLUMN_HEADER:
                    return iReport.getColumnHeader();
                case COLUMN_FOOTER:
                    return iReport.getColumnFooter();
                case DETAIL:
                    return iReport.getDetail();
                case SUMMARY:
                    return iReport.getSummary();
                case BACKGROUND:
                    return iReport.getBackground();
                case LAST_PAGE_FOOTER:
                    return iReport.getLastPageFooter();

            }

            return null;
        }catch(SSException ex){
            throw ex;
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        return null;
    }






    /**
     * Generates a default report to show when no pages are avaiable
     *
     * @return The report
     */
    private JasperPrint getNoPagesReport(){
        JasperDesign iDesign = new JasperDesign();
        iDesign.setResourceBundle("book");
        iDesign.setName("NoPages");
        iDesign.setWhenNoDataType(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL );

        JRDesignBand iTitle = new JRDesignBand();
        iTitle.setHeight(25);


        JRDesignTextField iTextField = new JRDesignTextField();
        iTextField.setX(0);
        iTextField.setY(0);
        iTextField.setWidth(515);
        iTextField.setHeight(25);
        iTextField.setForecolor(new Color(255, 0, 0));

        iTextField.setHorizontalAlignment( JRAlignment.HORIZONTAL_ALIGN_LEFT  );
        iTextField.setFontSize(12);
        iTextField.setItalic(true);

        JRDesignExpression iEpression = new JRDesignExpression();
        iEpression.setValueClass(String.class);
        iEpression.setText("$R{report.nopages}");

        iTextField.setExpression(iEpression);

        iTitle.addElement( iTextField );

        iDesign.setTitle(iTitle);
        try {
            iReport  = JasperCompileManager.compileReport(iDesign);

            return JasperFillManager.fillReport(iReport, iParameters, iDataSource);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }

    /**
     * Generates an empty report
     * 
     * @return
     */
    private JasperPrint getEmptyReport(){
        JasperDesign iDesign = new JasperDesign();
        iDesign.setResourceBundle("book");
        iDesign.setName("Empty");
        iDesign.setWhenNoDataType(JRReport.WHEN_NO_DATA_TYPE_ALL_SECTIONS_NO_DETAIL );

        try {
            iReport  = JasperCompileManager.compileReport(iDesign);

            return JasperFillManager.fillReport(iReport, iParameters, iDataSource);

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return null;
    }



    /**
     *
     * @return
     */
    public JasperPrint getPrinter(){
        return iPrinter;
    }

    /**
     *
     * @return
     */
    public JasperReport getReport(){
        return iReport;
    }


    /**
     *
     * @return
     */
    public Map<String, Object> getParameters() {
        return iParameters;
    }


    /**
     *
     * @param iName
     * @return
     * @param iName
     */
    public Object getParameter(String iName) {
        return iParameters.get(iName);
    }


    /**
     *
     * @return
     */
    public SSDefaultJasperDataSource getDataSource(){
        return iDataSource;
    }


    /**
     *
     */
    public void viewReport()  {
        try{
            generateReport();
        }  catch(SSException ex){
            ex.printStackTrace();
        }

        JasperViewer.viewReport(iPrinter, false);
    }

    /**
     *
     * @param iMainFrame
     */
    public void viewReport(SSMainFrame iMainFrame)  {
        try{
            generateReport();
        }  catch(SSException ex){
            new SSErrorDialog(iMainFrame, "exceptiondialog", ex.getLocalizedMessage());
        }

        SSJasperPreviewFrame iJasperPreviewFrame = new SSJasperPreviewFrame(iMainFrame, 800, 600);

        iJasperPreviewFrame.setInCenter(iMainFrame);
        iJasperPreviewFrame.setReport (this);
        iJasperPreviewFrame.setPrinter(iPrinter);
        iJasperPreviewFrame.setVisible(true);
    }

      /**
       *
       * @param iDialog
       */
    public void viewReport(JDialog iDialog)  {
        try{
            generateReport();
        }  catch(SSException ex){
            new SSErrorDialog(SSMainFrame.getInstance(), "exceptiondialog", ex.getLocalizedMessage());
        }

        SSJasperPreviewFrame iJasperPreviewFrame = new SSJasperPreviewFrame(SSMainFrame.getInstance(), 800, 600);

        iJasperPreviewFrame.setInCenter(iDialog);
        iJasperPreviewFrame.setReport (this);
        iJasperPreviewFrame.setPrinter(iPrinter);
        iJasperPreviewFrame.setVisible(true);
    }

    /**
     *
     * @param iMainFrame
     * @param listener
     */
    public void viewReport(SSMainFrame iMainFrame, InternalFrameListener listener) {
        try{
            generateReport();
        }  catch(SSException ex){
            new SSErrorDialog(iMainFrame, "exceptiondialog", ex.getLocalizedMessage());
        }

        SSJasperPreviewFrame iJasperPreviewFrame = new SSJasperPreviewFrame(iMainFrame, 800, 600);

        iJasperPreviewFrame.addInternalFrameListener(listener);
        iJasperPreviewFrame.setReport (this);
        iJasperPreviewFrame.setPrinter(iPrinter);
        iJasperPreviewFrame.setInCenter(iMainFrame);
        iJasperPreviewFrame.setVisible(true);
    }

}
