/***************************************************************************
 *   Copyright (C) 2007, 2009 by Kevin Krammer <kevin.krammer@gmx.at>      *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU Library General Public License as       *
 *   published by the Free Software Foundation; either version 2.1 of the  *
 *   License, or (at your option) any later version.                       *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU Library General Public     *
 *   License along with this program; if not, write to the                 *
 *   Free Software Foundation, Inc.,                                       *
 *   51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.         *
 ***************************************************************************/

package org.freedesktop.xdg;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.StringTokenizer;

/**
 * @author Kevin Krammer, <kevin.krammer@gmx.at>
 */
public class BaseDirs {
    public enum Resource {
        CONFIG( "XDG_CONFIG_HOME", ".config",
                "XDG_CONFIG_DIRS", "/etc/xdg" ),

            DATA( "XDG_DATA_HOME", ".local/share",
                  "XDG_DATA_DIRS", "/usr/local/share:/usr/share" );

        Resource( String userPathProperty, String userPathDefault,
                  String systemPathsProperty, String systemPathsDefault )
            {
                mUserPathProperty = userPathProperty;
                mUserPathDefault  = userPathDefault;

                mSystemPathsProperty = systemPathsProperty;
                mSystemPathsDefault  = systemPathsDefault;
            }

        public String getUserPathProperty() { return mUserPathProperty; }

        public String getUserPathDefault() { return mUserPathDefault; }

        public String getSystemPathsProperty() { return mSystemPathsProperty; }

        public String getSystemPathsDefault() { return mSystemPathsDefault; }

        private final String mUserPathProperty;
        private final String mUserPathDefault;
        private final String mSystemPathsProperty;
        private final String mSystemPathsDefault;
    }

    public BaseDirs()
    {
    }

    public String getUserPath( Resource resource )
    {
        if ( mUserPaths == null ) mUserPaths = new String[ Resource.values().length ];

        if ( mUserPaths[ resource.ordinal() ] == null ) {
            cacheHomePath( resource );
        }

        return mUserPaths[ resource.ordinal() ];
    }

    public String[] getSystemPaths( Resource resource )
    {
        if (mSystemPaths == null ) {
            mSystemPaths = new String[ Resource.values().length ][];
        }

        if ( mSystemPaths[ resource.ordinal() ] == null ) {
            cacheSystemPaths( resource );
        }

        return mSystemPaths[ resource.ordinal() ];
    }

    public File findResourceFile( Resource resource, String relPath )
        throws FileNotFoundException
    {
        File file = new File( getUserPath( resource ), relPath );
        if ( file.exists() && file.isFile() ) return file;

        String[] paths = getSystemPaths( resource );
        for ( int i = 0; i < paths.length; ++i ) {
            file = new File( paths[ i ], relPath );
            if ( file.exists() && file.isFile() ) return file;
        }

        throw new FileNotFoundException();
    }

    public File findResourceDirectory( Resource resource, String relPath )
        throws FileNotFoundException
    {
        File file = new File( getUserPath( resource ), relPath );
        if ( file.exists() && file.isDirectory() ) return file;

        String[] paths = getSystemPaths( resource );
        for ( int i = 0; i < paths.length; ++i ) {
            file = new File( paths[ i ], relPath );
            if ( file.exists() && file.isDirectory() ) return file;
        }

        throw new FileNotFoundException();
    }

    public File getSaveDirectory( Resource resource, String relPath )
    {
        File file = new File( getUserPath( resource ), relPath );

        if ( file.exists() ) {
            if ( !file.isDirectory() ) return null; // exception?
        } else if ( !file.mkdirs() ) return null; // exception?

        return file;
    }

    private void cacheHomePath( Resource resource )
    {
        String property = System.getenv( resource.getUserPathProperty() );
        if ( property == null || property.length() == 0 ) {
            property = resource.getUserPathDefault();
        }

        File file = new File( property );
        if ( !file.isAbsolute() ) {
            file = new File( System.getProperty( "user.home" ), property );

            mUserPaths[ resource.ordinal() ] = file.getAbsolutePath();
        } else {
            mUserPaths[ resource.ordinal() ] = property;
        }
    }

    private void cacheSystemPaths( Resource resource )
    {
        String property = System.getenv( resource.getSystemPathsProperty() );
        if ( property == null || property.length() == 0 ) {
            property = resource.getSystemPathsDefault();
        }

        StringTokenizer tokenizer = new StringTokenizer( property, ":" );

        mSystemPaths[ resource.ordinal() ] = new String[ tokenizer.countTokens() ];

        for ( int i = 0; tokenizer.hasMoreTokens(); ++i ) {
            mSystemPaths[ resource.ordinal() ][ i ] = tokenizer.nextToken();
        }
    }

    private String[]   mUserPaths   = null;
    private String[][] mSystemPaths = null;
}
