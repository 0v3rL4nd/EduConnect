package com.piattaforme.educonnect.presentation.servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet(name = "FileUploadServlet", urlPatterns = {"/upload/*"})
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,  // 10 MB
        maxRequestSize = 1024 * 1024 * 50 // 50 MB
)
public class FileUploadServlet extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";
    private static final String[] ALLOWED_IMAGE_TYPES = {"image/jpeg", "image/png", "image/gif"};
    private static final String[] ALLOWED_DOC_TYPES = {"application/pdf", "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"};

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Controlla autenticazione
        if (request.getSession().getAttribute("userId") == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Non autenticato");
            return;
        }

        String uploadType = request.getPathInfo();
        if (uploadType == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo upload non specificato");
            return;
        }

        try {
            // Crea directory upload se non esiste
            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Gestisci upload basato sul tipo
            switch (uploadType) {
                case "/profile-image":
                    handleProfileImageUpload(request, response, uploadPath);
                    break;
                case "/document":
                    handleDocumentUpload(request, response, uploadPath);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo upload non supportato");
            }

        } catch (Exception e) {
            log("Error in file upload", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Errore nel caricamento file");
        }
    }

    private void handleProfileImageUpload(HttpServletRequest request, HttpServletResponse response, String uploadPath)
            throws ServletException, IOException {

        Part filePart = request.getPart("profileImage");
        if (filePart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nessun file selezionato");
            return;
        }

        String contentType = filePart.getContentType();
        if (!isAllowedImageType(contentType)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo file non supportato. Usa JPG, PNG o GIF.");
            return;
        }

        // Genera nome file unico
        String originalFileName = getFileName(filePart);
        String extension = getFileExtension(originalFileName);
        String newFileName = UUID.randomUUID().toString() + extension;

        // Salva file
        String filePath = uploadPath + File.separator + "profiles" + File.separator + newFileName;
        File profileDir = new File(uploadPath + File.separator + "profiles");
        if (!profileDir.exists()) {
            profileDir.mkdirs();
        }

        filePart.write(filePath);

        // Restituisci URL del file
        String fileUrl = request.getContextPath() + "/uploads/profiles/" + newFileName;
        response.setContentType("application/json");
        response.getWriter().write("{\"success\": true, \"fileUrl\": \"" + fileUrl + "\"}");
    }

    private void handleDocumentUpload(HttpServletRequest request, HttpServletResponse response, String uploadPath)
            throws ServletException, IOException {

        Part filePart = request.getPart("document");
        if (filePart == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Nessun file selezionato");
            return;
        }

        String contentType = filePart.getContentType();
        if (!isAllowedDocType(contentType)) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Tipo file non supportato. Usa PDF o DOC.");
            return;
        }

        // Genera nome file unico
        String originalFileName = getFileName(filePart);
        String extension = getFileExtension(originalFileName);
        String newFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // Salva file
        String filePath = uploadPath + File.separator + "documents" + File.separator + newFileName;
        File docDir = new File(uploadPath + File.separator + "documents");
        if (!docDir.exists()) {
            docDir.mkdirs();
        }

        filePart.write(filePath);

        // Restituisci URL del file
        String fileUrl = request.getContextPath() + "/uploads/documents/" + newFileName;
        response.setContentType("application/json");
        response.getWriter().write("{\"success\": true, \"fileUrl\": \"" + fileUrl + "\", \"fileName\": \"" + originalFileName + "\"}");
    }

    private boolean isAllowedImageType(String contentType) {
        if (contentType == null) return false;
        for (String allowedType : ALLOWED_IMAGE_TYPES) {
            if (allowedType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAllowedDocType(String contentType) {
        if (contentType == null) return false;
        for (String allowedType : ALLOWED_DOC_TYPES) {
            if (allowedType.equals(contentType)) {
                return true;
            }
        }
        return false;
    }

    private String getFileName(Part part) {
        String contentDisposition = part.getHeader("content-disposition");
        for (String content : contentDisposition.split(";")) {
            if (content.trim().startsWith("filename")) {
                return content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return "unknown";
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf);
    }
}