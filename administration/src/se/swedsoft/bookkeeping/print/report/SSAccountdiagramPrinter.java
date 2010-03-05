package se.swedsoft.bookkeeping.print.report;

import se.swedsoft.bookkeeping.calc.math.SSAccountMath;
import se.swedsoft.bookkeeping.calc.math.SSVoucherMath;
import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSNewAccountingYear;
import se.swedsoft.bookkeeping.data.SSVoucher;
import se.swedsoft.bookkeeping.data.system.SSDB;
import se.swedsoft.bookkeeping.gui.util.SSBundle;
import se.swedsoft.bookkeeping.gui.util.model.SSDefaultTableModel;
import se.swedsoft.bookkeeping.print.SSPrinter;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Date: 2006-mar-03
 * Time: 15:00:01
 */
public class SSAccountdiagramPrinter extends SSPrinter {


    final ResourceBundle iBundle = SSBundle.getBundle();


    SSNewAccountingYear iAccountingYear;



    /**
     *
     */
    public SSAccountdiagramPrinter(  ){
        this( SSDB.getInstance().getCurrentYear()  );
    }

    /**
     *
     * @param pAccountingYear The accountingyear
     */
    public SSAccountdiagramPrinter(SSNewAccountingYear pAccountingYear ){
        super();
        iAccountingYear = pAccountingYear;

        setPageHeader  ("header_period.jrxml");
        setColumnHeader("accountdiagram.jrxml");
        setDetail      ("accountdiagram.jrxml");
    }



    /**
     * Gets the title file for this repport
     *
     * @return
     */
    @Override
    public String getTitle() {
        return SSBundle.getBundle().getString("accountdiagramreport.title");
    }

    /**
     * @return SSDefaultTableModel
     */
    @Override
    protected SSDefaultTableModel getModel() {
        addParameter("dateFrom", iAccountingYear.getFrom() );
        addParameter("dateTo"  , iAccountingYear.getTo()   );


        final List<SSVoucher> iVouchers  = iAccountingYear.getVouchers();

        final Map<SSAccount, BigDecimal> creditMinusDebetSum = SSVoucherMath.getCreditMinusDebetSumPlusIB(iAccountingYear, iVouchers);
        final Map<SSAccount, BigDecimal> debetMinusCreditSum = SSVoucherMath.getDebetMinusCreditSumPlusIB(iAccountingYear, iVouchers);

        // Maps the sru grops to the string representation
         List<AccountDiagramGroup> iGroups = createSRUGroups(creditMinusDebetSum, debetMinusCreditSum);


        SSDefaultTableModel<AccountDiagramGroup> iModel = new SSDefaultTableModel<AccountDiagramGroup>() {
            @Override
            public Class getType() {
                return AccountDiagramGroup.class;
            }

            public Object getValueAt(int rowIndex, int columnIndex) {
                Object value = null;
                AccountDiagramGroup iGroup = getObject(rowIndex);

                switch (columnIndex) {
                    case 0  :  // group.number
                        value = iGroup.getNumber();
                        break;
                    case 1: // group.description
                        value = iGroup.getDescription(iBundle);
                        break;
                    case 2: // group.sum
                        value = getGroupSum(iGroup, creditMinusDebetSum, debetMinusCreditSum);
                        break;


                    case 3: // group.one.index
                        value = iGroup.getGroup1() ;
                        break;
                    case 4: // group.one.titletext
                        value = iBundle.getString("accountdiagramreport.group.1.title." + iGroup.getGroup1() );
                        break;

                    case 5: // group.two.index
                        value = iGroup.getGroup2() ;
                        break;
                    case 6: // group.two.titletext
                        value = iBundle.getString("accountdiagramreport.group.2.title." + iGroup.getGroup2() );
                        break;
                    case 7: // group.two.sumtext
                        value = iBundle.getString("accountdiagramreport.group.2.sum." + iGroup.getGroup2() );
                        break;
                    case 8: // group.summary.titletext
                        value = iBundle.getString("accountdiagramreport.summary.title." + iGroup.getGroup2() );
                        break;
                }

                return value;
            }
        };


        iModel.addColumn("group.number");
        iModel.addColumn("group.description");
        iModel.addColumn("group.sum");

        iModel.addColumn("group.one.index");
        iModel.addColumn("group.one.titletext");

        iModel.addColumn("group.two.index");
        iModel.addColumn("group.two.titletext");
        iModel.addColumn("group.two.sumtext");


        iModel.addColumn("group.summary.titletext");


        iModel.setObjects(iGroups);

        return iModel;
    }






