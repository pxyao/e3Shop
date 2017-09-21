package cn.e3mall.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;

/**
 * 全局异常处理
 * @author 庞湘耀
 *
 */
public class GlobalExceptionResolver implements HandlerExceptionResolver{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object object,
			Exception exception) {
		//打印控制台
		exception.printStackTrace();
		//写日志
		LOGGER.debug("测试输出的日志");
		LOGGER.info("系统发生异常");
		LOGGER.error("系统发生异常",exception);
		//发邮件 发短信
		//jamll工具包
		//显示错误页面
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
