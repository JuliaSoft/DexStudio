package com.juliasoft.dexstudio.menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import com.juliasoft.amalia.apk.ApkReader;
import com.juliasoft.amalia.dex.codegen.CodegenException;
import com.juliasoft.amalia.dex.codegen.DexGen;
import com.juliasoft.amalia.dex.file.DexFile;
import com.juliasoft.amalia.dex.file.DexReader;
import com.juliasoft.dexstudio.DexFrame;
import com.juliasoft.dexstudio.utils.DexProgress;

/**
 * Waiting dialog for the opening function
 * @author Zanoncello Matteo
 *
 */
@SuppressWarnings("serial")
public class DexOpenApk extends JDialog
{
	private File apk;
	private DexFrame frame;
	private JLabel step;
	private JProgressBar progress;
	private DexOpenApkSwingWorker worker;
	
	/**
	 * Constructor.
	 * @param frame	The parent frame in which the dialog is displayed
	 * @param apk	The Apk to open
	 */
	public DexOpenApk(DexFrame frame, File apk)
	{
		this.frame = frame;
		this.apk = apk;
		
		//Setting layout
		this.setAlwaysOnTop(true);
		frame.setEnabled(false);
		this.setLayout(new BorderLayout());
		int width = frame.getSize().width;
		int height = frame.getSize().height;
		this.setBounds(width/3, height/3, width/2, height/4);
		this.setVisible(true);
		step = new JLabel();
		step.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		progress = new JProgressBar();
		progress.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		progress.setValue(0);
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				worker.stop();
			}
		});
		
		JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		bottom.add(cancel);
		
		this.add(step, BorderLayout.NORTH);
		this.add(progress, BorderLayout.CENTER);
		this.add(bottom, BorderLayout.SOUTH);
		
		
		new DexOpenApkSwingWorker().execute();
	}
	
	/**
	 * Swing worker for the DexGen generating function
	 * @author Zanoncello Matteo
	 *
	 */
	class DexOpenApkSwingWorker extends SwingWorker<DexGen, DexProgress>
	{
	    @Override
	    public DexGen doInBackground()
	    {
	    	//Converting file in a DexGen
	    	try
			{
				ApkReader apkReader;
				
				this.publish(new DexProgress("Reading Apk file...", 1));
				apkReader = new ApkReader(apk);
				if(Thread.currentThread().isInterrupted())
					return null;
				
				this.publish(new DexProgress("Extracting file Dex...", 10));
				byte[] dexFileBytes = apkReader.extractDex();
				if(Thread.currentThread().isInterrupted())
					return null;
				
				DexReader dexReader = new DexReader(true, true, dexFileBytes);
				
				this.publish(new DexProgress("Reading Dex file...", 40));
				DexFile dexFile = dexReader.ReadDex();
				if(Thread.currentThread().isInterrupted())
					return null;
				
				this.publish(new DexProgress("Generating DexGen...", 70));
				DexGen dexGen = new DexGen(dexFile);
				if(Thread.currentThread().isInterrupted())
					return null;
				
				this.publish(new DexProgress("Finalizing...", 100));
				return dexGen;
			}
			catch (IOException e)
	        {
	            e.printStackTrace();
	        }
			catch (CodegenException e)
	        {
	            e.printStackTrace();
	        }
	    	return null;
	    }

	    @Override
	    public void done()
	    {
	    	try
	    	{
	    		if(get() != null)
	    			frame.updateLayout(get(), apk.getName());
			}
	    	catch (InterruptedException | ExecutionException e)
	    	{
				e.printStackTrace();
			}
			frame.setEnabled(true);
			frame.requestFocus();
	    	DexOpenApk.this.setVisible(false);
	    	DexOpenApk.this.dispose();
	    }
	    
	    @Override
	    public void process(List<DexProgress> values)
	    {
	    	DexProgress prog = values.get(values.size() - 1);
	    	DexOpenApk.this.step.setText(prog.phrase);
	    	DexOpenApk.this.progress.setValue(prog.percentage);
	    	DexOpenApk.this.update(getGraphics());
	    }
	    
	    /**
	     * Stops the doInBackground function of the worker
	     */
	    public void stop()
	    {
	    	//Stop the backgound process
	    	Thread.currentThread().interrupt();
	    }
	};
}
