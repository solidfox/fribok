package se.swedsoft.bookkeeping.data.common;


import se.swedsoft.bookkeeping.data.SSAccount;
import se.swedsoft.bookkeeping.data.SSAccountPlan;

import java.io.Serializable;
import java.util.List;


/**
 * Date: 2006-mar-28
 * Time: 14:03:11
 */
public enum SSDefaultAccount implements Serializable {
    // Kundfodran
    CustomerClaim(1510), // Inbetalning
    InPayment(1930), // Utbetalning
    OutPayment(1930), // Kontant
    Cash(1910), // Leverantörsskuld
    SupplierDebt(2440), // Moms 1
    Tax1(2611), // Moms 2
    Tax2(2621), // Moms 3
    Tax3(2631), // Försäljning
    Sales(3051), // Inköp
    Purchases(4010), // Valutakursvinst
    CurrencyProfit(3960), // Valutakursförlust
    CurrencyLoss(7960), // Öresavrundning
    Rounding(3740), // Ränteintäkt
    InterestProfit(8300), // Ingående moms
    IncommingTax(2640);
    private static final long serialVersionUID = 6209981489711135940L;

    private int iDefaultAccountNumber;

    /**
     *
     * @param pDefaultAccountNumber
     */
    SSDefaultAccount(int pDefaultAccountNumber) {
        iDefaultAccountNumber = pDefaultAccountNumber;
    }

    /**
     *
     * @return
     */
    public int getDefaultAccountNumber() {
        return iDefaultAccountNumber;
    }

    /**
     *
     * @param iDefaultAccountNumber
     */
    public void setDefaultAccountNumber(int iDefaultAccountNumber) {
        this.iDefaultAccountNumber = iDefaultAccountNumber;
    }

    /**
     *
     * @param iAccountPlan
     * @return
     */
    public SSAccount getAccount(SSAccountPlan iAccountPlan) {
        return iAccountPlan.getAccount(iDefaultAccountNumber);
    }

    /**
     *
     * @param iAccounts
     * @return
     */
    public SSAccount getAccount(List<SSAccount> iAccounts) {
        for (SSAccount iAccount : iAccounts) {
            if (iAccount.getNumber() == iDefaultAccountNumber) {
                return iAccount;
            }
        }
        return null;
    }

}
