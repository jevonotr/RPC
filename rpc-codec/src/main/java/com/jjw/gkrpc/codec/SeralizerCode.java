package com.jjw.gkrpc.codec;

public enum SeralizerCode {
    FASTJSON(0),
    jackson(1),
    kryo(2);

    private int code;

    SeralizerCode(int code) {
        this.code = code;
    }
}
