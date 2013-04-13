package jap;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

public class javaTerminalResult {
	private String command;
	public javaTerminalResult(String command){
		this.command = command;
	}
	
	public boolean runTerminal(){
		try {
			Process process = Runtime.getRuntime().exec(command);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			while(input.readLine() != null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	public LineNumberReader getTerminalOutput(){
		LineNumberReader input;
		try {
			Process process = Runtime.getRuntime().exec(command);
			InputStreamReader ir = new InputStreamReader(process.getInputStream());
			input = new LineNumberReader(ir);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.err.println("IOException " + e.getMessage());
			return null;
		}
		return input;
	}
	
//	public static void main(String[] args) {
//	try {
//	Process process = Runtime.getRuntime().exec("ping 127.0.0.1");
//	InputStreamReader ir = new InputStreamReader(process
//	.getInputStream());
//	LineNumberReader input = new LineNumberReader(ir);
//	String line;
//	while ((line = input.readLine()) != null)
//	System.out.println(line);
//	} catch (java.io.IOException e) {
//		System.err.println("IOException " + e.getMessage());
//	}
//	}
}