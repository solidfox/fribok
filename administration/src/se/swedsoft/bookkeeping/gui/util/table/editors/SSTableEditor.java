package se.swedsoft.bookkeeping.gui.util.table.editors;

import se.swedsoft.bookkeeping.gui.util.table.SSTable;
import se.swedsoft.bookkeeping.data.common.SSUnit;
import se.swedsoft.bookkeeping.data.common.SSCurrency;
import se.swedsoft.bookkeeping.data.common.SSHeadingType;
import se.swedsoft.bookkeeping.data.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * User: Andreas Lago
 * Date: 2006-jun-20
 * Time: 09:49:27
 */
public class SSTableEditor {

    public static void setDefaultEditors(SSTable iTable){
       

        iTable.setDefaultRenderer(SSProduct.class, new SSProductCellRenderer());
        iTable.setDefaultEditor  (SSProduct.class, new SSProductCellEditor  ());

        iTable.setDefaultRenderer(SSUnit.class, new SSUnitCellRenderer());
        iTable.setDefaultEditor  (SSUnit.class, new SSUnitCellEditor  ());

        iTable.setDefaultRenderer(SSCurrency.class, new SSCurrencyCellRenderer());
        iTable.setDefaultEditor  (SSCurrency.class, new SSCurrencyCellEditor  ());

        iTable.setDefaultRenderer(SSAccount.class, new SSAccountCellRenderer() );
        iTable.setDefaultEditor  (SSAccount.class, new SSAccountCellEditor()  );

        iTable.setDefaultRenderer(SSNewProject.class, new SSProjectCellRenderer() );
        iTable.setDefaultEditor  (SSNewProject.class, new SSProjectCellEditor()  );

        iTable.setDefaultRenderer(SSNewResultUnit.class, new SSResultUnitCellRenderer() );
        iTable.setDefaultEditor  (SSNewResultUnit.class, new SSResultUnitCellEditor()  );
       

        iTable.setDefaultRenderer(BigDecimal.class, new SSBigDecimalCellRenderer(2));
        iTable.setDefaultEditor  (BigDecimal.class, new SSBigDecimalCellEditor  (2));

        iTable.setDefaultRenderer(Integer.class, new SSIntegerRenderer());
        iTable.setDefaultEditor  (Integer.class, new SSIntegerCellEditor());

        iTable.setDefaultRenderer( Date.class, new SSDateCellRenderer());
        iTable.setDefaultEditor  ( Date.class, new SSDateCellEditor());

        iTable.setDefaultRenderer(SSOrder.class, new SSOrderCellRenderer() );
        iTable.setDefaultEditor  (SSOrder.class, new SSOrderCellEditor()  );

        iTable.setDefaultRenderer(SSInvoice.class, new SSInvoiceCellRenderer() );
        iTable.setDefaultEditor  (SSInvoice.class, new SSInvoiceCellEditor()  );

        iTable.setDefaultRenderer(SSSupplierInvoice.class, new SSSupplierInvoiceCellRenderer() );
        iTable.setDefaultEditor  (SSSupplierInvoice.class, new SSSupplierInvoiceCellEditor()  );
    }
}
