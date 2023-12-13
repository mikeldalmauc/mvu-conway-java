package com.mycompany.app;

public class Update{

    /**
     * All changes on model should be performed by this function
     * 
     * @param msg
     * @param model
     */
    public static void update(Msg msg, Model model){

        switch (msg) {
            case Reset:
                break;
                
            case Start:
                break;
            
            case Stop:
                break;

            default:
                break;
        }

    }

    public static void update(Msg msg, Integer value, Model model){

        switch (msg) {
            case RedimensionH:
                model.setHeight(value);
                break;
            case RedimensionW:
                model.setWidth(value);
                break;
                
            default:
                break;
        }

    }
}
