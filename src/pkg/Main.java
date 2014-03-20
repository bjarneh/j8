package pkg;

import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;


public class Main{

    public static void main(String[] args){

        List<String> argList = Arrays.asList( args );
        ArrayList<String> input = new ArrayList<String>();

        argList.forEach( input::add );
        input.forEach( System.out::println );

        int len = input.stream().mapToInt( a -> a.length() ).sum();

        System.out.printf("total size args: %d\n", len);

    }

}
