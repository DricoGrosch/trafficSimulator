package view;

import controller.MeshController;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;


public class RoadMeshPanel extends JPanel {

    private static final long serialVersionUID = 1L;

    class RoadTableModel extends AbstractTableModel {
        private static final long serialVersionUID = 1L;

        @Override
        public int getRowCount() {
            return meshController.getLines();
        }

        @Override
        public int getColumnCount() {
            return meshController.getColumns();
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            return new ImageIcon(meshController.getMatrixPosition(rowIndex, columnIndex));
        }
    }

    private MeshController meshController;
    private JTable roadMesh;

    public RoadMeshPanel() {
        this.meshController = MeshController.getInstance();
        this.meshController.readAndCreateMatrix();
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        this.setOpaque(false);
        this.initComponents();
    }

    private void initComponents() {
        roadMesh = new JTable();
        roadMesh.setModel(new RoadTableModel());
        for (int x = 0; x < roadMesh.getColumnModel().getColumnCount(); x++) {
            roadMesh.getColumnModel().getColumn(x).setWidth(25);
            roadMesh.getColumnModel().getColumn(x).setMinWidth(25);
            roadMesh.getColumnModel().getColumn(x).setMaxWidth(25);
        }
        roadMesh.setRowHeight(25);
        roadMesh.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roadMesh.setIntercellSpacing(new Dimension(0, 0));
        roadMesh.setDefaultRenderer(Object.class, new RoadMeshItemRender());
        this.add(roadMesh);
    }


    public int getWidth() {
        return (int) roadMesh.getMaximumSize().getWidth();
    }

}
