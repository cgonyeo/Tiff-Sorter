package com.gonyeo.lyricssorter;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class LyricsImage extends JPanel {
	private Image img;
	
	public LyricsImage(Image image) {
		setImage(image);
	}
	
	public void setImage(Image image)
	{
		Dimension size;
		if(image != null)
		{
			int width = image.getWidth(null);
			int height = image.getHeight(null);
			double aspectRatio = width * 1.0 / height;
			if(false)//aspectRatio > 1)
				img = image.getScaledInstance(500, (int) (500 / aspectRatio), Image.SCALE_DEFAULT);
			else
				img = image.getScaledInstance((int) (500 * aspectRatio), 500, Image.SCALE_DEFAULT);
			size = new Dimension(image.getWidth(null), img.getHeight(null));
		}
		else
		{
			size = new Dimension(500, 500);
		}
		setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
	}
	
	public void paintComponent(Graphics g) {
        g.drawImage(img, 0, 0, null);
    }

}
