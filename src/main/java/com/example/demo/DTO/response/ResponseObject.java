package com.example.demo.DTO.response;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Hidden
@Data
public class ResponseObject {
	    private final boolean status;
	    private final String message;
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

		public boolean isStatus() {
			return status;
		}

		public String getMessage() {
			return message;
		}

		public Object getData() {
			return data;
		}
	    
	    


	}

