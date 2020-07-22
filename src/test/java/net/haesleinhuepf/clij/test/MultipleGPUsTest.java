package net.haesleinhuepf.clij.test;

import ij.IJ;
import ij.ImagePlus;
import net.haesleinhuepf.clij.CLIJ;
import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import org.junit.Ignore;
import org.junit.Test;

/**
 * MultipleGPUsTest
 * <p>
 * Author: @haesleinhuepf
 * June 2019
 */
public class MultipleGPUsTest {
    @Test
    public void listDevices() {
        for (String deviceName : CLIJ.getAvailableDeviceNames()) {
            System.out.println(deviceName);
        }
    }

    @Ignore // works only in computers with multiple GPUs
    @Test
    public void testMultipleGPUsInParallel() throws InterruptedException {
        // get CLIJ instanced operating on different GPUs
        // The indices were determined by executing the listDevices() test above
        CLIJ clijIntel = new CLIJ(0);
        CLIJ clijNvidia = new CLIJ(2);

        ImagePlus imp = IJ.openImage("src/test/resources/t1-head.tif");

        // create multiple processors using different GPUs
        Processor processorIntel = new Processor(clijIntel, imp);
        Processor processorNvidia = new Processor(clijNvidia, imp);

        // start processing
        processorIntel.start();
        processorNvidia.start();

        // wait until processing is done on both GPUs
        processorIntel.join();
        processorNvidia.join();

        Thread.sleep(5000);
    }

    class Processor extends Thread {
        CLIJ clij;
        ImagePlus imp;
        public Processor(CLIJ clij, ImagePlus imp) {
            this.clij = clij;
            this.imp = imp;
        }

        @Override
        public void run() {
            String gpuName = clij.getGPUName();
            log(gpuName + " start");

            // send an image to GPU and allocate memory for another image
            ClearCLBuffer input = clij.push(imp);
            ClearCLBuffer output = clij.create(input);
            log(gpuName + " copy done");

            // apply a Gaussian blur
            float sigma = 25;
            clij.op().blur(input, output, sigma, sigma, sigma);
            log(gpuName + " blur done");

            // show results
            clij.show(output, "Result on " + clij.getGPUName());
            log(gpuName + " finished");

            // cleanup memory
            input.close();
            output.close();
        }
    }

    private void log(String text) {
        synchronized (this) {
            System.out.println("" + System.currentTimeMillis() + "\t" + text);
        }
    }

}
