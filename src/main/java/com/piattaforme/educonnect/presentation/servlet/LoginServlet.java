package com.piattaforme.educonnect.presentation.servlet;


import com.piattaforme.educonnect.business.service.UserService;
import com.piattaforme.educonnect.persistence.entity.User;
import com.piattaforme.educonnect.persistence.entity.Student;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Optional;

@WebServlet(name = "LoginServlet", urlPatterns = {"/servlet/login"})
public class LoginServlet extends HttpServlet {

    @Autowired
    private UserService userService;

    @Override
    public void init() throws ServletException {
        // In un contesto Spring, userService sarebbe iniettato
        // Qui simuleremmo il lookup dal contesto Spring
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Forward alla pagina di login JSP
        request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Username e password sono obbligatori");
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
            return;
        }

        try {
            Optional<User> userOpt = userService.authenticateUser(username, password);

            if (userOpt.isPresent()) {
                User user = userOpt.get();

                // Crea sessione
                HttpSession session = request.getSession();
                session.setAttribute("currentUser", user);
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("userType", user.getClass().getSimpleName().toUpperCase());

                // Log dell'accesso
                log("User logged in: " + username + " [" + user.getClass().getSimpleName() + "]");

                // Redirect basato sul tipo utente
                String redirectUrl;
                if (user instanceof Student) {
                    redirectUrl = request.getContextPath() + "/student/dashboard";
                } else if (user instanceof Tutor) {
                    redirectUrl = request.getContextPath() + "/tutor/dashboard";
                } else {
                    redirectUrl = request.getContextPath() + "/admin/dashboard";
                }

                response.sendRedirect(redirectUrl);

            } else {
                request.setAttribute("error", "Username o password non corretti");
                request.setAttribute("username", username); // Mantieni username per UX
                request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
            }

        } catch (Exception e) {
            log("Error during login for user: " + username, e);
            request.setAttribute("error", "Errore del sistema. Riprova più tardi.");
            request.getRequestDispatcher("/WEB-INF/jsp/auth/login.jsp").forward(request, response);
        }
    }
}