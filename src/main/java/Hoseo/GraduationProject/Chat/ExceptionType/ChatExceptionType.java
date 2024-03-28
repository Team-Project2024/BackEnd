package Hoseo.GraduationProject.Chat.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum ChatExceptionType implements ExceptionType {
    CHAT_ERROR(400,"채팅에 실패하였습니다.");


    private int errorCode;
    private String errorMessage;

    ChatExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
