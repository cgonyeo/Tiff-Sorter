package com.gonyeo.lyricssorter;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class LyricsSorterUI extends JFrame implements ActionListener{
	LyricsSorter ls;
	JFileChooser sourceChooser, outputChooser;
	JLabel sourceLabel, outputLabel;
	JButton sourceButton, outputButton;
	JTextField songNameField;
	JButton backButton, deleteButton, nextButton;
	LyricsImage img;
	JButton finishButton, cancelButton, doneButton;
	int currentImage = 0;
	JPanel songList;
	File outputPath = null;
	
	JDialog doneDialog;
	public LyricsSorterUI(LyricsSorter ls)
	{
		super("Lyrics Sorter");
		
		this.ls = ls;
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setTitle("Lyrics Sorter");
		
		JPanel sideViewContainer = new JPanel(new BorderLayout());
		JPanel fileSelectors = new JPanel(new GridLayout(4, 1, 5, 5));
		
		sourceChooser = new JFileChooser();
		outputChooser = new JFileChooser();
		outputChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		sourceLabel = new JLabel("Source not chosen");
		
		sourceButton = new JButton("Input file");
		sourceButton.addActionListener(this);
		
		outputLabel = new JLabel("Output not chosen");
		
		outputButton = new JButton("Output Folder");
		outputButton.addActionListener(this);
		fileSelectors.add(sourceLabel);
		fileSelectors.add(sourceButton);
		fileSelectors.add(outputLabel);
		fileSelectors.add(outputButton);
		sideViewContainer.add(fileSelectors, BorderLayout.NORTH);
		
		JPanel pictureControlsContainer = new JPanel(new GridLayout(4, 1, 5, 5));
		JLabel reminder = new JLabel("Enter the song title, and press enter");
		pictureControlsContainer.add(reminder);
		songNameField = new JTextField(20);
		songNameField.addActionListener(this);
		pictureControlsContainer.add(songNameField);
		JPanel buttonsContainer = new JPanel(new GridLayout(1, 3, 2, 2));
		backButton = new JButton("Back");
		backButton.addActionListener(this);
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(this);
		nextButton = new JButton("Next");
		nextButton.addActionListener(this);
		buttonsContainer.add(backButton);
		buttonsContainer.add(deleteButton);
		buttonsContainer.add(nextButton);
		pictureControlsContainer.add(buttonsContainer);
		finishButton = new JButton("Done");
		finishButton.addActionListener(this);
		pictureControlsContainer.add(finishButton);
		sideViewContainer.add(pictureControlsContainer, BorderLayout.SOUTH);
		
		add(sideViewContainer, BorderLayout.WEST);
		
		img = new LyricsImage(null);
		add(img, BorderLayout.CENTER);
		
		pack();
		
		doneDialog = new JDialog();
		JPanel doneDialogContainer = new JPanel(new BorderLayout());
		doneDialogContainer.add(new JLabel("Here's the songs you've given me..."), BorderLayout.NORTH);
		songList = new JPanel(new GridLayout(1, 1, 5, 5));
		doneDialogContainer.add(songList, BorderLayout.CENTER);
		JPanel bottomContainer = new JPanel(new GridLayout(4, 1, 5, 5));
		bottomContainer.add(new JLabel("If you click Cancel, you can go back and make changes"));
		bottomContainer.add(new JLabel("If you click I'm Done, I'll write out the files you've specified and exit"));
		cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(this);
		bottomContainer.add(cancelButton);
		doneButton = new JButton("I'm Done");
		doneButton.addActionListener(this);
		bottomContainer.add(doneButton);
		doneDialogContainer.add(bottomContainer, BorderLayout.SOUTH);
		doneDialog.add(doneDialogContainer);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == sourceButton)
		{
			int returnVal = sourceChooser.showOpenDialog(LyricsSorterUI.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = sourceChooser.getSelectedFile();
                sourceLabel.setText(file.getPath());
                ls.setSourceFile(file);
            }
		} else if (e.getSource() == outputButton) {
			int returnVal = outputChooser.showOpenDialog(LyricsSorterUI.this);
			
			if (returnVal == JFileChooser.APPROVE_OPTION) {
                outputPath = outputChooser.getSelectedFile();
                outputLabel.setText(outputPath.getPath());
            }
		} else if (e.getSource() == songNameField) {
			ls.setName(currentImage, songNameField.getText());
		} else if(e.getSource() == nextButton) {
			if(currentImage != ls.getSize() - 1)
			{
				currentImage++;
				setImage(currentImage);
			}
		} else if (e.getSource() == backButton) {
			if(currentImage != 0)
			{
				currentImage--;
				setImage(currentImage);
			}
		} else if (e.getSource() == deleteButton) {
			if(ls.isFileOpen())
			{
				ls.setSkip(currentImage, !ls.getSkip(currentImage));
				if(ls.getSkip(currentImage))
				{
					songNameField.setText("");
					deleteButton.setText("Undelete");
				}
				else
				{
					songNameField.setText(ls.getName(currentImage));
					deleteButton.setText("Delete");
				}
				songNameField.setEnabled(!ls.getSkip(currentImage));
			}
		} else if (e.getSource() == finishButton) {
			if(ls.isFileOpen())
			{
				songList.setLayout(new GridLayout(ls.getSize() / 4 + 1, 4, 15, 15));
				songList.removeAll();
				for(int i = 0; i < ls.getSize(); i++)
				{
					if(!ls.getSkip(i))
						songList.add(new JLabel(ls.getName(i)));
				}
				doneDialog.pack();
				doneDialog.setVisible(true);
			}
		}
		else if (e.getSource() == cancelButton)
		{
			doneDialog.setVisible(false);
		}
		else if (e.getSource() == doneButton)
		{
			if(outputPath != null)
			{
				ls.writeFiles(outputPath);
				System.exit(0);
			}
		}
	}
	
	public void setImage(int i)
	{
		if(i < ls.getSize())
		{
			currentImage = i;
			img.setImage(ls.getImage(i));
			if(ls.getSkip(i))
			{
				songNameField.setText("");
				deleteButton.setText("Undelete");
			}
			else
			{
				songNameField.setText(ls.getName(i));
				deleteButton.setText("Delete");
			}
			songNameField.setEnabled(!ls.getSkip(i));
			pack();
		}
	}
}