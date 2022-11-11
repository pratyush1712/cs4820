import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

class Main {

    // declaring nodes and edges
    static String[][] inputMatrix;
    static int startPoint;
    static int endPoint;
    static int minRaisins;
    static int[][][][] solutionMatrix;
    static int toCheck;
    static int[][][][] numRaisinsMatrix;

    /**
     * It creates a 4D matrix of size rows x cols x rows x cols and initializes all
     * elements to -1
     * 
     * @param rows number of rows in the matrix
     * @param cols number of columns in the matrix
     * @return A 4D array of ints.
     */
    static int[][][][] createSegmentMatrix(int rows, int cols) {
        int[][][][] matrix = new int[rows][cols][rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                for (int k = 0; k < rows; k++) {
                    for (int z = 0; z < cols; z++) {
                        matrix[i][j][k][z] = -1;
                    }
                }
            }
        }
        return matrix;
    }

    /**
     * It takes in the coordinates of the top left and bottom right corners of a
     * rectangle, and returns
     * the maximum number of segments that can be made in that rectangle
     * 
     * @param a    starting row
     * @param b    the number of rows in the matrix
     * @param c    number of columns
     * @param d    number of columns
     * @param main the main matrix
     * @return The maximum number of segments that can be made in the given matrix.
     */
    static int findMax(int a, int b, int c, int d, int[] main) {
        if (c < a || d < b) {
            return -1;
        }
        if (solutionMatrix[a][b][c][d] != -1) {
            return solutionMatrix[a][b][c][d];
        }
        int numRaisins = 0;
        try {
            if (numRaisinsMatrix[a][b][c][d] != -1) {
                numRaisins = numRaisinsMatrix[a][b][c][d];
            } else if (c == main[2] && b == main[1] && d == main[3] && numRaisinsMatrix[main[0]][b][a - 1][d] != -1) {
                numRaisins = numRaisinsMatrix[main[0]][main[1]][main[2]][main[3]]
                        - numRaisinsMatrix[main[0]][b][a - 1][d];
            } else if (a == main[0] && b == main[1] && d == main[3] && numRaisinsMatrix[c + 1][b][main[2]][d] != -1) {
                numRaisins = numRaisinsMatrix[main[0]][main[1]][main[2]][main[3]]
                        - numRaisinsMatrix[c + 1][b][main[2]][d];
            } else if (b == main[1] && a == main[0] && c == main[2] && numRaisinsMatrix[a][d + 1][c][main[3]] != -1) {
                numRaisins = numRaisinsMatrix[main[0]][main[1]][main[2]][main[3]]
                        - numRaisinsMatrix[a][d + 1][c][main[3]];
            } else if (d == main[3] && a == main[0] && c == main[2] && numRaisinsMatrix[a][main[1]][c][b - 1] != -1) {
                numRaisins = numRaisinsMatrix[main[0]][main[1]][main[2]][main[3]]
                        - numRaisinsMatrix[a][main[1]][c][b - 1];
            } else {
                for (int i = a; i <= c; i++) {
                    for (int j = b; j <= d; j++) {
                        if (inputMatrix[i][j].equalsIgnoreCase("r")) {
                            numRaisins++;
                        }
                    }
                }
            }
        } catch (Exception e) {
            for (int i = a; i <= c; i++) {
                for (int j = b; j <= d; j++) {
                    if (inputMatrix[i][j].equalsIgnoreCase("r")) {
                        numRaisins++;
                    }
                }
            }
        }
        numRaisinsMatrix[a][b][c][d] = numRaisins;
        if (toCheck == 1) {
            return numRaisins;
        }
        if (numRaisins < minRaisins) {
            solutionMatrix[a][b][c][d] = 0;
            return 0;
        } else if (numRaisins < 2 * minRaisins) {
            solutionMatrix[a][b][c][d] = 1;
            return 1;
        } else {
            int maxSegments = 0;
            for (int row = a; row < c; row++) {
                int bottom = findMax(row + 1, b, c, d, new int[] { a, b, c, d });
                int top = findMax(a, b, row, d, new int[] { a, b, c, d });
                int val = (top >= 0 ? top : 0) + (bottom >= 0 ? bottom : 0);
                if (val > maxSegments) {
                    maxSegments = val;
                }
            }
            for (int col = b; col < d; col++) {
                int right = findMax(a, col + 1, c, d, new int[] { a, b, c, d });
                int left = findMax(a, b, c, col, new int[] { a, b, c, d });
                int val = right + left;
                if (val > maxSegments) {
                    maxSegments = val;
                }
            }
            solutionMatrix[a][b][c][d] = maxSegments;
            return maxSegments;
        }
    }

    static File file = new File("D:\\cs4820\\hw03\\test.txt");

    public static void main(String[] args) throws NumberFormatException, IOException {
        // BufferedReader br = new BufferedReader(new FileReader(file));
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String[] initalizers = br.readLine().split(" ");
        minRaisins = Integer.parseInt(initalizers[2]);
        toCheck = Integer.parseInt(initalizers[2]);
        int rows = Integer.parseInt(initalizers[0]);
        int cols = Integer.parseInt(initalizers[1]);
        solutionMatrix = createSegmentMatrix(rows, cols);
        numRaisinsMatrix = createSegmentMatrix(rows, cols);
        inputMatrix = new String[rows][cols];
        for (int row = 0; row < rows; row++) {
            String rowValues = br.readLine();
            for (int col = 0; col < cols; col++) {
                inputMatrix[row][col] = String.valueOf(rowValues.charAt(col));
            }
        }
        int val = findMax(0, 0, rows - 1, cols - 1, new int[] { 0, 0, rows - 1, cols - 1 });
        System.out.println(val);
        br.close();
    }
}