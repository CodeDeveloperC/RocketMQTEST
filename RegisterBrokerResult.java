package com.example.test.arraylistrocketmq;

import lombok.Data;

/**
 * @author Chen
 * @version 1.0
 * @date 2020/7/20 9:21
 * @description:
 */
@Data
public class RegisterBrokerResult {
    private String haServerAddr;
    private String masterAddr;
}
