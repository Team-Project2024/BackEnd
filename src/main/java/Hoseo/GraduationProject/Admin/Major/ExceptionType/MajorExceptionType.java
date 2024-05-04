package Hoseo.GraduationProject.Admin.Major.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum MajorExceptionType implements ExceptionType  {
    MAJOR_SAVE_ERROR(400,"전공 저장에 실패하였습니다."),
    MAJOR_NOT_FOUND(404,"존재하지 않는 전공 ID입니다.");

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
