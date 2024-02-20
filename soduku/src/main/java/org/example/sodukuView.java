package org.example;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.util.ArrayList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

public class sodukuView extends JFrame {
    private JTable tblSoduku;
    private JButton btnDefault;
    private JButton btnClear;
    private JButton btnSolve;
    public JPanel mainPanel;
    private JLabel lblHeader;
    private JTextArea reportArea;
    private JTextArea reportStatus;
    private JTextField tfl = new JTextField();


    ArrayList<Integer[]> arlSoduku = new ArrayList<>();
    ArrayList<Integer[]> arlCellPossibleValues = new ArrayList<Integer[]>();

    Object[][] data = {
            {0, 0, 2}, {0, 1, 4}, {0, 2, 9}, {0, 4, 7}, {0, 5, 6},
            {1, 0, 8}, {1, 1, 3}, {1, 7, 6}, {1, 8, 9},
            {2, 5, 9}, {2, 6, 5}, {2, 7, 2},
            {3, 0, 3}, {3, 1, 6}, {3, 3, 2}, {3, 4, 4}, {3, 7, 5},
            {4, 1, 9}, {4, 7, 1}, {4, 6, 4},
            {5, 1, 5}, {5, 2, 4}, {5, 3, 6}, {4, 7, 1},
            {6, 1, 1}, {6, 2, 6}, {6, 3, 9}, {6, 6, 2}, {6, 8, 8},
            {7, 2, 7}, {7, 3, 1}, {7, 4, 6}, {7, 5, 8}, {7, 8, 5},
            {8, 0, 9}, {8, 5, 5}, {8, 8, 1}
    };

