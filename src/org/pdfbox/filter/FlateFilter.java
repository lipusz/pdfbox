/**
 * Copyright (c) 2003, www.pdfbox.org
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
package org.pdfbox.filter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;

import org.pdfbox.cos.COSDictionary;

/**
 * This is the used for the FlateDecode filter.
 *
 * @author Ben Litchfield (ben@csh.rit.edu)
 * @version $Revision: 1.7 $
 */
public class FlateFilter implements Filter
{
    private static final int BUFFER_SIZE = 2048;

    /**
     * This will decode some compressed data.
     *
     * @param compressedData The compressed byte stream.
     * @param result The place to write the uncompressed byte stream.
     * @param options The options to use to encode the data.
     *
     * @throws IOException If there is an error decompressing the stream.
     */
    public void decode( InputStream compressedData, OutputStream result, COSDictionary options ) throws IOException
    {
        //System.out.println( "FlateFilter.decode options=" + options +
        //    " avail=" + compressedData.available() );
//        int read = 0;
//        int count = 0;
//        while( (read = compressedData.read()) != -1 )
//        {
//            System.out.println( "byte[" + count++ + "]" + read );
//        }

        InflaterInputStream decompressor = null;
        try
        {
            decompressor = new InflaterInputStream( compressedData );
            byte[] buffer = new byte[ BUFFER_SIZE ];
            int amountRead;
            while( (amountRead = decompressor.read( buffer, 0, BUFFER_SIZE )) != -1 )
            {
                //System.out.println( "READING " + amountRead + " avail=" + compressedData.available() );
                result.write( buffer, 0, amountRead );
            }
            result.flush();
        }
        finally
        {
            if( decompressor != null )
            {
                decompressor.close();
            }
        }
    }

    /**
     * This will encode some data.
     *
     * @param rawData The raw data to encode.
     * @param result The place to write to encoded results to.
     * @param options The options to use to encode the data.
     *
     * @throws IOException If there is an error compressing the stream.
     */
    public void encode( InputStream rawData, OutputStream result, COSDictionary options ) throws IOException
    {
        DeflaterOutputStream out = new DeflaterOutputStream( result );
        byte[] buffer = new byte[BUFFER_SIZE];
        int amountRead = 0;
        while( (amountRead = rawData.read( buffer, 0, BUFFER_SIZE )) != -1 )
        {
            out.write( buffer, 0, amountRead );
        }
        out.close();
        result.flush();
    }
}