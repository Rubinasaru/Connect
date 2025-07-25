package com.example.demo.Service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service	
public interface FileService {
	String uploadImage(String filePath, MultipartFile file)throws IOException;
}
