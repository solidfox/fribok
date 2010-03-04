package se.swedsoft.bookkeeping.print;

import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.SSMainFrame;

import javax.swing.event.InternalFrameListener;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.*;
import java.util.Date;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Locale;
import java.text.DateFormat;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JRParameter;

import static se.swedsoft.bookkeeping.print.SSReport.ReportField.*;
import se.swedsoft.bookkeeping.print.util.SSDefaultJasperDataSource;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.SSBookkeeping;

/**
 *
 * $Id$
 */
public abstract class SSPrinter {

    protected ResourceBundle iBundle = SSBundle.getBundle();

    private SSReport iReport;

    /**
     *
     */
    public SSPrinter() {
        iReport     = new SSReport();

        iReport.addParameter("company"  , SSDB.getInstance().getCurrentCompany().getName());
        iReport.addParameter("reportdate", new Date());

        iReport.addParameter("lastvoucher", SSVoucherMath.getMaxNumber());

    }


    /**
     *
     * @param pName
     * @param pValue
     */
    public void addParameter(String pName, Object pValue) {
        iReport.addParameter(pName, pValue);
    }

    /**
     *
     * @param pName
     * @param pValue
     */
    public void addParameter(String pName, Object pValue, boolean toString) {
       iReport.addParameter(pName, pValue == null ? null : pValue.toString());
    }

    /**
     *
     * @param pName
     * @param pDate
     */
    public void addParameter(String pName, Date pDate) {
        DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);


        iReport.addParameter(pName, pDate == null ? pDate : iFormat.format(pDate));
    }

    /**
     *
     * @param iParameters
     */
    public void addParameters(Map<String, Object> iParameters) {
        iReport.addParameters(iParameters);
    }




    /**
     *
     * @param pPageHeader
     */
    protected void setPageHeader(String pPageHeader){
        iReport.setField(PAGE_HEADER,  pPageHeader);
    }

    /**
     *
     * @param pPageFooter
     */
    protected void setPageFooter(String pPageFooter){
        iReport.setField(PAGE_FOOTER,  pPageFooter);
    }
    /**
     *
     * @param pColumnName
     */
    protected void setColumnHeader(String pColumnName){
        iReport.setField(COLUMN_HEADER,  pColumnName);
    }
      /**
     *
     * @param pColumnName
     */
    protected void setColumnFooter(String pColumnName){
        iReport.setField(COLUMN_FOOTER,  pColumnName);
    }

    /**
     *
     * @param pDetailName
     */
    protected void setDetail(String pDetailName){
        iReport.setField(DETAIL,  pDetailName);
    }

    /**
     *
     * @param pSummaryName
     */
    protected void setSummary(String pSummaryName){
        iReport.setField(SUMMARY,  pSummaryName);
    }

    /**
     *
     * @param pBackgroundName
     */
    protected void setBackground(String pBackgroundName){
        iReport.setField(BACKGROUND,  pBackgroundName);
    }
    /**
     *
     * @param pLastPageFootername
     */
    protected void setLastPageFooter(String pLastPageFootername){
        iReport.setField(LAST_PAGE_FOOTER,  pLastPageFootername);
    }




    /**
     *
     * @param pMargins
     */
    protected void setMargins(Insets pMargins){
        iReport.setMargins(pMargins);
    }

    /**
     *
     * @param iColumnCount
     */
    public void setColumnCount(int iColumnCount){
        iReport.setColumnCount(iColumnCount);
    }
       /**
     *
     * @param iColumnSpacing
     */
    public void setColumnSpacing(int iColumnSpacing){
        iReport.setColumnSpacing(iColumnSpacing);
    }

    /**
     *
     * @param iColumnWidth
     */
    public void setColumnWidth(int iColumnWidth){
       iReport.setColumnWidth(iColumnWidth);
    }

    /**
     *
     * @param top
     * @param left
     * @param bottom
     * @param right
     */
    protected void setMargins(int top, int left, int bottom, int right){
        iReport.setMargins(new Insets(top, left, bottom, right));
    }

    /**
     *
     * @param width
     * @param height
     */
    protected void setSize(int width, int height){
        iReport.setSize(new Point(width, height));
    }

    /**
     * Set the resourcebundle for the report
     *
     * @param iBundle
     */
    public void setBundle(ResourceBundle iBundle){
        this.iBundle = iBundle;

        addParameter(JRParameter.REPORT_RESOURCE_BUNDLE, iBundle );
    }

    /**
     * 
     * @return
     */
    public ResourceBundle getBundle() {
        return iBundle;
    }

    /**
     * Set the locale for the report
     *
     * @param iLocale
     */
    public void setLocale(Locale iLocale){
        addParameter(JRParameter.REPORT_LOCALE , iLocale);
    }





    /**
     * Gets the title for this report
     *
     * @return The title
     */
    public abstract String getTitle();


    /**
     *  Gets the data model for this report
     *
     * @return  SSDefaultTableModel
     */
    protected abstract SSDefaultTableModel getModel();



    /**
     * Gets the sub title  for this report
     *
     * @return The sub title
     */
    protected  String getSubTitle(){
        return null;
    }


    /**
     *
     * @return
     */
    public Map<String, Object> getParameters() {
        return iReport.getParameters();
    }



    /**
     *
     */
    public void generateReport() {
        iReport.addParameter("title"   , getTitle() );
        iReport.addParameter("subtitle", getSubTitle() );
        iReport.setModel   (getModel());

        iReport.generateReport();
    }


    /**
     *
     */
    public void preview() {
        iReport.addParameter("title"   , getTitle() );
        iReport.addParameter("subtitle", getSubTitle() );
        iReport.setModel(getModel());

        iReport.viewReport();
    }

    /**
     *
     */
    public void preview(SSMainFrame iMainFrame) {
        iReport.addParameter("title"    , getTitle() );
        iReport.addParameter("subtitle", getSubTitle() );
        iReport.setModel(getModel());
        iReport.viewReport(iMainFrame);
    }


    /**
     *
     */
    public void preview(JDialog iDialog) {
        iReport.addParameter("title"    , getTitle() );
        iReport.addParameter("subtitle", getSubTitle() );
        iReport.setModel(getModel());

        iReport.viewReport(iDialog);
    }

    /**
     *
     */
    public void preview(SSMainFrame iMainFrame, InternalFrameListener listener ) {
        iReport.addParameter("title"    , getTitle() );
        iReport.addParameter("subtitle", getSubTitle() );
        iReport.setModel(getModel());

        iReport.viewReport(iMainFrame, listener);
    }

    /**
     * 
     * @param iMainFrame
     * @param iCloseListener
     */
    public void preview(SSMainFrame iMainFrame, final ActionListener iCloseListener ) {
       preview(iMainFrame, new InternalFrameAdapter() {
           /**
            * Invoked when an internal frame has been closed.
            */
           @Override
           public void internalFrameClosed(InternalFrameEvent e) {
               ActionEvent iEvent = new ActionEvent(e.getSource(), e.getID(), "close");

               iCloseListener.actionPerformed(iEvent);

               e.getInternalFrame().removeInternalFrameListener(this);
           }
       });
    }














    /**
     *
     * @return  SSReport
     */
    public JasperPrint getPrinter(){
        return iReport.getPrinter();
    }
    /**
     *
     * @return  JasperReport
     */
    public JasperReport getReport(){
        return iReport.getReport();
    }
    /**
     *
     * @return  JasperReport
     */
    public SSDefaultJasperDataSource getDataSource(){
        return iReport.getDataSource();
    }











}
