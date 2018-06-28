package com.elm.common.revalidator.util;

public class ApplicationResult {

    public static final short SUCCESS_CODE = 0;
    public static final short ERROR_CODE = 1;

    //To be changed later to get a bilingual message
    private static final String GENERIC_ERROR_MESSAGE = "Service Unavailable";

    private int resultCode;
    private String resultMessage;

    private Exception exception;

    public ApplicationResult() {

    }

    public ApplicationResult(short resultCode, String resultMessage) {
        setResultCodeAndMessage(resultCode, resultMessage);
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultCode(int s) {
        resultCode = s;
    }

    public void setResultMessage(String string) {
        resultMessage = string;
    }

    public void setResultCodeAndMessage(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public boolean isSuccessfull() {
        return (getResultCode() == SUCCESS_CODE);
    }

    public void setAsError() {
        setResultCode(ERROR_CODE);
        setResultMessage(GENERIC_ERROR_MESSAGE);
    }

    public void setAsError(Exception exception) {
        setAsError();
    }

    public void setAsError(String resultMessage) {
        setResultCode(ERROR_CODE);
        setResultMessage(resultMessage);
    }

    public String toString() {
        return "[" + this.getClass().getSimpleName() +
                "]: ResultCode (" + getResultCode() +
                "), ResultMessage (" + getResultMessage() +
                ")";
    }
}
