package org.example;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;


import org.thymeleaf.context.Context;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;
    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();
        JakartaServletWebApplication jswa =
                JakartaServletWebApplication.buildApplication(this.getServletContext());

        WebApplicationTemplateResolver resolver =
                new WebApplicationTemplateResolver(jswa);
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=utf-8");

        String timezoneParam = req.getParameter("timezone");
        String finalTimezone;

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            finalTimezone = timezoneParam.replace(" ", "+");

            Cookie lastTimezone = new Cookie("lastTimezone", finalTimezone);
            lastTimezone.setHttpOnly(true);
            lastTimezone.setMaxAge(24 * 60 * 60);
            resp.addCookie(lastTimezone);
        }else {
            String cookieTimezone = null;

            Cookie[] cookies = req.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lastTimezone".equals(cookie.getName())) {
                        cookieTimezone = cookie.getValue();
                        break;
                    }
                }
            }
            finalTimezone =  (cookieTimezone != null) ? cookieTimezone : "UTC";
        }


        ZoneId zoneId = ZoneId.of(finalTimezone);

        ZonedDateTime now = ZonedDateTime.now(zoneId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = now.format(formatter);

        Context context = new Context();
        context.setVariable("formattedTime", formattedTime);
        context.setVariable("timezone", zoneId.toString());

        engine.process("time", context, resp.getWriter());

    }
}
