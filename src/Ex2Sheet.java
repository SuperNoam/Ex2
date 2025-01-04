import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;
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
                table[i][j].setType(1);
                ((SCell)table[i][j]).setEntry(new CellEntry(Ex2Utils.ABC[i]+""+j));
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
        SCell c = (SCell)get(x,y);
        if(c!=null) {
            if(c.getType() == Ex2Utils.ERR_FORM_FORMAT){
                return Ex2Utils.ERR_FORM;
            } else if (c.getType() == Ex2Utils.ERR_CYCLE_FORM) {
                return Ex2Utils.ERR_CYCLE;
            }
            ans = c.toString();
            if(c.getType() == Ex2Utils.FORM){
                try {
                    ans = String.valueOf(c.computeForm(ans));
                }catch (SCell.ErrorForm e) {
                    ans = Ex2Utils.ERR_FORM;
                }catch (SCell.ErrorCycle e) {
                    ans = Ex2Utils.ERR_CYCLE;
                }
            }else if(c.getType() == Ex2Utils.NUMBER){
                ans = Double.parseDouble(ans) + "";
            } else if (c.getType() == Ex2Utils.TEXT) {
                ans = ans;
            }
        }
        return ans;
    }

    @Override
    public Cell get(int x, int y) {
        return table[x][y];
    }

    @Override
    public Cell get(String cords) {
        Cell ans = null;
        CellEntry ce = new CellEntry(cords);
        if(ce.isValid()){
            int x = ce.getX();
            int y = ce.getY();
            ans = get(x,y);
        }
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
        SCell c = new SCell(s,this);
        c.setEntry(new CellEntry(Ex2Utils.ABC[x]+""+y));
        table[x][y] = c;
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
                    System.out.println(dd[j][i]);
                    if(dd[j][i] == -1){
                        cell.setType(Ex2Utils.ERR_CYCLE_FORM);
                        continue;
                    }
                    if (!str.isEmpty()) {
                        if (str.charAt(0) == '=') {
                            if (cell.isForm(str)) {
                                String form = eval(j, i);
                                ((SCell) this.get(j,i)).setValue(form);
                                this.get(j, i).setType(Ex2Utils.FORM);
                            } else {
                                ((SCell) this.get(j,i)).setValue(Ex2Utils.ERR_FORM);
                                this.get(j, i).setType(Ex2Utils.ERR_FORM_FORMAT);
                            }
                        } else if (cell.isNumber(str)) {
                            ((SCell) this.get(j,i)).setValue(Double.parseDouble(str)+"");
                            this.get(j, i).setType(Ex2Utils.NUMBER);
                        } else {
                            ((SCell) this.get(j,i)).setValue(str);
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
        for (int i = 0; i < this.height(); i++) {
            for (int j = 0; j < this.width(); j++) {
                SCell cell = (SCell)this.get(j,i);
                if(cell != null) {
                    if(cell.containHimself()){
                        ans[j][i] = -1;
                    }else {
                        ans[j][i] = cell.getOrder();
                    }
                }
            }
        }
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
            try {
                ans = String.valueOf(cell.computeForm(ans));
            }catch (SCell.ErrorForm e) {
                ans = Ex2Utils.ERR_FORM;
            }catch (SCell.ErrorCycle e) {
                ans = Ex2Utils.ERR_CYCLE;
            }
            cell.setValue(ans);
        }

        return ans;
        }
}
