package Hoseo.GraduationProject.Security.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum SecurityExceptionType implements ExceptionType {
    //인증 오류
    ID_ERROR(400, "ID는 6자리 또는 8자리이어야 합니다."),
    PASSWORD_ERROR(400, "비밀번호는 8~16자 영문 대 소문자, 숫자, 특수문자를 사용해야 합니다."),
    BAD_CREDENTIALS(401,"비밀번호가 일치하지 않습니다."),
    NOT_FOUND(404,"일치하는 회원이 없습니다."),
    FAIL_LOGIN(400,"로그인에 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    SecurityExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
