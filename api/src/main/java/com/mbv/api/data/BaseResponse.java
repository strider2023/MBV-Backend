package com.mbv.api.data;

public class BaseResponse {

	private ResponseCode status;
	
	private Object id;
	
	private String message;

    private int responseSize;

    private Object response;

	public BaseResponse() {
		this(null,ResponseCode.SUCCESS);
	}

	public BaseResponse(Object id) {
		this(id,ResponseCode.SUCCESS);
	}
	
	public BaseResponse(Object id, ResponseCode status) {
		this(id, status, null);
	}
	
	public BaseResponse(Object id, ResponseCode status, String message) {
		super();
		this.id = id;
		this.status = status;
		this.message = message;
	}

    public BaseResponse(Object id, ResponseCode status, int responseSize, Object response) {
        super();
        this.id = id;
        this.status = status;
        this.responseSize = responseSize;
        this.response = response;
    }

	public ResponseCode getStatus() {
		return status;
	}

	public Object getId() {
		return id;
	}	
	
	public String getMessage(){
		return message;
	}

    public Object getResponse() {
        return response;
    }

    public int getResponseSize() {
        return responseSize;
    }

    public enum ResponseCode{
		SUCCESS, FAILURE, WARNING, UPDATED;
	}
}
