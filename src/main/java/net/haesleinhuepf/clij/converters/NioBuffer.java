package net.haesleinhuepf.clij.converters;

import java.nio.Buffer;

/**
 * Wrapper for image data in NIO Buffers
 * In addition to the pixel data itself, holds information about the dimensions
 * of the image
 * 
 * Customary, this will be x, y, z, t, c, where x and y are always expected to be 
 * there, and z, t, and c are optional.  This class does not mandate any of 
 * the euclidean axes to be present.
 * 
 * It is the callers responsibility that the dimensions correspond to the data
 * in the buffer
 * 
 * @author nico
 */
public class NioBuffer {

   private final Buffer pixels_;
   private final long[] dimensions_;
   private long numberOfPixels_;

   public NioBuffer(final Buffer pixels, final long[] dimensions) {
      pixels_ = pixels;
      dimensions_ = dimensions;
      numberOfPixels_ = 1;
      for (int i = 0; i < dimensions.length; i++) {
         numberOfPixels_ *= dimensions[i];
      }
   }
   
   public Buffer getBuffer() { return pixels_; }
   public long[] getDimensions() { return dimensions_; }
   public long getNumberOfPixels() {return numberOfPixels_; }
   
}
