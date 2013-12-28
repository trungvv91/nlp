/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nlp.graph;
/**
 *
 * @author Manh Tien
 */
public class QuickSort {
    public static void  swap(double A[], int x, int y){
        double temp = A[x];
        A[x] = A[y];
        A[y] = temp;
    }
    public static void  swap(int A[], int x, int y){
        int temp = A[x];
        A[x] = A[y];
        A[y] = temp;
    }
    public static int partition(double A[], int B[], int left, int right){
        int i = left;
        int j = right;
        double pivot = A[(left + right)/2];
        while(i < j){
            while(A[i] > pivot)
                i++;
            while(A[j] < pivot)
                j--;
            if(i <= j){
                swap(A, i, j);
                swap(B, i, j);
                i++;
                j--;
            }
        }
        return i;
    }
    public static int partition(int A[], int B[], int left, int right){
        int i = left;
        int j = right;
        int pivot = A[(left + right)/2];
        while(i < j){
            while(A[i] > pivot)
                i++;
            while(A[j] < pivot)
                j--;
            if(i <= j){
                swap(A, i, j);
                swap(B, i, j);
                i++;
                j--;
            }
        }
        return i;
    }
    public static int partition(double[] A, int left, int right){
        int i = left;
        int j = right;
        double pivot = A[(left + right)/2];
        while(i < j){
            while(A[i] < pivot)
                i++;
            while(A[j] > pivot)
                j--;
            if(i <= j){
                swap(A, i, j);
                i++;
                j--;
            }
        }
        return i;
    }
    public static int partition(int[] A, int left, int right){
        int i = left;
        int j = right;
        int pivot = A[(left + right)/2];
        while(i < j){
            while(A[i] < pivot)
                i++;
            while(A[j] > pivot)
                j--;
            if(i <= j){
                swap(A, i, j);
                i++;
                j--;
            }
        }
        return i;
    }
    public static void QuickSortFunction(double A[], int B[], int left, int right){
        int index = partition(A, B, left, right);
        if(left < index - 1)
            QuickSortFunction(A, B, left, index - 1);
        if(right > index)
            QuickSortFunction(A, B, index, right);
    }
    
    public static void QuickSortFunction(int A[], int B[], int left, int right){
        int index = partition(A, B, left, right);
        if(left < index - 1)
            QuickSortFunction(A, B, left, index - 1);
        if(right > index)
            QuickSortFunction(A, B, index, right);
    }
    
    public static void QuickSortFunction(double A[], int left, int right){
        int index = partition(A, left, right);
        if(left < index - 1)
            QuickSortFunction(A, left, index - 1);
        if(right > index)
            QuickSortFunction(A, index, right);
    }
    
    public static void QuickSortFunction(int A[], int left, int right){
        //test();
        //if(left >= right) return;
        int index = partition(A, left, right);
        if(left < index - 1)
            QuickSortFunction(A, left, index - 1);
        if(right > index)
            QuickSortFunction(A, index, right);
    }

    public static void main(String[] args){
        int A[] = {4, 1, 9, 1, 8};
        int B[] = new int[A.length];
        
        for(int i = 0; i < A.length; i++){
            System.out.print(A[i] + " ");
            B[i] = i;
        }
        System.out.println();
        QuickSortFunction(A, B, 0, A.length - 1);
        for(int i = 0; i < 5; i++){
            System.out.print(A[i] + " ");
        }      
  
    }
     
}
