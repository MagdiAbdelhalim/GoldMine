package com.goldmine;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;
import java.sql.SQLOutput;
import java.util.Arrays;

public class GoldMineSolver {
    static int matrixSizeX = 0;
    static int matrixSizeY = 0;


    public static int main(String[] args) {

        int TreeMax;
        int goldFinal = 0;
        String csvFilePath;

        if (args[0] == null) {
            csvFilePath = getCSVFileName();  // get csv File name via Dialogue
        } else {
            csvFilePath = args[0];
        }

        validateCSVFile(csvFilePath);  // validation & array dimensional setting

        int[][] goldMatrix = new int[matrixSizeX][matrixSizeY];

        setGoldMatrix(goldMatrix, csvFilePath);

        for (int i = 0; i < matrixSizeX; i++) {
            TreeMax = goldMatrix[i][0] + calculateTreeMax(i, 0, matrixSizeX-1, matrixSizeY-1, goldMatrix);
            if (TreeMax > goldFinal) {
                goldFinal = TreeMax;
            }
        }
        System.out.println("------------------------");
        System.out.println("Maximum Gold = " + goldFinal);
        System.out.println("------------------------");

        return goldFinal;
    }

    public static int calculateTreeMax(int row, int col, int matrixSizeX, int matrixSizeY, int[][] goldMatrix) {

        int[] goldMax = new int[3];

        if (col < matrixSizeY) {
            if (((row - 1) >= 0) & ((row - 1) <= matrixSizeX-1)) {
                goldMax[0] = goldMatrix[row - 1][col + 1] + calculateTreeMax(row - 1, col + 1, matrixSizeX, matrixSizeY, goldMatrix);
            }
            if ((row >= 0) & (row <= matrixSizeX)) {
                goldMax[1] = goldMatrix[row][col + 1] + calculateTreeMax(row, col + 1, matrixSizeX, matrixSizeY, goldMatrix);
            }
            if (((row + 1) <= matrixSizeX) & ((row + 1) >= 0)) {
                goldMax[2] = goldMatrix[row + 1][col + 1] + calculateTreeMax(row + 1, col + 1, matrixSizeX, matrixSizeY, goldMatrix);
            }
        }
        return Math.max(goldMax[0], Math.max(goldMax[1], goldMax[2]));
    }

    public static void setGoldMatrix(int [][] goldMatrix, String csvFilePath) {

        // Select CSV Input File into goldMatrix
        String csvLine;
        String[] goldMatrixTXT = null;

        int i = 0;
        int j = 0;
        BufferedReader csvBR;
        try {
            csvBR = new BufferedReader(new FileReader(csvFilePath));

            while ((csvLine = csvBR.readLine()) != null) {
                goldMatrixTXT = csvLine.split(",");
                    for (i = 0; i < goldMatrixTXT.length; i++) {
                        goldMatrix[j][i] = Integer.parseInt(goldMatrixTXT[i]);
                    }
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert goldMatrixTXT != null;
        System.out.println("Matrix read successfully.");
        System.out.println(Arrays.deepToString(goldMatrix));
    }

    //
    // Select input csv file
    //
    public static String getCSVFileName() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        File selectedFile = null;
        FileFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
        fileChooser.addChoosableFileFilter(filter);
        // Set filter so, only csv files can be selected
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setDialogTitle("Gold Mine: Select CSV Input File");
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
        } else if (result == JFileChooser.CANCEL_OPTION) {
            System.out.println("No csv File was selected and cancel was selected");
            System.exit(0);
        }

        // Get the file path
        assert selectedFile != null;
        if (selectedFile.exists()) {
            return selectedFile.getAbsolutePath();
        } else {
            System.out.println("csv file can not be reach");
            System.exit(0);
            return null;
        }
    }

    public static void validateCSVFile(String csvFilePath) {
    // Validate all csv file records & get it dimensions
        String csvLine;
        String[] goldMatrixTXT = null;

        int i = 0;
        int j = 0;
        int goldMatrixOldLength = 0;
        BufferedReader csvBR;
        try {
            csvBR = new BufferedReader(new FileReader(csvFilePath));

            while ((csvLine = csvBR.readLine()) != null) {
                 // Check if two or more comas are there with no numbers in between
                 if(csvLine.trim().contains(",,")) {
                     System.out.println("CSV File Error - 005\nFound empty entries at row number " + (j + 1));
                     System.exit(0);
                     }
                //Split the new line values
                goldMatrixTXT = csvLine.split(",");

                // Set Row Length
                if ((goldMatrixOldLength == 0) & (goldMatrixTXT.length > 0)) goldMatrixOldLength = goldMatrixTXT.length; //set intial old value

                // Rows length are ot identicals
                if (goldMatrixTXT.length != goldMatrixOldLength) {
                    System.out.println("CSV File Error - 001\nArray shall be with identical row length.");
                    System.out.println("Row Number " + j + " has " + goldMatrixTXT.length + " Instead of " + goldMatrixOldLength);
                    System.exit(0);
                }
                for(i=0; i < goldMatrixTXT.length; i++){
                    // Check all values are > Zero
                    if(Integer.parseInt(goldMatrixTXT[i] )< 0 ) {
                        System.out.println("CSV File Error - 002\nArray shall be all positive numbers only.");
                        System.out.println("Row Number " + j + " has a negative value of " + goldMatrixTXT[i]);
                        System.exit(0);
                    }
                    // Check if all records are numbers only
                    if( !goldMatrixTXT[i].contentEquals(String.valueOf(Integer.parseInt(goldMatrixTXT[i])) ))  {
                        System.out.println("CSV File Error - 003\nArray shall be all numbers only.");
                        System.out.println("Row Number " + j + " has non number :" + goldMatrixTXT[i]);
                        System.exit(0);
                    }
                }
                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Test Array Size limits
        if(Math.max(i,j) > 20) {
            System.out.println("CSV File Error - 004\nprogram Can solve upto 100 rows and 100 col maximum.");
            System.out.println("Number of rows = " + j + " and number of columns = " + i );
            System.exit(0);
        }
        matrixSizeX = j;
        matrixSizeY = i;
        System.out.println("Matrix Size " + j + " x " + i);
    }
}