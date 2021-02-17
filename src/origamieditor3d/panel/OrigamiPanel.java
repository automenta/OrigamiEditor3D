// This file is part of Origami Editor 3D.
// Copyright (C) 2013-2017 Bágyoni Attila <ba-sz-at@users.sourceforge.net>
// Origami Editor 3D is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <http:// www.gnu.org/licenses/>.
package origamieditor3d.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.Random;

import origamieditor3d.origami.Camera;
import origamieditor3d.origami.Geometry;

/**
 * @author Attila Bágyoni (ba-sz-at@users.sourceforge.net)
 */
public class OrigamiPanel extends Panel {

    private static final long serialVersionUID = 1L;

    public OrigamiPanel() {

        super();
        ready_to_paint = false;
        PanelCamera = new Camera(0, 0, 1);
        linerOn = false;
        tracker_x = null;
        tracker_y = null;
        trackerOn = false;
        liner_triangle = new double[3][];
        liner_triangle[0] = null;
        liner_triangle[1] = null;
        liner_triangle[2] = null;
        liner_grab_index = 0;
        alignment_point = null;
        paper_front_color = 0xFFFFFF;
        previewOn = false;
        displaymode = DisplayMode.GRADIENT;
        rulerMode = RulerMode.Normal;
        antialiasOn = true;
    }
    final static private int[] random_front_colors =
                { 0x000097, 0x24A0DF, 0x397E79, //blue
                0xDF0000, //red
                0x00E371, 0x66E71D, 0x20CB07, //green
                0xFFFFCC, //yellow
                0xA840F4, 0xC40A86 }; //purple
    final private double[][] liner_triangle;
    private Integer ruler_x1, ruler_y1, ruler_x2, ruler_y2;
    private boolean linerOn;
    
    private double[] tracker_im;
    private int liner_grab_index;
    private int[] alignment_point;
    private int paper_front_color;
    private boolean previewOn;
    private DisplayMode displaymode;
    private Integer protractor_angle;
    private boolean antialiasOn;

    public enum DisplayMode {

        PLAIN, UV, WIREFRAME, GRADIENT
    }

    @Override
    public boolean isTracked() {
        return trackerOn;
    }

    @Override
    public void grabTriangleAt(int vertIndex) {
        liner_grab_index = vertIndex;
    }

    @Override
    public void rulerOn(Camera refcam, int x1, int y1, int x2, int y2) {

        ruler_x1 = x1;
        ruler_y1 = y1;
        ruler_x2 = x2;
        ruler_y2 = y2;
        linerOn = true;
    }

    @Override
    public void rulerOff() {
        linerOn = false;
    }

    public void previewOn() {
        previewOn = true;
    }

    public void previewOff() {
        previewOn = false;
    }

    @Override
    public void setTracker(Camera refkamera, int x, int y) {

        tracker_x = x;
        tracker_y = y;
        try {
            tracker_im = PanelOrigami.find3dImageOf(
                    ((double) tracker_x - refkamera.xshift
                            + new Camera(refkamera.xshift, refkamera.yshift, refkamera.zoom())
                            .projection0(refkamera.camera_pos())[0]) / refkamera.zoom(),
                    ((double) tracker_y - refkamera.yshift
                            + new Camera(refkamera.xshift, refkamera.yshift, refkamera.zoom())
                            .projection0(refkamera.camera_pos())[1]) / refkamera.zoom());
        } catch (RuntimeException ex) {
        }
        trackerOn = true;
    }

    @Override
    public void resetTracker() {

        tracker_x = null;
        tracker_y = null;
        trackerOn = false;
    }

