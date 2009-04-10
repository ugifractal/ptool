package ptools;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class LibraryLoader {
	
	
	public void loadFile(File f){
        if (!f.exists()) {
            System.out.println("File not found");
            return;
        }
        if (f.isDirectory()) {
        	File[] files = f.listFiles();
            for (int i=0;i<files.length;i++){
               loadFile(files[i]);
            }
        } else {
        	try{
        		if(f.getName().endsWith(".jar")){
        			System.out.println("loading.." + f.getAbsolutePath());
        			URLClassLoader obj =(URLClassLoader)getClass().getClassLoader().getSystemClassLoader();
        			Method m = URLClassLoader.class.getDeclaredMethod("addURL",URL.class);
        			m.setAccessible(true);
        			m.invoke(obj,f.toURL());
        			System.out.println("load library :"+f.getName());

        		}
        	}catch (Exception e){
        		e.printStackTrace();
        	}
        }
    }

}
