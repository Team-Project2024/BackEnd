package Hoseo.GraduationProject.API.GraduationRequirements.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum GRExceptionType implements ExceptionType {
    INVALID_INPUT_VALUE(400, "데이터에 null이 포함되어 있습니다."),
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
