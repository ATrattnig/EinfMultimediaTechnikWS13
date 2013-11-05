package main;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.HashMap;

public class HuffmanCSV {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1)
		{
			System.out.println("Usage: HuffmanCSV filename.csv");
			System.exit(-1);
		}
		
		HashMap<Node, Double> values = readInputFile(args[0]);		
		HashMap<Node, Double> leafs = (HashMap<Node, Double>) values.clone();
		for (Map.Entry<Node, Double> entry : values.entrySet())
		{
			System.out.println("Label: " + entry.getKey().getLabel() + " Prob: " + entry.getValue());
		}
		
		while (values.size() > 1)
		{
			Entry<Node, Double> first = getEntryWithLowestProb(values);
			values.remove(first.getKey());
			Entry<Node, Double> second = getEntryWithLowestProb(values);
			values.remove(second.getKey());
			Node newNode = new Node(false, first.getKey().getLabel() + second.getKey().getLabel(), null, first.getKey(), second.getKey());
			first.getKey().setParent(newNode);
			second.getKey().setParent(newNode);
			values.put(newNode, first.getValue() + second.getValue());
		}
		
		
		double entropy      = 0;
		double averageLength = 0;
		for (Map.Entry<Node, Double> entry : leafs.entrySet())
		{
			Node current = entry.getKey();
			String code = "";
			while (current != null)
			{
				code = code + current.getCode();
				current = current.getParent();
			}
			code = new StringBuffer(code).reverse().toString();
			entry.getKey().setCode(code);
			System.out.println("Label: " + entry.getKey().getLabel() + " Code: " + entry.getKey().getCode());
			
			entropy += Math.log10(1/entry.getValue())/Math.log10(2) * entry.getValue();
			averageLength += entry.getValue() * code.length();
		}
		
		System.out.println("Entropy: " + entropy);
		System.out.println("AverageLength: " + averageLength);
		System.out.println("Redundancy: " + (averageLength-entropy));
		
	}
	
	public static HashMap<Node, Double> readInputFile(String inputfile)
	{
		HashMap<Node, Double> values = new HashMap<Node, Double>();
		try 
		{
			// Open the file that is the first 
			// command line parameter
			FileInputStream fstream = new FileInputStream(inputfile);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				  String[] tmpIn = strLine.split(";");
				  String label = tmpIn[0];
				  double value = Double.parseDouble(tmpIn[1]);
				  values.put(new Node(true, label, null, null, null), value);
			}
			//Close the input stream
			in.close();
		} catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}	
		return values;
	}
	
	
	public static Entry<Node, Double> getEntryWithLowestProb(HashMap<Node, Double> values)
	{
		double lowestProb = 2;
		Entry<Node, Double> smallestEntry = null;
		for (Map.Entry<Node, Double> entry : values.entrySet())
		{
			if (entry.getValue() < lowestProb)
			{
				smallestEntry = entry;
				lowestProb = entry.getValue();
			}
		}
		return smallestEntry;
	}
	
    public static class Node {
        private boolean isLeaf = false;
        private String label = "";
        private String code = "0";
        private Node parent = null;
        private Node leftChild = null;
        private Node rightChild = null;
        
		public Node(boolean isLeaf, String label,
				Node parent, Node leftChild, Node rightChild) {
			super();
			this.isLeaf = isLeaf;
			this.label = label;
			this.parent = parent;
			this.leftChild = leftChild;
			this.rightChild = rightChild;
			if (leftChild != null)
				leftChild.setCode("0");
			if (rightChild != null)
				rightChild.setCode("1");
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public boolean isLeaf() {
			return isLeaf;
		}

		public void setLeaf(boolean isLeaf) {
			this.isLeaf = isLeaf;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public Node getLeftChild() {
			return leftChild;
		}

		public void setLeftChild(Node leftChild) {
			this.leftChild = leftChild;
		}

		public Node getRightChild() {
			return rightChild;
		}

		public void setRightChild(Node rightChild) {
			this.rightChild = rightChild;
		}
        
    }
}
