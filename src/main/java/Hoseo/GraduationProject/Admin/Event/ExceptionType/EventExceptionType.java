package Hoseo.GraduationProject.Admin.Event.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum EventExceptionType implements ExceptionType {
    EVENT_SAVE_ERROR(400,"이벤트 저장에 실패하였습니다."),
    EVENT_NOT_FOUND(404,"이벤트를 찾지 못하였습니다.");

    private int errorCode;
    private String errorMessage;

    EventExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
