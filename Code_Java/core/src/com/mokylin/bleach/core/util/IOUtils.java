package com.mokylin.bleach.core.util;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
/**
 * A tool we use to interact with IO streams.
 * @author yaguang.xiao
 *
 */
public class IOUtils {
	/**
	 * Read the content in the InputStream and close it after reading.
	 * @param in The InputStream
	 * @param charset 
	 * @return the content in the InputStream.
	 * @throws IOException 
	 */
	public static String readAndClose(InputStream in, String charset) throws IOException {
		InputStreamReader isr = null;
		BufferedReader br = null;
		try {
			isr = new InputStreamReader(in, charset);
			br = new BufferedReader(isr);
			
			StringBuilder builder = new StringBuilder();
			String line = null;
			while((line = br.readLine()) != null)
				builder.append(line).append("\n");
			
			return builder.toString();
		} catch (IOException e) {
			throw e;
		} finally {
			closeInputStream(br);
			closeInputStream(isr);
			closeInputStream(in);
		}
	}
	
	/**
	 * Close the InputStream
	 * @param in the InputStream to close.
	 * @throws IOException 
	 */
	static void closeInputStream(Closeable in) throws IOException {
		if(in != null) {
			try {
				in.close();
			} catch (IOException e) {
				throw e;
			}
		}
	}
}
