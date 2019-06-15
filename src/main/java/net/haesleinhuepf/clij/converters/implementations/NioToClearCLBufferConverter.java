package net.haesleinhuepf.clij.converters.implementations;


import net.haesleinhuepf.clij.clearcl.ClearCLBuffer;
import net.haesleinhuepf.clij.coremem.enums.NativeTypeEnum;
import net.haesleinhuepf.clij.converters.AbstractCLIJConverter;
import net.haesleinhuepf.clij.converters.CLIJConverterPlugin;
import org.scijava.plugin.Plugin;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import net.haesleinhuepf.clij.converters.NioBuffer;


/**
 * ImagePlusToRandomAccessibleIntervalConverter
 * <p>
 * <p>
 * <p>
 * Author: @nicost
 * 6 2019
 */
@Plugin(type = CLIJConverterPlugin.class)
public class NioToClearCLBufferConverter extends AbstractCLIJConverter<NioBuffer, ClearCLBuffer> {

    @Override
    public ClearCLBuffer convert(NioBuffer source) {
        //long time = System.currentTimeMillis();
        //convertLegacy(source).close();
        //IJ.log("legacy conv took " + (System.currentTimeMillis() - time));
        //long time2 = System.currentTimeMillis();

        ClearCLBuffer target;
        NativeTypeEnum type = null;
        if (source.getBuffer() instanceof ByteBuffer) {
           type = NativeTypeEnum.UnsignedByte;
        } else if (source.getBuffer() instanceof ShortBuffer) {
           type = NativeTypeEnum.UnsignedShort;
        } else if (source.getBuffer() instanceof FloatBuffer) {
           type = NativeTypeEnum.Float;
        } // Todo: other types, exception when type not found
        target = clij.createCLBuffer(source.getDimensions(), type);
        target.readFrom(source.getBuffer(), true);
        
        return target;
    }


    @Override
    public Class<NioBuffer> getSourceType() {
        return NioBuffer.class;
    }

    @Override
    public Class<ClearCLBuffer> getTargetType() {
        return ClearCLBuffer.class;
    }
}
