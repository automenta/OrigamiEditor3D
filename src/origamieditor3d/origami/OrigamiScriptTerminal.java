// This file is part of Origami Editor 3D.
// Copyright (C) 2013, 2014, 2015 Bágyoni Attila <ba-sz-at@users.sourceforge.net>
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
package origamieditor3d.origami;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import origamieditor3d.resources.Dictionary;
import origamieditor3d.resources.Instructor;

/**
 * Represents an OrigamiScript engine. To learn more about OrigamiScript, see
 * http://origamieditor3d.sourceforge.net/osdoc_en.html
 * @author Attila Bágyoni (ba-sz-at@users.sourceforge.net)
 * @since 2013-09-29
 */
public class OrigamiScriptTerminal {

    public OrigamiScriptTerminal(Access access) {

        this.access = access;
        version = maxVersion;
        TerminalCamera = new Camera(0, 0, 1);
        corners = new ArrayList<>();
        papertype = Origami.PaperType.Custom;
        paper_color = 0x0000FF;
        paper_texture = null;
        history = new ArrayList<>();
        filename = null;
        ppoint = null;
        pnormal = null;
        tracker = null;
        phi = null;
        title = null;

        prompt = false;
    }

    private OrigamiScriptTerminal(Access access, String filename) {

        this(access);
        this.filename = filename;
        TerminalCamera = new Camera(0, 0, 1);
    }

    final private Integer maxVersion = 1;
    private Access access;
    //
    // betöltött terminál mezők
    private Integer version = 1;
    //
    // eltárolt terminál mezők
    private final ArrayList<String> history;

    public ArrayList<String> history() {

        return history;
    }

    private String filename;
    //
    // betöltött szerkesztési mezők
    private double[] ppoint;
    private double[] pnormal;
    private double[] tracker;
    private Integer phi;
    private ArrayList<double[]> corners;
    private Origami.PaperType papertype;
    private String title;
    //
    // eltárolt szerkesztési mezők
    public Origami TerminalOrigami;
    private Camera TerminalCamera;
    private int paper_color;
    public int paper_color() {
        return paper_color;
    }
    final static private int default_paper_color = 0x000097;
    private java.awt.image.BufferedImage paper_texture;
    public java.awt.image.BufferedImage paper_texture() {
        return paper_texture;
    }

    private void paramReset() {

        ppoint = null;
        pnormal = null;
        tracker = null;
        phi = null;
        title = null;
        corners = new ArrayList<>(Collections.emptyList());
        papertype = Origami.PaperType.Custom;
    }

    private void totalReset() {

        paramReset();
        version = maxVersion;
        history.clear();

        TerminalOrigami.undo(TerminalOrigami.history().size());
        TerminalCamera = new Camera(0, 0, 1);
    }

    public void historyReset() {
        history.clear();
    }

    public Integer maxVersion() {
        return maxVersion;
    }

    public Access access() {
        return access;
    }

    public enum Access {
        USER, ROOT, DEV
    }

    private interface Command {

        void execute(String... args) throws Exception;
    }

    final private Map<String, Command> Commands = new HashMap<>();
    final private Map<String, Command> Params = new HashMap<>();

    {
        Params.put("plane", args -> plane(args));

        Params.put("planethrough", args -> planethrough(args));

        Params.put("angle-bisector", args -> angle_bisector(args));

        Params.put("planepoint", args -> planepoint(args));

        Params.put("planenormal", args -> planenormal(args));

        Params.put("target", args -> target(args));

        Params.put("angle", args -> angle(args));

        Params.put("paper", args -> paper(args));

        Params.put("corner", args -> corner(args));

        Params.put("version", args -> version(args));

        Params.put("locale", args -> locale(args));

        Params.put("filename", args -> filename(args));

        Params.put("title", args -> title(args));

        Params.put("camera", args -> camera(args));

        Params.put("color", args -> color(args));
        
        Params.put("uncolor", args -> uncolor(args));
    }

