import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream.GetField;
import java.util.Random;

import javax.imageio.ImageIO;


public class KMeans {
	public static void main(String [] args){
		if (args.length < 3){
			System.out.println("Usage: Kmeans <input-image> <k> <output-image>");
			return;
		}
		try{
			BufferedImage originalImage = ImageIO.read(new File(args[0]));
			int k=Integer.parseInt(args[1]);
			BufferedImage kmeansJpg = kmeans_helper(originalImage,k);
			ImageIO.write(kmeansJpg, "jpg", new File(args[2])); 

		}catch(IOException e){
			System.out.println(e.getMessage());
		}	
	}

	private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
		int w=originalImage.getWidth();
		int h=originalImage.getHeight();
		BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
		Graphics2D g = kmeansImage.createGraphics();
		g.drawImage(originalImage, 0, 0, w,h , null);
		// Read rgb values from the image
		int[] rgb=new int[w*h];
		int count=0;
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				rgb[count++]=kmeansImage.getRGB(i,j);
			}
		}
		// Call kmeans algorithm: update the rgb values
		kmeans(rgb,k);

		// Write the new rgb values to the image
		count=0;
		for(int i=0;i<w;i++){
			for(int j=0;j<h;j++){
				kmeansImage.setRGB(i,j,rgb[count++]);
			}
		}
		return kmeansImage;
	}

	// Your k-means code goes here
	// Update the array rgb by assigning each entry in the rgb array to its cluster center
private static void kmeans(int[] rgb, int k)
{
Random r=new Random();
int[] oldmeans=new int[k];
int[] newmeans=new int[k];
int[] clustercount=new int[k];

for(int i=0;i<k;i++)
{
oldmeans[i]=r.nextInt(rgb.length);
newmeans[i]=0;
clustercount[i]=0;
}
double[] distance=new double[k];
int[] clusterassigned=new int[rgb.length];
int count=0;
int hit=0;
do
{
count++;
for(int i=0;i<rgb.length;i++)
{
Color a =new Color(rgb[i]);
for(int j=0;j<k;j++)
{
Color b=new Color(oldmeans[j]);
distance[j]=calDistance(a,b);
}

double minDis=distance[0];
clusterassigned[i]=0;
for(int j=0;j<k;j++)
{
double temp=distance[j];
if(temp<minDis)
{
minDis=temp;
clusterassigned[i]=j;
}
}
int clusterno=clusterassigned[i];
newmeans[clusterno]=newmeans[clusterno]+rgb[i];
clustercount[clusterno]=clustercount[clusterno]+1;
}
for(int j=0;j<k;j++)
{
int mean=0;
if(clustercount[j]!=0)

{
mean=newmeans[j]/clustercount[j];
if(mean==oldmeans[j])
hit++;
else
oldmeans[j]=mean;
}
}
if(hit==k)
break;
}

while((count<100));
for(int i=0;i<rgb.length;i++)
{
rgb[i]=oldmeans[clusterassigned[i]];

}
		
}
public static boolean checkConverged(int[] oldmeans,int[] newmeans)
{
int n=newmeans.length;
for(int i=0;i<n;i++)
{
if(oldmeans[i]!=newmeans[i])
return false;
}
return true;

}

public static double calDistance(Color a, Color b)
{
double red=Math.abs(a.getRed()-b.getRed());
double green=Math.abs(a.getGreen()-b.getGreen());
double blue=Math.abs(a.getBlue()-b.getBlue());
double dis=Math.sqrt((red*red)+(green*green)+(blue*blue));
return dis;
}


}