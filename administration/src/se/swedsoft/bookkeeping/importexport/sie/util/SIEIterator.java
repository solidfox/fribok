package se.swedsoft.bookkeeping.importexport.sie.util;

import se.swedsoft.bookkeeping.data.SSMonth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 2006-feb-23
 * Time: 09:13:04
 */
public class SIEIterator  implements Iterator<String> {

    private static DateFormat iFormat = DateFormat.getDateInstance(DateFormat.SHORT);



    private static Pattern IntegerPattern = Pattern.compile("([-+]?[0-9]*)+");
    private static Pattern FloatPattern   = Pattern.compile("([-+]?[0-9]*.?[0-9]*)+");
    private static Pattern BooleanPattern = Pattern.compile("true|false");
    private static Pattern ArrayPattern   = Pattern.compile("[{](.*)[}]");

    private List<String> iValues;

    private int          iIndex;

    /**
     *
     * @param pLine
     */
    public SIEIterator(String pLine){
        iIndex  = 0;
        iValues = parseLine( pLine );
    }

    /**
     *
     * @param pValues
     */
    public SIEIterator(String ... pValues){
        iIndex  = 0;
        iValues = new LinkedList<String>();
        iValues.addAll(Arrays.asList(pValues));
    }

    /**
     *
     * @param pValues
     */
    public SIEIterator(List<String> pValues){
        iIndex  = 0;
        iValues = pValues;
    }

    /**
     * Returns <tt>true</tt> if the iteration has more elements. (In other
     * words, returns <tt>true</tt> if <tt>next</tt> would return an element
     * rather than throwing an exception.)
     *
     * @return <tt>true</tt> if the iterator has more elements.
     */
    public boolean hasNext() {
        return iIndex < iValues.size();
    }

    /**
     * Returns the next element in the iteration.  Calling this method
     * repeatedly until the {@link #hasNext()} method returns false will
     * return each element in the underlying collection exactly once.
     *
     * @return the next element in the iteration.
     * @throws java.util.NoSuchElementException
     *          iteration has no more elements.
     */
    public String next() {

        if(iIndex >= iValues.size()){
            throw new NoSuchElementException();
        }

        return iValues.get(iIndex++);
    }