    @Override
    public void tiltTriangleTo(Camera refkamera, Integer... xy) {

        try {
            int x = xy[0];
            int y = xy[1];
            liner_triangle[liner_grab_index] = PanelOrigami.find3dImageOf(
                    ((double) x - refkamera.xshift
                            + new Camera(refkamera.xshift, refkamera.yshift, refkamera.zoom())
                            .projection0(refkamera.camera_pos())[0]) / refkamera.zoom(),
                    ((double) y - refkamera.yshift
                            + new Camera(refkamera.xshift, refkamera.yshift, refkamera.zoom())
                            .projection0(refkamera.camera_pos())[1]) / refkamera.zoom());
        } catch (RuntimeException ex) {
            liner_triangle[liner_grab_index] = null;
        }
        liner_grab_index++;
        liner_grab_index %= 3;
    }

    @Override
    public void resetTriangle() {

        liner_grab_index = 0;
        liner_triangle[0] = null;
        liner_triangle[1] = null;
        liner_triangle[2] = null;
    }

    public void setAlignmentPoint(int... point) {
        alignment_point = point;
    }

    public void resetAlignmentPoint() {
        alignment_point = null;
    }

    public void displayProtractor(int angle) {
        protractor_angle = angle;
    }

    public void hideProtractor() {
        protractor_angle = null;
    }

    @Override
    public void reset() {

        tracker_x = null;
        tracker_y = null;
        liner_grab_index = 0;
        liner_triangle[0] = null;
        liner_triangle[1] = null;
        liner_triangle[2] = null;
        trackerOn = false;
        alignment_point = null;
        protractor_angle = null;
    }

    public void setFrontColor(int rgb) {
        paper_front_color = rgb;
    }

    public int getFrontColor() {
        return paper_front_color;
    }
    
    public void randomizeFrontColor() {
        paper_front_color = random_front_colors[new Random().nextInt(random_front_colors.length)];
    }

    public void setDisplaymode(DisplayMode value) {
        displaymode = value;
    }

    public DisplayMode displaymode() {
        return displaymode;
    }

    public void setTexture(java.awt.image.BufferedImage tex) throws Exception {
        PanelCamera.setTexture(tex);
    }
    
    public void antialiasOn() {
        
        antialiasOn = true;
        repaint();
    }
    
