package com.mycompany.app;

/**
 * Enum with messages of the application, this 
 */
public enum Msg {

    Reset(1),
    Start(2),
    Stop(3),
    RedimensionH(4),
    RedimensionW(4);

    private int value;
    
    private Msg(int value){
        this.value = value;
    }
}
