package com.smart.helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUploadHelper {
	public static class FileUploadResult {
	    private String uniqueFileName;
	    private boolean success=false;
	    
		public FileUploadResult() {
			super();
		}
		public FileUploadResult(String uniqueFileName, boolean success) {
			super();
			this.uniqueFileName = uniqueFileName;
			this.success = success;
		}
		public String getUniqueFileName() {
			return uniqueFileName;
		}
		public void setUniqueFileName(String uniqueFileName) {
			this.uniqueFileName = uniqueFileName;
		}
		public boolean isSuccess() {
			return success;
		}
		public void setSuccess(boolean success) {
			this.success = success;
		}

	}


	public final String UPLOAD_DIR = new ClassPathResource("static/image").getFile().getAbsolutePath();
	
	public FileUploadHelper() throws IOException{
		super();
		// TODO Auto-generated constructor stub
	}
	public FileUploadResult uploadFile(MultipartFile file) {
		FileUploadResult result = new FileUploadResult();
		try {
			String uniqueFileName = generateUniqueFileName(file.getOriginalFilename());
			result.setUniqueFileName(uniqueFileName);
			Files.copy(file.getInputStream(), Paths.get(UPLOAD_DIR+File.separator+uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
			result.setSuccess(true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	private String generateUniqueFileName(String originalFileName) {
        // Use a combination of timestamp and original filename to generate a unique name
        String timestamp = String.valueOf(System.currentTimeMillis());
        return timestamp + "_" + originalFileName.replaceAll("[^a-zA-Z0-9.-]", "");
    }
	
	
	public void deleteFile(String imageUrl) {
		
		try {
			if(!imageUrl.equals("default.png"))
				Files.deleteIfExists(Paths.get(UPLOAD_DIR+ File.separator+imageUrl));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
}