    public void antialiasOff() {
        
        antialiasOn = false;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        if (ready_to_paint) {

            Graphics2D gx2d = (Graphics2D)g;

            switch (displaymode) {
                case UV -> PanelCamera.drawTexture(g, this.getWidth(), this.getHeight());
                case GRADIENT -> {
                    PanelCamera.drawGradient(g, paper_front_color, PanelOrigami);
                    if (antialiasOn) {
                        gx2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }
                    PanelCamera.drawEdges(g, new Color(0, 0, 0, .5f), PanelOrigami);
                }
                case PLAIN -> {
                    PanelCamera.drawFaces(g, paper_front_color, PanelOrigami);
                    if (antialiasOn) {
                        gx2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }
                    PanelCamera.drawEdges(g, new Color(0, 0, 0, .5f), PanelOrigami);
                }
                case WIREFRAME -> {
                    if (antialiasOn) {
                        gx2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    }
                    PanelCamera.drawEdges(g, new Color(0, 0, 0, .5f), PanelOrigami);
                }
            }
            
            gx2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        if (alignment_point != null) {

            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g;
            g2.setColor(Color.DARK_GRAY);
            g2.setStroke(new java.awt.BasicStroke(2));
            g2.drawRect(alignment_point[0] - 3, alignment_point[1] - 3, 6, 6);
            g2.setStroke(new java.awt.BasicStroke(1));
        }
        /*
        if (beacons != null) {
            g.setColor(Color.red);
            for (int i = 0; i < beacons.length; i++) {
                int x = (int) (PanelCamera.projection(beacons[i])[0]) + PanelCamera.xshift;
                int y = (int) (PanelCamera.projection(beacons[i])[1]) + PanelCamera.yshift;
                g.fillOval(x - alignment_radius, y - alignment_radius, alignment_radius * 2, alignment_radius * 2);
            }
        }*/

        g.setColor(Color.red);
        if (linerOn) {

            if (previewOn) {

                double pontX = ((double) ruler_x2 - this.PanelCamera.xshift) / this.PanelCamera.zoom();
                double pontY = ((double) ruler_y2 - this.PanelCamera.yshift) / this.PanelCamera.zoom();
                double pont1X = ((double) ruler_x1 - this.PanelCamera.xshift) / this.PanelCamera.zoom();
                double pont1Y = ((double) ruler_y1 - this.PanelCamera.yshift) / this.PanelCamera.zoom();

                double[] vonalzoNV = {
                    this.PanelCamera.axis_x()[0] * (ruler_y2 - ruler_y1)
                    + this.PanelCamera.axis_y()[0] * (ruler_x1 - ruler_x2),
                    this.PanelCamera.axis_x()[1] * (ruler_y2 - ruler_y1)
                    + this.PanelCamera.axis_y()[1] * (ruler_x1 - ruler_x2),
                    this.PanelCamera.axis_x()[2] * (ruler_y2 - ruler_y1)
                    + this.PanelCamera.axis_y()[2] * (ruler_x1 - ruler_x2)
                };
                double[] vonalzoPT = {
                    this.PanelCamera.axis_x()[0] / this.PanelCamera.zoom() * pontX
                        + this.PanelCamera.axis_y()[0] / this.PanelCamera.zoom() * pontY
                        + this.PanelCamera.camera_pos()[0],
                    this.PanelCamera.axis_x()[1] / this.PanelCamera.zoom() * pontX
                        + this.PanelCamera.axis_y()[1] / this.PanelCamera.zoom() * pontY
                        + this.PanelCamera.camera_pos()[1],
                    this.PanelCamera.axis_x()[2] / this.PanelCamera.zoom() * pontX
                        + this.PanelCamera.axis_y()[2] / this.PanelCamera.zoom() * pontY
                        + this.PanelCamera.camera_pos()[2]
                };
                double[] vonalzoPT1 = {
                    this.PanelCamera.axis_x()[0] / this.PanelCamera.zoom() * pont1X
                        + this.PanelCamera.axis_y()[0] / this.PanelCamera.zoom() * pont1Y
                        + this.PanelCamera.camera_pos()[0],
                    this.PanelCamera.axis_x()[1] / this.PanelCamera.zoom() * pont1X
                        + this.PanelCamera.axis_y()[1] / this.PanelCamera.zoom() * pont1Y
                        + this.PanelCamera.camera_pos()[1],
                    this.PanelCamera.axis_x()[2] / this.PanelCamera.zoom() * pont1X
                        + this.PanelCamera.axis_y()[2] / this.PanelCamera.zoom() * pont1Y
                        + this.PanelCamera.camera_pos()[2]
                };
                if (rulerMode == RulerMode.Neusis) {
                    vonalzoNV = Geometry.vector(vonalzoPT, vonalzoPT1);
                }

                PanelCamera.drawPreview(g, Color.green, PanelOrigami, vonalzoPT, vonalzoNV);
            }

            g.setColor(Color.red);
            if (rulerMode == RulerMode.Neusis) {
                int maxdim = Math.max(this.getWidth(), this.getHeight())
                        /Math.max(Math.max(Math.abs(ruler_y1 - ruler_y2), Math.abs(ruler_x1 - ruler_x2)), 1) + 1;
                g.drawLine(ruler_x2 + maxdim * (ruler_y1 - ruler_y2), ruler_y2 + maxdim * (ruler_x2 - ruler_x1),
                        ruler_x2 - maxdim * (ruler_y1 - ruler_y2), ruler_y2 - maxdim * (ruler_x2 - ruler_x1));
            } else {
                int maxdim = Math.max(this.getWidth(), this.getHeight())
                        /Math.max(Math.max(Math.abs(ruler_y1 - ruler_y2), Math.abs(ruler_x1 - ruler_x2)), 1) + 1;
                g.drawLine(ruler_x1 + maxdim * (ruler_x1 - ruler_x2), ruler_y1 + maxdim * (ruler_y1 - ruler_y2),
                        ruler_x2 - maxdim * (ruler_x1 - ruler_x2), ruler_y2 - maxdim * (ruler_y1 - ruler_y2));
            }
        }
        if (trackerOn) {
            int x = (int) (PanelCamera.projection(tracker_im)[0]) + PanelCamera.xshift;
            int y = (int) (PanelCamera.projection(tracker_im)[1]) + PanelCamera.yshift;
            g.drawLine(x - 5, y, x + 5, y);
            g.drawLine(x, y - 5, x, y + 5);
        }

        g.setColor(Color.magenta);
        ((java.awt.Graphics2D)g).setStroke(new java.awt.BasicStroke(2));

        if (liner_triangle[0] != null) {
            int x = (int) (PanelCamera.projection(liner_triangle[0])[0]) + PanelCamera.xshift;
            int y = (int) (PanelCamera.projection(liner_triangle[0])[1]) + PanelCamera.yshift;
            g.drawLine(x - 3, y + 3, x + 3, y - 3);
            g.drawLine(x - 3, y - 3, x + 3, y + 3);
        }
        if (liner_triangle[1] != null) {
            int x = (int) (PanelCamera.projection(liner_triangle[1])[0]) + PanelCamera.xshift;
            int y = (int) (PanelCamera.projection(liner_triangle[1])[1]) + PanelCamera.yshift;
            g.drawLine(x - 3, y + 3, x + 3, y - 3);
            g.drawLine(x - 3, y - 3, x + 3, y + 3);
        }
        if (liner_triangle[2] != null) {
            int x = (int) (PanelCamera.projection(liner_triangle[2])[0]) + PanelCamera.xshift;
            int y = (int) (PanelCamera.projection(liner_triangle[2])[1]) + PanelCamera.yshift;
            g.drawLine(x - 3, y + 3, x + 3, y - 3);
            g.drawLine(x - 3, y - 3, x + 3, y + 3);
        }
        ((java.awt.Graphics2D)g).setStroke(new java.awt.BasicStroke(1));

        if (protractor_angle != null) {
            drawProtractor(g, protractor_angle);
        }
    }

    private void drawProtractor(Graphics g, int angle) {

        angle -= 90;
        while (angle < 0) {
            angle += 360;
        }

        int width = getWidth();
        int height = getHeight();
        int diam = Math.min(width, height)/2;
        g.setColor(new Color(255, 255, 255, 170));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.red);
        g.drawLine(
                (int)(width/2 + Math.cos(angle*Math.PI/180)*diam/2),
                (int)(height/2 + Math.sin(angle*Math.PI/180)*diam/2),
                width/2,
                height/2
        );

        g.setColor(Color.black);
        g.drawOval((width-diam)/2, (height-diam)/2, diam, diam);

        for (int i=0; i<360; i+=5) {

            int notch = i % 180 == 0 ? diam/6 : i % 90 == 0 ? diam/8 : i % 45 == 0 ? diam/10 : diam/14;
            g.drawLine(
                    (int)(width/2 + Math.cos(i*Math.PI/180)*diam/2),
                    (int)(height/2 + Math.sin(i*Math.PI/180)*diam/2),
                    (int)(width/2 + Math.cos(i*Math.PI/180)*(diam/2-notch)),
                    (int)(height/2 + Math.sin(i*Math.PI/180)*(diam/2-notch))
            );

        }
        g.setFont(g.getFont().deriveFont(12.0f));
        g.drawString("0°", width/2-5, (height-diam)/2-5);
        g.drawString("90°", (width+diam)/2+5, height/2+5);
        g.drawString("180°", width/2-10, (height+diam)/2+15);
        g.drawString("-90°", (width-diam)/2-30, height/2+5);

        if (angle % 90 != 0) {
            g.drawString(
                    (angle < 90 ? angle + 90 : angle - 270) + "°",
                    (int)(width/2 + Math.cos(angle*Math.PI/180)*diam/2 + (angle > 90 && angle < 270 ? -30 : 10)),
                    (int)(height/2 + Math.sin(angle*Math.PI/180)*diam/2) + (angle < 180 ? 15 : -5));
        }
    }

