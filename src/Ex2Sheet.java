import java.io.IOException;
// Add your documentation below:

public class Ex2Sheet implements Sheet {
    private Cell[][] table;
    // Add your code here

    // ///////////////////
    public Ex2Sheet(int x, int y) {
        table = new SCell[x][y];
        for(int i=0;i<x;i=i+1) {
            for(int j=0;j<y;j=j+1) {
                table[i][j] = new SCell("",this);
            }
        }
        eval();
    }
    public Ex2Sheet() {
        this(Ex2Utils.WIDTH, Ex2Utils.HEIGHT);
    }

    @Override
    public String value(int x, int y) {
        String ans = Ex2Utils.EMPTY_CELL;
        // Add your code here

        Cell c = get(x,y);
        if(c!=null) {ans = c.toString();}

        /////////////////////
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int width() {
        return table.length;
    }
    @Override
    public int height() {
        return table[0].length;
    }
    @Override
    public void set(int x, int y, String s) {
        Cell c = new SCell(s,this);
        table[x][y] = c;
        // Add your code here

        /////////////////////
    }
    @Override
    public void eval() {
        int[][] dd = depth();
        // Add your code here


        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                SCell cell = (SCell)this.get(j,i);
                if(cell != null) {
                    String str = cell.getData();
                    if (!str.isEmpty()) {
                        if (str.charAt(0) == '=') {
                            if (cell.isForm(str)) {
                                String form = eval(j, i);
                                this.set(j, i, form);
                                this.get(j, i).setType(Ex2Utils.FORM);
                            } else {
                                this.set(j, i, Ex2Utils.ERR_FORM);
                                this.get(j, i).setType(Ex2Utils.ERR_FORM_FORMAT);
                            }
                        } else if (cell.isNumber(str)) {
                            this.set(j, i, Double.parseDouble(str) + "");
                            this.get(j, i).setType(Ex2Utils.NUMBER);
                        } else {
                            this.set(j, i, str);
                            this.get(j, i).setType(Ex2Utils.TEXT);
                        }
                    }
                }
            }
        }
        // ///////////////////
    }

    @Override
    public boolean isIn(int xx, int yy) {
        boolean ans = xx>=0 && yy>=0;
        // Add your code here

        /////////////////////
        return ans;
    }

    @Override
    public int[][] depth() {
        int[][] ans = new int[width()][height()];
        // Add your code here

        // ///////////////////
        return ans;
    }

    @Override
    public void load(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public void save(String fileName) throws IOException {
        // Add your code here

        /////////////////////
    }

    @Override
    public String eval(int x, int y) {
        String ans = null;
        SCell cell = (SCell)this.get(x,y);
        if(cell != null) {
            ans = cell.toString();
        }
        ans = String.valueOf(cell.computeForm(ans));
        // Add your code here

        /////////////////////
        return ans;
        }
}
