package com.example.demo.DTO.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ResponseObject {
    private boolean status;
    private String message;
    private Object data;

    public ResponseObject(boolean status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public boolean isStatus() {
		return status;
	}



	public void setStatus(boolean status) {
		this.status = status;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public Object getData() {
		return data;
	}



	public void setData(Object data) {
		this.data = data;
	}



	public static ResponseObject success(String message, Object data) {
        return new ResponseObject(true, message, data);
    }

    public static ResponseObject failure(String message) {
        return new ResponseObject(false, message, null);
    }
}