    /**
     *
     * @param pGroup
     * @param creditMinusDebetSum
     * @param debetMinusCreditSum
     * @return
     */
    private BigDecimal getGroupSum(AccountDiagramGroup pGroup, Map<SSAccount, BigDecimal> creditMinusDebetSum, Map<SSAccount, BigDecimal> debetMinusCreditSum){
        BigDecimal sum = new BigDecimal(0.0);

        String iSRUCode = pGroup.getSRUCode();

        switch( pGroup.getGroup1() ){
            case 1: sum = SSAccountMath.getSumBySRUCodeForAccounts(debetMinusCreditSum, iSRUCode); break;
            case 2: sum = SSAccountMath.getSumBySRUCodeForAccounts(creditMinusDebetSum, iSRUCode); break;
        }

        return sum;
    }

    /**
     *
     * @return List of groups
     */
    private List<AccountDiagramGroup> createSRUGroups(Map<SSAccount, BigDecimal> creditMinusDebetSum, Map<SSAccount, BigDecimal> debetMinusCreditSum){
        List<AccountDiagramGroup> iList = new LinkedList<AccountDiagramGroup>();

        iList.add( new AccountDiagramGroup(234, 1, 1) );
        iList.add( new AccountDiagramGroup(235, 1, 1) );
        iList.add( new AccountDiagramGroup(237, 1, 1) );
        iList.add( new AccountDiagramGroup(236, 1, 1) );
        iList.add( new AccountDiagramGroup(230, 1, 1) );
        iList.add( new AccountDiagramGroup(231, 1, 1) );
        iList.add( new AccountDiagramGroup(241, 1, 1) );
        iList.add( new AccountDiagramGroup(233, 1, 1) );

        iList.add( new AccountDiagramGroup(219, 1, 2) );
        iList.add( new AccountDiagramGroup(207, 1, 2) );
        iList.add( new AccountDiagramGroup(206, 1, 2) );
        iList.add( new AccountDiagramGroup(204, 1, 2) );
        iList.add( new AccountDiagramGroup(205, 1, 2) );
        iList.add( new AccountDiagramGroup(220, 1, 2) );
        iList.add( new AccountDiagramGroup(202, 1, 2) );
        iList.add( new AccountDiagramGroup(203, 1, 2) );
        iList.add( new AccountDiagramGroup(200, 1, 2) );

        iList.add( new AccountDiagramGroup(350, 1, 3) );
        iList.add( new AccountDiagramGroup(351, 1, 3) );
        iList.add( new AccountDiagramGroup(354, 1, 3) );
        iList.add( new AccountDiagramGroup(360, 1, 3) );
        iList.add( new AccountDiagramGroup(362, 1, 3) );
        iList.add( new AccountDiagramGroup(363, 1, 3) );
        iList.add( new AccountDiagramGroup(364, 1, 3) );
        iList.add( new AccountDiagramGroup(365, 1, 3) );
        iList.add( new AccountDiagramGroup(366, 1, 3) );
        iList.add( new AccountDiagramGroup(367, 1, 3) );
        iList.add( new AccountDiagramGroup(368, 1, 3) );
        iList.add( new AccountDiagramGroup(369, 1, 3) );

        iList.add( new AccountDiagramGroup(340, 1, 4) );
        iList.add( new AccountDiagramGroup(341, 1, 4) );
        iList.add( new AccountDiagramGroup(342, 1, 4) );
        iList.add( new AccountDiagramGroup(343, 1, 4) );
        iList.add( new AccountDiagramGroup(344, 1, 4) );
        iList.add( new AccountDiagramGroup(345, 1, 4) );
        iList.add( new AccountDiagramGroup(346, 1, 4) );
        iList.add( new AccountDiagramGroup(347, 1, 4) );
        iList.add( new AccountDiagramGroup(348, 1, 4) );
        iList.add( new AccountDiagramGroup(349, 1, 4) );
        iList.add( new AccountDiagramGroup(330, 1, 4) );
        iList.add( new AccountDiagramGroup(339, 1, 4) );

        iList.add( new AccountDiagramGroup(320, 1, 5) );
        iList.add( new AccountDiagramGroup(304, 1, 5) );
        iList.add( new AccountDiagramGroup(302, 1, 5) );

        iList.add( new AccountDiagramGroup(321, 1, 6) );
        iList.add( new AccountDiagramGroup(329, 1, 6) );
        iList.add( new AccountDiagramGroup(307, 1, 6) );
        iList.add( new AccountDiagramGroup(301, 1, 6) );
        iList.add( new AccountDiagramGroup(310, 1, 6) );
        iList.add( new AccountDiagramGroup(300, 1, 6) );
        iList.add( new AccountDiagramGroup(319, 1, 6) );
        iList.add( new AccountDiagramGroup(305, 1, 6) );

        iList.add( new AccountDiagramGroup(400, 2, 7) );
        iList.add( new AccountDiagramGroup(509, 2, 7) );
        iList.add( new AccountDiagramGroup(510, 2, 7) );
        iList.add( new AccountDiagramGroup(402, 2, 7) );
        iList.add( new AccountDiagramGroup(552, 2, 7) );
        iList.add( new AccountDiagramGroup(401, 2, 7) );

        iList.add( new AccountDiagramGroup(500, 2, 8) );
        iList.add( new AccountDiagramGroup(501, 2, 8) );

        iList.add( new AccountDiagramGroup(526, 2, 9) );
        iList.add( new AccountDiagramGroup(528, 2, 9) );
        iList.add( new AccountDiagramGroup(529, 2, 9) );
        iList.add( new AccountDiagramGroup(531, 2, 9) );
        iList.add( new AccountDiagramGroup(536, 2, 9) );
        iList.add( new AccountDiagramGroup(538, 2, 9) );
        iList.add( new AccountDiagramGroup(541, 2, 9) );
        iList.add( new AccountDiagramGroup(530, 2, 9) );

        iList.add( new AccountDiagramGroup(512, 2, 10) );
        iList.add( new AccountDiagramGroup(514, 2, 10) );
        iList.add( new AccountDiagramGroup(511, 2, 10) );
        iList.add( new AccountDiagramGroup(520, 2, 10) );
        iList.add( new AccountDiagramGroup(524, 2, 10) );
        iList.add( new AccountDiagramGroup(527, 2, 10) );

        iList.add( new AccountDiagramGroup(561, 2, 11) );
        iList.add( new AccountDiagramGroup(560, 2, 11) );
        iList.add( new AccountDiagramGroup(559, 2, 11) );
        iList.add( new AccountDiagramGroup(553, 2, 11) );
        iList.add( new AccountDiagramGroup(554, 2, 11) );
        iList.add( new AccountDiagramGroup(556, 2, 11) );

        iList.add( new AccountDiagramGroup(564, 2, 12) );
        iList.add( new AccountDiagramGroup(565, 2, 12) );
        iList.add( new AccountDiagramGroup(550, 2, 12) );
        iList.add( new AccountDiagramGroup(551, 2, 12) );
        iList.add( new AccountDiagramGroup(566, 2, 12) );
        iList.add( new AccountDiagramGroup(567, 2, 12) );
        iList.add( new AccountDiagramGroup(570, 2, 12) );
        iList.add( new AccountDiagramGroup(571, 2, 12) );
        iList.add( new AccountDiagramGroup(568, 2, 12) );
        iList.add( new AccountDiagramGroup(569, 2, 12) );

        iList.add( new AccountDiagramGroup(578, 2, 13) );
        iList.add( new AccountDiagramGroup(590, 2, 13) );
        iList.add( new AccountDiagramGroup(589, 2, 13) );
        iList.add( new AccountDiagramGroup(586, 2, 13) );
        iList.add( new AccountDiagramGroup(593, 2, 13) );
        iList.add( new AccountDiagramGroup(594, 2, 13) );
        iList.add( new AccountDiagramGroup(598, 2, 13) );

        List<AccountDiagramGroup> iFiltered = new LinkedList<AccountDiagramGroup>();

        for(AccountDiagramGroup iGroup: iList){
            BigDecimal iSum = getGroupSum(iGroup, creditMinusDebetSum, debetMinusCreditSum);
            if(iSum.signum() != 0){
                iGroup.iSum = iSum;

                iFiltered.add(iGroup);
            }
        }


        return iFiltered;
    }

    /**
     *
     */
    private class AccountDiagramGroup {

        private int iNumber;

        private BigDecimal iSum;

        private int iGroup1;

        private int iGroup2;

        private AccountDiagramGroup(int pNumber, int  pGroup1, int pGroup2){
            iNumber    = pNumber;
            iGroup1    = pGroup1;
            iGroup2    = pGroup2;
        }



        public int getNumber(){
            return iNumber;
        }

        public int getGroup1(){
            return iGroup1;
        }

        public int getGroup2(){
            return iGroup2;
        }


        public BigDecimal getSum(){
            return iSum;
        }

        public String getSRUCode(){
            return Integer.toString( iNumber );
        }


        public String getDescription(){
            return SSBundle.getBundle().getString("accountdiagramreport.title.sru." + iNumber );
        }

        public String getDescription(ResourceBundle pBundle){
            return pBundle.getString("accountdiagramreport.title.sru." + iNumber );
        }

    }


}
