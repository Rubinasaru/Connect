package com.example.demo.DTO.response;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Hidden
@Data
public class ResponseObject {
		@Getter
		@Setter
	    private final boolean status;
		
		@Getter
		@Setter
	    private final String message;
		
		@Getter
		@Setter
	    private final Object data;

	    // Constructor for single object responses
	    public ResponseObject(boolean status, String message, Object data) {
	        this.status = status;
	        this.message = message;
	        this.data = data;
	    }

	    public static ResponseObject success(String message, Object data) {
	        return new ResponseObject(true, message, data);
	    }

	    public static ResponseObject failure(String message) {
	        return new ResponseObject(false, message, null);
	    }


	}

