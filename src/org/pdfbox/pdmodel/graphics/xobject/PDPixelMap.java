/**
 * Copyright (c) 2004-2005, www.pdfbox.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of pdfbox; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * http://www.pdfbox.org
 *
 */
package org.pdfbox.pdmodel.graphics.xobject;

import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import org.pdfbox.pdmodel.common.PDStream;

import org.pdfbox.pdmodel.graphics.color.PDColorSpace;

/**
 * This class contains a PixelMap Image. 
 * @author Ben Litchfield (ben@benlitchfield.com)
 * @author mathiak
 * @version $Revision: 1.5 $
 */
public class PDPixelMap extends PDXObjectImage 
{
    private BufferedImage image = null;
    
    /**
     * Standard constructor. Basically does nothing. 
     * @param pdStream The stream that holds the pixel map.
     */
    public PDPixelMap(PDStream pdStream) 
    {
        super(pdStream, "png");
    }
    
    /**
     * Construct a pixel map image from an AWT image.
     * 
     * @param doc The PDF document to embed the image in.
     * @param awtImage The image to read data from.
     * 
     * @throws IOException If there is an error while embedding this image.
     */
    /*
     * This method is broken and needs to be implemented, any takers?
    public PDPixelMap(PDDocument doc, BufferedImage awtImage) throws IOException
    {
        super( doc, "png");
        image = awtImage;
        setWidth( image.getWidth() );
        setHeight( image.getHeight() );
        
        ColorModel cm = image.getColorModel();
        ColorSpace cs = cm.getColorSpace();
        PDColorSpace pdColorSpace = PDColorSpaceFactory.createColorSpace( doc, cs );
        setColorSpace( pdColorSpace );
        //setColorSpace( )
        
        PDStream stream = getPDStream();
        OutputStream output = null;
        try
        {
            output = stream.createOutputStream();
            DataBuffer buffer = awtImage.getRaster().getDataBuffer();
            if( buffer instanceof DataBufferByte )
            {
                DataBufferByte byteBuffer = (DataBufferByte)buffer;
                byte[] data = byteBuffer.getData();
                output.write( data );
            }
            setBitsPerComponent( cm.getPixelSize() );
        }
        finally
        {
            if( output != null )
            {
                output.close();
            }
        }
    }*/

    /**
     * Returns a {@link java.awt.image.BufferedImage} of the COSStream 
     * set in the constructor or null if the COSStream could not be encoded.   
     * 
     * @see org.pdfbox.pdmodel.graphics.xobject.PDXObjectImage#getRGBImage()
     */
    public BufferedImage getRGBImage() throws IOException 
    {
        if( image != null )
        {
            return image; 
        }

        //byte[] index =
        //ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);  
        int width = getWidth();
        int height = getHeight();
        int bpc = getBitsPerComponent();
        //COSInteger length =
        //        (COSInteger) stream.getStream().getDictionary().getDictionaryObject(COSName.LENGTH);
        //byte[] array = new byte[stream.getFilteredStream().];
        byte[] array = getPDStream().getByteArray(); 

//      Get the ColorModel right
        PDColorSpace colorspace = getColorSpace();
        ColorModel cm = colorspace.createColorModel( bpc );
        WritableRaster raster = cm.createCompatibleWritableRaster( width, height );
        //DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        DataBufferByte buffer = (DataBufferByte)raster.getDataBuffer();
        byte[] bufferData = buffer.getData();
        System.arraycopy( array, 0, bufferData, 0, array.length );
        
        image = new BufferedImage(cm, raster, false, null);
        return image;
    }

    /**
     * Writes the image as .png.
     * 
     * @see org.pdfbox.pdmodel.graphics.xobject.PDXObjectImage#write2OutputStream(java.io.OutputStream)
     */
    public void write2OutputStream(OutputStream out) throws IOException 
    {
        getRGBImage();
        if (image!=null)
        {
            ImageIO.write(image, "png", out);
        }        
    }

}
