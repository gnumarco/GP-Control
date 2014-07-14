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
package data;

import java.util.ArrayList;
import java.awt.Point;

/**
 * cet objet est un conteneur de VortexGeom
 */
public class CollVortexGeom {

    private VortexGeom lot[] = null;

    /**
     * inutile Creates a new instance of candidates
     */
    public CollVortexGeom(int n) {
        if (n > 0) {
            lot = new VortexGeom[n];
            for (int i = 0; i < n; i++) {
                lot[n] = new VortexGeom();
            }
        }
    }

    public CollVortexGeom() {

    }

    public void ajouter(VortexGeom b) {
        if (lot == null) {
            lot = new VortexGeom[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            VortexGeom[] tmp = lot;
            lot = null;
            lot = new VortexGeom[s + 1];
            for (s = 0; s < tmp.length; s++) {
                lot[s] = tmp[s];
            }
            lot[tmp.length] = b;
            tmp = null;
        }
    }

    public void ajouter(int[] xpts, int[] ypts, int npts, int tag) {
        VortexGeom b = new VortexGeom(xpts, ypts, npts, tag);
        if (lot == null) {
            lot = new VortexGeom[1];
            lot[0] = b;
        } else {
            int s = lot.length;
            VortexGeom[] tmp = lot;
            lot = null;
            lot = new VortexGeom[s + 1];
            for (s = 0; s < tmp.length; s++) {
                lot[s] = tmp[s];
            }
            lot[tmp.length] = b;
            tmp = null;
        }
    }

    public void ajouter(ArrayList v, int tag) {
        int[] x = null;
        int[] y = null;
        int n = 0;
        if ((v != null) && (v.size() != 0)) {
            //extraireX(ArrayList v)
            x = new int[v.size()];
            for (int i = 0; i < v.size(); i++) {
                x[i] = ((Point) v.get(i)).x;
            }

            // extraireY(ArrayList v)
            y = new int[v.size()];
            for (int i = 0; i < v.size(); i++) {
                y[i] = ((Point) v.get(i)).y;
            }
            n = v.size();
        }
        this.ajouter(x, y, n, tag);
    }

    public VortexGeom getVortexGeom(int num) {
        VortexGeom ret = null;
        if ((lot != null) && ((num > -1) & (num < lot.length))) {
            ret = lot[num];
        }
        return ret;
    }

    public VortexGeom[] getVortexGeom() {
        VortexGeom[] ret = lot;
        return ret;
    }

    public void effacerVortexGeom(int num) {
        if ((lot != null) && ((num > -1) & (num < lot.length))) {
            int s = lot.length;
            ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
            for (s = 0; s < lot.length; s++) {
                tmp.add(lot[s]);
            }

            tmp.remove(num);
            lot = null;
            if (tmp.size() > 0) {
                lot = new VortexGeom[tmp.size()];
                tmp.toArray(lot);
            }
        }
    }

    public void effacerVortexGeom(int from, int to) {
        if ((lot != null) && ((from > -1) & (to < lot.length))) {
            int s = lot.length;
            ArrayList tmp = new ArrayList(s);//new ArrayList((java.util.Collection)lot);
            for (s = 0; s < lot.length; s++) {
                tmp.add(lot[s]);
            }

            for (s = from; s <= to; s++) {
                tmp.remove(from);
            }
            lot = null;
            if (tmp.size() > 0) {
                lot = new VortexGeom[tmp.size()];
                tmp.toArray(lot);
            }
        }
    }

    public void effacerTout() {
        if (lot != null) {
            for (int s = 0; s < lot.length; s++) {
                lot[s] = null;
            }
        }
        lot = null;
    }

    /**
     * rend la memoire !
     */
    public void dispose() {
        if (lot != null) {
            for (int s = 0; s < lot.length; s++) {
                lot[s].dispose();
                lot[s] = null;
            }
        }
        lot = null;
    }

    public int size() {
        int ret = 0;
        if (lot != null) {
            ret = lot.length;
        }
        return ret;
    }

}
