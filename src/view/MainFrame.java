package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import controller.MeshController;
import controller.observer.*;


public class MainFrame extends JFrame implements Observer {

    private static final long serialVersionUID = 1L;

    private MeshController meshController;
    private RoadMeshPanel roadMeshPanel;
    private JPanel main;

    public MainFrame() {
        this.meshController = MeshController.getInstance();
        this.meshController.addObserver(this);

        super.setFocusable(true);
        super.setFocusTraversalKeysEnabled(false);
        super.setDefaultCloseOperation(EXIT_ON_CLOSE);
        super.setResizable(false);
        super.setLayout(new BorderLayout());
        super.getContentPane().setBackground(Color.WHITE);
        super.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        initComponents();
        start();
    }

    private void initComponents() {
        this.roadMeshPanel = new RoadMeshPanel();
        JButton buttonStopSimulation = new JButton();
        buttonStopSimulation.setText("Stop simulation");
        buttonStopSimulation.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        this.roadMeshPanel.add(buttonStopSimulation);
        this.add(this.roadMeshPanel);
    }


    private void start() {
        this.meshController.runSimulation();
        super.pack();
        super.setLocationRelativeTo(null);
    }

    @Override
    public void message(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    @Override
    public synchronized void roadMeshUpdate() {
        this.roadMeshPanel.updateUI();
    }

}
