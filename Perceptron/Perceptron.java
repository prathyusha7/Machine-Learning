import java.io.File;

public class Perceptron
{

public static void main(String args[])
{
String trainpath=args[0];
String testpath=args[1];
String stp=args[2];

File hamF=new File(trainpath+"/ham/");
File spamF=new File(trainpath+"/spam/");
File stopwordFile=new File(stp);

Parsing p=new Parsing(hamF,spamF,stopwordFile);
p.getFileContent();

Classifier c=new Classifier();
c.train();
p=null;
hamF=new File(testpath+"/ham/");
spamF=new File(testpath+"/spam/");

Parsing testp=new Parsing(hamF,spamF,stopwordFile);
c.test(0);

System.out.println("Accuracy by removing the stop words:");
c.test(1);
}


}