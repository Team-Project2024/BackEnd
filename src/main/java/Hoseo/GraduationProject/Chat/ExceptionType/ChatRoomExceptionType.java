package Hoseo.GraduationProject.Chat.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum ChatRoomExceptionType implements ExceptionType {
    SAVE_CHATROOM_ERROR(400,"채팅방 생성에 실패하였습니다."),
    NOT_FOUND_CHATROOM(404, "채팅방을 찾을 수 없습니다.");


    private int errorCode;
    private String errorMessage;

    ChatRoomExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }
}
