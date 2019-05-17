package Parse;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void get() {
        int a = 4, b = 16;
        Parser temp = new Parser();

        Assert.assertEquals(b, temp.get(a));
    }
}