package com.mycompany.app;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.mycompany.app.Model.GameState;

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
                    Map<Integer, Map<Integer, Boolean>> nG = newGeneration(model);
                    
                    if(nG.equals(model.getCells()))
                        model.setGameState(GameState.Stopped);
                    else    
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
            }, 1000 / model.getGensPerSecond());
        }

        model.notifyChange();
    }

    public static void updateFC(Msg msg, Integer value, Model model){

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

        model.initCells();
        model.setGameState(GameState.Stopped);
        model.setGeneration(0);
        
        model.notifyFullChange();
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
        
        Predicate<Pair> insideBounds = p -> p.getLeft() >= 0 && p.getLeft() < model.getHeight()
            && p.getRight() >= 0 && p.getRight() < model.getWidth();
        Predicate<Pair> notSelf = p -> !p.getLeft().equals(p.getRight());
        
        return Arrays.asList(i-1, i, i+1).stream()
            .flatMap(row -> Arrays.asList(j-1, j, j+1).stream().map(col -> new Pair(row, col)))
            .filter(p -> notSelf.test(p) && insideBounds.test(p))
            .filter(p -> model.getCells().get(p.getLeft()).containsKey(p.getRight()))
            .collect(Collectors.toList())
            .size();
    }


}
