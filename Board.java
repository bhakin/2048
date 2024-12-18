import java.util.*;
import java.io.*;

public class Board {
   /* Number of tiles showing when the game starts */
   public final int NUM_START_TILES = 2;

   /* The probability (times 100) that a randomly generated tile will be a 2 (vs a 4) */
   public final int TWO_PROBABILITY = 90;

   /* The size of the grid */
   public final int GRID_SIZE;

   /* A reference to the Random object, passed in as a parameter in Board's constructors */
   private final Random random;

   /* The grid of tile values, its size being boardSize * boardSize */
   private int[][] grid;

   /* Current game score, incremented as the tiles merge */
   private int score;

   /* Direction strings */
   public final String LEFT = "LEFT";
   public final String RIGHT = "RIGHT";
   public final String UP = "UP";
   public final String DOWN = "DOWN";

   /**
    * The purpose of this method is to create or construct a fresh
    * board for the user with two random tiles places within the board. This
    * board will have a particular boardSize that the user sets, as well as a
    * random
    */
   public Board(Random random, int boardSize) {
      if (boardSize < 2) boardSize = 4;

      // initialize member variables
      this.random = random;
      this.GRID_SIZE = boardSize;
      this.grid = new int[this.GRID_SIZE][this.GRID_SIZE];
      this.score = 0;

      // loop through and add two initial tiles to the board randomly
      for (int index = 0; index < this.NUM_START_TILES; index++) {
         addRandomTile();
      }
   }

   /**
    * Constructs a board using an input file
    *
    * @param Random random - the random generator for tile values
    * @param String inputBoard - the String type of the name of the file
    *                            that will be loaded
    */
    public Board(Random random, String inputBoard) throws IOException {
        this.random = random;

        File boardFile = new File(inputBoard);
        Scanner input = new Scanner(boardFile);
        int boardSize = 0;
        while ( input.hasNext() ) {
            boardSize = input.nextInt();
            this.score = input.nextInt();
            this.grid = new int[boardSize][boardSize];
            //after loading the first two tokens as the boardSize and score,
            //iterate through the grid and update the values with the rest of the
            //tokens
            for (int row = 0; row <this.grid.length; row++) {
                for (int column = 0; column < this.grid[row].length; column++) {
                    int tileValue = input.nextInt();
                    this.grid[row][column] = tileValue;
                }
            }
        }
       this.GRID_SIZE = boardSize;
    }

    /**
     * Constructor used to load boards for grading/testing
     *
     * @param random
     * @param inputBoard
     */
    public Board(Random random, int[][] inputBoard) {
        this.random = random;
        this.GRID_SIZE = inputBoard.length;
        this.grid = new int[this.GRID_SIZE][this.GRID_SIZE];
        for (int r = 0; r < this.GRID_SIZE; r++) {
            for (int c = 0; c < this.GRID_SIZE; c++) {
                this.grid[r][c] = inputBoard[r][c];
            }
        }
    }

    /**
     * Print the current board as a 2D grid.
     * You should call this method from PA5Tester
     * or whenever you debug your code
     */
    public void printBoard() {
        //looping through the 2D array 
        for (int r = 0; r < grid.length; r+= 1){
            for (int c = 0; c < grid[r].length; c += 1){
                if (c % 4 == 0){
                    System.out.println("");
                }
                System.out.printf("%5d", grid[r][c]);
            }
        }
    }


    /**
     * Print the current board as a 2D grid.
     * You should call this method from PA5Tester
     * or whenever you debug your code
     *
     * @param String direction the tiles will move
     */
    public void printBoard(String direction) {
        int [][] new_board;

        new_board = getGrid(); //get a copy of the grid

        this.move(direction); //moving the board 

        this.printBoard();   //printing the board

        //pass in the saved 2D array from step 1 to reset the board
        setGrid(new_board);  
    }



    /**
     * The purpose of this method is to check to see if the movement of
     * the tiles in any direction can actually take place. It does not move the
     * tiles, but at every index of the grid, checks to see if there is a tile
     * above, below, to the left or right that has the same value. If this is
     * the case, then that tile can be moved. It also checks if there is an
     * empty (0) tile at a specified index, as this also indicates that movement
     * can be possible. This method is called within move() so that that method
     * can determine whether or not tiles should be moved.
     *
     * @param String direction the tiles will move (if possible)
     * @return true if the movement can be done and false if it cannot
     */
    public boolean canMove(String direction){
        // utilize helper methods to check if movement in a particular
        // direction is possible

        if (direction.equals(this.UP)) {
            return this.canMoveUp();
        }
        else if (direction.equals(this.RIGHT)) {
            return this.canMoveRight();
        }
        else if (direction.equals(this.DOWN)) {
            return this.canMoveDown();
        }
        else if (direction.equals(this.LEFT)) {
            return this.canMoveLeft();
        }
        else {
            return false;
        }
    }