    @Override
    public java.awt.Point getToolTipLocation(java.awt.event.MouseEvent e) {

        java.awt.Point pt = e.getPoint();
        pt.y += java.awt.Toolkit.getDefaultToolkit().getBestCursorSize(10, 10).height / 2;
        pt.x += java.awt.Toolkit.getDefaultToolkit().getBestCursorSize(10, 10).width / 2;
        return pt;
    }
    
    public double[] getRulerNormalvector() {
        
        if (linerOn && rulerMode == RulerMode.Normal) {
            
            double[] rulerNV = {
                PanelCamera.axis_x()[0] * (ruler_y2 - ruler_y1)
                        + PanelCamera.axis_y()[0] * (ruler_x1 - ruler_x2),
                PanelCamera.axis_x()[1] * (ruler_y2 - ruler_y1)
                        + PanelCamera.axis_y()[1] * (ruler_x1 - ruler_x2),
                PanelCamera.axis_x()[2] * (ruler_y2 - ruler_y1)
                        + PanelCamera.axis_y()[2] * (ruler_x1 - ruler_x2)};
            
            if (Geometry.scalar_product(PanelCamera.camera_pos(), rulerNV)
                    - Geometry.scalar_product(getRulerPoint(), rulerNV) > 0) {
                rulerNV = new double[] { -rulerNV[0], -rulerNV[1], -rulerNV[2] };
            }
            return rulerNV;
        }
        if (linerOn && rulerMode == RulerMode.Neusis) {
            
            double[] rulerPT = getRulerPoint();
            double[] rulerNV = Geometry.vector(rulerPT, getRulerPoint1());
            if (Geometry.scalar_product(PanelCamera.camera_pos(), rulerNV)
                    - Geometry.scalar_product(rulerPT, rulerNV) > 0) {
                rulerNV = new double[] { -rulerNV[0], -rulerNV[1], -rulerNV[2] };
            }
            return rulerNV;
        }
        return null;
    }
    
