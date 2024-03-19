package Hoseo.GraduationProject.Member.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum MemberExceptionType implements ExceptionType {
    MEMBER_CONFLICT(409,"이미 존재하는 회원입니다"),
    NOT_ACCESS_LEVEL(400,"허용하지 않는 권한입니다."),
    MEMBER_SAVE_ERROR(400,"회원가입에 실패하였습니다");

    private int errorCode;
    private String errorMessage;

    MemberExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
