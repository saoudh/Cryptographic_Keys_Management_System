package crypto;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * 
 * Dieser ContextListener ist dafür da um den Filepath zu bestimmen für das
 * abspeichern der Key-Dateien
 *
 */
@WebListener
public class FileLocationContextListener implements ServletContextListener {

	/**
	 * Default constructor.
	 */
	public FileLocationContextListener() {
		// TODO Auto-generated constructor stub
	}

	public void contextInitialized(ServletContextEvent servletContextEvent) {
		String rootPath = System.getProperty("catalina.home");
		ServletContext ctx = servletContextEvent.getServletContext();
		String relativePath = ctx.getInitParameter("tempfile.dir");
		File file = new File(rootPath + File.separator + relativePath);
		if (!file.exists())
			file.mkdirs();
		ctx.setAttribute("FILES_DIR_FILE", file);
		ctx.setAttribute("FILES_DIR", rootPath + File.separator + relativePath);
	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		// do cleanup if needed
	}

}
