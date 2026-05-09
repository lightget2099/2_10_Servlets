package org.example;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");
        resp.setCharacterEncoding("utf-8");

        String timezone = req.getParameter("timezone");

        ZoneId zoneId;
        if (timezone == null || timezone.isEmpty()) {
           zoneId = ZoneId.of("UTC");
        } else{
            zoneId = ZoneId.of(timezone.replace(" ", "+"));
        }

        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);

        PrintWriter out = resp.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1> Welcome to TimeServlet </h1>");
        out.println("<p> Current time is: " + formattedTime + " " + zoneId + "</p>");
        out.println("</body></html>");
    }
}
