package com.piattaforme.educonnect.business.webservice;

import com.piattaforme.educonnect.persistence.entity.Lesson;
import com.piattaforme.educonnect.persistence.entity.Booking;
import com.piattaforme.educonnect.business.dto.CalendarEventDTO;
import com.piattaforme.educonnect.business.ejb.BookingSessionBean;
import com.piattaforme.educonnect.persistence.entity.Tutor;
import com.piattaforme.educonnect.persistence.repository.TutorRepository;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebService(name = "CalendarService",
        targetNamespace = "http://webservice.educonnect.com/",
        serviceName = "CalendarWebService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@Stateless
public class CalendarWebService {

    @Inject
    private BookingSessionBean bookingSessionBean;
    private TutorRepository tutorRepository;

    /**
     * Sincronizza eventi calendario per studente
     */
    @WebMethod(operationName = "getStudentCalendarEvents")
    @WebResult(name = "calendarEvents")
    public List<CalendarEventDTO> getStudentCalendarEvents(
            @WebParam(name = "studentId") Long studentId,
            @WebParam(name = "startDate") String startDate,
            @WebParam(name = "endDate") String endDate) {

        List<Booking> bookings = bookingSessionBean.getBookingsForStudent(studentId);

        return bookings.stream()
                .filter(booking -> {
                    LocalDateTime lessonStart = booking.getLesson().getStartTime();
                    LocalDateTime start = LocalDateTime.parse(startDate);
                    LocalDateTime end = LocalDateTime.parse(endDate);
                    return !lessonStart.isBefore(start) && !lessonStart.isAfter(end);
                })
                .map(this::convertToCalendarEvent)
                .collect(Collectors.toList());
    }

    /**
     * Sincronizza eventi calendario per tutor
     */
    @WebMethod(operationName = "getTutorCalendarEvents")
    @WebResult(name = "calendarEvents")
    public List<CalendarEventDTO> getTutorCalendarEvents(
            @WebParam(name = "tutorId") Long tutorId,
            @WebParam(name = "startDate") String startDate,
            @WebParam(name = "endDate") String endDate) {

        Optional<Tutor> tutor =  tutorRepository.findById(tutorId);
        List<Booking> bookings = bookingSessionBean.getBookingsForTutor(tutor);

        return bookings.stream()
                .filter(booking -> {
                    LocalDateTime lessonStart = booking.getLesson().getStartTime();
                    LocalDateTime start = LocalDateTime.parse(startDate);
                    LocalDateTime end = LocalDateTime.parse(endDate);
                    return !lessonStart.isBefore(start) && !lessonStart.isAfter(end);
                })
                .map(this::convertToCalendarEvent)
                .collect(Collectors.toList());
    }

    /**
     * Crea evento calendario da prenotazione
     */
    @WebMethod(operationName = "createCalendarEvent")
    @WebResult(name = "eventCreated")
    public boolean createCalendarEvent(
            @WebParam(name = "bookingId") Long bookingId,
            @WebParam(name = "externalCalendarId") String externalCalendarId) {

        try {
            // Logica per creare evento su calendario esterno (Google Calendar, Outlook, etc.)
            // Qui simuleremmo l'integrazione con API esterne
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private CalendarEventDTO convertToCalendarEvent(Booking booking) {
        Lesson lesson = booking.getLesson();

        CalendarEventDTO event = new CalendarEventDTO();
        event.setId(booking.getId());
        event.setTitle("Lezione di " + lesson.getSubject().getName());
        event.setStartTime(lesson.getStartTime());
        event.setEndTime(lesson.getEndTime());
        event.setLocation(lesson.getLocation());
        event.setDescription(String.format("Lezione con %s - %s",
                lesson.getTutor().getFullName(),
                lesson.getSubject().getName()));
        event.setStatus(booking.getStatus().name());

        return event;
    }
}