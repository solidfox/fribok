package se.swedsoft.bookkeeping.importexport.sie.util;

import se.swedsoft.bookkeeping.data.SSMonth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * Date: 2006-feb-22
 * Time: 10:23:20
 */
public class SIEReader implements Iterator<String> {

    private List<String> iLines;

    private int          iIndex;

    private SIEIterator  iValues;

    /**
     *
     */
    public SIEReader(){
        iLines    = new LinkedList<String>();
        iIndex    = 0;
        iValues   = null;
    }

    /**
     *
     * @param pLine
     */
    public SIEReader(String pLine){
        iLines    = new ArrayList<String>(1);
        iValues   = null;
        iLines.add(pLine);
        reset();
    }

    /**
     *
     * @param pLines
     */
    public SIEReader(List<String> pLines){
        iLines    = pLines;

        reset();
    }

    /**
     *
     */
    private void reset() {
        iIndex    = 0;

        if(iLines.isEmpty()) return;

        String iLine = iLines.get(0);

        iValues = new SIEIterator(iLine);
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNextLine() {
        return iIndex+1 < iLines.size();
    }

    /**
     * Returns the next element in the iteration.  Calling this method
     * repeatedly until the {@link #hasNext()} method returns false will
     * return each element in the underlying collection exactly once.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException
     *          iteration has no more elements.
     */
    public String nextLine() {
        iIndex++;

        if(iIndex >= iLines.size()){
            throw new NoSuchElementException();
        }
        String iLine = iLines.get(iIndex);

        iValues = new SIEIterator(iLine);

        return iLine;
    }

    /**
     * Returns the next element in the iteration without incrementing
     * the current item .
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException
     *          iteration has no more elements.
     */
    public String peekLine() {

        if(iIndex >= iLines.size()){
            throw new NoSuchElementException();
        }

        return iLines.get(iIndex);
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext() {
        return iValues.hasNext();
    }

    /**
     * Returns the next element in the iteration.  Calling this method
     * repeatedly until the {@link #hasNext()} method returns false will
     * return each element in the underlying collection exactly once.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException
     *          iteration has no more elements.
     */
    public String next() {
        return iValues.next();
    }
    /**
     * Returns the next element in the iteration.  Calling this method
     * repeatedly until the {@link #hasNext()} method returns false will
     * return each element in the underlying collection exactly once.
     *
     * @return the next element in the iteration.
     * @throws NoSuchElementException
     *          iteration has no more elements.
     */
    public String peek() {
        return iValues.peek();
    }
    /**
     * Removes from the underlying collection the last element returned by the
     * iterator (optional operation).  This method can be called only once per
     * call to <tt>next</tt>.  The behavior of an iterator is unspecified if
     * the underlying collection is modified while the iteration is in
     * progress in any way other than by calling this method.
     *
     * @throws UnsupportedOperationException if the <tt>remove</tt>
     *                                       operation is not supported by this Iterator.
     * @throws IllegalStateException         if the <tt>next</tt> method has not
     *                                       yet been called, or the <tt>remove</tt> method has already
     *                                       been called after the last call to the <tt>next</tt>
     *                                       method.
     */
    public void remove() {
    }


    public enum SIEDataType{
        STRING,
        INT,
        FLOAT,
        BOOLEAN,
        ARRAY
    }

    /**
     *
     * @param pDataTypes
     * @return
     */
    public boolean hasFields(SIEDataType ... pDataTypes) {

        SIEIterator iIterator = new SIEIterator( peekLine() );

        for(SIEDataType iDataType: pDataTypes){
            boolean hasNext         = iIterator.hasNext();
            boolean hasNextInteger  = iIterator.hasNextInteger();
            boolean hasNextFloat    = iIterator.hasNextFloat();
            boolean hasNextBoolean  = iIterator.hasNextBoolean();
            boolean hasNextArray    = iIterator.hasNextArray();

            if((iDataType == SIEDataType.STRING ) && !hasNext       ) return false;
            if((iDataType == SIEDataType.INT    ) && !hasNextInteger) return false;
            if((iDataType == SIEDataType.FLOAT  ) && !hasNextFloat  ) return false;
            if((iDataType == SIEDataType.BOOLEAN) && !hasNextBoolean) return false;
            if((iDataType == SIEDataType.ARRAY  ) && !hasNextArray  ) return false;

            iIterator.next();
        }
        return true;
    }



    /**
     *
     * @return
     */
    public boolean hasNextString() {
        return iValues.hasNext();
    }
    /**
     *
     * @return
     */
    public boolean hasNextInteger() {
        return iValues.hasNextInteger();
    }

    /**
     *
     * @return
     */
    public boolean hasNextFloat() {
        return iValues.hasNextFloat();
    }

    /**
     *
     * @return
     */
    public boolean hasNextDouble() {
        return iValues.hasNextDouble();
    }

    /**
     *
     * @return
     */
    public boolean hasNextBoolean() {
        return iValues.hasNextBoolean();
    }

    /**
     *
     * @return
     */
    public boolean hasNextBigInteger() {
        return iValues.hasNextBigInteger();
    }

    /**
     *
     * @return
     */
    public boolean hasNextBigDecimal() {
        return iValues.hasNextBigDecimal();
    }

    /**
     *
     * @return
     */
    public boolean hasNextArray() {
        return iValues.hasNextArray();
    }

    /**
     *
     * @return
     */
    public boolean hasNextDate() {
        return iValues.hasNextDate();
    }

    /**
     *
     * @return
     */
    public boolean hasNextMonth() {
        return iValues.hasNextMonth();
    }



    /**
     *
     * @return
     */
    public String nextString() {
        return iValues.next();
    }

    /**
     *
     * @return
     */
    public Integer nextInteger() {
        return iValues.nextInteger();
    }

    /**
     *
     * @return
     */
    public Float nextFloat() {
        return iValues.nextFloat();
    }

    /**
     *
     * @return
     */
    public Double nextDouble() {
        return iValues.nextDouble();
    }

    /**
     *
     * @return
     */
    public Boolean nextBoolean() {
        return iValues.nextBoolean();
    }

    /**
     *
     * @return
     */
    public BigInteger nextBigInteger() {
        return iValues.nextBigInteger();
    }

    /**
     *
     * @return
     */
    public BigDecimal nextBigDecimal() {
        return iValues.nextBigDecimal();
    }

    /**
     *
     * @return
     */
    public  List<String> nextArray() {
        return iValues.nextArray();
    }

    /**
     *
     * @return
     */
    public Date nextDate() {
        return iValues.nextDate();
    }

    /**
     *
     * @return
     */
    public SSMonth nextMonth() {
        return iValues.nextMonth();
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.sie.util.SIEReader");
        sb.append("{iIndex=").append(iIndex);
        sb.append(", iLines=").append(iLines);
        sb.append(", iValues=").append(iValues);
        sb.append('}');
        return sb.toString();
    }


}
