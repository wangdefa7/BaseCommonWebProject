package com.web.core;

import java.util.List;

public class CommonResult {

    public int total;

    public List list;

    public CommonResult() {

    }

    public CommonResult(int total, List list) {
        this.total = total;
        this.list = list;
    }
}
