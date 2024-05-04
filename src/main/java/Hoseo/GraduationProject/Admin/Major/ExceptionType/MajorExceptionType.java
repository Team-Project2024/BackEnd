package Hoseo.GraduationProject.Admin.Major.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum MajorExceptionType implements ExceptionType  {
    MAJOR_SAVE_ERROR(400,"이벤트 저장에 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    MajorExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
