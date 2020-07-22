package net.haesleinhuepf.clij.test;

import net.haesleinhuepf.clij.CLIJ;
import org.junit.Ignore;
import org.junit.Test;

/**
 * InitialisationTest
 * <p>
 * <p>
 * <p>
 * Author: @haesleinhuepf
 * 01 2019
 */
public class InitialisationTest {
    @Ignore // fails on AMD Vega 10
    @Test
    public void testInitContext() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("reinit " + i);
            CLIJ clij = CLIJ.getInstance();
            clij.close();
        }
    }
}
