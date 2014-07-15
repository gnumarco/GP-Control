/* 
 * Copyright (C) 2014 Marc Segond <dr.marc.segond@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
/**
 * @author Segond
 * @society Laboratoire D Informatique du Littoral - ULCO - Calais - FRANCE
 * @version 2.0.0
 */

package data;

import java.io.IOException;
import ucar.nc2.*;
import ucar.ma2.*;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 *
 * @author  marco
 */
public class OpenNetCdf extends javax.swing.JDialog {
    
    Stream[][] mer = null;
    String nomFichier;
    
    /** Creates new form openNetCdf */
    public OpenNetCdf(java.awt.Frame parent, boolean modal, String s) {
	super(parent, modal);
	initComponents();
	nomFichier = s;
	lireNetCDF(s);
	this.setVisible(true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        java.awt.GridBagConstraints gridBagConstraints;

        cartesDisposLabel = new javax.swing.JLabel();
        cartesDisposOK = new javax.swing.JButton();
        timesLabel = new javax.swing.JLabel();
        profLabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        cartesDispos = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        profsDispos = new javax.swing.JList();

        getContentPane().setLayout(new java.awt.GridBagLayout());

        setTitle("Cartes Disponibles dans ce fichier");
	
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        cartesDisposLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        cartesDisposLabel.setText("Cartes disponibles");
        cartesDisposLabel.setBorder(new javax.swing.border.EtchedBorder());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 0);
        getContentPane().add(cartesDisposLabel, gridBagConstraints);

        cartesDisposOK.setText("OK");
        cartesDisposOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ok(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 5);
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        getContentPane().add(cartesDisposOK, gridBagConstraints);


        timesLabel.setText("Instants");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        getContentPane().add(timesLabel, gridBagConstraints);

        profLabel.setText("Profondeurs");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
        getContentPane().add(profLabel, gridBagConstraints);

        cartesDispos.setBorder(new javax.swing.border.EtchedBorder());
        cartesDispos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        cartesDispos.setToolTipText("null");
        cartesDispos.setDoubleBuffered(true);
        cartesDispos.setDragEnabled(true);
        cartesDispos.setVisibleRowCount(6);
        jScrollPane1.setViewportView(cartesDispos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jScrollPane1, gridBagConstraints);

        profsDispos.setBorder(new javax.swing.border.EtchedBorder());
        profsDispos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        profsDispos.setDoubleBuffered(true);
        profsDispos.setVisibleRowCount(6);
        jScrollPane2.setViewportView(profsDispos);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        getContentPane().add(jScrollPane2, gridBagConstraints);
	setSize(450,400);
	setResizable(false);
    }//GEN-END:initComponents
    
    public Stream[][] getMer(){ return mer;}
    
