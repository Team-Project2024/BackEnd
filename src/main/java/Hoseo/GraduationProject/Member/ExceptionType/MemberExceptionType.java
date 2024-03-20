package Hoseo.GraduationProject.Member.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum MemberExceptionType implements ExceptionType {
    MEMBER_CONFLICT(409,"이미 존재하는 회원입니다"),
    NOT_ACCESS_LEVEL(400,"허용하지 않는 권한입니다."),
    MEMBER_SAVE_ERROR(400,"회원가입에 실패하였습니다"),
    NONE_MEMBER(400,"존재하지 않는 회원입니다."),
    INVALID_CODE(400,"인증코드가 일치하지 않습니다."),
    ERROR_CHANGE_PW(400,"비밀번호 변경에 실패하였습니다.");

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
