import java.util.ArrayList;

public class Classifier
{
public static final int spam_d=-1;
public static final int ham_d=1;
public static final int with_sw=1;
public static final int no_sw=0;

Parsing pin=new Parsing();

public final int noOfIters=1000;
public final double eta=0.001;
int w0=1;

public void train()
{
System.out.println("Processing Training data:");
ArrayList<String> vocabulary=new ArrayList<String>();
int sOff=0;

System.out.println("Learning rate:"+eta+" and interation no:"+noOfIters);

for(int i=0;i<Parsing.fileCount;i++)
{

if(i<Parsing.hamFiles.length)
{
vocabulary=pin.createDocVocab(pin.hamFiles[i],no_sw);
weightsUpdate(ham_d,vocabulary);
}
else
{
vocabulary=pin.createDocVocab(Parsing.spamFiles[sOff],no_sw);
weightsUpdate(spam_d,vocabulary);
sOff++;
}
}
}

public void test(int indicator)
{
int spamClassified=0;
int hamClassified=0;

ArrayList<String> vocabulary=new ArrayList<String>();
int sOff=0;
System.out.println("Procession Testing Data:");

for(int i=0;i<Parsing.fileCount;i++)
{
if(i<Parsing.hamFiles.length)
{
vocabulary=pin.createDocVocab(Parsing.hamFiles[i],indicator);
int classifiedAs=classify(ham_d,vocabulary);
if(classifiedAs==1)
hamClassified++;
}
else
{

vocabulary=pin.createDocVocab(Parsing.spamFiles[sOff],indicator);
int classifiedAs=classify(spam_d,vocabulary);
if(classifiedAs==-1)
spamClassified++;
sOff++;
}

}

accuracy(hamClassified,Parsing.hamFiles.length,spamClassified,Parsing.spamFiles.length);

pin.stopWordDictionary();
pin.deleteStopWords();
}

public void accuracy(int hc,int hlen,int sc,int slen)
{
double cc=hc+sc;
double tt=hlen+slen;

double accuracy=cc/tt;
System.out.println("HAm:"+hc+" Total:"+hlen);
System.out.println("SpAm:"+sc+" Total:"+slen);
System.out.println("Accuracy:"+accuracy);
}

public int classify(int type,ArrayList<String> vcb)
{
int cas=((w0+dotProduct(vcb))>0.0 ?1:-1);
return cas;
}

public void weightsUpdate(int c,ArrayList<String> vcb)
{
for(int i=0;i<noOfIters;i++)
{
double current=((w0+dotProduct(vcb))>0.0?1.0:-1.0);

for(int j=0;j<vcb.size();j++)
{
String wrd=vcb.get(j);
double wgt=(eta*(c-current)*Parsing.WordFreq.get(wrd));
if(wgt!=0.0)
{
Parsing.WordFreq.put(wrd,wgt);
}
}
}
}

public double dotProduct(ArrayList<String> vcb)
{
double sum=0.0;

for(int i=0;i<vcb.size();i++)
{
double freq=1;
double wrdWgt=-1;

if((Parsing.Dictionary.containsKey(vcb.get(i)))&&(Parsing.WordFreq.containsKey(vcb.get(i))))
{
freq=Parsing.Dictionary.get(vcb.get(i));
wrdWgt=Parsing.WordFreq.get(vcb.get(i));
}
sum+=(freq*wrdWgt);
}
return sum;
}



}