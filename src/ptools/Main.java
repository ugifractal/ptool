package ptools;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import com.ugi.script.RubyExecutor;



import eszlibrary.gui.FrameRef;
import eszlibrary.gui.MainFrame;
import javax.swing.JMenuItem;
import java.awt.event.*;
import java.awt.*;
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LibraryLoader loader = new LibraryLoader();
		loader.loadFile(new File("libs"));
		eszlibrary.Main.main(null);
		MainFrame mf = FrameRef.getMainFrame();
		Terminal term = new Terminal();
		//term.connect("/dev/tty.HUAWEIMobile-Modem",19200,8,1,0);
		try{
			RubyExecutor.getInstance().declareBean("frame",
			 	FrameRef.getMainFrame(), 
				FrameRef.getMainFrame().getClass());
			RubyExecutor.getInstance().declareBean("term",
			 	term,
				term.getClass());
			
		}catch(Exception e){
			System.out.println(e);
		}
				
		JMenuItem mReload = new JMenuItem("Reload");
		Starter.start();
		
		
		
		mReload.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent evt){
				File f = new File("scripts/initializer.rb");
				try{
					FileReader fr = new FileReader(f);
					BufferedReader br = new BufferedReader(fr);
					String script = "";
					String line = null;
					while((line = br.readLine()) != null) {
						script += line + "\n";
					}
					RubyExecutor.getInstance().eval(script);
				}catch(Exception e) {
					System.out.println(e);
				}
			}
		});
		mf.getJMenuBar().add(mReload);
		mf.setVisible(true);
	}
	

}
