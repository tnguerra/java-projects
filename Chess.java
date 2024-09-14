/*****************************************************************************
 *  The point of this code is to read a .txt file that has chess piece info  *
 *  store that data into an array using an object structure we made, finally *
 *  we then take user input for a 'next move' and determine if valid (Move).  *
 ****************************************************************************/
/********************************************************
 * Group 15:                                            *
 *      Vicente Corral, Jose Garcia, Thomas Guerra      *
 *                                                      *
 * Responsibilities:                                    *
 *      Vicente - ChessPiece class parameters           *
 *      Jose - Boolean methods for chess pieces         *
 *      Thomas - Implemented file reading, switch case  *
 *                  logic, and integrated code          *
 *                                                      *
 * Version 1.0                                          *
 * - Initial release with basic functionality           *
 *******************************************************/
/*********************************** CHANGE LOG *****************************************
 * 2024-01-18 Once complete with pseudocode, Vicente, Thomas, and Jose began the project.
 * 2024-01-18 Created Chess class and ChessPiece class; Thomas implemented file reading.
 * 2024-01-18 Vicente updated ChessPiece class parameters.
 * 2024-01-19 Modified readFile method to split by comma and convert columns to ASCII.
 * 2024-01-22 Jose developed boolean methods for chess pieces.
 * 2024-01-26 Thomas drafted switch case logic for Knight, Queen, Rook.
 * 2024-01-26 Vicente completed switch case in verifyMove and initial implementation of
 *              checkRook and checkBishop methods.
 * 2024-01-27 Thomas integrated code into one file, adding validation for target position.
 * 2024-01-28 Thomas and Jose refined checkRook and fixed Queen movement validation issues.
 * 2024-01-28 Improved verifyMove method and checkBishop method to fix previous errors.
 * 2024-01-29 Thomas finalized touches, organized code, and corrected documentation.
 ***************************************************************************************/

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Chess{
    /**
     * A class that represents a chess piece with type, color, and position.
     */
    public static class ChessPiece {//did public static class ChessPieces
        public String type;// The type of the chess piece (e.g., "Pawn", "Rook", etc.)
        public String color;// The color of the piece ("white" or "black")
        public int col;// The column position on the board (converted from 'a' to an integer)
        public int row;// The row position on the board
    }
    /**
     * Reads a file containing chess piece information and stores it in an array of ChessPiece objects.
     *
     * @param filepath the path of the file to be read
     * @return an array of ChessPiece objects representing the pieces read from the file
     */
    public static ChessPiece[] readFile(String filepath){
    //Thomas, Vicente Contributed
        int lines = 0;
        try{
            //try catch will catch if no file has name and print the error code
            BufferedReader reader = new BufferedReader(new FileReader(filepath));
            while (reader.readLine() != null){
                lines++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChessPiece[] pieces = new ChessPiece[lines];//create the object using ChessPiece class
        try {
            String currentLine;
            BufferedReader objReader = new BufferedReader(new FileReader(filepath));
            int i = 0;//make sure the line will start at the beginning and increment as we go
            while ((currentLine = objReader.readLine()) != null) {//while has new lines populate the array
                //assuming the file will be in the same format, will split at every comma into 4 tokens
                String[] info = currentLine.split(", ");
                ChessPiece curPiece = new ChessPiece();
                curPiece.type = info[0];
                curPiece.color = info[1];
                curPiece.col = info[2].charAt(0)-97;//convert abc -> ASCII
                curPiece.row = Integer.parseInt(info[3].trim()) - 1 ;
                pieces[i] = curPiece;//using the i we can assign a specific location to store the current piece being read
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pieces;//return the list of pieces
    }
    /**
     * Collects user input for the next move in chess.
     * Ensures input is in a valid format (e.g., a,4).
     *
     * @return a string representing the target position of the move
     */
    public static String userInput(){
    //Thomas, Vicente, Jose Contributed
        //simple buffered reader implementation with a try catch
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = "";//this is to register a blank input of length 0
        try {
            Boolean validInput = false;
            while (!validInput) {
                System.out.println("Enter the target position in the form Letter,Number(ex: a,4): ");
                input = reader.readLine();
                //here we use the if statment to ensure the input is formatted correctly utilizing the string input from earlier
                if (input.length() == 3 && input.charAt(0) - 97 <= 7 && Character.getNumericValue(input.charAt(2)) <= 8 && Character.getNumericValue(input.charAt(2)) >= 1) {
                    validInput = true;
                } else {
                    System.out.println("The target position entered was not valid");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();//this will make sure the reader closes at the end of the method if not will catch anything that could go wrong.
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return input;//if it made it to here will take the user input and return it to be used
    }
    /**
     * Verifies whether the move is valid based on the target position and pieces' current positions.
     *
     * @param targetPosition the user's input move
     * @param pieces the array of chess pieces
     */
    public static void verifyMove(String targetPosition, ChessPiece[] pieces){
    //Thomas, Vicente, Jose Contributed
        //this method will take both the list of pieces and target position
        String[] info = targetPosition.split(",");
        int targetCol = targetPosition.charAt(0)-97;//convert the user input letter to ASCII
        int targetRow = Integer.parseInt(info[1]) - 1;

        // For each piece, check if it can move to the target position
        for(int i = 0; i < pieces.length; i++) {
            boolean valid = false;// Assume move is invalid initially
            ChessPiece curPiece = pieces[i];//make a copy so we do not directly alter the list
            String type = curPiece.type;
            // Prevent the piece from moving to the same square it already occupies
            if (curPiece.col == targetCol && curPiece.row == targetRow) {
                valid = false;
            }else{
                // Depending on the type of piece, determine if the move is valid
                switch (type) {
                    case "Pawn":
                    // Pawns move forward, white moves up, black moves down
                        if (curPiece.col == targetCol) {
                            if (curPiece.color.equals("white") && curPiece.row + 1 == targetRow) {//white pieces ONLY moves up
                                valid = true;
                            } else if (curPiece.color.equals("black") && curPiece.row - 1 == targetRow) {//black pieces ONLY moves downward
                                valid = true;
                            }
                        }
                        break;
                    case "Bishop":
                        valid = checkBishop(curPiece, targetCol, targetRow);
                        break;
                    case "Rook":
                        valid = checkRook(curPiece, targetCol, targetRow);
                        break;
                    case "Queen":
                    // Queen uses logic of both rook and bishop
                        valid = ((checkBishop(curPiece, targetCol, targetRow) || checkRook(curPiece, targetCol, targetRow)) && curPiece.type.equals("Queen"));
                        break;
                    // Knight moves in "L" shape
                    case "Knight":
                        // If the row difference is 2, the column difference must be 1
                        if (Math.abs(targetRow - curPiece.row) == 2) {
                            if (targetCol == curPiece.col - 1 || targetCol == curPiece.col + 1) {
                                valid = true;
                            }
                        // If the column difference is 2, the row difference must be 1
                        } else if (Math.abs(targetCol - curPiece.col) == 2) {
                            if (targetRow == curPiece.row - 1 || targetRow == curPiece.row + 1) {
                                valid = true;
                            }
                        }
                        break;
                    // King moves one square in any direction
                    case "King":
                        if (Math.abs(targetCol - curPiece.col) <= 1 && Math.abs(targetRow - curPiece.row) <= 1) {
                            valid = true;
                        }
                        break;
                }
            }
            // Output whether the move is valid or not
            if(valid){
                System.out.println(curPiece.type + " at " + (char) (curPiece.col + 97) + "," + (curPiece.row+1) + " CAN move to " + targetPosition);
            }else{
                System.out.println(curPiece.type + " at " + (char) (curPiece.col + 97) + "," + (curPiece.row+1) + " can NOT move to " + targetPosition);
            }
        }
    }
    public static boolean checkBishop(ChessPiece curPiece, int targetCol, int targetRow){
    //Thomas, Vicente, Jose Contributed
        try {
            //calculate the differences between start and end points. if both are equal it is valid
            int colDiff = Math.abs(targetCol - curPiece.col);
            int rowDiff = Math.abs(targetRow - curPiece.row);
            return colDiff == rowDiff;//return boolean true or false
        } catch (Exception e){
            return false;//this catch is just incase some kind of calculation error takes place.
        }
    }
    public static boolean checkRook(ChessPiece curPiece, int targetCol, int targetRow){
    //Thomas, Vicente, Jose Contributed
        //both of the following work the same, if in the same row OR column is valid move
        return targetRow == curPiece.row || targetCol == curPiece.col;

    }
    public static void main(String [] args){
        //Thomas, Vicente, Jose Contributed
        String filepath = "chessmoves.txt";//read file
        ChessPiece[] pieces = readFile(filepath);//populate array with data from file
        String target_position = userInput();//collect user input
        verifyMove(target_position, pieces);//take input and calculate using data and user input
    }
}