    {
        Commands.put("new", args -> NEW());

        Commands.put("rotate", args -> ROTATE());

        Commands.put("reflect", args -> REFLECT());

        Commands.put("cut", args -> CUT());

        Commands.put("undo", args -> UNDO());

        Commands.put("redo", args -> REDO());

        Commands.put("diagnostics", args -> DIAGNOSTICS());

        Commands.put("compile", args -> COMPILE());

        Commands.put("load", args -> LOAD());

        Commands.put("open", args -> OPEN());

        Commands.put("load-texture", args -> LOAD_TEXTURE());

        Commands.put("unload-texture", args -> UNLOAD_TEXTURE());

        Commands.put("export-ctm", args -> EXPORT_CTM());

        Commands.put("export-autopdf", args -> EXPORT_AUTOPDF());

        Commands.put("export-gif", args -> EXPORT_GIF());

        Commands.put("export-jar", args -> EXPORT_JAR());

        Commands.put("export-png", args -> EXPORT_PNG());

        Commands.put("export-revolving-gif", args -> EXPORT_REVOLVING_GIF());

        Commands.put("export-ori", args -> EXPORT_ORI());

        Commands.put("root", args -> ROOT());

        Commands.put("debug", args -> DEBUG());
    }

    /*
     * ///////////////////////////
     * EDITOR PARAMETERS
     */// /////////////////////////
    private void plane(String... args) throws Exception {

        switch (version) {
            default -> plane1(args);
        }
    }

    private void planethrough(String... args) throws Exception {

        switch (version) {
            default -> planethrough1(args);
        }
    }

    private void angle_bisector(String... args) throws Exception {

        switch (version) {
            default -> angle_bisector1(args);
        }
    }

    private void planepoint(String... args) throws Exception {

        switch (version) {
            default -> planepoint1(args);
        }
    }

    private void planenormal(String... args) throws Exception {

        switch (version) {
            default -> planenormal1(args);
        }
    }

    private void target(String... args) throws Exception {

        switch (version) {
            default -> target1(args);
        }
    }

    private void angle(String... args) throws Exception {

        switch (version) {
            default -> angle1(args);
        }
    }

    private void paper(String... args) throws Exception {

        switch (version) {
            default -> paper1(args);
        }
    }

    private void corner(String... args) throws Exception {

        switch (version) {
            default -> corner1(args);
        }
    }

