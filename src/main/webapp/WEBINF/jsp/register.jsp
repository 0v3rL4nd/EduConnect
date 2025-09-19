<%-- register.jsp - Pagina di registrazione --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Registrazione - EduConnect</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="<c:url value='/static/css/main.css'/>" rel="stylesheet">
</head>
<body class="bg-light">
<div class="container">
    <div class="row justify-content-center">
        <div class="col-md-8">
            <div class="card shadow mt-4">
                <div class="card-header bg-success text-white text-center">
                    <h3><i class="fas fa-user-plus"></i> Registrati su EduConnect</h3>
                </div>
                <div class="card-body">
                    <c:if test="${not empty error}">
                    <div class="alert alert-danger">${error}</div>
                    </c:if>

                    <%-- Tab per scegliere tipo registrazione --%>
                    <ul class="nav nav-tabs" id="registrationTabs" role="tablist">
                        <li class="nav-item" role="presentation">
                            <button class="nav-link active" id="student-tab" data-bs-toggle="tab"
                                    data-bs-target="#student" type="button" role="tab">
                                <i class="fas fa-user-graduate"></i> Sono uno Studente
                            </button>
                        </li>
                        <li class="nav-item" role="presentation">
                            <button class="nav-link" id="tutor-tab" data-bs-toggle="tab"
                                    data-bs-target="#tutor" type="button" role="tab">
                                <i class="fas fa-chalkboard-teacher"></i> Sono un Tutor
                            </button>
                        </li>
                    </ul>

                    <div class="tab-content mt-3" id="registrationTabContent">
                        <%-- Registrazione Studente --%>
                        <div class="tab-pane fade show active" id="student" role="tabpanel">
                            <form action="<c:url value='/auth/register/student'/>" method="post">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="studentUsername" class="form-label">Username *</label>
                                            <input type="text" class="form-control" id="studentUsername"
                                                   name="username" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="studentEmail" class="form-label">Email *</label>
                                            <input type="email" class="form-control" id="studentEmail"
                                                   name="email" required>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="studentFirstName" class="form-label">Nome *</label>
                                            <input type="text" class="form-control" id="studentFirstName"
                                                   name="firstName" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="studentLastName" class="form-label">Cognome *</label>
                                            <input type="text" class="form-control" id="studentLastName"
                                                   name="lastName" required>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="studentPassword" class="form-label">Password *</label>
                                            <input type="password" class="form-control" id="studentPassword"
                                                   name="password" minlength="6" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="studentConfirmPassword" class="form-label">Conferma Password *</label>
                                            <input type="password" class="form-control" id="studentConfirmPassword"
                                                   name="confirmPassword" required>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="schoolName" class="form-label">Nome Scuola</label>
                                            <input type="text" class="form-control" id="schoolName" name="schoolName">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="educationLevel" class="form-label">Livello di Istruzione</label>
                                            <select class="form-control" id="educationLevel" name="educationLevel">
                                                <option value="">Seleziona...</option>
                                                <option value="MIDDLE_SCHOOL">Scuola Media</option>
                                                <option value="HIGH_SCHOOL">Scuola Superiore</option>
                                                <option value="BACHELOR">Laurea Triennale</option>
                                                <option value="MASTER">Laurea Magistrale</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <input type="hidden" name="userType" value="STUDENT">

                                <button type="submit" class="btn btn-success w-100">
                                    <i class="fas fa-user-graduate"></i> Registrati come Studente
                                </button>
                            </form>
                        </div>

                        <%-- Registrazione Tutor --%>
                        <div class="tab-pane fade" id="tutor" role="tabpanel">
                            <form action="<c:url value='/auth/register/tutor'/>" method="post">
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="tutorUsername" class="form-label">Username *</label>
                                            <input type="text" class="form-control" id="tutorUsername"
                                                   name="username" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="tutorEmail" class="form-label">Email *</label>
                                            <input type="email" class="form-control" id="tutorEmail"
                                                   name="email" required>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="tutorFirstName" class="form-label">Nome *</label>
                                            <input type="text" class="form-control" id="tutorFirstName"
                                                   name="firstName" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="tutorLastName" class="form-label">Cognome *</label>
                                            <input type="text" class="form-control" id="tutorLastName"
                                                   name="lastName" required>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="tutorPassword" class="form-label">Password *</label>
                                            <input type="password" class="form-control" id="tutorPassword"
                                                   name="password" minlength="6" required>
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="tutorConfirmPassword" class="form-label">Conferma Password *</label>
                                            <input type="password" class="form-control" id="tutorConfirmPassword"
                                                   name="confirmPassword" required>
                                        </div>
                                    </div>
                                </div>

                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="schoolName" class="form-label">Nome Scuola</label>
                                            <input type="text" class="form-control" id="schoolName" name="schoolName">
                                        </div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="mb-3">
                                            <label for="educationLevel" class="form-label">Livello di Istruzione</label>
                                            <select class="form-control" id="educationLevel" name="educationLevel">
                                                <option value="">Seleziona...</option>
                                                <option value="MIDDLE_SCHOOL">Scuola Media</option>
                                                <option value="HIGH_SCHOOL">Scuola Superiore</option>
                                                <option value="BACHELOR">Laurea Triennale</option>
                                                <option value="MASTER">Laurea Magistrale</option>
                                            </select>
                                        </div>
                                    </div>
                                </div>

                                <input type="hidden" name="userType" value="TUTOR">

                                <button type="submit" class="btn btn-success w-100">
                                    <i class="fas fa-chalkboard-teacher"></i> Registrati come Tutor
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>