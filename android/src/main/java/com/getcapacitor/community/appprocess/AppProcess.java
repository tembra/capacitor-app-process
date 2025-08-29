package com.getcapacitor.community.appprocess;

import com.getcapacitor.Logger;

public class AppProcess {

    public String echo(String value) {
        Logger.info("Echo", value);
        return value;
    }
}