    private void plane1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 2) {

            String[] pt = args[0].split(" ");
            String[] nv = args[1].split(" ");

            if (pt.length == 3 && nv.length == 3) {

                ppoint = new double[]{Double.parseDouble(pt[0]),
                    Double.parseDouble(pt[1]), Double.parseDouble(pt[2])};

                pnormal = new double[]{Double.parseDouble(nv[0]),
                    Double.parseDouble(nv[1]), Double.parseDouble(nv[2])};

            } else if (pt.length == 2 && nv.length == 3) {

                ppoint = TerminalOrigami.find3dImageOf(Double.parseDouble(pt[0]), Double.parseDouble(pt[1]));

                pnormal = new double[]{Double.parseDouble(nv[0]),
                    Double.parseDouble(nv[1]), Double.parseDouble(nv[2])};

            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void planethrough1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 3) {

            String[] pont1 = args[0].split(" ");
            String[] pont2 = args[1].split(" ");
            String[] pont3 = args[2].split(" ");

            double[] pt1, pt2, pt3;

            if (pont1.length == 3) {

                pt1 = new double[]{Double.parseDouble(pont1[0]),
                    Double.parseDouble(pont1[1]),
                    Double.parseDouble(pont1[2])};
            } else if (pont1.length == 2) {

                pt1 = TerminalOrigami.find3dImageOf(Double.parseDouble(pont1[0]),
                        Double.parseDouble(pont1[1]));
            } else {
                throw OrigamiException.H007;
            }

            if (pont2.length == 3) {

                pt2 = new double[]{Double.parseDouble(pont2[0]),
                    Double.parseDouble(pont2[1]),
                    Double.parseDouble(pont2[2])};
            } else if (pont2.length == 2) {

                pt2 = TerminalOrigami.find3dImageOf(Double.parseDouble(pont2[0]),
                        Double.parseDouble(pont2[1]));
            } else {
                throw OrigamiException.H007;
            }

            if (pont3.length == 3) {

                pt3 = new double[]{Double.parseDouble(pont3[0]),
                    Double.parseDouble(pont3[1]),
                    Double.parseDouble(pont3[2])};
            } else if (pont3.length == 2) {

                pt3 = TerminalOrigami.find3dImageOf(Double.parseDouble(pont3[0]),
                        Double.parseDouble(pont3[1]));
            } else {
                throw OrigamiException.H007;
            }

            if (Geometry.vector_length(Geometry.vector_product(
                    Geometry.vector(pt2, pt1), Geometry.vector(pt3, pt1))) != 0.0d) {

                ppoint = pt1;
                pnormal = Geometry.vector_product(Geometry.vector(pt2, pt1),
                        Geometry.vector(pt3, pt1));
            } else {
                throw OrigamiException.H008;
            }
        } else {
            throw OrigamiException.H007;
        }
    }

    private void angle_bisector1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 3) {

            String[] pont1 = args[0].split(" ");
            String[] pont2 = args[1].split(" ");
            String[] pont3 = args[2].split(" ");

            double[] pt1, pt2, pt3;

            if (pont1.length == 3) {

                pt1 = new double[]{Double.parseDouble(pont1[0]),
                    Double.parseDouble(pont1[1]),
                    Double.parseDouble(pont1[2])};
            } else if (pont1.length == 2) {

                pt1 = TerminalOrigami.find3dImageOf(Double.parseDouble(pont1[0]),
                        Double.parseDouble(pont1[1]));
            } else {
                throw OrigamiException.H007;
            }

            if (pont2.length == 3) {

                pt2 = new double[]{Double.parseDouble(pont2[0]),
                    Double.parseDouble(pont2[1]),
                    Double.parseDouble(pont2[2])};
            } else if (pont2.length == 2) {

                pt2 = TerminalOrigami.find3dImageOf(Double.parseDouble(pont2[0]),
                        Double.parseDouble(pont2[1]));
            } else {
                throw OrigamiException.H007;
            }

            if (pont3.length == 3) {

                pt3 = new double[]{Double.parseDouble(pont3[0]),
                    Double.parseDouble(pont3[1]),
                    Double.parseDouble(pont3[2])};
            } else if (pont3.length == 2) {

                pt3 = TerminalOrigami.find3dImageOf(Double.parseDouble(pont3[0]),
                        Double.parseDouble(pont3[1]));
            } else {
                throw OrigamiException.H007;
            }
            ppoint = pt2;
            pnormal = Geometry.vector(
                    Geometry.length_to_100(Geometry.vector(pt1, pt2)),
                    Geometry.length_to_100(Geometry.vector(pt3, pt2)));

            if (Geometry.vector_length(pnormal) == 0.0) {
                throw OrigamiException.H012;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void planepoint1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] pt = args[0].split(" ");

            if (pt.length == 3) {

                ppoint = new double[]{Double.parseDouble(pt[0]),
                    Double.parseDouble(pt[1]), Double.parseDouble(pt[2])};

            } else if (pt.length == 2) {

                ppoint = TerminalOrigami.find3dImageOf(Double.parseDouble(pt[0]), Double.parseDouble(pt[1]));

            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void planenormal1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] nv = args[1].split(" ");

            if (nv.length == 3) {

                pnormal = new double[]{Double.parseDouble(nv[0]),
                    Double.parseDouble(nv[1]), Double.parseDouble(nv[2])};

            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void target1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] maghely = args[0].split(" ");

            if (maghely.length == 2) {

                tracker = new double[]{Double.parseDouble(maghely[0]),
                    Double.parseDouble(maghely[1])};
            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void angle1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] szog = args[0].split(" ");

            if (szog.length == 1) {

                phi = Integer.parseInt(szog[0]);
            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void paper1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] bajf = args[0].split(" ");

            if (bajf.length == 4) {

                corners = new ArrayList<>(Collections.emptyList());
                corners.add(new double[]{Double.parseDouble(bajf[0]),
                    Double.parseDouble(bajf[1])});
                corners.add(new double[]{Double.parseDouble(bajf[0]),
                    Double.parseDouble(bajf[3])});
                corners.add(new double[]{Double.parseDouble(bajf[2]),
                    Double.parseDouble(bajf[1])});
                corners.add(new double[]{Double.parseDouble(bajf[2]),
                    Double.parseDouble(bajf[3])});

            } else if (bajf.length == 1) {

                switch (bajf[0]) {
                    case "square" -> papertype = Origami.PaperType.Square;
                    case "a4" -> papertype = Origami.PaperType.A4;
                    case "hexagon" -> papertype = Origami.PaperType.Hexagon;
                    case "usd" -> papertype = Origami.PaperType.Dollar;
                    case "huf" -> papertype = Origami.PaperType.Forint;
                    default -> throw OrigamiException.H009;
                }
            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void corner1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] koo = args[0].split(" ");

            if (koo.length == 2) {

                corners.add(new double[]{Double.parseDouble(koo[0]),
                    Double.parseDouble(koo[1])});
            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    /*
     * ///////////////////////////
     * EDITOR COMMANDS
     */// /////////////////////////
    private void NEW() throws Exception {

        switch (version) {
            default -> NEW1();
        }
    }

    private void ROTATE() throws Exception {

        switch (version) {
            default -> ROTATE1();
        }
    }

    private void REFLECT() throws Exception {

        switch (version) {
            default -> REFLECT1();
        }
    }

    private void CUT() throws Exception {

        switch (version) {
            default -> CUT1();
        }
    }

    private void UNDO() throws Exception {

        switch (version) {
            default -> UNDO1();
        }
    }

    private void REDO() throws Exception {

        switch (version) {
            default -> REDO1();
        }
    }

    private void NEW1() throws OrigamiException {

        historyReset();
        if (papertype == Origami.PaperType.Custom) {

            try {
                TerminalOrigami = new OrigamiGen2(corners);
            } catch (Exception ex) {
                throw OrigamiException.H001;
            }
        } else {

            TerminalOrigami = new OrigamiGen2(papertype);
        }
        paramReset();
    }

    private void ROTATE1() throws OrigamiException {

        if (ppoint != null && pnormal != null && phi != null && tracker == null) {
            TerminalOrigami.rotationFold(ppoint, pnormal, phi);

        } else if (ppoint != null && pnormal != null && phi != null
                && tracker != null) {

            TerminalOrigami.crease(ppoint, pnormal);

            int mag = TerminalOrigami.findPolygonContaining(tracker);

            TerminalOrigami.rotationFold(ppoint, pnormal, phi, mag);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void REFLECT1() throws OrigamiException {

        if (ppoint != null && pnormal != null && tracker == null) {

            TerminalOrigami.reflectionFold(ppoint, pnormal);
        } else if (ppoint != null && pnormal != null && tracker != null) {

            TerminalOrigami.crease(ppoint, pnormal);

            int mag = TerminalOrigami.findPolygonContaining(tracker);

            TerminalOrigami.reflectionFold(ppoint, pnormal, mag);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void CUT1() throws OrigamiException {

        if (ppoint != null && pnormal != null && tracker == null) {

            TerminalOrigami.mutilation(ppoint, pnormal);
        } else if (ppoint != null && pnormal != null && tracker != null) {

            TerminalOrigami.crease(ppoint, pnormal);

            int mag = TerminalOrigami.findPolygonContaining(tracker);

            TerminalOrigami.mutilation(ppoint, pnormal, mag);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void UNDO1() throws Exception {

        if (!TerminalOrigami.history().isEmpty()) {
            TerminalOrigami.undo();
        } else {
            undo(1);
        }
        paramReset();
    }

    private void REDO1() {

        TerminalOrigami.redo();
        paramReset();
    }

    /*
     * ///////////////////////////
     * TERMINAL PARAMETERS
     */// /////////////////////////
    private void version(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] ver = args[0].split(" ");

            if (ver.length == 1) {

                version = Integer.parseInt(ver[0]);
            } else {
                throw OrigamiException.H007;
            }
        }
    }

    private static void locale(String... args) throws OrigamiException {

        if (args.length == 1) {

            String[] loc = args[0].split(" ");

            if (loc.length == 2) {
                Dictionary.setLocale(new java.util.Locale(loc[0], loc[1]));
                Instructor.setLocale(new java.util.Locale(loc[0], loc[1]));
            } else {
                throw OrigamiException.H007;
            }
        }
    }

    private void filename(String... args) throws Exception {

        switch (version) {
            default -> filename1(args);
        }
    }

    private void title(String... args) throws Exception {

        switch (version) {
            default -> title1(args);
        }
    }

    private void camera(String... args) throws Exception {

        switch (version) {
            default -> camera1(args);
        }
    }

    private void color(String... args) throws Exception {

        switch (version) {
            default -> color1(args);
        }
    }
    
    private void uncolor(String... args) throws Exception {

        switch (version) {
            default -> uncolor1(args);
        }
    }

    private void filename1(String... args) throws OrigamiException {

        if (args.length == 1) {

            filename = args[0];
        } else {
            throw OrigamiException.H007;
        }
    }

    private void title1(String... args) throws OrigamiException {

        if (args.length == 1) {

            title = args[0];
        } else {
            throw OrigamiException.H007;
        }
    }

    private void camera1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 3) {

            String[] Dir = args[0].split(" ");
            String[] Xaxis = args[1].split(" ");
            String[] Yaxis = args[2].split(" ");

            double[] dir, xaxis, yaxis;
            if (Dir.length == 3) {
                dir = new double[]{Double.parseDouble(Dir[0]),
                    Double.parseDouble(Dir[1]), Double.parseDouble(Dir[2])};
            } else {
                throw OrigamiException.H007;
            }
            if (Xaxis.length == 3) {
                xaxis = new double[]{Double.parseDouble(Xaxis[0]),
                    Double.parseDouble(Xaxis[1]), Double.parseDouble(Xaxis[2])};
            } else {
                throw OrigamiException.H007;
            }
            if (Yaxis.length == 3) {
                yaxis = new double[]{Double.parseDouble(Yaxis[0]),
                    Double.parseDouble(Yaxis[1]), Double.parseDouble(Yaxis[2])};
            } else {
                throw OrigamiException.H007;
            }

            TerminalCamera.camera_dir = dir;
            TerminalCamera.axis_x = xaxis;
            TerminalCamera.axis_y = yaxis;

        } else {
            throw OrigamiException.H007;
        }
    }

    private void color1(String... args) throws NumberFormatException, OrigamiException {

        if (args.length == 1) {

            String[] Col = args[0].split(" ");

            if (Col.length == 1) {
                paper_color = Integer.parseInt(Col[0]) & 0xFFFFFF;
            } else if (Col.length == 3) {

                paper_color = ((Integer.parseInt(Col[0]) & 0xFF) << 16)
                        + ((Integer.parseInt(Col[1]) & 0xFF) << 8)
                        + (Integer.parseInt(Col[2]) & 0xFF);
            } else {
                throw OrigamiException.H007;
            }
        } else {
            throw OrigamiException.H007;
        }
    }
    
    private void uncolor1(String... args) throws OrigamiException {

        if (args.length == 0) {
            paper_color = default_paper_color;
        } else {
            throw OrigamiException.H007;
        }
    }

    /*
     * ///////////////////////////
     * TERMINAL COMMANDS
     */// /////////////////////////
    private void DIAGNOSTICS() throws Exception {

        switch (version) {
            default -> DIAGNOSTICS1();
        }
    }

    private void COMPILE() throws Exception {

        switch (version) {
            default -> COMPILE1();
        }
    }

    private void LOAD() throws Exception {

        switch (version) {
            default -> LOAD1();
        }
    }

    private void OPEN() throws Exception {

        switch (version) {
            default -> OPEN1();
        }
    }

    private void LOAD_TEXTURE() throws Exception {

        switch (version) {
            default -> LOAD_TEXTURE1();
        }
    }

    private void UNLOAD_TEXTURE() throws Exception {

        switch (version) {
            default -> UNLOAD_TEXTURE1();
        }
    }

    private void EXPORT_CTM() throws Exception {

        switch (version) {
            default -> EXPORT_CTM1();
        }
    }

    private void EXPORT_AUTOPDF() throws Exception {

        switch (version) {
            default -> EXPORT_AUTOPDF1();
        }
    }

    private void EXPORT_GIF() throws Exception {

        switch (version) {
            default -> EXPORT_GIF1();
        }
    }

    private void EXPORT_REVOLVING_GIF() throws Exception {

        switch (version) {
            default -> EXPORT_REVOLVING_GIF1();
        }
    }

    private void EXPORT_JAR() throws Exception {

        switch (version) {
            default -> EXPORT_JAR1();
        }
    }

    private void EXPORT_PNG() throws Exception {

        switch (version) {
            default -> EXPORT_PNG1();
        }
    }

    private void EXPORT_ORI() throws Exception {

        switch (version) {
            default -> EXPORT_ORI1();
        }
    }

    private void ROOT() throws Exception {

        switch (version) {
            default -> ROOT1();
        }
    }

    private void DEBUG() throws Exception {

        switch (version) {
            default -> DEBUG1();
        }
    }

    private void DIAGNOSTICS1() throws OrigamiException {

        if (access == Access.DEV) {

            System.out.println("TerminalOrigami.vertices_size == "
                    + TerminalOrigami.vertices_size());
            System.out.println("TerminalOrigami.polygons_size == "
                    + TerminalOrigami.polygons_size());
            for (int i = 0; i < TerminalOrigami.vertices_size(); i++) {

                System.out.print("planar vertex " + i + ": ");
                for (double comp : TerminalOrigami.vertices2d().get(i)) {

                    System.out.print(comp);
                    System.out.print(" ");
                }
                System.out.println();
            }
            for (int i = 0; i < TerminalOrigami.polygons_size(); i++) {

                System.out.print("polygon " + i + ": ");
                for (int vert : TerminalOrigami.polygons().get(i)) {

                    System.out.print(vert);
                    System.out.print(" ");
                }
                System.out.println();
            }
            System.out.println("TerminalOrigami.corners:");
            for (double[] pont : TerminalOrigami.corners()) {

                System.out.println(pont[0] + " "
                        + pont[1]);
            }
        } else {
            throw OrigamiException.H011;
        }
    }

    private void COMPILE1() throws Exception, java.io.IOException, java.io.FileNotFoundException, OrigamiException {

        if (access == Access.ROOT || access == Access.DEV) {

            if (filename != null) {

                try (BufferedReader br = new BufferedReader(new FileReader(
                        filename))) {

                    StringBuilder bytes = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        bytes.append(line).append(" ");
                    }

                    OrigamiScriptTerminal sandbox = new OrigamiScriptTerminal(
                            OrigamiScriptTerminal.Access.USER, filename);
                    sandbox.execute(bytes.toString());
                }
            } else {
                throw OrigamiException.H010;
            }

            paramReset();
        } else {
            throw OrigamiException.H011;
        }
    }

    private void LOAD1() throws Exception, java.io.IOException, java.io.FileNotFoundException, OrigamiException {

        if (access == Access.ROOT || access == Access.DEV) {

            historyReset();
            history.add("version 1 filename [" + filename + "] load");
            if (filename != null) {

                try (BufferedReader br = new BufferedReader(new FileReader(
                        filename))) {

                    StringBuilder bajtok = new StringBuilder();
                    String sor;
                    while ((sor = br.readLine()) != null) {
                        bajtok.append(sor).append((char) 10);
                    }

                    execute(bajtok.toString(), Access.USER);
                }
            } else {
                throw OrigamiException.H010;
            }

            paramReset();
        } else {
            throw OrigamiException.H011;
        }
    }

    private void OPEN1() throws Exception, java.io.FileNotFoundException, java.io.IOException, OrigamiException {

        if (access == Access.ROOT || access == Access.DEV) {

            historyReset();
            history.add("version 1 filename [" + filename + "] open");
            if (filename != null) {

                java.util.ArrayList<Byte> bytesb = new java.util.ArrayList<>();
                java.io.FileInputStream fis = new java.io.FileInputStream(filename);
                int nxb;
                while ((nxb = fis.read()) != -1) {
                    bytesb.add((byte) nxb);
                }
                fis.close();
                byte[] bytes = new byte[bytesb.size()];
                for (int i = 0; i < bytesb.size(); i++) {
                    bytes[i] = bytesb.get(i);
                }
                paper_color = default_paper_color;
                int[] rgb = { (paper_color >>> 16) & 0xFF, (paper_color >>> 8) & 0xFF, paper_color & 0xFF };
                TerminalOrigami = OrigamiIO.read_gen2(new java.io.ByteArrayInputStream(bytes), rgb);
                paper_color = rgb[0]*0x10000 + rgb[1]*0x100 + rgb[2];
            } else {
                throw OrigamiException.H010;
            }
        } else {
            throw OrigamiException.H011;
        }
    }

    private void LOAD_TEXTURE1() throws java.io.IOException, OrigamiException {

        if (filename != null) {

            paper_texture = javax.imageio.ImageIO.read(new java.io.File(filename));
            if (paper_texture.getColorModel().hasAlpha()) {
                throw OrigamiException.H013;
            }
        } else {
            throw OrigamiException.H010;
        }
    }

    private void UNLOAD_TEXTURE1() {
        paper_texture = null;
    }

    private void EXPORT_CTM1() throws Exception, OrigamiException {

        if (filename != null) {

            if (new java.io.File(filename).exists() && access != Access.ROOT && access != Access.DEV) {
                throw OrigamiException.H011;
            }
            Export.exportCTM(TerminalOrigami, filename, paper_texture);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_AUTOPDF1() throws Exception, OrigamiException {

        if (filename != null && title != null) {

            if (new java.io.File(filename).exists() && access != Access.ROOT && access != Access.DEV) {
                throw OrigamiException.H011;
            }
            Export.exportPDF(TerminalOrigami, filename, title);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_GIF1() throws Exception, OrigamiException {

        if (filename != null) {

            if (new java.io.File(filename).exists() && access != Access.ROOT && access != Access.DEV) {
                throw OrigamiException.H011;
            }
            Export.exportGIF(TerminalOrigami, TerminalCamera, paper_color, 250, 250, filename);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_REVOLVING_GIF1() throws Exception, OrigamiException {

        if (filename != null) {

            if (new java.io.File(filename).exists() && access != Access.ROOT && access != Access.DEV) {
                throw OrigamiException.H011;
            }
            Export.exportRevolvingGIF(TerminalOrigami, TerminalCamera, paper_color, 250, 250, filename);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_JAR1() throws Exception, OrigamiException {

        if (filename != null) {

            if (new java.io.File(filename).exists() && access != Access.ROOT && access != Access.DEV) {
                throw OrigamiException.H011;
            }
            int[] rgb = { (paper_color >>> 16) & 0xFF, (paper_color >>> 8) & 0xFF, paper_color & 0xFF };
            if (paper_color == default_paper_color) {
                rgb = null;
            }
            Export.exportJAR(TerminalOrigami, filename, rgb);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_PNG1() throws Exception, OrigamiException {

        if (filename != null) {

            if (new java.io.File(filename).exists() && access != Access.ROOT && access != Access.DEV) {
                throw OrigamiException.H011;
            }
            Export.exportPNG(TerminalOrigami, filename);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_ORI1() throws Exception, OrigamiException {

        if (filename != null) {

            if (new java.io.File(filename).exists() && access != Access.ROOT && access != Access.DEV) {
                throw OrigamiException.H011;
            }
            int[] rgb = { (paper_color >>> 16) & 0xFF, (paper_color >>> 8) & 0xFF, paper_color & 0xFF };
            if (paper_color == default_paper_color) {
                rgb = null;
            }
            OrigamiIO.write_gen2(TerminalOrigami, filename, rgb);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void ROOT1() {

        Object[] options = {Dictionary.getString("yes"), Dictionary.getString("no")};
        prompt = true;
        if (javax.swing.JOptionPane.showOptionDialog(null, Dictionary.getString("enter-root"), Dictionary.getString("question"), javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[1]) != javax.swing.JOptionPane.YES_OPTION) {
            prompt = false;
            return;
        }
        prompt = false;
        this.access = Access.ROOT;
    }

    private void DEBUG1() {

        Object[] options = {Dictionary.getString("yes"), Dictionary.getString("no")};
        prompt = true;
        if (javax.swing.JOptionPane.showOptionDialog(null, Dictionary.getString("enter-debug"), Dictionary.getString("question"), javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[1]) != javax.swing.JOptionPane.YES_OPTION) {
            prompt = false;
            return;
        }
        prompt = false;
        access = Access.DEV;
    }

    static public String obfuscate(CharSequence code) {

        // 1. lépés: kommentek eltávolítása
        StringBuilder result = new StringBuilder();
        boolean phys = true;
        for (int i = 0; i < code.length(); i++) {

            if (code.charAt(i) == '{') {
                phys = false;
            }
            if (phys) {
                result.append(code.charAt(i));
            }
            if (code.charAt(i) == '}') {
                phys = true;
            }
        }

        // 2. lépés: hiányzó szóközök pótlása
        result = new StringBuilder(result.toString().replace("[", " ["));
        result = new StringBuilder(result.toString().replace("]", "] "));

        // 3. lépés: tabulátorok és sortörések eltávolítása
        result = new StringBuilder(result.toString().replace((char) 9, ' '));
        result = new StringBuilder(result.toString().replace((char) 10, ' '));

        // 4. lépés: többszörös szóközök összevonása
        int tmp_hossz = -1;
        while (tmp_hossz != result.length()) {

            tmp_hossz = result.length();
            result = new StringBuilder(result.toString().replace("  ", " "));
        }

        // 5. lépés: szögletes zárójelen belüli szóközök cseréje |-ra.
        boolean param = false;
        for (int i = 0; i < result.length() - 1; i++) {

            if (result.charAt(i) == ']') {
                param = false;
            }
            if (param && result.charAt(i) == ' ') {
                result = new StringBuilder(result.substring(0, i) + "|" + result.substring(i + 1));
            }
            if (result.charAt(i) == '[') {
                param = true;
            }
        }

        return result.toString();
    }

    private boolean prompt;

    @SuppressWarnings("deprecation")
    public void execute(String code) throws Exception, java.awt.HeadlessException, InterruptedException {

        history.add(code);
        code = obfuscate(code);
        final String[] szavak = code.split(" ");

        final Exception[] unreportedException = {null};
        Thread exec = new Thread(() -> {

            try {
                for (int i = 0; i < szavak.length; i++) {
                    if (!(szavak[i].contains("[") || szavak[i].contains("]"))) {
                        if (Commands.containsKey(szavak[i])) {
                            Commands.get(szavak[i]).execute();
                        }

                        if (Params.containsKey(szavak[i])) {

                            ArrayList<String> argumentumok = new ArrayList<>();
                            for (int ii = i + 1; ii < szavak.length; ii++) {
                                if (Commands.containsKey(szavak[ii])
                                        || Params.containsKey(szavak[ii])) {
                                    break;
                                }
                                if (!szavak[ii].equals("")) {
                                    argumentumok.add(szavak[ii].replace("[", "")
                                            .replace("]", "").replace("|", " "));
                                }
                            }

                            String[] tombarg = new String[argumentumok.size()];
                            for (int iii = 0; iii < argumentumok.size(); iii++) {
                                tombarg[iii] = argumentumok.get(iii);
                            }

                            Params.get(szavak[i]).execute(tombarg);
                        }
                    }
                }
            } catch (Exception exc) {

                history.subList(history.size() - 1, history.size()).clear();
                unreportedException[0] = exc;
            }

        });

        long timeout = System.currentTimeMillis() + 10000;
        String timeout_notif = Dictionary.getString("timeout");
        exec.start();
        while (exec.isAlive()) {
            if (System.currentTimeMillis() > timeout) {
                if (prompt) {
                    break;
                }
                Object[] options = {Dictionary.getString("timeout_stop"), Dictionary.getString("timeout_wait")};
                if (javax.swing.JOptionPane.showOptionDialog(null, timeout_notif, Dictionary.getString("question"), javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.QUESTION_MESSAGE, null, options, options[0]) == javax.swing.JOptionPane.YES_OPTION) {
                    exec.stop();
                    break;
                } else {
                    timeout = System.currentTimeMillis() + 30000;
                    timeout_notif = Dictionary.getString("+30timeout");
                }
            } else {
                //estimation of the longest time we feel instantaneous
                Thread.sleep(80);
            }
        }

        if (unreportedException[0] != null) {
            throw unreportedException[0];
        }
    }

    public void execute(String code, Access access) throws Exception {

        Access tmp = this.access;
        this.access = access;
        try {
            execute(code);
        } catch (Exception ex) {
            this.access = tmp;
            throw ex;
        }
        this.access = tmp;
    }

    @SuppressWarnings("unchecked")
    private void execute() throws Exception {

        Iterable<String> tmp = (ArrayList<String>) history.clone();
        totalReset();
        for (String p : tmp) {
            execute(p);
        }
    }

    private void undo(int steps) throws Exception {

        if (history.size() >= steps) {

            history.subList(history.size() - steps, history.size()).clear();
            Access tmp = this.access;
            this.access = Access.DEV;
            execute();
            this.access = tmp;
        }
    }
}