    /**
     * determines if a move upwards is possible
     *
     * @return true if such a move is possible, false if no such move is
     */
    private boolean canMoveUp() {
        //looping through the 2D array 
        for (int r = 1; r < grid.length; r+= 1){
            for (int c = 0; c < grid[r].length; c += 1){
                int currentTiles = grid[r][c];
                int nextTiles = grid[r-1][c];

                //checking if the nextTiles is an empty space or not
                if(currentTiles != 0 && nextTiles == 0){
                    //if the next tile is an empty space, it can move
                    return true;
                }

                //checking whether the tiles are empty or not
                if(nextTiles > 0 && currentTiles > 0){
                    //if the tiles match then it could merge 
                    if(nextTiles == currentTiles ){
                        return true;
                    }
                } 
            }
        }
        return false;
    }


    /**
     * determines if a move downwards is possible
     *
     * @return true if such a move is possible, false if no such move is
     */
    private boolean canMoveDown() {
        //looping through the 2D array 
        for (int r = 0; r < grid.length -1; r+= 1){
            for (int c = 0; c < grid[r].length; c += 1){
            //making sure row is not out of bound and won't cause an error
                if(r!= (grid.length -1)){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r+1][c];

                //checking if the nextTiles is an empty space or not
                    if(currentTiles != 0 && nextTiles ==0){
                    //if the next tiles is an empty space, it can move
                        return true;
                    }

                    //checking whether the tiles are empty or not
                    if(nextTiles > 0 && currentTiles> 0){
                        //if the tiles match then it could merge 
                        if(nextTiles == currentTiles ){
                            return true;
                        }
                    }
                }
            } 
        }
        return false;
    }


    /**
     * determines if a move rightward is possible
     *
     * @return true if such a move is possible, false if no such move is
     */
    private boolean canMoveRight() {
        //looping through the 2D array 
       for (int r = 0; r < grid.length; r+= 1){
            for (int c = 0; c < grid[r].length-1; c += 1){
                
            //making sure col is not out of bound and won't an error
                if(c!= (grid[r].length-1)){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r][c+1];

                    //checking if the nextTiles is an empty space or not
                    if(currentTiles != 0 && nextTiles ==0){
                    //if the next tiles is an empty space, it can move
                        return true;
                    }
                    //checking whether the tiles are empty or not
                    if(nextTiles > 0 && currentTiles> 0){
                        //if the tiles match then it could merge 
                        if(nextTiles == currentTiles ){
                            return true;
                        }
                    }
                }
            }   
        }
        return false;
    }


    /**
     * determines if a move leftwards is possible
     *
     * @return true if such a move is possible, false if no such move is
     */
    private boolean canMoveLeft() {
        //looping through the 2D array 
        for (int r = 0; r < grid.length; r+= 1){
            for (int c = 1; c < grid[r].length; c += 1){
                int currentTiles = grid[r][c];
                int nextTiles = grid[r][c-1];

                //checking if the nextTiles is an empty space or not
                if(currentTiles != 0 && nextTiles ==0){
                    //if the next tiles is an empty space, it can move
                    return true;
                }

                //checking whether the tiles are empty or not
                if(nextTiles > 0 && currentTiles> 0){
                    //if the tiles match then it could merge 
                    if(nextTiles == currentTiles ){
                        return true;
                    }
                } 
            }
        }
        return false;
    }


    /**
     * The purpose of this method is to move the tiles in the game
     * board by a specified direction passed in as a parameter. If the movement
     * cannot be done, the method returns false. If the movement can be done, it
     * moves the tiles and returns true. This method relies on the help of four
     * other helper methods to perform the game play.
     *
     * @param String direction the tiles will move (if possible)
     * @return true if the movement can be done and false if it cannot
     */
    public boolean move(String direction) {
        /* if canMove is false, exit and don't move tiles */
        if (!this.canMove(direction)) return false;

        /* move in relationship to the direction passed in */
        if (direction.equals(this.UP)) {
            this.moveUp();
        }
        else if (direction.equals(this.RIGHT)) {
            this.moveRight();
        }
        else if (direction.equals(this.DOWN)) {
            this.moveDown();
        }
        else if (direction.equals(this.LEFT)) {
            this.moveLeft();
        }
        else {
            return false;
        }

        return true;
    }

    /**
     * performs a move upward
     *
     * Precondition: an upward move is possible.
     * Postcondition: The board is modified to reflect the move
     */
    private void moveUp() {
        //moving the tiles to an empty space if there is any
        for(int l = 0; l < grid[0].length; l +=1){
            //looping through the 2D array
            for (int r = 1; r < grid.length; r+= 1){
                for (int c = 0; c < grid[r].length; c += 1){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r-1][c];

                //checking whether nextTiles is an empty spcace or not
                    if(currentTiles != 0 && nextTiles == 0){
                        int temp = 0;
                    //if nextTiles is an empty spcace, move to that space
                        temp = grid[r-1][c];
                        grid[r-1][c] = grid [r][c];
                        grid[r][c] = temp;
                
                    }
                }
            }
        }
        //looping through the 2D array
        for (int r2 = 1; r2 < grid.length; r2+= 1){
            for (int c2 = 0; c2 < grid[r2].length; c2 += 1){
                int currentTiles2 = grid[r2][c2];
                int nextTiles2 = grid[r2-1][c2];
                //checking whether nextTiles is an empty spcace or not
                if (currentTiles2 != 0 && currentTiles2 == nextTiles2){
                    
                    //if the tiles match, merge the tiles together
                    // and set the currentTiles to 0
                    grid[r2-1][c2] = grid[r2][c2] + grid[r2-1][c2];
                    grid[r2][c2] = 0;
                }
            }
        }
        //moving the tiles to an empty space if there is any
        for(int l = 0; l < grid[0].length; l +=1){
            //looping through the 2D array
            for (int r = 1; r < grid.length; r+= 1){
                for (int c = 0; c < grid[r].length; c += 1){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r-1][c];

                //checking whether nextTiles is an empty spcace or not
                    if(currentTiles != 0 && nextTiles == 0){
                        int temp = 0;
                //if nextTiles is an empty spcace, move to that space
                        temp = grid[r-1][c];
                        grid[r-1][c] = grid [r][c];
                        grid[r][c] = temp;
                
                    }
                }
            }
        }
    }


    /**
     * performs a move downward
     * Precondition: a downward move is possible.
     * Postcondition: The board is modified to reflect the move
     */
    private void moveDown() {
         //moving the tiles to an empty space if there is any
        for(int l = 0; l < grid[0].length; l+= 1){
            //looping through the 2D array
            for (int r = 0; r < grid.length -1; r+= 1){
                for (int c = 0; c < grid[r].length; c += 1){
                //making sure the row is not out of bound and won't an error
                    if(r!= (grid.length -1)){
                        int currentTiles = grid[r][c];
                        int nextTiles = grid[r+1][c];

                    //checking whether nextTiles is an empty spcace or not
                        if(currentTiles != 0 && nextTiles == 0){
                            int temp = 0;
                    //if nextTiles is an empty spcace, move to that space
                            temp = grid[r+1][c];
                            grid[r+1][c] = grid [r][c];
                            grid[r][c] = temp;
                        }
                    }
                }
            }
        }
        //looping through the 2D array
        for (int r2 = 2; r2 > -1; r2-= 1){
            for (int c2 = 0; c2 < grid[r2].length; c2 += 1){
                //making sure the row is not out of bound and won't an error
                if(r2!= (grid.length -1)){
                    int currentTiles2 = grid[r2][c2];
                    int nextTiles2 = grid[r2+1][c2];

                    //checking whether nextTiles is an empty spcace or not
                    if (currentTiles2 != 0 && currentTiles2 == nextTiles2){

                    //if the tiles match, merge the tiles together
                    // and set the currentTiles to 0
                    grid[r2+1][c2] = grid[r2][c2] + grid[r2+1][c2];
                    grid[r2][c2] = 0;
                    }
               }
            }
        }
        //moving the tiles to an empty space if there is any
        for(int l = 0; l < grid[0].length; l+= 1){
            //looping through the 2D array
            for (int r = 0; r < grid.length -1; r+= 1){
                for (int c = 0; c < grid[r].length; c += 1){
            //making sure the row is not out of bound and won't an error
                    if(r!= (grid.length -1)){
                        int currentTiles = grid[r][c];
                        int nextTiles = grid[r+1][c];

                    //checking whether nextTiles is an empty spcace or not
                        if(currentTiles != 0 && nextTiles == 0){
                            int temp = 0;
                    //if nextTiles is an empty spcace, move to that space
                            temp = grid[r+1][c];
                            grid[r+1][c] = grid [r][c];
                            grid[r][c] = temp;
                        }
                    }
                }
            }
        }
    }


    /**
     * performs a move left
     * Precondition: a left move is possible.
     * Postcondition: The board is modified to reflect the move
     */
    private void moveLeft() {
        //moving the tiles to an empty space if there is any
        for (int l = 0; l < grid[0].length; l += 1){
            //looping through the 2D array
            for (int r = 0; r < grid.length; r+= 1){
                for (int c = 1; c < grid[r].length; c += 1){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r][c-1];

                //checking whether nextTiles is an empty spcace or not
                    if(currentTiles != 0 && nextTiles == 0){
                        int temp = 0;
                    //if nextTiles is an empty spcace, move to that space
                        temp = grid[r][c-1];
                        grid[r][c-1] = grid [r][c];
                        grid[r][c] = temp;
                
                    }
                }
            }
        }
        //looping through the 2D array
        for (int r2 = 0; r2 < grid.length; r2+= 1){
            for (int c2 = 1; c2 < grid[r2].length; c2 += 1){ 
                int currentTiles2 = grid[r2][c2];
                int nextTiles2 = grid[r2][c2-1];

                //checking whether nextTiles is an empty spcace or not
                if (currentTiles2 != 0 && currentTiles2 == nextTiles2){

                    //if the tiles match, merge the tiles together
                    // and set the currentTiles to 0
                    grid[r2][c2-1] = grid[r2][c2] + grid[r2][c2-1];
                    grid[r2][c2] = 0;
                }
            }
        }
        //moving the tiles to an empty space if there is any
        for (int l = 0; l < grid[0].length; l += 1){
            //looping through the 2D array
            for (int r = 0; r < grid.length; r+= 1){
                for (int c = 1; c < grid[r].length; c += 1){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r][c-1];

            //checking whether nextTiles is an empty spcace or not
                    if(currentTiles != 0 && nextTiles == 0){
                        int temp = 0;
                //if nextTiles is an empty spcace, move to that space
                        temp = grid[r][c-1];
                        grid[r][c-1] = grid [r][c];
                        grid[r][c] = temp;
                
                    }
                }
            }
        }
    }


    /**
     * performs a move right
     * Precondition: a right move is possible.
     * Postcondition: The board is modified to reflect the move
     */
    private void moveRight() {
         //moving the tiles to an empty space if there is any
        for(int l = 0; l < grid[0].length; l += 1){
             //looping through the 2D array
            for (int r = 0; r < grid.length; r+= 1){
                for (int c = 0; c < grid[r].length-1; c += 1){
                //making sure the col is not out of bound and won't an error
                    if(c!= (grid[r].length-1)){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r][c+1];

                    //checking whether nextTiles is an empty spcace or not
                        if(currentTiles != 0 && nextTiles == 0){
                            int temp = 0;
                    //if nextTiles is an empty spcace, move to that space
                            temp = grid[r][c+1];
                            grid[r][c+1] = grid [r][c];
                            grid[r][c] = temp;
                        }
                    }
                }
            }
        }
        //looping through the 2D array
        for (int r2 = 0; r2 < grid.length; r2+= 1){
            for (int c2 = 2; c2 >-1; c2 -= 1){  
                //making sure the col is not out of bound and won't an error
                if(c2!= (grid[r2].length-1)){
                    int currentTiles2 = grid[r2][c2];
                    int nextTiles2 = grid[r2][c2+1];

                    //checking whether nextTiles is an empty spcace or not
                    if (currentTiles2 != 0 && currentTiles2 == nextTiles2){
                    
                    //if the tiles match, merge the tiles together
                    // and set the currentTiles to 0
                    grid[r2][c2+1] = grid[r2][c2] + grid[r2][c2+1];
                    grid[r2][c2] = 0;
                    }
                }
            }
        }   
        //moving the tiles to an empty space if there is any
        for(int l = 0; l < grid[0].length; l += 1){
            //looping through the 2D array
            for (int r = 0; r < grid.length; r+= 1){
                for (int c = 0; c < grid[r].length-1; c += 1){
                //making sure the col is not out of bound and won't an error
                    if(c!= (grid[r].length-1)){
                    int currentTiles = grid[r][c];
                    int nextTiles = grid[r][c+1];

                    //checking whether nextTiles is an empty spcace or not
                        if(currentTiles != 0 && nextTiles == 0){
                            int temp = 0;
                        //if nextTiles is an empty spcace, move to that space
                            temp = grid[r][c+1];
                            grid[r][c+1] = grid [r][c];
                            grid[r][c] = temp;
                        }
                    }
                }
            }
        }

    }



    /**
     * saves the current board to a file
     *
     * @param String outputBoard - the string of the name of the file that
     *                              will be created
     */
    public void saveBoard(String outputBoard) throws IOException {
        File boardFile = new File( outputBoard );
        PrintWriter output = new PrintWriter( boardFile );
        output.println(this.GRID_SIZE);
        output.println(this.score);
        //iterate through the grid and print it, new line for every row
        for (int row = 0; row < this.grid.length; row++) {
            int count = 0;
            for (int column = 0; column < this.grid[row].length; column++) {
                output.print(this.grid[row][column] + " ");
                count += 1;
                if ( count == this.GRID_SIZE) {
                    output.println();
                    count = 0;
                }
            }
        }
        output.close();
    }

    /**
     * gets the count of empty spaces in a grid
     *
     * @return int count - the number of empty spaces
     */
    public int getCount() {
        int count = 0;
        for (int row = 0; row < this.grid.length; row++) {
            for (int column = 0; column < this.grid[row].length; column++) {
                int tileValue = this.grid[row][column];
                if ( tileValue == 0 ) {
                    count += 1;
                }
            }
        }
        return count;
    }

    /**
     * The purpose of this method is to add a random tile of either
     * value 2 or 4 to a random empty space on the 2048
     * board. The place where this tile is added is dependent on the random
     * value associated with each board object. If no tiles are empty, it
     * returns without changing the board.
     */
    public void addRandomTile() {
        int count = this.getCount();
        if (count == 0) {
            System.out.println("There are no empty spaces!");
            return;
        }

        int location = this.random.nextInt(count);
        int value = this.random.nextInt(100);
        int emptySpaces = -1;
        //iterate through the 2D array
        for (int row = 0; row < this.grid.length; row++) {
            for (int column = 0; column < this.grid[row].length; column++) {
                int tileValue = this.grid[row][column];
                if (tileValue == 0)
                    emptySpaces += 1;

                //when the number of the empty space equals the random location,
                //add either a 2 or 4 to that location
                if (emptySpaces == location) {
                    if ( value < this.TWO_PROBABILITY ) {
                        this.grid[row][column] = 2;
                        return;
                    }
                    else {
                        this.grid[row][column] = 4;
                        return;
                    }
                }
            }
        }
    }

    /**
     * The purpose of this method is to check whether or not the game
     * in play is over. The game is officially over once there are no longer any
     * valid moves that can be made in any direction. If the game is over, this
     * method will return true and print the words: "Game Over!" This method
     * will be checked before any movement is ever made.
     *
     * @return true if the game is over, and false if the game isn't over
     */
    public boolean isGameOver() {
        return (!canMoveLeft() && !canMoveRight() && !canMoveUp()
                && !canMoveDown());
    }

    /**
     * get a copy of the grid
     *
     * @return A copy of the grid
     */
    public int[][] getGrid() {
        int[][] gridCopy = new int[this.GRID_SIZE][this.GRID_SIZE];
        for (int r = 0; r < this.grid.length; r++) {
            for (int c = 0; c < this.grid[r].length; c++) {
                gridCopy[r][c] = this.grid[r][c];
            }
        }
        return gridCopy;
    }

    /**
     * get a copy of the grid
     *
     * @param newGrid the grid that you want to set to
     */
    public void setGrid(int[][] newGrid) {
        for (int r = 0; r < this.grid.length; r++) {
            for (int c = 0; c < this.grid[r].length; c++) {
                this.grid[r][c] = newGrid[r][c];
            }
        }
    }

    /**
     * return the tile value in a particular cell in the grid.
     *
     * @param row The row
     * @param col The column
     * @return The value of the tile at (row, col)
     */
    public int getTileValue(int row, int col) {
        return this.grid[row][col];
    }

    /**
     * Get the current score
     *
     * @return the current score of the game
     */
    public int getScore() {
        return this.score;
    }

    @Override
    public String toString() {
        StringBuilder outputString = new StringBuilder();
        outputString.append(String.format("Score: %d\n", this.score));
        for (int row = 0; row < this.GRID_SIZE; row++) {
            for (int column = 0; column < this.GRID_SIZE; column++)
                outputString.append(this.grid[row][column] == 0 ? "    -" :
                        String.format("%5d", this.grid[row][column]));

            outputString.append("\n");
        }
        return outputString.toString();
    }
}
