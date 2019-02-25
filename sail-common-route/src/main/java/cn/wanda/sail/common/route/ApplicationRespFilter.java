/**
 * 
 */
package cn.wanda.sail.common.route;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

/**
 * @Author: DanielCao
 * @Date:   2017年10月29日
 * @Time:   下午8:07:21
 *
 */
public class ApplicationRespFilter extends ZuulFilter {
	private Logger logger = LoggerFactory.getLogger(ApplicationRespFilter.class);
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.netflix.zuul.IZuulFilter#run()
	 */
	@Override
	public Object run() {
		long traceId = System.currentTimeMillis();
		
		RequestContext context = RequestContext.getCurrentContext();
		
		String summary = context.getFilterExecutionSummary().toString();
		logger.info("[" + traceId + "][ZuulResponseSummary:]" + summary);
		
		return "50";
	}

	/* (non-Javadoc)
	 * @see com.netflix.zuul.ZuulFilter#filterType()
	 */
	@Override
	public String filterType() {
		return "post";
	}

	/* (non-Javadoc)
	 * @see com.netflix.zuul.ZuulFilter#filterOrder()
	 */
	@Override
	public int filterOrder() {
		return 1;
	}

}
