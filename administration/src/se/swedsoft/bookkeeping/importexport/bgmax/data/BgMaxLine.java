package se.swedsoft.bookkeeping.importexport.bgmax.data;

/**
 * User: Andreas Lago
 * Date: 2006-aug-23
 * Time: 09:34:35
 */
public class BgMaxLine {


    private char [] iChars;

    /**
     *
     * @param iLine
     * @throws RuntimeException
     */
    public BgMaxLine(String iLine) throws RuntimeException{
        if(iLine.length()!= 80)
            throw new RuntimeException("BgMaxLine lengt mismatch: " + iLine.length() );

        iChars = new char[81];

        iLine.getChars(0, 80, iChars, 1);
    }



    /**
     *
     * @return
     */
    public String getTransaktionsKod(){
        return getField(1,2);
    }

    /**
     *
     * @param iStart
     * @return
     */
    public String getField(int iStart){
        return String.valueOf(iChars[iStart]);

    }

    /**
     *
     * @param iStart
     * @param iEnd
     * @return
     */
    public String getField(int iStart, int iEnd){
        String iField = "";

        for(int i = iStart; i <= iEnd; i++){
            iField = iField + iChars[i];
        }
        return iField.trim();

    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("se.swedsoft.bookkeeping.importexport.bgmax.data.BgMaxLine");
        sb.append("{iChars=").append(iChars == null ? "null" : "");
        for (int i = 0; iChars != null && i < iChars.length; ++i)
            sb.append(i == 0 ? "" : ", ").append(iChars[i]);
        sb.append('}');
        return sb.toString();
    }
}
