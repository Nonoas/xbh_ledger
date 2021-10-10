package indi.nonoas.xbh;

import org.junit.Test;

import static org.junit.Assert.*;

import java.text.SimpleDateFormat;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        System.out.println(format.format(System.currentTimeMillis()));
    }
}