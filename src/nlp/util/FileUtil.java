package nlp.util;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
	
	static public File[] findFiles(String directory, final String extension){
		 File input = new File(directory);
         File [] children = input.listFiles(new FilenameFilter() {
             public boolean accept(File dir, String name) {
                 return name.endsWith(extension);
             }
         });
         return children;
	}
	
	static public File[] findFiles(String directory, String[] extension){
		 List<File> holder = new ArrayList<File>();
		 for(String ext: extension) 
			 for(File file: findFiles(directory, ext))
				 holder.add(file);
		 return holder.toArray(new File[holder.size()]);
	}

}
