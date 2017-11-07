public class Test {

  public static void main(String[] args) {


    System.out.println("System.out.println( \" Thread: \"+ Thread.currentThread().getName() + \"\t\" +  \"" + "m1"+ "." + "gc" + ":\t\"+ (System.currentTimeMillis()-stime) +\" ms\" );)");

    System.out.println( " Thread: "+ Thread.currentThread().getName() + "	" +  "m1.gc:	"+ (System.currentTimeMillis()-1) +" ms" );


  }

}
