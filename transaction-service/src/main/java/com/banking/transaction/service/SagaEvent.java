package com.banking.transaction.service;

public class SagaEvent {
    private String step;
    private Long txId;

    public SagaEvent() {} // Default constructor for deserialization

    public SagaEvent(String step, Long txId) {
        this.step = step;
        this.txId = txId;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public Long getTxId() {
        return txId;
    }

    public void setTxId(Long txId) {
        this.txId = txId;
    }
}
