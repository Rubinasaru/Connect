package com.example.demo.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.utils.FileValidator;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;

@Service
public class FileServiceImp implements FileService {

    public String uploadImage(String filePath, MultipartFile file) throws IOException {
    	 // Validate file using FileValidator
        FileValidator.validateFile(file);

        // Create a unique filename
        String originalFilename = file.getOriginalFilename();
        String uniqueFileName = System.currentTimeMillis() + "_" + originalFilename;

        // Full path to save the file
        String fullPath = Paths.get(filePath, uniqueFileName).toString();

        // Save the file
        try (FileOutputStream fos = new FileOutputStream(fullPath)) {
            fos.write(file.getBytes());
        }

        // Return relative path or URL for DB (you can adjust this if hosting files separately)
        return uniqueFileName;
    }
}