    public final void lireNetCDF(String s){
	try{
	    NetcdfFile f = NetcdfFile.open(s);
	    Variable var = f.findVariable("time");
	    Array tps = var.read();
	    String stmp;
	    String[] times = new String[tps.getShape()[0]];
	    System.out.println("nbcartesdispos: "+times.length);
	    Index ind = tps.getIndex();
	 /*for(int i=0;i<tmpTimes.length;i++){
	    System.out.println("ID "+i+":"+tmpTimes[i]);
	 }*/
	    for(int i=0;i<(tps.getShape())[0];i++){
		ind.set(i);
		double t = tps.getDouble(ind);
		long tmp = (long)t;
		GregorianCalendar cal = new GregorianCalendar(java.util.Locale.FRENCH);
		cal.set(0000,0,1,0,0,0);
		Date ref = cal.getTime();
		tmp = tmp * 1000;
		tmp = tmp + ref.getTime();
		Date cur = new Date(tmp);
		stmp = cur.toString();
		stmp = stmp.substring(0,22);
		times[i]=stmp;
	    }
	    cartesDispos.setListData(times);
	    
	    var = f.findVariable("z");
	    tps = var.read();
	    String[] profs = new String[tps.getShape()[0]];
	    ind = tps.getIndex();
	    for(int i=0;i<(tps.getShape())[0];i++){
		ind.set(i);
		float p = tps.getFloat(ind);
		
		profs[i]=Float.toString(p);
	    }
	    profsDispos.setListData(profs);
	    profsDispos.setLocale(java.util.Locale.FRENCH);
	    f.close();
	}catch(IOException e){}
    }
    
    
    private void ok(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ok
	int sel = cartesDispos.getSelectedIndex();
	double max = -9999d;
	try{
	    NetcdfFile f = NetcdfFile.open(nomFichier);
	    int tailleX = (f.findDimension("lon")).getLength();
	    int tailleY = (f.findDimension("lat")).getLength();
	    mer = new Stream[tailleX][tailleY];
	    System.out.println("creation d'une matrice "+tailleX+";"+tailleY);
	    for(int r=0;r<tailleX;r++)
		for(int t=0;t<tailleY;t++)
		    mer[r][t] = new Stream();
	    Variable var = f.findVariable("u");
	    Array tab = var.read();
	    Index ind = tab.getIndex();
	    for(int i=0;i<tailleX;i++){
		for(int j=0;j<tailleY;j++){
		    ind.set(sel,j,i);
		    mer[i][j].setXBase(tab.getDouble(ind));
		    if(Math.abs(mer[i][j].getXBase())>max)
			max = Math.abs(mer[i][j].getXBase());
		    if( mer[i][j].getXBase()==0d &&  mer[i][j].getYBase()==0d)
			mer[i][j].setSurTerre(true);
		}
	    }
	    var = f.findVariable("v");
	    if(var != null){
		tab = var.read();
		ind = tab.getIndex();
		for(int i=0;i<tailleX;i++){
		    for(int j=0;j<tailleY;j++){
			ind.set(sel,j,i);
			mer[i][j].setYBase(tab.getDouble(ind));
			if(Math.abs(mer[i][j].getYBase())>max)
			    max = Math.abs(mer[i][j].getYBase());
		    }
		}
	    }
	    
	    //Normalisation
	    for(int i=0;i<tailleX;i++){
		for(int j=0;j<tailleY;j++){
		    mer[i][j].setXBase(mer[i][j].getXBase()*(1d/max));
		    mer[i][j].setYBase(mer[i][j].getYBase()*(1d/max));
		}
	    }
	    
	    var = f.findVariable("HO");
	    tab = var.read();
	    ind = tab.getIndex();
	    for(int i=0;i<tailleX;i++){
		for(int j=0;j<tailleY;j++){
		    ind.set(j,i);
		    mer[i][j].setBat(tab.getFloat(ind));
		}
	    }
	    
	    var = f.findVariable("SAL");
	    tab = var.read();
	    ind = tab.getIndex();
	    for(int i=0;i<tailleX;i++){
		for(int j=0;j<tailleY;j++){
		    ind.set(sel,j,i);
		    mer[i][j].setSal(tab.getFloat(ind));
		}
	    }
	    
	    var = f.findVariable("TEMP");
	    tab = var.read();
	    ind = tab.getIndex();
	    for(int i=0;i<tailleX;i++){
		for(int j=0;j<tailleY;j++){
		    ind.set(sel,j,i);
		    mer[i][j].setTemp(tab.getFloat(ind));
		}
	    }
	    f.close();
	    
	}catch(IOException e){System.out.println(e);}
	setVisible(false);
	dispose();
    }//GEN-LAST:event_ok
    
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
	setVisible(false);
	dispose();
    }//GEN-LAST:event_closeDialog
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList cartesDispos;
    private javax.swing.JLabel timesLabel;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList profsDispos;
    private javax.swing.JButton cartesDisposOK;
    private javax.swing.JLabel profLabel;
    private javax.swing.JLabel cartesDisposLabel;
    // End of variables declaration//GEN-END:variables
    
}
