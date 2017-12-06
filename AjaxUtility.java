import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.sql.*;

public class AjaxUtility extends HttpServlet {

    private ServletContext context;    


    
    public void init(ServletConfig config) throws ServletException {
        this.context = config.getServletContext();
    }

    
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
    {
      try
      {

          HashMap<String, MovieCatalog> products = WfCache.getAllMovies();

          String action = request.getParameter("action");
          String targetId = request.getParameter("id");
          StringBuffer sb = new StringBuffer();

          if (targetId != null) {
            targetId = targetId.trim().toLowerCase();
        } else {
            context.getRequestDispatcher("/error.jsp").forward(request, response);
        }

        boolean namesAdded = false;
        if (action.equals("complete")) {

            // check if user sent empty string
            if (!targetId.equals("")) {

                Iterator it = products.keySet().iterator();

                while (it.hasNext()) {
                    String id = (String) it.next();
                    MovieCatalog product = products.get(id);

                    if (product.getName().toLowerCase().startsWith(targetId)) {

                        sb.append("<product>");
                        sb.append("<productid>" + product.getId() + "</productid>");
                        sb.append("<productname>" + product.getName() + "</productname>");                       
                        sb.append("</product>");
                        namesAdded = true;
                    }
                }
            }

            if (namesAdded) {
                response.setContentType("text/xml");
                response.setHeader("Cache-Control", "no-cache");
                response.getWriter().write("<products>" + sb.toString() + "</products>");
            } else {
                //nothing to show
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
            }
        }

        if (action.equals("lookup")) {

            response.sendRedirect("hubViewdescription?movieid="+targetId); 
    }
}
catch(SQLException ex)
{

}

}
}
