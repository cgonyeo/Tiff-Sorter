package com.gonyeo.lyricssorter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import javax.media.jai.PlanarImage;

import com.sun.media.jai.codec.ImageCodec;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.FileSeekableStream;

public class LyricsSorter {
	ArrayList<BufferedImage> images;
	ArrayList<Boolean> skips;
	ArrayList<String> names;
	LyricsSorterUI ui;
	boolean fileOpen = false;
	public static void main(String[] args)
	{		
		new LyricsSorter();
	}
	
	public LyricsSorter()
	{
		images = new ArrayList<BufferedImage>();
		skips = new ArrayList<Boolean>();
		names = new ArrayList<String>();
		ui = new LyricsSorterUI(this);
        ui.setVisible(true);
	}
	
	public void setSourceFile(File inputLyricsFile)
	{
		try {
			SeekableStream ss = new FileSeekableStream(inputLyricsFile);
			ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", ss, null);
			
			int numPages = decoder.getNumPages();
            for(int i = 0; i < numPages; i++)
            {
                PlanarImage op = new NullOpImage(decoder.decodeAsRenderedImage(i), null, null, OpImage.OP_IO_BOUND);
                images.add(op.getAsBufferedImage());
                skips.add(false);
                names.add("" + (i+1));
            }
            fileOpen = true;
            ui.setImage(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void writeFiles(File path)
	{
    	try {
    		for(int i = 0; i < images.size(); i++)
        	{
    			if(!skips.get(i))
    			{

            		File outputFile = new File(path.getPath() + "/" + names.get(i) + ".png");
    				ImageIO.write(images.get(i), "png", outputFile);
    			}
        	}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Image getImage(int i) 
	{
		return images.get(i);
	}
	public boolean getSkip(int i)
	{
		return skips.get(i);
	}
	public String getName(int i)
	{
		return names.get(i);
	}
	public void setSkip(int i, boolean skip)
	{
		skips.set(i, skip);
	}
	public void setName(int i, String name)
	{
		names.set(i, name);
	}
	
	public int getSize()
	{
		return images.size();
	}
	public boolean isFileOpen()
	{
		return fileOpen;
	}
}
