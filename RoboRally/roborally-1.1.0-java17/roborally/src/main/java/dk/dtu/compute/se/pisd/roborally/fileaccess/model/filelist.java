package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class filelist {

    public static String[] fileNames(String directoryPath) {

        File dir = new File(directoryPath);

        Collection<String> files  =new ArrayList<String>();

        if(dir.isDirectory()){
            File[] listFiles = dir.listFiles();

            for(File file : listFiles){
                if(file.isFile()) {
                    files.add(file.getName());
                }
            }
        }

        return files.toArray(new String[]{});
    }
}
