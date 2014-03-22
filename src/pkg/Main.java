package pkg;

// std
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

/**
 * Small test of Java 8.
 *
 * @version 1.0
 * @author  bjarneh@ifi.uio.no
 */

public class Main {


    final static String help =
        "\n jsloc - simple source line counter\n\n"+
        " usage: jsloc <DIR>                  \n\n"+
        " options:                            \n\n"+
        "   -h --help : print this and exit     \n";


    public static void main(String[] args)
        throws IOException, SecurityException
    {

        List<String> argList     = Arrays.asList( args );
        Predicate<String> isHelp = 
            (s) -> s.equals("-h") || s.equals("-help") || s.equals("--help");

        boolean needsHelp = argList.stream().anyMatch( isHelp );

        if( needsHelp || argList.isEmpty() ) {
            System.out.println( help );
            System.exit(0);
        }

        Path path = Paths.get(argList.get(0));

        if( ! Files.isDirectory( path ) ){
            System.err.printf(" [ERROR] directory '%s' not found\n", path);
            System.exit(1);
        }

        List<Path> files = Files.walk( path )
                                .filter( Files::isRegularFile )
                                .collect( Collectors.toList() );

        int total = 0;
        for( Path p: files ) {
            if( Files.probeContentType(p).startsWith("text/") ){
                total += Files.lines(p)
                              .filter( (s) -> !s.matches("^\\s*$") )
                              .count();
            }
        }

        System.out.printf(" sloc: %s\n", total);
    }

}
