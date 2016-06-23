package com.odm.utility;

import org.springframework.stereotype.Component;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

/**
 * Created by mohamed on 6/22/16.
 */
@Component
public class PropertyFileUtility {
    public void saveToDefaultPropertyFile(Properties props,String headerComment) throws IOException {
        // get or create the file
        File f = new File("application.properties");
        OutputStream out = new FileOutputStream( f );
        // write into it
        DefaultPropertiesPersister p = new DefaultPropertiesPersister();
        p.store(props, out, headerComment);
    }
}
