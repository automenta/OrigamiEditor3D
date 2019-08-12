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
package origamieditor3d.resources;

/**
 *
 * @author bsza
 */
public class BaseFolds {
    
    public java.io.InputStream getFile(String name) {
        
        return getClass().getResourceAsStream("/res/bases/"+name);
    }
    
    public java.util.ArrayList<String> names() {
        
        java.util.ArrayList<String> namelist = new java.util.ArrayList<>();
        java.util.Scanner sc = new java.util.Scanner(getClass().getResourceAsStream("/res/bases/0-filelist"));
        while(sc.hasNextLine()) {
            namelist.add(sc.nextLine());
        }
        return namelist;
    }
}
