package Hoseo.GraduationProject.Chat.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum ChatExceptionType implements ExceptionType {
    CHAT_ERROR(400,"채팅에 실패하였습니다."),
    DELETE_CHAT_ERROR(400,"채팅 삭제에 실패하였습니다."),
    SAVE_CHAT_ERROR(400,"채팅 저장에 실패하였습니다.");


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
