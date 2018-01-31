import java.util.Scanner;

public class test {

    public static void main(String args[]){

        Scanner sc = new Scanner(System.in);

        int n = sc.nextInt();

        int matrix[][] = new int[n][n];
        int rowMax = 0, colMin = 0;
        int x = 0, y = 0;

        for (int i = 0; i < n; i++){
            int row_min = 0;
            for (int j = 0; j < n; j++){
                int tmp = sc.nextInt();
                matrix[i][j] = tmp;
                if (tmp < row_min || row_min == 0)
                    row_min = tmp;
            }

            if (rowMax < row_min || rowMax == 0) {
                rowMax = row_min;
                x = i;
            }
        }


        for (int i = 0; i < n; i++){
            int col_max = 0;
            for (int j = 0; j < n; j++){

                if (matrix[j][i] > col_max || col_max == 0)
                    col_max = matrix[j][i];
            }

            if (colMin > col_max || colMin == 0) {
                colMin = col_max;
                y = i;
            }
        }

//        System.out.println(colMin + " "+y+" " + rowMax+" "+x);

        if (colMin == rowMax)
            System.out.println(0);
        else {
            int count1 = 0, count2 = 0;
            for (int i = 0; i < n; i++){
                if (matrix[x][i] < colMin)
                    count1++;
            }
            for (int i = 0; i < n; i++){
                if (matrix[i][y] > rowMax)
                    count2++;
            }

            System.out.println(Math.min(count1, count2));
        }


    }
}