    int[][] sudoku = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    public sodukuView() {
        //Setting row & column sizes
        int intRowSize = 40;
        int intColSize = 40;
        tblSoduku.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        tblSoduku.setRowHeight(intRowSize);
        for (int i = 0; i < tblSoduku.getRowCount(); i++) {
            tblSoduku.setRowHeight(i, intColSize);
        }
        //Setting up table for gui
        int intNoCols = 9;
        int intNoRows = 9;
        DefaultTableModel tableModel = new DefaultTableModel(intNoCols, intNoRows);
        tblSoduku.setModel(tableModel);

        //TableModelListener to track changes in the table
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                int row = e.getFirstRow();
                int column = e.getColumn();
                if (e.getType() == TableModelEvent.UPDATE) {
                    Object data = tblSoduku.getValueAt(row, column);
                    System.out.println("Cell at (" + row + ", " + column + ") updated to: " + data);
                }
            }
        });
        btnDefault.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //tblSoduku.setEnabled(false);
                //Clear table
                for (int i = 0; i < tblSoduku.getColumnCount(); i++) {
                    for (int j = 0; j < tblSoduku.getRowCount(); j++) {
                        if (tblSoduku.isCellEditable(i, j)) {
                            tblSoduku.setValueAt(null, i, j);
                        }
                        ;
                    }
                }
                //Set default values
                for (int i = 0; i < data.length; i++) {
                    int dataRow = (int) data[i][0];
                    int dataCol = (int) data[i][1];
                    int dataAt = (int) data[i][2];
                    tblSoduku.setValueAt(dataAt, dataRow, dataCol);
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tblSoduku.setEnabled(true);
                for (int i = 0; i < tblSoduku.getColumnCount(); i++) {
                    for (int j = 0; j < tblSoduku.getRowCount(); j++) {
                        if (tblSoduku.isCellEditable(i, j)) {
                            tblSoduku.setValueAt(null, i, j);
                        }
                        ;
                    }
                }
            }
        });
        btnSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tblSoduku.setEnabled(false);
                populateCellPossibleValues();
                getSudokuData();
                completeSudoku();
                if(fullyCompleted(data)){
                    tblSoduku.remove(btnSolve);
                    tblSoduku.updateUI();
                    btnSolve.setVisible(false);
                    tblSoduku.updateUI();
                }
            }
        });
        reportArea.addContainerListener(new ContainerAdapter() {
            @Override
            public void componentAdded(ContainerEvent e) {
                super.componentAdded(e);
            }
        });
    }
    public void getSudokuData() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (tblSoduku.getValueAt(i, j) != null) {
                    int cellValue = (int) tblSoduku.getValueAt(i, j);
                    sudoku[i][j] = cellValue;
                }
            }
        }
    }
    public void populateCellPossibleValues() {
        for (int i = 0; i < 100; i++) {
            Integer[] possibleValues = {1, 2, 3, 4, 5, 6, 7, 8, 9};
            arlCellPossibleValues.add(i, possibleValues);
        }
    }
    public void completeSudoku() {
        for (int i = 0; i < 9; i++) {//row
            for (int j = 0; j < 9; j++) {//col
                if (sudoku[i][j]==0){
                    int possibleValuesPosition = i * 10 + j;
                    System.out.println(possibleValuesPosition + " Empty");
                    //Remove used values in possible cell values
                    for (int k = 0; k < 9; k++) {//Iterator for rows and columns
                        //Remove used in row values in possible cell values
                        Integer[] possibleValues = arlCellPossibleValues.get(possibleValuesPosition);
                        for (int l = 0; l < 9; l++) {//iterator of possibleValues for each cell
                            int pv = possibleValues[l];
                            if(pv==sudoku[i][k]||pv==sudoku[k][j]){
                                possibleValues[l] = 0;
                            }
                        }
                        //Block Checks
                        if(i<3){
                            if(j<3){//looping through block
                                for (int l = 0; l < 3; l++) {
                                    for (int m = 0; m < 3; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (j<6) {
                                for (int l = 0; l < 3; l++) {
                                    for (int m = 3; m < 6; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            }else {
                                for (int l = 0; l < 3; l++) {
                                    for (int m = 6; m < 9; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else if (i<6) {//Middle rank
                            if(j<3){
                                for (int l = 3; l < 6; l++) {
                                    for (int m = 0; m < 3; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            } else if (j<6) {
                                for (int l = 3; l < 6; l++) {
                                    for (int m = 3; m < 6; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }else {
                                for (int l = 3; l < 6; l++) {
                                    for (int m = 6; m < 9; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }else {
                            if(j<3){
                                for (int l = 6; l < 9; l++) {
                                    for (int m = 0; m < 3; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }

                            } else if (j<6) {
                                for (int l = 6; l < 9; l++) {
                                    for (int m = 3; m < 6; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }else {
                                for (int l = 6; l < 9; l++) {
                                    for (int m = 6; m < 9; m++) {
                                        if(i!=l&&j!=m){
                                            if (sudoku[l][m]!=0){
                                                for (int n = 0; n < 9; n++) {//iterator of possibleValues for each cell
                                                    int pv = possibleValues[n];
                                                    if(pv==sudoku[l][m]){
                                                        possibleValues[n] = 0;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (greatestArrayValue(possibleValues)==sumArray(possibleValues))
                        {
                            tblSoduku.setValueAt(greatestArrayValue(possibleValues),i,j);
                        }
                        //Monitor code--------Possible cell Values-------
                        for(int pv : possibleValues){
                            System.out.print(pv + " ");
                        }
                        System.out.println( " ");
                        //-----------------------------------------------
                        //Remove used in column values in possible cell values
                    }
                    //Remove used in block values in possible cell values
                }else {
                    System.out.println("Filled");
                }
            }
        }
    }
    public boolean fullyCompleted(Object[][] arrayData){
        boolean done = true;
        for (Object[] arrayDatum : arrayData) {
            for (Object integer : arrayDatum) {
                Integer integer1 = (Integer) integer;
                if (integer1 == 0) {
                    done = false;
                    break;
                }
            }
        }
        return done;
    }
    public int greatestArrayValue(Integer[] array) {
        int greatestValue = 0;
        int length = array.length;
        for (Integer cellValue : array) {
            if (greatestValue < cellValue) {
                greatestValue = cellValue;
            }
        }
        return greatestValue;
    }
    public int sumArray(Integer[] array) {
        int sum = 0;
        int length = array.length;
        for (Integer s : array) {
            int cellValue = s;
            sum += cellValue;
        }
        return sum;
    }
}
