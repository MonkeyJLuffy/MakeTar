package com.tar;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;

/**
 * The Class TarArchive.
 * Implement the creation of tar archive with Apache commons compress
 */
public class TarArchive {

	/**
	 * Creates a tar of a list files that can be file or directory.
	 *
	 * @param rootFiles the files to put in the tar
	 * @param tarPath the output tar path
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void createTarOfFiles(String[] rootFiles, String tarPath) throws IOException
    {
        FileOutputStream fOut = null;
        BufferedOutputStream bOut = null;
        TarArchiveOutputStream tOut = null;
        
        Arrays.sort(rootFiles);
        
        try
        {
            fOut = new FileOutputStream(new File(tarPath));
            bOut = new BufferedOutputStream(fOut);
            tOut = new TarArchiveOutputStream(bOut);
            
            for (String file : rootFiles) {
                addFileToTar(tOut, file, "");
			}
        }
        finally
        {
            tOut.finish();
            tOut.close();
            bOut.close();
            fOut.close();
        }
    }

    /**
     * Adds the file to tar.
     *
     * @param tOut the t out
     * @param path the path
     * @param base the base
     * @throws IOException Signals that an I/O exception has occurred.
     */
    private static void addFileToTar(TarArchiveOutputStream tOut, String path, String base) throws IOException
    {
        File f = new File(path);
        String entryName = base + f.getName();
        TarArchiveEntry tarEntry = new TarArchiveEntry(f, entryName);

        tOut.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);

        if(f.isFile())
        {
            tOut.putArchiveEntry(tarEntry);
            IOUtils.copy(new FileInputStream(f), tOut);

            tOut.closeArchiveEntry();
        }
        else
        {
//            tOut.closeArchiveEntry();

            File[] children = f.listFiles();
            Arrays.sort(children);
            
            if(children != null)
            {
                for(File child : children)
                {
                    addFileToTar(tOut, child.getAbsolutePath(), entryName + "/");
                }
            }
        }
    }


}
