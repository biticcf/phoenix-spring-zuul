package cn.wanda.sail.common.route;

import cn.wanda.sail.common.route.Constants;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: DanielCao
 * @Date:   2017年10月29日
 * @Time:   下午8:07:21
 *
 */
public class ApplicationZuulFilter extends ZuulFilter {

    private Logger logger = LoggerFactory.getLogger(ApplicationZuulFilter.class);

    public static final List<String> merchantServerRules = new ArrayList<String>();
    public static final List<String> memberServerRules = new ArrayList<String>();
    public static final List<String> marketingServerRules = new ArrayList<String>();
    public static final List<String> pointServerRules = new ArrayList<String>();

    static {
        merchantServerRules.add("merchant");
        merchantServerRules.add("images");
        merchantServerRules.add("basic");
        merchantServerRules.add("image");

//        memberServerRules.add("point");
        memberServerRules.add("member");
//        memberServerRules.add("point");
        memberServerRules.add("gifts");
        memberServerRules.add("gift");

        marketingServerRules.add("marketing");
        
//        pointServerRules.add("pointtest");
        
        pointServerRules.add("point");

    }

    @Value("${spring.cloud.deploy.mode}")
    String deplpyMode;

    @Value("${tenant.group}")
    String tenantGroupDefault;
    
    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
    	long tracrId = System.currentTimeMillis();
    	logger.info("start[" + tracrId + "]...");
    	
        RequestContext context = RequestContext.getCurrentContext();
        String serverName = null;
        context.setSendZuulResponse(true);
        try {
            serverName = checkServiceName(context);
            if (StringUtils.isEmpty(serverName)) {
                context.setSendZuulResponse(false);
                context.setResponseStatusCode(402);
                return null;
            }
            StringBuilder routeName = new StringBuilder();
            String tenantGroup = (String) context.get(Constants.TENANT_GROUP);
            if (StringUtils.isNotEmpty(tenantGroup)) {
                routeName.append(tenantGroup).append("-").append(serverName);
                logger.info("select route name:{}", routeName.toString());
                context.set("serviceId", routeName.toString());
                return null;
            }
            if (deplpyMode.equals("private")) {
                if (StringUtils.isBlank(tenantGroupDefault)) {
                    routeName.append(serverName);
                } else {
                	
                	if(tenantGroupDefault.endsWith("-") == false)
                        routeName.append(tenantGroupDefault).append("-").append(serverName);
                	else
                		routeName.append(tenantGroupDefault).append(serverName);
                }
                logger.info("select route name:{}", routeName.toString());
                context.set("serviceId", routeName.toString());
                routeSwaggerApi(context);
                return null;
            }
            
            String serviceId = getServiceId(serverName, context);
            context.set("serviceId", serviceId);
            
            routeSwaggerApi(context);
        } catch (Exception ex) {
            context.set("error.status_code", HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            context.set("error.exception", ex);
            context.setSendZuulResponse(false);
            logger.error(ex.getMessage(), ex);
        }finally{
        	long t = System.currentTimeMillis();
        	logger.info("start[" + serverName + "][" + tracrId + "][" + (t - tracrId) + "ms]...");
        }
        return null;
    }

    private void routeSwaggerApi(RequestContext context) {
        String requestURI = (String) context.get(Constants.REQUEST_URI);
        if ((requestURI.contains(Constants.SWAGGER) || requestURI.contains(Constants.API))
                && !requestURI.contains(Constants.SAIL_MARKETING)) {
            if (requestURI.contains(Constants.SAIL_MERCHANT))
                requestURI = requestURI.replaceAll(Constants.SAIL_MERCHANT_PATH, Constants.EMPTY);
            if (requestURI.contains(Constants.SAIL_MEMBER))
                requestURI = requestURI.replaceAll(Constants.SAIL_MEMBER_PATH, Constants.EMPTY);
            if(requestURI.contains(Constants.SAIL_POINT))
				 requestURI = requestURI.replaceAll(Constants.SAIL_POINT_PATH,Constants.EMPTY);
            context.set(Constants.REQUEST_URI, requestURI);
        }
    }
    
    @Override
    public String filterType() {
        return "route";
    }

    @Override
    public int filterOrder() {
        return 9;
    }

    private String checkServiceName(RequestContext context) {
        String requestURI = (String) context.get(Constants.REQUEST_URI);
        String serverName = "";

        for (String rule : marketingServerRules) {
            if (requestURI.contains(rule)) {
                serverName = Constants.MARKETING_APP_NAME;
                break;
            }
        }

        for (String rule : memberServerRules) {
            if (requestURI.contains(rule)) {
                serverName = Constants.MEMBER_APP_NAME;
                break;
            }
        }

        for (String rule : merchantServerRules) {
            if (requestURI.contains(rule)) {
                serverName = Constants.MERCHANT_APP_NAME;
                break;
            }
        }
        
        for (String rule : pointServerRules) {
            if (requestURI.contains("/"+rule+"/")) {
                serverName = Constants.POINT_APP_NAME;
                break;
            }
        }
        
        return serverName;
    }

    private String getServiceId(String serverName, RequestContext context) {
        StringBuilder routeName = new StringBuilder();
        routeName.append(serverName);
        logger.info("route get group-server is:{}", routeName.toString());
        
        return routeName.toString();
    }
    
}
