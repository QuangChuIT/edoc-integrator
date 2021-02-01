package com.bkav.edoc.util;

import com.bkav.edoc.service.commonutil.Checker;
import com.bkav.edoc.service.kernel.util.Validator;
import com.bkav.edoc.service.xml.base.header.CheckPermission;
import com.bkav.edoc.service.xml.base.header.Error;
import com.bkav.edoc.service.xml.base.header.Report;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

public class ProcessRequestUtil {
    private final static Checker CHECKER = new Checker();

    public static Map<String, String> getHeaders(HttpServletRequest request) {
        Enumeration<String> headers = request.getHeaderNames();
        Map<String, String> map = new HashMap<>();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement();
            String value = request.getHeader(key);
            map.put(key, value);
        }
        return map;
    }

    public static List<Error> validateHeader(Map<String, String> header) {
        List<Error> errors = new ArrayList<>();
        if (header.size() <= 0) {
            errors.add(new Error("H.OrganId", "OrganId must add to header !!!"));
            errors.add(new Error("H.Token", "Token must add to header !!!"));
        } else {
            String organId = header.get(EdocServiceConstant.ORGAN_ID);
            String token = header.get(EdocServiceConstant.TOKEN);
            if (Validator.isNullOrEmpty(organId)) {
                errors.add(new Error("H.OrganId", "OrganId must init !!!"));
            }
            if (Validator.isNullOrEmpty(token)) {
                errors.add(new Error("H.Token", "Token must init !!!"));
            }
        }
        if (errors.size() == 0) {
            String organId = header.get(EdocServiceConstant.ORGAN_ID);
            String token = header.get(EdocServiceConstant.TOKEN);
            CheckPermission checkPermission = new CheckPermission();
            checkPermission.setOrganId(organId);
            checkPermission.setToken(token);
            Report permission = CHECKER.checkPermission(checkPermission);
            if (!permission.isIsSuccess()) {
                errors.add(new Error("Permission", "Organ don't have permission on EdocService !!!"));
            }
        }
        return errors;
    }
}
