package eu.simuline.util;

import javax.swing.ImageIcon;

import java.net.URL;
import java.net.MalformedURLException;

import java.util.Map;
import java.util.HashMap;

/**
 * Describe class GifResource here.
 *
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
     * <code>/cls/</code>: the directory of the classfiles 
     * within simuline-developing environment. 
     */
    private final static String CLASS    = URL_SEP +"cls"      + URL_SEP;

    /**
     * <code>/resource/</code>: the directory of the classfiles 
     * within simuline-developing environment. 
     */
    private final static String RESOURCE = URL_SEP +"resource" + URL_SEP;

    /**
     * A cache for gif-files represented by GifResources. 
     */
    private final static Map<Class,ImageIcon> gifs = 
	new HashMap<Class,ImageIcon>();

    /* -------------------------------------------------------------------- *
     * fields.                                                              *
     * -------------------------------------------------------------------- */

    //private Icon icon;

    /* -------------------------------------------------------------------- *
     * constructor.                                                         *
     * -------------------------------------------------------------------- */

    /**
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
     * Note that gif's are cached within {@link #gifs}. 
     * 
     * @param res
     *    a subclass of GifResource. 
     * @return
     *    the icon determined by the given class. 
     */
    public static ImageIcon getIcon(Class<? extends GifResource> res) {
	ImageIcon ret = gifs.get(res);
	if (ret == null) {
	    String path = res.getName().replace('.','/')+"."+CLASS_END;
	
	    //System.out.println("path: "+path);
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

	    gifs.put(res,new ImageIcon(url));
	    ret = gifs.get(res);
	}

	return ret;
    }

}
