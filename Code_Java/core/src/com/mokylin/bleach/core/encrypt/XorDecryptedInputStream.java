package com.mokylin.bleach.core.encrypt;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


/**
 * 采用异或算法进行解密的输入流
 * 
 */
public class XorDecryptedInputStream extends InputStream {
	private ByteArrayInputStream inputStream;

	public XorDecryptedInputStream(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		if (!file.exists()) {
			throw new FileNotFoundException();
		}
		this.decryptFile(file);
	}

	/**
	 * 对文件进行解密
	 * 
	 * @param file
	 */
	private void decryptFile(File file) {
		try {
			BufferedInputStream in = new BufferedInputStream(
					new FileInputStream(file));
			int fileSize = in.available();
			ByteArrayOutputStream out = new ByteArrayOutputStream(fileSize);
			byte[] temp = new byte[1024];
			int size = 0;
			while ((size = in.read(temp)) != -1) {
				out.write(temp, 0, size);
			}
			in.close();
			inputStream = new ByteArrayInputStream(EncryptUtils.decryptBytes(out
					.toByteArray()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

}