    public double[] getRulerPoint() {
        
        if (linerOn) {
            
            double pontX = ((double) ruler_x2 - PanelCamera.xshift) / PanelCamera.zoom();
            double pontY = ((double) ruler_y2 - PanelCamera.yshift) / PanelCamera.zoom();

            return new double[]{
                    PanelCamera.axis_x()[0] / PanelCamera.zoom() * pontX
                            + PanelCamera.axis_y()[0] / PanelCamera.zoom() * pontY
                            + PanelCamera.camera_pos()[0],
                    PanelCamera.axis_x()[1] / PanelCamera.zoom() * pontX
                            + PanelCamera.axis_y()[1] / PanelCamera.zoom() * pontY
                            + PanelCamera.camera_pos()[1],
                    PanelCamera.axis_x()[2] / PanelCamera.zoom() * pontX
                            + PanelCamera.axis_y()[2] / PanelCamera.zoom() * pontY
                            + PanelCamera.camera_pos()[2] };
        }
        return null;
    }
    
    private double[] getRulerPoint1() {
        
        if (linerOn) {
            
            double pont1X = ((double) ruler_x1 - PanelCamera.xshift) / PanelCamera.zoom();
            double pont1Y = ((double) ruler_y1 - PanelCamera.yshift) / PanelCamera.zoom();

            return new double[]{
                    PanelCamera.axis_x()[0] / PanelCamera.zoom() * pont1X
                            + PanelCamera.axis_y()[0] / PanelCamera.zoom() * pont1Y
                            + PanelCamera.camera_pos()[0],
                    PanelCamera.axis_x()[1] / PanelCamera.zoom() * pont1X
                            + PanelCamera.axis_y()[1] / PanelCamera.zoom() * pont1Y
                            + PanelCamera.camera_pos()[1],
                    PanelCamera.axis_x()[2] / PanelCamera.zoom() * pont1X
                            + PanelCamera.axis_y()[2] / PanelCamera.zoom() * pont1Y
                            + PanelCamera.camera_pos()[2] };
        }
        return null;
    }
    
}