package com.clvspk.photo.config.resolver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.clvspk.photo.exception.BusinessException;
import com.clvspk.photo.response.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Writer;

/**
 * Exception Qualify
 *
 * @author Mr.C
 * @date 2018-6-26 11:00
 */
@Component
public class ResponseExceptionResolver extends SimpleMappingExceptionResolver {

    @Override
    @ResponseBody
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON);
        response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        Writer writer = null;
        try {
            Result result;
            if (ex instanceof BusinessException) {
                BusinessException businessException = (BusinessException) ex;
                result = new Result(businessException.getCode(), businessException.getMessage());
            } else {
                result = new Result(getStatus(ex), Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase());
            }
            writer = response.getWriter();
            writer.write(JSON.toJSONString(result, SerializerFeature.WriteMapNullValue));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return new ModelAndView();
    }

    private int getStatus(Throwable ex) {
        printEx(ex);
        if (ex instanceof WebApplicationException) {
            return ((WebApplicationException) ex).getResponse().getStatus();
        } else {
            return Response.Status.INTERNAL_SERVER_ERROR.getStatusCode();
        }
    }

    private void printEx(Throwable ex) {
        ex.printStackTrace();
    }
}
