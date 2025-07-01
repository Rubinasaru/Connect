package com.example.demo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class FileValidator {
	private static final Logger logger = LoggerFactory.getLogger(FileValidator.class);

    private static Set<String> allowedMimeTypes;

    // Maximum file size in bytes (configurable via properties, default 5MB)
    private static long maxFileSize;


    public FileValidator(
            @Value("${file.upload.allowed-types:image/png,image/jpeg,image/jpg,application/pdf}") String allowedMimeTypes,
            @Value("${file.upload.max-size:5242880}") long maxFileSize) { // 5MB default
        this.allowedMimeTypes = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(allowedMimeTypes.split(","))));
        this.maxFileSize = maxFileSize;
        logger.info("Initialized FileValidator with allowed types: {} and max size: {} bytes", this.allowedMimeTypes, this.maxFileSize);
    }

    public static void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            logger.error("File validation failed: File is null or empty");
            throw new IllegalArgumentException("File cannot be null or empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !allowedMimeTypes.contains(contentType)) {
            logger.error("File validation failed: Invalid content type '{}'. Allowed types: {}", contentType, allowedMimeTypes);
            throw new IllegalArgumentException("Invalid file type. Allowed types are: " + allowedMimeTypes);
        }

        if (file.getSize() > maxFileSize) {
            logger.error("File validation failed: Size {} bytes exceeds limit of {} bytes", file.getSize(), maxFileSize);
            throw new IllegalArgumentException("File size exceeds the maximum limit of " + (maxFileSize / (1024 * 1024)) + "MB");
        }

        logger.debug("File validated successfully: {} (size: {} bytes)", file.getOriginalFilename(), file.getSize());
    }

 
    public void deleteFile(String filePath) throws IOException {
        if (filePath == null || filePath.trim().isEmpty()) {
            logger.warn("No file deletion performed: File path is null or empty");
            throw new IllegalArgumentException("File path cannot be null or empty");
        }

        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            try {
                Files.delete(path);
                logger.info("File deleted successfully: {}", filePath);
            } catch (IOException e) {
                logger.error("Failed to delete file: {}", filePath, e);
                throw new IOException("Failed to delete file: " + filePath, e);
            }
        } else {
            logger.debug("File not deleted: {} does not exist", filePath);
        }
    }
}
