import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Parsing
{
public static HashMap<String,Integer> Dictionary=new HashMap<String, Integer>();

public static HashMap<String,Double> WordFreq=new HashMap<String, Double>();

public static ArrayList<String> DistinctWords=new ArrayList<String>();

public static ArrayList<String> StopWords=new ArrayList<String>();

public static File[] hamFiles=null;
public static File[] spamFiles=null;
public static File stopFl=null;
public static int fileCount=0;

Parsing(){}
Parsing(File hams,File spams,File stop)
{

this.hamFiles=hams.listFiles();
this.spamFiles=spams.listFiles();
fileCount=getFilesCount(hamFiles,spamFiles);
this.stopFl=stop;
}

public int getFilesCount(File[] ham,File[] spam)
{
int count;
count=ham.length+spam.length;
return count;
}
public void getFileContent()
{
int a=0;
for(int i=0;i<fileCount;i++)
{
if(i<hamFiles.length)
createVocab(hamFiles[i]);
else
{
createVocab(spamFiles[a]);
a++;
}
}
for(String key:Dictionary.keySet())
{WordFreq.put(key,0.5);
}
}

public StringTokenizer readData(String data)
{
StringTokenizer st=null;
st=new StringTokenizer(data," ");
return st;
}

public void createVocab(File fname)
{
try
{
BufferedReader br=new BufferedReader(new FileReader(fname));

String line=null;
while((line=br.readLine())!=null)
{
StringTokenizer tokens=readData(line.toLowerCase());

while(tokens.hasMoreTokens())
{
String word=tokens.nextToken();
if(Dictionary.containsKey(word))
{
int k=Dictionary.get(word);
Dictionary.put(word,k+1);
}

else{Dictionary.put(word,1);}
}
}
}catch(Exception e){
e.getMessage();
}
}

public void stopWordDictionary()
{
StopWords.clear();
try
{
BufferedReader br=new BufferedReader(new FileReader(stopFl));

String line=null;
while((line=br.readLine())!=null)
{
StringTokenizer tokens=readData(line.toLowerCase());

while(tokens.hasMoreTokens())
{
String word=tokens.nextToken();

if(!StopWords.contains(word))
{
StopWords.add(word);
}
}

}

}catch(Exception e)
{
e.getMessage();
}
}

public void deleteStopWords()
{
for(int i=0;i<StopWords.size();i++)
{
if(WordFreq.containsKey(StopWords.get(i)))
{
WordFreq.remove(StopWords.get(i));
}
}
}


public ArrayList<String> createDocVocab(File fname, int indicator)
{
DistinctWords.clear();

try
{
BufferedReader br=new BufferedReader(new FileReader(fname));

String line=null;
while((line=br.readLine())!=null)
{
StringTokenizer tokens=readData(line.toLowerCase());

while(tokens.hasMoreTokens())
{
String word=tokens.nextToken();

if(!DistinctWords.contains(word)&&(0==indicator))
{
DistinctWords.add(word);
}

if(1==indicator)
{
if(!DistinctWords.contains(word)&&(!StopWords.contains(word)))
{
DistinctWords.add(word);
}
}

}

}

}catch(Exception e)
{
e.getMessage();
}


return DistinctWords;
}



}