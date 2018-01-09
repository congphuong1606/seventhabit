package com.discovertodo.phone.android.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.domain.Resources;
import nl.siegmann.epublib.domain.Spine;
import nl.siegmann.epublib.domain.SpineReference;
import nl.siegmann.epublib.epub.EpubReader;
import nl.siegmann.epublib.service.MediatypeService;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

public class ReadEpubUtil {
	
	private final static String fileName = "ebook6.epub";
	
	@SuppressWarnings("deprecation")
	public static String getEpub(Context context){
    	String result = "";
	    try {
	    	AssetManager assetManager = context.getAssets();
	        String basePath = Environment.getExternalStorageDirectory() + "/SevenHabitsBooks/";
	        deleteRecursive(new File(basePath));
	        InputStream epubInputStream = assetManager.open(fileName, Context.MODE_WORLD_READABLE);
	        Book book = (new EpubReader()).readEpub(epubInputStream);
	        downloadResource(book, basePath);
	        String line;
	        Spine spine = book.getSpine(); 
	        List<SpineReference> spineList = spine.getSpineReferences() ;
	        int count = spineList.size();
	        StringBuilder string = new StringBuilder();
	        for (int i = 0; i < count; i++) {
	            Resource res = spine.getResource(i);
	            try {
	                InputStream is = res.getInputStream();
	                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	                try {
	                    while ((line = reader.readLine()) != null) {
	                        result =   string.append(line + "\n").toString();
	                    }
	                } catch (IOException e) {e.printStackTrace();}
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
//	        result = result.replace("../", "");
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    copyFont(context);
		return result;
    }
	
	private static void deleteRecursive(File fileOrDirectory) {
	    if (fileOrDirectory.isDirectory())
	        for (File child : fileOrDirectory.listFiles())
	            deleteRecursive(child);

	    fileOrDirectory.delete();
	}
	
	private static void downloadResource(Book book, String directory) {
	    try {
	        Resources rss = book.getResources();
	        Collection<Resource> clrst = rss.getAll();
	        Iterator<Resource> itr = clrst.iterator();
	        while (itr.hasNext()) {
	            Resource rs = itr.next();
	            if ((rs.getMediaType() == MediatypeService.JPG)
	            		|| (rs.getMediaType() == MediatypeService.PNG)
	            		|| (rs.getMediaType() == MediatypeService.GIF)) {
	                
	                File oppath1 = new File(directory, rs.getHref().replace("OEBPS/", "")); 
	                if (!oppath1.exists()){
	                	oppath1.getParentFile().mkdirs();
		                oppath1.createNewFile();	
		                FileOutputStream fos1 = new FileOutputStream(oppath1);
		                fos1.write(rs.getData());
		                fos1.close();
	                }else{
	                	
	                }
	            } else if (rs.getMediaType() == MediatypeService.CSS) {
	            	
	                File oppath = new File(directory, rs.getHref());
	                if(!oppath.exists()){
	                	oppath.getParentFile().mkdirs();
		                oppath.createNewFile();
		                FileOutputStream fos = new FileOutputStream(oppath);
		                fos.write(rs.getData());
		                fos.close();
	                }else{
	                	
	                }
	            }
	        }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
	
	private static void copyFont(Context context) {
	    AssetManager assetManager = context.getAssets();
        InputStream in = null;
        OutputStream out = null;
		try {
			in = assetManager.open("RockElegance.otf");
			File outFile = new File(Environment.getExternalStorageDirectory()
					+ "/SevenHabitsBooks/", "RockElegance.otf");
			out = new FileOutputStream(outFile);
			copyFile(in, out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// NOOP
				}
			}
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// NOOP
				}
			}
		}
	}
	
	private static void copyFile(InputStream in, OutputStream out) throws IOException {
		byte[] buffer = new byte[1024];
		int read;
		while ((read = in.read(buffer)) != -1) {
			out.write(buffer, 0, read);
		}
	}
}
