// This file is part of Origami Editor 3D.
// Copyright (C) 2013 Bágyoni Attila <bagyoni.attila@gmail.com>
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

import origamieditor3d.Export;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Attila Bágyoni <bagyoni.attila@gmail.com>
 * @since 2013-09-29
 */
public class OrigamiScriptTerminal {

    public OrigamiScriptTerminal(Access szint) {

        this.access = szint;
        this.version = maxVersion;
        this.TerminalCamera = new Camera(0, 0, 1);
        this.corners = new ArrayList<>();
        this.papertype = Origami.PaperType.Custom;
        this.history = new ArrayList<>();
        this.filename = null;
        this.ppoint = null;
        this.pnormal = null;
        this.tracker = null;
        this.phi = null;
        this.title = null;
    }

    public OrigamiScriptTerminal(Access szint, String fajlnev) {

        this(szint);
        this.filename = fajlnev;
        this.TerminalCamera = new Camera(0, 0, 1);
    }
    final private Integer maxVersion = 1;
    private Access access;
    //
    //betöltött terminál mezők
    private Integer version = 1;
    //
    //eltárolt terminál mezők
    private ArrayList<String> history;

    public ArrayList<String> history() {

        return history;
    }
    private String filename;
    //
    //betöltött szerkesztési mezők
    private double[] ppoint;
    private double[] pnormal;
    private double[] tracker;
    private Integer phi;
    private ArrayList<double[]> corners;
    private Origami.PaperType papertype;
    private String title;
    //
    //eltárolt szerkesztési mezők
    public Origami TerminalOrigami;
    public Camera TerminalCamera;

    private void paramReset() {

        this.ppoint = null;
        this.pnormal = null;
        this.tracker = null;
        this.phi = null;
        this.title = null;
        this.corners = new ArrayList<>(Arrays.asList(new double[][]{}));
        this.papertype = Origami.PaperType.Custom;
    }

    private void totalReset() {

        paramReset();
        this.version = maxVersion;
        this.history.clear();

        this.TerminalOrigami.undo(this.TerminalOrigami.history.size());
        this.TerminalCamera = new Camera(0, 0, 1);
    }

    private void historyReset() {
        this.history.clear();
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

        abstract void execute(String... args) throws Exception;
    }
    final private HashMap<String, Command> Commands = new HashMap<>();
    final private HashMap<String, Command> Params = new HashMap<>();