    /**
     * Returns the next element in the iteration without incrementing
     * the current item .
     *
     * @return the next element in the iteration.
     */
    public String peek() {
        if(iIndex >= iValues.size()){
            return null;
        }
        return iValues.get(iIndex);
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



    /**
     *
     * @param pPattern
     * @return
     */
    private boolean hasNext(Pattern pPattern){
        String iNext = peek();

        if( iNext == null || iNext.length() == 0) return false;

        Matcher m = pPattern.matcher(iNext);

        return m.matches();
    }


    /**
     *
     * @return
     */
    public boolean hasNextArray() {
        return hasNext(ArrayPattern);
    }

    /**
     *
     * @return
     */
    public boolean hasNextInteger() {
        return hasNext(IntegerPattern);
    }

    /**
     *
     * @return
     */
    public boolean hasNextFloat() {
        return hasNext(FloatPattern);
    }

    /**
     *
     * @return
     */
    public boolean hasNextDouble() {
        return hasNext(FloatPattern);
    }

    /**
     *
     * @return
     */
    public boolean hasNextBoolean() {
        return hasNext(BooleanPattern);
    }

    /**
     *
     * @return
     */
    public boolean hasNextBigInteger() {
        return hasNext(IntegerPattern);
    }

    /**
     *
     * @return
     */
    public boolean hasNextBigDecimal() {
        return hasNext(FloatPattern);
    }


    /**
     *
     * @return
     */
    public boolean hasNextDate() {
        return hasNext(IntegerPattern);
    }

    /**
     *
     * @return
     */
    public boolean hasNextMonth() {
        return hasNext(IntegerPattern);
    }

    /**
     *
     * @return
     */
    public  List<String> nextArray() {
        String pLine = next();

        List<String> iObjects = new LinkedList<String>();
        for(int iIndex = 0; iIndex < pLine.length(); iIndex++){
            char c = pLine.charAt(iIndex);

            if( isOpenArray(c) || isCloseArray(c) || isWhitespace(c)) continue;

            int iStart = iIndex;

            for(iIndex = iStart; iIndex < pLine.length(); iIndex++){
                c = pLine.charAt(iIndex);

                if(isWhitespace(c) || isCloseArray(c) ){
                    break;
                }
            }
            String iString = pLine.substring(iStart, iIndex);

            if(iString.length() > 0) iObjects.add(iString);

        }

        return iObjects;
    }

    /**
     *
     * @return
     */
    public Integer nextInteger() {
        String s = next();
        Integer iNumber = null;
        try{
            if (s.length() > 0) {
                iNumber = new Integer(s);
            }
            return iNumber;
        } catch (NumberFormatException e){
            return null;
        }
    }

    /**
     *
     * @return
     */
    public Float nextFloat() {
        Double iValue = nextDouble();

        return new Float(iValue);
    }


    /**
     *
     * @return
     */
    public Boolean nextBoolean() {
        String s = next();

        return s.equals("true");
    }

    /**
     *
     * @return
     */
    public BigInteger nextBigInteger() {
        String s = next();

        return new BigInteger(s);
    }

    /**
     *
     * @return
     */
    public BigDecimal nextBigDecimal() {
        Double iValue = nextDouble();

        return new BigDecimal(iValue);
    }
    /**
     *
     * @return
     */
    public Double nextDouble() {
        String iValue = next();

        return Double.parseDouble(iValue);
    }
    /**
     *
     * @return
     */
    public Date nextDate() {
        String iValue = next();

        if(iValue != null && iValue.length() == 8) {

            iValue = iValue.substring(0,4) + '-' + iValue.substring(4,6) + '-' + iValue.substring(6,8);

            try{
                return iFormat.parse(iValue);
            }catch(ParseException ex){
                ex.printStackTrace();
            }
        }
        return new Date();
    }

    /**
     *
     * @return
     */
    public SSMonth nextMonth() {
        String iValue = next();

        if(iValue != null && iValue.length() == 6) {

            iValue = iValue.substring(0,4) + '-' + iValue.substring(4,6) + "-01";

            try{
                return new SSMonth(iFormat.parse(iValue));
            }catch(ParseException ex){
                ex.printStackTrace();
            }
        }
        return new SSMonth( new Date() );
    }



    /**
     *
     * @param c
     * @return
     */
    private boolean isWhitespace(char c){
        return c == ' ' || c == '\t';
    }

    /**
     *
     * @param c
     * @return
     */
    private boolean isString(char c){
        return c == '\"';
    }

    /**
     *
     * @param c
     * @return
     */
    private boolean isOpenArray(char c){
        return c == '{';
    }

    /**
     *
     * @param c
     * @return
     */
    private boolean isCloseArray(char c){
        return c == '}';
    }

    /**
     *
     * @param pLine
     * @return
     */
    private List<String> parseLine(String pLine) {
        List<String> iValues = new LinkedList<String>();

        for(int iIndex = 0; iIndex < pLine.length(); iIndex++){
            char c = pLine.charAt(iIndex);

            if( isWhitespace(c) ) continue;

            // Start of string, "
            if(isString(c)){
                int iStart = iIndex+1;

                for(iIndex = iStart ; iIndex < pLine.length(); iIndex++){
                    c = pLine.charAt(iIndex);

                    if(isString(c)){
                        break;
                    }
                }
                String iString = pLine.substring(iStart, iIndex);

                iValues.add(iString.trim());
            }
            // Array value, {...}
            else if( isOpenArray(c) ){
                int iStart = iIndex+1;
                int iLevel = 1;

                for(iIndex = iStart ; iIndex < pLine.length(); iIndex++){
                    c = pLine.charAt(iIndex);

                    if(isOpenArray(c)){
                        iLevel++;
                    }
                    if(isCloseArray(c)){
                        iLevel--;
                    }
                    if( iLevel == 0){
                        break;
                    }
                }
                String iString = pLine.substring(iStart, iIndex);

                iValues.add('{' + iString.trim() + '}');
            }
            // Start of token, any char exept "
            else{
                int iStart = iIndex;

                for(iIndex = iStart ; iIndex < pLine.length(); iIndex++){
                    c = pLine.charAt(iIndex);

                    if( isWhitespace(c) ){
                        break;
                    }
                }
                String iString = pLine.substring(iStart, iIndex);

                iValues.add(iString.trim());
            }
        }

        return iValues;
    }

    /**
     * Returns a string representation of the object. In general, the
     * <code>toString</code> method returns a string that
     * "textually represents" this object. The result should
     * be a concise but informative representation that is easy for a
     * person to read.
     *
     * @return a string representation of the object.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("SIEIterator {\n");
        for(String iValue: iValues){
            sb.append("  ");
            sb.append(iValue);
            sb.append('\n');
        }
        sb.append('}');

        return sb.toString();
    }


}