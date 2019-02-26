package cn.wanda.sail.common.route;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Controller;

import com.ffan.soa.platform.common.config.AdminConfiguration;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @Author: DanielCao
 * @Date:   2017年10月29日
 * @Time:   下午8:07:21
 *
 */
@Import({AdminConfiguration.class})
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Controller
@EnableZuulProxy
@EnableFeignClients(basePackages = {"cn.wanda.sail.common.route"})
public class SmartRouteApplication {
	
	private static final String API = "/api";
	private static final String SWAGGER = "swagger";
	private static final String REQUEST_URI = "requestURI";
	private static final String EMPTY = "";

	private static final String SAIL_MEMBER = "member";
	private static final String SAIL_MERCHANT = "merchant";
	private static final String SAIL_MARKETING = "marketing";
	private static final String SAIL_MEMBER_PATH = "sail/member/";
	private static final String SAIL_MERCHANT_PATH = "sail/merchant/";
	
    public static void main(String[] args) {
        SpringApplication.run(SmartRouteApplication.class, args);
    }
    
    @Bean
    public ApplicationZuulFilter swaggerZuulFilter(){
    	return new ApplicationZuulFilter();
    
    }
    
    @Bean
    public ApplicationRespFilter respFilter(){
    	return new ApplicationRespFilter();
    }
    
    @Bean
    public AppliccationPostImagesFilter fileUploadFilter(){
    	return new AppliccationPostImagesFilter();
    }
    
    class SwaggerZuulFilter extends ZuulFilter {

		@Override
		public boolean shouldFilter() {
			return true;
		}

		@Override
		public Object run() {
			RequestContext context = RequestContext.getCurrentContext();
			ProxyRequestHelper proxyRequestHelper = new ProxyRequestHelper();
			proxyRequestHelper.addIgnoredHeaders();
			try {
				String requestURI = (String)context.get(REQUEST_URI);
				 if((requestURI.contains(SWAGGER) || requestURI.contains(API)) && !requestURI.contains(SAIL_MARKETING)){
					 if(requestURI.contains(SAIL_MERCHANT))
						 requestURI = requestURI.replaceAll(SAIL_MERCHANT_PATH,EMPTY);
					 if(requestURI.contains(SAIL_MEMBER))
						 requestURI = requestURI.replaceAll(SAIL_MEMBER_PATH,EMPTY);
					 context.set(REQUEST_URI, requestURI);
	    	      }
			}
			catch (Exception ex) {
				context.set("error.status_code",HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				context.set("error.exception", ex);
			}
			return null;
		}

		@Override
		public String filterType() {
			return "route";
		}

		@Override
		public int filterOrder() {
			return 9;
		}
    	
    }
}
