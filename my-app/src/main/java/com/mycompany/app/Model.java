package com.mycompany.app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class Model {
    
    /*
     * Support to add change listeners 
     */
    private PropertyChangeSupport support;

    public void setListener(PropertyChangeListener view) {
        support.addPropertyChangeListener(view);
    }

    /**
     *  Model data should go here
     */
    private Integer width;
    private Integer height;
    private Integer gensPerSecond;
    private Integer generation;
    private GameState gameState;
    private Map<Integer, Map<Integer, Boolean>> cells;

    public enum GameState {
        Running,
        Stopped;
        private GameState(){
        }
    }

    public Model(){
        this.support = new PropertyChangeSupport(this);

        /**
         * Model initial data
         */
        this.width = 10;
        this.height = 10;
        this.generation = 0;
        this.gameState = GameState.Stopped;
        this.cells = initCells();
        this.gensPerSecond = 2;
    }
    

    public Map<Integer, Map<Integer, Boolean>> initCells(){
        Map<Integer, Map<Integer, Boolean>> cells = new HashMap<>();

        for (int i = 0; i < this.width; i++) {
            Map<Integer, Boolean> cellRow = new HashMap<>();
            cells.put(i, cellRow);

            for (int j = 0; j < this.height; j++) {
                if ((i + j) % 2 == 0) {
                   cellRow.put(j, true);
                } 
            }
        }
        
        return cells;
    }


    public void notifyChange(){
        support.firePropertyChange("c", null, null);;
    }
    
    public void notifyFullChange(){
        support.firePropertyChange("fc", null, null);;
    }
}
