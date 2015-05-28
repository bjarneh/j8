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
        "\n jsloc - simple source line counter  \n\n"+
        " usage: jsloc <DIR>                    \n\n"+
        " options:                              \n\n"+
        "   -h --help    : print this and exit    \n"+
        "   -v --verbose : show info for each file\n";


    public static void main(String[] args)
        throws IOException, SecurityException
    {

        List<String> argList     = Arrays.asList( args );

        Predicate<String> isHelp = (s) -> s.matches("-(h|-?help)");
        Predicate<String> isVerbose = (s) -> s.matches("-(v|-?verbose)");
        Predicate<String> nonWhite = (s) -> !s.matches("^\\s*$");

        boolean needsHelp = argList.stream().anyMatch( isHelp );
        boolean verbose   = argList.stream().anyMatch( isVerbose );

        if( needsHelp || argList.isEmpty() ) {
            System.out.println( help );
            System.exit(0);
        }

        // filter out the -verbose argument
        if( verbose ){
            argList = argList.stream()
                             .filter( isVerbose.negate() )
                             .collect( Collectors.toList() );
        }

        Path path = Paths.get(argList.get(0));

        if( ! Files.isDirectory( path ) ){
            System.err.printf(" [ERROR] directory '%s' not found\n", path);
            System.exit(1);
        }

        List<Path> files = Files.walk( path )
                                .filter( Files::isReadable )
                                .filter( Files::isRegularFile )
                                .collect( Collectors.toList() );

        long tmp;
        long total = 0;
        String contentType;

        for( Path p: files ) {

            contentType = Files.probeContentType(p);

            if( contentType != null && contentType.startsWith("text/") ){

                tmp = Files.lines(p)
                           .filter( nonWhite )
                           .count();

                total += tmp;

                if( verbose ){
                    System.out.printf(" %7d: %10s : %s\n", tmp,
                            Files.probeContentType(p), p);
                }

            }
        }

        System.out.printf(" %7d: sloc\n", total);

    }

}
