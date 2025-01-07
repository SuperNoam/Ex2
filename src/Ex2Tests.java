import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class Ex2Tests {
    @Test
    void testIsForm(){
        SCell cell = new SCell("Hey", new Ex2Sheet());
        String[] validFormulas = {
                "=1", "=1+2", "=(1+2)", "=2*3", "=(2*3)", "=(1+2)*3", "=A1", "=A2+3", "=(1+2)*((3))-1",
                "=(2+A3)/A2", "=(A1*2)", "=A1+A2*B3", "=C1/D2", "=(A1+2)*(B3-C4)", "=(2)", "=0.5", "=0+1",
                "=(1+2)/3", "=10-3", "=1.2+0.8", "=-1", "=-(A1+B2)", "=1*2+3", "=((1*2)+3)", "=2/3", "=4/2",
                "=4/2+1", "=A2-1", "=B3+B4", "=2*A1", "=(B2+1)/C3", "=(A1)+(A2)", "=(2)+(3*2)", "=1+2+3",
                "=1+2+3+4", "=(1*2)*3", "=1*2*3*4", "=(1+2)*2/2", "=(1/2)*(2/3)", "=1.2+3.4", "=1.2-0.4",
                "=1.2*2.0", "=2.2/2.0", "=2.0*(2.0/2.0)", "=((1.2*2)/2)", "=(2*A1)", "=1+((2*3)-1)", "=1+2-3",
                "=1*2+3-4", "=1+2*3-4/5", "=0+1+2", "=1-2-3", "=1*2*3", "=1/2/3", "=(A1)+(B2)", "=1+(2/3)",
                "=A1*(2/3)", "=1*2+3*4", "=(1+2)*(3+4)", "=(1+(2*3))", "=1/2+3*4", "=(1/2)+(3/4)", "=((1/2)+(3/4))",
                "=1.5+2.5-1.0", "=1.1*2.2/2.0", "=1.2/2.0+3.4", "=(1.2*2)+(2.2/2.0)", "=A1+(B2*C3)", "=(A1)+(B2+C3)",
                "=1.2+(A1)", "=1+(A1*B2)", "=A1*A2/B3", "=(1+A1)", "=1+A1", "=(1*A1)", "=A1+(2/3)", "=1+(A2)",
                "=2*(A1+A2)", "=(A1)*(B2)", "=(1+A1)*2", "=1+2+3+(A1)", "=A1+B2*(C3)", "=A1+A2+A3", "=B1*B2*B3",
                "=(1)+(2*3)", "=(1*A1)+(2*B2)", "=1+(A1/A2)", "=A1+A2-A3", "=(A1/A2)*B3", "=(1.2)+(2.2)", "=1+(2*A1)",
                "=1/(A1+A2)", "=A1/A2+(2/3)", "=A1*A2/B3", "=A1/(2*A2)", "=1*(A1/A2)", "=(1/2)*(A1/A2)",
        };
        for (String form : validFormulas) {
            assertTrue(cell.isForm(form));
        }
        System.out.println("------------------------------------------------");
        String[] invalidFormulas = {
                "=a", "=AB", "=@2", "=2+)", "=(3+1*2)-", "==1", "=1+", "=A1+2+3)", "=(1+A2", "=A1+(2-3",
                "=1*2+", "=(1/2", "=1/(2", "=1*2/3+)", "=1+2-3/", "=(1+2)*3-)", "=1-2+", "=(A1+A2-", "=1+2+3)+4",
                "=(1*A2", "=A1/(A2", "=A1-(A2", "=A1+(2", "=1+", "=1/2/", "=(1/(2)", "=1*A2/", "=1+A2-","=a100","=5+a400+2",
                "=(1+2)-(3+", "=1/(A2", "=1+(A2", "=1-(A2", "=1+(2+A2", "=1+A2/(A3", "=(A1)+(B2)-", "=A1-(B2", "=(1+A2",
                "=(1+A2-", "=(1+2)*(3)+)", "=1-2/3-", "=1/(2+3-", "=1+(A2/(B3", "=1-2/(3", "=1*2/3+", "=(1/A2", "=1-(A2",
                "=1+(A2-", "=1+(A2)+", "=1+(A2*(B3", "=1/A2*(B3", "=(A1)/(B2-", "=A1/(B2+(C3", "=(1+A2-(3+", "=(A1+A2-(3+",
                "=(1+2)-(3+", "=1+A2+", "=1+A2-", "=(A1-(B2", "=1+(A2-(B3", "=(1-(A2)-(B3", "=1/A2+(2/3-", "=1/(A2+(B3",
                "=A1/A2*(B3-", "=1+A2-", "=A1+A2+(3+", "=(A1-(A2)+(3+", "=A1-(A2)+)", "=1+A2/(B3-", "=(1+(A2+(B3-", "=1+(2*(A2)",
                "=1+(A2-(B3+", "=1+(2*(A2)-(B3+", "=1+(A2)+3*", "=1+(A2/(B3+(C4", "=(A1)-(A2)+(3+", "=(1/(A2)+(B3-", "=A1/A2/(B3-",
                "=1-(A2+(B3-", "=1/(A2+(B3)+)", "=1+(A2-(B3-(C4", "=A1+A2+(B3+(C4-", "=1/(A2)+3*(B3", "=1/(A2)-(B3+(C4", "=A1-(A2+(B3-",
                "=(1/(A2)+(B3)-(C4", "=1/(A2)+(3/A3", "=1/A2/(B3-(C4"
        };
        for (String form : invalidFormulas) {
            assertFalse(cell.isForm(form));
        }
    }


    @Test
    void testValidDependencies() throws Exception {
        Ex2Sheet mockSheet = new Ex2Sheet();
        mockSheet.set(0,0,"=12");
        mockSheet.set(0,1,"=A0+2");
        mockSheet.set(0,2,"=A1+A0");
        int[][] depth = mockSheet.depth();
        assertEquals(2, depth[0][2], "Depth should correctly compute as 2 for valid dependencies.");
    }

    @Test
    void evalTest() {

    }
}