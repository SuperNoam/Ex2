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
        if(s.isEmpty()){
            return;
        }
        if(s.charAt(0) == '=') {
            if (this.isForm(s)) {
                //evel
                this.type = Ex2Utils.FORM;
            }else {
                this.type=Ex2Utils.ERR_FORM_FORMAT;
            }
        } else if (isNumber(s)) {
            this.type=Ex2Utils.NUMBER;
        }else {
            this.type=Ex2Utils.TEXT;
        }
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
    public int getOrder() throws ErrorCycle {
        // Add your code here
        if(this.type == Ex2Utils.TEXT || this.type == Ex2Utils.NUMBER) {
            return 0;
        }
        try{
            ArrayList<String> arr = new ArrayList<String>();
            arr.add(this.entry.toString());
            return computeDepth(this.line, new ArrayList<String>());
        }catch (ErrorCycle | ErrorForm e) {
            return -1;
        }
        // ///////////////////
    }

    public int computeDepth(String line, ArrayList<String> visited) throws ErrorCycle, ErrorForm {
        // Check if the current line is already visited to detect cycles
        if (visited.contains(line)) {
            throw new ErrorCycle();
        }

        // Add the current line to visited to track it
        visited.add(line);

        int maxDepth = 0;

        // Iterate over the characters in the line to identify cell references
        for (int i = 0; i < line.length(); i++) {
            // Check for letter characters (cell references)
            if (isLetter(line.charAt(i))) {
                // Find the end of the current cell reference
                String sub = line.substring(i, closestOpOrBrackets(line, i));

                // If the substring has been already visited, a cycle exists
                if (visited.contains(sub)) {
                    throw new ErrorCycle();
                }

                // Find the cell that the reference refers to
                Cell dependentCell = this.sheet.get(sub);
                if (dependentCell != null) {
                    // If the cell exists, compute its depth recursively
                    int dependencyDepth = computeDepth(dependentCell.getData(), visited);
                    maxDepth = Math.max(maxDepth, dependencyDepth + 1);
                } else {
                    // If the cell doesn't exist, throw an ErrorForm exception
                    throw new ErrorForm();
                }
            }
        }

        // Return the maximum depth calculated from dependencies
        return maxDepth;
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

    public static boolean isOp(char c) {
        String ops = "+-/*";
        return ops.contains(String.valueOf(c));
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
    public static boolean isNumber(String s){
        try {
            Double.parseDouble(s);
            return true;
        }catch (NumberFormatException _){
            return false;
        }
    }
    public class ErrorCycle extends Exception {
        public ErrorCycle() {
            super("ErrorCycle");
        }
    }
    public class ErrorForm extends Exception {
        public ErrorForm() {
            super("ErrorForm");
        }
    }
}
