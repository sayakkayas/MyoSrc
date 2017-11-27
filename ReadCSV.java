import java.io.*;
import java.lang.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.sqrt;
import static java.lang.Math.abs;



class ReadCSV
{

     static int[][] model=new int[500][8];
     static int model_index=0;
      static int[][] series=new int[500][8];
      static int series_index=0;
          
        static double max0=-2;
         static int win0=0;
          static double max1=-2;
         static int win1=0;
          static double max2=-2;
         static int win2=0;
          static double max3=-2;
         static int win3=0;
          static double max4=-2;
         static int win4=0;
          static double max5=-2;
         static int win5=0;
          static double max6=-2;
         static int win6=0;
          static double max7=-2;
         static int win7=0;

     
          static double maxC0=-2;
         static int winC0=0;
          static double maxC1=-2;
         static int winC1=0;
          static double maxC2=-2;
         static int winC2=0;
          static double maxC3=-2;
         static int winC3=0;
          static double maxC4=-2;
         static int winC4=0;
          static double maxC5=-2;
         static int winC5=0;
          static double maxC6=-2;
         static int winC6=0;
          static double maxC7=-2;
         static int winC7=0;
 //New
   static int [] agg_model=new int[500];
   static int agg_model_index=0;
   static int [] agg_series=new int [500];
   static int agg_series_index=0;
   static double max_agg=-2;
         static int win_agg=0;
          static double maxCAgg=-2;
         static int winCAgg=0;
        

   /*Newly added*/

   public static void aggregate()
   {
      for(int i=0;i<model_index;i++)
      {
          int sum=0;
          for(int j=0;j<8;j++)
            sum=sum+model[i][j];

        agg_model[agg_model_index]=sum;
        agg_model_index++;
      }


     for(int i=0;i<series_index;i++)
      {
          int sum=0;
          for(int j=0;j<8;j++)
            sum=sum+series[i][j];

        agg_series[agg_series_index]=sum;
        agg_series_index++;
      }



    for(int i=20;i<agg_model_index;i++)
         {//System.out.println("Window size "+ i);
          compute_correlation2(i);
         }  

      
          


   }



  public static double corr2(int start,int windowSize)
    {
        double numerator1=0;
        double numerator2=0;
        double numerator3=0;
        
        double numerator=0;
        double denominator1=0;
        double denominator2=0;
        double denominator=0;

        for(int i=0;i<windowSize;i++)
        {
          numerator1=numerator1+(agg_model[i]*agg_series[i+start]);
          numerator2=numerator2+(agg_model[i]);
          numerator3=numerator3+(agg_series[i+start]);

          denominator1=denominator1+(agg_model[i]*agg_model[i]);
          denominator2=denominator2+(agg_series[i+start]*agg_series[i+start]);
          
        }


          //numerator1=SUM(XY)
        //numerator2=SUM(X)
        //numerator3=SUM(Y)

        //denominator1=SUM(X2)
        //denominator2=SUM(Y2)

        numerator=(windowSize*numerator1)-(numerator2*numerator3);

        denominator1=(windowSize*denominator1)-(numerator2*numerator2);
        denominator2=(windowSize*denominator2)-(numerator3*numerator3);

        denominator=denominator1*denominator2;
        
        double root = Math.sqrt(denominator);
      // System.out.println(numerator+"/"+denominator);
      double res=0.0;
            if(root!=0.0)
        res=(double)numerator/root;

        //System.out.println(res);

          return res;


   }

  public static int matchPoints2(int start,int windowSize)
    {
    

      int counter=0;
       for(int i=0;i<windowSize;i++)
        {
          if(agg_model[i]==agg_series[i+start])
            counter++;
          
        }


      return counter;

   }


