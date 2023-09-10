package com.ogive.oheo.filter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FreeBeeFilter implements Filter {

	@Value("${application.free.bee.allowed}")
	private String isFreeBeeAllowed;

	@Value("${application.free.bee.allowed.maxdate}")
	private String isFreeBeeMaxDate;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		Date currentDate = new Date();
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		if (!"Y".equalsIgnoreCase(isFreeBeeAllowed)) {

			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
			try {
				Date maxAllowedDate = simpleDateFormat.parse(isFreeBeeMaxDate);
				if (currentDate.after(maxAllowedDate)) {
					//res.setStatus(400);
					res.setContentType("text/html;charset=UTF-8");
					res.getOutputStream().write(
							"You are no longer allowed to use this service. Your free service uses has been expired and you need to pay the pending cost in order to start using service."
									.getBytes());
					return;
				}

			} catch (ParseException e) {
				e.printStackTrace();
			}

		}
		chain.doFilter(req, res);
	}

}
