// Add your documentation below:

import java.util.ArrayList;

public class SCell implements Cell {
    private String line;
    private int type;
    //1 - Text, 2 - Number, 3 - Form, -2 - Err form, -1 - Err cycle\Err
    private Ex2Sheet sheet;
    private CellEntry entry;
    // Add your code here

    public SCell(String s, Ex2Sheet sheet) {
        // Add your code here
        this.sheet = sheet;
        setData(s);
    }



    //@Override
    @Override
    public String toString() {
        return getData();
    }

    @Override
    public void setData(String s) {
        // Add your code here
        line = s;
        /////////////////////
    }
    @Override
    public String getData() {
        return line;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int t) {
        type = t;
    }

    @Override
    public void setOrder(int t) {
        // Add your code here

    }

    @Override
    public int getOrder() {
        // Add your code here

        return 0;
        // ///////////////////
    }



    public boolean isForm(String str){
        if (str.isEmpty() || str.length() == 1){
            return false;
        }
        str = str.replaceAll(" ", "");
        str = str.toUpperCase();
        if(str.charAt(0) != '='){
            return false;
        }

        if(str.indexOf("=") != str.lastIndexOf("=")){
            return false;
        }
        str = str.substring(1);
        try {
            Double.parseDouble(str);
            return true;
        }catch (NumberFormatException _){
        }
        for (int i = 0; i < str.length(); i++) {
            if(!validChars(str.charAt(i)) && !isLetter(str.charAt(i)) && !isDigit(str.charAt(i))){
                return false;
            }
            if(isLetter(str.charAt(i))){
                int index = closestOpOrBrackets(str,i);
                if(index == -1){
                    return false;
                }
                CellEntry current = new CellEntry(str.substring(i,index));
                if(!current.isValid()){
                    return false;
                }
            }
        }

        int bracketsCount = 0;
        char c0 = str.charAt(0);
        char cEnd = str.charAt(str.length()-1);
        if((isOp(c0) && c0 != '-') || isOp(cEnd) || isLetter(cEnd)){
            return false;
        }
        for (int i = 0; i < str.length()-1; i++) {
            if(bracketsCount < 0){
                return false;
            }
            char c = str.charAt(i);
            char cPlus = str.charAt(i+1);
            if(isOp(c)){
                if(isOp(cPlus)){
                    return false;
                }
                if (!isDigit(cPlus) && !isLetter(cPlus) && cPlus != '('){
                    return false;
                }
            }
            if(c == '('){
                if(i!=0){
                    if(isDigit(str.charAt(i-1)) || isLetter(str.charAt(i-1))||str.charAt(i-1) == '.'){
                        return false;
                    }
                }
                bracketsCount++;
                if(cPlus == ')'){
                    return false;
                }
            }else if(c == ')'){
                if(isDigit(str.charAt(i+1)) || isLetter(str.charAt(i+1)) || str.charAt(i+1) == '.'){
                    return false;
                }
                bracketsCount--;
            }

        }
        int lastIndex = str.length()-1;
        if(str.charAt(lastIndex) == '('){
            bracketsCount++;
        }else if(str.charAt(lastIndex) == ')'){
            bracketsCount--;
        }
        return bracketsCount == 0;
    }

    public double computeForm(String expression) {
        if(expression.charAt(0) == '='){
            expression = expression.substring(1);
        }
        if(expression.isEmpty()){
            return 0;
        }
        if(noOps(expression)){
            /*
            if(isLetter(expression.charAt(0))){
                if(expression.length() > 3){
                    throw new NumberFormatException();
                }else{
                    return this.computeForm(this.sheet.get(this.sheet.(expression))[this.sheet.xCell(expression)].value);
                }
            }
             */
            expression = expression.replace("(","");
            expression = expression.replace(")","");
            return Double.parseDouble(expression);
        }
        while (expression.contains("(")){
            int indexOfClosed = correctClosedBracket(expression,expression.indexOf('('));
            String subStrAfterClose = expression.substring(indexOfClosed+1);
            expression = expression.substring(0, expression.indexOf("(")) +
                    String.valueOf(computeForm(expression.substring(expression.indexOf("(")+1, indexOfClosed)))
                    + subStrAfterClose;
        }
        int additionIndex = expression.indexOf("+");
        if(additionIndex != -1){
            return computeForm(expression.substring(0, additionIndex)) + computeForm(expression.substring(additionIndex +1));
        }
        int subIndex = expression.indexOf("-");
        if(subIndex != -1) {
            if(subIndex == 0 && !isOp(expression.charAt(subIndex+1))){
                return computeForm("") - computeForm(expression.substring(subIndex +1));
            }
            else if (!isOp(expression.charAt(subIndex - 1))) {
                return computeForm(expression.substring(0, subIndex)) - computeForm(expression.substring(subIndex + 1));
            }
        }
        int multiIndex = expression.indexOf("*");
        if(multiIndex != -1){
            return computeForm(expression.substring(0, multiIndex)) * computeForm(expression.substring(multiIndex+1));
        }
        int divisionIndex = expression.indexOf("/");
        if(divisionIndex != -1){
            return computeForm(expression.substring(0, divisionIndex)) / computeForm(expression.substring(divisionIndex+1));
        }
        return -1;

    }

    public static boolean isOp(char c) {
        String ops = "+-/*";
        return ops.contains(String.valueOf(c));
    }
    public static boolean noOps(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (isOp(str.charAt(i))) {
                if (str.charAt(i) != '-') {
                    return false;
                }
            }
        }
        if(str.indexOf("-") != str.lastIndexOf("-")){
            return false;
        }
        if(str.contains("-")){
            if(str.indexOf("-") != 0){
                return false;
            }
        }
        return true;
    }
    public static int correctClosedBracket(String str,int index){
        int count = 0;
        for (int i = index; i < str.length(); i++) {
            if(str.charAt(i) == '('){
                count++;
            } else if (str.charAt(i) == ')') {
                count--;
            }
            if(count == 0){
                return i;
            }
        }
        return -1;
    }
    public static boolean validChars(char c){
        String validChars = "+-*/.()";
        return validChars.indexOf(c) != -1;
    }
    public static boolean isLetter(char c){
        String ABC = "ABCDEFHIGKLMNOPQRSTUVWXYZ";
        return ABC.indexOf(c) != -1;
    }
    public static boolean isDigit(char c){
        String digits = "0123456789";
        return digits.indexOf(c) != -1;
    }
    public static int closestOpOrBrackets(String str,int start){
        int i;
        for (i = start; i < str.length(); i++) {
            if(isOp(str.charAt(i)) || str.charAt(i) == ')'){
                return i;
            }
        }
        return i;
    }
    public boolean isNumber(String s){
        try {
            Double.parseDouble(s);
            return true;
        }catch (NumberFormatException _){
            return false;
        }
    }
}
