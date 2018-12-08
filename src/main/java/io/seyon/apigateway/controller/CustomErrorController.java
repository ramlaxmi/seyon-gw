package io.seyon.apigateway.controller;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class CustomErrorController implements ErrorController {

	private static final String PATH = "/error";

	@Override
	public String getErrorPath() {
		return PATH;
	}

	@RequestMapping("/error")
	public ModelAndView handleError(HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("error");
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

		if (status != null) {
			Integer statusCode = Integer.valueOf(status.toString());
			mav.addObject("errorCode", statusCode);
			switch (statusCode) {
			case 400: {
				mav.addObject("error", "Bad Request");
			}
			case 401: {
				mav.addObject("error", "Unauthorized");
			}
			case 404: {
				mav.addObject("error", "Resource not found");
			}
			case 500: {
				mav.addObject("error", "Internal Server Error");
			}
			default: {
				mav.addObject("error", "Service Unavailable");
			}
			}

		}

		return mav;
	}

}
