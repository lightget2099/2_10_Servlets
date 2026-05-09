package org.example;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;

@WebFilter(value = "/time")
public class TimezoneValidateFilter extends HttpFilter {
    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String timezone = request.getParameter("timezone");
        ZoneId zoneId;

        try {
            if (timezone == null || timezone.isEmpty()){
                zoneId = ZoneId.of("UTC");
            } else{
                zoneId = ZoneId.of(timezone.replace(" ", "+"));
            }
            chain.doFilter(request,response);

        } catch(Exception e){
            response.sendError(400, "Invalid timezone");
        }
    }
}
