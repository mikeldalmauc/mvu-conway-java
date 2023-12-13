package com.mycompany.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mycompany.app.Model.GameState;

import javafx.util.Pair;

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
                model.setGameState(GameState.Stopped);
                model.setCells(model.initCells());
                break;
                
            case Start:
                model.setGameState(GameState.Running);
                break;
            
            case Stop:
                model.setGameState(GameState.Stopped);
                break;

            case NewGeneration:
                if(model.getGameState().equals(GameState.Running)){
                    model.setGeneration(model.getGeneration() + 1);
                    model.setCells(newGeneration(model));
                }
                break;
            default:
                break;
        }

        if(model.getGameState().equals(GameState.Running)){
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    Update.update(Msg.NewGeneration, model);
                }
            }, 500);
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

    public static Map<Integer, Map<Integer, Boolean>> newGeneration(Model model){

        Map<Integer, Map<Integer, Boolean>> newCells = new HashMap<>();
        for (int k = 0; k < model.getWidth(); k++)
            newCells.put(k,  new HashMap<>());

        
        for (int i = 0; i < model.getWidth(); i++) {
            for (int j = 0; j < model.getHeight(); j++) {
                
                Integer neighbours = neighbours(i, j, model);
                Boolean alive = model.getCells().get(i).containsKey(j);

                if(alive & neighbours == 2 || neighbours == 3){
                    newCells.get(i).put(j, true);
                }
            }
        }

        return newCells;
    }

    public static Integer neighbours(Integer i, Integer j, Model model){
        
        Predicate<Pair<Integer, Integer>> insideBounds = p -> p.getKey() > 0 && p.getKey() < model.getHeight()
            && p.getValue() > 0 && p.getValue() < model.getWidth();
        Predicate<Pair<Integer, Integer>> notSelf = p -> !p.getKey().equals(p.getValue());
        
        return Arrays.asList(i-1, i, i+1).stream()
            .flatMap(row -> Arrays.asList(j-1, j, j+1).stream().map(col -> new Pair<Integer,Integer>(row, col)))
            .filter(p -> notSelf.test(p) && insideBounds.test(p))
            .filter(p -> model.getCells().get(p.getKey()).containsKey(p.getValue()))
            .collect(Collectors.toList())
            .size();
    }
}