    {
        Params.put("plane", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                plane(args);
            }
        });

        Params.put("planethrough", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                planethrough(args);
            }
        });

        Params.put("angle-bisector", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                angle_bisector(args);
            }
        });

        Params.put("planepoint", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                planepoint(args);
            }
        });

        Params.put("planenormal", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                planenormal(args);
            }
        });

        Params.put("target", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                target(args);
            }
        });

        Params.put("angle", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                angle(args);
            }
        });

        Params.put("paper", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                paper(args);
            }
        });

        Params.put("corner", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                corner(args);
            }
        });

        Params.put("version", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                version(args);
            }
        });

        Params.put("filename", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                filename(args);
            }
        });

        Params.put("title", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                title(args);
            }
        });
    }

    {
        Commands.put("new", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                NEW();
            }
        });

        Commands.put("rotate", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                ROTATE();
            }
        });

        Commands.put("reflect", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                REFLECT();
            }
        });

        Commands.put("undo", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                UNDO();
            }
        });

        Commands.put("redo", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                REDO();
            }
        });

        Commands.put("diagnostics", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                DIAGNOSTICS();
            }
        });

        Commands.put("compile", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                COMPILE();
            }
        });

        Commands.put("load", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                LOAD();
            }
        });

        Commands.put("open", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                OPEN();
            }
        });

        Commands.put("export-ctm", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                EXPORT_CTM();
            }
        });

        Commands.put("export-autopdf", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                EXPORT_AUTOPDF();
            }
        });

        Commands.put("export-ori", new Command() {
            @Override
            public void execute(String... args) throws Exception {
                EXPORT_ORI();
            }
        });
    }

    /* ///////////////////////////
     * SZERKESZTÉSI PARAMÉTEREK
     *////////////////////////////
    private void plane(String... args) throws Exception {

        switch (version) {

            case 1:
                plane1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void planethrough(String... args) throws Exception {

        switch (version) {

            case 1:
                planethrough1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void angle_bisector(String... args) throws Exception {

        switch (version) {

            case 1:
                angle_bisector1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void planepoint(String... args) throws Exception {

        switch (version) {

            case 1:
                planepoint1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void planenormal(String... args) throws Exception {

        switch (version) {

            case 1:
                planenormal1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void target(String... args) throws Exception {

        switch (version) {

            case 1:
                target1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void angle(String... args) throws Exception {

        switch (version) {

            case 1:
                angle1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void paper(String... args) throws Exception {

        switch (version) {

            case 1:
                paper1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void corner(String... args) throws Exception {

        switch (version) {

            case 1:
                corner1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void plane1(String... args) throws Exception {

        if (args.length == 2) {

            String[] pt = args[0].split(" ");
            String[] nv = args[1].split(" ");

            if (pt.length == 3 && nv.length == 3) {

                ppoint = new double[]{
                    Double.parseDouble(pt[0]),
                    Double.parseDouble(pt[1]),
                    Double.parseDouble(pt[2])
                };

                pnormal = new double[]{
                    Double.parseDouble(nv[0]),
                    Double.parseDouble(nv[1]),
                    Double.parseDouble(nv[2])
                };

            } else if (pt.length == 2 && nv.length == 3) {

                ppoint = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pt[0]),
                            Double.parseDouble(pt[1])
                        }).trackPoint();

                pnormal = new double[]{
                    Double.parseDouble(nv[0]),
                    Double.parseDouble(nv[1]),
                    Double.parseDouble(nv[2])
                };

            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void planethrough1(String... args) throws Exception {

        if (args.length == 3) {

            String[] pont1 = args[0].split(" ");
            String[] pont2 = args[1].split(" ");
            String[] pont3 = args[2].split(" ");

            double[] pt1, pt2, pt3;

            if (pont1.length == 3) {

                pt1 = new double[]{
                    Double.parseDouble(pont1[0]),
                    Double.parseDouble(pont1[1]),
                    Double.parseDouble(pont1[2])
                };
            } else if (pont1.length == 2) {

                pt1 = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pont1[0]),
                            Double.parseDouble(pont1[1])
                        }).trackPoint();
            } else {
                throw OrigamiException.H007;
            }

            if (pont2.length == 3) {

                pt2 = new double[]{
                    Double.parseDouble(pont2[0]),
                    Double.parseDouble(pont2[1]),
                    Double.parseDouble(pont2[2])
                };
            } else if (pont2.length == 2) {

                pt2 = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pont2[0]),
                            Double.parseDouble(pont2[1])
                        }).trackPoint();
            } else {
                throw OrigamiException.H007;
            }

            if (pont3.length == 3) {

                pt3 = new double[]{
                    Double.parseDouble(pont3[0]),
                    Double.parseDouble(pont3[1]),
                    Double.parseDouble(pont3[2])
                };
            } else if (pont3.length == 2) {

                pt3 = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pont3[0]),
                            Double.parseDouble(pont3[1])
                        }).trackPoint();
            } else {
                throw OrigamiException.H007;
            }

            if (Origami.vector_length(Origami.vector_product(Origami.vector(pt2, pt1), Origami.vector(pt3, pt1))) != 0d) {

                ppoint = pt1;
                pnormal = Origami.vector_product(Origami.vector(pt2, pt1), Origami.vector(pt3, pt1));
            } else {
                throw OrigamiException.H008;
            }
        } else {
            throw OrigamiException.H007;
        }
    }

    private void angle_bisector1(String... args) throws Exception {

        if (args.length == 3) {

            String[] pont1 = args[0].split(" ");
            String[] pont2 = args[1].split(" ");
            String[] pont3 = args[2].split(" ");

            double[] pt1, pt2, pt3;

            if (pont1.length == 3) {

                pt1 = new double[]{
                    Double.parseDouble(pont1[0]),
                    Double.parseDouble(pont1[1]),
                    Double.parseDouble(pont1[2])
                };
            } else if (pont1.length == 2) {

                pt1 = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pont1[0]),
                            Double.parseDouble(pont1[1])
                        }).trackPoint();
            } else {
                throw OrigamiException.H007;
            }

            if (pont2.length == 3) {

                pt2 = new double[]{
                    Double.parseDouble(pont2[0]),
                    Double.parseDouble(pont2[1]),
                    Double.parseDouble(pont2[2])
                };
            } else if (pont2.length == 2) {

                pt2 = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pont2[0]),
                            Double.parseDouble(pont2[1])
                        }).trackPoint();
            } else {
                throw OrigamiException.H007;
            }

            if (pont3.length == 3) {

                pt3 = new double[]{
                    Double.parseDouble(pont3[0]),
                    Double.parseDouble(pont3[1]),
                    Double.parseDouble(pont3[2])
                };
            } else if (pont3.length == 2) {

                pt3 = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pont3[0]),
                            Double.parseDouble(pont3[1])
                        }).trackPoint();
            } else {
                throw OrigamiException.H007;
            }
            ppoint = pt2;
            pnormal = Origami.vector(Origami.length_to_100(Origami.vector(pt1, pt2)), Origami.length_to_100(Origami.vector(pt3, pt2)));

            if (Origami.vector_length(pnormal) == 0.) {
                throw OrigamiException.H012;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void planepoint1(String... args) throws Exception {

        if (args.length == 1) {

            String[] pt = args[0].split(" ");

            if (pt.length == 3) {

                ppoint = new double[]{
                    Double.parseDouble(pt[0]),
                    Double.parseDouble(pt[1]),
                    Double.parseDouble(pt[2])
                };

            } else if (pt.length == 2) {

                ppoint = new TrackedOrigami(
                        TerminalOrigami,
                        new double[]{
                            Double.parseDouble(pt[0]),
                            Double.parseDouble(pt[1])
                        }).trackPoint();

            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void planenormal1(String... args) throws Exception {

        if (args.length == 1) {

            String[] nv = args[1].split(" ");

            if (nv.length == 3) {

                pnormal = new double[]{
                    Double.parseDouble(nv[0]),
                    Double.parseDouble(nv[1]),
                    Double.parseDouble(nv[2])
                };

            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void target1(String... args) throws Exception {

        if (args.length == 1) {

            String[] maghely = args[0].split(" ");

            if (maghely.length == 2) {

                tracker = new double[]{
                    Double.parseDouble(maghely[0]),
                    Double.parseDouble(maghely[1])
                };
            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void angle1(String... args) throws Exception {

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

    private void paper1(String... args) throws Exception {

        if (args.length == 1) {

            String[] bajf = args[0].split(" ");

            if (bajf.length == 4) {

                corners = new ArrayList<>(Arrays.asList(new double[][]{}));
                corners.add(new double[]{
                    Double.parseDouble(bajf[0]),
                    Double.parseDouble(bajf[1])
                });
                corners.add(new double[]{
                    Double.parseDouble(bajf[0]),
                    Double.parseDouble(bajf[3])
                });
                corners.add(new double[]{
                    Double.parseDouble(bajf[2]),
                    Double.parseDouble(bajf[1])
                });
                corners.add(new double[]{
                    Double.parseDouble(bajf[2]),
                    Double.parseDouble(bajf[3])
                });

            } else if (bajf.length == 1) {

                switch (bajf[0]) {

                    case "square":
                        papertype = Origami.PaperType.Square;
                        break;
                    case "a4":
                        papertype = Origami.PaperType.A4;
                        break;
                    case "hexagon":
                        papertype = Origami.PaperType.Hexagon;
                        break;
                    case "usd":
                        papertype = Origami.PaperType.Dollar;
                        break;
                    case "huf":
                        papertype = Origami.PaperType.Forint;
                        break;
                    default:
                        throw OrigamiException.H009;
                }
            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    private void corner1(String... args) throws Exception {

        if (args.length == 1) {

            String[] koo = args[0].split(" ");

            if (koo.length == 2) {

                corners.add(new double[]{
                    Double.parseDouble(koo[0]),
                    Double.parseDouble(koo[1])
                });
            } else {
                throw OrigamiException.H007;
            }

        } else {
            throw OrigamiException.H007;
        }
    }

    /* ///////////////////////////
     * SZERKESZTÉSI PARANCSOK
     *////////////////////////////
    private void NEW() throws Exception {

        switch (version) {

            case 1:
                NEW1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void ROTATE() throws Exception {

        switch (version) {

            case 1:
                ROTATE1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void REFLECT() throws Exception {

        switch (version) {

            case 1:
                REFLECT1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void UNDO() throws Exception {

        switch (version) {

            case 1:
                UNDO1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void REDO() throws Exception {

        switch (version) {

            case 1:
                REDO1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void NEW1() throws Exception {

        historyReset();
        if (papertype == Origami.PaperType.Custom) {

            try {
                TerminalOrigami = new Origami(corners);
            } catch (Exception ex) {
                throw OrigamiException.H001;
            }
            paramReset();
        } else {

            TerminalOrigami = new Origami(papertype);
            paramReset();
        }
    }

    private void ROTATE1() throws Exception {

        if (ppoint != null && pnormal != null && phi != null && tracker == null) {
            if (TerminalOrigami.rotationFold(ppoint, pnormal, phi) == 1) {
                undo(1);
            }
        } else if (ppoint != null && pnormal != null && phi != null && tracker != null) {

            TerminalOrigami.crease(ppoint, pnormal);

            int mag = new TrackedOrigami(
                    TerminalOrigami,
                    tracker).trackPolygon();

            TerminalOrigami.rotationFold(ppoint, pnormal, phi, mag);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void REFLECT1() throws Exception {

        if (ppoint != null && pnormal != null && tracker == null) {

            TerminalOrigami.reflectionFold(ppoint, pnormal);
        } else if (ppoint != null && pnormal != null && tracker != null) {

            TerminalOrigami.crease(ppoint, pnormal);

            int mag = new TrackedOrigami(
                    TerminalOrigami,
                    tracker).trackPolygon();

            TerminalOrigami.reflectionFold(ppoint, pnormal, mag);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void UNDO1() throws Exception {

        if (TerminalOrigami.history.size() > 0) {
            TerminalOrigami.undo();
        } else {
            undo(1);
        }
        paramReset();
    }

    private void REDO1() throws Exception {

        TerminalOrigami.redo();
        paramReset();
    }

    /* ///////////////////////////
     * TERMINÁL PARAMÉTEREK
     *////////////////////////////
    private void version(String... args) throws Exception {

        if (args.length == 1) {

            String[] ver = args[0].split(" ");

            if (ver.length == 1) {

                version = Integer.parseInt(ver[0]);
            } else {
                throw OrigamiException.H007;
            }
        }
    }

    private void filename(String... args) throws Exception {

        switch (version) {

            case 1:
                filename1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void title(String... args) throws Exception {

        switch (version) {

            case 1:
                title1(args);
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void filename1(String... args) throws Exception {

        if (this.access == Access.ROOT || this.access == Access.DEV) {

            if (args.length == 1) {

                filename = args[0];
            } else {
                throw OrigamiException.H007;
            }
        } else {
            throw OrigamiException.H011;
        }
    }

    private void title1(String... args) throws Exception {

        if (args.length == 1) {

            title = args[0];
        } else {
            throw OrigamiException.H007;
        }
    }

    /* ///////////////////////////
     * TERMINÁL PARANCSOK
     *////////////////////////////
    private void DIAGNOSTICS() throws Exception {

        switch (version) {

            case 1:
                DIAGNOSTICS1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void COMPILE() throws Exception {

        switch (version) {

            case 1:
                COMPILE1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void LOAD() throws Exception {

        switch (version) {

            case 1:
                LOAD1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void OPEN() throws Exception {

        switch (version) {

            case 1:
                OPEN1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void EXPORT_CTM() throws Exception {

        switch (version) {

            case 1:
                EXPORT_CTM1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void EXPORT_AUTOPDF() throws Exception {

        switch (version) {

            case 1:
                EXPORT_AUTOPDF1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void EXPORT_ORI() throws Exception {

        switch (version) {

            case 1:
                EXPORT_ORI1();
                break;

            default:
                throw OrigamiException.H006;
        }
    }

    private void DIAGNOSTICS1() throws Exception {

        if (this.access != Access.DEV) {

            System.out.println("TerminalOrigami.vertices_size == " + Integer.toString(TerminalOrigami.vertices_size()));
            System.out.println("TerminalOrigami.polygons_size == " + Integer.toString(TerminalOrigami.polygons_size()));
            for (int i=0; i<TerminalOrigami.polygons_size(); i++) {
            	
            	System.out.print("polygon " + i + ": ");
            	for (int vert : TerminalOrigami.polygons().get(i)) {
            		
            		System.out.print(vert);
            		System.out.print(" ");
            	}
            	System.out.println();
            }
            System.out.println("TerminalOrigami.corners:");
            for (double[] pont : TerminalOrigami.corners()) {

                System.out.println(Double.toString(pont[0]) + " " + Double.toString(pont[1]));
            }
        } else {
            throw OrigamiException.H011;
        }
    }

    private void COMPILE1() throws Exception {

        if (this.access == Access.ROOT || this.access == Access.DEV) {

            if (filename != null) {

                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

                    String bajtok = "", sor;
                    while ((sor = br.readLine()) != null) {
                        bajtok += sor + " ";
                    }

                    OrigamiScriptTerminal homokozo = new OrigamiScriptTerminal(OrigamiScriptTerminal.Access.USER, filename);
                    homokozo.execute(bajtok);
                }
            } else {
                throw OrigamiException.H010;
            }

            paramReset();
        } else {
            throw OrigamiException.H011;
        }
    }

    private void LOAD1() throws Exception {

        if (this.access == Access.ROOT || this.access == Access.DEV) {

            historyReset();
            history.add("version 1 filename [" + filename + "] load");
            if (filename != null) {

                try (BufferedReader br = new BufferedReader(new FileReader(filename))) {

                    String bajtok = "", sor;
                    while ((sor = br.readLine()) != null) {
                        bajtok += sor + (char) 10;
                    }

                    this.execute(bajtok, Access.USER);
                }
            } else {
                throw OrigamiException.H010;
            }

            paramReset();
        } else {
            throw OrigamiException.H011;
        }
    }

    private void OPEN1() throws Exception {

        if (this.access == Access.ROOT || this.access == Access.DEV) {

            historyReset();
            history.add("version 1 filename [" + filename + "] open");
            if (filename != null) {

                this.TerminalOrigami = OrigamiIO.read2(filename);
            } else {
                throw OrigamiException.H010;
            }
        } else {
            throw OrigamiException.H011;
        }
    }

    private void EXPORT_CTM1() throws Exception {

        if (filename != null) {

            Export.exportCTM(TerminalOrigami, filename);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_AUTOPDF1() throws Exception {

        if (filename != null && title != null) {

            Export.exportPDF(TerminalOrigami, filename, title);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    private void EXPORT_ORI1() throws Exception {

        if (filename != null) {

            OrigamiIO.write(TerminalOrigami, filename);
        } else {
            throw OrigamiException.H010;
        }

        paramReset();
    }

    static public String obfuscate(String code) {

        //1. lépés: kommentek eltávolítása
        String result = "";
        boolean phys = true;
        for (int i = 0; i < code.length(); i++) {

            if (code.charAt(i) == '{') {
                phys = false;
            }
            if (phys) {
                result += code.charAt(i);
            }
            if (code.charAt(i) == '}') {
                phys = true;
            }
        }

        //2. lépés: hiányzó szóközök pótlása
        result = result.replace("[", " [");
        result = result.replace("]", "] ");

        //3. lépés: tabulátorok és sortörések eltávolítása
        result = result.replace((char) 9, ' ');
        result = result.replace((char) 10, ' ');

        //4. lépés: többszörös szóközök összevonása
        int tmp_hossz = -1;
        while (tmp_hossz != result.length()) {

            tmp_hossz = result.length();
            result = result.replace("  ", " ");
        }

        //5. lépés: szögletes zárójelen belüli szóközök cseréje _-ra.
        boolean param = false;
        for (int i = 0; i < result.length() - 1; i++) {

            if (result.charAt(i) == ']') {
                param = false;
            }
            if (param && result.charAt(i) == ' ') {
                result = result.substring(0, i) + "_" + result.substring(i + 1);
            }
            if (result.charAt(i) == '[') {
                param = true;
            }
        }

        return result;
    }

    public void execute(String code) throws Exception {

        history.add(code);
        code = obfuscate(code);
        String[] szavak = code.split(" ");

        try {

            for (int i = 0; i < szavak.length; i++) {

                if (!(szavak[i].contains("[") || szavak[i].contains("]"))) {

                    if (Commands.containsKey(szavak[i])) {

                        Commands.get(szavak[i]).execute();
                    }

                    if (Params.containsKey(szavak[i])) {

                        ArrayList<String> argumentumok = new ArrayList<>();
                        for (int ii = i + 1; ii < szavak.length; ii++) {

                            if (Commands.containsKey(szavak[ii]) || Params.containsKey(szavak[ii])) {
                                break;
                            }

                            if (!szavak[ii].equals("")) {
                                argumentumok.add(szavak[ii].replace("[", "").replace("]", "").replace("_", " "));
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

            undo(1);
            throw exc;
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

        ArrayList<String> tmp = (ArrayList<String>) history.clone();
        totalReset();
        for (String p : tmp) {

            execute(p);
        }
    }

    private void undo(int steps) throws Exception {

        if (history.size() >= steps) {

            history.subList(history.size() - steps, history.size()).clear();
            execute();
        }
    }
}
