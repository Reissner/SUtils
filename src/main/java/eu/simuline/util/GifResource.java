package eu.simuline.util;

import javax.swing.ImageIcon;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.Map;
import java.util.HashMap;
import org.javalobby.icons20x20.Open;
import org.javalobby.icons20x20.Hammer;

/**
 * Provides a single method only, {@link #getIcon(Class)}, 
 * which returns the icon associated with the given class. 
 * The class must be a subclass of {@link GifResource} as is {@link Open}. 
 * That class is also an example for using {@link GifResource}s: 
 * Just derive a class from {@link GifResource} 
 * and put it into a package 
 * (<code>org.javalobby.icons16x16</code> in this case) 
 * pointing to the according gif-image 
 * which can be found in this case 
 * in <code>src/main/resources/org/javalobby/icons16x16/Open.gif</code>. 
 *
 * Created: Sun Jun  4 20:50:12 2006
 *
 * @author <a href="mailto:ernst@">Ernst Reissner</a>
 * @version 1.0
 */
public class GifResource {

    /* -------------------------------------------------------------------- *
     * private class constants.                                             *
     * -------------------------------------------------------------------- */

    /**
     * The separator in urls. 
     * Note that this is unified unlike file separators 
     * which depend on the operating system. 
     */
    private final static String URL_SEP = "/";

    /**
     * The ending of a java class file. 
     */
    private final static String CLASS_END = "class";

    /**
     * The ending of a gif file. 
     */
    private final static String GIF_END = "gif";

    /**
     * <code>target/classes/</code>: the directory of the classfiles 
     * within the simuline-developing environment. 
     * **** bad: path is hardcoded **** 
     */
    private final static String CLASS    = URL_SEP + "target" 
	+ URL_SEP  + "classes" + URL_SEP;

    /**
     * <code>src/main/resource/</code>: the directory of the resources
     * within simuline-developing environment. 
     * **** bad: path is hardcoded **** 
     */
    private final static String RESOURCE = 
	URL_SEP + "src/main/resources" + URL_SEP;// **** URL_SEP and /

    /**
     * A cache for gif-files represented by GifResources. 
     */
    private final static Map<Class<?>,ImageIcon> gifs = 
	new HashMap<Class<?>,ImageIcon>();

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    //private Icon icon;

    /* -------------------------------------------------------------------- *
     * constructor.                                                         *
     * -------------------------------------------------------------------- */

    /*
     * Creates a new <code>GifResource</code> instance.
     * To this end **** is this constructor really needed???? **** 
     * <ul>
     * <li>
     * converts the classname into the name of the corresponding file, 
     * <li>
     * replaces class files directory by resource files directory, 
     * <li>
     * adds the GIF-ending, 
     * <li>
     * forms the corresponding url and finally 
     * <li>
     * read the so determined gif file into the icon {@link #icon}. 
     * </ul>
     */
/*
    protected GifResource() {
	String path = this.getClass().getName()
	    .replace('.','/')+"."+CLASS_END;
	
	//***NO PURE JAVA
	URL url = ClassLoader.getSystemResource(path);
	//System.out.println("url: "+url);
	path = url.toString().replace(CLASS_END,GIF_END);
	//System.out.println("path2: "+path);
	path = path.replace(CLASS,RESOURCE); /// **** a little weak
	//System.out.println("path3: "+path);
	try {
	    url = new URL(path);
	} catch (MalformedURLException e) {
	    throw new UnsatisfiedLinkError// NOPMD
		("Resource not found: "+path);
	}
	this.icon = new ImageIcon(url);
    }
*/
    /* -------------------------------------------------------------------- *
     * methods.                                                             *
     * -------------------------------------------------------------------- */
/*
    // **** really needed? 
    public Icon getIcon() {
	return this.icon;
    }

*/
    /**
     * Converts a GifResource class into the corresponding icon. 
     * This is done in the following steps: 
     * If the image is cached in {@link #gifs}, take this one. 
     * Else load it into {@link #gifs}as described below 
     * before taking it from {@link #gifs}. 
     * <p>
     * Loading an image consists in 
     * loading the class-file associated with the image, 
     * determining the according gif-file 
     * and creating the according ImageIcon. 
     * 
     * @param res
     *    a subclass of GifResource. 
     * @return
     *    the icon determined by the given class. 
     *    <p>
     *    CAUTION: Note that an icon is returned 
     *    even if there is no according gif-image. 
     *    This image can be identified by width <code>-1</code> 
     *    which is an undocumented property. 
     */
    public static ImageIcon getIcon(Class<? extends GifResource> res) {
	ImageIcon ret = gifs.get(res);
	if (ret == null) {
	    // Here the icon is not yet loaded. 

	    // transform class into path of class file name... 
	    String path = res.getName().replace('.','/') + "." + CLASS_END;
	    //***NO PURE JAVA
	    // ... and further into an URL (ensuring that this class exists) 
	    URL url = ClassLoader.getSystemResource(path);
	    assert url != null;// because class exists 

	    // The class URL into the gif-image url 
	    path = url.toString().replaceAll(CLASS_END + "\\z", GIF_END);
	    //System.out.println("path2: "+path);
	    path = path.replace(CLASS, RESOURCE); /// **** a little weak
	    //System.out.println("path3: "+path);
	    try {
		url = new URL(path);
	    } catch (MalformedURLException e) {
		throw new UnsatisfiedLinkError// NOPMD
		    ("Resource not found: " + path);
	    }

	    // **** the following works even 
	    // if the url does not point to any file.
	    gifs.put(res, new ImageIcon(url));
	    ret = gifs.get(res);
	}
	assert ret == gifs.get(res);

	return ret;
    }

    public final static void main(String[] args) {
	ImageIcon icon = getIcon(Hammer.class);
	System.out.println("icon: "+icon.getImage());
	System.out.println("icon: "+icon.getIconWidth());
	
    }

}
