package Hoseo.GraduationProject.Admin.GraduationRequirements.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum GRExceptionType implements ExceptionType {
    GR_SAVE_ERROR(400,"졸업요건 저장에 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    GRExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