  public static void compute_correlation2(int windowSize)
  {

         for(int i=0;i<(agg_series_index-windowSize);i++)
          {
             double res_agg=corr2(i,windowSize);

            if(res_agg>max_agg)
            {max_agg=res_agg;
             win_agg=windowSize;
            } 
      



            int c_agg=matchPoints2(i,windowSize);
        
            
             if(c_agg>maxCAgg)
            {maxCAgg=c_agg;
             winCAgg=windowSize;
            } 
      
           

         }  
          
  }



//----------------------------
    public static void main(String[] args)
    {

    	//System.out.println(args[0]);

    	readCSV(args[0],1);
    	readCSV(args[1],2);
        //displayMat();
        //windowSize<model_index
        for(int i=20;i<model_index;i++)
         {//System.out.println("Window size "+ i);
         	compute_correlation(i);
         }	

          System.out.println("Sensor 0: max corr =" +max0 +" windowSize="+win0+" match points :"+maxC0 +" w:"+winC0);
          System.out.println("Sensor 1: max corr =" +max1 +" windowSize="+win1+" match points :"+maxC1 +" w:"+winC1);
          System.out.println("Sensor 2: max corr =" +max2 +" windowSize="+win2+" match points :"+maxC2 +" w:"+winC2);
          System.out.println("Sensor 3: max corr =" +max3 +" windowSize="+win3+" match points :"+maxC3 +" w:"+winC3);
          System.out.println("Sensor 4: max corr =" +max4 +" windowSize="+win4+" match points :"+maxC4 +" w:"+winC4);
          System.out.println("Sensor 5: max corr =" +max5 +" windowSize="+win5+" match points :"+maxC5 +" w:"+winC5);
          System.out.println("Sensor 6: max corr =" +max6 +" windowSize="+win6+" match points :"+maxC6 +" w:"+winC6);
          System.out.println("Sensor 7: max corr =" +max7 +" windowSize="+win7+" match points :"+maxC7 +" w:"+winC7);
          
    
    
    
       aggregate();

        System.out.println("Sensor Agg: max corr =" +max_agg +" windowSize="+win_agg+" match points :"+maxCAgg +" w:"+winCAgg);
              


        System.out.println(model_index+" "+series_index);


    }

/*   public static double corr(int start,int windowSize,int col)
   {
        int numerator=0;
        int denominator1=0;
        int denominator2=0;
        int denominator=0;

        for(int i=0;i<windowSize;i++)
        {
        	numerator=numerator+(model[i][col]*series[i+start][col]);
        	denominator1=denominator1+(model[i][col]*model[i][col]);
        	denominator2=denominator2+(series[i+start][col]*series[i+start][col]);
        	
        }
        denominator=denominator1*denominator2;
        double root = Math.sqrt(denominator);


        double res=(double)numerator/root;

        //System.out.println(res);

          return res;


   }
   */

    public static int matchPoints(int start,int windowSize,int col)
    {
    

      int counter=0;
       for(int i=0;i<windowSize;i++)
        {
        	if(model[i][col]==series[i+start][col])
        		counter++;
        	
        }


      return counter;

   }



      public static double corr(int start,int windowSize,int col)
    {
        double numerator1=0;
        double numerator2=0;
        double numerator3=0;
        
        double numerator=0;
        double denominator1=0;
        double denominator2=0;
        double denominator=0;

        for(int i=0;i<windowSize;i++)
        {
        	numerator1=numerator1+(model[i][col]*series[i+start][col]);
        	numerator2=numerator2+(model[i][col]);
        	numerator3=numerator3+(series[i+start][col]);

        	denominator1=denominator1+(model[i][col]*model[i][col]);
        	denominator2=denominator2+(series[i+start][col]*series[i+start][col]);
        	
        }


          //numerator1=SUM(XY)
        //numerator2=SUM(X)
        //numerator3=SUM(Y)

        //denominator1=SUM(X2)
        //denominator2=SUM(Y2)

        numerator=(windowSize*numerator1)-(numerator2*numerator3);

        denominator1=(windowSize*denominator1)-(numerator2*numerator2);
        denominator2=(windowSize*denominator2)-(numerator3*numerator3);

        denominator=denominator1*denominator2;
        
        double root = Math.sqrt(denominator);
      // System.out.println(numerator+"/"+denominator);
      double res=0.0;
            if(root!=0.0)
        res=(double)numerator/root;

        //System.out.println(res);

          return res;


   }



