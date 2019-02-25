/**
 * 
 */
package cn.wanda.sail.common.route;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.pre.FormBodyWrapperFilter;

import com.netflix.zuul.context.RequestContext;

/**
 * @Author: DanielCao
 * @Date:   2017年10月30日
 * @Time:   上午11:41:01
 * 文件上传filter
 */
public class AppliccationPostImagesFilter extends FormBodyWrapperFilter {
	private Logger logger = LoggerFactory.getLogger(AppliccationPostImagesFilter.class);
	
	@Override
	public Object run() {
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		String contentType = request.getContentType();
		String method = request.getMethod();
		
		logger.info("[ContentType:]" + contentType + ",[RequestMethod:]" + method);
		
		if (contentType == null || contentType.trim().equals("")) {
			return "0";
		}
		
		if(contentType.toLowerCase().indexOf("multipart/form-data") >= 0){
			logger.info("Into super!");
			return super.run();
		}
		
		return "0";
	}

	@Override
	public int filterOrder() {
		return -1; //数字越大，优先级越低
	}
}
