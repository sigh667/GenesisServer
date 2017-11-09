package com.mokylin.bleach.tools.message;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * 文件操作工具类
 * @author baoliang.shen
 *
 */
public class FileUtils {
	
	/**
	 * 递归遍历指定目录，将符合要求的文件放入fileList中
	 * @param file
	 * @param fileList
	 */
	public static void traversal(File file, ArrayList<File> fileList) { // 假定是文件夹
		File[] files = file.listFiles(); // 获取文件夹下面的所有文件
		for (File f : files) {
			// 判断是否为文件夹
			if (f.isDirectory()) {
				traversal(f,fileList); // 如果是文件夹，重新遍历
			} else { // 如果是文件 就打印文件的路径
				fileList.add(f);
			}
		}
	}
	
	/**
	 * 只保留指定的扩展名的文件，其余的抛弃掉
	 * @param fileList	文件列表
	 * @param extName	扩展名
	 */
	public static void filtrate(ArrayList<File> fileList, String extName) {
		for (Iterator<File> iterator = fileList.iterator(); iterator.hasNext();) {
			File file = (File) iterator.next();
			String nameWhole = file.getAbsolutePath();
			int length = nameWhole.length()-extName.length();
			if (length<=0) {
				iterator.remove();
				continue;
			}
			String strNew = nameWhole.substring(length, nameWhole.length());
			if (!strNew.equalsIgnoreCase(extName)) {
				iterator.remove();
				continue;
			}
		}
	}
	
	/**
     * 将文本文件中的内容读入到buffer中
     * @param buffer buffer
     * @param filePath 文件路径
     * @throws IOException 异常
     * @author cn.outofmemory
     * @date 2013-1-7
     */
    public static void readToBuffer(StringBuffer buffer, String filePath) throws IOException {
        InputStream is = new FileInputStream(filePath);
        String line; // 用来保存每行读取的内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        line = reader.readLine(); // 读取第一行
        while (line != null) { // 如果 line 为空说明读完了
            buffer.append(line); // 将读到的内容添加到 buffer 中
            buffer.append("\n"); // 添加换行符
            line = reader.readLine(); // 读取下一行
        }
        reader.close();
        is.close();
    }
    
    /**
     * 将字符串写入到指定文件中
     * @param str		要写入的内容
     * @param filePath	路径（不包含文件名）
     * @param fileName	文件名
     * @throws IOException 
     */
    public static void writeToFile(String str, String filePath, String fileName) throws IOException {
        
        Path pathToDirectory = FileSystems.getDefault().getPath(filePath);
        Files.createDirectories(pathToDirectory);
        
        File writename = new File(filePath + File.separator + fileName); // 相对路径，如果没有则要建立一个新的文件  
        writename.createNewFile(); // 创建新文件  
        BufferedWriter out = new BufferedWriter(new FileWriter(writename));  
        out.write(str);
        out.flush(); // 把缓存区内容压入文件  
        out.close(); // 最后记得关闭文件
    }

    /**
     * 删除指定目录及其下面的所有文件和文件夹
     * @param filepath	指定目录
     * @throws IOException
     */
    public static void del(String filepath) throws IOException{  
    	File f = new File(filepath);//定义文件路径         
    	if(f.exists() && f.isDirectory()){//判断是文件还是目录  
    		if(f.listFiles().length==0){//若目录下没有文件则直接删除  
    			f.delete();  
    		}else{//若有则把文件放进数组，并判断是否有下级目录  
    			File delFile[]=f.listFiles();  
    			int i =f.listFiles().length;  
    			for(int j=0;j<i;j++){  
    				if(delFile[j].isDirectory()){  
    					del(delFile[j].getAbsolutePath());//递归调用del方法并取得子目录路径  
    				}  
    				delFile[j].delete();//删除文件  
    			}  
    		}  
    	}      
    }

}
