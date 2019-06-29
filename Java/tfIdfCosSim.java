
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;

	public class tfIdfCosSim{
	    
	    public double tf(List<String> doc, String term) {
	        double result = 0;
	        for (String word : doc) {
	            if (term.equalsIgnoreCase(word))
	                result++;
	        }
	        return result / doc.size();
	    }

	    
	    public double idf(List<List<String>> docs, String term) {
	        double n = 0;
	        for (List<String> doc : docs) {
	            for (String word : doc) {
	                if (term.equalsIgnoreCase(word)) {
	                    n++;
	                    break;
	                }
	            }
	        }
	        return Math.log(docs.size() / n);
	    }

	    
	    public double tfIdf(List<String> doc, List<List<String>> docs, String term) {
	        return tf(doc, term) * idf(docs, term);

	    }
	    
	    

	    public static void main(String[] args) {

	      
	        String[] dictionary = null;
	        int dictionarySize = 50;
	        double threshold=0.5;
			boolean isStem=true;
			boolean remove=true;
			boolean stem=true;
			int element=0;
			String line="";
			List<String> userStory=new ArrayList<>();
	        dictionary = new String[200];
	        String tokenizedUS[][]=new String[dictionarySize][1000];
	        List<String> keywords=new ArrayList<>();
	        List<List<String>> listOfLists = new ArrayList<>(dictionarySize);
	        List<List<Double>> tfidfList = new ArrayList<>();
	        Scanner in = null; 
	        String resource="workgroup";
//	        String resource="metadata";
	    
	        boolean inn;
	        try {
	  	      in = new Scanner(new File(args[0]));
	  	    } catch (FileNotFoundException e){
	  	      System.err.println("not found");
	  	      System.exit(1);
	  	    }
	        
	        String temp="";
	        
    			//Removing Extra Info Before US1
    			temp=in.nextLine();
    			while(!temp.contains("US1"))
    				temp=in.nextLine();	
    			
		    for (int i = 0; i < dictionarySize; i++){
		    	line="";
		    		if(temp.contains("US1:"))
		    			temp= temp.substring(temp.indexOf("US1"));
		    		
		    		int usNum=i+2;
		    		while(!temp.contains("US"+usNum)) {
		    			
		    			line+=temp+" ";
		    			temp=in.nextLine();
		    			if(in.hasNextLine()==false) {
			    			break;
		    			}
		    		}
		    		element=0;
		    		
		    		Tokenizer tokenizer = new Tokenizer();
		    		List<String> st = tokenizer.token(line,isStem,remove);
		    		
		    		
		    		for(int key=0; key<st.size();key++) {
		    		
		    			if(st.get(key).contains(":"))
		    				
		    				continue;
		    			else
		    				tokenizedUS[i][element]=st.get(key).toString();
		    			
		    			element++;
		    			
		   		}
		  
		    		
		    		
		    		listOfLists.add(new ArrayList(element));
		    		for(int k=0;k<element;k++) {
		    			//System.out.println(i+"   "+tokenizedUS[i][k]);
		    	
		    			//Erasing commas after each words
		    			if(tokenizedUS[i][k].contains(","))             
		    				tokenizedUS[i][k]=tokenizedUS[i][k].substring(0, tokenizedUS[i][k].indexOf(","));
		    		
		    			listOfLists.get(i).add(tokenizedUS[i][k]);
		    			
		    			//finding words that are not repeated for the purpose of attributes
		    			if(!keywords.contains(tokenizedUS[i][k]))            
		    				keywords.add(tokenizedUS[i][k]);
		     
		    		}
		    		
		    		if(in.hasNextLine())
		    			temp=in.nextLine();
		    		else
		    			
		    		//if(in.hasNextLine()==false) 
		    			break;
		    }
		    
		    
		    //--------------------------Filling final matrix with tf-idf values-------------------
	        TfIdf calculator = new TfIdf();
	        //String searchKeyword="workgroup";
	        double[][] tfidf=new double[listOfLists.size()][keywords.size()];
	        
	       

	        int z=0;
	     
	        List<String> uniqueList=new ArrayList<>();
	        	for(int k=0;k<listOfLists.size();k++) {
	        		int m=k+1;

	        		for(z=0;z<keywords.size(); z++) {
	        			
	        				tfidf[k][z] = calculator.tfIdf(listOfLists.get(k), listOfLists, keywords.get(z));
	        				tfidfList.add(new ArrayList(keywords.size()));
	        				tfidfList.get(k).add(tfidf[k][z]);
	        				
	        		}
	       		//System.out.println();
	        		
	        	}
	        //	System.out.println();
	        	
	        	
	        	
	        	//----------------------------returning US stories involved with a resource-----------------
	        	int wgIndex=0;
	        	for(z=0;z<keywords.size(); z++) {
	        		if(keywords.get(z).equals(resource)) {
	        			wgIndex=z;
	        			break;
	        		}
	        	}
	        	double[] resourceArray=new double[dictionarySize];
	        	List<Integer> list = new ArrayList<Integer>();
	        	int[] UsIndex=new int[dictionarySize];
	        	int tmpIdx=0;
	        	for(int k=0;k<listOfLists.size();k++) {
	        		if(tfidf[k][wgIndex]>0.0) {
	        			resourceArray[tmpIdx]=tfidf[k][wgIndex];
        				UsIndex[tmpIdx]=k+1;
        				list.add(k+1);
        				tmpIdx+=1;
	        		}
	        	}
	        	String St1="";
	        	String St2="";
	        	for(int k=0;k<tmpIdx;k++) {
	        		St1+=resourceArray[k]+",";
	        		
	        	}
	        St1=St1.substring(0, St1.length()-1);
//	        	System.out.println(St1);
	       
	        	for(int k=0;k<tmpIdx;k++) {
	        		St2+=UsIndex[k]+",";
	        		
	        	}
	        	St2=St2.substring(0, St2.length()-1);
//	        	System.out.println(St2);
	        	
	        	
	        	//--------------------------------Calculating Cosine Similarity----------------------------------
	        	
	        	
	        	double[][] cosMatrix=new double[listOfLists.size()][listOfLists.size()];
	        	CosineSimilarity cosSim=new CosineSimilarity();
	        double x= 0.0;

	        	for(int a=0; a<listOfLists.size(); a++) {
	        		int tmp=a+1;

	        		
	        		for(int b=a+1; b<listOfLists.size(); b++) {
	        			//System.out.println(tfidfList.get(a));
	        			ArrayList xx=new ArrayList<>(tfidfList.get(a));
	        			ArrayList yy=new ArrayList<>(tfidfList.get(b));
	        			

	        			cosMatrix[a][b]=cosSim.cosineSimilarity(tfidfList.get(a),tfidfList.get(b));
//	        			System.out.print(cosMatrix[a][b]);
	        			
	        		}
//	        		System.out.println();	
	        	}
	        	
	   //---------------------------User stories Cosine Similarities for Us involved with specified resource----------------     	
	     String s3="";
	     for(int y:list) {
	    	 s3="";
	    	 	for(int p:list) {
	    	 		s3+=cosMatrix[y-1][p-1]+",";
	    	 		
	    	 	}
	    	 	s3=s3.substring(0,s3.length()-1);
	    	 	System.out.println(s3);

	     }
	    	 
	        	
	     
	 
	        			
	        }

	}
