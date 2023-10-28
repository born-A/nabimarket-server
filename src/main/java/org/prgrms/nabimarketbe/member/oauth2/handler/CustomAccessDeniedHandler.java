// package org.prgrms.nabimarketbe.member.oauth2.handler;
//
// import java.io.IOException;
//
// import javax.servlet.ServletException;
// import javax.servlet.http.HttpServletRequest;
// import javax.servlet.http.HttpServletResponse;
//
// import org.springframework.security.access.AccessDeniedException;
// import org.springframework.security.web.access.AccessDeniedHandler;
// import org.springframework.stereotype.Component;
//
// @Component
// public class CustomAccessDeniedHandler implements AccessDeniedHandler {
// 	@Override
// 	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws
// 		IOException, ServletException {
// 		String requestURI = request.getRequestURI();
// 		response.sendRedirect("/denied"+"?requestURI="+requestURI);
// 	}
// }