    public static void compute_correlation(int windowSize)
    {

      	for(int i=0;i<(series_index-windowSize);i++)
    	{
    		double res0=corr(i,windowSize,0);
    	    double res1=corr(i,windowSize,1);
    	    double res2=corr(i,windowSize,2);
    	    double res3=corr(i,windowSize,3);
    	    double res4=corr(i,windowSize,4);
    	    double res5=corr(i,windowSize,5);
    	    double res6=corr(i,windowSize,6);
    	    double res7=corr(i,windowSize,7);
    	  


    	     if(res0>max0)
    	     	{max0=res0;
    	     	 win0=windowSize;
    	     	}	
    	
    	      if(res1>max1)
    	     	{max1=res1;
    	     	 win1=windowSize;
    	     	}	
    	
    	      if(res2>max2)
    	     	{max2=res2;
    	     	 win2=windowSize;
    	     	}	
              
               if(res3>max3)
    	     	{max3=res3;
    	     	 win3=windowSize;
    	     	}	


                if(res4>max4)
    	     	{max4=res4;
    	     	 win4=windowSize;
    	     	}	
    	
    	      if(res5>max5)
    	     	{max5=res5;
    	     	 win5=windowSize;
    	     	}	
    	
    	      if(res6>max6)
    	     	{max6=res6;
    	     	 win6=windowSize;
    	     	}	
               
               if(res7>max7)
                {max7=res7;
                 win7=windowSize;
                }   


    	

    	  int c0=matchPoints(i,windowSize,0);
    	  int c1=matchPoints(i,windowSize,1);
    	  int c2=matchPoints(i,windowSize,2);
    	  int c3=matchPoints(i,windowSize,3);
    	  int c4=matchPoints(i,windowSize,4);
    	  int c5=matchPoints(i,windowSize,5);
    	  int c6=matchPoints(i,windowSize,6);
    	  int c7=matchPoints(i,windowSize,7);
    	  
            
             if(c0>maxC0)
    	     	{maxC0=c0;
    	     	 winC0=windowSize;
    	     	}	
    	
    	      if(c1>maxC1)
    	     	{maxC1=c1;
    	     	 winC1=windowSize;
    	     	}	
    	
    	      if(c2>maxC2)
    	     	{maxC2=c2;
    	     	 winC2=windowSize;
    	     	}	
              
               if(c3>maxC3)
    	     	{maxC3=c3;
    	     	 winC3=windowSize;
    	     	}	


                if(c4>maxC4)
    	     	{maxC4=c4;
    	     	 winC4=windowSize;
    	     	}	
    	
    	      if(c5>maxC5)
    	     	{maxC5=c5;
    	     	 winC5=windowSize;
    	     	}	
    	
    	      if(c6>maxC6)
    	     	{maxC6=c6;
    	     	 winC6=windowSize;
    	     	}	
               
               if(c7>maxC7)
                {maxC7=c7;
                 winC7=windowSize;
                }   

    	  
    	   


    	}

    }

    public static void displayMat()
    {
    	for(int i=0;i<model_index;i++)
    	{
    		for(int j=0;j<8;j++)
    		{
    			System.out.print(model[i][j]+" ");
    		}
    		 System.out.println();

    	}

         System.out.println();
         
    	for(int i=0;i<series_index;i++)
    	{
    		for(int j=0;j<8;j++)
    		{
    			System.out.print(series[i][j]+" ");
    		}
    		 System.out.println();

    	}

    } 

    public static void readCSV(String filename,int flag)
    {

    	Path pathToFile = Paths.get(filename);

       try (BufferedReader br = Files.newBufferedReader(pathToFile,StandardCharsets.US_ASCII)) 
       {

            // read the first line from the text file
            String line = br.readLine();

            // loop until all lines are read
            while (line != null) 
            {
               // use string.split to load a string array with the values from
                // each line of
                // the file, using a comma as the delimiter
                String[] attributes = line.split(",");

                    if(flag==1)
                    {
                     for(int i=0;i<attributes.length;i++)
                  	  {// System.out.print(attributes[i]+",");
                          model[model_index][i]=Integer.parseInt(attributes[i]);
                       }

                       model_index++;
                    }
                //  System.out.println();

                    if(flag==2)
                    {
                     for(int i=0;i<attributes.length;i++)
                  	  {// System.out.print(attributes[i]+",");
                          series[series_index][i]=Integer.parseInt(attributes[i]);
                       }

                       series_index++;
                    }

               
                line = br.readLine();
 
            }//while close
       }//try close                   
        catch(IOException ioe)
        {
        	ioe.printStackTrace();
        }

     

    }


}