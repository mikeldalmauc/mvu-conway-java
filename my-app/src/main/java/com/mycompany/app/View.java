package com.mycompany.app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;

import lombok.Data;

@Data
public class View implements PropertyChangeListener{
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("fc")){

            panelPrincipal.remove(world);
            world = viewTableroInit((Model) evt.getSource());
            panelPrincipal.add(world);

            panelPrincipal.repaint();
            panelPrincipal.revalidate();
        } else {

            this.view((Model) evt.getSource());
        }

    }
    
    /**
     * View
     */
    private JFrame jframe;
    private JPanel panelPrincipal;
    private JPanel world;
    private JPanel menu;

    private JFormattedTextField campoH;
    private JFormattedTextField campoW;
    private JFormattedTextField campoGs;
    private JLabel labelGen;

    public View(Model model){

        jframe = new JFrame("World");

        jframe.setSize(1000, 800);
        jframe.setLocationRelativeTo(null);

        // Configurar el layout
        world = viewTableroInit(model);
        menu = viewMenuInit(model);

        // Agregar paneles al panel principal
        panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.add(menu, BorderLayout.WEST);
        panelPrincipal.add(world, BorderLayout.EAST);

        jframe.add(panelPrincipal);
    }

    public JPanel viewTableroInit(Model model){
        JPanel panelMatriz = new JPanel(new GridLayout(model.getWidth(), model.getHeight()));

        for (int i = 0; i < model.getWidth(); i++) {
            for (int j = 0; j < model.getHeight(); j++) {
                
                JPanel casilla = new JPanel();
                casilla.setBounds(j * 50, i * 50, 50, 50);
                casilla.setLayout(new BorderLayout());
                
                if ((i + j) % 2 == 0) {
                    // Cuadrado blanco
                    casilla.setBackground(Color.WHITE);
                }

                if(model.getCells().get(i).containsKey(j)){
                    // Cuadrado con círculo azul
                    casilla.add(new JLabel(new ImageIcon(createBlueCircle())), BorderLayout.CENTER);
                }

                int index = i * model.getHeight() + j;
                panelMatriz.add(casilla, index);
            }
        }
        
        panelMatriz.setPreferredSize(new Dimension(model.getWidth() * 50, model.getHeight() * 50));
        return panelMatriz;
    }

    public JPanel viewMenuInit(Model model){

        JPanel panelMenu = new JPanel(new GridLayout(5, 2));

        JPanel panelLateral = new JPanel(new FlowLayout(FlowLayout.LEADING));

        JButton btnReset = new JButton("Reset");
        btnReset.addActionListener(e -> Update.update(Msg.Reset, model));
        panelLateral.add(btnReset);
        
        JButton btnStart = new JButton("Start");
        btnStart.addActionListener(e -> Update.update(Msg.Start, model));
        panelLateral.add(btnStart);

        JButton btnStop = new JButton("Stop");
        btnStop.addActionListener(e -> Update.update(Msg.Stop, model));
        panelLateral.add(btnStop);
        
        NumberFormat integerFormat = NumberFormat.getIntegerInstance();
        NumberFormatter integerFormatter = new NumberFormatter(integerFormat);
        integerFormatter.setValueClass(Integer.class);
        integerFormatter.setAllowsInvalid(false); // No permite entradas inválidas

        JLabel labelH = new JLabel("H:");
        campoH = new JFormattedTextField(integerFormatter);
        campoH.setColumns(3);
        campoH.setValue(model.getHeight());
        campoH.getDocument().addDocumentListener(createDocumentListener(campoH, Msg.RedimensionH, model));
        
        JLabel labelW = new JLabel("W:");
        campoW = new JFormattedTextField(integerFormatter);
        campoW.setColumns(3);
        campoW.setValue(model.getWidth());
        campoW.getDocument().addDocumentListener(createDocumentListener(campoW, Msg.RedimensionW, model));
        
        JLabel labelGs = new JLabel("Generations Per Second:");
        campoGs = new JFormattedTextField(integerFormatter);
        campoGs.setColumns(3);
        campoGs.setValue(model.getGensPerSecond());
        campoGs.getDocument().addDocumentListener(createDocumentListener(campoGs, Msg.GensPerSecond, model));

        labelGen = new JLabel("Gen: "+ model.getGeneration());

        // Agregar paneles al panel principal
        panelMenu.add(panelLateral);
        panelMenu.add(new JLabel());

        panelMenu.add(labelW);
        panelMenu.add(campoW);

        panelMenu.add(labelH);
        panelMenu.add(campoH);

        panelMenu.add(labelGs);
        panelMenu.add(campoGs);

        panelMenu.add(labelGen);

        return panelMenu;
    }

    private DocumentListener createDocumentListener(JFormattedTextField field, Msg msg, Model model){
        return new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                Update.updateFC(msg, (Integer) field.getValue() ,model);
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                Update.updateFC(msg, (Integer) field.getValue(), model);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // No es relevante para campos de texto sin formato
            }
        };
    }

    // Método para crear un círculo azul (imagen)
    private static Image createBlueCircle() {
        int diameter = 50;
        BufferedImage image = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();

        g2d.setColor(Color.BLUE);
        g2d.fillOval(0, 0, diameter, diameter);
        g2d.dispose();

        return image;
    }

    /**
     * View function
     * @param model
     */
    public void view(Model model){

         for (int i = 0; i < model.getWidth(); i++) {
            for (int j = 0; j < model.getHeight(); j++) {
                
                
                JPanel casilla = (JPanel) world.getComponent(i * model.getHeight() + j);
                casilla.removeAll();
    
                if(model.getCells().get(i).containsKey(j)){
                    casilla.add(new JLabel(new ImageIcon(createBlueCircle())), BorderLayout.CENTER);
                }

                // casilla.repaint();
                // casilla.revalidate();
            }
        }

        world.repaint();
        world.revalidate();
        labelGen.setText("Gen: " + model.getGeneration());
    }
    
}

