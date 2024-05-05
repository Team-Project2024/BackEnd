package Hoseo.GraduationProject.Admin.Lecture.ExceptionType;

import Hoseo.GraduationProject.Exception.ExceptionType;

public enum LectureExceptionType implements ExceptionType {
    INVALID_INPUT_VALUE(400, "데이터에 null이 포함되어 있습니다."),
    LECTURE_NOT_FOUND(404,"존재하지 않는 강의입니다."),
    LECTURE_SAVE_ERROR(400,"강의 저장에 실패하였습니다.");

    private int errorCode;
    private String errorMessage;

    LectureExceptionType(int errorCode, String errorMessage){
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    @Override
    public int getErrorCode() { return errorCode; }

    @Override
    public String getErrorMessage() { return errorMessage; }

}
