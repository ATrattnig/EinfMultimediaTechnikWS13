import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



public class DPCMCoder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try 
		{
			FileInputStream fstream = new FileInputStream("sequence.csv");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			ArrayList<Integer> f_original = new ArrayList<Integer>(); 
			while ((strLine = br.readLine()) != null)   {
				  String[] tmpIn = strLine.split(",");
				  for (int i = 0; i < tmpIn.length; i++)
				  {
					  if (i == 0)
					  {
						  f_original.add(Integer.parseInt(tmpIn[i]));
						  f_original.add(Integer.parseInt(tmpIn[i]));
					  }
					  f_original.add(Integer.parseInt(tmpIn[i]));
				  }
			}	
			
			ArrayList<Integer> f_predicted     = new ArrayList<Integer>(); 
			ArrayList<Integer> f_reconstructed = new ArrayList<Integer>(); 
			ArrayList<Integer> f_realError     = new ArrayList<Integer>(); 
			ArrayList<Integer> f_quantError     = new ArrayList<Integer>(); 
			
			f_predicted.add(f_original.get(0));
			f_predicted.add(f_original.get(1));
			f_reconstructed.add(f_original.get(0));
			f_reconstructed.add(f_original.get(1));
			f_realError.add(0);
			f_realError.add(0);
			f_quantError.add(0);
			f_quantError.add(0);
			
			for (int i = 2; i < f_original.size(); i++)
			{
				int original  = f_original.get(i);
				int predicted = (f_reconstructed.get(i-2) + f_reconstructed.get(i-1)) / 2;
				f_predicted.add(predicted);
				int error = original - predicted;
				f_realError.add(error);
				int quantError = 16 * ((255 + error) / 16) - 256 + 8;
				if (i == 2)
					quantError = 0;
				f_quantError.add(quantError);
				int reconstruct = predicted + quantError;
				f_reconstructed.add(reconstruct);			
			}
			System.out.println(" f " + "\t" + "f_p" + "\t" + "e_o" + "\t" + "e_q"  + 
			          "\t" + "f_r");
			for (int i = 2; i < f_original.size(); i++)
			{
				System.out.println(f_original.get(i) + "\t" + f_predicted.get(i) + "\t" + f_realError.get(i) + "\t" + f_quantError.get(i) + 
						          "\t" + f_reconstructed.get(i));
			}
			
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

}
