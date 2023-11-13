package com.shopme.admin.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shopme.admin.service.SettingService;
import com.shopme.common.entity.Setting;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class SettingFilter implements Filter {
	
	@Autowired
	private SettingService service;
	
	@Autowired
	HikariDataSource dataSource;
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

	HttpServletRequest servletRequest = (HttpServletRequest) request;
		String url = servletRequest.getRequestURL().toString();
		
		if (url.endsWith(".css") || url.endsWith(".js") || url.endsWith(".png") || url.endsWith(".jpg")) {
		chain.doFilter(request, response);
			return ;
		}

		List<Setting> generalSettings = service.getListGeneralSettings();
		
		generalSettings.forEach(setting -> {
			request.setAttribute(setting.getKey(), setting.getValue());
		});

		chain.doFilter(request, response);	
	}
	
	public void destroy() {
		dataSource.close();
	}
}
