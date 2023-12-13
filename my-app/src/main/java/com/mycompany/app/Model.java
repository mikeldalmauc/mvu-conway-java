package com.mycompany.app;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import java.util.Map;

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

        this.gameState = GameState.Stopped;
        this.generation = 0;

        return cells;
    }

    public Integer getWidth() {
        return width;
    }

    /*
     * This can be improved and be made with a tag
     */
    public void setWidth(Integer width) {
        Integer old = this.width;
        this.width= width;

        initCells();
        support.firePropertyChange("width", old, this.width);;
    }

    public Integer getHeight() {
        return height;
    }

    /*
     * This can be improved and be made with a tag
     */
    public void setHeight(Integer height) {
        Integer old = this.height;
        this.height= height;

        initCells();
        support.firePropertyChange("height", old, this.height);;
    }
    
    public  Map<Integer, Map<Integer, Boolean>> getCells(){
        return cells;
    }
    
    public void setCells(Map<Integer, Map<Integer, Boolean>> cells){
        Map<Integer, Map<Integer, Boolean>> old = this.cells;
        
        this.cells = cells;
        support.firePropertyChange("cells", old, this.cells);;
    }
 
    public Integer getGeneration(){
        return this.generation;
    }

    public void setGeneration(Integer generation){
        this.generation = generation;
    }

    public GameState getGameState(){
        return this.gameState;
    }

    public void setGameState(GameState state){
        this.gameState = state;
    }

}
