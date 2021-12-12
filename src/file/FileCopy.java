package file;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class FileCopy{
	
	private static String rootLocation = "경로";
	private static String targetLocation = "경로";
	private static Date fromDate = setFromDate("2021-12-10 06:50:00"); //yyyy-MM-dd HH:mm:ss

	private static Date setFromDate(String date) {
		Date formattedDate = null;
		try {
			formattedDate = changeDateFormat(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return formattedDate;
	}

	private static Date changeDateFormat(String date) throws ParseException{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date fromDate = dateFormat.parse(date);
		return fromDate;
	}

	public static void main(String[] args) {
		String todayDirectoryName = makeTodayDirectory(targetLocation);
		targetLocation += File.separator + todayDirectoryName;
		
		File rootDirectory = new File(rootLocation);
		copyFileFromRoot(rootDirectory);
	}

	private static String makeTodayDirectory(String targetLocation) {
		LocalDate today = LocalDate.now();
		File todayDirectory = new File(targetLocation + File.separator + today.toString());
		if (!todayDirectory.exists()) {
			todayDirectory.mkdir();
		}
		return todayDirectory.getName();
	}

	private static void copyFileFromRoot(File rootDirectory) {
		try {
			setAndCopyFile(rootDirectory);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void setAndCopyFile(File rootDirectory) throws IOException{
		File[] sourceFiles =  rootDirectory.listFiles();
		FileInputStream fis = null;
		FileOutputStream fos = null;

		for (File sourceFile : sourceFiles) {
			
			if (sourceFile.isDirectory()) {
				setAndCopyFile(sourceFile);
				continue;
			}
			
			if (isBeforeFromDate(sourceFile.lastModified())) {
				continue;
			}
			
			StringBuffer targetDirectory = makeTargetDirectoryFromSourceFile(sourceFile);
			File copiedFile = new File(targetDirectory.toString());
			
			try {
				fis = new FileInputStream(sourceFile);
				fos = new FileOutputStream(copiedFile);
				copyFile(fis, fos);
			} catch(IOException e) {
				throw e;
			} finally {
				if(fis != null) {
					fis.close();
				}
				if(fos != null) {
					fos.close();
				}
			}
			
		}
	}
	
	private static Boolean isBeforeFromDate(Long lastModified) {
		Date modifiedDate = new Date(lastModified);
		return modifiedDate.before(fromDate) ? true : false;
	}
	
	private static StringBuffer makeTargetDirectoryFromSourceFile(File sourceFile) {
		StringBuffer routeAndFilename = new StringBuffer(sourceFile.getAbsolutePath());
		routeAndFilename.replace(0, rootLocation.length(), targetLocation);
		String route = routeAndFilename.substring(0, routeAndFilename.lastIndexOf("\\"));
		(new File(route)).mkdirs();
		return routeAndFilename;
	}
	
	private static void copyFile(FileInputStream fis, FileOutputStream fos) throws IOException {
		byte[] sizeRead = new byte[4096];
		int order = 0;
		while ((order = fis.read(sizeRead)) != -1) {
			fos.write(sizeRead, 0, order);
		}
	}
}